package com.rakamin.newsapp.ui.home

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModel
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModelFactory
import com.rakamin.newsapp.ui.components.AppBottomNavigation
import com.rakamin.newsapp.ui.components.NewsImage
import com.rakamin.newsapp.ui.theme.MandiriBlue
import com.rakamin.newsapp.ui.theme.RadiantGold
import com.rakamin.newsapp.ui.util.toFormattedDate

private val MandiriNavyDark = Color(0xFF001838)
private val MandiriGoldLight = Color(0xFFFFDE94)
private val SurfaceColor = Color(0xFFF8FAFC)
private val TextSecondary = Color(0xFF64748B)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewsClick: (News) -> Unit,
    onSearchClick: () -> Unit,
    onSavedNewsClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory()
    ),
    bookmarkViewModel: BookmarkViewModel = viewModel(
        factory = BookmarkViewModelFactory(LocalContext.current)
    ),
) {
    val uiState by viewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val bookmarkedNews by bookmarkViewModel.bookmarkedNews.collectAsState()
    val bookmarkedUrls = bookmarkedNews.mapNotNull { it.url }.toSet()

    val newsItems = viewModel.newsPagingFlow.collectAsLazyPagingItems()

    val categories = listOf(
        "Semua" to null,
        "Bisnis & Finansial" to "business",
        "Teknologi" to "technology",
        "Sports" to "sports",
        "Science" to "science",
        "Entertainment" to "entertainment",
        "Health" to "health"
    )

    Scaffold(
        containerColor = SurfaceColor,
        bottomBar = {
            AppBottomNavigation(
                currentRoute = "home",
                onHomeClick = { },
                onSearchClick = onSearchClick,
                onSavedClick = onSavedNewsClick
            )
        }
    ) { innerPadding ->
        PullToRefreshBox(
            isRefreshing = newsItems.loadState.refresh is LoadState.Loading && newsItems.itemCount > 0,
            onRefresh = {
                newsItems.refresh()
                viewModel.getHeadlineNews()
            },
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .background(SurfaceColor),
                contentPadding = PaddingValues(
                    start = 16.dp,
                    end = 16.dp,
                    top = 12.dp,
                    bottom = 24.dp
                ),
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    HomeHeader()
                }

                item {
                    GreetingSection()
                }

                item {
                    SearchSection(
                        onClick = onSearchClick
                    )
                }

                item {
                    CategorySection(
                        categories = categories,
                        selectedCategory = selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected
                    )
                }

                item {
                    val headlineArticle = uiState.headlines.firstOrNull()
                    if (uiState.isLoading) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(220.dp)
                                .clip(RoundedCornerShape(18.dp))
                                .background(MandiriBlue.copy(alpha = 0.05f)),
                            contentAlignment = Alignment.Center
                        ) {
                            CircularProgressIndicator(color = MandiriBlue)
                        }
                    } else if (headlineArticle != null) {
                        FeaturedNewsCard(
                            news = headlineArticle,
                            onClick = onNewsClick,
                            isBookmarked = headlineArticle.url in bookmarkedUrls,
                            onBookmarkClick = {
                                bookmarkViewModel.toggleBookmark(
                                    headlineArticle,
                                    headlineArticle.url in bookmarkedUrls
                                )
                            }
                        )
                    }
                }

                item {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "Berita Terkini",
                                fontSize = 17.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF0F172A)
                            )

                            Spacer(Modifier.width(8.dp))

                            Surface(
                                color = MandiriBlue.copy(alpha = 0.06f),
                                shape = RoundedCornerShape(50)
                            ) {
                                Text(
                                    text = "● LIVE",
                                    modifier = Modifier.padding(
                                        horizontal = 8.dp,
                                        vertical = 4.dp
                                    ),
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = MandiriBlue
                                )
                            }
                        }
                    }
                }

                when (val refreshState = newsItems.loadState.refresh) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(160.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator(color = MandiriBlue)
                            }
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = refreshState.error.localizedMessage ?: "Gagal memuat berita",
                                    fontSize = 12.sp,
                                    color = TextSecondary
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = { newsItems.retry() },
                                    colors = ButtonDefaults.buttonColors(containerColor = MandiriBlue)
                                ) {
                                    Text("Coba Lagi")
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (newsItems.itemCount == 0) {
                            item {
                                Text(
                                    text = "Tidak ada berita tersedia",
                                    fontSize = 13.sp,
                                    color = TextSecondary
                                )
                            }
                        }
                    }
                }

                items(
                    count = newsItems.itemCount,
                    key = { index -> newsItems[index]?.url ?: index }
                ) { index ->
                    val article = newsItems[index]
                    if (article != null) {
                        NewsCard(
                            news = article,
                            onClick = onNewsClick,
                            isBookmarked = article.url in bookmarkedUrls,
                            onBookmarkClick = {
                                bookmarkViewModel.toggleBookmark(
                                    article,
                                    article.url in bookmarkedUrls
                                )
                            }
                        )
                    }
                }

                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 8.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(18.dp),
                            color = MandiriBlue,
                            strokeWidth = 2.dp
                        )

                        Spacer(Modifier.height(8.dp))

                        Text(
                            text = "Sinkronisasi berita terkini...",
                            fontSize = 11.sp,
                            color = TextSecondary
                        )
                    }
                }
            }
        }
    }
}

