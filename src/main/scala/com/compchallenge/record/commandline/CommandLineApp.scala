package com.compchallenge.record.commandline

import com.compchallenge.record.flyway.FlywayMigration
import com.mchange.v2.c3p0.ComboPooledDataSource
import org.joda.time.DateTime

import com.compchallenge.record.datetime.JsonDateFormats
import com.compchallenge.record.database.tablemapping.H2TablesWithDb
import com.compchallenge.record.dependency.impl.RecordApiDBImpl
import com.compchallenge.record.extractor.RecordExtractor
import com.compchallenge.record.handler.RecordRequestHandler
import com.compchallenge.record.validation.BOValidationFailure
import com.compchallenge.record.validation.BOValidationFailureCode
import com.compchallenge.record.businessobject.RecordBO
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.GenderAscLastNameAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.GenderAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.BirthDateAsc
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput.LastNameDesc

import java.io.{File, PrintWriter}

import scala.concurrent.ExecutionContext.Implicits.global
import scala.concurrent.{Future, Promise}
import scala.concurrent._
import scala.concurrent.duration.Duration
import scala.util.{Try => UTry, Failure => UFailure, Success => USuccess}

/**
  * Entry point for command line call to parse input files and create sorted output files
  */
object CommandLineApp extends App {
  if (args.length < 2) {
    println(s"Not enough args for extracting and returning records. Need 1 or more input file and 1 output file location")
  }
  else {
    val outputLoc = args.last
    val inputFilePaths = args.dropRight(1)  
    val dbUrl = "jdbc:h2:mem:cmd;DB_CLOSE_DELAY=-1"
    val dbUser = "root"
    val dbPassword = ""

    FlywayMigration.migrate(dbUrl, dbUser, dbPassword)
    val cpds = new ComboPooledDataSource
    cpds.setJdbcUrl(dbUrl)
    cpds.setUser(dbUser)
    cpds.setPassword(dbPassword)
    val tablesWithDb = new H2TablesWithDb(cpds)
    val recordApi = new RecordApiDBImpl(tablesWithDb)
    val recordHandler = new RecordRequestHandler(recordApi)
    val storeRecordsFutures = inputFilePaths.toList.map(path => {
      val maybeRecordLines = readFileToLines(path)
      maybeRecordLines.fold(failure => Future.successful(Left(List(failure))), succ => {
        val maybeRecords = RecordExtractor.extractRecords(succ)
        maybeRecords.fold(failures => Future.successful(Left(failures)), 
        succ => recordHandler.createRecords(succ).map(Right.apply))
      })
    })
    val resultFut = for {
      maybeStoreRecords <- Future.sequence(storeRecordsFutures)
      maybeGetRecords <- getRecords(maybeStoreRecords, recordHandler)
      _ = writeRecordsToFile(maybeGetRecords, outputLoc)
    } yield ()
    val prom = Promise[UTry[Unit]]
    resultFut.onComplete(prom.success)

    Await.result(prom.future, Duration.Inf) match {
      case USuccess(_) => println(s"Successfully wrote records in desired formats")
      case UFailure(err) => println(s"Failure: $err")
    }
  }

  private def readFileToLines(path: String): Either[BOValidationFailure, List[String]] = {
    val inputIter = scala.io.Source.fromFile(new File(path))
    try {
      Right(inputIter.getLines().toList)
    }
    catch {
      case e: Exception =>
        Left(BOValidationFailure(BOValidationFailureCode.Invalid, s"Invalid file path - $path"))
    }
    finally {
      inputIter.close()
    }
  }

  private def getRecords(maybeStoreRecords: List[Either[List[BOValidationFailure], List[RecordBO]]], 
                         recordHandler: RecordRequestHandler): 
                         Future[Either[List[BOValidationFailure], (Seq[RecordBO], Seq[RecordBO], Seq[RecordBO])]] = {
    if (maybeStoreRecords.exists(_.isLeft)) {
      val failures = for {
        eitherRecord <- maybeStoreRecords
        if eitherRecord.isLeft
      } yield eitherRecord.left.get
      Future.successful(Left(failures.flatten))
    }
    else {
      for {
        genderLastNameSortedRecs <- recordHandler.getRecords(SearchTypeInput.GenderAscLastNameAsc)
        dobSortedRecs <- recordHandler.getRecords(SearchTypeInput.BirthDateAsc)
        lastNameSortedRecs <- recordHandler.getRecords(SearchTypeInput.LastNameDesc)
      } yield Right((genderLastNameSortedRecs, dobSortedRecs, lastNameSortedRecs))
    }
  }

  private def writeRecordsToFile(maybeRecords: Either[List[BOValidationFailure], (Seq[RecordBO], Seq[RecordBO], Seq[RecordBO])], 
                                 outLoc: String): Unit = {
    val dt = JsonDateFormats.dateTimeFormatter.print(new DateTime)
    maybeRecords.fold(failures => {
      val failureOutPath = s"$outLoc/Failures_${dt}_Res.txt"
      writeToFile(failureOutPath, failures.map(_.toString))
    }, 
    records => {
      val (genderRecs, dobRecs, lastNameRecs) = records
      val genderOutPath = s"$outLoc/Output_${dt}_${SearchTypeInput.GenderAscLastNameAsc.entryName}.csv"
      writeToFile(genderOutPath, genderRecs.map(recordToString))
      val dobOutPath = s"$outLoc/Output_${dt}_${SearchTypeInput.BirthDateAsc.entryName}.csv"
      writeToFile(dobOutPath, dobRecs.map(recordToString))
      val lastNameOutPath = s"$outLoc/Output_${dt}_${SearchTypeInput.LastNameDesc.entryName}.csv"
      writeToFile(lastNameOutPath, lastNameRecs.map(recordToString))
    })
  }

  private def recordToString(rec: RecordBO): String = {
    s"${rec.lastName}, ${rec.firstName}, ${rec.gender}, ${rec.favColor}, ${JsonDateFormats.dateFormatter.print(rec.dateOfBirth)}"
  }

  private def writeToFile(outFilePath: String, lines: Seq[String]): Unit = {
    val outFile = new File(outFilePath)
    val outputStream = new PrintWriter(outFile)
    try {
      lines.foreach(outputStream.println)
    }
    catch {
      case e: Exception =>
        println(s"Got an exception when writing records to file - $e, out path - $outFilePath")
    }
    finally {
      outputStream.close()
    }
    println(s"Successfully wrote to file - $outFilePath")
  }
}