# TimeTable

Edit root/app/build.gradle like below.
```
allprojects {
    repositories {
      ...
      maven { url 'https://jitpack.io' }
    }
}
```

and:
```
dependencies {
    implementation 'com.github.AndroidJustSeries:TimeTable:1.0.0'
}
```
