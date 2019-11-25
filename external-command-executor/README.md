CS474 - Homework 3
---
Homework 3 : object-oriented pure functional design and implementation of an external command execution framework as an I/O monad.
---
Name : Abhijeet Mohanty
---
### Overview

* As a part of this homework, I create an external command execution framework for commands such as `wc, grep, netstat, cat, ps`. Here I provide
a command option or provide provision to pipe using **sealed traits**, **type aliases** and **implicits** for type checking when
building composable functions.
 
### Setup information

* OS: MacOS Mojave
* Local development IDE: IntelliJ IDEA 2018.1
* Language: Scala v2.11.8
* Build tool: Simple build tool (SBT) v1.1.2 

### Steps to follow to set up the execution environment

* After cloning the project titled **abhijeet_mohanty_cs474_hw3** navigate to **abhijeet_mohanty_cs474_hw3/external-command-executor** and run the following command to clean, compile and run the project.
    
    `> sbt clean compile run`
    

### Command execution framework

#### ps command
* The idea I have used is that the command to be executed is first instantiated, an example for the `ps` command is as follows :

``
new PsCommand[PsCommand.Ps]()
``

* In order to execute the `ps` command, we have to either pipe with the `wc` command or add the `aux` option. One can do so by the following composition :

````
new PsCommand[PsCommand.Ps]
   .addAuxOption
   .executeWithAux
````

or

````
new PsCommand[PsCommand.Ps]
   .pipe(new WcCommand[WcCommand.Wc].addLOption)
   .executeWithPipe
````

* Next, we need to display the results of the execution of the `ps` command, so we use a monadic combinator `flatMap` to
 take the string outputs of `ps aux` and map it to `PsAuxResult` as below :

````
new PsCommand[PsCommand.Ps]
  .addAuxOption
  .executeWithAux
  .flatMap(PsSchemaUtils.generatePsAuxResult(_))
  .filter(_.user == "root")
  .foreach(x => logger.info(x.toString))
````

#### cat command
* The idea I have used is that the command to be executed is first instantiated, an example for the `cat` command is as follows :

``
new CatCommand[CatCommand.Cat]
``

* In order to execute the `cat` command, we have to add the string to specify the path and the file. A directory in the directory hierarchy can be added in a composable manner as below :

````
new CatCommand[CatCommand.Cat]
      .appendPath("files")
      .appendFile("story.txt")
      
````

* Next, we need to display the results of the execution of the `cat` command, so we further compose as below :

````
val catCommandResults = new CatCommand[CatCommand.Cat]
   .appendPath("files")
   .appendFile("story.txt")
   .execute
   
println(catCommandResults)    
````

#### grep command
* The idea I have used is that the command to be executed is first instantiated, an example for the `grep` command is as follows :

``
new GrepCommand[GrepCommand.Grep]
``

* In order to execute the `grep` command, we have to add the string to be searched and the file name to search in. One can do so by the following composition :

````
new GrepCommand[GrepCommand.Grep]
      .addStringToSearch("Hello")
      .addFileType("files/story.txt")
      
````


* Next, we need to execute and display the results of the execution of the `grep` command, so we further compose as below :

````
val grepCommandResults = new GrepCommand[GrepCommand.Grep]
    .addStringToSearch("Hello")
    .addFileType("files/story.txt")
    .execute
      
println(grepCommandResults)    
````

#### netstat command
* The idea I have used is that the command to be executed is first instantiated, an example for the `ps` command is as follows :

``
new NetstatCommand[NetstatCommand.Netstat]()
``

* In order to execute the `netstat` command, we have to either pipe with the `grep` command or add the `-natu` option. One can do so by the following composition :

````
new NetstatCommand[NetstatCommand.Netstat]()
  .addNatuOption
````

or

````
new NetstatCommand[NetstatCommand.Netstat]()
  .addNatuOption
  .pipe(new GrepCommand[MinGrepCommandForFileType].addStringToSearch("docker"))
````

* Next, we need to display the results of the execution of the `netstat` command, so we further compose using a flatMap
 to convert results returned as a `String` and map it to a `NetstatResult` as below :

````
new NetstatCommand[NetstatCommand.Netstat]()
  .addNatuOption
  .pipe(new GrepCommand[MinGrepCommandForFileType].addStringToSearch("docker"))
  .executeWithPipe
  .flatMap(x => NetstatSchemaUtils.generateNetstatResults(x))
  .foreach(x => logger.info(x.toString))
````
### Phantom types

* 

### Results

### Future improvements


