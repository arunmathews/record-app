package com.compchallenge.record.service

import org.scalatra.{AsyncResult, FutureSupport}

import scala.concurrent.{ExecutionContext, Future}
import com.compchallenge.record.handler.RecordRequestHandler
import com.compchallenge.record.extractor.RecordExtractor
import com.compchallenge.record.businessobject._
import com.compchallenge.record.validation.BOValidationFailure
import scala.concurrent.{ExecutionContext, Future}

class RecordServlet(reqHandler: RecordRequestHandler) extends RecordStack 
  with JsonSupportMixin
  with ErrorSupportMixin
  with FutureSupport {
    post("/records") {
      new AsyncResult() {
        override val is = {
          RecordExtractor.extractRecord(request.body).fold(
              haltOnFailure,
              record => reqHandler.createRecord(record).map(res => RecordOperationOutputBO(record = Option(res))))
        }
      }
    }

    get("/records/:searchtype") {
      new AsyncResult() {
        override val is = {
          val maybeSearchType = params.get("searchtype")
          RecordExtractor.extractSearchType(maybeSearchType).fold(
            haltOnFailure,
            searchType => reqHandler.getRecords(searchType).map(res => RecordOperationOutputBO(records = Option(res)))
          )
        }
      }
    }

    def haltOnFailure(failure: BOValidationFailure): Nothing = {
        haltInvalidRequest(Option(RecordOperationOutputBO(validationFailure = Option(failure))))
      }
    
      override implicit protected def executor: ExecutionContext = ExecutionContext.Implicits.global
  }