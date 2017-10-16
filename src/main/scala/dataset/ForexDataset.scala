package dataset

import model.{ForexRecord, ForexRecordRaw}
import org.apache.spark.sql.{DataFrame, Dataset, Encoders, SparkSession}
import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.{DoubleType, LongType, IntegerType}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ForexDataset {

  type ForexDataset = Dataset[ForexRecord]

  def loadParquet(inputPath: String)(implicit spark: SparkSession) = {
    import spark.implicits._

    spark.read.parquet(inputPath).as[ForexRecord]
  }

  def loadSocketStream(host: String="localhost", port: Long=9999)(implicit spark: SparkSession) = {
    import spark.implicits._

    val df = spark.readStream
      .format("socket")
      .option("host", host)
      .option("port", port)


      .load()
      .withColumn("tmp", split('value, ","))
      .filter(size('tmp) === lit(8))
      .select(
        'tmp.getItem(0).as("<TICKER>"),
        'tmp.getItem(1).as("<DTYYYYMMDD>"),
        'tmp.getItem(2).as("<TIME>"),
        'tmp.getItem(3).cast(DoubleType).as("<OPEN>"),
        'tmp.getItem(4).cast(DoubleType).as("<HIGH>"),
        'tmp.getItem(5).cast(DoubleType).as("<LOW>"),
        'tmp.getItem(6).cast(DoubleType).as("<CLOSE>"),
        'tmp.getItem(7).cast(IntegerType).as("<VOL>")
      )
      .drop("tmp")
      .filter($"<VOL>".isNotNull)


    dataframeToDataset(df)

  }


  def loadCSV(inputPath: String)(implicit spark: SparkSession) = {

    val schema = Encoders.product[ForexRecordRaw].schema

    val df = spark
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

    dataframeToDataset(df)

  }

  def dataframeToDataset(dataFrame: DataFrame)(implicit spark: SparkSession):Dataset[ForexRecord] = {
    import spark.implicits._


    dataFrame

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
