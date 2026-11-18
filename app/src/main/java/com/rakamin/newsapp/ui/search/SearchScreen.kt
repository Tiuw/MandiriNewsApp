package com.rakamin.newsapp.ui.search

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.MicNone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Tune
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
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

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SearchScreen(
    onNewsClick: (News) -> Unit,
    onHomeClick: () -> Unit,
    onSavedClick: () -> Unit,
    searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory()
    ),
    bookmarkViewModel: BookmarkViewModel = viewModel(
        factory = BookmarkViewModelFactory(LocalContext.current)
    )
) {
    val searchUiState by searchViewModel.uiState.collectAsState()
    val searchItems = searchViewModel.searchResults.collectAsLazyPagingItems()

    val bookmarkedNews by bookmarkViewModel.bookmarkedNews.collectAsState()
    val bookmarkedUrls = bookmarkedNews.mapNotNull { it.url }.toSet()

    var showAdvancedFilters by remember { mutableStateOf(false) }

    Scaffold(
        containerColor = Color(0xFFF8FAFC),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Pencarian Berita",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = MandiriBlue
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = "search",
                onHomeClick = onHomeClick,
                onSearchClick = {},
                onSavedClick = onSavedClick
            )
        }
    ) { innerPadding ->
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                            .padding(horizontal = 12.dp, vertical = 13.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Search,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8)
                        )
                        Spacer(Modifier.width(8.dp))
                        BasicTextField(
                            value = searchUiState.query,
                            onValueChange = searchViewModel::onQueryChange,
                            modifier = Modifier.weight(1f),
                            singleLine = true,
                            textStyle = LocalTextStyle.current.copy(
                                fontSize = 12.sp,
                                color = Color(0xFF334155)
                            ),
                            keyboardOptions = KeyboardOptions(imeAction = ImeAction.Search),
                            keyboardActions = KeyboardActions(onSearch = { searchViewModel.searchNews() }),
                            decorationBox = { innerTextField ->
                                if (searchUiState.query.isEmpty()) {
                                    Text(
                                        text = "Ketik kata kunci berita, emiten...",
                                        fontSize = 12.sp,
                                        color = Color(0xFF94A3B8)
                                    )
                                }
                                innerTextField()
                            }
                        )
                        if (searchUiState.query.isNotEmpty()) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Hapus",
                                tint = Color(0xFF94A3B8),
                                modifier = Modifier
                                    .size(18.dp)
                                    .clickable {
                                        searchViewModel.onQueryChange("")
                                        searchViewModel.searchNews()
                                    }
                            )
                        } else {
                            Icon(
                                imageVector = Icons.Default.MicNone,
                                contentDescription = "Suara",
                                tint = Color(0xFF94A3B8)
                            )
                        }
                    }

                    Spacer(Modifier.width(8.dp))

                    Box(
                        modifier = Modifier
                            .size(46.dp)
                            .clip(RoundedCornerShape(15.dp))
                            .background(MandiriBlue.copy(alpha = 0.08f))
                            .clickable { showAdvancedFilters = !showAdvancedFilters },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Tune,
                            contentDescription = "Filter Lanjutan",
                            tint = MandiriBlue
                        )
                    }
                }
            }

            if (showAdvancedFilters) {
                item {
                    AdvancedSearchFilters(
                        state = searchUiState,
                        onFromDateChange = searchViewModel::onFromDateChange,
                        onToDateChange = searchViewModel::onToDateChange,
                        onSortByChange = searchViewModel::onSortByChange,
                        onSourcesChange = searchViewModel::onSourcesChange,
                        onDomainsChange = searchViewModel::onDomainsChange,
                        onApply = { searchViewModel.searchNews() },
                        onReset = { searchViewModel.clearFilters() }
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
                            text = if (searchUiState.query.isBlank()) "Berita Terkini" else "Hasil Pencarian",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(0xFF0F172A)
                        )

                        Spacer(Modifier.width(8.dp))

                        Surface(
                            color = MandiriBlue.copy(alpha = 0.06f),
                            shape = RoundedCornerShape(50)
                        ) {
                            Text(
                                text = if (searchUiState.query.isBlank()) "● LIVE" else searchUiState.query,
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                fontSize = 10.sp,
                                fontWeight = FontWeight.Bold,
                                color = MandiriBlue
                            )
                        }
                    }
                }
            }

            when (val refreshState = searchItems.loadState.refresh) {
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
                                color = Color(0xFF64748B)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Button(
                                onClick = { searchItems.retry() },
                                colors = ButtonDefaults.buttonColors(containerColor = MandiriBlue)
                            ) {
                                Text("Coba Lagi")
                            }
                        }
                    }
                }
                is LoadState.NotLoading -> {
                    if (searchItems.itemCount == 0) {
                        item {
                            Text(
                                text = if (searchUiState.query.isBlank()) "Tidak ada berita tersedia" else "Tidak ada berita ditemukan untuk \"${searchUiState.query}\"",
                                fontSize = 13.sp,
                                color = Color(0xFF64748B)
                            )
                        }
                    }
                }
            }

            items(
                count = searchItems.itemCount,
                key = { index -> searchItems[index]?.url ?: index }
            ) { index ->
                val article = searchItems[index]
                if (article != null) {
                    SearchNewsCard(
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
        }
    }
}

@Composable
private fun SearchNewsCard(
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
            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
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
                    Text(text = "  •  ", color = Color(0xFF64748B))
                    Text(
                        text = news.author ?: news.sourceName ?: "Mandiri News",
                        color = Color(0xFF64748B),
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
                    color = Color(0xFF64748B)
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