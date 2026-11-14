
package com.rakamin.newsapp.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun HomeScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(MaterialTheme.colorScheme.background)
            .verticalScroll(rememberScrollState())
            .padding(20.dp)
    ) {
        HeaderSection()

        Spacer(modifier = Modifier.height(28.dp))

        GreetingSection()

        Spacer(modifier = Modifier.height(24.dp))

        HeadlineSection()

        Spacer(modifier = Modifier.height(28.dp))

        LatestNewsSection()
    }
}

@Composable
private fun HeaderSection() {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = "MANDIRI",
                fontSize = 13.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.primary,
                letterSpacing = 2.sp
            )

            Text(
                text = "NEWS",
                fontSize = 22.sp,
                fontWeight = FontWeight.ExtraBold,
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        Surface(
            shape = RoundedCornerShape(50),
            color = MaterialTheme.colorScheme.surfaceVariant,
            modifier = Modifier.size(44.dp)
        ) {
            Box(contentAlignment = Alignment.Center) {
                Text(
                    text = "N",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}

@Composable
private fun GreetingSection() {
    Column {
        Text(
            text = "Good morning!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold,
            color = MaterialTheme.colorScheme.onBackground
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Stay informed with the latest updates.",
            fontSize = 14.sp,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun HeadlineSection() {
    Column {
        SectionTitle(title = "Today's Headline")

        Spacer(modifier = Modifier.height(12.dp))

        Card(
            modifier = Modifier.fillMaxWidth(),
            shape = RoundedCornerShape(20.dp),
            colors = CardDefaults.cardColors(
                containerColor = MaterialTheme.colorScheme.primary
            )
        ) {
            Column(
                modifier = Modifier.padding(20.dp)
            ) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(160.dp)
                        .background(
                            color = MaterialTheme.colorScheme.primaryContainer,
                            shape = RoundedCornerShape(14.dp)
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "FEATURED NEWS",
                        color = MaterialTheme.colorScheme.onPrimaryContainer,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 2.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = "Business & Economy",
                    fontSize = 12.sp,
                    color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f)
                )

                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "The Latest Developments in Indonesia's Economy",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onPrimary,
                    lineHeight = 28.sp
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "Read more →",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onPrimary
                )
            }
        }
    }
}

@Composable
private fun LatestNewsSection() {
    Column {
        SectionTitle(title = "Latest News")

        Spacer(modifier = Modifier.height(12.dp))

        NewsItem(
            category = "Business",
            title = "Indonesia's Digital Banking Industry Continues to Grow",
            time = "10 minutes ago"
        )

        NewsItem(
            category = "Technology",
            title = "How Technology Is Transforming Financial Services",
            time = "30 minutes ago"
        )

        NewsItem(
            category = "Economy",
            title = "Market Updates: What Investors Need to Know",
            time = "1 hour ago"
        )
    }
}

@Composable
private fun SectionTitle(title: String) {
    Text(
        text = title,
        fontSize = 19.sp,
        fontWeight = FontWeight.Bold,
        color = MaterialTheme.colorScheme.onBackground
    )
}

@Composable
private fun NewsItem(
    category: String,
    title: String,
    time: String
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        ),
        elevation = CardDefaults.cardElevation(
            defaultElevation = 1.dp
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(88.dp)
                    .background(
                        color = MaterialTheme.colorScheme.secondaryContainer,
                        shape = RoundedCornerShape(12.dp)
                    ),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "NEWS",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSecondaryContainer
                )
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = category.uppercase(),
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = title,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = MaterialTheme.colorScheme.onSurface,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = time,
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
