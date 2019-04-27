package com.compchallenge.record.service

import org.scalatra.Ok

/**
  * Basic ping servlet to see if service is up
  */
class PingServlet() extends RecordStack {
  get("/v1/ping") {
    //No op - returns 200. Validates that the service is up
    Ok("pong")
  }
}
