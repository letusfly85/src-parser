package com.jellyfish85.srcParser.executor

import com.jellyfish85.dbaccessor.manager.DatabaseManager

trait ExecutorTrait {

  var db: DatabaseManager = null

  def databaseInitialize {
    db = new DatabaseManager

    db.connect

  }
}
