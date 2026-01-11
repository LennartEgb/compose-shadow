> Customizable shadows are now supported in Kotlin Multiplatform: https://kotlinlang.org/docs/multiplatform/whats-new-compose-190.html#customizable-shadows

# compose-shadow

<p align="center">
    <img src="art/shadow.png" alt="shadow" width="214">
    <img src="art/shadow-inset.png" alt="shadow-inset" width="214">
</p>

A compose multiplatform library to support drawing css style box-shadows.
The reason for this library is to replicate drop shadows and inner shadows from Figma.

## Usage

This library contains a single modifier with a simple API. The composable at the top could be created like this:

```kotlin
Box(
    modifier = Modifier
        .size(40.dp)
        .boxShadow(
            blurRadius = 20.dp,
            spreadRadius = 0.dp,
            offset = DpOffset(0.dp, 0.dp),
            color = Color.Magenta,
            shape = CircleShape,
            clip = true,
            inset = true,
        )
        .background(Color.Blue)
)
```

## Installation

To use compose-shadow in your project add the github package maven repository to your root `settings.gradle.kts`

```kotlin
dependencyResolutionManagement {
    repositories {
        maven {
            url = uri("https://maven.pkg.github.com/LennartEgb/compose-shadow")
            credentials {
                username = <GITHUB-USERNAME>
                password = <GITHUB-TOKEN>
            }
        }
    }
}
```

and add the dependency to the module level `build.gradle.kts`

```kotlin
kotlin {
    sourceSets {
        commonMain.dependencies {
            implementation("dev.lennartegb.compose:shadow:0.1.3")
        }
    }
}
```
