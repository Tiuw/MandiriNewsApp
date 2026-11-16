
package com.rakamin.newsapp.ui.home

import androidx.compose.foundation.background
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshBox
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.paging.LoadState
import androidx.paging.compose.collectAsLazyPagingItems
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModel
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModelFactory
import com.rakamin.newsapp.ui.components.NewsImage
import com.rakamin.newsapp.ui.search.AdvancedSearchFilters
import com.rakamin.newsapp.ui.search.SearchViewModel
import com.rakamin.newsapp.ui.search.SearchViewModelFactory
import com.rakamin.newsapp.ui.util.toFormattedDate

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    onNewsClick: (News) -> Unit,
    onSavedNewsClick: () -> Unit,
    viewModel: HomeViewModel = viewModel(
        factory = HomeViewModelFactory()
    ),
    searchViewModel: SearchViewModel = viewModel(
        factory = SearchViewModelFactory()
    ),
    bookmarkViewModel: BookmarkViewModel = viewModel(
        factory = BookmarkViewModelFactory(LocalContext.current)
    ),
) {
    val uiState by viewModel.uiState.collectAsState()
    val searchUiState by searchViewModel.uiState.collectAsState()
    val selectedCategory by viewModel.selectedCategory.collectAsState()

    val bookmarkedNews by bookmarkViewModel.bookmarkedNews.collectAsState()

    val bookmarkedUrls = bookmarkedNews
        .mapNotNull { it.url }
        .toSet()

    val newsItems = viewModel.newsPagingFlow.collectAsLazyPagingItems()
    val searchItems = searchViewModel.searchResults.collectAsLazyPagingItems()

    var isSearchActive by remember { mutableStateOf(false) }

    PullToRefreshBox(
        isRefreshing = if (isSearchActive) {
            searchItems.loadState.refresh is LoadState.Loading &&
                    searchItems.itemCount > 0
        } else {
            newsItems.loadState.refresh is LoadState.Loading &&
                    newsItems.itemCount > 0
        },
        onRefresh = {
            if (isSearchActive) {
                searchItems.refresh()
            } else {
                newsItems.refresh()
                viewModel.getHeadlineNews()
            }
        },
        modifier = Modifier.fillMaxSize()
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .background(MaterialTheme.colorScheme.background),
            contentPadding = PaddingValues(20.dp)
        ) {

            // HEADER AND SEARCH BAR
            item {
                HeaderSection(
                    onSavedNewsClick = onSavedNewsClick
                )

                Spacer(modifier = Modifier.height(24.dp))

                SearchBar(
                    query = searchUiState.query,
                    onQueryChange = searchViewModel::onQueryChange,
                    onSearch = {
                        searchViewModel.searchNews()
                        isSearchActive = searchUiState.query.isNotBlank()
                    },
                    onClear = {
                        searchViewModel.onQueryChange("")
                        searchViewModel.searchNews()
                        isSearchActive = false
                    }
                )
            }

            // ADVANCED SEARCH FILTERS
            item {
                Spacer(modifier = Modifier.height(12.dp))

                AdvancedSearchFilters(
                    state = searchUiState,
                    onFromDateChange = searchViewModel::onFromDateChange,
                    onToDateChange = searchViewModel::onToDateChange,
                    onSortByChange = searchViewModel::onSortByChange,
                    onSourcesChange = searchViewModel::onSourcesChange,
                    onDomainsChange = searchViewModel::onDomainsChange,
                    onApply = {
                        if (searchUiState.query.isNotBlank()) {
                            searchViewModel.searchNews()
                            isSearchActive = true
                        }
                    },
                    onReset = {
                        searchViewModel.clearFilters()
                    }
                )
            }

            // GREETING
            if (!isSearchActive) {
                item {
                    Spacer(modifier = Modifier.height(28.dp))

                    GreetingSection()

                    Spacer(modifier = Modifier.height(24.dp))
                }
            }

            if (isSearchActive) {

                // SEARCH RESULTS
                item {
                    Text(
                        text = "Search Results",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
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
                                CircularProgressIndicator()
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
                                    text = refreshState.error.localizedMessage
                                        ?: "Failed to search news"
                                )

                                Button(
                                    onClick = { searchItems.retry() }
                                ) {
                                    Text("Try Again")
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (searchItems.itemCount == 0) {
                            item {
                                Text("No news found")
                            }
                        }
                    }
                }

                items(searchItems.itemCount) { index ->
                    val article = searchItems[index]

                    if (article != null) {
                        NewsItem(
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

                when (searchItems.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            Button(
                                onClick = { searchItems.retry() }
                            ) {
                                Text("Retry")
                            }
                        }
                    }

                    is LoadState.NotLoading -> Unit
                }

            } else {

                // HOME CONTENT

                item {
                    Text(
                        text = "Today's Headline",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
                }

                item {
                    when {
                        uiState.isLoading -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(200.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }

                        uiState.error != null -> {
                            Column(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text(
                                    text = uiState.error
                                        ?: "Something went wrong"
                                )

                                Button(
                                    onClick = viewModel::getHeadlineNews
                                ) {
                                    Text("Try Again")
                                }
                            }
                        }

                        uiState.headlines.isEmpty() -> {
                            Text("No headline available")
                        }

                        else -> {
                            HeadlineCard(
                                news = uiState.headlines.first(),
                                onClick = onNewsClick,
                                isBookmarked = uiState.headlines.first().url in bookmarkedUrls,
                                onBookmarkClick = {
                                    bookmarkViewModel.toggleBookmark(
                                        uiState.headlines.first(),
                                        uiState.headlines.first().url in bookmarkedUrls
                                    )
                                }
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(28.dp))

                    // CATEGORY SECTION
                    CategorySection(
                        selectedCategory = selectedCategory,
                        onCategorySelected = viewModel::onCategorySelected
                    )

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Latest News",
                        fontSize = 19.sp,
                        fontWeight = FontWeight.Bold
                    )

                    Spacer(modifier = Modifier.height(12.dp))
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
                                CircularProgressIndicator()
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
                                    text = refreshState.error.localizedMessage
                                        ?: "Failed to load news"
                                )

                                Button(
                                    onClick = { newsItems.retry() }
                                ) {
                                    Text("Try Again")
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> {
                        if (newsItems.itemCount == 0) {
                            item {
                                Text("No news available")
                            }
                        }
                    }
                }

                items(newsItems.itemCount) { index ->
                    val article = newsItems[index]

                    if (article != null) {
                        NewsItem(
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

                when (newsItems.loadState.append) {
                    is LoadState.Loading -> {
                        item {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                contentAlignment = Alignment.Center
                            ) {
                                CircularProgressIndicator()
                            }
                        }
                    }

                    is LoadState.Error -> {
                        item {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(16.dp),
                                horizontalAlignment = Alignment.CenterHorizontally
                            ) {
                                Text("Failed to load more news")

                                Button(
                                    onClick = { newsItems.retry() }
                                ) {
                                    Text("Retry")
                                }
                            }
                        }
                    }

                    is LoadState.NotLoading -> Unit
                }
            }
        }
    }
}

@Composable
private fun CategorySection(
    selectedCategory: String?,
    onCategorySelected: (String?) -> Unit
) {
    val categories = listOf(
        "All" to null,
        "Business" to "business",
        "Technology" to "technology",
        "Sports" to "sports",
        "Science" to "science",
        "Entertainment" to "entertainment",
        "Health" to "health"
    )

    Column {
        Text(
            text = "Categories",
            fontSize = 19.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(12.dp))

        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(categories.size) { index ->
                val (label, category) = categories[index]

                FilterChip(
                    selected = selectedCategory == category,
                    onClick = {
                        onCategorySelected(category)
                    },
                    label = {
                        Text(text = label)
                    }
                )
            }
        }
    }
}

@Composable
private fun SearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    onClear: () -> Unit
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        OutlinedTextField(
            value = query,
            onValueChange = onQueryChange,
            modifier = Modifier.weight(1f),
            placeholder = {
                Text("Search news...")
            },
            singleLine = true,
            keyboardOptions = KeyboardOptions(
                imeAction = ImeAction.Search
            ),
            keyboardActions = KeyboardActions(
                onSearch = { onSearch() }
            ),
            trailingIcon = {
                if (query.isNotEmpty()) {
                    IconButton(onClick = onClear) {
                        Text("×", fontSize = 20.sp)
                    }
                }
            },
            shape = RoundedCornerShape(14.dp)
        )

        Button(
            onClick = onSearch,
            enabled = query.isNotBlank()
        ) {
            Text("Search")
        }
    }
}

