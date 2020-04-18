package com.nifu.study.Base_01_Scala._10akka.akkaDemo

import akka.actor.{Actor, ActorRef, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

class Master extends Actor{

  override def preStart(): Unit = {
    println("preStart方法执行")
  }

  override def receive: Receive = {
    case "start" => println("接收到自己发送的Start信息")
    case "stop" => println("stop")
    case "connect" => {
      println("connect")
      sender ! "reply"
    }
  }
}
object Master{
  def main(args: Array[String]): Unit = {

    val host = "localhost"
    val port = "6666"

    val configStr =
      s"""
        |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
        |akka.remote.netty.tcp.hostname = "$host"
        |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    // 配置信息
    val config: Config = ConfigFactory.parseString(configStr)

    // 创建ActorSystem
    val actorSystem = ActorSystem("MasterSystem", config)

    // 创建Actor
    val master: ActorRef = actorSystem.actorOf(Props[Master], "Master")

    master ! "start"

    // 退出方法
    actorSystem.awaitTermination()
    actorSystem.shutdown()
  }
}











