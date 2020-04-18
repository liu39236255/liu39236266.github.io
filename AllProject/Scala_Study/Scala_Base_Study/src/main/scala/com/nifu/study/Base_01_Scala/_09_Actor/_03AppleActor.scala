package com.nifu.study.Base_01_Scala._09_Actor

import scala.actors.Actor
import scala.concurrent.Future

class _03AppleActor extends Actor {

  def act(): Unit = {
    while (true) {
      receive {
        case "start" => println("starting ...")
        case SyncMsg(id, msg) => {
          println(id + ",sync " + msg)
          Thread.sleep(5000)
          sender ! ReplyMsg(3, "finished")

        }
        case AsyncMsg(id, msg) => {
          println(id + ",async " + msg)
          Thread.sleep(5000)
        }
      }
    }
  }
}

object _03AppleActor {
  def main(args: Array[String]) {
    val a = new _03AppleActor
    a.start()
    //异步消息
    a ! AsyncMsg(1, "hello actor")
    println("异步消息发送完成")
    //同步消息
    //val content = a.!?(1000, SyncMsg(2, "hello actor"))
    //println(content)
    val reply = a !! SyncMsg(2, "hello actor")
    println(reply.isSet) // false
    //println("123")
    val c = reply.apply()
    println(reply.isSet) // true
    println(c)

  }

}

case class SyncMsg(id: Int, msg: String)

case class AsyncMsg(id: Int, msg: String)

case class ReplyMsg(id: Int, msg: String)