package com.danielecampogiani.gltfdemo.shared

import io.ktor.client.*
import io.ktor.client.request.*

internal class RemoteApi : Api {

    private val client = HttpClient()

    private val address =
        "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/README.md"

    override suspend fun fetchRawData(): String = client.get(address)
    override suspend fun download(uri: String): ByteArray = client.get(uri)
}