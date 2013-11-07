package com.jellyfish85.srcParser.runner

import com.jellyfish85.dbaccessor.manager.DatabaseManager
import com.jellyfish85.srcParser.utils.ApplicationProperties

import java.sql.Connection

public class BaseRunner {

    public BaseRunner() {}

    public ApplicationProperties app = new ApplicationProperties()

    private static DatabaseManager manager = new DatabaseManager()

    public static Connection getConnection() {
        return this.manager.conn()
    }

    public static void databaseInitialize() {
        println("database initialize..")
        println(".. getting connection ..")
        this.manager.connect()
    }

    public static void databaseFinalize() {
        this.manager.jCommit()
        this.manager.jClose()
    }
}
