import dataset.ForexDataset
import util.{AppConfig, SparkJob}
import org.apache.spark.sql.functions._
/**
  * Created by Tom Lous on 15/10/2017.
  */
object Preview extends App with SparkJob with AppConfig {

  import spark.implicits._

 val forexDataset = ForexDataset.loadCSV(forexFilePath).cache()

  println("Show some lines from CSV")
  forexDataset.show()

  println(s"Total number of records ${forexDataset.count}")

  forexDataset.select(max('timestamp), min('timestamp)).show()

  forexDataset.select(max('high), min('high),max('low), min('low),max('open), min('open),max('vol), min('vol)).show()

  val naCount = forexDataset.filter('high.isNull || 'low.isNull || 'open.isNull || 'vol.isNull).count()

  println(s"# Record with null values : $naCount")


}
