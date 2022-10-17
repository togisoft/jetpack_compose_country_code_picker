# Jetpack Compose Country Code Picker

Jetpack Compose Country Code Picker

<a href="https://www.buymeacoffee.com/togitech" target="_blank"><img src="https://cdn.buymeacoffee.com/buttons/v2/default-yellow.png" alt="Buy Me A Coffee" style="height: 60px !important;width: 217px !important;" ></a>

<h1>Updated</h1>

If you are looking for Country Phone Code Picker for Jetpack Compose you can use the package.


* Country numbers hints
* Phone number visualTransformation (Automatic number formatting)
* Automatic country recognition (detection by sim card if sim card is inserted)
* With TextField
* Can Customize
* Added language translations
* Added clear text button
* Dialog changed

Languages:

* Turkish
* English
* Italian
* Arabic
* Russian

New features will be added every day. This project is open source without any profit motive.

For language support, you can translate the file below and send it to me.
https://github.com/togisoft/jetpack_compose_country_code_picker/blob/master/ccp/src/main/res/values/strings.xml

<h3>Screenshots</h3>
<div class="row">
  <img src="screenshots/1.png" width="300"> 
  <img src="screenshots/2.png" width="300"> 
  <img src="screenshots/3.png" width="300"> 
  <img src="screenshots/4.png" width="300"> 
  <img src="screenshots/5.png" width="300"> 
  <img src="screenshots/6.png" width="300"> 
  <img src="screenshots/7.png" width="300"> 
  <img src="screenshots/8.png" width="300"> 
 </div>


**** Specifications ****

<h3> DEFAULT </h3>

```kotlin
@Composable
fun TogiCountryCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
)

```  

<h3> Rounded </h3>

```kotlin
@Composable
fun TogiRoundedPicker(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    shape: Shape = RoundedCornerShape(24.dp),
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unFocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
)

```  

<h3> Bottom Text Field </h3>

```kotlin
@Composable
fun TogiBottomCodePicker(
    text: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    color: Color = MaterialTheme.colors.background,
    showCountryCode: Boolean = true,
    showCountryFlag: Boolean = true,
    showCountryName: Boolean = true,
    defaultCountry: CountryData,
    pickedCountry: (CountryData) -> Unit,
    focusedBorderColor: Color = MaterialTheme.colors.primary,
    unfocusedBorderColor: Color = MaterialTheme.colors.onSecondary,
    cursorColor: Color = MaterialTheme.colors.primary,
    error: Boolean,
    rowPadding: Modifier = modifier.padding(vertical = 16.dp, horizontal = 16.dp)
)

```  


<h3> Rounded Field Usage </h3>

```kotlin
val context = LocalContext.current
var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode(context)) }
var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
val phoneNumber = rememberSaveable { mutableStateOf("") }
var isValidPhone by remember { mutableStateOf(true) }

TogiRoundedPicker(
    value = phoneNumber.value,
    onValueChange = { phoneNumber.value = it },
    defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
    pickedCountry = {
        phoneCode = it.countryPhoneCode
        defaultLang = it.countryCode
    },
    error = isValidPhone
)


```  

```kotlin
  // With Country Phone Code
@Composable
fun SelectCountryWithCountryCode() {
    val context = LocalContext.current
    var phoneCode by rememberSaveable { mutableStateOf(getDefaultPhoneCode(context)) }
    var defaultLang by rememberSaveable { mutableStateOf(getDefaultLangCode(context)) }
    val phoneNumber = rememberSaveable { mutableStateOf("") }
    var isValidPhone by remember { mutableStateOf(true) }
    var verifyText by remember { mutableStateOf("") }
    Column(
        modifier = Modifier.padding(16.dp)
    ) {
        Text(
            text = verifyText,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .fillMaxWidth()
                .wrapContentSize(Alignment.Center)
        )
        TogiCountryCodePicker(
            pickedCountry = {
                phoneCode = it.countryPhoneCode
                defaultLang = it.countryCode
            },
            defaultCountry = getLibCountries().single { it.countryCode == defaultLang },
            focusedBorderColor = MaterialTheme.colors.primary,
            unfocusedBorderColor = MaterialTheme.colors.primary,
            error = isValidPhone,
            text = phoneNumber.value,
            onValueChange = { phoneNumber.value = it }
        )

        val fullPhoneNumber = "$phoneCode${phoneNumber.value}"
        val checkPhoneNumber = checkPhoneNumber(
            phone = phoneNumber.value,
            fullPhoneNumber = fullPhoneNumber,
            countryCode = defaultLang
        )
        Button(
            onClick = {
                verifyText = if (checkPhoneNumber) {
                    isValidPhone = true
                    "Phone Number Correct"
                } else {
                    isValidPhone = false
                    "Phone Number is Wrong"

                }
            },
            modifier = Modifier.fillMaxWidth()
                .padding(16.dp)
                .height(60.dp)
        ) {
            Text(text = "Phone Verify")
        }
    }
}
```

<h3><- ********* Extras ********* -></h3>

* focusedBorderColor = TextField Border Color
* unfocusedBorderColor = TextField Unfocused Border Color
* cursorColor = TextField Cursor Color


<h3> How to add in your project </h3>

In the build.gradle add maven central repository

```groovy
    repositories {
    maven { url 'https://jitpack.io' }
}

```

Step 2. Add the dependency

```
  dependencies {
	    implementation 'com.github.togisoft:jetpack_compose_country_code_picker:1.1.3'

	}  
```    


