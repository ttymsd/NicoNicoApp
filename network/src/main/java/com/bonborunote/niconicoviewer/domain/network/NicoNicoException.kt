package com.bonborunote.niconicoviewer.domain.network

class NicoNicoException(val status: Int, val code: String, message: String) : Exception(message)
