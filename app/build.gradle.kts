import org.jetbrains.kotlin.konan.properties.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("kotlin-kapt")
    id("dagger.hilt.android.plugin")
    id("kotlin-parcelize")
    id("androidx.navigation.safeargs.kotlin")
    id("com.google.android.libraries.mapsplatform.secrets-gradle-plugin")
    id("com.google.gms.google-services")
    id("com.google.firebase.crashlytics")
}

android {
    namespace = "com.bonface.openweather"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.bonface.openweather"
        minSdk = 24
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        vectorDrawables {
            useSupportLibrary = true
        }

        val properties = Properties().apply {
            load(rootProject.file("local.properties").reader())
        }

        buildConfigField("String", "BASE_URL", "\"${properties.getProperty("BASE_URL", "")}\"")
        buildConfigField("String", "OPEN_WEATHER_API_KEY", "\"${properties.getProperty("OPEN_WEATHER_API_KEY", "")}\"")
        manifestPlaceholders["MAPS_API_KEY"] = properties.getProperty("MAPS_API_KEY", "")
    }

    buildTypes {
        release {
            isDebuggable = true
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }

    applicationVariants.all {
        val variant = this
        variant.outputs
            .map { it as com.android.build.gradle.internal.api.BaseVariantOutputImpl }
            .forEach { output ->
                val outputFileName = "openweather_${variant.baseName}_${variant.versionName}_${variant.versionCode}.apk"
                output.outputFileName = outputFileName
            }
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = JavaVersion.VERSION_17.toString()
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions.unitTests.apply {
        isIncludeAndroidResources = true
        isReturnDefaultValues = true
    }
}

dependencies {

    implementation("androidx.core:core-ktx:1.12.0")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.appcompat:appcompat:1.6.1")
    implementation("com.google.android.material:material:1.10.0")
    implementation("androidx.constraintlayout:constraintlayout:2.1.4")
    implementation("androidx.cardview:cardview:1.0.0")

    //Lifacycle
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-process:2.6.2")
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-extensions:2.2.0")
    kapt("androidx.lifecycle:lifecycle-common-java8:2.6.2")

    //Firebase crashlytics
    implementation("com.google.firebase:firebase-crashlytics:18.6.0")
    implementation("com.google.firebase:firebase-analytics:21.5.0")

    //Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.7.5")
    implementation("androidx.navigation:navigation-ui-ktx:2.7.5")

    //Splash Screen
    implementation("androidx.core:core-splashscreen:1.1.0-alpha02")

    // Coroutines
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-core:1.7.3")
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")

    // Coroutine Lifecycle Scopes
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.6.2")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.6.2")

    //Google Map
    implementation("com.google.android.gms:play-services-maps:18.2.0")
    implementation("com.google.android.gms:play-services-location:21.0.1")
    //Places
    implementation("com.google.android.libraries.places:places:3.3.0")

    // Dexter (for permissions)
    implementation("com.karumi:dexter:6.2.3")

    //Dagger Hilt
    implementation("com.google.dagger:hilt-android:2.48.1")
    kapt("com.google.dagger:hilt-android-compiler:2.48.1")

    //Recyclerview
    implementation("androidx.recyclerview:recyclerview:1.3.2")

    //Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    //OKHTTP
    implementation("com.squareup.okhttp3:okhttp:5.0.0-alpha.2")
    //Logging Interceptor
    implementation("com.squareup.okhttp3:logging-interceptor:5.0.0-alpha.6")

    //Moshi
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    //noinspection KaptUsageInsteadOfKsp
    kapt("com.squareup.moshi:moshi-kotlin-codegen:1.15.0")
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")

    //Timber
    implementation("com.jakewharton.timber:timber:5.0.1")

    //Room Database
    implementation("androidx.room:room-runtime:2.6.0")
    implementation("androidx.room:room-ktx:2.6.0")
    //noinspection KaptUsageInsteadOfKsp
    kapt("androidx.room:room-compiler:2.6.0")

    //Palette
    implementation("androidx.palette:palette-ktx:1.0.0")

    //Tests
    testImplementation("junit:junit:4.13.2")
    implementation("androidx.test.ext:junit-ktx:1.1.5")
    androidTestImplementation("androidx.test.ext:junit:1.1.5")
    // Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.5.1")
    androidTestImplementation("androidx.test.espresso:espresso-contrib:3.5.1")
    androidTestImplementation ("androidx.test.espresso:espresso-intents:3.5.1")

    androidTestImplementation("androidx.test:rules:1.5.0")
    androidTestImplementation("androidx.test:core-ktx:1.5.0")
    androidTestImplementation("org.robolectric:robolectric:4.11.1")

    // For instrumented tests.
    androidTestImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kaptAndroidTest("com.google.dagger:hilt-android-compiler:2.48.1")
    androidTestAnnotationProcessor("com.google.dagger:hilt-android-compiler:2.48.1")

    testImplementation("io.mockk:mockk:1.13.8")
    testImplementation("com.squareup.okhttp3:mockwebserver:5.0.0-alpha.11")
    testImplementation("android.arch.core:core-testing:1.1.1")
    testImplementation("org.jetbrains.kotlinx:kotlinx-coroutines-test:1.7.3")

    testImplementation("androidx.arch.core:core-testing:2.2.0")
    testImplementation("org.robolectric:robolectric:4.11.1")
    testImplementation("com.google.truth:truth:1.1.5")
    testImplementation("androidx.room:room-testing:2.6.0")

    testImplementation("com.google.dagger:hilt-android-testing:2.48.1")
    kaptTest("com.google.dagger:hilt-android-compiler:2.48.1")

}


