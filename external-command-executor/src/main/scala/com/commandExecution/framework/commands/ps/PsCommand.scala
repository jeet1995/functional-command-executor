package com.commandExecution.framework.commands.ps

import com.commandExecution.framework.commands.ps.PsCommand.Ps._
import com.commandExecution.framework.commands.wc.WcCommand
import com.commandExecution.framework.commands.wc.WcCommand.Wc.MinWcCommand
import com.commandExecution.framework.schema.PsAuxResult
import com.commandExecution.framework.utils.CommandStringBuilder

import scala.collection.mutable
import scala.collection.mutable.ArrayBuffer
import scala.sys.process._

class PsCommand[Ps <: PsCommand.Ps](commandStrings: Seq[String] = Seq()) {

  def addAuxOption: PsCommand[Ps with AuxOption] = new PsCommand(commandStrings :+ "aux")


  def pipe(command: WcCommand[MinWcCommand])(implicit ev: Ps <:< MinPsCommand): PsCommand[Ps with Pipe] = {

    var commandSeq: Seq[String] = Seq()

    commandSeq = commandStrings
    commandSeq = commandSeq :+ "|"
    commandSeq = commandSeq ++ command.build

    new PsCommand(commandSeq)
  }

  def getAuxProjection(string: String) = {


    val strings = string.split("\n")
    val psResults = mutable.ArrayBuffer[PsAuxResult]()

    if (strings.length > 2) {

      strings.foreach {
        string =>

          if (!string.contains("USER")) {

            val stringContents = string.split("\\s+")

            psResults += PsAuxResult(stringContents(0),
              stringContents(1).toLong,
              stringContents(2).toDouble,
              stringContents(3).toDouble,
              stringContents(4).toLong,
              stringContents(5).toLong,
              stringContents(6),
              stringContents(7),
              stringContents(8),
              stringContents(9),
              stringContents(10)
            )
          }
      }
    }
    psResults
  }

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
