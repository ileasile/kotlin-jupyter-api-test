rootProject.name = "kotlin-jupyter-api-test"

pluginManagement {
    val kotlinVersion: String by settings
    val shadowJarVersion: String by settings
    val ktlintGradleVersion: String by settings
    val jupyterApiVersion: String by settings

    repositories {
        jcenter()
        mavenLocal()
        mavenCentral()
        gradlePluginPortal()
        maven("https://kotlin.bintray.com/kotlin-datascience/")
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.github.johnrengelman.shadow" -> useModule("com.github.jengelman.gradle.plugins:shadow:$shadowJarVersion")
                "org.jlleitschuh.gradle.ktlint" -> useModule("org.jlleitschuh.gradle:ktlint-gradle:$ktlintGradleVersion")
                "org.jetbrains.kotlinx.jupyter.api.plugin" -> useModule("org.jetbrains.kotlinx.jupyter:kotlin-jupyter-api-gradle-plugin:$jupyterApiVersion")
            }
        }
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowJarVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintGradleVersion
        id("org.jetbrains.kotlinx.jupyter.api.plugin") version jupyterApiVersion
    }
}
