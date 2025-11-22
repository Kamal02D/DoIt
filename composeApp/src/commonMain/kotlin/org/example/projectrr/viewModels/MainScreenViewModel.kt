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
import org.example.projectrr.enums.DialogType
import org.example.projectrr.enums.SelectedTab
import org.example.projectrr.models.Task
import org.example.projectrr.states.MainScreenState

class MainScreenViewModel(private val todoDao: TodoDao) : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenState(
        selectedTab = SelectedTab.INCOMPLETE,
        isDialogOpen = false,
        openDialogType = DialogType.ADD,
        pendingForDeletionTask = null,
        currentTaskToAddOrEdit = null
    ))
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()
    val allTasks: Flow<List<Task>> = todoDao.getAll()

    // database function
    fun insertCurrentTask(){
        viewModelScope.launch {
            uiState.value.currentTaskToAddOrEdit?.let {
                todoDao.insert(it)
            }
        }
    }
    fun deletePendingTask(){
        viewModelScope.launch {
            uiState.value.pendingForDeletionTask?.let {
                todoDao.delete(it)
            }
        }
    }



    // state functions
    fun setSelectedTab(selectedTab : SelectedTab){
        _uiState.update {
            it.copy(
                selectedTab = selectedTab
            )
        }
    }
    fun setIsDialogOpen(isNewTaskDialogOpen : Boolean){
        _uiState.update {
            it.copy(
                isDialogOpen = isNewTaskDialogOpen
            )
        }
    }
    fun setOpenDialogType(openDialogType : DialogType){
        _uiState.update {
            it.copy(
                openDialogType = openDialogType
            )
        }
    }
    fun setPendingForDeletionTask(pendingForDeletionTask : Task?){
        _uiState.update {
            it.copy(
                pendingForDeletionTask = pendingForDeletionTask
            )
        }
    }

    fun setCurrentTaskToAddOrEdit(currentTaskToAddOrEdit : Task?){
        _uiState.update {
            it.copy(
                currentTaskToAddOrEdit = currentTaskToAddOrEdit
            )
        }
    }

}