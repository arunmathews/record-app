package com.compchallenge.record.dependency.impl

import com.compchallenge.record.database.tablemapping.Tables._
import com.compchallenge.record.database.tablemapping.TablesWithDb

import com.compchallenge.record.businessobject.RecordBO

import com.compchallenge.record.dependency.api.RecordApi
import com.compchallenge.record.dependency.api.RecordApi._
import com.compchallenge.record.dependency.api.RecordApi.GetRecordsSpecifiers
import com.compchallenge.record.datetime.DateTimeFactory

import scala.concurrent.{ExecutionContext, Future}

class RecordApiDBImpl(val tablesWithDb: TablesWithDb)(implicit val ec: ExecutionContext) extends RecordApi {
  import tablesWithDb.profile.api._

  private val db = tablesWithDb.db

  private val records = TableQuery[Record]

  override def createRecord(record: RecordBO): Future[RecordBO] = {
    val dbAction = for {
      _ <- records += RecordRow(0, record.lastName, record.firstName, record.gender.entryName, record.favColor, 
        DateTimeFactory.toSqlDate(record.dateOfBirth))
    } yield record
    
    db.run(dbAction)
  }

  override def createRecords(recordList: List[RecordBO]): Future[List[RecordBO]] = {
    val dbAction = (for {
      res <- records ++= recordList.map(record => RecordRow(0, record.lastName, record.firstName, 
      record.gender.entryName, record.favColor, DateTimeFactory.toSqlDate(record.dateOfBirth)))
    } yield res).map(_.fold(List.empty[RecordBO])(_ => recordList))

    db.run(dbAction)
  }

  override def getRecords(specifiers: GetRecordsSpecifiers): Future[Seq[RecordBO]] = {
    val query = (specifiers.maybeGender, specifiers.maybeLastName, specifiers.maybeBirthDate) match {
      case (Some(GenderSearchType(true)), Some(LastNameSearchType(true)), None) =>
        records.sortBy(r => (r.gender.asc, r.lastName.desc))  
      case (None, None, Some(BirthDateSearchType(true))) =>
        records.sortBy(_.birthDate.asc)
      case (Some(GenderSearchType(true)), None, None) =>
        records.sortBy(_.gender.asc)
      case (None, Some(LastNameSearchType(false)), None) =>
        records.sortBy(_.lastName.desc)
      case _ =>
        records
    }
    val dbAction = query.result.map(toRecordBOs)

    db.run(dbAction)
  }

  private def toRecordBOs(rows: Seq[RecordRow]): Seq[RecordBO] = {
    rows.map(row => RecordBO(row.lastName, row.firstName, RecordBO.GenderType.withName(row.gender), 
      row.favoriteColor, DateTimeFactory.toLocalDate(row.birthDate)))
  }
}