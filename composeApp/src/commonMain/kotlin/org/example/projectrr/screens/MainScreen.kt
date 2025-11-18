package org.example.projectrr.screens


import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.BasicAlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Surface
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import org.example.projectrr.enums.DialogType
import org.example.projectrr.enums.IconButtonType
import org.example.projectrr.models.Task
import org.example.projectrr.viewModels.MainScreenViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import testkmp.composeapp.generated.resources.Res
import testkmp.composeapp.generated.resources.add
import testkmp.composeapp.generated.resources.pen
import testkmp.composeapp.generated.resources.trash

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel =  koinViewModel<MainScreenViewModel>() ,
    modifier: Modifier=Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    val allTasks by viewModel.allTasks.collectAsState(emptyList())
    Box{
        Column(
            modifier = modifier
                .fillMaxSize()
                .padding(all = 8.dp)
        ) {
            Text(
                text = "DO IT",
                fontSize = 40.sp,
                fontWeight = FontWeight.Bold
            )
            ProgressView()
            TasksTabsView(
                selectedIndex = uiState.selectedIndex,
                onSelectedIndexChange = viewModel::setSelectedIndex
            )
            TasksListView(
                modifier = modifier,
                onEditTask = { taskId ->
                    allTasks.firstOrNull { it.id == taskId }?.let { taskToEdit ->
                        viewModel.setCurrentTaskToAddOrEdit(taskToEdit)
                        viewModel.setOpenDialogType(DialogType.EDIT)
                        viewModel.setIsDialogOpen(true)
                    }
                },
                onDeleteTask = { taskId ->
                    allTasks.firstOrNull { it.id == taskId }?.let { taskToDelete ->
                        viewModel.setPendingForDeletionTask(taskToDelete)
                        viewModel.setOpenDialogType(DialogType.DELETE)
                        viewModel.setIsDialogOpen(true)
                    }
                },
                tasks = allTasks
            )
        }
        additionFloatingButton(
            modifier = modifier
                .align(Alignment.BottomEnd),
            onClick = {
                viewModel.setCurrentTaskToAddOrEdit(Task.empty())
                viewModel.setOpenDialogType(DialogType.ADD)
                viewModel.setIsDialogOpen(true)
            }
        )
    }
    if (uiState.isDialogOpen) {
        // executes when closing and when confirming
        fun onDismiss(){
            viewModel.setIsDialogOpen(false)
            if(uiState.openDialogType == DialogType.DELETE){
                viewModel.setPendingForDeletionTask(null)
            }else{
                viewModel.setCurrentTaskToAddOrEdit(null)
            }
        }

        MDialog(
            dialogType = uiState.openDialogType,
            currentTask = uiState.currentTaskToAddOrEdit,
            setCurrentTaskText = viewModel::setCurrentTaskToAddOrEdit,
            onDismiss = {onDismiss()},
            onConfirm = {
                when(uiState.openDialogType){
                    DialogType.ADD, DialogType.EDIT -> viewModel.insertCurrentTask()
                    DialogType.DELETE -> viewModel.deletePendingTask()
                }
                onDismiss()
            }
        )
    }
}

/// Composables
@Composable
fun ProgressView(modifier: Modifier=Modifier){
    Column(
        modifier = modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(15.dp))
            .background(Color.Green)
            .padding(15.dp)

    ){
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            Text(
                text = "Progress",
                fontSize = 14.sp
            )
            Spacer(Modifier.weight(1f))
            Text("3/5 done")
        }
        Row(
            modifier = Modifier
                .fillMaxWidth()
        ){
            LinearProgressIndicator(
                drawStopIndicator = {},
                gapSize = -(8.dp),
                modifier = Modifier
                    .height(8.dp)
                    .fillMaxWidth(),
                progress = {
                    0.5f
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Text(
                text = "60%",
                modifier = Modifier
                    .padding(top = 8.dp),
                fontSize = 21.sp,
                fontWeight = FontWeight.Bold,
            )
        }
    }
}


