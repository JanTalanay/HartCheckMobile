plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
}

android {
    namespace = "com.example.hartcheck"
    compileSdk = 33

    defaultConfig {
        applicationId = "com.example.hartcheck"
        minSdk = 30
        targetSdk = 33
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }
    buildFeatures{
        viewBinding = true
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            getByName("release") {
                // Enables code shrinking, obfuscation, and optimization for only
                // your project's release build type. Make sure to use a build
                // variant with `isDebuggable=false`.
                isMinifyEnabled = true

                // Enables resource shrinking, which is performed by the
                // Android Gradle plugin.
                isShrinkResources = true

                // Includes the default ProGuard rules files that are packaged with
                // the Android Gradle plugin. To learn more, go to the section about
                // R8 configuration files.
                proguardFiles(
                    getDefaultProguardFile("proguard-android-optimize.txt"),
                    "proguard-rules.pro"
                )
            }
        }
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

    implementation("androidx.core:core-ktx:1.9.0")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.navigation:navigation-fragment-ktx:2.5.3")
    implementation("androidx.navigation:navigation-ui-ktx:2.5.3")
    testImplementation("junit:junit:4.13.2")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")

    //retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.9.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.9.0")

    //okhttp
    implementation("com.squareup.okhttp3:okhttp:4.10.0")

    //Google
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // Coroutines
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.5.2")
    implementation ("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.5.2")

    //nav
    implementation("com.github.ismaeldivita:chip-navigation-bar:1.4.0")

    //accordion view
    implementation ("androidx.appcompat:appcompat:1.3.0")
    implementation ("androidx.recyclerview:recyclerview:1.2.1")

    //MP Chart
    implementation ("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation ("org.apache.commons:commons-csv:1.5")

    //MSSQL Thing
    implementation ("com.microsoft.sqlserver:mssql-jdbc:9.4.0.jre11")



}