package com.nifu.study.Base_01_Scala._10akka.akkaDemo

import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

class Worker extends Actor{
  override def preStart(): Unit = {
    // 获取Master的地址
    val master: ActorSelection =
      context.actorSelection("akka.tcp://MasterSystem@localhost:6666/user/Master")
    master ! "connect"
  }

  override def receive: Receive = {
    case "self" => println("接收到自己给自己发送的信息")
    case "reply" => {
      println("接收到Master发送的reply信息")
    }
  }
}
object Worker{
  def main(args: Array[String]): Unit = {

    val host = "localhost"
    val port = "8888"

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config: Config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem("WorkerSystem", config)
    val worker = actorSystem.actorOf(Props(new Worker), "worker")

    worker ! "self"

    actorSystem.awaitTermination()
    actorSystem.shutdown()
  }
}
