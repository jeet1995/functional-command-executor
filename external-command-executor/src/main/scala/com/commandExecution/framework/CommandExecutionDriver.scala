package com.commandExecution.framework

import com.commandExecution.framework.commands.cat.CatCommand
import com.commandExecution.framework.commands.grep.GrepCommand
import com.commandExecution.framework.commands.grep.GrepCommand.Grep.MinGrepCommandForFileType
import com.commandExecution.framework.commands.netstat.NetstatCommand
import com.commandExecution.framework.commands.ps.PsCommand
import com.commandExecution.framework.commands.wc.WcCommand
import com.typesafe.scalalogging.LazyLogging

object CommandExecutionDriver extends LazyLogging {

  def main(args: Array[String]): Unit = {

    new PsCommand[PsCommand.Ps]
      .pipe(new WcCommand[WcCommand.Wc].addLOption)
      .executeWithPipe

    new PsCommand[PsCommand.Ps]
      .addAuxOption
      .executeWithAux
      .filter(_.user != "root")
      .filter(_.user != "SubrataMohanty")
      .foreach(println)

    val catCommandResults = new CatCommand[CatCommand.Cat]
      .appendPath("files")
      .appendFile("story.txt")
      .execute
      .get

    val grepCommandResults1 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Hello")
      .addFileType("files/story.txt")
      .execute

    println(grepCommandResults1)

    val grepCommandResults2 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Moto")
      .addFileType("files/story.txt")
      .execute


    println(grepCommandResults2)

    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .executeWithNatu
      .filter(_.typeMessage == "dgram")
      .foreach(println)

    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .pipe(new GrepCommand[MinGrepCommandForFileType].addStringToSearch("docker"))
      .executeWithPipe
      .foreach(println)


  }

}
