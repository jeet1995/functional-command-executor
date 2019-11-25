package com.commandExecution.framework.commands.grep

import com.commandExecution.framework.commands.grep.GrepCommand.Grep.{FileType, MinGrepCommand, MinGrepCommandForFileType, StringToSearch}
import scala.sys.process._

class GrepCommand[Grep <: GrepCommand.Grep](commandStrings: Seq[String] = Seq()) {

  def addStringToSearch(string: String)(implicit ev: Grep <:< GrepCommand.Grep): GrepCommand[MinGrepCommandForFileType] = {
    new GrepCommand[MinGrepCommandForFileType](commandStrings :+ string)
  }

  def addFileType(fileType: String)(implicit ev: Grep <:< MinGrepCommandForFileType): GrepCommand[Grep with FileType] = {
    new GrepCommand[Grep with FileType](commandStrings :+ fileType)
  }

  def execute(implicit ev: Grep <:< MinGrepCommand): String = {
    ("grep" +:  commandStrings).!!
  }

  def build = "grep" +: commandStrings


}

object GrepCommand {

  sealed trait Grep

  object Grep {

    sealed trait StringToSearch extends Grep

    sealed trait FileType extends Grep

    type MinGrepCommandForFileType = Grep with StringToSearch

    type MinGrepCommand = Grep with StringToSearch with FileType
  }


}
