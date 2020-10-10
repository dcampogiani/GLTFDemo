package com.danielecampogiani.gltfdemo.shared

interface Api {

    suspend fun fetchRawData(): String

    object Implementation : Api by RemoteApi()

}