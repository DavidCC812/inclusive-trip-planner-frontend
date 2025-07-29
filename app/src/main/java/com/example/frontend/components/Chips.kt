package com.example.frontend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import com.google.accompanist.flowlayout.FlowRow
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.google.accompanist.flowlayout.FlowMainAxisAlignment


@Composable
fun CustomChipGroup(
    options: List<String>,
    selectedOptions: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit,
    disabledOptions: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    val chunkedOptions = options.chunked(2)

    Box(
        modifier = modifier.fillMaxWidth(),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            chunkedOptions.forEach { rowOptions ->
                Row(
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.padding(vertical = 4.dp)
                ) {
                    rowOptions.forEach { option ->
                        val isSelected = selectedOptions.contains(option)
                        val isDisabled = disabledOptions.contains(option)

                        Chip(
                            text = option,
                            isSelected = isSelected,
                            onClick = {
                                if (!isDisabled) {
                                    val newSelection = if (isSelected) {
                                        selectedOptions - option
                                    } else {
                                        selectedOptions + option
                                    }
                                    onSelectionChanged(newSelection)
                                }
                            },
                            isDisabled = isDisabled
                        )
                        Spacer(modifier = Modifier.width(8.dp)) // spacing between chips
                    }
                }
            }
        }
    }
}

@Composable
fun CustomChipGrid(
    options: List<String>,
    selectedOptions: Set<String>,
    onSelectionChanged: (Set<String>) -> Unit,
    disabledOptions: List<String> = emptyList(),
    modifier: Modifier = Modifier
) {
    FlowRow(
        modifier = modifier.fillMaxWidth(),
        mainAxisSpacing = 16.dp,
        crossAxisSpacing = 16.dp,
        mainAxisAlignment = FlowMainAxisAlignment.Center
    ) {
        options.forEach { option ->
            val isSelected = selectedOptions.contains(option)
            val isDisabled = disabledOptions.contains(option)

            Chip(
                text = option,
                isSelected = isSelected,
                onClick = {
                    if (!isDisabled) {
                        val newSelection = if (isSelected) {
                            selectedOptions - option
                        } else {
                            selectedOptions + option
                        }
                        onSelectionChanged(newSelection)
                    }
                },
                isDisabled = isDisabled
            )
        }
    }
}


@Composable
fun Chip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    isDisabled: Boolean = false
) {
    val backgroundColor = when {
        isDisabled -> Color(0xFFBCCCDC).copy(alpha = 0.5f)
        isSelected -> Color(0xFF9AA6B2)
        else -> Color(0xFFBCCCDC)
    }

    val textColor = when {
        isDisabled -> Color.Black.copy(alpha = 0.5f)
        isSelected -> Color.White
        else -> Color.Black
    }

    val borderColor = when {
        isDisabled -> Color(0xFFBCCCDC).copy(alpha = 0.5f)
        isSelected -> Color(0xFFBCCCDC)
        else -> Color(0xFF9AA6B2)
    }

    Box(
        modifier = Modifier
            .background(backgroundColor, shape = RoundedCornerShape(16.dp))
            .border(2.dp, borderColor, RoundedCornerShape(16.dp))
            .clickable(
                enabled = !isDisabled,
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) { onClick() }
            .padding(horizontal = 20.dp, vertical = 10.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = text, fontSize = 16.sp, fontWeight = FontWeight.Bold, color = textColor, textAlign = TextAlign.Center)
    }
}