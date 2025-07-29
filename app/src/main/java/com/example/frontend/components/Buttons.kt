package com.example.frontend.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun CustomButton(
    text: String,
    enabled: Boolean = true,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFFBCCCDC),
    textColor: Color = Color.Black,
    shape: RoundedCornerShape = RoundedCornerShape(12.dp),
    disabledBackgroundAlpha: Float = 0.3f,
    fontWeight: FontWeight = FontWeight.Bold,
    modifier: Modifier = Modifier,
    buttonHeight: Dp = 50.dp,
    icon: (@Composable (() -> Unit))? = null,
    alignIconLeft: Boolean = false,
    cornerRadius: Dp = 12.dp
) {
    Button(
        onClick = onClick,
        modifier = modifier
            .fillMaxWidth()
            .height(buttonHeight)
            .shadow(0.dp, shape = shape),
        enabled = enabled,
        shape = RoundedCornerShape(cornerRadius),
        colors = ButtonDefaults.buttonColors(
            containerColor = backgroundColor,
            contentColor = textColor,
            disabledContainerColor = backgroundColor.copy(alpha = disabledBackgroundAlpha),
            disabledContentColor = textColor.copy(alpha = 0.4f)
        ),
        elevation = ButtonDefaults.buttonElevation(0.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (icon != null) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    contentAlignment = Alignment.CenterStart
                ) {
                    icon()
                }
            } else {
                Spacer(modifier = Modifier.weight(1f))
            }

            Text(
                text = text,
                fontSize = 16.sp,
                fontWeight = fontWeight,
                color = textColor,
                modifier = Modifier.weight(3f),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.weight(1f))
        }
    }
}

@Composable
fun NotificationBadge(count: Int) {
    if (count > 0) {
        Box(
            modifier = Modifier
                .size(14.dp)
                .background(Color.Red, shape = CircleShape)
                .offset(x = 10.dp, y = (-5).dp)
        ) {
            androidx.compose.material.Text(
                text = count.toString(),
                color = Color.White,
                fontSize = 8.sp,
                modifier = Modifier.align(Alignment.Center)
            )
        }
    }
}

@Composable
fun SettingsActionButton(
    text: String,
    onClick: () -> Unit,
    backgroundColor: Color = Color(0xFF9AA6B2),
    contentColor: Color = Color.Black,
    modifier: Modifier = Modifier
        .fillMaxWidth()
        .padding(horizontal = 16.dp, vertical = 8.dp)
        .height(36.dp),
    icon: (@Composable () -> Unit)? = null
) {
    androidx.compose.material.Button(
        onClick = onClick,
        modifier = modifier,
        shape = RoundedCornerShape(10.dp),
        colors = androidx.compose.material.ButtonDefaults.buttonColors(
            backgroundColor = backgroundColor,
            contentColor = contentColor
        ),
        contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp)
    ) {
        if (icon != null) {
            icon()
            Spacer(modifier = Modifier.width(8.dp))
        }
        Text(
            text,
            fontSize = 14.sp,
            fontWeight = FontWeight.Medium,
            color = contentColor
        )
    }
}