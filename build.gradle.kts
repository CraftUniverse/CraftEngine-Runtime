plugins {
    id("java")
    id("com.github.johnrengelman.shadow") version "8.1.1"
    id("net.kyori.blossom") version "1.3.1"
}

group = "dev.craftengine"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

dependencies {
    implementation("net.minestom:minestom-snapshots:96cedb1bab")

    // Logback
    implementation("ch.qos.logback:logback-core:1.5.16")
    implementation("ch.qos.logback:logback-classic:1.5.16")

    // IPC Communication
    implementation("org.msgpack:msgpack-core:0.9.9")

    testImplementation(platform("org.junit:junit-bom:5.10.0"))
    testImplementation("org.junit.jupiter:junit-jupiter")
}

tasks.test {
    useJUnitPlatform()
}

blossom {
    val gitFile = "src/main/java/dev/craftengine/runtime/debug/Git.java"

    val gitCommit = System.getenv("GIT_COMMIT")
    val gitBranch = System.getenv("GIT_BRANCH")

    replaceToken("\"%COMMIT%\"", if (gitCommit == null) "null" else "\"${gitCommit}\"", gitFile)
    replaceToken("\"%BRANCH%\"", if (gitBranch == null) "null" else "\"${gitBranch}\"", gitFile)
}