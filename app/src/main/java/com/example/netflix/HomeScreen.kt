package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.constraintlayout.compose.ConstraintLayout
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController

@Composable
fun HomeScreen(onClick: ()->Unit) {
    Surface (
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        ConstraintLayout (
            modifier = Modifier.fillMaxSize()
        ) {
            val (topbar, heading1, moviesrow1, heading2, moviesrow2, heading3, moviesrow3) = createRefs()
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(topbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(painter = painterResource(id = R.drawable.netflix), contentDescription = null, modifier = Modifier
                    .align(
                        Alignment.CenterStart
                    )
                    .size(48.dp))
                Image(painter = painterResource(id = R.drawable.group_8), contentDescription = null, modifier = Modifier.align(
                    Alignment.CenterEnd))
            }
            Headings(text = "Only on Netflix", modifier = Modifier.constrainAs(heading1) {
                top.linkTo(topbar.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            MoviesRow(modifier = Modifier.constrainAs(moviesrow1) {
                top.linkTo(heading1.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            Headings(text = "Top Picks For You", modifier = Modifier.constrainAs(heading2) {
                top.linkTo(moviesrow1.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            MoviesRow(modifier = Modifier.constrainAs(moviesrow2) {
                top.linkTo(heading2.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            Headings(text = "Action Films", modifier = Modifier.constrainAs(heading3) {
                top.linkTo(moviesrow2.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
            MoviesRow(modifier = Modifier.constrainAs(moviesrow3) {
                top.linkTo(heading3.bottom)
                start.linkTo(parent.start)
                end.linkTo(parent.end)
            })
        }
    }
}

@Composable
fun Headings(text: String, modifier: Modifier) {
    Box(modifier = modifier
        .fillMaxWidth()
        .padding(16.dp)) {
        Text(text = text, color = Color.White, fontWeight = FontWeight.Bold, fontSize = 20.sp)
    }
}

@Composable
fun MoviesRow(modifier: Modifier) {
    Row (
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Start
    ) {
        MovieCard("Dunki", R.drawable.dunki)
        MovieCard("Money Heist", R.drawable.moneyheist)
        MovieCard("Jawan", R.drawable.jawan)
        MovieCard("Wednesday", R.drawable.wednesday)
        MovieCard("Avatar", R.drawable.avatar)
    }
}

@Composable
fun MovieCard(title: String, image: Int) {
    Column (modifier = Modifier
        .width(120.dp)
        .padding(start = 8.dp)
        ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .fillMaxWidth()
                .height(200.dp)
        )
        Text(text = title, color = Color.White, fontSize = 18.sp)
    }
}

@Preview
@Composable
fun PreviewHomeScreen() {
    HomeScreen({})
}