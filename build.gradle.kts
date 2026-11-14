plugins {
    id("java")
    id("application")
}

group = "org.ruscayn"
version = "1.0-SNAPSHOT"

repositories {
    mavenCentral()
}

java {
    toolchain {
        languageVersion = JavaLanguageVersion.of(25)
    }
}

dependencies {
    implementation("tools.jackson.core:jackson-databind:3.3.0")

    testImplementation(platform("org.junit:junit-bom:6.1.3"))
    testImplementation("org.junit.jupiter:junit-jupiter")
    testRuntimeOnly("org.junit.platform:junit-platform-launcher")
}

application {
    mainClass = "org.ruscayn.infradiagram.app.Main"
    // 화면이 없는 환경(서버, CI)에서도 그림을 그릴 수 있게 한다.
    applicationDefaultJvmArgs = listOf("-Djava.awt.headless=true")
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    systemProperty("java.awt.headless", "true")
}
