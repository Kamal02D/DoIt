package org.example.projectrr.screens


import androidx.compose.animation.core.Animatable
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectHorizontalDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
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
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.key
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.DialogProperties
import kotlinx.coroutines.launch
import org.example.projectrr.enums.DialogType
import org.example.projectrr.enums.IconButtonType
import org.example.projectrr.enums.SelectedTab
import org.example.projectrr.models.Task
import org.example.projectrr.viewModels.MainScreenViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import testkmp.composeapp.generated.resources.Res
import testkmp.composeapp.generated.resources.add
import testkmp.composeapp.generated.resources.pen
import testkmp.composeapp.generated.resources.trash
import kotlin.math.roundToInt

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
            ProgressView(
                tasksDoneCount = allTasks.filter { it.isDone }.size,
                taskNotDoneCount = allTasks.filter { !it.isDone }.size,
                modifier = Modifier.padding(vertical = 16.dp)
            )
            TasksTabsView(
                selectedTab = uiState.selectedTab,
                onSelectedTabChange = viewModel::setSelectedTab
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
                tasks = allTasks.filter { task: Task ->
                    when (uiState.selectedTab) {
                        SelectedTab.COMPLETE -> task.isDone
                        SelectedTab.INCOMPLETE -> !task.isDone
                    }
                },
                itemWidth = uiState.selectedTab,
                onTaskDone = { task ->
                    viewModel.upsert(task.copy(
                        isDone = true
                    ))
                },
                onTaskUnDone = { task ->
                    viewModel.upsert(task.copy(
                        isDone = false
                    ))
                }
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
fun ProgressView(
    tasksDoneCount : Int,
    taskNotDoneCount : Int,
    modifier: Modifier=Modifier){
    var progress = ((tasksDoneCount/(tasksDoneCount+taskNotDoneCount).toDouble())*100)
    if (progress.isNaN()){
        progress = 0f.toDouble()
    }
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
            Text("$tasksDoneCount/${(taskNotDoneCount+tasksDoneCount)} done")
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
                   progress.toFloat() / 100f
                },
            )
        }
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ){
            Text(
                text = "${progress.toString(numOfDec = 1)}%",
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
    selectedTab : SelectedTab,
    onSelectedTabChange : (SelectedTab) -> Unit,
    modifier: Modifier=Modifier){
    val selectedTabIndex = if (selectedTab == SelectedTab.INCOMPLETE)  0 else 1
    TabRow(
        selectedTabIndex = selectedTabIndex,
        containerColor = Color.White,
        indicator = { tabPositions ->
            if (selectedTabIndex < tabPositions.size) {
                TabRowDefaults.Indicator(
                    modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                    color = Color(0xff4299E1)
                )
            }},
        divider = {},
        modifier = modifier
    ) {
        for (tab in SelectedTab.entries) {
            Tab(
                selected = selectedTab == tab,
                selectedContentColor = Color.Black,
                unselectedContentColor = Color.Gray,
                onClick = {
                    onSelectedTabChange(tab)
                },
                text = {
                    Text(
                        text = when (tab){
                            SelectedTab.COMPLETE ->  "Complete"
                            SelectedTab.INCOMPLETE -> "Incomplete"
                        },
                        maxLines = 2
                    )
                }
            )
        }
    }
}
@Composable
fun TasksListView(tasks : List<Task>,
                  onTaskDone: (task : Task) -> Unit,
                  onTaskUnDone: (task : Task) -> Unit,
                  itemWidth : SelectedTab,
                  onEditTask: (taskId : Long) -> Unit,
                  onDeleteTask: (taskId : Long) -> Unit,
                  modifier: Modifier=Modifier){
    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        tasks.forEach { task ->
            key(task.id) {
                TaskItem(
                    task = task,
                    currentTab = itemWidth,
                    onEditTask = { onEditTask(task.id) },
                    onDeleteTask = { onDeleteTask(task.id) },
                    onTaskDone = {onTaskDone(task)},
                    onTaskUnDone = {onTaskUnDone(task)},
                )
            }
        }
    }
}

@Composable
fun TaskItem(task : Task,
             onTaskDone : () -> Unit,
             onTaskUnDone : () -> Unit,
             currentTab : SelectedTab,
             onEditTask : () -> Unit,
             onDeleteTask : () -> Unit,
             modifier: Modifier=Modifier){
    val maxSwipeThreshold = 0.6f

    var itemWidth by remember{
        mutableStateOf(0f)
    }
    val offset = remember {
       Animatable(initialValue = 0f)
    }
    val scope = rememberCoroutineScope()

    Box(
        modifier = Modifier
            .fillMaxSize()
            .padding(vertical = 6.dp)
            .clip(RoundedCornerShape(8.dp))
            .background( when(currentTab){
                SelectedTab.INCOMPLETE -> Color.Green
                SelectedTab.COMPLETE -> Color.Red
            })
            .offset { IntOffset(offset.value.roundToInt(),0) }
            .pointerInput(Unit) {
                detectHorizontalDragGestures(
                    onHorizontalDrag = { _, dragAmount ->
                        val newOffset = when(currentTab){
                            SelectedTab.INCOMPLETE -> (offset.value + dragAmount).coerceIn(0f, itemWidth*0.85f) // swipe right. not pass left
                            SelectedTab.COMPLETE -> (offset.value + dragAmount).coerceIn(itemWidth*0.85f * -1, 0f)
                        }
                        scope.launch {
                            offset.snapTo(targetValue = newOffset)
                        }
                    },
                    onDragEnd = {
                        when(currentTab){
                            SelectedTab.INCOMPLETE -> {
                                if (offset.value > maxSwipeThreshold*itemWidth){
                                    scope.launch {
                                        offset.snapTo(targetValue = itemWidth)
                                        onTaskDone()
                                    }
                                }else{
                                    scope.launch {
                                        offset.snapTo(targetValue = 0f)
                                    }
                                }
                            }
                            SelectedTab.COMPLETE -> {
                                if (offset.value < maxSwipeThreshold*itemWidth*-1){
                                    scope.launch {
                                        offset.snapTo(targetValue = itemWidth)
                                        onTaskUnDone()
                                    }
                                }else{
                                    scope.launch {
                                        offset.snapTo(targetValue = 0f)
                                    }
                                }
                            }
                        }
                    }
                )
            }
            .onGloballyPositioned {
                itemWidth = it.size.width.toFloat()
            }
    ) {
        Row(
            modifier = modifier
                .fillMaxSize()
                .clip(RoundedCornerShape(8.dp))
                .background(Color.White)
                .padding(all = 12.dp)

        ) {
            Text(
                text = task.text,
                modifier = Modifier
                    .fillMaxWidth(0.8f)
            )
            Row(
                modifier = Modifier
                    .weight(1f),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
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




fun Double.toString(numOfDec: Int=1): String {
    var stringDouble = this.toString()
    if (stringDouble == "NAN"){
        return "0"
    }
    val indexOfDot = stringDouble.indexOf(".")
    if (indexOfDot != -1){
        val indexOfDotPlusDec = indexOfDot + numOfDec + 1
        stringDouble = stringDouble.substring(startIndex = 0,endIndex = indexOfDotPlusDec)
        if (stringDouble.endsWith(".0")) {
            stringDouble = stringDouble.replace(".0", "")
        }
    }
    return stringDouble
}
