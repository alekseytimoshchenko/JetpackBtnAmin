package com.example.jetpackbtnamin

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.core.*
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.waitForUpOrCancellation
import androidx.compose.foundation.indication
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.PressInteraction
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Button
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Surface
import androidx.compose.material.Text
import androidx.compose.material.ripple.rememberRipple
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.composed
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.jetpackbtnamin.ui.theme.JetpackBtnAminTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            JetpackBtnAminTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colors.background) {
                    Buttons()
                }
            }
        }
    }
}

private enum class ButtonState { Idle, Pressed }

@Composable
private fun Buttons() {
    Column(horizontalAlignment = Alignment.CenterHorizontally) {
        Spacer(modifier = Modifier.height(16.dp))
        PulsateEffect()
        Spacer(modifier = Modifier.height(16.dp))
        PressEffect()
        Spacer(modifier = Modifier.height(16.dp))
        ShakeEffect()
        Spacer(modifier = Modifier.height(16.dp))
        AnimateShapeTouch()
    }
}

/*=== Pulsate Effect ===*/
@Composable
fun PulsateEffect() {
    Button(
        modifier = Modifier.pulsateClick(),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(12.dp), 
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = "Pulsate Effect")
    }
}

// Create modifier for effect
fun Modifier.pulsateClick() = composed {
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    //Animate scale for effect
    val scale by animateFloatAsState(targetValue = if (buttonState == ButtonState.Pressed) 0.7f else 1f)

    this@pulsateClick
        .graphicsLayer {
            scaleX = scale
            scaleY = scale
        }
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null
        ) { }
}

/*=== Press Effect ===*/
@Composable
private fun PressEffect() {
    Button(
        modifier = Modifier.pressClick(),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = "Press Effect")
    }
}

// Implement modifier for press effect
private fun Modifier.pressClick() = composed {
    // define the button state
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }

    // define this ty value
    val ty by animateFloatAsState(targetValue = if (buttonState == ButtonState.Pressed) 0f else -20f)

    this@pressClick
        .graphicsLayer {
            translationY = ty
        }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {}
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

/*=== Shake Effect ===*/
@Composable
private fun ShakeEffect() {
    Button(
        modifier = Modifier.shakeClick(),
        onClick = { /*TODO*/ },
        shape = RoundedCornerShape(12.dp),
        contentPadding = PaddingValues(16.dp)
    ) {
        Text(text = "Shake Effect")
    }
}

private fun Modifier.shakeClick() = composed { 
    var buttonState by remember { mutableStateOf(ButtonState.Idle) }
    
    val tx by animateFloatAsState(
        targetValue = if (buttonState == ButtonState.Pressed) 0f else -50f,
        animationSpec = repeatable(
            iterations = 2,
            animation = tween(durationMillis = 50, easing = LinearEasing),
            repeatMode = RepeatMode.Reverse
        )
    )

    this@shakeClick
        .graphicsLayer { translationX = tx }
        .clickable(
            interactionSource = remember { MutableInteractionSource() },
            indication = null,
            onClick = {}
        )
        .pointerInput(buttonState) {
            awaitPointerEventScope {
                buttonState = if (buttonState == ButtonState.Pressed) {
                    waitForUpOrCancellation()
                    ButtonState.Idle
                } else {
                    awaitFirstDown(false)
                    ButtonState.Pressed
                }
            }
        }
}

/*=== Animate Shape Touch Effect ===*/
@Composable
private fun AnimateShapeTouch() {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed = interactionSource.collectIsPressedAsState()
    val cornerRadius by animateDpAsState(targetValue = if (isPressed.value) 10.dp else 50.dp)

    Box(
        modifier = Modifier
            .background(
                color = Color.Red,
                RoundedCornerShape(cornerRadius)
            )
            .size(100.dp)
            .clip(RoundedCornerShape(cornerRadius))
            .clickable(
                interactionSource = interactionSource,
                indication = rememberRipple(),
                onClick = {}
            )
            .padding(horizontal = 20.dp),
        contentAlignment = Alignment.Center
    ) {
        Text(text = "Animate Shape Touch", color = Color.White)
    }
}


@Preview(showBackground = true)
@Composable
fun DefaultPreview() {
    JetpackBtnAminTheme {
        Buttons()
    }
}