package com.jellyfish85.srcParser.executor

import com.jellyfish85.srcParser.utils.ApplicationProperties
import com.jellyfish85.svnaccessor.getter.SVNGetFiles
import com.jellyfish85.svnaccessor.manager.SVNManager
import org.tmatesoft.svn.core.io.SVNRepository

class RegisterSrcHeadRevision2DB {

  def run(args: Array[String]) {

    val projectNames: List[String] = ApplicationProperties.targetProjectNames

    val manager: SVNManager = new SVNManager
    val repository: SVNRepository = manager.repository

    val getter: SVNGetFiles = new SVNGet



  }

}