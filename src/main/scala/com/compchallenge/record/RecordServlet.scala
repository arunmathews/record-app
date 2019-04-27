package com.compchallenge.record

import org.scalatra._

class RecordServlet extends ScalatraServlet {

  get("/") {
    views.html.hello()
  }

}
