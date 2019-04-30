package com.compchallenge.record.extractor

import org.scalatest.{FlatSpec, Matchers}
import com.compchallenge.record.businessobject.RecordBO.GenderType
import org.joda.time.LocalDate
import com.compchallenge.record.validation.BOValidationFailureCode

class RecordExtractorSpec extends FlatSpec with Matchers {
  it should "extract record successfully" in {
    val input = "Eastland | James | Male | Blue | 10/05/1983"
    val maybeExtracted = RecordExtractor.extractRecord(input)

    assert(maybeExtracted.isRight)
    maybeExtracted.fold(failure => fail(s"Unexpected failure"),
      succ => {
        assert(succ.gender === GenderType.Male)
        assert(succ.dateOfBirth === new LocalDate(1983, 10, 5))
      })
  }

  it should "return failure for invalid input" in {
    val input = "Eastland | James | Male | Blue | 10/051983"
    val maybeExtracted = RecordExtractor.extractRecord(input)

    assert(maybeExtracted.isLeft)
    maybeExtracted.fold(failure => {
        assert(failure.failureCode === BOValidationFailureCode.Invalid)
    }, succ => {
      fail(s"Unexpected success - $succ")
    })
  }

  it should "return list of records for list of input strings" in {
    val input1 = "Eastland, James, Male, Blue, 10/05/1983"
    val input2 = "Eastland Lisa Female Pink 12/27/1987"

    val maybeExtracted = RecordExtractor.extractRecords(List(input1, input2))
    maybeExtracted.fold(failure => fail(s"Unexpected failure"),
      succ => {
        assert(succ.length == 2)
        succ match {
          case rec1 :: rec2 :: Nil =>
          assert(rec1.gender === GenderType.Female)
          assert(rec1.dateOfBirth === new LocalDate(1987, 12, 27))
          assert(rec2.gender === GenderType.Male)
          assert(rec2.dateOfBirth === new LocalDate(1983, 10, 5))
          case _ =>
            fail(s"Unexpected number of records")
        }
      })
  }
}