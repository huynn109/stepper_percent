plugins {
    id("com.android.library")
    kotlin("android")
    kotlin("android.extensions")
    kotlin("kapt")
    id("maven-publish")
}

android {
    compileSdk = ConfigData.compileSdkVersion

    defaultConfig {
        minSdk = ConfigData.minSdkVersion
        targetSdk = ConfigData.targetSdkVersion
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"

        vectorDrawables.useSupportLibrary = true
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    androidExtensions {
        isExperimental = true
    }

    lint {
        isCheckAllWarnings = true
        isWarningsAsErrors = true
    }

    testOptions {
        unitTests.isIncludeAndroidResources = true
        unitTests.isReturnDefaultValues = true
    }

    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
        }
    }
}

kotlin {
    explicitApi()
}

dependencies {
    implementation(Deps.kotlin)
    implementation(Deps.materialDesign)
    implementation(Deps.constraintLayout)
}
afterEvaluate {
    publishing {
        publications {
            // Creates a Maven publication called "release".
            val release by publications.registering(MavenPublication::class) {
                // Applies the component for the release build variant.
                from(components["release"])
                // You can then customize attributes of the publication as shown below.
                groupId = "com.github.huynn109"
                artifactId = "stepper-percent"
                version = "v0.0.8"
            }
        }
    }
}

