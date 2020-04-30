package com.commandExecution.framework.commands.ps

import com.commandExecution.framework.commands.ps.PsCommand.Ps._
import com.commandExecution.framework.commands.wc.WcCommand
import com.commandExecution.framework.commands.wc.WcCommand.Wc.MinWcCommandForPipe
import com.commandExecution.framework.utils.CommandStringBuilder

import scala.sys.process._

/**
  * This class is an encapsulation of some features of the ps command which returns a snapshot of the current processes.
  **/
class PsCommand[Ps <: PsCommand.Ps](commandStrings: Seq[String] = Seq()) {


  /**
    * This method adds the aux option to the ps command
    *
    * @return An instance of type PsCommand
    **/
  def addAuxOption(): PsCommand[Ps with AuxOption] = new PsCommand(commandStrings :+ "aux")


  /**
    * This method adds a word count pipe to the ps command to give the resultant command such as ps aux | wc
    *
    * @param command Command of type WcCommand which is to be piped with a subtype of MinPsCommand
    * @param ev      An implicit event which is a subtype of MinPsCommand
    * @return An instance of type PsCommand
    **/
  def pipe(command: WcCommand[MinWcCommandForPipe])(implicit ev: Ps <:< MinPsCommand): PsCommand[Ps with Pipe] = {

    var commandSeq: Seq[String] = Seq()

    commandSeq = commandStrings
    commandSeq = commandSeq :+ "|"
    commandSeq = commandSeq ++ command.build

    new PsCommand(commandSeq)
  }


  /**
    * This method executes a ps command with a pipe
    *
    * @param ev An implicit event which is a subtype of PsCommandWithPipe
    * @return The output of the execution of ps aux | wc
    **/
  def executeWithPipe(implicit ev: Ps <:< PsCommandWithPipe): String = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("ps" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "ps" +: commandStrings

    }

    commandSeq.!!
  }


  /**
    * This method adds a word count pipe to the ps comma
    *
    * @param ev An implicit event which is an instance of type PsWithAux
    * @return The output of the execution of ps aux
    **/
  def executeWithAux(implicit ev: Ps =:= PsWithAux): Array[String] = {

    var commandSeq: Seq[String] = Seq()

    if (commandStrings.exists(_.contains("|"))) {

      val pipedCommandString = CommandStringBuilder.getGetStringFromCommandStrings("ps" +: commandStrings)

      commandSeq = commandSeq :+ pipedCommandString
      commandSeq = "-c" +: commandSeq
      commandSeq = "/bin/sh" +: commandSeq

    } else {
      commandSeq = "ps" +: commandStrings

    }

    commandSeq.!!.split("\n")
  }


}

/**
  * This companion object creates traits which are used to define type aliases which in turn help in type
  * checking when composing methods that can be called on an instance of PsCommand
  * */
object PsCommand {

  sealed trait Ps

  object Ps {

    sealed trait AuxOption extends Ps

    sealed trait Pipe extends Ps

    sealed trait Result extends Ps

    type MinPsCommand = Ps

    type PsWithAux = Ps with AuxOption

    type PsCommandWithPipe = Ps with Pipe

  }

}
