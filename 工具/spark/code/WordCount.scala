package mycore

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

object WordCount {

  def main(args: Array[String]): Unit = {
    val conf = new SparkConf()
    conf.setAppName("WordCount")
    conf.setMaster("local[2]")

    val sc = new SparkContext(conf)

    val fileRDD: RDD[String] = sc.textFile("./src/main/scala/data/")
    val wordRDD = fileRDD.flatMap(_.split(" "))
    val wordMapRDD = wordRDD.map((_, 1))
    val wordCountRDD = wordMapRDD.reduceByKey((v1, v2) => v1 + v2)
    wordCountRDD.foreach(res => println(res))

    sc.stop()

  }

}
