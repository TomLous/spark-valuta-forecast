package job.preview

import dataset.ForexDataset
import org.apache.spark.sql.functions._
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 15/10/2017.
  */
object PreviewParquet extends App with SparkJob with AppConfig {

  import spark.implicits._

 val forexDataset = ForexDataset.loadParquet(forexParquetPath).cache()

  println("Show some lines from CSV")
  forexDataset.show()

  println(s"Total number of records ${forexDataset.count}")

  forexDataset.select(max('timestamp), min('timestamp)).show()

  forexDataset.select(max('high), min('high),max('low), min('low),max('open), min('open),max('vol), min('vol)).show()

  val naCount = forexDataset.filter('high.isNull || 'low.isNull || 'open.isNull || 'vol.isNull).count()

  println(s"# Record with null values : $naCount")


}
