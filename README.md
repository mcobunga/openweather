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
    * [Kotlin](https://kotlinlang.org/): 
    * [Jetpack](https://developer.android.com/jetpack)
    * [Kotlin Coroutines](https://kotlinlang.org/docs/reference/coroutines-overview.html)
    * [Dagger Hilt](https://developer.android.com/training/dependency-injection/hilt-android) 
    * [Retrofit](https://square.github.io/retrofit/)
    * [Firebase crashlytics](https://firebase.google.com/docs/crashlytics) Helps you track, prioritize, and fix stability issues that erode your app quality.
    * [Room persistence library](https://developer.android.com/jetpack/androidx/releases/room) 
    * [LiveData](https://developer.android.com/topic/libraries/architecture/livedata) 
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

![](https://user-images.githubusercontent.com/17246592/127883474-fdf83d0c-fd39-4305-992b-36633983fbfc.jpg)
![](https://user-images.githubusercontent.com/17246592/127883500-612aa0f7-6a41-4aa2-bc8b-9fe858b91ed6.jpg)
![](https://user-images.githubusercontent.com/17246592/127883510-296329aa-59ec-43fa-ab2a-38e13c750bd0.jpg)
![](https://user-images.githubusercontent.com/17246592/127883537-7f4ccdd3-d9d2-43c3-af58-d80f233d5e27.jpg)


