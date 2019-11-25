package com.commandExecution.framework

import com.commandExecution.framework.commands.cat.CatCommand
import com.commandExecution.framework.commands.grep.GrepCommand
import com.commandExecution.framework.commands.grep.GrepCommand.Grep.MinGrepCommandForFileType
import com.commandExecution.framework.commands.netstat.NetstatCommand
import com.commandExecution.framework.commands.ps.PsCommand
import com.commandExecution.framework.commands.wc.WcCommand
import com.typesafe.scalalogging.LazyLogging

/**
  * This singleton class represents the driver program for testing the command execution framework.
  * */
object CommandExecutionDriver extends LazyLogging {

  def main(args: Array[String]): Unit = {

    // Executing the ps | wc -l command
    new PsCommand[PsCommand.Ps]
      .pipe(new WcCommand[WcCommand.Wc].addLOption)
      .executeWithPipe

    // Executing the ps aux command
    new PsCommand[PsCommand.Ps]
      .addAuxOption
      .executeWithAux
      .filter(_.user != "root")
      .foreach(println)

    // Executing the cat files/story.txt command
    val catCommandResults = new CatCommand[CatCommand.Cat]
      .appendPath("files")
      .appendFile("story.txt")
      .execute
      .get

    // Executing the grep Hello files/story.txt command
    val grepCommandResults1 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Hello")
      .addFileType("files/story.txt")
      .execute

    println(grepCommandResults1)

    // Executing the grep Moto files/story.txt command
    val grepCommandResults2 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Moto")
      .addFileType("files/story.txt")
      .execute


    // Executing the netstat -natu command and then filtering to include datagrams
    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .executeWithNatu
      .filter(_.typeMessage == "dgram")
      .foreach(println)

    // Executing the netstat -natu | grep docker command to check for lines containing `docker`
    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .pipe(new GrepCommand[MinGrepCommandForFileType].addStringToSearch("docker"))
      .executeWithPipe
      .foreach(println)


  }

}
