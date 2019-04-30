package com.compchallenge.record.handler

import org.scalatest.{AsyncFlatSpec, Matchers}
import com.compchallenge.record.businessobject.RecordBO.GenderType
import com.compchallenge.record.businessobject.RecordBO
import com.compchallenge.record.handler.RecordRequestHandler.SearchTypeInput
import com.compchallenge.record.bootstrap.handler.RecordRequestHandlerTestSupport
import org.joda.time.LocalDate

class RecordRequestHandlerSpec extends AsyncFlatSpec 
  with RecordRequestHandlerTestSupport with Matchers {

  it should "create record successfully" in {
    val recordBO = RecordBO("One", "Two", GenderType.Female, "Blue", new LocalDate(1985, 11, 15))

    val testSeq = for {
      createdRec <- recordHandler.createRecord(recordBO)
    } yield (recordBO, createdRec)

    testSeq.map {
      case (origRec, createdRec) =>
        assert(origRec === createdRec)
    }
  }

  it should "create and get records successfully" in {
    val record1BO = RecordBO("Two", "One", GenderType.Female, "Test2", new LocalDate(1985, 11, 15))
    val record2BO = RecordBO("Three", "One", GenderType.Male, "Test2", new LocalDate(1982, 12, 12))

    val testSeq = for {
      _ <- recordHandler.createRecords(List(record2BO, record1BO))
      genderAscRecs <- recordHandler.getRecords(SearchTypeInput.GenderAsc)
      lastNameDescRecs <- recordHandler.getRecords(SearchTypeInput.LastNameDesc)
    } yield (List(record1BO, record2BO), genderAscRecs.filter(_.favColor == "Test2"), 
        lastNameDescRecs.filter(_.favColor == "Test2"))

    testSeq.map {
      case (origRecs, genderAscRecs, lastNameDescRecs) =>
        assert(origRecs.sortBy(_.gender.entryName) === genderAscRecs.toList)
        assert(origRecs.sortBy(_.lastName).reverse === lastNameDescRecs.toList)
    }
  }
}