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
                Pair(R.drawable.ic_baseline_adb_24, 0),
                Pair(R.drawable.ic_baseline_adb_24, 28),
                Pair(R.drawable.ic_baseline_adb_24, 56),
                Pair(R.drawable.ic_baseline_adb_24, 84),
                Pair(R.drawable.ic_baseline_adb_24, 100)
            )
            value(35f)
            disableTouch(false)
        }
```
