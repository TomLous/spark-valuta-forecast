package util

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by Tom Lous on 15/10/2017.
  */
trait AppConfig {

  lazy implicit val config: Config = ConfigFactory.load()

  lazy val forexFilePath:String = getClass.getResource(config.getString("forexFile")).getPath
  lazy val forexParquetPath:String = getClass.getResource(config.getString("forexParquet")).getPath

  lazy val mlTargetField:String = config.getString("ml.targetField")
  lazy val windowSize:Int = config.getInt("window.size")
  lazy val windowSlide:Int = config.getInt("window.slide")
}
