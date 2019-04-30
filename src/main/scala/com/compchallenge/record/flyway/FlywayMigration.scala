package com.compchallenge.record.flyway

import org.flywaydb.core.Flyway

object FlywayMigration {
  def migrate(url: String, user: String, password: String): Unit = {
    val flyway = Flyway.configure().dataSource(url, user, password).locations("classpath:com/compchallenge/record/database/migration").load()

    flyway.migrate()
  }
}