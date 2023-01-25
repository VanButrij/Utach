package com.gpd.utach.models

data class TokensModel(
    val accessToken: String,
    val refreshToken: String,
    val idToken: String
)