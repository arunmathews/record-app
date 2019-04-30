package com.compchallenge.record.bootstrap.handler

import com.compchallenge.record.handler.RecordRequestHandler
import com.compchallenge.record.bootstrap.api.RecordApiTestSupport
import org.scalatest._

import scala.concurrent.ExecutionContext.Implicits.global

trait RecordRequestHandlerTestSupport extends RecordApiTestSupport {
  this: Suite => 
    val recordHandler = new RecordRequestHandler(recordApi)
}