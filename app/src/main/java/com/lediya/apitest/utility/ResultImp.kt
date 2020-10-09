package com.lediya.apitest.utility

class ResultImp(var resultType: ResultType) {
    var message: String? = ""

    constructor(resultType: ResultType, message: String?) : this(resultType) {
        this.message = message
    }
}