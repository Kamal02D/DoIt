package org.example.projectrr.viewModels

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import org.example.projectrr.states.MainScreenState

class MainScreenViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(MainScreenState(
        selectedIndex = 0
    ))
    val uiState: StateFlow<MainScreenState> = _uiState.asStateFlow()





    // functions
    fun setSelectedIndex(selectedIndex : Int){
        _uiState.update {
            it.copy(
                selectedIndex = selectedIndex
            )
        }
    }

}