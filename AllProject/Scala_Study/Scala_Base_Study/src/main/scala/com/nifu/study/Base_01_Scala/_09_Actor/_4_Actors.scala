package com.nifu.study.Base_01_Scala._09_Actor

import java.io.File

import scala.actors.{Actor, Future}
import scala.collection.mutable
import scala.io.Source


class Task extends Actor {

  override def act(): Unit = {
    loop {
      react {
        case SubmitTask(fileName) => {
          val contents = Source.fromFile(new File(fileName)).mkString
          val arr = contents.split("\r\n")
          val result = arr.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.length)

          //val result = arr.flatMap(_.split(" ")).map((_, 1)).groupBy(_._1).mapValues(_.foldLeft(0)(_ + _._2))
          sender ! ResultTask(result)
        }
        case StopTask => {
          exit()
        }
      }
    }
  }
}

object _4_Actors {
  def main(args: Array[String]): Unit = {
    val files = Array("D://words.txt", "D://words.log")

    val replaySet = new mutable.HashSet[Future[Any]]
    val resultList = new mutable.ListBuffer[ResultTask]

    for (f <- files) {
      val t = new Task
      val replay = t.start() !! SubmitTask(f)
      replaySet += replay
    }

    while (replaySet.size > 0) {
     val toCumpute = replaySet.filter(_.isSet)
      for (r <- toCumpute) {
        println(r.getClass)// class scala.actors.FutureActor
        val result = r.apply()
        resultList += result.asInstanceOf[ResultTask]
        replaySet.remove(r)
      }
      Thread.sleep(100)
    }
    val finalResult = resultList.map(_.result).flatten.groupBy(_._1).mapValues(x => x.foldLeft(0)(_ + _._2))
    println(finalResult) // Map(java -> 3, php -> 3, scala -> 4, python -> 2, c -> 3)

  }

}

case class SubmitTask(fileName: String)

case object StopTask

case class ResultTask(result: Map[String, Int])
