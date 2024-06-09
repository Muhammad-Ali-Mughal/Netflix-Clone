package com.example.netflix

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
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
fun HomeScreen(onNavigateToHome: () -> Unit, onNavigateToMovieDetails: () -> Unit) {
    Surface(
        modifier = Modifier.fillMaxSize(),
        color = Color.Black
    ) {
        ConstraintLayout(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
        ) {
            val (topbar, herosection, heading1, moviesrow1, heading, bigThumbnails, heading2, moviesrow2, heading3, moviesrow3) = createRefs()
            TopBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(topbar) {
                        top.linkTo(parent.top)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            )
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
                    .constrainAs(herosection) {
                        top.linkTo(topbar.bottom)
                        start.linkTo(parent.start)
                        end.linkTo(parent.end)
                    }
            ) {
                Image(
                    painter = painterResource(id = R.drawable.hdkung),
                    contentDescription = "",
                    modifier = Modifier
                        .align(Alignment.Center)
                        .size(500.dp)
                        .padding(16.dp)
                )
            }
            Headings(
                text = "Action Movies",
                modifier = Modifier.constrainAs(heading1) {
                    top.linkTo(herosection.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            MoviesRow(
                modifier = Modifier.constrainAs(moviesrow1) {
                    top.linkTo(heading1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onNavigateToMovieDetails
            )
            Headings(
                text = "Netflix Exculsive",
                modifier = Modifier.constrainAs(heading) {
                    top.linkTo(moviesrow1.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            ThumbnailRow(
                modifier = Modifier.constrainAs(bigThumbnails) {
                    top.linkTo(heading.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onNavigateToMovieDetails
            )
            Headings(
                text = "Top Picks For You",
                modifier = Modifier.constrainAs(heading2) {
                    top.linkTo(bigThumbnails.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            MoviesRow(
                modifier = Modifier.constrainAs(moviesrow2) {
                    top.linkTo(heading2.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onNavigateToMovieDetails
            )
            Headings(
                text = "Action Films",
                modifier = Modifier.constrainAs(heading3) {
                    top.linkTo(moviesrow2.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                }
            )
            MoviesRow(
                modifier = Modifier.constrainAs(moviesrow3) {
                    top.linkTo(heading3.bottom)
                    start.linkTo(parent.start)
                    end.linkTo(parent.end)
                },
                onNavigateToMovieDetails
            )
        }
    }
}

@Composable
fun TopBar(modifier: Modifier) {
    Box(modifier = modifier) {
        Image(
            painter = painterResource(id = R.drawable.netflix),
            contentDescription = null,
            modifier = Modifier
                .align(Alignment.CenterStart)
                .size(48.dp)
        )
        Image(
            painter = painterResource(id = R.drawable.group_8),
            contentDescription = null,
            modifier = Modifier.align(Alignment.CenterEnd)
        )
    }
}

@Composable
fun Headings(text: String, modifier: Modifier) {
    Box(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp)
    ) {
        Text(
            text = text,
            color = Color.White,
            fontWeight = FontWeight.Bold,
            fontSize = 20.sp
        )
    }
}

@Composable
fun MoviesRow(modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Start
    ) {
        val movies = listOf(
            "Dunki" to R.drawable.dunki,
            "Money Heist" to R.drawable.moneyheist,
            "Jawan" to R.drawable.jawan,
            "Wednesday" to R.drawable.wednesday,
            "Avatar" to R.drawable.avatar
        )
        for (movie in movies) {
            MovieCard(title = movie.first, image = movie.second, onClick)
        }
    }
}

@Composable
fun ThumbnailRow(modifier: Modifier, onClick: () -> Unit) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(start = 12.dp)
            .horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.Start
    ) {

        ThumbnailCard(image = R.drawable.damsel, onClick)
        ThumbnailCard(image = R.drawable.witcher, onClick)
        ThumbnailCard(image = R.drawable.lucifer, onClick)
        ThumbnailCard(image = R.drawable.strangerthings, onClick)
        ThumbnailCard(image = R.drawable.titans, onClick)
        ThumbnailCard(image = R.drawable.lockandkey, onClick)
    }
}

@Composable
fun ThumbnailCard(image: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(150.dp)
            .padding(start = 8.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(300.dp)
                .clickable { onClick }
        )
    }
}

@Composable
fun MovieCard(title: String, image: Int, onClick: () -> Unit) {
    Column(
        modifier = Modifier
            .width(120.dp)
            .padding(start = 8.dp)
    ) {
        Image(
            painter = painterResource(id = image),
            contentDescription = null,
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clickable { onClick }
        )
        Text(
            text = title,
            color = Color.White,
            fontSize = 18.sp
        )
    }
}

//@Preview
//@Composable
//fun PreviewHomeScreen() {
//    HomeScreen({})
//}
