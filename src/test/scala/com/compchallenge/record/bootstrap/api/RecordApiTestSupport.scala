package com.compchallenge.record.bootstrap.api

import com.compchallenge.record.bootstrap.TestTablesWithDb
import com.compchallenge.record.dependency.impl.RecordApiDBImpl
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global

trait RecordApiTestSupport extends TestTablesWithDb {
  this: Suite =>

  val recordApi = new RecordApiDBImpl(tablesWithDb)
}