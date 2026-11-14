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

// run.sh가 PC에 깔린 자바가 아니라 Gradle이 고른 JDK 25로 프로그램을 띄울 수 있게, 그 JDK의 위치를 파일에 적는다.
// Gradle의 run 작업으로 띄우면 입력이 거부될 때 Gradle의 실패 안내가 프로그램의 안내문 뒤에 덧붙고
// 종료 코드도 모두 1이 되어서, run.sh는 프로그램을 직접 띄운다.
tasks.register("writeJavaHome") {
    val javaHome = javaToolchains.launcherFor(java.toolchain).map { it.metadata.installationPath.asFile.absolutePath }
    val target = layout.buildDirectory.file("java-home.txt")
    inputs.property("javaHome", javaHome)
    outputs.file(target)
    doLast {
        target.get().asFile.writeText(javaHome.get(), Charsets.UTF_8)
    }
}

tasks.withType<JavaCompile>().configureEach {
    options.encoding = "UTF-8"
}

tasks.test {
    useJUnitPlatform()
    systemProperty("java.awt.headless", "true")
}
