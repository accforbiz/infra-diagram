plugins {
    // JDK 25가 설치되지 않은 PC에서 Gradle이 JDK 25를 찾아 내려받게 한다.
    id("org.gradle.toolchains.foojay-resolver-convention") version "1.0.0"
}

rootProject.name = "infra-diagram"
