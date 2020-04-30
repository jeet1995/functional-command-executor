package com.commandExecution.framework.commands.netstat

import com.commandExecution.framework.commands.grep.GrepCommand
import com.commandExecution.framework.commands.grep.GrepCommand.Grep.MinGrepCommandForAddingFile
import com.commandExecution.framework.commands.netstat.NetstatCommand.Netstat.{MinNetstatCommand, MinNetstatCommandWithPipe, NatuOption}
import com.commandExecution.framework.utils.CommandStringBuilder

import scala.sys.process._

/**
  * This class is an encapsulation of some features of the netstat command which displays network statistics.
  * */
class NetstatCommand[Netstat <: NetstatCommand.Netstat](commandStrings: Seq[String] = Seq()) {

  /**
    * This method builds the command string so as to add -natu option to the netstat command.
    *
    * @param ev Implicit event variable to check if type is that of NetstatCommand.Netstat
    * @return An instance of NetstatCommand
    * */
  def addNatuOption(implicit ev: Netstat =:= NetstatCommand.Netstat): NetstatCommand[Netstat with NatuOption] = {
    new NetstatCommand(commandStrings :+ "-natu")
  }

  /**
    * This method builds the command string so as to add a pipe to netstat -natu command to give netstat -natu | grep [string to be searched] command
    *
    * @param ev Implicit event variable to check if invoking type is a subtype of MinNetstatCommand
    * @return An instance of NetstatCommand
    * */
  def pipe(command: GrepCommand[MinGrepCommandForAddingFile])(implicit ev: Netstat <:< MinNetstatCommand): NetstatCommand[MinNetstatCommandWithPipe] = {
    var commandSeq: Seq[String] = Seq()

    commandSeq = commandStrings
    commandSeq = commandSeq :+ "|"
    commandSeq = commandSeq ++ command.build

    new NetstatCommand(commandSeq)
  }

  /**
    * This method executes netstat -natu | grep [string to be searched] command
    *
    * @param ev Implicit event variable to check if invoking type is a subtype of MinNetstatCommand
    * @return An array of string results.
    * */
  def executeWithPipe(implicit ev: Netstat <:< MinNetstatCommandWithPipe): Array[String] = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("netstat" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "netstat" +: commandStrings

    }

    commandSeq.!!.split("\n")
  }

  /**
    * This method executes the netstat -natu
    *
    * @param ev Implicit event variable to check if invoking type is a subtype of MinNetstatCommand
    * @return An instance of NetstatCommand
    * */
  def executeWithNatu(implicit ev: Netstat =:= MinNetstatCommand): Array[String] = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("netstat" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "netstat" +: commandStrings

    }
    commandSeq.!!.split("\n")
  }
}

/**
  * This companion object creates traits which are used to define type aliases which in turn help in type
  * checking when composing methods that can be called on an instance of NetstatCommand
  * */
object NetstatCommand {

  sealed trait Netstat

  object Netstat {

    sealed trait Pipe extends Netstat

    sealed trait NatuOption extends Netstat

    type MinNetstatCommand = Netstat with NatuOption

    type MinNetstatCommandWithPipe = MinNetstatCommand with Pipe
  }

}