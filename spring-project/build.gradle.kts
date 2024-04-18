import org.gradle.internal.impldep.org.h2.store.fs.split.FilePathSplit
import org.jetbrains.kotlin.gradle.tasks.KotlinCompile
import java.io.BufferedReader
import java.io.InputStreamReader

plugins {
    id("org.springframework.boot") version "3.2.3"
    id("io.spring.dependency-management") version "1.1.4"
    kotlin("jvm") version "1.9.22"
    kotlin("plugin.spring") version "1.9.22"
    kotlin("plugin.serialization") version "1.9.22"

}

group = "cloud.qingyangyunyun"
version = "0.0.1-SNAPSHOT"

java {
    sourceCompatibility = JavaVersion.VERSION_21
}

repositories {
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.repsy.io/mvn/nielinjie/default")
}

dependencies {
    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("net.sf.supercsv:super-csv:2.4.0")
    implementation("xyz.nietongxue:common-jvm:1.0-SNAPSHOT")
    implementation("cloud.qingyangyunyun:docbaseK-jvm:1.0-SNAPSHOT")
    implementation(platform("org.springframework.ai:spring-ai-bom:0.8.1-SNAPSHOT"))
    // Replace the following with the starter dependencies of specific modules you wish to use
    implementation("org.springframework.ai:spring-ai-openai")
    implementation("org.springframework.ai:spring-ai-ollama")
    implementation("net.sourceforge.htmlunit:htmlunit:2.70.0")

    testImplementation("io.kotest:kotest-runner-junit5:5.6.2")
    testImplementation("io.kotest.extensions:kotest-extensions-spring:1.1.3")
    testImplementation("org.springframework.boot:spring-boot-starter-test")
}

tasks.withType<KotlinCompile> {
    kotlinOptions {
        freeCompilerArgs += "-Xjsr305=strict"
        jvmTarget = "21"
    }
}

tasks.withType<Test> {
    useJUnitPlatform()
}

fun executeCommand(command: String): String {
    val process = ProcessBuilder(*command.split(" ").toTypedArray()).start()
    val reader = BufferedReader(InputStreamReader(process.inputStream))
    return reader.readText().trim()
}
task("copyFiles", type = Copy::class) {
    from("../vue-project/dist")
    into("src/main/resources/static")
}
task("yarnBuild", type = Exec::class) {
    val yarn = executeCommand("which yarn")
    workingDir("../vue-project")
    commandLine(yarn, "build")
}