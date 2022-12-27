package com.dongwoo.testnavigation.ui.viewmodels

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dongwoo.testnavigation.data.models.Priority
import com.dongwoo.testnavigation.data.models.ToDoTask
import com.dongwoo.testnavigation.data.repositories.DataStoreRepository
import com.dongwoo.testnavigation.data.repositories.ToDoRepository
import com.dongwoo.testnavigation.util.Action
import com.dongwoo.testnavigation.util.Constants.MAX_TITLE_LENGTH
import com.dongwoo.testnavigation.util.RequestState
import com.dongwoo.testnavigation.util.SearchAppBarState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SharedViewModel @Inject constructor(
    private val repository: ToDoRepository,
    private val datastoreRepository: DataStoreRepository
): ViewModel() {
    var searchAppBarState by mutableStateOf(SearchAppBarState.CLOSED)
        private set

    var searchTextState by mutableStateOf("")
        private set

    var id by mutableStateOf(0)
        private set

    var title by mutableStateOf("")
        private set

    var description by mutableStateOf("")
        private set

    var priority by mutableStateOf(Priority.LOW)
        private set

    var action by mutableStateOf(Action.NO_ACTION)
        private set

    private val _allTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val allTasks: StateFlow<RequestState<List<ToDoTask>>> = _allTasks

    private val _selectedTask: MutableStateFlow<ToDoTask?> = MutableStateFlow(null)
    val selectedTask: StateFlow<ToDoTask?> = _selectedTask

    private val _searchedTasks = MutableStateFlow<RequestState<List<ToDoTask>>>(RequestState.Idle)
    val searchedTasks: StateFlow<RequestState<List<ToDoTask>>> = _searchedTasks

    private val _sortState = MutableStateFlow<RequestState<Priority>>(RequestState.Idle)
    val sortState: StateFlow<RequestState<Priority>> = _sortState

    val lowPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByLowPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    val highPriorityTasks: StateFlow<List<ToDoTask>> = repository.sortByHighPriority.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(),
        emptyList()
    )

    init {
        getAllTasks()
        readSortState()
    }

    private fun getAllTasks(){
        _allTasks.value = RequestState.Loading
        try{
            viewModelScope.launch {
                repository.getAllTasks.collect{
                    _allTasks.value = RequestState.Success(it)
                }
            }
        } catch (e: Exception) {
            _allTasks.value = RequestState.Error(e)
        }
    }

    fun searchDatabase(searchQuery: String){
        _searchedTasks.value = RequestState.Loading
        try{
            viewModelScope.launch {
                repository.searchDatabase("%$searchQuery%").collect{ searchedTasks ->
                    _searchedTasks.value = RequestState.Success(searchedTasks)
                }
            }
        } catch (e: Exception) {
            _searchedTasks.value = RequestState.Error(e)
        }
        searchAppBarState = SearchAppBarState.TRIGGERED
    }

    fun getSelectedTask(taskId: Int){
        viewModelScope.launch {
            repository.getSelectedTask(taskId = taskId).collect { task ->
                _selectedTask.value = task
            }
        }
    }

    private fun addTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                title = title,
                description = description,
                priority = priority
            )
            repository.addTask(task = toDoTask)
        }
        searchAppBarState = SearchAppBarState.CLOSED
    }

    private fun updateTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.updateTask(task = toDoTask)
        }
    }

    private fun deleteTask(){
        viewModelScope.launch(Dispatchers.IO) {
            val toDoTask = ToDoTask(
                id = id,
                title = title,
                description = description,
                priority = priority
            )
            repository.deleteTask(task = toDoTask)
        }
    }

    private fun deleteAllTask(){
        viewModelScope.launch(Dispatchers.IO) {
            repository.deleteAllTasks()
        }
    }

    fun handleDatabaseActions(action: Action){
        when (action){
            Action.ADD -> {
                addTask()
            }
            Action.UPDATE -> {
                updateTask()
            }
            Action.DELETE -> {
                deleteTask()
            }
            Action.DELETE_ALL -> {
                deleteAllTask()
            }
            Action.UNDO -> {
                addTask()
            }
            Action.NO_ACTION -> {}
        }
    }

    fun updateTaskFields(selectedTask: ToDoTask?){
        if (selectedTask != null){
            id = selectedTask.id
            title = selectedTask.title
            description = selectedTask.description
            priority = selectedTask.priority
        } else {
            id = 0
            title = ""
            description = ""
            priority = Priority.LOW
        }
    }

    fun updateTitle(newTitle: String){
        if (newTitle.length < MAX_TITLE_LENGTH){
            title = newTitle
        }
    }

    fun validateFields(): Boolean {
        return title.isNotEmpty() && description.isNotEmpty()
    }

    fun persistSortState(priority: Priority){
        viewModelScope.launch(Dispatchers.IO) {
            datastoreRepository.persistSortState(priority = priority)
        }
    }

    private fun readSortState(){
        _sortState.value = RequestState.Loading
        try{
            viewModelScope.launch {
                datastoreRepository.readSortState
                    .map { Priority.valueOf(it) }
                    .collect{ _sortState.value = RequestState.Success(it) }
            }
        } catch (e: Exception) {
            _sortState.value = RequestState.Error(e)
        }
    }

    fun updateAction(newAction: Action){
        action = newAction
    }

    fun updatePriority(newPriority: Priority){
        priority = newPriority
    }

    fun updateDescription(newDescription: String){
        description = newDescription
    }

    fun updateSearchAppBarState(newSearchAppBarState: SearchAppBarState){
        searchAppBarState = newSearchAppBarState
    }

    fun updateSearchTextState(newSearchTextState: String){
        searchTextState = newSearchTextState
    }
}