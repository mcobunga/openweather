# OpenWeather


## Introduction
OpenWeather Android app consumes Open Weather APIs to show current weather and daily weather forecast of user's current location using Location permissions.
A user can save location weather information as favorite. Show list of favorites as well as show favorite locations on a Google Map.
The application also supports offline viewing of saved weather information.

## Requirements to setup
- Android Studio with JDK 17
- Android Gradle Plugin 8.1+
- Kotlin 1.8+

## How to setup

- Clone the repository
- Get [OpenWeather API](https://openweathermap.org)
- Get [Google Maps API](https://console.cloud.google.com/apis/dashboard)
- Now add your Google Maps API key to the project at `local.properties` with `MAPS_API_KEY` as the property
- Also, add your OpenWeather API key to the project at `local.properties` with `OPEN_WEATHER_API_KEY` as the property
- Finally, add the base url `https://api.openweathermap.org/` to `local.properties` with `BASE_URL` as the property
- Build the project (Disable Gradle offline if yo need to sync the dependencies)


## OpenWeather Tech stack

This project has been implemented using a combination of tools and resources, some of which are;
* Architecture of choice
    * MVVM - Model View View Model
* CI/CD - Continuous Integration and Continuous Delivery
    * Github Actions

* Dependencies + Third party libraries
    * [Kotlin](https://kotlinlang.org) - Kotlin is a cross-platform, statically typed, general-purpose high-level programming language with type inference
    * [Android Jetpack](https://developer.android.com/jetpack/getting-started) - Jetpack encompasses a collection of Android libraries that incorporate best practices and provide backwards compatibility in your Android apps.
    * [Kotlin Coroutines](https://kotlinlang.org/docs/coroutines-overview.html) - Provides concept of suspending function providing a safer and less error-prone abstraction for asynchronous operations
    * [Dagger Hilt](https://dagger.dev/hilt/) - Hilt provides a standard way to incorporate Dagger dependency injection into an Android application.
    * [Retrofit](https://github.com/square/retrofit) - A type-safe HTTP client for Android and the JVM
    * [Firebase crashlytics](https://firebase.google.com/docs/crashlytics) Helps you track, prioritize, and fix stability issues that erode your app quality.
    * [AndroidX Lifecycle](https://developer.android.com/jetpack/androidx/releases/lifecycle)
    * [Google maps](https://developer.android.com/develop/sensors-and-location/location/maps-and-places) - is part of the Google Play services platform and lets you include maps and customized mapping information in your app
    * [Google Places](https://developers.google.com/maps/documentation/places/android-sdk/overview) -  Places SDK for Android allows you to build location-aware apps that respond contextually to the local businesses and other places near the user's device
    * [Dexter](https://github.com/Karumi/Dexter) - Android library that simplifies the process of requesting permissions at runtime.
    * [OKHTTP](https://square.github.io/okhttp/) - OkHttp is an HTTP client
    * [Logging Interceptor](https://square.github.io/okhttp/features/interceptors/) - Interceptors are a powerful mechanism that can monitor, rewrite, and retry calls
    * [Moshi](https://github.com/square/moshi) - A modern JSON library for Kotlin and Java.
    * [Timber](https://github.com/JakeWharton/timber) - A logger with a small, extensible API which provides utility on top of Android's normal Log class.
    * [Room Database](https://developer.android.com/jetpack/androidx/releases/room) - Room persistence library provides an abstraction layer over SQLite to allow for more robust database access while harnessing the full power of SQLite.
    * [Palette](https://github.com/Karumi/Dexter) - Palette library provides a powerful and intuitive API for creating more engaging apps extracting prominent colors from images

* Unit testing - A unit test verifies the behavior of a small section of code, the unit under test.
    * [Robolectric](https://robolectric.org) - Robolectric is a framework that brings fast and reliable unit tests to Android
    * [MockK](https://mockk.io) - Supports regular unit tests allowing you to mock objects.
    * [Google Truth](https://github.com/google/truth) - Fluent assertions for Java and Android

   





# Screenshots

