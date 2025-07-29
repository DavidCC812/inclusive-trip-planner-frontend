package com.example.frontend.components

import android.app.DatePickerDialog
import android.content.Context
import android.widget.DatePicker
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.DpOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.text.SimpleDateFormat
import java.util.*

@Composable
fun SearchBar(
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    selectedFilter: String,
    onFilterChange: (String) -> Unit,
    accessibilityFilters: List<String>,
    selectedDates: String,
    onDateChange: (String) -> Unit,
    onSearch: () -> Unit
) {
    var filterExpanded by remember { mutableStateOf(false) }
    val context = LocalContext.current

    Column(modifier = Modifier.fillMaxWidth()) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(Color(0xFFEFEFEF))
                .padding(horizontal = 16.dp, vertical = 12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(id = android.R.drawable.ic_menu_search),
                contentDescription = "Search Icon",
                tint = Color.Black,
                modifier = Modifier.size(22.dp)
            )

            Spacer(modifier = Modifier.width(10.dp))

            BasicTextField(
                value = searchQuery,
                onValueChange = onSearchQueryChange,
                modifier = Modifier.weight(1f),
                textStyle = LocalTextStyle.current.copy(fontSize = 16.sp, color = Color.Black),
                singleLine = true,
                decorationBox = { innerTextField ->
                    Box(modifier = Modifier.fillMaxWidth()) {
                        if (searchQuery.isEmpty()) {
                            Text("Search destinations...", fontSize = 16.sp, color = Color.Gray)
                        }
                        innerTextField()
                    }
                }
            )
        }

        Spacer(modifier = Modifier.height(4.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(10.dp))
                .background(Color(0xFFEFEFEF))
                .padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                modifier = Modifier.weight(1f),
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = { showDatePickerDialog(context, onDateChange) },
                    modifier = Modifier.size(36.dp)
                ) {
                    Icon(
                        imageVector = Icons.Filled.CalendarToday,
                        contentDescription = "Calendar",
                        tint = Color.Black,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Text(
                    text = selectedDates,
                    fontSize = 14.sp,
                    color = Color.Black,
                    modifier = Modifier.padding(start = 2.dp)
                )
            }

            Box {
                IconButton(onClick = { filterExpanded = true }) {
                    Icon(
                        imageVector = Icons.Filled.Tune,
                        contentDescription = "Filter",
                        tint = Color.Black,
                        modifier = Modifier.size(22.dp)
                    )
                }

                DropdownMenu(
                    expanded = filterExpanded,
                    onDismissRequest = { filterExpanded = false },
                    offset = DpOffset(x = 0.dp, y = 0.dp)
                ) {
                    accessibilityFilters.forEach { filter ->
                        DropdownMenuItem(
                            text = { Text(filter) },
                            onClick = {
                                onFilterChange(filter)
                                filterExpanded = false
                            }
                        )
                    }
                }
            }

            IconButton(onClick = onSearch) {
                Icon(
                    imageVector = Icons.Filled.Search,
                    contentDescription = "Search",
                    tint = Color.Black,
                    modifier = Modifier.size(22.dp)
                )
            }
        }
    }
}

fun showDatePickerDialog(context: Context, onDateChange: (String) -> Unit) {
    val calendar = Calendar.getInstance()
    val startDate = Calendar.getInstance()
    val endDate = Calendar.getInstance()

    DatePickerDialog(
        context,
        { _: DatePicker, year: Int, month: Int, dayOfMonth: Int ->
            startDate.set(year, month, dayOfMonth)

            DatePickerDialog(
                context,
                { _: DatePicker, endYear: Int, endMonth: Int, endDay: Int ->
                    endDate.set(endYear, endMonth, endDay)

                    val sdf = SimpleDateFormat("MMM dd", Locale.getDefault())
                    val formattedStart = sdf.format(startDate.time)
                    val formattedEnd = sdf.format(endDate.time)

                    val displayDate =
                        if (startDate.get(Calendar.YEAR) == endDate.get(Calendar.YEAR)) {
                            "$formattedStart - $formattedEnd"
                        } else {
                            "$formattedStart ${startDate.get(Calendar.YEAR)} - $formattedEnd ${
                                endDate.get(
                                    Calendar.YEAR
                                )
                            }"
                        }

                    onDateChange(displayDate)
                },
                endDate.get(Calendar.YEAR),
                endDate.get(Calendar.MONTH),
                endDate.get(Calendar.DAY_OF_MONTH)
            ).show()
        },
        startDate.get(Calendar.YEAR),
        startDate.get(Calendar.MONTH),
        startDate.get(Calendar.DAY_OF_MONTH)
    ).show()
}