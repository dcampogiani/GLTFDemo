package com.danielecampogiani.gltfdemo.shared

interface Api {

    suspend fun fetchRawData(): String

    suspend fun download(uri: String): ByteArray

    object Implementation : Api by RemoteApi()

}