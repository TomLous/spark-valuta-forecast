import Preview.forexFilePath
import dataset.ForexDataset
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ConvertCSV2Parquet extends App with SparkJob with AppConfig {

  import spark.implicits._

  ForexDataset
    .loadCSV(forexFilePath)
    .write
//    .partitionBy("year","month")
    .mode(SaveMode.Overwrite)
    .save("src/main/resources/" + config.getString("forexParquet"))



}
