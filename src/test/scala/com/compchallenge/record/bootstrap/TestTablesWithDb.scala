package com.compchallenge.record.bootstrap

import com.mchange.v2.c3p0.ComboPooledDataSource
import com.compchallenge.record.flyway.FlywayMigration
import com.compchallenge.record.database.tablemapping.H2TablesWithDb
import org.scalatest._

trait TestTablesWithDb extends BeforeAndAfterAll {
  this: Suite => 
    val dbUrl = "jdbc:h2:mem:test;DB_CLOSE_DELAY=-1"
    val dbUser = "root"
    val dbPassword = ""

    FlywayMigration.migrate(dbUrl, dbUser, dbPassword)
    val cpds = new ComboPooledDataSource
    cpds.setJdbcUrl(dbUrl)
    cpds.setUser(dbUser)
    cpds.setPassword(dbPassword)
    val tablesWithDb = new H2TablesWithDb(cpds)

    override def afterAll(): Unit = {
      try closeDbConnection(tablesWithDb, cpds)
      finally super.afterAll()
    }
    
    private def closeDbConnection(tablesWithDb: H2TablesWithDb, cpds: ComboPooledDataSource) {
      tablesWithDb.db.close()
      cpds.close()
    }
}