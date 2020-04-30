package com.commandExecution.framework.commands.wc

import java.io.File

import com.commandExecution.framework.commands.cat.CatCommand.Cat.AppendPath
import com.commandExecution.framework.commands.wc.WcCommand.Wc.{LOption, MinWcCommandForExecution, MinWcCommandForPipe}

import scala.sys.process._

/**
  * This class is an encapsulation of wc -l command which counts the no. of lines in a file or the size
  * in bytes of a file.
  **/
class WcCommand[Wc <: WcCommand.Wc](commandStrings: Seq[String] = Seq()) {

  /**
    * This method adds the line count option to the wc command
    **/
  def addLOption: WcCommand[Wc with LOption] = new WcCommand(commandStrings :+ "-l")


  /**
    * This method appends the path to search for a file to the command wc
    *
    * @param path The name of the path to be appended. This method can be called repeatedly to build the complete path.
    * @param ev   An implicit event of type Wc
    **/
  def appendPath(path: String)(implicit ev: Wc <:< WcCommand.Wc): WcCommand[Wc with AppendPath] = {
    new WcCommand[Wc with AppendPath](commandStrings :+ path + File.separator)
  }

  /**
    * This method adds name of the file to the command wc
    *
    * @param fileName Name of the file
    * @param ev       An implicit event of type Wc
    **/
  def appendFile(fileName: String)(implicit ev: Wc <:< WcCommand.Wc): WcCommand[MinWcCommandForExecution] = {
    new WcCommand[MinWcCommandForExecution](commandStrings :+ fileName)
  }

  /**
    * This method builds the command string corresponding to wc
    *
    * @param ev An implicit event whose type is a subtype of MinWcCommandForPipe
    **/
  def build(implicit ev: Wc <:< MinWcCommandForPipe): Seq[String] = "wc" +: commandStrings


  /**
    * This method executes the wc command
    *
    * @param ev An implicit event whose type is a subtype of MinWcCommandForExecution
    **/
  def execute(implicit ev: Wc <:< MinWcCommandForExecution): String = {
    if (commandStrings.contains("-l")) {

      ("wc" +: Seq("-l", commandStrings.filter(!_.contains("-l")).foldLeft("")((a, b) => a.concat(b)).trim)).!!

    } else {

      commandStrings.foldLeft("")((a, b) => a.concat(b))
      ("wc" +: commandStrings).!!

    }
  }
}

/**
  * This companion object creates traits which are used to define type aliases which in turn help in type
  * checking when composing methods that can be called on an instance of PsCommand
  **/
object WcCommand {

  sealed trait Wc

  object Wc {

    sealed trait LOption extends Wc

    sealed trait AppendPath extends Wc

    sealed trait AppendFileName extends Wc

    // Type alias for upper bound to pipe
    type MinWcCommandForPipe = Wc with LOption

    // Type alias for upper bound to execute
    type MinWcCommandForExecution = Wc with LOption with AppendFileName

  }

}
