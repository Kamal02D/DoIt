package org.example.projectrr.screens


import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import org.example.projectrr.viewModels.MainScreenViewModel
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun MainScreen(
    viewModel: MainScreenViewModel =  koinViewModel<MainScreenViewModel>() ,
    modifier: Modifier=Modifier
){
    val uiState by viewModel.uiState.collectAsState()
    Column(
        modifier = modifier
            .fillMaxSize()
            .background(Color(0xF7FAFC))
    ){
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
    }
}

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