package dataset

import model.ForexRecord
import org.apache.spark.sql.{Dataset, Encoders, SparkSession}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object ForexDataset {

  type ForexDataset = Dataset[ForexRecord]

  def apply(inputPath: String)(implicit spark: SparkSession) = {

    import spark.implicits._

    val schema = Encoders.product[ForexRecord].schema

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

      .withColumnRenamed("<TICKER>", "ticker")
      .withColumnRenamed("<DTYYYYMMDD>", "date")
      .withColumnRenamed("<TIME>", "time")
      .withColumnRenamed("<OPEN>", "open")
      .withColumnRenamed("<HIGH>", "high")
      .withColumnRenamed("<LOW>", "low")
      .withColumnRenamed("<CLOSE>", "close")
      .withColumnRenamed("<VOL>", "vol")

      .as[ForexRecord]

  }
}
