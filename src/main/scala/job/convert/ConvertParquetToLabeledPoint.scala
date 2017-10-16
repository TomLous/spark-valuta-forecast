package job.convert


import dataset.{ForexDataset, ForexLabeledPointDataset}
import model.ForexRecord
import org.apache.spark.ml.feature.LabeledPoint
import org.apache.spark.ml.linalg.Vectors
import util.{AppConfig, SparkJob}
import org.apache.spark.sql._
import org.apache.spark.sql.functions._

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ConvertParquetToLabeledPoint extends App with SparkJob with AppConfig {

  import spark.implicits._

  val postFix = args.headOption.map(p => "-" + p).getOrElse("")

  val path = forexParquetPath + postFix

  println(s"Converting ${forexParquet + postFix} to LabeledPoints in ${targetDir + forexParquet + postFix}.libsvm, using windows of $windowSize minutes, sliding every $windowSlide minute(s)")

  val forexDataset:Dataset[ForexRecord] = ForexDataset.loadParquet(path).cache()

  val labeledPointDataset = ForexLabeledPointDataset.convert(forexDataset, mlTargetField, windowSize, windowSlide)

  labeledPointDataset
    .write
    //    .format("libsvm") //@todo implement after bugfix in Spark
    .mode(SaveMode.Overwrite)
    .save(targetDir + forexParquet + postFix + ".libsvm")


  println(s"Wrote ${labeledPointDataset.count} LabeledPoints based on ${forexDataset.count} records")


}
