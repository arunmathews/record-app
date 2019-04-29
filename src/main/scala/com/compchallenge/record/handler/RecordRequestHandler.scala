package com.compchallenge.record.handler

import enumeratum.{Enum, EnumEntry}
import com.compchallenge.record.businessobject._
import com.compchallenge.record.dependency.api._
import com.compchallenge.record.businessobject._
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput
import com.compchallenge.record.dependency.api.RecordApi._

import scala.concurrent.{ExecutionContext, Future}
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.GenderAscLastNameAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.GenderAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.BirthDateAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.LastNameDesc

class RecordRequestHandler(recordApi: RecordApi)(implicit val ec: ExecutionContext) {
  def createRecord(record: RecordBO): Future[RecordBO] = {
    recordApi.createRecord(record)
  }

  def createRecords(records: List[RecordBO]): Future[List[RecordBO]] = {
    recordApi.createRecords(records)
  }

  def getRecords(searchType: SearchTypeInput): Future[Seq[RecordBO]] = {
    val specifiers = searchType match {
      case GenderAscLastNameAsc => 
        GetRecordsSpecifiers(maybeGender = Option(GenderSearchType(true)), 
          maybeLastName = Option(LastNameSearchType(true)))
      case GenderAsc =>
        GetRecordsSpecifiers(maybeGender = Option(GenderSearchType(true)))
      case BirthDateAsc => 
        GetRecordsSpecifiers(maybeBirthDate = Option(BirthDateSearchType(true)))
      case LastNameDesc => 
        GetRecordsSpecifiers(maybeLastName = Option(LastNameSearchType(false)))
    }

    recordApi.getRecords(specifiers)
  }
}

object RecordRequestHandler {
  sealed trait SearchTypeInput extends EnumEntry with EnumEntry.Snakecase

  object SearchTypeInput extends Enum[SearchTypeInput] {
    val values = findValues

    case object GenderAscLastNameAsc extends SearchTypeInput
    case object GenderAsc extends SearchTypeInput
    case object BirthDateAsc extends SearchTypeInput
    case object LastNameDesc extends SearchTypeInput
  }
}