package com.commandExecution.framework.commands.wc

import com.commandExecution.framework.commands.wc.WcCommand.Wc.{LOption, MinWcCommand}

class WcCommand[Wc <: WcCommand.Wc](commandStrings: Seq[String] = Seq()) {

  def addLOption: WcCommand[Wc with LOption] = new WcCommand(commandStrings :+ "-l")

  def build(implicit ev: Wc <:< MinWcCommand): Seq[String] = "wc" +: commandStrings

}

object WcCommand {

  sealed trait Wc

  object Wc {

    sealed trait LOption extends Wc

    type MinWcCommand = Wc with LOption

  }


}
