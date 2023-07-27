plugins {
    id("java")
    id("idea")
    id("org.jetbrains.intellij") version "1.12.0"
}

group = "com.jayuroun.plugin"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
    jcenter()
}
dependencies {
    implementation("org.projectlombok:lombok:1.18.22")

    implementation("org.glassfish.jersey.core:jersey-client:2.25.1")
    implementation("org.glassfish.jersey.media:jersey-media-json-jackson:2.25.1")
    implementation("org.apache.commons:commons-lang3:3.5")
    implementation("ch.qos.logback:logback-classic:1.2.3")
    implementation("com.optimaize.languagedetector:language-detector:0.6")
    implementation("com.google.guava:guava:21.0")
    implementation("org.projectlombok:lombok:1.18.2")

    compileOnly ("org.projectlombok:lombok:1.18.20")
    annotationProcessor ("org.projectlombok:lombok:1.18.20")
}

// Configure Gradle IntelliJ Plugin
// Read more: https://plugins.jetbrains.com/docs/intellij/tools-gradle-intellij-plugin.html
intellij {
    version.set("2022.1.4")
    type.set("IC") // Target IDE Platform

    plugins.set(listOf(/* Plugin Dependencies */))
}

tasks {
    // Set the JVM compatibility versions
    withType<JavaCompile> {
        sourceCompatibility = "11"
        targetCompatibility = "11"
    }

    patchPluginXml {
        sinceBuild.set("221")
        untilBuild.set("231.*")
    }

    signPlugin {
        certificateChain.set(System.getenv("CERTIFICATE_CHAIN"))
        privateKey.set(System.getenv("PRIVATE_KEY"))
        password.set(System.getenv("PRIVATE_KEY_PASSWORD"))
    }

    publishPlugin {
        token.set(System.getenv("PUBLISH_TOKEN"))
    }

}
