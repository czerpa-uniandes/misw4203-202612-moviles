plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.compose)
}

android {
    namespace = "com.team4.vinilosapp"
    compileSdk {
        version = release(36) {
            minorApiLevel = 1
        }
    }

    defaultConfig {
        applicationId = "com.team4.vinilosapp"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    buildFeatures {
        compose = true
    }
}

dependencies {
    implementation(libs.retrofit)
    implementation(libs.retrofit.moshi)
    implementation(libs.moshi.kotlin)
    implementation(libs.okhttp.logging)
    implementation(libs.androidx.lifecycle.viewmodel.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.compose.ui)
    implementation(libs.androidx.compose.ui.graphics)
    implementation(libs.androidx.compose.ui.tooling.preview)
    implementation(libs.androidx.compose.material3)
    implementation(libs.androidx.navigation.compose)
    implementation(libs.coil.compose)
    implementation(libs.androidx.compose.runtime)
    implementation(libs.androidx.compose.foundation.layout)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.compose.foundation)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.compose.ui.test.junit4)
    debugImplementation(libs.androidx.compose.ui.tooling)
    debugImplementation(libs.androidx.compose.ui.test.manifest)
    implementation(libs.androidx.compose.material.icons.extended)
    implementation(libs.volley)
    implementation(libs.converter.gson)
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.8.1")
    testImplementation("app.cash.turbine:turbine:1.1.0")
    testImplementation("org.jetbrains.kotlin:kotlin-test:2.2.10")
    testImplementation("com.squareup.okhttp3:mockwebserver:4.12.0")
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("junit:junit:4.13.2")
    testImplementation("com.squareup.moshi:moshi:1.15.1")
    testImplementation("com.squareup.moshi:moshi-kotlin:1.15.1")
}