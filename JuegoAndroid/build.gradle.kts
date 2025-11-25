// build.gradle.kts (raíz)

plugins {
    // AGP y Kotlin se aplican en los módulos
    id("com.android.application") version "8.12.3" apply false
    id("org.jetbrains.kotlin.android") version "1.9.24" apply false

    // Calidad (se aplican en el módulo app)
    id("org.jlleitschuh.gradle.ktlint") version "11.6.1" apply false
    id("io.gitlab.arturbosch.detekt") version "1.23.6" apply false
}

tasks.register<Delete>("clean") {
    delete(rootProject.buildDir)
}
