package com.bonborunote.niconicoviewer.network

class NicoNicoException(val status: Int, val code: String, message: String) : Exception(message)
