package com.compchallenge.record.dependency.api

import enumeratum.{Enum, EnumEntry}
import com.compchallenge.record.businessobject.RecordBO
import com.compchallenge.record.dependency.api.RecordApi.GetRecordsSpecifiers

import scala.concurrent.Future

trait RecordApi {
  def createRecord(record: RecordBO): Future[RecordBO]

  def createRecords(recordList: List[RecordBO]): Future[List[RecordBO]]

  def getRecords(specifiers: GetRecordsSpecifiers): Future[Seq[RecordBO]]
}

object RecordApi {
  sealed trait SearchType
  
  case class GenderSearchType(isAsc: Boolean) extends SearchType
  case class LastNameSearchType(isAsc: Boolean) extends SearchType
  case class BirthDateSearchType(isAsc: Boolean) extends SearchType

  case class GetRecordsSpecifiers(maybeGender: Option[GenderSearchType] = None, 
  maybeLastName: Option[LastNameSearchType] = None, 
  maybeBirthDate: Option[BirthDateSearchType] = None)
}