package com.compchallenge.record.database.tablemapping

import slick.jdbc.JdbcProfile

trait TablesWithDb extends Tables {
  val profile: JdbcProfile

  import profile.api._

  val db: Database

}
