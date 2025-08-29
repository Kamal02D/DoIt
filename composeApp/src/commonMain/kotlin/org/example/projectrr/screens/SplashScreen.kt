package org.example.projectrr.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import org.example.projectrr.routing.Routes
import org.jetbrains.compose.resources.painterResource
import testkmp.composeapp.generated.resources.Res
import testkmp.composeapp.generated.resources.checklist
import testkmp.composeapp.generated.resources.checklist_0
import testkmp.composeapp.generated.resources.checklist_1
import testkmp.composeapp.generated.resources.checklist_2
import testkmp.composeapp.generated.resources.checklist_3


@Composable
fun SplashScreen(navController: NavHostController,modifier: Modifier = Modifier){
    var animationProgress : Float by remember { mutableStateOf(0f) }
    val animationState: Float by animateFloatAsState(
        targetValue = animationProgress,
        animationSpec = tween(durationMillis = 2500),
        finishedListener = {
            navController.navigate(Routes.Main){
                popUpTo(Routes.Splash) {
                    inclusive = true
                }
            }
        }
    )
    LaunchedEffect(animationProgress) {
            animationProgress = 1f
    }
    Column(
        modifier = modifier
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Column(
            modifier = Modifier
                .clip(RoundedCornerShape(16.dp))
                .background(Color(24, 119, 242))
                .size(200.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ){
            Image(
                painter = if (animationState < 0.2f) {
                    painterResource(Res.drawable.checklist_0)
                }else if (animationState < 0.4f) {
                    painterResource(Res.drawable.checklist_1)
                }else if (animationState < 0.6f) {
                    painterResource(Res.drawable.checklist_2)
                }else if (animationState < 0.8f) {
                        painterResource(Res.drawable.checklist_3)
                }else{
                    painterResource(Res.drawable.checklist)
                },
                contentDescription = null,
                modifier = Modifier
                    .size(150.dp)
            )

            AnimatedVisibility(animationState > 0.9f) {
                Text(
                    modifier = Modifier,
                    text = "DO IT",
                    color = Color.White,
                    fontSize = 30.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}