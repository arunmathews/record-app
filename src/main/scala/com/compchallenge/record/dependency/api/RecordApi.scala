package com.compchallenge.record.dependency.api

import com.compchallenge.record.businessobject.RecordBO
import scala.concurrent.Future

trait RecordApi {
  def createRecord(record: RecordBO): Future[RecordBO]
}