package dataset

import model.{ForexRecord, ForexRecordRaw}
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}
import org.apache.spark.sql.functions._

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ForexDataset {

  type ForexDataset = Dataset[ForexRecord]

  def loadParquet(inputPath: String)(implicit spark: SparkSession) = {
    import spark.implicits._

    spark.read.parquet(inputPath).as[ForexRecord]
  }

  def loadCSV(inputPath: String)(implicit spark: SparkSession) = {

    import spark.implicits._

    val schema = Encoders.product[ForexRecordRaw].schema

    spark
      .read

      .schema(schema)

      .option("header", true)
      .option("sep", ",")
      .option("ignoreLeadingWhiteSpace", true)
      .option("ignoreTrailingWhiteSpace", true)
      .option("nullValue", "")
      .option("quote", "")

      .option("mode", "FAILFAST")
//      .option("mode", "DROPMALFORMED")

      .csv(inputPath)

      // rename crazy ass headers
      .withColumnRenamed("<TICKER>", "ticker")
      .withColumnRenamed("<DTYYYYMMDD>", "date")
      .withColumnRenamed("<TIME>", "time")
      .withColumnRenamed("<OPEN>", "open")
      .withColumnRenamed("<HIGH>", "high")
      .withColumnRenamed("<LOW>", "low")
      .withColumnRenamed("<CLOSE>", "close")
      .withColumnRenamed("<VOL>", "vol")
      .as[ForexRecordRaw]

      // restructure to ForestRecord
      .withColumn("timestamp", to_timestamp(concat_ws(" ", 'date, 'time), "yyyyMMdd HHmmss"))
      .drop("date", "time")

      .as[ForexRecord]

  }
}
