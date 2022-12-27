package com.dongwoo.testnavigation.ui.screens.list

import android.util.Log
import androidx.activity.compose.BackHandler
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.layout.Arrangement.End
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.dongwoo.testnavigation.R
import com.dongwoo.testnavigation.data.models.Priority
import com.dongwoo.testnavigation.ui.components.DisplayAlertDialog
import com.dongwoo.testnavigation.ui.components.PriorityItem
import com.dongwoo.testnavigation.ui.theme.*
import com.dongwoo.testnavigation.ui.viewmodels.SharedViewModel
import com.dongwoo.testnavigation.util.Action
import com.dongwoo.testnavigation.util.RequestState
import com.dongwoo.testnavigation.util.SearchAppBarState

@Composable
fun ListAppBar(
    sharedViewModel: SharedViewModel,
    searchAppBarState: SearchAppBarState,
    searchTextState: String
){

    val sortState by sharedViewModel.sortState.collectAsState()

    when(searchAppBarState){
        SearchAppBarState.CLOSED -> {
            DefaultListAppBar(
                onSearchClicked = {
                    sharedViewModel.updateSearchAppBarState(SearchAppBarState.OPENED)
                },
                onSortClicked ={
                    sharedViewModel.persistSortState(priority = it)
                },
                onDeleteAllConfirmed = {
                    sharedViewModel.updateAction(newAction = Action.DELETE_ALL)
                },
                sortState = sortState
            )
        }
        else -> {
            SearchAppBar(
                text = searchTextState,
                onTextChange = { newText ->
                    sharedViewModel.updateSearchTextState(newText)
                },
                onCloseClicked = {
                    sharedViewModel.updateSearchAppBarState(
                        newSearchAppBarState = SearchAppBarState.CLOSED
                    )
                    sharedViewModel.updateSearchTextState(newSearchTextState = "")
                },
                onSearchClicked = { searchQuery ->
                    sharedViewModel.searchDatabase(searchQuery = searchQuery)
                }
            )
        }
    }
}

@Composable
fun DefaultListAppBar(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
    sortState: RequestState<Priority>
){
    TopAppBar(
        backgroundColor = MaterialTheme.colors.topAppBarBackgroundColor,
        title = {
            Text(text = "Tasks", color = MaterialTheme.colors.topAppBarContentColor)
        },
        actions = {
            ListAppBarActions(
                onSearchClicked = onSearchClicked,
                onSortClicked = onSortClicked,
                onDeleteAllConfirmed = onDeleteAllConfirmed,
                sortState = sortState
            )
        }
    )
}

@Composable
fun ListAppBarActions(
    onSearchClicked: () -> Unit,
    onSortClicked: (Priority) -> Unit,
    onDeleteAllConfirmed: () -> Unit,
    sortState: RequestState<Priority>
){
    var openDialog by remember { mutableStateOf(false) }

    DisplayAlertDialog(
        title = "Delete All Tasks?",
        message = "Are you sure to delete all tasks?",
        openDialog = openDialog,
        closeDialog = { openDialog = false },
        onYesClicked = { onDeleteAllConfirmed() }
    )


    SearchAction(onSearchClicked)
    SortAction(
        onSortClicked = onSortClicked,
        sortState = sortState
    )
    DeleteAllAction { openDialog = true }
}

@Composable
fun SearchAction(
    onSearchClicked: () -> Unit
){
    IconButton(
        onClick = { onSearchClicked() }
    ) {
        Icon(
            imageVector = Icons.Filled.Search,
            contentDescription = stringResource(R.string.search_icon),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
    }
}

@Composable
fun SortAction(
    onSortClicked: (Priority) -> Unit,
    sortState: RequestState<Priority>
){
    var expanded by remember { mutableStateOf(false) }

    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_sort_priority),
            contentDescription = stringResource(R.string.sort_priority),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            Priority.values().slice(setOf(0,2,3)).forEach { priority ->
                DropdownMenuItem(
                    onClick = {
                        expanded = false
                        onSortClicked(priority)
                    },

                ) {
                    PriorityDropdownMenuItem(priority = priority, sortState = sortState)
                }
            }
        }
    }
}

