package com.commandExecution.framework.commands.netstat

import com.commandExecution.framework.schema.NetstatResult

object NetstatSchemaUtils {

  def generateNetstatResults(string: String): Option[NetstatResult] = {

    if (!string.contains("Address") && !string.contains("Active")) {

      val stringContents = string.split("\\s+")

      Some(NetstatResult(stringContents(0),
        stringContents(1),
        stringContents(2),
        stringContents(3),
        stringContents(4),
        stringContents(5),
        stringContents(6),
        stringContents(7),
        if (stringContents.length < 9) "" else stringContents(8)))

    } else {
      None
    }
  }
}
