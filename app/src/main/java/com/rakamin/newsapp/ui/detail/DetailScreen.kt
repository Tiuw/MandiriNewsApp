
package com.rakamin.newsapp.ui.detail

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.components.NewsImage
import com.rakamin.newsapp.ui.util.toFormattedDate

@Composable
fun DetailScreen(
    news: News,
    onBackClick: () -> Unit
) {
    val uriHandler = LocalUriHandler.current

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        OutlinedButton(
            onClick = onBackClick
        ) {
            Text("← Back")
        }

        Spacer(modifier = Modifier.height(20.dp))

        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            modifier = Modifier
                .fillMaxWidth()
                .height(240.dp)
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = news.sourceName ?: "Unknown Source",
            style = MaterialTheme.typography.labelLarge,
            color = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = news.title ?: "Untitled Article",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "By ${news.author ?: "Unknown Author"}",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = news.publishedAt.toFormattedDate(),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Description",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = news.description ?: "No description available.",
            style = MaterialTheme.typography.bodyLarge,
            lineHeight = 24.sp
        )

        Spacer(modifier = Modifier.height(20.dp))

        Text(
            text = "Article Content",
            style = MaterialTheme.typography.titleMedium,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = news.content ?: "Full article content is not available.",
            style = MaterialTheme.typography.bodyMedium,
            lineHeight = 22.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        if (!news.url.isNullOrBlank()) {
            Button(
                onClick = {
                    uriHandler.openUri(news.url)
                },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text("Read Full Article")
            }
        }
    }
}
