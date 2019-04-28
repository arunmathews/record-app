package com.compchallenge.record.businessobject

import com.compchallenge.record.validation.BOValidationFailure

case class RecordOperationOutputBO(record: Option[RecordBO] = None, 
                                   records: Option[Seq[RecordBO]] = None, 
                                   validationFailure: Option[BOValidationFailure] = None)