package com.iideprived.jetpackcomposecomponents.ui.inputs

import android.util.Log
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.*
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.*
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.iideprived.jetpackcomposecomponents.ui.inputs.AnimatedTextField
import com.iideprived.jetpackcomposecomponents.data.PhoneNumberVisualTransformation
import com.iideprived.jetpackcomposecomponents.utils.countrydata.Country
import kotlinx.coroutines.delay

@Composable
fun Picker(
    modifier: Modifier = Modifier,
    country: Country = Country.getFromLocale(),
    contentPadding: PaddingValues = PaddingValues(4.dp),
    onCountryChange: (Country) -> Unit,
    pickerType: PickerType = PickerType.Flag,
    showCountryCode: Boolean = true,
    textStyle: TextStyle = MaterialTheme.typography.h6.copy(fontWeight = FontWeight.Normal),
    textColor: Color = MaterialTheme.colors.onBackground,
    showDropdown: Boolean = true,
    icon: (@Composable () -> Unit)? = {
        Icon(
            modifier = Modifier
                .rotate(90f)
                .alpha(0.5f)
                .size(24.dp),
            imageVector = Icons.Rounded.ChevronRight,
            tint = MaterialTheme.colors.onBackground,
            contentDescription = "Dropdown"
        )
    },
    fullScreenDialog: Boolean = true,
    showCountryList: Boolean = false,
    countries: @Composable (Country) -> Country? = { countryList(it) },
)
{
    var showCountryListDialog by remember { mutableStateOf(showCountryList) }
    if (showCountryListDialog){
        Dialog(
            onDismissRequest = {
                showCountryListDialog = false
            },
            properties = DialogProperties(
                usePlatformDefaultWidth = !fullScreenDialog
            )
        ) {
            val temp = countries(country) ?: country
            if (temp != country){
                showCountryListDialog = false
                onCountryChange(temp)
            }
        }
    }
    Row(
        modifier
            .clickable(
                indication = rememberRipple(

                    bounded = true,
                    color = Color.Black,
                ),
                interactionSource = MutableInteractionSource()
            ) { showCountryListDialog = true }
            .padding(contentPadding),
        horizontalArrangement = Arrangement.SpaceEvenly,
        verticalAlignment = Alignment.CenterVertically) {
        Text(
            text = if (pickerType == PickerType.Flag) country.flagEmoji else country.name,
            style = textStyle,
            color = textColor
        )
        if (showCountryCode){
            Text(
                modifier = Modifier.padding(start = 8.dp),
                text = country.countryCode,
                style = textStyle,
                color = textColor
            )
        }
        if (showDropdown) icon?.invoke()
    }
}

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun countryList(
    currentCountry: Country,
    countryList: List<Country> = Country.all()
) : Country? {
    var selectedCountry : Country? by remember { mutableStateOf(currentCountry) }
    Column(
        Modifier
            .fillMaxSize()
            .background(color = MaterialTheme.colors.background)
            .padding(16.dp)

    ) {
        var query by remember { mutableStateOf("") }
        var countries by remember { mutableStateOf(countryList) }
        Text(
            modifier = Modifier.padding(bottom = 6.dp),
            text = "Countries",
            style = MaterialTheme.typography.h5,
            color = MaterialTheme.colors.onBackground
        )
        countrySearch(query = query, onValueChange = {query = it; countries = Country.searchAll(query, countryList)} )
        Spacer(Modifier.height(4.dp))
        Divider(color = MaterialTheme.colors.onBackground.copy(0.5f))
        LazyColumn(
            Modifier
                .weight(1f)
                .fillMaxWidth()
        ){
            if (countries.isEmpty()){
                item {
                    Text(
                        text = "No Results Found!",
                        style = MaterialTheme.typography.h6,
                        color = Color.Gray,
                        modifier = Modifier
                            .padding(vertical = 60.dp)
                            .fillMaxWidth(),
                        textAlign = TextAlign.Center
                    )
                }
            }else {
                items(countries, { it }){
                    val prevCountry = countries.getOrNull(countries.indexOf(it) - 1)
                    if (prevCountry?.country?.first() != it.country.first()) {
                        Column(Modifier.animateItemPlacement()) {
                            Text(
                                modifier = Modifier
                                    .alpha(0.6f)
                                    .padding(vertical = 4.dp, horizontal = 12.dp)
                                    .animateItemPlacement(),
                                text = it.country.first().uppercase(),
                                style = MaterialTheme.typography.h6,
                                color = MaterialTheme.colors.onBackground
                            )
                            it.ListItem(modifier = Modifier
                                .animateItemPlacement()
                                .clickable {
                                    selectedCountry = if (selectedCountry == it) null else it
                                })
                        }
                    }else {
                        it.ListItem(modifier = Modifier
                            .animateItemPlacement()
                            .clickable { selectedCountry = if (selectedCountry == it) null else it })
                    }
                }
            }
        }
    }
    return selectedCountry
}

