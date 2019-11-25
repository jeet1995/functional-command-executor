package com.commandExecution.framework.commands.netstat

import com.commandExecution.framework.commands.grep.GrepCommand
import com.commandExecution.framework.commands.grep.GrepCommand.Grep.MinGrepCommandForFileType
import com.commandExecution.framework.commands.netstat.NetstatCommand.Netstat.{MinNetstatCommand, MinNetstatCommandWithPipe, NatuOption}
import com.commandExecution.framework.schema.NetstatResults
import com.commandExecution.framework.utils.CommandStringBuilder

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.sys.process._

class NetstatCommand[Netstat <: NetstatCommand.Netstat](commandStrings: Seq[String] = Seq()) {

  def addNatuOption(implicit ev: Netstat =:= NetstatCommand.Netstat): NetstatCommand[Netstat with NatuOption] = {
    new NetstatCommand(commandStrings :+ "-natu")
  }

  def pipe(command: GrepCommand[MinGrepCommandForFileType])(implicit ev: Netstat <:< MinNetstatCommand): NetstatCommand[MinNetstatCommandWithPipe] = {
    var commandSeq: Seq[String] = Seq()

    commandSeq = commandStrings
    commandSeq = commandSeq :+ "|"
    commandSeq = commandSeq ++ command.build

    new NetstatCommand(commandSeq)
  }

  def executeWithPipe(implicit ev: Netstat <:< MinNetstatCommandWithPipe): ArrayBuffer[NetstatResults] = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("netstat" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "netstat" +: commandStrings

    }

    getExecutionProjection(commandSeq.!!)
  }


  def executeWithNatu(implicit ev: Netstat =:= MinNetstatCommand): ArrayBuffer[NetstatResults] = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("netstat" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "netstat" +: commandStrings

    }
    getExecutionProjection(commandSeq.!!)
  }

  def getExecutionProjection(string: String): ArrayBuffer[NetstatResults] = {

    val strings = string.split("\n")
    val netstatResults = mutable.ArrayBuffer[NetstatResults]()

    if (strings.length > 2) {

      strings.foreach {
        string =>

          if (!string.contains("Address") && !string.contains("Active")) {

            val stringContents = string.split("\\s+")

            netstatResults += NetstatResults(stringContents(0),
              stringContents(1),
              stringContents(2),
              stringContents(3),
              stringContents(4),
              stringContents(5),
              stringContents(6),
              stringContents(7),
              if (stringContents.length < 9) "" else stringContents(8))

          }
      }
    }
    netstatResults
  }

}

object NetstatCommand {

  sealed trait Netstat

  object Netstat {

    sealed trait Pipe extends Netstat

    sealed trait NatuOption extends Netstat

    type MinNetstatCommand = Netstat with NatuOption

    type MinNetstatCommandWithPipe = MinNetstatCommand with Pipe
  }

}