package core

import java.util.*

class ConfigLoader(profile: String) {
    private val props = Properties()

    init {
        javaClass.getResourceAsStream("/$profile.properties").use {
            props.load(it)
        }
    }

    fun pageUrl(): String {
        return props.getProperty("page.url")
    }
}
