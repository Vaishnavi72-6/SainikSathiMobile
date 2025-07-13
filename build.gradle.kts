buildscript {
    dependencies {
        classpath("com.android.tools.build:gradle:8.3.1")
        classpath("com.google.gms:google-services:4.4.1")
    }

    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}

allprojects {
    repositories {
        google()
        mavenCentral()
        maven(url = "https://jitpack.io")
    }
}
