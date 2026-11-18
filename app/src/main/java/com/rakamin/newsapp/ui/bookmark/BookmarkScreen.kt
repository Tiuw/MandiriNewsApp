package com.rakamin.newsapp.ui.bookmark

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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmarks
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.components.AppBottomNavigation
import com.rakamin.newsapp.ui.theme.MandiriBlue
import com.rakamin.newsapp.ui.util.toFormattedDate
import kotlinx.coroutines.launch

private val SavedPrimary = MandiriBlue
private val SavedSecondary = Color(0xFF005696)
private val SavedBackground = Color(0xFFFAF8FF)
private val SavedSurface = Color(0xFFFFFFFF)
private val SavedText = Color(0xFF131B2E)
private val SavedTextVariant = Color(0xFF44474F)
private val SavedError = Color(0xFFBA1A1A)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun BookmarkScreen(
    onNewsClick: (News) -> Unit,
    onBackClick: () -> Unit,
    onHomeClick: () -> Unit = onBackClick,
    onSearchClick: () -> Unit = {},
    bookmarkViewModel: BookmarkViewModel = viewModel(
        factory = BookmarkViewModelFactory(LocalContext.current)
    )
) {
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val bookmarkedNews by bookmarkViewModel.bookmarkedNews.collectAsState()

    var isSelectMode by remember { mutableStateOf(false) }
    var selectedUrls by remember { mutableStateOf(setOf<String>()) }

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        containerColor = SavedBackground,
        topBar = {
            SavedTopBar(
                articleCount = bookmarkedNews.size,
                isSelectMode = isSelectMode,
                onSelectClick = {
                    isSelectMode = !isSelectMode
                    selectedUrls = emptySet()
                    showMessage(if (isSelectMode) "Mode multi-pilih aktif" else "Mode normal")
                }
            )
        },
        bottomBar = {
            AppBottomNavigation(
                currentRoute = "saved",
                onHomeClick = onHomeClick,
                onSearchClick = onSearchClick,
                onSavedClick = {}
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = Color(0xFF283044),
                    contentColor = Color.White,
                    shape = RoundedCornerShape(50)
                )
            }
        }
    ) { innerPadding ->

        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            if (bookmarkedNews.isEmpty()) {
                item {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(top = 60.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Icon(
                            imageVector = Icons.Default.Bookmarks,
                            contentDescription = null,
                            tint = Color(0xFF94A3B8),
                            modifier = Modifier.size(54.dp)
                        )
                        Spacer(Modifier.height(12.dp))
                        Text(
                            text = "Belum Ada Artikel Tersimpan",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold,
                            color = SavedText
                        )
                        Spacer(Modifier.height(4.dp))
                        Text(
                            text = "Tandai berita favorit Anda agar dapat dibaca kembali secara offline.",
                            fontSize = 12.sp,
                            color = SavedTextVariant
                        )
                    }
                }
            } else {
                items(
                    items = bookmarkedNews,
                    key = { it.url ?: it.title.hashCode().toString() }
                ) { article ->
                    val url = article.url.orEmpty()
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(16.dp))
                            .background(Color.White)
                            .border(1.dp, Color(0xFFE2E8F0), RoundedCornerShape(16.dp))
                            .clickable {
                                if (isSelectMode) {
                                    selectedUrls = if (url in selectedUrls) selectedUrls - url else selectedUrls + url
                                } else {
                                    onNewsClick(article)
                                }
                            }
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = article.title ?: "Tanpa Judul",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SavedText,
                                maxLines = 2
                            )
                            Spacer(Modifier.height(4.dp))
                            Text(
                                text = "${article.sourceName ?: "Redaksi"} • ${article.publishedAt.toFormattedDate()}",
                                fontSize = 10.sp,
                                color = SavedTextVariant
                            )
                        }

                        IconButton(
                            onClick = {
                                if (url.isNotBlank()) {
                                    bookmarkViewModel.removeBookmark(url)
                                    showMessage("Artikel dihapus dari simpanan")
                                }
                            }
                        ) {
                            Icon(
                                imageVector = Icons.Default.Delete,
                                contentDescription = "Hapus",
                                tint = SavedError
                            )
                        }
                    }
                }
            }

            if (isSelectMode && selectedUrls.isNotEmpty()) {
                item {
                    val count = selectedUrls.size
                    Button(
                        onClick = {
                            selectedUrls.forEach { url -> bookmarkViewModel.removeBookmark(url) }
                            selectedUrls = emptySet()
                            isSelectMode = false
                            showMessage("$count artikel dihapus dari bookmark")
                        },
                        modifier = Modifier.fillMaxWidth(),
                        colors = ButtonDefaults.buttonColors(containerColor = SavedError),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                        Spacer(Modifier.width(8.dp))
                        Text(
                            text = "Hapus $count artikel terpilih",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun SavedTopBar(
    articleCount: Int,
    isSelectMode: Boolean,
    onSelectClick: () -> Unit
) {
    TopAppBar(
        title = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(34.dp)
                        .clip(RoundedCornerShape(9.dp))
                        .background(SavedPrimary),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Bookmarks,
                        contentDescription = null,
                        tint = Color.White,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(Modifier.width(10.dp))
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "Artikel Tersimpan",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SavedPrimary
                    )
                    Spacer(Modifier.width(7.dp))
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50))
                            .background(Color(0xFFD5E3FF))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = articleCount.toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = SavedPrimary
                        )
                    }
                }
            }
        },
        actions = {
            TextButton(onClick = onSelectClick) {
                Text(
                    text = if (isSelectMode) "Selesai" else "Pilih",
                    color = SavedSecondary,
                    fontWeight = FontWeight.SemiBold
                )
            }
        },
        colors = TopAppBarDefaults.topAppBarColors(
            containerColor = SavedSurface
        )
    )
}