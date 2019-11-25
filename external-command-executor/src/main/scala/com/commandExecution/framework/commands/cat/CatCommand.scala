package com.commandExecution.framework.commands.cat

import java.io.File

import com.commandExecution.framework.commands.cat.CatCommand.Cat.{AppendFileName, AppendPath, MinCatCommand}

import scala.sys.process._

class CatCommand[Cat <: CatCommand.Cat](commandStrings: Seq[String] = Seq()) {

  def appendPath(path: String)(implicit ev: Cat <:< CatCommand.Cat): CatCommand[Cat with AppendPath] = {
    new CatCommand(commandStrings :+ path + File.separator)
  }

  def appendFile(fileName: String)(implicit ev: Cat <:< CatCommand.Cat): CatCommand[Cat with AppendFileName] = {
    new CatCommand(commandStrings :+ fileName)
  }

  def execute(implicit ev: Cat <:< MinCatCommand): Option[String] = {
    Some(("cat" +: Seq() :+ commandStrings.foldLeft("")((a, b) => a + b)).!!)
  }


}

object CatCommand {

  sealed trait Cat

  object Cat {

    sealed trait AppendPath extends Cat

    sealed trait AppendFileName extends Cat

    type MinCatCommand = Cat with AppendFileName

    type MinCatCommandForAppendPath = Cat with AppendPath

  }

}