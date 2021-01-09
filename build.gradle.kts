// Top-level build file where you can add configuration options common to all sub-projects/modules.

buildscript {
    repositories {
        jcenter()
        google()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:4.1.1")
        classpath(kotlin("gradle-plugin", version = "1.4.0"))
        classpath("com.google.dagger:hilt-android-gradle-plugin:2.28-alpha")

        val navVersion = "2.3.0"
        classpath("androidx.navigation:navigation-safe-args-gradle-plugin:$navVersion")
        // NOTE: Do not place your application dependencies here; they belong
        // in the individual module build.gradle files
    }
}

allprojects {
    repositories {
        jcenter()
        google()
    }
}

tasks.register("clean", Delete::class) {
    delete(rootProject.buildDir)
    delete("app/src/main/play/release-notes")
}
