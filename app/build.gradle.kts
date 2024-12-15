plugins {
    alias(libs.plugins.android.application)


    // Add the Google services Gradle plugin
    id("com.google.gms.google-services")

}

android {
    namespace = "com.example.shoe"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.shoe"
        minSdk = 29
        targetSdk = 34
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
        viewBinding = true
    }
    sourceSets {
        getByName("main") {
            res {
                srcDirs("src/main/res", "src/main/res/layouts")
            }
        }
    }
}

dependencies {
    // default libs
    implementation(libs.circleimageview)
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.constraintlayout)
    implementation(libs.navigation.fragment)
    implementation(libs.navigation.ui)
    implementation(libs.activity)
    implementation(fileTree(mapOf(
        "dir" to "src/main/libs",
        "include" to listOf("*.aar", "*.jar"),
        "exclude" to listOf("")
    )))


    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)

    implementation(libs.picasso)

    implementation(libs.commons.codec)


    // imports
    implementation(libs.retrofit)
    implementation(libs.retrofit2.converter.gson)
    implementation(libs.converter.scalars)

    implementation(libs.lifecycle.livedata.ktx)
    implementation(libs.lifecycle.viewmodel.ktx)
    implementation(libs.lifecycle.viewmodel.android)

    implementation(libs.glide)

    implementation(libs.logging.interceptor)

    implementation(libs.legacy.support.v4)

    implementation(libs.play.services.maps)
    implementation(libs.play.services.location)

    implementation(libs.material)

    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")

// Import the BoM for the Firebase platform
    implementation(platform(libs.firebase.bom))
    implementation(libs.firebase.auth)
    implementation(libs.play.services.auth)

    implementation("com.google.firebase:firebase-messaging:23.0.0")
}