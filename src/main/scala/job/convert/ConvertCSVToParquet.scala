package job.convert

import dataset.ForexDataset
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ConvertCSVToParquet extends App with SparkJob with AppConfig {

  import spark.implicits._

  val yearValue = args.headOption
  val postFix = yearValue.map(p => "-" + p).getOrElse("")

  println(s"Converting $forexFile to ${targetDir + forexParquet + postFix}, filtered by: $yearValue")

  val forexDataset = ForexDataset
    .loadCSV(forexFilePath)

  val forexDataset2 = yearValue match {
    case Some(yr) => forexDataset.filter(year('timestamp) === lit(yr))
    case None => forexDataset
  }

  forexDataset2.write
    //    .partitionBy("year","month")
    .mode(SaveMode.Overwrite)
    .save(targetDir + forexParquet + postFix)

  println(s"Wrote ${forexDataset2.count} records")


}
