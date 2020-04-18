package com.nifu.study.Base_01_Scala._09_Actor

import scala.actors.Actor


object _01MyActor2 extends Actor{
  //重新act方法
  def act(){
    for(i <- 1 to 10){
      println("actor-1 " + i)
      Thread.sleep(2000)
    }
  }
}

object MyActor2 extends Actor{
  //重新act方法
  def act(){
    for(i <- 1 to 10){
      println("actor-2 " + i)
      Thread.sleep(2000)
    }
  }
}

//
object ActorTest extends App{
  //启动Actor
  _01MyActor2.start()
  MyActor2.start()
}
