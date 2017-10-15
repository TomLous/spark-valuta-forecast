import Preview.forexFilePath
import dataset.ForexDataset
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ConvertCSVToParquet extends App with SparkJob with AppConfig {

  import spark.implicits._

  val forexDataset = ForexDataset
    .loadCSV(forexFilePath)
    .cache()

    forexDataset.write
//    .partitionBy("year","month")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/" + config.getString("forexParquet"))

  forexDataset
      .filter('timestamp.gt(lit("2017-01-01")))
    .write
    //    .partitionBy("year","month")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/" + config.getString("forexParquet") + "-2017")



}
