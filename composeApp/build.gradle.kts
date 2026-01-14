import org.jetbrains.kotlin.gradle.ExperimentalWasmDsl

plugins {
    alias(libs.plugins.kotlin.multiplatform)
    alias(libs.plugins.compose.multiplatform)
    alias(libs.plugins.compose.compiler)
}

kotlin {
    @OptIn(ExperimentalWasmDsl::class)
    wasmJs {
        browser()
        binaries.executable()
    }

    sourceSets {
        commonMain.dependencies {
            implementation("org.jetbrains.compose.runtime:runtime:1.10.0")
            implementation("org.jetbrains.compose.foundation:foundation:1.10.0")
            implementation("org.jetbrains.compose.material3:material3:1.9.0")
            implementation("org.jetbrains.compose.material:material-icons-extended:1.7.3")
            implementation("org.jetbrains.compose.ui:ui:1.10.0")
            implementation("org.jetbrains.compose.components:components-resources:1.10.0")
            implementation("org.jetbrains.compose.ui:ui-tooling-preview:1.10.0")

            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)

            implementation("com.github.eygraber.indexeddb:core:wasm-SNAPSHOT")
            implementation("io.github.vinceglb:confettikit:0.7.0")
            implementation("io.ktor:ktor-client-core:3.3.3")
            implementation("io.ktor:ktor-client-cio:3.3.3")
            implementation("io.ktor:ktor-client-websockets:3.3.3")
            implementation("org.jetbrains.kotlinx:kotlinx-collections-immutable:0.4.0")
            implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.7.1")
        }

        wasmJsMain {
            dependencies {
                implementation(npm("@js-joda/timezone", "2.22.0"))
            }
        }

        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

compose.resources {
    packageOfResClass = "fr.outadoc.teabot.generated"
}
