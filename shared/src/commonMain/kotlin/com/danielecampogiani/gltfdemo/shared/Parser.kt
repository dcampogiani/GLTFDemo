package com.danielecampogiani.gltfdemo.shared

interface Parser {

    fun String.parse(): List<Model>

    companion object Implementation : Parser {

        private val REGEX = """!\[]\((\w+)\/\w+\/(\w+)\.(\w+)\)""".toRegex()

        private const val SCREENSHOT_BASE_URL =
            "https://github.com/KhronosGroup/glTF-Sample-Models/raw/master/2.0/"
        private const val MODEL_BASE_URL =
            "https://raw.githubusercontent.com/KhronosGroup/glTF-Sample-Models/master/2.0/"

        override fun String.parse(): List<Model> {

            val results = REGEX.findAll(this)

            return results.map { result ->
                val values = result.groupValues
                val name = values[1]
                Model(
                    name = name,
                    screenshot = "$SCREENSHOT_BASE_URL$name/screenshot/${values[2]}.${values[3]}",
                    url = "$MODEL_BASE_URL$name/glTF/$name.gltf"
                )
            }.toList()
        }

    }
}