@Composable
private fun HomeHeader() {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .height(56.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(38.dp)
                .clip(RoundedCornerShape(12.dp))
                .background(MandiriBlue),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "m",
                color = RadiantGold,
                fontSize = 27.sp,
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(Modifier.width(10.dp))

        Column {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = "Mandiri",
                    color = MandiriBlue,
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(Modifier.width(4.dp))
                Text(
                    text = "News",
                    color = Color(0xFF0F172A),
                    fontSize = 17.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(text = " •", color = RadiantGold)
            }

            Text(
                text = "FINANCIAL & GLOBAL INSIGHTS",
                color = Color(0xFF94A3B8),
                fontSize = 9.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 1.sp
            )
        }
    }
}

@Composable
private fun GreetingSection() {
    Column {
        Text(
            text = "Selamat Datang",
            fontSize = 18.sp,
            fontWeight = FontWeight.Bold,
            color = Color(0xFF0F172A)
        )

        Spacer(Modifier.height(4.dp))

        Text(
            text = "Dapatkan informasi & berita terkini hari ini.",
            fontSize = 12.sp,
            color = TextSecondary
        )
    }
}

@Composable
private fun SearchSection(
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                1.dp,
                Color(0xFFE2E8F0),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick() }
            .padding(horizontal = 12.dp, vertical = 13.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Search,
            contentDescription = null,
            tint = Color(0xFF94A3B8)
        )

        Spacer(Modifier.width(8.dp))

        Text(
            text = "Cari berita terkini, pasar, emiten...",
            fontSize = 12.sp,
            color = Color(0xFF94A3B8),
            modifier = Modifier.weight(1f)
        )

        Icon(
            imageVector = Icons.Default.Tune,
            contentDescription = "Filter",
            tint = MandiriBlue
        )
    }
}

@Composable
private fun CategorySection(
    categories: List<Pair<String, String?>>,
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    Row(
        modifier = Modifier.horizontalScroll(rememberScrollState()),
        horizontalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        categories.forEach { (label, category) ->
            val selected = selectedCategory == category

            Surface(
                modifier = Modifier.clickable {
                    onCategorySelected(category)
                },
                shape = RoundedCornerShape(50),
                color = if (selected) MandiriBlue else Color.White,
                border = if (selected) null else BorderStroke(
                    1.dp,
                    Color(0xFFE2E8F0)
                )
            ) {
                Row(
                    modifier = Modifier.padding(
                        horizontal = 14.dp,
                        vertical = 9.dp
                    ),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    if (selected) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = RadiantGold,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(Modifier.width(5.dp))
                    }

                    Text(
                        text = label,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = if (selected) Color.White else Color(0xFF334155)
                    )
                }
            }
        }
    }
}