@Composable
fun PriorityDropdownMenuItem(
    priority: Priority,
    sortState: RequestState<Priority>
){
    Row(
        modifier = Modifier.width(PRIORITY_DROPDOWN_MENU_ITEM_WIDTH),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        PriorityItem(
            priority = priority
        )
        Spacer(modifier = Modifier.weight(1f))
        Icon(
            modifier = Modifier
                .padding(start = MEDIUM_PADDING)
                .size(16.dp)
                .alpha(
                    if (sortState is RequestState.Success && sortState.data == priority)
                        1f
                    else
                        0f
                ),
            imageVector = Icons.Filled.Check,
            contentDescription = stringResource(R.string.priority_selected)
        )
    }
}

@Composable
fun DeleteAllAction(
    onDeleteAllConfirmed: () -> Unit
) {

    var expanded by remember { mutableStateOf(false) }


    IconButton(
        onClick = { expanded = true }
    ) {
        Icon(
            painter = painterResource(id = R.drawable.ic_vertical_menu),
            contentDescription = stringResource(id = R.string.delete_all_action),
            tint = MaterialTheme.colors.topAppBarContentColor
        )
        DropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }) {
            DropdownMenuItem(
                onClick = {
                    expanded = false
                    onDeleteAllConfirmed()
                }
            ) {
                Text(
                    modifier = Modifier
                        .padding(LARGE_PADDING),
                    text = stringResource(id = R.string.delete_all_action),
                    style = Typography.subtitle2
                )
            }
        }
    }
}


@Composable
fun SearchAppBar(
    text: String,
    onTextChange: (String) -> Unit,
    onCloseClicked: () -> Unit,
    onSearchClicked: (String) -> Unit
){
    val focusRequester = remember { FocusRequester() }
    LaunchedEffect(key1 = true){
        focusRequester.requestFocus()
    }

    BackHandler {
        if (text.isNotEmpty()){
            onTextChange("")
        } else {
            onCloseClicked()
        }
    }

    Surface(
        modifier = Modifier
            .fillMaxWidth()
            .height(TOP_APP_BAR_HEIGHT),
        elevation = AppBarDefaults.TopAppBarElevation,
        color = MaterialTheme.colors.topAppBarBackgroundColor
    ){
        TextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester = focusRequester),
            value = text,
            onValueChange = { searchQuery ->
                onTextChange(searchQuery)
                onSearchClicked(searchQuery)
            },
            placeholder = {
                Text(
                    modifier = Modifier
                        .alpha(ContentAlpha.medium),
                    text = stringResource(R.string.search_placeholder),
                    color = Color.White
                )
            },
            textStyle = TextStyle(
                color = MaterialTheme.colors.topAppBarContentColor,
                fontSize = MaterialTheme.typography.subtitle1.fontSize
            ),
            singleLine = true,
            leadingIcon = {
                IconButton(
                    modifier = Modifier
                        .alpha(ContentAlpha.disabled),
                    enabled = false,
                    onClick = { /*TODO*/ }
                ) {
                    Icon(
                        imageVector = Icons.Filled.Search,
                        contentDescription = stringResource(R.string.search_leading_icon),
                        tint = MaterialTheme.colors.topAppBarContentColor
                    )
                }
            },
            trailingIcon = {
                IconButton(
                    onClick = {
                        if (text.isNotEmpty()){
                            onTextChange("")
                        } else {
                            onCloseClicked()
                        }
                    }
                ) {
                   Icon(
                       imageVector = Icons.Filled.Close,
                       contentDescription = stringResource(R.string.close_icon),
                       tint = MaterialTheme.colors.topAppBarContentColor
                   )
                }
            },
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
//            keyboardActions = KeyboardActions(
//                onSearch = {
//                    onSearchClicked(text)
//                }
//            ),
            colors = TextFieldDefaults.textFieldColors(
                cursorColor = MaterialTheme.colors.topAppBarContentColor,
                focusedIndicatorColor = Color.Transparent,
                disabledIndicatorColor = Color.Transparent,
                unfocusedIndicatorColor = Color.Transparent,
                backgroundColor = Color.Transparent
            )
        )
    }
}

@Composable
@Preview
fun PriorityDropdownMenuItemPreview(){
    PriorityDropdownMenuItem(Priority.LOW, RequestState.Success(data = Priority.LOW))
}