import kotlin.Suppress
import com.jfrog.bintray.gradle.BintrayExtension
import java.util.Date

plugins {
    kotlin("jvm")
    `maven-publish`
    id("org.jetbrains.dokka") version "1.4.10.2"
    id("com.jfrog.bintray") version "1.8.5"
    kotlin("jupyter.api")
}

group = "org.jetbrains.test.kotlinx.jupyter.api"
version = "0.0.18"

val jupyterApiVersion: String by project

repositories {
    mavenCentral()
    mavenLocal()
    jcenter()
}

dependencies {
    compileOnly(kotlin("stdlib"))
    /*compileOnly("org.jetbrains.kotlinx.jupyter:kotlin-jupyter-api:$jupyterApiVersion"){
        exclude("org.jetbrains.kotlin")
    }*/

    testImplementation(kotlin("test"))

    val junitVersion = "5.6.1"
    testImplementation("org.junit.jupiter:junit-jupiter-api:$junitVersion")
    testRuntimeOnly("org.junit.jupiter:junit-jupiter-engine:$junitVersion")
}

tasks {
    test {
        useJUnitPlatform()
        testLogging {
            events("passed", "skipped", "failed")
        }
    }

    @Suppress("UNUSED_VARIABLE")
    val sourceJar by registering(Jar::class) {
        archiveClassifier.set("sources")
        from(sourceSets.main.get().allSource)
    }

    dokkaHtml {
        outputDirectory.set(File("$buildDir/dokka"))
    }

    dokkaJavadoc{
        outputDirectory.set(File(this@tasks.javadoc.get().destinationDir!!.path))
        inputs.dir("src/main/kotlin")
    }

    @Suppress("UNUSED_VARIABLE")
    val javadocJar by registering(Jar::class) {
        group = "documentation"
        dependsOn(dokkaJavadoc)
        archiveClassifier.set("javadoc")
        from(this@tasks.javadoc.get().destinationDir!!)
    }

    processJupyterApiResources {
        libraryProducers = listOf(
            "org.jetbrains.test.kotlinx.jupyter.api.Integration1",
            "org.jetbrains.test.kotlinx.jupyter.api.Integration2",
            "org.jetbrains.test.kotlinx.jupyter.api.Integration3"
        )
    }
}

publishing {
    publications {
        create<MavenPublication>("api-test") {
            artifactId = "notebook-api-test"
            groupId = project.group as String

            from(components["java"])
            artifact(tasks["sourceJar"])
            artifact(tasks["javadocJar"])
        }
    }
}

bintray {

    // property must be set in ~/.gradle/gradle.properties
    user = project.findProperty("bintray_user") as String? ?: ""
    key = project.findProperty("bintray_key") as String? ?: ""

    setPublications("api-test") //When uploading configuration files

    dryRun = false //Whether to run this as dry-run, without deploying
    publish = true // If version should be auto published after an upload

    pkg(delegateClosureOf<BintrayExtension.PackageConfig> {
        repo = "kotlin-datascience-ileasile"
        name = "kotlin-jupyter-api-test"
        userOrg = "ileasile"

        vcsUrl = "https://github.com/Kotlin/kotlin-jupyter"

        setLicenses("MIT")
        publicDownloadNumbers = true

        //Optional version descriptor
        version(delegateClosureOf<BintrayExtension.VersionConfig>{
            val projVersion = project.version as String
            name = projVersion //Bintray logical version name
            desc = "API for Kotlin Jupyter notebooks"
            released = Date().toString()
            vcsTag = "v$projVersion"
        })
    })
}
