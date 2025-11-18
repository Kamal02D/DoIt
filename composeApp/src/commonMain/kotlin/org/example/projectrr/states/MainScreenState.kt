package org.example.projectrr.states

import org.example.projectrr.enums.DialogType
import org.example.projectrr.models.Task

data class MainScreenState(
    val selectedIndex : Int, // todo : replace with custom type
    val isDialogOpen : Boolean,
    val openDialogType : DialogType,
    val currentTaskToAddOrEdit : Task?,
    val pendingForDeletionTask : Task?
)