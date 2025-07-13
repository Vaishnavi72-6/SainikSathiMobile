plugins {

        id("com.android.application") // ✅ No version here
        id("org.jetbrains.kotlin.android")
        id("org.jetbrains.kotlin.kapt")
        id("com.google.gms.google-services")


}

android {
    namespace = "com.example.sainiksathimobile"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.sainiksathimobile"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
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

    buildFeatures {
        viewBinding = true
        compose = true
    }

    composeOptions {
        kotlinCompilerExtensionVersion = "1.5.14"
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }

    kotlinOptions {
        jvmTarget = "1.8"
    }
}

dependencies {
    implementation("org.mindrot:jbcrypt:0.4")
    implementation("at.favre.lib:bcrypt:0.9.0")
    implementation ("com.google.mlkit:translate:17.0.1")
            implementation ("com.google.android.gms:play-services-location:21.0.1")

    implementation("com.google.android.material:material:1.11.0")

    implementation(platform("androidx.compose:compose-bom:2024.04.00"))
    androidTestImplementation(platform("androidx.compose:compose-bom:2024.04.00"))
    implementation ("com.google.mlkit:text-recognition:16.0.0-beta6")
    implementation("androidx.compose.ui:ui")
    implementation("androidx.activity:activity-compose")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx")
    implementation("androidx.compose.material3:material3:1.2.1")
    implementation ("androidx.cardview:cardview:1.0.0")

    debugImplementation("androidx.compose.ui:ui-tooling")
    debugImplementation("androidx.compose.ui:ui-test-manifest")

    implementation("androidx.room:room-runtime:2.6.1")
    implementation("androidx.room:room-ktx:2.6.1")
    kapt("androidx.room:room-compiler:2.6.1")

    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.7.0")
    implementation("androidx.activity:activity-ktx:1.9.0")

    implementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    implementation("com.google.zxing:core:3.5.2")
    implementation("com.journeyapps:zxing-android-embedded:4.3.0")
    implementation("com.github.gcacace:signature-pad:1.3.1")


    // OCR & CameraX


    implementation ("androidx.camera:camera-camera2:1.3.0")
    implementation ("androidx.camera:camera-lifecycle:1.3.0")
    implementation ("androidx.camera:camera-view:1.3.0")


// Latin

        // Latin script (English, French, etc.)
        implementation("com.google.mlkit:text-recognition:16.0.0")

        // Indian scripts
        implementation("com.google.mlkit:text-recognition-devanagari:16.0.0")   // Hindi, Marathi
    implementation("com.google.firebase:firebase-firestore:25.0.0")
    implementation("com.google.mlkit:translate:17.0.1")

    // Optional: East Asian scripts
        // implementation("com.google.mlkit:text-recognition-chinese:16.0.0")
        // implementation("com.google.mlkit:text-recognition-japanese:16.0.0")
        // implementation("com.google.mlkit:text-recognition-korean:16.0.0")
    }




