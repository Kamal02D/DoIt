package org.example.projectrr.states

import org.example.projectrr.models.Task

data class MainScreenState(
    val selectedIndex : Int,
    val isNewTaskDialogOpen : Boolean,
    val currentTaskToAdd : Task
)