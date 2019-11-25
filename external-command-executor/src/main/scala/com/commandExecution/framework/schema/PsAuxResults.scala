package com.commandExecution.framework.schema

case class PsAuxResults(user: String,
                        pId: Long,
                        pctCPU: Double,
                        pctMem: Double,
                        vsz: Long,
                        rss: Long,
                        tt: String,
                        stat: String,
                        started: String,
                        time: String,
                        command: String)
