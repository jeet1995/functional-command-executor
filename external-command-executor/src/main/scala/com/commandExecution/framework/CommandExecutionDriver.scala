package com.commandExecution.framework

import com.commandExecution.framework.commands.cat.CatCommand
import com.commandExecution.framework.commands.grep.GrepCommand
import com.commandExecution.framework.commands.grep.GrepCommand.Grep.MinGrepCommandForAddingFile
import com.commandExecution.framework.commands.netstat.{NetstatCommand, NetstatSchemaUtils}
import com.commandExecution.framework.commands.ps.{PsCommand, PsSchemaUtils}
import com.commandExecution.framework.commands.wc.WcCommand
import com.typesafe.scalalogging.LazyLogging

/**
  * This singleton class represents the driver program for testing the command execution framework.
  **/
object CommandExecutionDriver extends LazyLogging {

  def main(args: Array[String]): Unit = {

    // Executing the ps | wc -l command
    val psCommandResults = new PsCommand[PsCommand.Ps]
      .addAuxOption
      .pipe(new WcCommand[WcCommand.Wc].addLOption)
      .executeWithPipe

    logger.info("Logging ps aux | wc -l command output")
    logger.info(psCommandResults)

    logger.info("Logging ps aux")

    // Executing the ps aux command
    new PsCommand[PsCommand.Ps]
      .addAuxOption
      .executeWithAux
      .flatMap(PsSchemaUtils.generatePsAuxResult(_))
      .filter(_.user == "root")
      .foreach(x => logger.info(x.toString))

    // Executing the cat files/story.txt command
    val catCommandResults = new CatCommand[CatCommand.Cat]
      .appendPath("files")
      .appendFile("story.txt")
      .execute

    logger.info("Logging cat files/story.txt command output")

    logger.info(catCommandResults)

    // Executing the grep Hello files/story.txt command
    val grepCommandResults1 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Hello")
      .addFileType("files/story.txt")
      .execute

    logger.info("Logging grep Hello files/story.txt command output")

    logger.info(grepCommandResults1)

    // Executing the grep Moto files/story.txt command
    val grepCommandResults2 = new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Moto")
      .addFileType("files/story.txt")
      .execute

    logger.info("Logging netstat -natu command output")

    // Executing the netstat -natu command and then filtering to include datagrams
    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .executeWithNatu
      .flatMap(NetstatSchemaUtils.generateNetstatResults(_))
      .foreach(x => logger.info(x.toString))

    logger.info("Logging netstat -natu | grep docker command output")

    // Executing the netstat -natu | grep docker command to check for lines containing `docker`
    new NetstatCommand[NetstatCommand.Netstat]()
      .addNatuOption
      .pipe(new GrepCommand[MinGrepCommandForAddingFile].addStringToSearch("docker"))
      .executeWithPipe
      .flatMap(x => NetstatSchemaUtils.generateNetstatResults(x))
      .foreach(x => logger.info(x.toString))

    val wcCommandResults = new WcCommand[WcCommand.Wc]()
      .addLOption
      .appendPath("files")
      .appendFile("story.txt")
      .execute

    logger.info("Logging wc -l files/story.txt command output")

    logger.info(wcCommandResults)

  }

}
