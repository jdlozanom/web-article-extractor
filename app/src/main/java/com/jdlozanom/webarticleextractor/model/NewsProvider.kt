package com.jdlozanom.webarticleextractor.model

class NewsProvider(
    val domain: String,
    val headLineCss: String,
    val subtitleCss: String,
    val articleCss: String,
    val imageCss: String,
    val imageSrcAttr: String,
    val twitterAccount: String,
    val instagramAccount: String,
    val facebookAccount: String
)