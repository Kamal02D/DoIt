package org.example.projectrr.viewModels

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import org.example.projectrr.data.TodoDao
import org.example.projectrr.models.Task
import org.example.projectrr.states.MainScreenState

class MainScreenViewModel(private val todoDao: TodoDao) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenState(
        selectedIndex = 0,
        isNewTaskDialogOpen = false,
        currentTaskToAdd = Task(
            text = "",
            isDone = false
        )
    ))
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()
    val allTasks: Flow<List<Task>> = todoDao.getAll()

    // database function
    fun insertCurrentTask(){
        // insert to db
        viewModelScope.launch {
            todoDao.insert(uiState.value.currentTaskToAdd)
        }
        // reset current task
        _uiState.update {
            it.copy(
                currentTaskToAdd = Task(
                    text = "",
                    isDone = false
                )
            )
        }
        // close the dialog
        setIsNewTaskDialogOpen(false)
    }



    // state functions
    fun setSelectedIndex(selectedIndex : Int){
        _uiState.update {
            it.copy(
                selectedIndex = selectedIndex
            )
        }
    }
    fun setIsNewTaskDialogOpen(isNewTaskDialogOpen : Boolean){
        _uiState.update {
            it.copy(
                isNewTaskDialogOpen = isNewTaskDialogOpen
            )
        }
    }
    fun setCurrentTaskText(text : String){
        _uiState.update {
            it.copy(
                currentTaskToAdd = it.currentTaskToAdd.copy(
                    text = text
                )
            )
        }
    }

}