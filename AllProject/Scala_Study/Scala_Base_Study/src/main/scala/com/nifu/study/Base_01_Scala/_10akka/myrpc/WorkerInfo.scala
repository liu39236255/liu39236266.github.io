package com.nifu.study.Base_01_Scala._10akka.myrpc

class WorkerInfo(val id: String, val host: String,
                 val port: Int, val memory: Int, val cores: Int) {

  var lastHeartbeatTime: Long = _
}
