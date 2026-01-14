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
            implementation(libs.androidx.lifecycle.runtimeCompose)
            implementation(libs.androidx.lifecycle.viewmodelCompose)
            implementation(libs.compose.foundation)
            implementation(libs.compose.material.icons)
            implementation(libs.compose.material3)
            implementation(libs.compose.resources)
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.ui.preview)
            implementation(libs.confettikit)
            implementation(libs.indexeddb.core)
            implementation(libs.koin.compose)
            implementation(libs.koin.compose.viewmodel)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.datetime)
            implementation(libs.kotlinx.immutable)
            implementation(libs.ktor.client.cio)
            implementation(libs.ktor.client.core)
            implementation(libs.ktor.client.websockets)
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
