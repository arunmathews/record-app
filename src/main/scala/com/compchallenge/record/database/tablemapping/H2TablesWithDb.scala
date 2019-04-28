package com.compchallenge.record.database.tablemapping

import com.mchange.v2.c3p0.ComboPooledDataSource

/**
  * H2 specific slick profile
  */
class H2TablesWithDb(cpds: ComboPooledDataSource) extends TablesWithDb {
  override val profile = slick.jdbc.H2Profile

  import profile.api._

  override val db = Database.forDataSource(cpds,None)
}
