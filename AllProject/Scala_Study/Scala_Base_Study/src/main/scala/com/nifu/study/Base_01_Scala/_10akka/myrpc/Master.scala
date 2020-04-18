package com.nifu.study.Base_01_Scala._10akka.myrpc

import akka.actor.{Actor, ActorSystem, Props}
import com.typesafe.config.{Config, ConfigFactory}

import scala.collection.mutable

import scala.concurrent.duration._

class Master(val masterHost: String, val masterPort: Int) extends Actor{

  // 用来存储Worker的注册信息
  val idToWorker = new mutable.HashMap[String, WorkerInfo]()
  // 用来存储Worker的信息
  val workers = new mutable.HashSet[WorkerInfo]()
  // 超时时间
  val checkInterval: Long = 15000

  // preStart会被调用一次，在构造器之后，receive方法之前
  override def preStart(): Unit = {
    // 启动一个定时器，用于周期性的检查超时的Worker
    import context.dispatcher
    context.system.scheduler.schedule(0 millis, checkInterval millis,
      self, CheckTimeOutWorker)
  }

  override def receive: Receive = {
    // Worker -> Master
    case RegisterWorker(id, host, port, memory, cores) => {
      if (!idToWorker.contains(id)){
        val workerInfo = new WorkerInfo(id, host, port, memory, cores)
        idToWorker += (id -> workerInfo)
        workers += workerInfo
        println("a worker registered")

        sender ! RegisteredWorker(s"akka.tcp://${Master.MASTER_SYSTEM}" +
          s"@$masterHost:$masterPort/user/${Master.MASTER_ACTOR}")
      }
    }
    case HeartBeat(workerId) => {
      // 根据传过来的workerId获取相应的WorkerInfo
      val workerInfo = idToWorker(workerId)
      // 获取当前时间，用来更新心跳时间
      val currentTime = System.currentTimeMillis()
      // 更新最后一次心跳时间
      workerInfo.lastHeartbeatTime = currentTime
    }
    case CheckTimeOutWorker => {
      val currentTime = System.currentTimeMillis()
      val toRemove: mutable.HashSet[WorkerInfo] =
        workers.filter(w => currentTime - w.lastHeartbeatTime > checkInterval)
      toRemove.foreach(deadWorker => {
        // 将超时的Worker从内存中移除
        idToWorker -= deadWorker.id
        workers -= deadWorker
      })
      println(s"num of workers: ${workers.size}")
    }
  }

}
object Master{

  val MASTER_SYSTEM = "MasterSystem"
  val MASTER_ACTOR = "Master"

  def main(args: Array[String]): Unit = {

    val host = args(0)
    val port = args(1).toInt
    val configStr =
      s"""
         |akka.actor.provider = "akka.remote.RemoteActorRefProvider"
         |akka.remote.netty.tcp.hostname = "$host"
         |akka.remote.netty.tcp.port = "$port"
       """.stripMargin

    val config: Config = ConfigFactory.parseString(configStr)
    val actorSystem = ActorSystem(MASTER_SYSTEM, config)
    actorSystem.actorOf(Props(new Master(host, port)), MASTER_ACTOR)
    actorSystem.awaitTermination()

  }
}
