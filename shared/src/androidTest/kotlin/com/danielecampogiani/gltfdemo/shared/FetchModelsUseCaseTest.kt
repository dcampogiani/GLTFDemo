package com.danielecampogiani.gltfdemo.shared

import io.mockk.coEvery
import io.mockk.every
import io.mockk.mockk
import org.junit.Rule
import org.junit.Test
import kotlin.test.assertEquals

class FetchModelsUseCaseTest {

    @get:Rule //TODO I'm using Junit because I was not able to find the equivalent of a rule in kotlin.test
    val coroutineRule = CoroutineRule()

    private val api: Api = mockk()
    private val parser: Parser = mockk()
    private val sut = FetchModelsUseCase(api, parser)

    @Test
    fun invoke() = coroutineRule.runBlockingTest {
        val data = listOf(Model("name", "screenshot", "url"))
        coEvery { api.fetchRawData() } returns "rawData"
        with(parser) {
            every { "rawData".parse() } returns data
        }

        val result = sut.invoke()
        assertEquals(data, result)
    }
}