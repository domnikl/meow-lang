plugins {
    java
    kotlin("jvm") version "1.3.72"
    antlr
}

group = "org.meow"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation(kotlin("stdlib-jdk8"))
    implementation("org.ow2.asm:asm:8.0.1")
    antlr("org.antlr:antlr4:4.5")
    testImplementation("junit", "junit", "4.12")
    testImplementation("io.mockk:mockk:1.9.3")
}

configure<JavaPluginConvention> {
    sourceCompatibility = JavaVersion.VERSION_1_8
}

tasks {
    compileKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
    compileTestKotlin {
        kotlinOptions.jvmTarget = "1.8"
    }
}

tasks.generateGrammarSource {
    arguments = arguments + listOf("-visitor", "-long-messages", "-package", "org.meow")
}
