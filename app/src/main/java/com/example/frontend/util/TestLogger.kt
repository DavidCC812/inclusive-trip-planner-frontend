package com.example.frontend.util

object TestLogger : Logger {
    override fun d(tag: String, message: String) {}
    override fun e(tag: String, message: String, throwable: Throwable?) {}
}