@Composable
private fun HeaderSection(
    onSavedNewsClick: () -> Unit
) {
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
                fontWeight = FontWeight.ExtraBold
            )
        }

        Button(
            onClick = onSavedNewsClick
        ) {
            Text("Saved")
        }
    }
}

@Composable
private fun GreetingSection() {
    Column {
        Text(
            text = "Good morning!",
            fontSize = 26.sp,
            fontWeight = FontWeight.Bold
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
private fun HeadlineCard(
    news: News,
    onClick: (News) -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable {
                onClick(news)
            },
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primary
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = news.title,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(180.dp)
            )

            Spacer(modifier = Modifier.height(14.dp))

            Text(
                text = news.sourceName ?: "News",
                color = MaterialTheme.colorScheme.onPrimary.copy(alpha = 0.8f),
                fontSize = 12.sp
            )

            Button(
                onClick = onBookmarkClick
            ) {
                Text(if (isBookmarked) "Saved" else "Save")
            }

            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = news.title ?: "Untitled article",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold,
                color = MaterialTheme.colorScheme.onPrimary
            )
        }
    }
}

@Composable
private fun NewsItem(
    news: News,
    onClick: (News) -> Unit,
    isBookmarked: Boolean,
    onBookmarkClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 12.dp)
            .clickable {
                onClick(news)
            },
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surface
        )
    ) {
        Row(
            modifier = Modifier.padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = news.title,
                modifier = Modifier.size(88.dp)
            )

            TextButton(
                onClick = onBookmarkClick
            ) {
                Text(if (isBookmarked) "Saved" else "Save")
            }

            Spacer(modifier = Modifier.width(14.dp))

            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = news.sourceName ?: "News",
                    fontSize = 10.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = news.title ?: "Untitled article",
                    fontSize = 14.sp,
                    fontWeight = FontWeight.SemiBold,
                    lineHeight = 20.sp
                )

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = news.publishedAt.toFormattedDate(),
                    fontSize = 11.sp,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}
