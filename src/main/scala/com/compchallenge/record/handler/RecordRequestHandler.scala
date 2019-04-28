package com.compchallenge.record.handler

import com.compchallenge.record.businessobject._
import com.compchallenge.record.dependency.api._
import com.compchallenge.record.businessobject._

import scala.concurrent.{ExecutionContext, Future}

class RecordRequestHandler(recordApi: RecordApi)(implicit val ec: ExecutionContext) {
  def createRecord(record: RecordBO): Future[RecordBO] = {
    recordApi.createRecord(record)
  }
}