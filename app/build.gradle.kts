import java.io.FileInputStream
import java.util.*

plugins {
    id("com.android.application")
    id("kotlin-android")
    id("kotlin-android-extensions")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("androidx.navigation.safeargs.kotlin")
}

android {
    compileSdkVersion(29)

    defaultConfig {
        applicationId = "com.unbounds.trakt"

        minSdkVersion(21)
        targetSdkVersion(29)

        versionCode = 1
        versionName = "1.0"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_1_8.toString()
    }

    signingConfigs {
        register("release") {
            if (project.hasProperty("keystore_password") && project.hasProperty("key_password")) {
                storeFile = file("keystore")
                storePassword = "${project.property("keystore_password")}"
                keyAlias = "trakt"
                keyPassword = "${project.property("key_password")}"
            }
        }
    }

    buildTypes {
        val fis = FileInputStream("trakt.properties")
        val prop = Properties()
        prop.load(fis)

        named("release") {
            isMinifyEnabled = false
            proguardFiles(getDefaultProguardFile("proguard-android.txt"), "proguard-rules.pro")
            signingConfig = signingConfigs.getByName("release")
            
            buildConfigField("String", "CLIENT_ID", "\"${prop.getProperty("client_id")}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${prop.getProperty("client_secret")}\"")
            buildConfigField("String", "TMDB_KEY", "\"${prop.getProperty("tmdb_key")}\"")
            buildConfigField("String", "BASE_API_URL", "\"https://api-v2launch.trakt.tv\"")
            buildConfigField("String", "BASE_URL", "\"https://trakt.tv\"")
            buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/3\"")

            resValue("string", "oauth_referrer_scheme", "unbounds-trakt")
            resValue("string", "oauth_referrer", "unbounds-trakt://oauth")
        }

        named("debug") {
            applicationIdSuffix = ".debug"

            buildConfigField("String", "CLIENT_ID", "\"${prop.getProperty("staging_client_id")}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${prop.getProperty("staging_client_secret")}\"")
            buildConfigField("String", "TMDB_KEY", "\"${prop.getProperty("tmdb_key")}\"")
            buildConfigField("String", "BASE_API_URL", "\"http://api.staging.trakt.tv\"")
            buildConfigField("String", "BASE_URL", "\"http://staging.trakt.tv\"")
            buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/3\"")

            resValue("string", "oauth_referrer_scheme", "unbounds-trakt-debug")
            resValue("string", "oauth_referrer", "unbounds-trakt-debug://oauth")
        }
    }
}

dependencies {
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.3.9")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.3.9")

    implementation("androidx.core:core-ktx:1.3.1")
    implementation("androidx.appcompat:appcompat:1.2.0")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.browser:browser:1.2.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.2.0")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.2.0")
    implementation("androidx.constraintlayout:constraintlayout:1.1.3")

    implementation("com.google.android.material:material:1.2.0")

    implementation("com.google.code.gson:gson:2.8.6")
    implementation("com.squareup.picasso:picasso:2.71828")

    val retrofit2Version = "2.9.0"
    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:$retrofit2Version")
    implementation("com.squareup.retrofit2:converter-moshi:$retrofit2Version")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // Okhttp3
    implementation("com.squareup.okhttp3:okhttp:4.8.1")
    implementation("com.squareup.okhttp3:logging-interceptor:4.8.1")

    // Hilt
    implementation("com.google.dagger:hilt-android:2.28.3-alpha")
    kapt("com.google.dagger:hilt-android-compiler:2.28.3-alpha")

    val navVersion = "2.3.0"

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:$navVersion")
    implementation("androidx.navigation:navigation-ui-ktx:$navVersion")

    testImplementation("junit:junit:4.13")
}
