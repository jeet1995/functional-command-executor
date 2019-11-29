CS474 - Homework 3
---
Homework 3 : An object-oriented pure functional design and implementation of an external command execution framework as an I/O monad.
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
* Language: Scala v2.12.8
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
  .flatMap(NetstatSchemaUtils.generateNetstatResults(_))
  .foreach(x => logger.info(x.toString))
````

#### wc command

* In order to execute the `wc` command, we have to add the `-l` option followed by the path to the file. One can do so as follows :

````
new WcCommand[WcCommand.Wc]()
      .addLOption
      .appendPath("files")
      .appendFile("story.txt")
````
* As we can see above, the path is created folder by folder in the folder hierarchy.

* Next, we need to display the results of the execution of the `wc -l` command and obtain results as a `string`

````
new WcCommand[WcCommand.Wc]()
      .addLOption
      .appendPath("files")
      .appendFile("story.txt")
      .execute
````


### Phantom types

* This idea forms the crux of the submission, wherein **type aliases** on **sealed traits** are defined within the companion object of some `*Command` class. 
* They enable the compiler to perform a type check which in turn enables the compiler to determine what methods can be called
for that type.
* For example, consider the `netstat` command. The executeWithPipe method can only be invoked on a `Netstat with Pipe` type otherwise a compilation error is issued.

### Results

* Running programmatically `netstat -natu | grep docker` 

````
NetstatResult(d581e466cd6d5833,stream,0,0,0,d581e466cd6d576b,0,0,docker.sock)
NetstatResult(d581e466dadbe1f3,stream,0,0,d581e466cc795353,0,0,0,docker.raw.sock)
NetstatResult(d581e466dadbe063,stream,0,0,0,d581e466dadbdd43,0,0,docker-api.sock)
NetstatResult(d581e466dadbec1b,stream,0,0,d581e466db5c9efb,0,0,0,/Users/SubrataMohanty/Library/Containers/com.docker.docker/Data/vms/0/00000002.0000f3a4)
NetstatResult(d581e466dadbdaeb,stream,0,0,d581e466db5ca6bb,0,0,0,docker-api.sock)
NetstatResult(d581e466dadbe8fb,stream,0,0,d581e466cc6a72db,0,0,0,docker.sock)
NetstatResult(d581e466dadbedab,stream,0,0,d581e466db53a4cb,0,0,0,/Users/SubrataMohanty/Library/Containers/com.docker.docker/Data/vms/0/00000002.00001000)
NetstatResult(d581e466cb704833,stream,0,0,d581e466cbae5f73,0,0,0,/var/run/com.docker.vmnetd.sock)
````
* Running programmatically `ps aux` command and then filtering user based on `root` and storing the results in `PsAuxResult`

````
PsAuxResult(root,84,0.2,0.3,4405972,24012,??,Rs,1:21PM,0:37.02,/System/Library/Frameworks/CoreServices.framework/Frameworks/Metadata.framework/Support/mds)
PsAuxResult(root,8446,0.0,0.1,4304640,11096,??,Ss,6:58PM,0:00.03,/System/Library/PrivateFrameworks/CommerceKit.framework/Versions/A/Resources/storeinstalld)
PsAuxResult(root,4269,0.0,0.1,4337992,6532,??,Ss,3:38PM,0:00.23,/usr/libexec/dprivacyd)
PsAuxResult(root,768,0.0,0.2,4332676,15344,??,Ss,1:23PM,0:00.09,/usr/libexec/dmd)
PsAuxResult(root,724,0.0,0.1,4307352,4580,??,Ss,1:22PM,0:00.21,/System/Library/PrivateFrameworks/CoreSymbolication.framework/coresymbolicationd)
PsAuxResult(root,723,0.0,0.0,4305208,916,??,Ss,1:22PM,0:00.01,/System/Library/Frameworks/CoreMediaIO.framework/Versions/A/XPCServices/com.apple.cmio.registerassistantservice.xpc/Contents/MacOS/com.apple.cmio.registerassistantservice)
````

* Running programmatically `wc -l files/story.txt`

````
3 files/story.txt
````

* Running programmatically `grep Hello files/story.txt`

````
Hello world
Hello tomorrow
````

* Running programmatically `cat files/story.txt`

````
Hello world
Hello tomorrow
Connecting people
Moto
````

* Running programatically `ps | wc -l` and `ps aux | wc -l` returned results `2` and `353` respectively