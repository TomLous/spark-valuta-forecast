package dataset


import model.ForexRecord
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.{Dataset, SparkSession}
import org.apache.spark.sql.functions.{col, collect_list, size, window}

/**
  * Created by Tom Lous on 16/10/2017.
  * Copyright © 2017 Datlinq B.V..
  */
object ForexLabeledPointDataset {

  type ForexLabeledPointDataset = Dataset[LabeledPoint]


  def convert(forexDataset: Dataset[ForexRecord], mlTargetField:String, windowSize: Int, windowSlide: Int)(implicit spark: SparkSession) = {
    assert(windowSize > 1 && windowSize < 100)
    assert(windowSlide > 0)

    import spark.implicits._

    forexDataset
      .select('timestamp, col(mlTargetField).as("target"))
      .groupBy(
        window('timestamp, s"$windowSize minutes", s"$windowSlide minute")
      )
      .agg(collect_list("target").as("featureVector"))
      .filter(size('featureVector) === windowSize)
      .drop("window")
      .as[Array[Double]]
      .map(arr => LabeledPoint(arr.last, Vectors.dense(arr.init)))

  }
}
