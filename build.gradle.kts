group = "org.deimoscm"
version = "1.0-SNAPSHOT"

plugins {
    id("application")
    id("java")
    kotlin("jvm") version "1.9.23"
}

repositories {
    mavenCentral()
}

dependencies {
    implementation("com.google.guava:guava:33.0.0-jre")
    testImplementation("org.junit.jupiter:junit-jupiter:5.9.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib-jdk8:1.3.50")
    testImplementation("org.jetbrains.kotlin:kotlin-test-junit5")
    implementation("org.jetbrains.kotlin:kotlin-reflect:1.9.20")
    implementation("com.formdev:flatlaf:3.4.1")
    implementation("com.formdev:flatlaf-intellij-themes:3.4.1")
    implementation("com.fasterxml.jackson.dataformat:jackson-dataformat-xml:2.15.0")
    implementation(files("/home/matthew/src/Projects/MarodiGameLib/app/build/libs/MarodiGameLibrary-1.0-SNAPSHOT.jar"))
}

sourceSets.main {
    java.srcDirs("src/main/kotlin")
}

tasks.test {
    useJUnitPlatform()
}

application {
    mainClass = "org.deimoscm.MainKt"
}
