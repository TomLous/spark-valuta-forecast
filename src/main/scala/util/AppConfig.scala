package util

import com.typesafe.config.{Config, ConfigFactory}

/**
  * Created by Tom Lous on 15/10/2017.
  */
trait AppConfig {

  lazy implicit val config: Config = ConfigFactory.load()

  val targetDir:String = config.getString("output.targetDir")
  val modelSubdir:String = config.getString("output.modelSubdir")
  val modelDir:String = s"$targetDir/$modelSubdir"

  val forexFile:String = config.getString("input.forexFile")
  lazy val forexFilePath:String = getClass.getResource(forexFile).getPath

  val forexParquet:String = config.getString("input.forexParquet")
  lazy val forexParquetPath:String = getClass.getResource(forexParquet).getPath

  lazy val mlTargetField:String = config.getString("ml.targetField")
  lazy val windowSize:Int = config.getInt("window.size")
  lazy val windowSlide:Int = config.getInt("window.slide")
}
