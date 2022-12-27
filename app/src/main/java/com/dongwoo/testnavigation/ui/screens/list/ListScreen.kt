package com.dongwoo.testnavigation.ui.screens.list

import android.icu.text.Normalizer.NO
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.dongwoo.testnavigation.R
import com.dongwoo.testnavigation.data.models.ToDoTask
import com.dongwoo.testnavigation.ui.theme.fabBackgroundColor
import com.dongwoo.testnavigation.ui.viewmodels.SharedViewModel
import com.dongwoo.testnavigation.util.Action
import com.dongwoo.testnavigation.util.SearchAppBarState
import kotlinx.coroutines.launch

@Composable
fun ListScreen(
    action: Action,
    navigateToTaskScreen: (taskId: Int) -> Unit,
    sharedViewModel: SharedViewModel
){
    LaunchedEffect(key1 = action){
        sharedViewModel.handleDatabaseActions(action = action)
    }
    val allTasks by sharedViewModel.allTasks.collectAsState()
    val searchedTasks by sharedViewModel.searchedTasks.collectAsState()
    val sortState by sharedViewModel.sortState.collectAsState()
    val lowPriorityTasks by sharedViewModel.lowPriorityTasks.collectAsState()
    val highPriorityTasks by sharedViewModel.highPriorityTasks.collectAsState()

    val searchAppBarState: SearchAppBarState = sharedViewModel.searchAppBarState
    val searchTextState: String = sharedViewModel.searchTextState

    val scaffoldState = rememberScaffoldState()

    DisplaySnackBar(
        scaffoldState = scaffoldState,
        onComplete = { sharedViewModel.updateAction(it)},
        onUndoClicked = { sharedViewModel.updateAction(it) },
        taskTitle = sharedViewModel.title,
        action = action
    )

    Scaffold(
        scaffoldState = scaffoldState,
        topBar = {
            ListAppBar(
                sharedViewModel = sharedViewModel,
                searchAppBarState = searchAppBarState,
                searchTextState = searchTextState
            )
        },
        content = {
            ListContent(
                allTasks = allTasks,
                searchedTasks = searchedTasks,
                lowPriorityTasks = lowPriorityTasks,
                highPriorityTasks = highPriorityTasks,
                sortState = sortState,
                searchAppBarState = searchAppBarState,
                onSwipeToDelete = {action, task ->
                    sharedViewModel.updateAction(newAction = action)
                    sharedViewModel.updateTaskFields(selectedTask = task)
                    scaffoldState.snackbarHostState.currentSnackbarData?.dismiss()
                },
                navigateToTaskScreen = navigateToTaskScreen
            )
        },
        floatingActionButton = {
            ListFab(onFabClicked = navigateToTaskScreen)
        }
    )
}

@Composable
fun DisplaySnackBar(
    scaffoldState: ScaffoldState,
    onComplete: (Action) -> Unit,
    onUndoClicked: (Action) -> Unit,
    taskTitle: String,
    action: Action
){
    val scope = rememberCoroutineScope()
    LaunchedEffect(key1 = action){
        if (action != Action.NO_ACTION){
            scope.launch {
                val snackBarResult = scaffoldState.snackbarHostState.showSnackbar(
                    message = setMessage(action = action, taskTitle = taskTitle),
                    actionLabel = setActionLabel(action)
                )
                undoDeletedTask(
                    action = action,
                    snackBarResult = snackBarResult,
                    onUndoClicked = onUndoClicked
                )
            }
            onComplete(Action.NO_ACTION)
        }
    }
}

private fun setMessage(
    action: Action,
    taskTitle: String
) : String {
    return when (action){
        Action.DELETE_ALL -> "All Tasks Removed"
        else -> "${action.name}: $taskTitle"
    }
}

private fun setActionLabel(action: Action): String{
    return if (action == Action.DELETE) return "UNDO" else "OK"
}

private fun undoDeletedTask(
    action: Action,
    snackBarResult: SnackbarResult,
    onUndoClicked: (Action) -> Unit
){
    if (snackBarResult == SnackbarResult.ActionPerformed && action == Action.DELETE) {
        onUndoClicked(Action.UNDO)
    }
}

@Composable
fun ListFab(
    onFabClicked: (taskId: Int) -> Unit
){
    FloatingActionButton(
        onClick = {
            onFabClicked(-1)
        },
        backgroundColor = MaterialTheme.colors.fabBackgroundColor
    ) {
       Icon(
           imageVector = Icons.Filled.Add,
           contentDescription = stringResource(R.string.add_button),
           tint = Color.White
       )
    }
}