package com.nifu.study.Base_01_Scala._09_Actor

import  scala.actors.Actor
class _01MyActor  extends Actor {
  override def act(): Unit = {
    while (true) {
      receive {
        case "start" => {
          println("starting ...")
          Thread.sleep(5000)
          println("started")
        }
        case "stop" => {
          println("stopping ...")
          Thread.sleep(5000)
          println("stopped")
        }
      }
    }
  }
}

object  _01MyActor {
  def main(args: Array[String]) {
    val actor = new _01MyActor
    actor.start()
    actor ! "start"
    actor ! "stop"
    println("消息发送完成！")
  }
}