@Composable
fun countrySearch(
    query: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    searchBox: @Composable () -> Unit = {
        TextField(
            textStyle = MaterialTheme.typography.body1.copy(color = MaterialTheme.colors.onSurface),
            singleLine = true,
            maxLines = 1,
            modifier = Modifier
                .padding(8.dp)
                .fillMaxWidth()
                .border(
                    border = BorderStroke(
                        color = MaterialTheme.colors.onBackground,
                        width = 1.dp
                    ),
                    shape = CircleShape
                )
                .background(
                    color = Color.Transparent,
                    shape = CircleShape
                )
                .clip(CircleShape),
            value = query,
            onValueChange = onValueChange,
            leadingIcon = {
                Icon(
                    modifier = Modifier
                        .padding(end = 8.dp)
                        .size(16.dp),
                    imageVector = Icons.Rounded.Search,
                    contentDescription = "Search Countries",
                    tint = MaterialTheme.colors.onSurface
                )
            },
            label = {
                Text(
                    text = "Search Countries",
                    style = MaterialTheme.typography.body1,
                    color = MaterialTheme.colors.onSurface
                )
            },
            placeholder = {
                var exampleType by remember { mutableStateOf(0) }
                val randomCountry = Country.values().random()
                val typeText = when (exampleType){
                    0 -> "Emoji ( ${randomCountry.flagEmoji} )"
                    1 -> "Country Code ( ${randomCountry.countryCode} ) "
                    2 -> "Abbreviation ( ${randomCountry.name} )"
                    else -> "Country Name"
                }
                LaunchedEffect(key1 = exampleType){
                    delay(2500)
                    exampleType = (exampleType + 1) % 4
                }
                AnimatedContent(targetState = exampleType) {
                    Text(
                        modifier = Modifier.alpha(0.5f),
                        text = "By $typeText",
                        color = MaterialTheme.colors.onSurface,
                        style = MaterialTheme.typography.body1,
                    )
                }
            }
        )
    },
    countryList: List<Country> = Country.values().toList(),
) : List<Country> {

    Box(modifier) { searchBox() }

    return Country.searchAll(query, countryList)
}

enum class PickerType {
    Flag,
    Code
}

@Composable
fun Country.ListItem(
    modifier: Modifier = Modifier
){
    Column(modifier) {
        Row(
            Modifier
                .background(color = MaterialTheme.colors.background)
                .fillMaxWidth()
                .padding(horizontal = 12.dp, 16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = flagEmoji,
                fontSize = 24.sp
            )
            Text(
                modifier = Modifier.padding(start = 12.dp),
                text = country,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground
            )
            Spacer(Modifier.weight(1f))
            Text(
                text = countryCode,
                style = MaterialTheme.typography.body1,
                color = MaterialTheme.colors.onBackground.copy(0.8f)
            )
        }
        Divider(color = MaterialTheme.colors.onBackground.copy(0.3f))
    }
}

