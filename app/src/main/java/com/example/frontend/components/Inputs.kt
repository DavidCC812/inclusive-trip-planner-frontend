package com.example.frontend.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.frontend.R

@Composable
fun CustomInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    keyboardType: KeyboardType,
    enabled: Boolean = true,
    visualTransformation: VisualTransformation = VisualTransformation.None,
    backgroundAlpha: Float = if (enabled) 0.15f else 0.1f,
    disabledBackgroundColor: Color = Color(0xFFE0E0E0),
    leadingIcon: @Composable (() -> Unit)? = null,
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
) {
    var showPassword by remember { mutableStateOf(false) }

    OutlinedTextField(
        value = value,
        onValueChange = { if (enabled) onValueChange(it) },
        label = { Text(label, color = Color.Black.copy(alpha = 0.8f)) },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        visualTransformation = if (keyboardType == KeyboardType.Password && !showPassword) {
            PasswordVisualTransformation()
        } else {
            visualTransformation
        },
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        enabled = enabled,
        leadingIcon = leadingIcon,
        trailingIcon = {
            if (keyboardType == KeyboardType.Password) {
                IconButton(onClick = { showPassword = !showPassword }) {
                    Icon(
                        imageVector = if (showPassword) Icons.Filled.Visibility else Icons.Filled.VisibilityOff,
                        contentDescription = if (showPassword) "Hide password" else "Show password",
                        tint = Color.Black
                    )
                }
            }
        },
        textStyle = textStyle.copy(
            color = if (enabled) Color.Black else Color.Black.copy(alpha = 0.6f)
        ),
        colors = TextFieldDefaults.colors(
            focusedContainerColor = if (enabled) Color(0xFFF8FAFC) else disabledBackgroundColor,
            unfocusedContainerColor = if (enabled) Color(0xFFF8FAFC) else disabledBackgroundColor,
            disabledContainerColor = disabledBackgroundColor,
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Black.copy(alpha = 0.6f),
            errorTextColor = Color.Red,
            focusedIndicatorColor = Color(0xFF9AA6B2),
            unfocusedIndicatorColor = Color(0xFFBCCCDC),
            errorIndicatorColor = Color.Red
        ),
        shape = RoundedCornerShape(12.dp)
    )
}

@Composable
fun PhoneInputField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    isError: Boolean,
    keyboardType: KeyboardType = KeyboardType.Phone,
    countryCode: String = "+33",
    modifier: Modifier = Modifier,
    textStyle: TextStyle = TextStyle(fontSize = 16.sp, color = Color.Black)
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label, color = Color.Black.copy(alpha = 0.8f)) },
        isError = isError,
        keyboardOptions = KeyboardOptions(keyboardType = keyboardType),
        modifier = modifier
            .fillMaxWidth()
            .padding(horizontal = 4.dp),
        textStyle = textStyle.copy(
            fontSize = 20.sp,
            color = Color.Black,
            baselineShift = androidx.compose.ui.text.style.BaselineShift(-0.05f)
        ),
        leadingIcon = {
            Row(
                modifier = Modifier
                    .padding(start = 8.dp, top = 1.dp)
                    .height(56.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Image(
                    painter = painterResource(id = R.drawable.ic_france_flag),
                    contentDescription = "France Flag",
                    modifier = Modifier.size(22.dp)
                )
                Spacer(modifier = Modifier.width(6.dp))
                Text(
                    text = countryCode,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color.Black
                )
            }
        },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = Color(0xFFF8FAFC),
            unfocusedContainerColor = Color(0xFFF8FAFC),
            disabledContainerColor = Color(0xFFE0E0E0),
            focusedTextColor = Color.Black,
            unfocusedTextColor = Color.Black,
            disabledTextColor = Color.Black.copy(alpha = 0.6f),
            errorTextColor = Color.Red,
            cursorColor = Color.Black,
            focusedIndicatorColor = Color(0xFF9AA6B2),
            unfocusedIndicatorColor = Color(0xFFBCCCDC),
            disabledIndicatorColor = Color.Black.copy(alpha = 0.5f),
            errorIndicatorColor = Color.Red
        ),
        shape = RoundedCornerShape(12.dp)
    )
}