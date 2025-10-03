package com.example.nasa

data class ApodResponse(
    val date: String,
    val explanation: String,
    val title: String,
    val url: String,
    val media_type: String
)