@Composable
@Preview
private fun PickerPreview(){
    var country by remember { mutableStateOf(Country.getFromLocale())}
    MaterialTheme {
        var phone by remember { mutableStateOf("") }
        Column(
            Modifier
                .fillMaxSize()
                .background(
                    brush = Brush.verticalGradient(
                        listOf(
                            Color(0xFFFFCB37),
                            Color(0xFFFFBF2F)
                        )
                    )
                )
                .padding(16.dp)
        ) {
            Icon(
                modifier = Modifier
                    .padding(bottom = 4.dp)
                    .size(32.dp),
                imageVector = Icons.Rounded.ChevronLeft,
                contentDescription = "Back",
                tint = Color.Black,
            )

            Column(
                Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(8.dp)) {
                Text(
                    modifier = Modifier.padding(bottom = 12.dp),
                    text = "What's your number?",
                    style = MaterialTheme.typography.h4,
                    color = Color.Black,
                    fontWeight = FontWeight.SemiBold,
                )
                Text(
                    modifier = Modifier.padding(bottom = 24.dp),
                    text = "We protect our community by making sure everyone on Bumble is real.",
                    style = MaterialTheme.typography.body1,
                    color = Color.Black,
                )
                Row(
                    Modifier
                        .fillMaxWidth()
                        .height(IntrinsicSize.Min),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Picker(
                        country = country,
                        modifier = Modifier
                            .fillMaxHeight()
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            ),
                        pickerType = PickerType.Code,
                        textColor = Color.Black,
                        onCountryChange = { country = it; Log.d("Picker", "Country Picked = $it") }
                    )
                    Spacer(Modifier.width(24.dp))
                    var focused by remember { mutableStateOf(false) }
                    AnimatedTextField(
                        modifier = Modifier
                            .weight(0.65f)
                            .fillMaxHeight()
                            .shadow(
                                animateDpAsState(if (focused) 5.dp else 0.dp).value,
                                RoundedCornerShape(8.dp)
                            )
                            .background(
                                color = Color.White,
                                shape = RoundedCornerShape(8.dp)
                            )
                        ,
                        onFocusChanged = { focused = it },
                        value = phone,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        cursorBrush = SolidColor(Color(0xFFFFBF2F)),
                        onValueChange = {phone = it},
                        visualTransformation = PhoneNumberVisualTransformation(),
                        placeholder = {
                            Text("Phone number",
                                modifier = Modifier
                                    .alpha(0.5f)
                                    .weight(0.6f),
                                style = MaterialTheme.typography.h6,
                                color = Color.Black,
                                )
                        }
                    )
                }
                Spacer(Modifier.weight(1f))
                Row(Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                    ) {
                    Icon(
                        modifier = Modifier.size(24.dp),
                        imageVector = Icons.Rounded.Lock,
                        contentDescription = "Lock",
                        tint = Color.Black,
                    )
                    Text(
                        modifier = Modifier
                            .padding(start = 12.dp)
                            .weight(1f),
                        text = "We never share this with anyone and it won't be on your profile.",
                        color = Color.Black,
                        style = MaterialTheme.typography.body2

                        ,
                    )
                    IconButton(
                        modifier = Modifier
                            .padding(start = 24.dp)
                            .size(48.dp)
                            .background(
                                color = Color.White,
                                shape = CircleShape
                            )
                            .alpha(if (phone.length > 5) 1f else 0.5f)
                        ,
                        onClick = {  }
                    ) {
                        Icon(
                            modifier = Modifier.size(24.dp),
                            imageVector = Icons.Rounded.ChevronRight,
                            contentDescription = "Go",
                            tint = Color(0xFFFFBF2F),
                        )
                    }
                }
            }
        }
    }
}

@Composable
@Preview
private fun ListItemPreview(){
    MaterialTheme {
        Column {
            LazyColumn(
                Modifier
                    .fillMaxSize()
                    .background(color = MaterialTheme.colors.background)){
                items(Country.values(), key = {it} ){
                     it.ListItem()
                }
            }
        }
    }
}