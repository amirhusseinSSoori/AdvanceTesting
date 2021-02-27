package com.amirhusseinsoori.adavancetesting.data.remote.response

data class ImageResponse(
    val hits:List<ImageResult>,
    val total:Int,
    val totalHint:Int
)