@Composable
private fun FeaturedNewsCard(
    news: News,
    onClick: (News) -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .height(270.dp)
            .clip(RoundedCornerShape(18.dp))
            .background(MandiriBlue)
            .clickable { onClick(news) }
    ) {
        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            modifier = Modifier.fillMaxSize()
        )

        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            Color.Black.copy(alpha = 0.35f),
                            Color.Transparent,
                            MandiriBlue.copy(alpha = 0.98f)
                        )
                    )
                )
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(14.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Surface(
                    color = RadiantGold,
                    shape = RoundedCornerShape(50)
                ) {
                    Text(
                        text = "● HEADLINE UTAMA",
                        modifier = Modifier.padding(
                            horizontal = 10.dp,
                            vertical = 6.dp
                        ),
                        color = MandiriNavyDark,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.ExtraBold
                    )
                }

                IconButton(
                    onClick = onBookmarkClick,
                    modifier = Modifier
                        .size(34.dp)
                        .background(
                            Color.Black.copy(alpha = 0.35f),
                            CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                        contentDescription = "Simpan berita",
                        tint = if (isBookmarked) RadiantGold else Color.White
                    )
                }
            }

            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = news.sourceName ?: "Headline",
                        color = MandiriGoldLight,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = "  •  ${news.publishedAt.toFormattedDate()}",
                        color = Color.White.copy(alpha = 0.8f),
                        fontSize = 10.sp
                    )
                }

                Spacer(Modifier.height(7.dp))

                Text(
                    text = news.title ?: "Untitled article",
                    color = Color.White,
                    fontSize = 16.sp,
                    lineHeight = 22.sp,
                    fontWeight = FontWeight.Bold,
                    maxLines = 3,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(Modifier.height(10.dp))

                HorizontalDivider(
                    color = Color.White.copy(alpha = 0.15f)
                )

                Spacer(Modifier.height(8.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = news.author ?: news.sourceName ?: "Redaksi",
                        color = Color.White.copy(alpha = 0.85f),
                        fontSize = 10.sp
                    )

                    Text(
                        text = "Baca →",
                        color = Color.White,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
private fun NewsCard(
    news: News,
    onClick: (News) -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(16.dp))
            .background(Color.White)
            .border(
                1.dp,
                Color(0xFFE2E8F0),
                RoundedCornerShape(16.dp)
            )
            .clickable { onClick(news) }
            .padding(12.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .height(88.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Column {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = (news.sourceName ?: "BERITA").uppercase(),
                        color = MandiriBlue,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Text(text = "  •  ", color = TextSecondary)

                    Text(
                        text = news.author ?: news.sourceName ?: "Mandiri News",
                        color = TextSecondary,
                        fontSize = 10.sp,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis
                    )
                }

                Spacer(Modifier.height(5.dp))

                Text(
                    text = news.title ?: "Untitled article",
                    fontSize = 12.sp,
                    lineHeight = 17.sp,
                    fontWeight = FontWeight.Bold,
                    color = Color(0xFF0F172A),
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )
            }

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = news.publishedAt.toFormattedDate(),
                    fontSize = 10.sp,
                    color = TextSecondary
                )

                Icon(
                    imageVector = if (isBookmarked) Icons.Default.Bookmark else Icons.Default.BookmarkBorder,
                    contentDescription = "Simpan berita",
                    modifier = Modifier
                        .size(20.dp)
                        .clickable { onBookmarkClick() },
                    tint = if (isBookmarked) RadiantGold else Color(0xFF94A3B8)
                )
            }
        }

        NewsImage(
            imageUrl = news.imageUrl,
            contentDescription = news.title,
            modifier = Modifier
                .size(88.dp)
                .clip(RoundedCornerShape(12.dp))
        )
    }
}