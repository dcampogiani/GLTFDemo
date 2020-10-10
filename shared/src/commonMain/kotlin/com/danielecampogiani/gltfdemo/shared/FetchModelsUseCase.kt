package com.danielecampogiani.gltfdemo.shared

class FetchModelsUseCase constructor(
    private val api: Api,
    private val parser: Parser = Parser.Implementation
) {

    suspend operator fun invoke(): List<Model> = with(parser) { api.fetchRawData().parse() }

}