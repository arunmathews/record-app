package com.compchallenge.record.validation

case class BOValidationFailure(failureCode: BOValidationFailureCode,
                               displayString: String)
