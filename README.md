# JetpackComposeComponents
This is a collection of UI components that I've made with jetpack compose


### Installation
In your ```project/build.gradle```, be sure the Jitpack IO repository is included
```kotlin
// In project/build.gradle (top-level gradle file)
  repositories {
    ...
    mavenCentral()
    maven { url "https://jitpack.io" }
  }
```

In your ```app/build.gradle```, import the project
```kotlin
// In app/build.gradle
  dependencies {
    ...
    implementation 'com.github.iideprived:JetpackComposeComponents:0.1.6'
  }
```



## List of Components
This will be regularly updated to show examples of the components provided
Many components do not have access to ```Modifier``` because they are intended to enforce consistency across the application.
This library will be growing

### Buttons
When the ```onClick``` is the most important
#### Rounded Button
This is a customizable button that could contain just an icon, just text, or both.
```kotlin
  @Composable
  @Preview
  private fun RoundedButtonPreview() {
      MaterialTheme {
          Column(verticalArrangement = Arrangement.spacedBy(4.dp)) {
              "Details".RoundedButton(icon = Icons.Rounded.ArrowDropDown)
              "Location".RoundedButton()
              "".RoundedButton(icon = Icons.Rounded.Place)
          }
      }
  }
```
<img width="241" alt="image" src="https://github.com/iideprived/JetpackComposeComponents/assets/117201446/5a45581c-112d-4f01-9928-35b8492cdf25">

#### Extended Text Button
This button will expand to the full size of it's container. This is nice to add at the bottom of a screen.
```kotlin
  @Composable
  @Preview
  private fun ExtendedTextButtonPreview() {
    MaterialTheme {
      Column( ... ) {
       ...
       ExtendedTextButton(text = "I Don't Have An Account") { ... }
      }
    }
  }
```
<img width="256" alt="image" src="https://github.com/iideprived/JetpackComposeComponents/assets/117201446/ac634f50-321d-41d0-b64f-523c77a6e671">

#### ToggleButton
This button is just an icon when it's not selected, and expands to show that it is selected.
Showing visual cues beides a click indicator or color is necessary for accessibility reasons. Color-blind users would have
a hard time distinguishing between a selected and unselected component in some cases, without implementing multiple
cues.
```kotlin
  @Composable
  @Preview
  private fun ToggleButton() {
    var selectedItem by remember { mutableIntStateOf(0) }
    MaterialTheme {
      Box( ... ) {
        ...
        Row ( ... ) {
          ToggleButton(
                    isSelected = selectedItem == 1,
                    text = "Friends",
                    icon = Icons.Rounded.PersonAddAlt
                ) { selectedItem = if (selectedItem == 1) 0 else 1}

                ToggleButton(
                    isSelected = selectedItem == 1,
                    text = "Messages",
                    icon = Icons.Rounded.Message
                ) { selectedItem = if (selectedItem == 2) 0 else 2}

                ToggleButton(
                    isSelected = selectedItem == 1,
                    text = "Explore",
                    icon = Icons.Rounded.Search
                ) { selectedItem = if (selectedItem == 3) 0 else 3}
          }
      }
  }
}
```
<img width="248" alt="image" src="https://github.com/iideprived/JetpackComposeComponents/assets/117201446/c90f0d09-ec59-4416-8ec1-06078816f1ed">
<img width="246" alt="image" src="https://github.com/iideprived/JetpackComposeComponents/assets/117201446/9d952386-b4ea-4b69-a48e-59c74a8394a0">

