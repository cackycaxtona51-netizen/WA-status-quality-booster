plugins {
    id("com.android.application") version "8.1.1"
}

android {
    namespace = "com.hdstatus.module"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.hdstatus.module"
        minSdk = 28
        targetSdk = 34
        versionCode = 1
        versionName = "1.0"
    }
}

dependencies {
    compileOnly("de.robv.android.xposed:api:82")
}
