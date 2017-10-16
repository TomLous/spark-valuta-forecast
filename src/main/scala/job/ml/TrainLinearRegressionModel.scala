package job.ml

import java.nio.file.{Paths, Files}
import java.nio.charset.StandardCharsets

import org.apache.spark.ml.evaluation.RegressionEvaluator
import org.apache.spark.ml.regression.{LinearRegression, LinearRegressionModel}
import org.apache.spark.ml.tuning.{ParamGridBuilder, TrainValidationSplit}
import util.{AppConfig, SparkJob}

/**
  * Created by Tom Lous on 16/10/2017.
  */
object TrainLinearRegressionModel extends App with SparkJob with AppConfig {

  val postFix = args.headOption.map(p => "-" + p).getOrElse("")

  val path = forexParquetPath + postFix + ".libsvm"

  val labeledPoints = spark.read.parquet(path)

  val split = Array(0.8,0.2)
  val seed = 11081979

  val (training, test) = labeledPoints.randomSplit(split, seed=seed).toList match {
    case a :: b :: _ => (a,b)
    case _ => throw new Exception("Invalid train/test split")
  }


  val lr = new LinearRegression()
    .setMaxIter(100)

  val paramGrid = new ParamGridBuilder()
    .addGrid(lr.regParam, Array(0.1, 0.01, 0.001))
    .addGrid(lr.fitIntercept)
    .addGrid(lr.elasticNetParam, Array(0.0, 0.25, 0.5, 0.75, 1.0))
    .build()


  val trainValidationSplit = new TrainValidationSplit()
    .setEstimator(lr)
    .setEvaluator(new RegressionEvaluator)
    .setEstimatorParamMaps(paramGrid)
    .setTrainRatio(split(0))

  val model = trainValidationSplit.fit(training)

  val predictions = model.transform(test)

  val evaluator = new RegressionEvaluator()
    .setLabelCol("label")
    .setPredictionCol("prediction")

  val testRmse = evaluator.setMetricName("rmse").evaluate(predictions)
  val testR2 = evaluator.setMetricName("r2").evaluate(predictions)



  model.bestModel match {
    case m:LinearRegressionModel => {
        val trainingSummary = m.summary

      val modelInfo =
        s"""
          |Data:
          |-------------------------
          |source: $path
          |#records: ${labeledPoints.count}
          |split: ${split.mkString("/")}
          |seed: $seed
          |
          |Best Model:
          |-------------------------
          |regParam: ${m.getRegParam}
          |elasticNetParam: ${m.getElasticNetParam}
          |fitIntercept: ${m.getFitIntercept}
          |solver: ${m.getSolver}
          |standardization: ${m.getStandardization}
          |tol: ${m.getTol}
          |aggregationDepth: ${m.getAggregationDepth}
          |
          |Training Summary
          |-------------------------
          |numIterations: ${trainingSummary.totalIterations}
          |objectiveHistory: [${trainingSummary.objectiveHistory.mkString(",")}]
          |RMSE: ${trainingSummary.rootMeanSquaredError}
          |r²: ${trainingSummary.r2}
          |
          |Test Summary:
          |-------------------------
          |RMSE: $testRmse
          |r²: $testR2
        """.stripMargin

        println(modelInfo)
        m.write.overwrite().save(s"$modelDir/linreg$postFix/")

        Files.write(Paths.get(s"$modelDir/linreg$postFix.info.txt"), modelInfo.getBytes(StandardCharsets.UTF_8))


        println(s"Stored model to $modelDir/linreg$postFix/")

    }
  }




}
