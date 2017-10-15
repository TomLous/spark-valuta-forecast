import dataset.ForexDataset
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 15/10/2017.
  * Copyright © 2017 Datlinq B.V..
  */
object Preview extends App with SparkJob with AppConfig {


 ForexDataset(forexFilePath)
    .show()



}
