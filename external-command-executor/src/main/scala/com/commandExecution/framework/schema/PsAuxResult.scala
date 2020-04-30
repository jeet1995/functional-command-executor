package com.commandExecution.framework.schema

/**
  * Case class encapsulating the results of the ps aux command
  * */
case class PsAuxResult(user: String,
                       pId: Long,
                       pctCPU: Double,
                       pctMem: Double,
                       vsz: Long,
                       rss: Long,
                       tt: String,
                       stat: String,
                       started: String,
                       time: String,
                       command: String)
