package com.danielecampogiani.gltfdemo.shared

import kotlin.test.Test
import kotlin.test.assertTrue

class DummyTest {

    @Test
    fun success() {
        assertTrue { Platform().platform.startsWith("Android ") }
    }
}
