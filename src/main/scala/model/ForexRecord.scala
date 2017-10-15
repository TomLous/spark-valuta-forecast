package model

import org.apache.spark.sql.execution.streaming.FileStreamSource.Timestamp

/**
  * Created by Tom Lous on 15/10/2017.
  */
case class ForexRecord(
                           ticker: String, // <TICKER>
                           timestamp: Timestamp, //  Combi of <DTYYYYMMDD && <TIME>
                           open: Double, //<OPEN>
                           high: Double, //<HIGH>
                           low: Double, //<LOW>
                           close: Double, //<CLOSE>
                           vol: Int //<VOL>
                         )
