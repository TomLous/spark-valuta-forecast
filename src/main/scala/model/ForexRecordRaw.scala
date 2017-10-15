package model

import java.sql.{Date, Time, Timestamp}

/**
  * Created by Tom Lous on 15/10/2017.
  */
case class ForexRecordRaw(
                      ticker: String, // <TICKER>
                      date: String, //  <DTYYYYMMDD>
                      time: String, //<TIME>
                      open: Double, //<OPEN>
                      high: Double, //<HIGH>
                      low: Double, //<LOW>
                      close: Double, //<CLOSE>
                      vol: Int //<VOL>
                      )
