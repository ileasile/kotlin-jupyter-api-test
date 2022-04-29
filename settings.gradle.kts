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
    }

    resolutionStrategy {
        eachPlugin {
            when (requested.id.id) {
                "com.github.johnrengelman.shadow" -> useModule("com.github.jengelman.gradle.plugins:shadow:$shadowJarVersion")
                "org.jlleitschuh.gradle.ktlint" -> useModule("org.jlleitschuh.gradle:ktlint-gradle:$ktlintGradleVersion")
                "org.jetbrains.kotlin.jupyter.api" -> useModule("org.jetbrains.kotlin:kotlin-jupyter-api-gradle-plugin:$jupyterApiVersion")
            }
        }
    }

    plugins {
        kotlin("jvm") version kotlinVersion
        id("com.github.johnrengelman.shadow") version shadowJarVersion
        id("org.jlleitschuh.gradle.ktlint") version ktlintGradleVersion
        id("org.jetbrains.kotlin.jupyter.api") version jupyterApiVersion
    }
}
