package job.ml

import dataset.{ForexDataset, ForexLabeledPointDataset}
import model.{ForexRecord, ForexRecordRaw}
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import org.apache.spark.sql.Encoders
import org.apache.spark.sql.functions.{col, collect_list, size, window}
import org.apache.spark.streaming.{Seconds, StreamingContext}
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 16/10/2017.
  */
object StreamingLinearRegressionPredict  extends App with SparkJob with AppConfig {

  import spark.implicits._

  val forexStream = ForexDataset.loadSocketStream()

//  val forexLabeledPointStream = ForexLabeledPointDataset.convert(forexStream, mlTargetField, windowSize, windowSlide)

  val forexLabeledPointStream = forexStream
    .select('timestamp, col(mlTargetField).as("target"))
    .groupBy(
      window('timestamp, s"$windowSize minutes", s"$windowSlide minute")
    )
    .agg(collect_list("target").as("featureVector"))
    .withColumn("start", 'window.getItem("start"))
    .withColumn("end", 'window.getItem("end"))
    .filter(size('featureVector) === windowSize)



  forexLabeledPointStream.printSchema()


  val query = forexLabeledPointStream.writeStream
    .outputMode("update")
    .format("console")
    .start()

  query.awaitTermination()
//  val x:Int = lines
////  lines.as[ForexRecordRaw].map()
//
//  ssc.start()             // Start the computation
//  ssc.awaitTermination()


  //  val stream = spark.readStream.parquet(path).as[ForexRecord]
//
// stream
//   .select('timestamp, col(mlTargetField).as("target"))
//   .groupBy(
//     window('timestamp, s"$windowSize minutes", s"$windowSlide minute")
//   )
//   .agg(collect_list("target").as("featureVector"))
//   .filter(size('featureVector) === windowSize)
//   .drop("window")
//   .as[Array[Double]]
//   .map(arr => LabeledPoint(arr.last, Vectors.dense(arr.init)))






}
