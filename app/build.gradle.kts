import org.jetbrains.kotlin.gradle.tasks.KotlinCompile

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
    maven("https://maven.aliyun.com/repository/public/")
    maven("https://maven.aliyun.com/repository/spring/")
    mavenLocal()
    mavenCentral()
    maven("https://repo.spring.io/milestone")
    maven("https://repo.spring.io/snapshot")
    maven("https://repo.repsy.io/mvn/nielinjie/default")
}

dependencies {
    implementation(project(":core"))
    implementation(project(":custom"))

    implementation("org.springframework.boot:spring-boot-starter")
    implementation("org.springframework.boot:spring-boot-starter-web")
    implementation("org.springframework.boot:spring-boot-starter-websocket")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-api:2.5.0")
    implementation("org.springdoc:springdoc-openapi-starter-webmvc-ui:2.5.0")

    implementation("org.jetbrains.kotlin:kotlin-reflect")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.5.0")

    implementation("net.sf.supercsv:super-csv:2.4.0")
    implementation("io.arrow-kt:arrow-core-jvm:1.2.4")

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

task("copyDocs", type = Copy::class) {
    from("../README.MD")
    into( "src/main/resources/markdowns")
}
tasks.named("processResources") {
    dependsOn("copyDocs")
    dependsOn("copyFrontFiles")
}

task("copyFrontFiles", type = Copy::class) {
    from("../front/dist")
    into("src/main/resources/static") //TODO get path of app subproject.


    // dependsOn("yarnBuild") 在构建环境没有yarn，所以在本地构建，结果上传到git。
}