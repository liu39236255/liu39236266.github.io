package com.nifu.study.Base_01_Scala._09_Actor

import scala.actors.Actor

class _02YourActor extends Actor {

  override def act(): Unit = {
    loop {
      react {
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(8000)
          println("stopped ...")
        }
      }

    }
  }
}


object _02YourActor {
  def main(args: Array[String]) {
    val actor = new _02YourActor
    actor.start()
    actor ! "stop"
    actor ! "start"
    println("消息发送完成！")
  }
}