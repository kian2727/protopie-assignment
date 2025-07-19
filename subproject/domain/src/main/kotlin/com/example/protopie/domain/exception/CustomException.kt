package com.example.protopie.domain.exception

class CustomException(val value:String):RuntimeException()

class NotFoundUserException:RuntimeException()