package com.commandExecution.framework.commands.ps

import com.commandExecution.framework.schema.PsAuxResult

object PsSchemaUtils {

  def generatePsAuxResult(string: String): Option[PsAuxResult] = {

    if (!string.contains("USER")) {

      val stringContents = string.split("\\s+")

      Some(PsAuxResult(stringContents(0),
        stringContents(1).toLong,
        stringContents(2).toDouble,
        stringContents(3).toDouble,
        stringContents(4).toLong,
        stringContents(5).toLong,
        stringContents(6),
        stringContents(7),
        stringContents(8),
        stringContents(9),
        stringContents(10)))
    } else {
      None
    }
  }
}

