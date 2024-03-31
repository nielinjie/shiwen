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
	mavenCentral()
	maven("https://repo.spring.io/milestone")
	maven("https://repo.spring.io/snapshot")
	maven("https://packages.aliyun.com/maven/repository/2331954-snapshot-Z6hK35/"){
		credentials {
			username = project.findProperty("aliMUser") as String? ?: System.getenv("ALIMUSER")
			password = project.findProperty("aliMPass") as String? ?: System.getenv("ALIMPASS")
		}
	}

}

dependencies {
	implementation("org.springframework.boot:spring-boot-starter")
	implementation("org.springframework.boot:spring-boot-starter-web")
	implementation("org.jetbrains.kotlin:kotlin-reflect")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-core:1.6.3")
	implementation("org.jetbrains.kotlinx:kotlinx-serialization-json:1.6.3")
	implementation("xyz.nietongxue:common:1.0-SNAPSHOT")

	implementation(platform("org.springframework.ai:spring-ai-bom:0.8.1-SNAPSHOT"))
	// Replace the following with the starter dependencies of specific modules you wish to use
	implementation("org.springframework.ai:spring-ai-openai")
	implementation("org.springframework.ai:spring-ai-ollama")
//	implementation("org.springframework.ai:spring-ai-openai-spring-boot-starter")
//	implementation("org.springframework.ai:spring-ai-ollama-spring-boot-starter")
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


task("copyFiles", type = Copy::class) {
	from("../vue-project/dist")
	into("src/main/resources/static")
}
task("yarnBuild", type = Exec::class) {
	workingDir("../vue-project")
	commandLine("yarn", "build")
}