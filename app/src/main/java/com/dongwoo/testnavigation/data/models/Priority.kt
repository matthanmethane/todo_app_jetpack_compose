package com.dongwoo.testnavigation.data.models

import androidx.compose.ui.graphics.Color
import com.dongwoo.testnavigation.ui.theme.HighPriorityColor
import com.dongwoo.testnavigation.ui.theme.LowPriorityColor
import com.dongwoo.testnavigation.ui.theme.MediumPriorityColor
import com.dongwoo.testnavigation.ui.theme.NonePriorityColor

enum class Priority(val color: Color){
    LOW(LowPriorityColor),
    MEDIUM(MediumPriorityColor),
    HIGH(HighPriorityColor),
    NONE(NonePriorityColor)
}
