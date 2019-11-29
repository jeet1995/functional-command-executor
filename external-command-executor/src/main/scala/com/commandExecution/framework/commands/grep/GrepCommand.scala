package com.commandExecution.framework.commands.grep

import com.commandExecution.framework.commands.grep.GrepCommand.Grep.{File, MinGrepCommand, MinGrepCommandForAddingFile, StringToSearch}
import scala.sys.process._

/**
  * This class is an encapsulation of some features of the grep command which displays the line containing a
  * particular string.
  * */
class GrepCommand[Grep <: GrepCommand.Grep](commandStrings: Seq[String] = Seq()) {

  def addStringToSearch(string: String)(implicit ev: Grep <:< GrepCommand.Grep): GrepCommand[MinGrepCommandForAddingFile] = {
    new GrepCommand[MinGrepCommandForAddingFile](commandStrings :+ string)
  }

  def addFileType(file: String)(implicit ev: Grep <:< MinGrepCommandForAddingFile): GrepCommand[Grep with File] = {
    new GrepCommand[Grep with File](commandStrings :+ file)
  }

  def execute(implicit ev: Grep <:< MinGrepCommand): String = ("grep" +:  commandStrings).!!


  def build = "grep" +: commandStrings


}

/**
  * This companion object creates traits which are used to define type aliases which in turn help in type
  * checking when composing methods that can be called on an instance of NetstatCommand
  * */
object GrepCommand {

  sealed trait Grep

  object Grep {

    sealed trait StringToSearch extends Grep

    sealed trait File extends Grep

    type MinGrepCommandForAddingFile = Grep with StringToSearch

    type MinGrepCommand = Grep with StringToSearch with File
  }


}
