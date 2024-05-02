# Gemini Travel Planner
Android application for planning a travel itinerary based on generative AI responses from Google's Gemini API.

* Android Architecture Components (MVVM)
* Coroutines
* Hilt
* Retrofit
* OkHttp
* Material 3 Compose Components
* Jetpack Navigation Compose
* JUnit
* Mockito

## Screenshots

[Screenshot 1 placeholder]
[Screenshot 2 placeholder]

## Video

[Video placeholder]

## Features

* Get recommendations for activities and accommodations based on your preferences.

## Modules

## APIs

### GeoApify API

The app uses the GeoApify API to validate the entered locations. To use the API, you will need to obtain an API key from https://geoapify.com/ and add it to your build config as follows:

`buildConfigField("String" ,  "GEOAPIFY_API_KEY", ""YOUR_API_KEY"")`

### Google Gemini API

The app uses the Google Gemini API to develop an itinerary based on the entered location and dates. To use the API, you will need to obtain an API key from https://developers.google.com/maps/documentation/gemini/get-api-key and add it to your build config as follows:

`buildConfigField("String" ,  "GOOGLE_GEMINI_API_KEY" ,  ""YOUR_API_KEY"")`