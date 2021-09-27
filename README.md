# StepperPercent

[![](https://jitpack.io/v/huynn109/stepper_percent.svg)](https://jitpack.io/#huynn109/stepper_percent)

**StepperPercent is an Android library, which has a bubble view, icon stepper when seeking**

## Screenshot

<img src="https://raw.githubusercontent.com/huynn109/stepper_percent/main/screenshot/demo-1.png">

## Download
The lastest version: [![](https://jitpack.io/v/huynn109/stepper_percent.svg)](https://jitpack.io/#huynn109/stepper_percent)

```groovy
  dependencies {
        implementation 'com.github.huynn109:stepper_percent:${LATEST_VERSION}'
  }
```
## Usage
### Init in xml
```xml
 <com.huynn109.stepperslider.StepperPercent
    android:paddingStart="16dp"
    android:paddingEnd="16dp"
    android:layout_width="match_parent"
    android:layout_height="wrap_content"
    app:active_color="@color/yellow"
    app:inactive_color="@color/grey"/>
```
### Init in kotlin
```kotlin
val stepperPercent = findViewById<StepperPercent>(R.id.stepperPercent)
        stepperPercent.apply {
            setSteps(
                Pair(R.drawable.ic_baseline_adb_24, 0), // Add icon stepper and position
                Pair(R.drawable.ic_baseline_adb_24, 28),
                Pair(R.drawable.ic_baseline_adb_24, 56),
                Pair(R.drawable.ic_baseline_adb_24, 84),
                Pair(R.drawable.ic_baseline_adb_24, 100)
            )
            value(35f) // Set value slider
            disableTouch(false) // Enable/Disable drag slider
        }
```
## License
```
   Copyright 2021 huynn109

   Licensed under the Apache License, Version 2.0 (the "License");
   you may not use this file except in compliance with the License.
   You may obtain a copy of the License at

     http://www.apache.org/licenses/LICENSE-2.0

   Unless required by applicable law or agreed to in writing, software
   distributed under the License is distributed on an "AS IS" BASIS,
   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
   See the License for the specific language governing permissions and lim
```
