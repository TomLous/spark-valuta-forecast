package util

import org.apache.spark.sql.SparkSession

/**
  * Created by Tom Lous on 15/10/2017.
  */
trait SparkJob {

  implicit val spark = SparkSession
    .builder()
    .appName(this.getClass.getSimpleName)
    .getOrCreate()
  //    .config("parquet.enable.summary-metadata", "false")
  //    .config("spark.sql.parquet.mergeSchema", "false")
}

