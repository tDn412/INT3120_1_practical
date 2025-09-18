package com.example.artspace

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.artspace.ui.theme.ArtSpaceTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ArtSpaceTheme {
                Surface(Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    ArtSpaceScreen()
                }
            }
        }
    }
}

@Composable
fun ArtSpaceScreen() {
    var index by remember { mutableStateOf(0) }

    // Thay tên ảnh đúng với drawable của bạn
    val images = listOf(R.drawable.artwork_1, R.drawable.artwork_2, R.drawable.artwork_3)
    val titles = listOf(
        "1",
        "2",
        "3"
    )
    val artists = listOf("Tu", "Unknown Artist", "Van Gogh")
    val years = listOf("2021", "2020", "2025")

    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp)
    ) {
        //đẩy 2 nút xuống đáy
        Column(
            modifier = Modifier.weight(1f),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // 1) Khung tranh nổi có elevation + viền trắng bên trong
            ElevatedCard(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                elevation = CardDefaults.elevatedCardElevation(defaultElevation = 8.dp),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(24.dp)) { // tạo “viền trắng” dày quanh ảnh
                    Image(
                        painter = painterResource(images[index]),
                        contentDescription = titles[index],
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // 2) Thẻ mô tả (nền xám nhạt)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 8.dp),
                colors = CardDefaults.cardColors(
                    containerColor = MaterialTheme.colorScheme.surfaceVariant
                ),
                shape = MaterialTheme.shapes.medium
            ) {
                Column(Modifier.padding(24.dp)) {
                    Text(
                        text = titles[index],
                        fontSize = 22.sp
                    )
                    Text(
                        text = buildAnnotatedString {
                            withStyle(SpanStyle(fontWeight = FontWeight.Bold)) {
                                append(artists[index])
                            }
                            append(" (${years[index]})")
                        },
                        fontSize = 16.sp
                    )
                }
            }
        }

        Spacer(Modifier.height(24.dp))

        // 3) Hai nút pill rộng bằng nhau, sát đáy
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Button(
                onClick = {
                    index = if (index > 0) index - 1 else images.lastIndex
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large // bo tròn
            ) {
                Text("Previous")
            }
            Button(
                onClick = {
                    index = if (index < images.lastIndex) index + 1 else 0
                },
                modifier = Modifier
                    .weight(1f)
                    .height(56.dp),
                shape = MaterialTheme.shapes.large
            ) {
                Text("Next")
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewArtSpace() {
    ArtSpaceTheme { ArtSpaceScreen() }
}
