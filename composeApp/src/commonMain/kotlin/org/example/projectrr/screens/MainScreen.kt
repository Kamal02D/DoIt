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
import org.example.projectrr.models.Task
import org.example.projectrr.viewModels.MainScreenViewModel
import org.jetbrains.compose.resources.painterResource
import org.koin.compose.viewmodel.koinViewModel
import testkmp.composeapp.generated.resources.Res
import testkmp.composeapp.generated.resources.add

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
                .background(Color(0xF7FAFC))
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
                tasks = allTasks
            )
        }
        additionFloatingButton(
            modifier = modifier
                .align(Alignment.BottomEnd),
            onClick = {viewModel.setIsNewTaskDialogOpen(true)}
        )
    }
    if (uiState.isNewTaskDialogOpen) {
        InputDialog(
            modifier = modifier,
            currentTask = uiState.currentTaskToAdd,
            setCurrentTaskText = viewModel::setCurrentTaskText,
            onDismiss = {viewModel.setIsNewTaskDialogOpen(false)},
            onAddTask = viewModel::insertCurrentTask
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
fun TasksListView(modifier: Modifier=Modifier,tasks : List<Task>){
    Column (
        modifier = modifier
            .fillMaxSize()
            .verticalScroll(rememberScrollState())
    ){
        tasks.forEach { task ->
            Text(task.text)
        }
    }
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
internal fun InputDialog(
    modifier: Modifier = Modifier,
    currentTask : Task,
    setCurrentTaskText : (String) -> Unit,
    onDismiss : () -> Unit,
    onAddTask : () -> Unit
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
                        text = "New Task",
                        fontWeight = FontWeight.Bold,
                        fontSize = 28.sp
                    )
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
                            setCurrentTaskText(it)
                        }
                    )
                    Button(
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = Color(0xff4299E1)
                        ),
                        modifier = Modifier
                            .fillMaxWidth(),
                        onClick = onAddTask
                    ){
                        Text(
                            text = "Add Task"
                        )
                    }
                }
            }
        }
    )
}