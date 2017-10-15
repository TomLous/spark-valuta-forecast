package model

import java.sql.{Date, Time}

/**
  * Created by Tom Lous on 15/10/2017.
  * Copyright © 2017 Datlinq B.V..
  */
case class ForexRecord(
                      ticker: String, // <TICKER>
                      date: String, //  <DTYYYYMMDD>
                      time: String, //<TIME>
                      open: Double, //<OPEN>
                      high: Double, //<HIGH>
                      low: Double, //<LOW>
                      close: Double, //<CLOSE>
                      vol: Int //<VOL>
                      )