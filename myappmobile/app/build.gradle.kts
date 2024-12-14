plugins {
    alias(libs.plugins.android.application)
    id("com.google.gms.google-services")  // Firebase plugin in Kotlin DSL
}

android {
    namespace = "com.example.myapplication"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.myapplication"
        minSdk = 24
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
}

dependencies {
    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    implementation (libs.recyclerview)
    implementation (libs.cardview)
    implementation (libs.mpandroidchart)
    implementation (libs.material)
    implementation (libs.itext7.core)
    implementation (libs.androidx.activity.ktx)
    implementation (libs.androidx.core)

    // Firebase BOM
    implementation(platform(libs.firebase.bom))
    implementation (libs.activity.v172)
    // Firebase libraries
    implementation(libs.firebase.analytics)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.firebase.firestore)
}
