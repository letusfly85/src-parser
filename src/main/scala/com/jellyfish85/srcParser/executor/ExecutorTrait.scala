package com.jellyfish85.srcParser.executor

import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.utils.SrcParserProp

trait ExecutorTrait {

  var db: DatabaseManager = null

  val parserProp: SrcParserProp = new SrcParserProp

  def databaseInitialize() {
    db = new DatabaseManager

    db.connect

  }

  def databaseFinalize() {db.jClose}

}