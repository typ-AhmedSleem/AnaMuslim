package com.typ.muslim.core

annotation class Buggy(val reason: String = "")

annotation class NeedsTesting(val reason: String = "")

annotation class BackwardCompatible(val reason: String = "")

annotation class NotBackwardCompatible(val reason: String = "")