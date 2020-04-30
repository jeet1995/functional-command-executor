package com.commandExecution.framework.schema

/**
  * Case class encapsulating the results of the netstat command
  * */
case class NetstatResult(address: String,
                         typeMessage: String,
                         receiveQ: String,
                         sendQ: String,
                         iNode: String,
                         conn: String,
                         refs: String,
                         nextRef: String,
                         addr: String)
