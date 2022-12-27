package com.dongwoo.testnavigation.ui.screens.task

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.Divider
import androidx.compose.material.MaterialTheme
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.dongwoo.testnavigation.R
import com.dongwoo.testnavigation.data.models.Priority
import com.dongwoo.testnavigation.ui.components.PriorityDropDown
import com.dongwoo.testnavigation.ui.theme.LARGE_PADDING
import com.dongwoo.testnavigation.ui.theme.MEDIUM_PADDING

@Composable
fun TaskContent(
    title: String,
    onTitleChange: (String) -> Unit,
    description: String,
    onDescriptionChange: (String) -> Unit,
    priority: Priority,
    onPrioritySelected: (Priority) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colors.background)
            .padding(all = LARGE_PADDING)
    ) {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth(),
            value = title,
            onValueChange = { onTitleChange(it) },
            label = { Text(text = stringResource(R.string.title)) },
            textStyle = MaterialTheme.typography.body1,
            singleLine = true
        )
        Divider(
            modifier = Modifier
                .height(MEDIUM_PADDING),
            color = MaterialTheme.colors.background
        )
        PriorityDropDown(
            priority = priority,
            onPrioritySelected = { onPrioritySelected(it) }
        )
        OutlinedTextField(
            modifier = Modifier
                .fillMaxSize(),
            value = description,
            onValueChange = { onDescriptionChange(it) },
            label = { Text(text = stringResource(R.string.description)) }
        )
    }
}

@Composable
@Preview
fun TaskContentPreview(){
    TaskContent(
        title = "",
        onTitleChange = {},
        description = "",
        onDescriptionChange = {},
        priority = Priority.HIGH,
        onPrioritySelected = {}
    )
}