package com.dongwoo.testnavigation.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntSize
import androidx.compose.ui.unit.dp
import com.dongwoo.testnavigation.R
import com.dongwoo.testnavigation.data.models.Priority
import com.dongwoo.testnavigation.ui.theme.MEDIUM_PADDING
import com.dongwoo.testnavigation.ui.theme.PRIORITY_DROPDOWN_HEIGHT
import com.dongwoo.testnavigation.ui.theme.PRIORITY_INDICATOR_SIZE

@Composable
fun PriorityDropDown(
    priority : Priority,
    onPrioritySelected : (Priority) ->  Unit
) {
    var expanded by remember { mutableStateOf(false) }
    val angle: Float by animateFloatAsState(
        targetValue = if (expanded) 180f else 0f
    )
    var parentSize by remember { mutableStateOf(IntSize.Zero) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .onGloballyPositioned {
                parentSize = it.size
            }
            .background(MaterialTheme.colors.background)
            .height(PRIORITY_DROPDOWN_HEIGHT)
            .clickable { expanded = true }
            .border(
                width = 1.dp,
                color = MaterialTheme.colors.onSurface.copy(alpha = ContentAlpha.disabled),
                shape = MaterialTheme.shapes.small
            ),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Canvas(
            modifier = Modifier
                .size(PRIORITY_INDICATOR_SIZE)
                .weight(1f)
        ){
            drawCircle(color = priority.color)
        }
        Text(
            modifier = Modifier
                .weight(8f),
            text = priority.name,
            style = MaterialTheme.typography.subtitle2
        )
        IconButton(
            modifier = Modifier
                .alpha(ContentAlpha.medium)
                .rotate(degrees = angle)
                .weight(weight = 1f),
            onClick = { expanded = true }
        ) {
            Icon(
                imageVector = Icons.Filled.ArrowDropDown,
                contentDescription = stringResource(R.string.drop_down_arrow)
            )
        }
        DropdownMenu(
            modifier = Modifier.width(with(LocalDensity.current) { parentSize.width.toDp() } ),
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().slice(0..2).forEach { priority ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onPrioritySelected(priority)
                    }
                ) {
                    PriorityItem(priority = priority)
                }
            }
        }
    }
}

@Composable
@Preview
fun PriorityDropDownPreview(){
    PriorityDropDown(
        priority = Priority.LOW,
        onPrioritySelected = {}
    )
}