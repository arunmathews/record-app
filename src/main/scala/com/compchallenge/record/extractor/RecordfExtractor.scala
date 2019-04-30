package com.compchallenge.record.extractor

import com.compchallenge.record.businessobject.RecordBO
import com.compchallenge.record.validation.BOValidationFailure
import com.compchallenge.record.validation.BOValidationFailureCode
import com.compchallenge.record.datetime.JsonDateFormats
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput

/**
  * Extracts records from inputs formatted in different ways
  */
object RecordExtractor {
  val wordParser = """\w+""".r

  def extractRecord(input: String): Either[BOValidationFailure, RecordBO] = {
    try {
      val wordList = wordParser.findAllIn(input).toList
        wordList match {
          case ln :: fn :: g :: fc :: m :: d :: y :: Nil =>
            val gender = RecordBO.GenderType.withName(g)
            val dateOfBirth = JsonDateFormats.dateFormatter.parseLocalDate(s"$m/$d/$y")
            Right(RecordBO(ln, fn, gender, fc, dateOfBirth))
          case _ =>
            Left(BOValidationFailure(BOValidationFailureCode.Invalid, s"Invalid input - $input"))
      }
    }
    catch {
      case e: Exception =>
        Left(BOValidationFailure(BOValidationFailureCode.Invalid, s"Invalid input - $input"))
    }
  }

  def extractSearchType(maybeSearchType: Option[String]): Either[BOValidationFailure, SearchTypeInput] = {
    maybeSearchType.fold({
      val res: Either[BOValidationFailure, SearchTypeInput] = 
        Left(BOValidationFailure(BOValidationFailureCode.Missing, s"Missing search type"))
      res
    })(
      searchTypeString => {
        if (searchTypeString == "gender") {
          Right(SearchTypeInput.GenderAsc)
        }
        else if(searchTypeString == "birthdate") {
          Right(SearchTypeInput.BirthDateAsc)
        }
        else if(searchTypeString == "name") {
          Right(SearchTypeInput.LastNameDesc)
        }
        else {
          Left((BOValidationFailure(BOValidationFailureCode.Invalid, s"Invalid search type - $searchTypeString")))
        }
      })
  }

  def extractRecords(input: List[String]): Either[List[BOValidationFailure], List[RecordBO]] = {
    val maybeRecords = input.map(line => extractRecord(line))
    val (failures, records) = maybeRecords.foldLeft((List.empty[BOValidationFailure], List.empty[RecordBO]))({
        case ((failList, recordList), maybeRecord) =>
        maybeRecord.fold(failure => (failure :: failList, recordList), record => (failList, record :: recordList))
    })

    if (failures.nonEmpty) {
      Left(failures)
    }
    else {
      Right(records)
    }
  }
}