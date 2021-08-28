import java.io.FileInputStream
import java.time.Instant
import java.time.ZoneId
import java.time.format.DateTimeFormatter
import java.util.*

plugins {
    id("com.android.application")
    kotlin("android")
    kotlin("kapt")
    id("dagger.hilt.android.plugin")
    id("com.github.triplet.play") version "3.6.0"
    id("androidx.navigation.safeargs.kotlin")
}

fun String.runCommand(workingDir: File = File(".")): String? {
    try {
        val parts = this.split("\\s".toRegex())
        val proc = ProcessBuilder(*parts.toTypedArray())
                .directory(workingDir)
                .redirectOutput(ProcessBuilder.Redirect.PIPE)
                .redirectError(ProcessBuilder.Redirect.PIPE)
                .start()

        proc.waitFor(30, TimeUnit.SECONDS)
        return proc.inputStream.bufferedReader().readText().trim()
    } catch (e: java.io.IOException) {
        e.printStackTrace()
        return null
    }
}

val dynamicVersionCode = (System.currentTimeMillis() / 1000L).toInt()

val gitSha = "git rev-parse --short HEAD".runCommand()

val timestamp: String
    get() {
        val formatter = DateTimeFormatter.ofPattern("yyMMdd.HHmm").withZone(ZoneId.of("Europe/Stockholm"))
        return formatter.format(Instant.now())
    }

val latestVersion = "git describe --tags --abbrev=0".runCommand()

fun updateReleaseNotes() {
    val notes = "git tag -l --format=%(body) $latestVersion".runCommand()
    if (notes.isNullOrBlank()) {
        return
    }

    with(file("src/main/play/release-notes/en-US/default.txt")) {
        with(parentFile) {
            if (!exists()) try {
                mkdirs()
            } catch (e: Exception) {
                println(e.message)
            }
        }

        if (!exists()) try {
            createNewFile()
        } catch (e: Exception) {
            println(e.message)
        }
        printWriter().use { out ->
            out.println(notes)
        }
    }
}

android {
    compileSdk = 30

    buildFeatures.viewBinding = true

    defaultConfig {
        applicationId = "com.unbounds.trakt"

        minSdk = 21
        targetSdk = 30

        versionCode = dynamicVersionCode
        versionName = "$latestVersion-$timestamp-$gitSha"
    }

    signingConfigs {
        register("release") {
            if (project.hasProperty("keystore_password") && project.hasProperty("key_password")) {
                storeFile = file("upload-keystore.jks")
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
            buildConfigField("String", "BASE_URL", "\"https://trakt.tv\"")
            buildConfigField("String", "BASE_API_URL", "\"https://api-v2launch.trakt.tv\"")
            buildConfigField("String", "TMDB_KEY", "\"${prop.getProperty("tmdb_key")}\"")
            buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/3\"")

            resValue("string", "oauth_referrer_scheme", "unbounds-trakt")
            resValue("string", "oauth_referrer", "unbounds-trakt://oauth")
        }

        named("debug") {
            applicationIdSuffix = ".debug"

            buildConfigField("String", "CLIENT_ID", "\"${prop.getProperty("staging_client_id")}\"")
            buildConfigField("String", "CLIENT_SECRET", "\"${prop.getProperty("staging_client_secret")}\"")
            buildConfigField("String", "BASE_URL", "\"https://staging.trakt.tv\"")
            buildConfigField("String", "BASE_API_URL", "\"https://api-staging.trakt.tv\"")
            buildConfigField("String", "TMDB_KEY", "\"${prop.getProperty("tmdb_key")}\"")
            buildConfigField("String", "TMDB_API_URL", "\"https://api.themoviedb.org/3\"")

            resValue("string", "oauth_referrer_scheme", "unbounds-trakt-debug")
            resValue("string", "oauth_referrer", "unbounds-trakt-debug://oauth")
        }
    }
}

play {
    serviceAccountCredentials.set(file("key.json"))
    defaultToAppBundles.set(true)
}

dependencies {
    // Kotlin Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.1-native-mt")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.1-native-mt")

    implementation("androidx.core:core-ktx:1.6.0")
    implementation("androidx.appcompat:appcompat:1.3.1")
    implementation("androidx.cardview:cardview:1.0.0")
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("androidx.browser:browser:1.3.0")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.3.1")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.3.1")
    implementation("androidx.constraintlayout:constraintlayout:2.1.0")

    implementation("com.google.android.material:material:1.4.0")

    implementation("com.google.code.gson:gson:2.8.7")
    implementation("com.squareup.picasso:picasso:2.71828")

    // Retrofit2
    implementation("com.squareup.retrofit2:retrofit:${Version.retrofit2}")
    implementation("com.squareup.retrofit2:converter-moshi:${Version.retrofit2}")
    implementation("com.jakewharton.retrofit:retrofit2-kotlin-coroutines-adapter:0.9.2")

    // Okhttp3
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.2")

    // Hilt
    implementation("com.google.dagger:hilt-android:${Version.hilt}")
    kapt("com.google.dagger:hilt-android-compiler:${Version.hilt}")

    // Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:${Version.nav}")
    implementation("androidx.navigation:navigation-ui-ktx:${Version.nav}")

    testImplementation("junit:junit:4.13.2")
}

kapt {
    correctErrorTypes = true
}

tasks.register("updateReleaseNotes") {
    updateReleaseNotes()
}

