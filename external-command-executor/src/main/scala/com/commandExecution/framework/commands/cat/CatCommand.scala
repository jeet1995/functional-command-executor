package com.commandExecution.framework.commands.cat

import java.io.File

import com.commandExecution.framework.commands.cat.CatCommand.Cat.{AppendFileName, AppendPath, MinCatCommand}

import scala.sys.process._

/**
  * This class is an encapsulation of some features of the cat command which displays the contents of a file.
  * */
class CatCommand[Cat <: CatCommand.Cat](commandStrings: Seq[String] = Seq()) {

  def appendPath(path: String)(implicit ev: Cat <:< CatCommand.Cat): CatCommand[Cat with AppendPath] = {
    new CatCommand(commandStrings :+ path + File.separator)
  }

  def appendFile(fileName: String)(implicit ev: Cat <:< CatCommand.Cat): CatCommand[Cat with AppendFileName] = {
    new CatCommand(commandStrings :+ fileName)
  }

  def execute(implicit ev: Cat <:< MinCatCommand): String = {
    ("cat" +: Seq() :+ commandStrings.foldLeft("")((a, b) => a + b)).!!
  }


}

/**
  * This companion object creates traits which are used to define type aliases which in turn help in type
  * checking when composing methods that can be called on an instance of CatCommand
  * */
object CatCommand {

  sealed trait Cat

  object Cat {

    sealed trait AppendPath extends Cat

    sealed trait AppendFileName extends Cat

    // Type alias
    type MinCatCommand = Cat with AppendFileName

    // Type alias
    type MinCatCommandForAppendPath = Cat with AppendPath

  }

}