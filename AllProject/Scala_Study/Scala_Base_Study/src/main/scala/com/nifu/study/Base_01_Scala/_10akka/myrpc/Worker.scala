package com.nifu.study.Base_01_Scala._10akka.myrpc

import java.util.UUID

import scala.concurrent.duration._
import akka.actor.{Actor, ActorSelection, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

class Worker(val host: String, val port: Int, val masterHost: String, val masterPort: Int,
             val memory: Int, val cores: Int) extends Actor{

  // 随机生成一个worker id
  val workerId = UUID.randomUUID().toString
  // 用来存储Master URL
  var masterUrl: String = _
  // 心跳间隔
  val heargBeat_Interval: Long = 10000
  // master的地址
  var master: ActorSelection = _

  override def preStart(): Unit = {
    master = context.actorSelection(s"akka.tcp://${Master.MASTER_SYSTEM}" +
      s"@$masterHost:$masterPort/user/${Master.MASTER_ACTOR}")
    master ! RegisterWorker(workerId, host, port, memory, cores)
  }

  override def receive: Receive = {
    // Worker接收到Master注册成功的反馈消息
    case RegisteredWorker(masterUrl) => {
      this.masterUrl = masterUrl
      // 启动一个定时器，用来定时发送心跳
      import context.dispatcher
      context.system.scheduler.schedule(0 millis, heargBeat_Interval millis,
        self, SendHeartBeat)
    }
    case SendHeartBeat => {
      // 发送心跳之前进行一些检查
      master ! HeartBeat(workerId)
    }
  }

}
object Worker{

  val WORKER_SYSTEM = "WorkerSystem"
  val WORKER_ACTOR = "Worker"

  def main(args: Array[String]): Unit = {

    val host = args(0)
    val port = args(1).toInt
    val masterHost = args(2)
    val masterPort = args(3).toInt
    val memory = args(4).toInt
    val cores = args(5).toInt

    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config: Config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem(WORKER_SYSTEM, config)
    actorSystem.actorOf(Props(new Worker(host, port, masterHost,
      masterPort, memory, cores)), WORKER_ACTOR)

    actorSystem.awaitTermination()

  }
}
