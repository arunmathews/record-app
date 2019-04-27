package com.compchallenge.record

import org.scalatra.test.scalatest._

class RecordServletTests extends ScalatraFunSuite {

  addServlet(classOf[RecordServlet], "/*")

  test("GET / on RecordServlet should return status 200") {
    get("/") {
      status should equal (200)
    }
  }

}
