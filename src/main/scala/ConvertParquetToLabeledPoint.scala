
import dataset.ForexDataset
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.ml.regression.LinearRegression
import util.{AppConfig, SparkJob}
import org.apache.spark.sql._
import org.apache.spark.sql.expressions.Window
import org.apache.spark.sql.functions._

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ConvertParquetToLabeledPoint extends App with SparkJob with AppConfig {

  import spark.implicits._

  val path = forexParquetPath + "-2017"

  val forexDataset = ForexDataset.loadParquet(path).cache()


//  val w = Window.orderBy('timestamp)

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
    .withColumn("numFeatures", lit(windowSize))
    .write
//    .format("libsvm") //@todo implement after bugfix in Spark
    .mode(SaveMode.Overwrite)
    .save(path + ".libsvm")



}
