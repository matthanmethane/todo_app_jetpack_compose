package com.dongwoo.testnavigation.ui.screens.task

import android.content.Context
import android.widget.Toast
import androidx.activity.compose.BackHandler
import androidx.compose.material.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.platform.LocalContext
import com.dongwoo.testnavigation.data.models.Priority
import com.dongwoo.testnavigation.data.models.ToDoTask
import com.dongwoo.testnavigation.ui.viewmodels.SharedViewModel
import com.dongwoo.testnavigation.util.Action

@Composable
fun TaskScreen(
    selectedTask : ToDoTask?,
    navigateToListScreen : (Action) -> Unit,
    sharedViewModel: SharedViewModel
) {
    val title: String = sharedViewModel.title
    val description: String = sharedViewModel.description
    val priority: Priority = sharedViewModel.priority

    val context = LocalContext.current

//    BackHandler(onBackPressed = {navigateToListScreen(Action.NO_ACTION)})
    BackHandler {
        navigateToListScreen(Action.NO_ACTION)
    }

    Scaffold(
        topBar = {
            TaskAppBar(
                selectedTask = selectedTask,
                navigateToListScreen = { action ->
                    if (action == Action.NO_ACTION){
                        navigateToListScreen(action)
                    } else {
                        if (sharedViewModel.validateFields()){
                            navigateToListScreen(action)
                        } else {
                            displayToast(context = context)
                        }
                    }

                }
            )
        },
        content = {
            TaskContent(
                title = title,
                onTitleChange = { sharedViewModel.updateTitle(it) },
                description = description,
                onDescriptionChange = { sharedViewModel.updateDescription(newDescription = it) },
                priority = priority,
                onPrioritySelected = { sharedViewModel.updatePriority(it) }
            )
        }
    )
}

fun displayToast(context: Context) {
    Toast.makeText(
        context,
        "Fields Empty!",
        Toast.LENGTH_SHORT
    ).show()
}

//@Composable
//fun BackHandler(
//    backDispatcher: OnBackPressedDispatcher? = LocalOnBackPressedDispatcherOwner.current?.onBackPressedDispatcher,
//    onBackPressed: () -> Unit
//){
//    val currentOnBackPressed by rememberUpdatedState(newValue = onBackPressed)
//
//    val backCallBack = remember {
//        object: OnBackPressedCallback(true) {
//            override fun handleOnBackPressed() {
//                currentOnBackPressed()
//            }
//        }
//    }
//
//    DisposableEffect(key1 = backDispatcher){
//        backDispatcher?.addCallback(backCallBack)
//        onDispose{
//            backCallBack.remove()
//        }
//    }
//}
