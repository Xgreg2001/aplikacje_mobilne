package com.example.rest

import com.google.gson.JsonElement

data class MathResponse(
    val operation: String,
    val expression: String,
    val result: JsonElement
)
