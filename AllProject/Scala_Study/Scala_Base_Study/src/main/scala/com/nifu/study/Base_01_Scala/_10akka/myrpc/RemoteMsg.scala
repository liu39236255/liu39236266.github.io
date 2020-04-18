package com.nifu.study.Base_01_Scala._10akka.myrpc

trait RemoteMsg extends Serializable{

}
// Master -> self
case object CheckTimeOutWorker

// Master -> Worker
case class RegisteredWorker(masterUrl: String) extends RemoteMsg

// Worker -> Master
case class RegisterWorker(id: String, host: String,
                          port: Int, memory: Int, cores: Int) extends RemoteMsg

// Worker -> self
case object SendHeartBeat

// Worker -> Master (HeartBeat)
case class HeartBeat(workerId: String) extends RemoteMsg
