package com.commandExecution.framework.utils

object CommandStringBuilder {

  def getGetStringFromCommandStrings(commandStrings: Seq[String]): String = commandStrings.foldLeft("")((a, b) => a.concat(b).concat(" ")).trim

}