@Composable
fun TasksTabsView(
    selectedIndex : Int,
    onSelectedIndexChange : (Int) -> Unit,
    modifier: Modifier=Modifier){
    TabRow(
        selectedTabIndex = selectedIndex,
        containerColor = Color.White,
        indicator = {},
        modifier = modifier
    ) {
        val tabs = arrayOf("Incomplete","Complete")
        for (tabIndex in tabs.indices) {
            Tab(
                selected = selectedIndex == tabIndex,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                onClick = {
                    onSelectedIndexChange(tabIndex)
                },
                text = {
                    Text(
                        text = tabs[tabIndex],
                        maxLines = 2
                    )
                }
            )
        }
    }
}
@Composable
fun TasksListView(tasks : List<Task>,
                  onEditTask: (taskId : Long) -> Unit,
                  onDeleteTask: (taskId : Long) -> Unit,
                  modifier: Modifier=Modifier){
    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        tasks.forEach { task ->
            TaskItem(
                task = task,
                onEditTask = {onEditTask(task.id)},
                onDeleteTask = {onDeleteTask(task.id)},
            )
        }
    }
}

@Composable
fun TaskItem(task : Task,
             onEditTask : () -> Unit,
             onDeleteTask : () -> Unit,
             modifier: Modifier=Modifier){
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .padding(all = 12.dp)
    ){
       Text(text = task.text,
           modifier = Modifier
               .fillMaxWidth(0.8f)
       )
        Row(
            modifier = Modifier
                .weight(1f),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ){
            IconButton(
                iconButtonType = IconButtonType.EDIT,
                onAction = onEditTask
            )
            IconButton(
                iconButtonType = IconButtonType.DELETE,
                onAction = onDeleteTask
            )

        }
    }
}

@Composable
fun IconButton(iconButtonType: IconButtonType,
               onAction : () -> Unit ,
               modifier: Modifier=Modifier){
    Image(
        modifier = modifier
            .size(30.dp)
            .clickable {
                    onAction()
            },
        painter = painterResource(
            when (iconButtonType){
                IconButtonType.DELETE -> Res.drawable.trash
                IconButtonType.EDIT -> Res.drawable.pen
            }
        ),
        contentDescription = null
    )
}

@Composable
fun additionFloatingButton(
    onClick : () -> Unit,
    modifier: Modifier =
        Modifier
){
    Image(
        contentDescription = null,
        painter = painterResource(Res.drawable.add),
        modifier =
            modifier.size(70.dp)
                .clip(RoundedCornerShape(70.dp))
                .background(Color(0xff4299E1))
                .clickable {
                    onClick()
                }
    )
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
internal fun MDialog(
    dialogType: DialogType,
    currentTask : Task?,
    setCurrentTaskText : (task : Task?) -> Unit,
    onDismiss : () -> Unit,
    onConfirm : () -> Unit,
    modifier: Modifier = Modifier
){
    BasicAlertDialog(
        modifier = modifier
            .fillMaxWidth(0.9f)
            .clip(RoundedCornerShape(10.dp)),
        properties = DialogProperties(usePlatformDefaultWidth = false),
        onDismissRequest = onDismiss,
        content = {
            Surface{
                Column(
                    modifier = modifier
                        .padding(16.dp)
                        .fillMaxWidth()
                ) {
                    Text(
                        modifier = Modifier
                            .padding(bottom = 16.dp),
                        text = when(dialogType){
                            DialogType.ADD -> "New Task"
                            DialogType.EDIT -> "Edit Task"
                            DialogType.DELETE -> "Delete Task"
                        },
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
                    if (dialogType != DialogType.DELETE) {
                        if (currentTask != null) {
                            Text(
                                text = "Task Name",
                                fontWeight = FontWeight.Thin
                            )
                            TextField(
                                modifier = Modifier
                                    .padding(bottom = 10.dp)
                                    .fillMaxWidth(),
                                colors = TextFieldDefaults.colors(
                                    focusedIndicatorColor = Color.Transparent,
                                    unfocusedIndicatorColor = Color.Transparent
                                ),
                                value = currentTask.text,
                                onValueChange = {
                                    setCurrentTaskText(
                                        currentTask.copy(
                                            text =  it
                                        )
                                    )
                                }
                            )
                        }
                    }else{
                        Text(
                            text = "Are you sure you want to delete?",
                            modifier = Modifier
                                .padding(bottom = 12.dp)
                        )
                    }
                    Button(
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (dialogType == DialogType.DELETE) {
                                Color(0xffC70000)
                            }else {
                                Color(0xff4299E1)
                            }
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = onConfirm
                    ){
                        Text(
                            text =
                                when(dialogType){
                                    DialogType.ADD -> "Add"
                                    DialogType.EDIT -> "Save"
                                    DialogType.DELETE -> "Delete"
                                }
                        )
                    }
                }
            }
        }
    )
}