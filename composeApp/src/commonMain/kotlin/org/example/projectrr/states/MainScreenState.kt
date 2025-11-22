package org.example.projectrr.states

import org.example.projectrr.enums.DialogType
import org.example.projectrr.enums.SelectedTab
import org.example.projectrr.models.Task

data class MainScreenState(
    val selectedTab: SelectedTab,
    val isDialogOpen : Boolean,
    val openDialogType : DialogType,
    val currentTaskToAddOrEdit : Task?,
    val pendingForDeletionTask : Task?
)