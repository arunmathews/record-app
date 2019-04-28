package com.compchallenge.record.dependency.impl

import com.compchallenge.record.database.tablemapping.Tables._
import com.compchallenge.record.database.tablemapping.TablesWithDb

import com.compchallenge.record.businessobject.RecordBO

import com.compchallenge.record.dependency.api.RecordApi

import scala.concurrent.{ExecutionContext, Future}

class RecordApiDBImpl(val tablesWithDb: TablesWithDb)(implicit val ec: ExecutionContext) extends RecordApi {
  import tablesWithDb.profile.api._

  private val db = tablesWithDb.db

  def createRecord(record: RecordBO): Future[RecordBO] = {
    Future.successful(RecordBO())
  }
}