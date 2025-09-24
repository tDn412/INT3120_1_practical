package com.example.unit2_pathway2_artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent { MaterialTheme { ArtSpaceApp() } }
    }
}

data class Artwork(
    val imageRes: Int,
    val title: String,
    val artist: String,
    val year: String
)

@Composable
fun ArtSpaceApp() {
    val artworks = remember {
        listOf(
            Artwork(R.drawable.bridge, "Sailing Under the Bridge", "Kat Kuan", "2017"),
            Artwork(R.drawable.starry_night, "The Starry Night", "Vincent van Gogh", "1889"),
            Artwork(R.drawable.monalisa, "Mona Lisa", "Leonardo da Vinci", "1503")
        )
    }
    var index by remember { mutableStateOf(0) }
    val item = artworks[index]

    Surface(
        modifier = Modifier.fillMaxSize(),
        color = MaterialTheme.colorScheme.background
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp, vertical = 16.dp)
        ) {
            Spacer(Modifier.height(12.dp))

            // Framed image with shadow
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .weight(1f)
                    .padding(top = 24.dp, bottom = 24.dp),
                contentAlignment = Alignment.Center
            ) {
                Surface(
                    tonalElevation = 2.dp,
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .shadow(12.dp, RoundedCornerShape(12.dp))
                        .padding(0.dp)
                ) {
                    Box(Modifier.padding(48.dp)) {
                        Image(
                            painter = painterResource(id = item.imageRes),
                            contentDescription = item.title,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier
                                .fillMaxWidth()
                                .aspectRatio(3f / 4f)
                        )
                    }
                }
            }

            // Caption card
            Surface(
                color = MaterialTheme.colorScheme.surfaceVariant,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = item.title,
                        fontSize = 26.sp,
                        lineHeight = 30.sp,
                        maxLines = 2,
                        overflow = TextOverflow.Ellipsis
                    )
                    Spacer(Modifier.height(8.dp))
                    Row {
                        Text(item.artist, fontWeight = FontWeight.Bold)
                        Text("  (${item.year})")
                    }
                }
            }

            Spacer(Modifier.height(16.dp))

            // Buttons
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(bottom = 8.dp),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Button(
                    onClick = {
                        index = if (index == 0) artworks.lastIndex else index - 1
                    },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) { Text("Previous") }

                Spacer(Modifier.width(16.dp))

                Button(
                    onClick = {
                        index = (index + 1) % artworks.size
                    },
                    shape = RoundedCornerShape(28.dp),
                    modifier = Modifier
                        .weight(1f)
                        .height(56.dp)
                ) { Text("Next") }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewApp() {
    ArtSpaceApp()
}
