package com.compchallenge.record.service

import org.scalatra._
import org.slf4j.LoggerFactory

/**
 * Mixin error specific logic to servlets
 */
trait ErrorSupportMixin {
  self: ScalatraBase =>

  def haltInvalidRequest[T: Manifest](maybeT: Option[T]): Nothing = {
    maybeT.fold(halt(400, ""))(t => halt(400, t))
  }

  private def createBody(message: String): String = {
    if (isDevelopmentMode) {
      message
    }
    else {
      ""
    }
  }
}
