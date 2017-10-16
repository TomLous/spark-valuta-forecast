Spark Valuta Forecaster
=====

Attempt to use Deep or Machine Learning to predict the next tick in forex data.

Caveat: I know next to nothing about stocks, valuta or any financial system, so it's more a fun experiment with time series data than anything else

Data downloaded from:
http://www.forextester.com/data/datasources

Store files in src/main/resources/forex and adjust application.conf


Run with `-Dspark.master=local[N]` where N is number of cores you want to use

Steps,
1. *optional* `Preview`
2. `ConvertCSVToParquet` optionally with year to filter as param
3. *optional* `PreviewParquet`
4. `ConvertParquetToLabeledPoint` optionally with year to filter as param
5. `TrainLinearRegressionModel` optionally with year to filter as param
6.