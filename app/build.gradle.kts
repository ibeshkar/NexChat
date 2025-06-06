import java.util.Properties

plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)

    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    alias(libs.plugins.google.gms.google.services)
}

val localProperties = Properties()
val localPropertiesFile = rootProject.file("local.properties")
if (localPropertiesFile.exists()) {
    localProperties.load(localPropertiesFile.inputStream())
}

android {
    namespace = "com.artofelectronic.nexchat"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.artofelectronic.nexchat"
        minSdk = 24
        targetSdk = 35
        versionCode = 1
        versionName = "1.0.0"

        testInstrumentationRunner = "com.artofelectronic.nexchat.ui.HiltTestRunner"

        val webClientId: String =
            localProperties.getProperty("Firebase_Web_Client_ID") ?: "default_key"
        buildConfigField("String", "Firebase_Web_Client_ID", "\"$webClientId\"")
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
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += setOf(
                "/META-INF/{AL2.0,LGPL2.1}",
                "META-INF/LICENSE.md",
                "META-INF/LICENSE.txt",
                "META-INF/NOTICE.md",
                "META-INF/NOTICE.txt",
                "META-INF/*.kotlin_module",
                "META-INF/DEPENDENCIES",
                "META-INF/LICENSE",
                "META-INF/LICENSE*",
                "META-INF/NOTICE",
                "META-INF/NOTICE*",
                "META-INF/*.RSA",
                "META-INF/*.SF",
                "META-INF/*.DSA"
            )
        }
    }
    testOptions {
        unitTests.isIncludeAndroidResources = true
        execution = "ANDROIDX_TEST_ORCHESTRATOR"
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.junit.ktx)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)

    // Testing
    testImplementation(libs.junit)
    testImplementation(libs.junit.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    testImplementation(libs.mockito.core)
    androidTestImplementation(libs.mockito.android)
    testImplementation(libs.mockk)
    androidTestImplementation(libs.mockk.android)
    androidTestImplementation(libs.hilt.android.testing)
    androidTestUtil(libs.androidx.orchestrator)
    testImplementation(libs.kotlinx.coroutines.test)
    testImplementation(libs.androidx.core.testing)

    // Material design
    implementation(libs.androidx.material.icons.extended)

    // Splash Screen
    implementation(libs.androidx.core.splashscreen)

    // Hilt
    implementation(libs.hilt.android)
    kapt(libs.hilt.compiler)

    // Firebase services
    implementation(libs.play.services.auth)
    implementation(libs.firebase.firestore)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.credentials)
    implementation(libs.androidx.credentials.play.services.auth)
    implementation(libs.googleid)

    // Facebook Login SDK
    implementation(libs.facebook.login)

    // Coroutines support for Firebase Tasks
    implementation(libs.kotlinx.coroutines.play.services)

    // Navigation
    implementation(libs.androidx.navigation.compose)
    implementation(libs.androidx.navigation.testing.android)

}