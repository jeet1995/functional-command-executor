package com.commandExecution.framework.schema

case class NetstatResults(address: String,
                          typeMessage: String,
                          receiveQ: String,
                          sendQ: String,
                          iNode: String,
                          conn: String,
                          refs: String,
                          nextRef: String,
                          addr: String)
