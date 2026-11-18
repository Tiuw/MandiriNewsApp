package com.rakamin.newsapp.ui.detail

import android.content.Intent
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.OpenInNew
import androidx.compose.material.icons.automirrored.filled.TrendingUp
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.BookmarkBorder
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.FormatSize
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Share
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModel
import com.rakamin.newsapp.ui.bookmark.BookmarkViewModelFactory
import com.rakamin.newsapp.ui.components.NewsImage
import com.rakamin.newsapp.ui.util.toFormattedDate
import kotlinx.coroutines.launch

private val Navy = Color(0xFF001E4A)
private val NavyContainer = Color(0xFF002B66)
private val MandiriGold = Color(0xFFF3A511)
private val SurfaceBackground = Color(0xFFFAFBFC)
private val TextPrimary = Color(0xFF0F172A)
private val TextSecondary = Color(0xFF475569)
private val BorderColor = Color(0xFFE2E8F0)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(
    news: News,
    onBackClick: () -> Unit,
    bookmarkViewModel: BookmarkViewModel = viewModel(
        factory = BookmarkViewModelFactory(LocalContext.current),
    )
) {
    val context = LocalContext.current
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    val bookmarkedNews by bookmarkViewModel.bookmarkedNews.collectAsState()
    val isBookmarked = bookmarkedNews.any { it.url == news.url }

    var fontSizeLevel by remember { mutableIntStateOf(1) }

    val articleTitle = news.title.orEmpty()
    val articleUrl = news.url.orEmpty()

    val bodyFontSize = when (fontSizeLevel) {
        0 -> 14.sp
        1 -> 16.sp
        else -> 18.sp
    }

    fun showMessage(message: String) {
        scope.launch {
            snackbarHostState.showSnackbar(message)
        }
    }

    Scaffold(
        containerColor = SurfaceBackground,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Detail Berita",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = TextPrimary
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Kembali",
                            tint = TextPrimary
                        )
                    }
                },
                actions = {
                    // Pengubah ukuran teks
                    IconButton(
                        onClick = {
                            fontSizeLevel = (fontSizeLevel + 1) % 3
                            val label = when (fontSizeLevel) {
                                0 -> "Normal"
                                1 -> "Sedang"
                                else -> "Besar"
                            }
                            showMessage("Ukuran teks: $label")
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.FormatSize,
                            contentDescription = "Ubah ukuran teks",
                            tint = TextSecondary
                        )
                    }

                    // Toggle Bookmark
                    IconButton(
                        onClick = {
                            bookmarkViewModel.toggleBookmark(news, isBookmarked)
                            showMessage(
                                if (!isBookmarked) {
                                    "Artikel disimpan ke bookmark Anda"
                                } else {
                                    "Artikel dihapus dari bookmark"
                                }
                            )
                        }
                    ) {
                        Icon(
                            imageVector = if (isBookmarked) {
                                Icons.Default.Bookmark
                            } else {
                                Icons.Default.BookmarkBorder
                            },
                            contentDescription = "Bookmark",
                            tint = Navy
                        )
                    }

                    // Share Intent
                    IconButton(
                        onClick = {
                            if (articleUrl.isNotBlank()) {
                                val intent = Intent(Intent.ACTION_SEND).apply {
                                    type = "text/plain"
                                    putExtra(Intent.EXTRA_SUBJECT, articleTitle)
                                    putExtra(
                                        Intent.EXTRA_TEXT,
                                        "$articleTitle\n\n$articleUrl"
                                    )
                                }
                                context.startActivity(
                                    Intent.createChooser(intent, "Bagikan artikel")
                                )
                            } else {
                                showMessage("Tautan artikel tidak tersedia")
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Share,
                            contentDescription = "Bagikan",
                            tint = TextSecondary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        snackbarHost = {
            SnackbarHost(hostState = snackbarHostState) { data ->
                Snackbar(
                    snackbarData = data,
                    containerColor = TextPrimary,
                    contentColor = Color.White
                )
            }
        }
    ) { innerPadding ->

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp)
                .padding(bottom = 32.dp)
        ) {

            Spacer(modifier = Modifier.height(12.dp))

            // Tag Kategori / Sumber
            Row(
                modifier = Modifier
                    .clip(RoundedCornerShape(50))
                    .background(Color(0xFFEFF6FF))
                    .padding(horizontal = 12.dp, vertical = 7.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(7.dp)
                        .clip(CircleShape)
                        .background(MandiriGold)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Text(
                    text = (news.sourceName ?: "BERITA").uppercase(),
                    color = NavyContainer,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.7.sp
                )
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Judul Artikel dari API
            Text(
                text = articleTitle.ifBlank { "Untitled Article" },
                color = Navy,
                fontSize = 24.sp,
                lineHeight = 32.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(18.dp))

            // Informasi Publisher & Penulis
            PublisherInformation(news = news)

            Spacer(modifier = Modifier.height(20.dp))

            // Gambar Utama dari API
            NewsImage(
                imageUrl = news.imageUrl,
                contentDescription = articleTitle,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(230.dp)
                    .clip(RoundedCornerShape(18.dp))
            )

            if (!news.sourceName.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(8.dp))

                Text(
                    text = "Sumber: ${news.sourceName}",
                    color = TextSecondary,
                    fontSize = 11.sp,
                    fontStyle = FontStyle.Italic
                )
            }

            // Ringkasan Eksekutif (Description dari API)
            if (!news.description.isNullOrBlank()) {
                Spacer(modifier = Modifier.height(22.dp))

                ExecutiveSummary(description = news.description)
            }

            // Body Konten Berita dari API
            Spacer(modifier = Modifier.height(24.dp))

            ArticleBody(
                content = news.content,
                description = news.description,
                fontSize = bodyFontSize
            )

            // Tombol Buka Artikel Asli di Web
            if (articleUrl.isNotBlank()) {
                Spacer(modifier = Modifier.height(28.dp))

                SourceSection(
                    sourceName = news.sourceName ?: "Sumber Berita",
                    onOpenSource = {
                        val intent = Intent(Intent.ACTION_VIEW).apply {
                            data = articleUrl.toUri()
                        }
                        context.startActivity(intent)
                    }
                )
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}

@Composable
private fun PublisherInformation(news: News) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .background(Color(0xFFF8FAFC))
            .padding(14.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .clip(CircleShape)
                        .background(Navy),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = (news.sourceName?.take(2) ?: "MN").uppercase(),
                        color = Color.White,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }

                Spacer(modifier = Modifier.width(9.dp))

                Text(
                    text = news.sourceName ?: "Sumber Resmi",
                    color = TextPrimary,
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
                )

                Spacer(modifier = Modifier.width(4.dp))

                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = "Terverifikasi",
                    tint = Color(0xFF2563EB),
                    modifier = Modifier.size(16.dp)
                )
            }

            if (!news.publishedAt.isNullOrBlank()) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(Color.White)
                        .padding(horizontal = 7.dp, vertical = 5.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Schedule,
                        contentDescription = null,
                        tint = TextSecondary,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = news.publishedAt.toFormattedDate(),
                        fontSize = 10.sp,
                        color = TextSecondary
                    )
                }
            }
        }

        HorizontalDivider(
            modifier = Modifier.padding(vertical = 11.dp),
            color = BorderColor
        )

        Text(
            text = "Penulis: ${news.author ?: news.sourceName ?: "Redaksi"}",
            color = TextSecondary,
            fontSize = 11.sp
        )
    }
}

@Composable
private fun ExecutiveSummary(description: String) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(18.dp))
            .background(Color(0xFFF0F6FF))
            .padding(16.dp)
    ) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(4.dp)
                .background(MandiriGold)
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = Icons.Default.Bolt,
                contentDescription = null,
                tint = Navy,
                modifier = Modifier.size(21.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = "RINGKASAN ARTIKEL",
                color = Navy,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        SummaryItem(
            icon = {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.TrendingUp,
                    contentDescription = null,
                    tint = Navy,
                    modifier = Modifier.size(17.dp)
                )
            },
            title = "Sorotan Utama Artikel",
            description = description
        )

        Spacer(modifier = Modifier.height(9.dp))

        SummaryItem(
            icon = {
                Icon(
                    imageVector = Icons.Default.Shield,
                    contentDescription = null,
                    tint = Color(0xFF047857),
                    modifier = Modifier.size(17.dp)
                )
            },
            title = "Informasi Terverifikasi",
            description = "Disajikan langsung dari penyedia berita tepercaya."
        )
    }
}

@Composable
private fun SummaryItem(
    icon: @Composable () -> Unit,
    title: String,
    description: String
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(12.dp))
            .background(Color.White.copy(alpha = 0.85f))
            .padding(11.dp),
        verticalAlignment = Alignment.Top
    ) {
        Box(
            modifier = Modifier
                .size(29.dp)
                .clip(CircleShape)
                .background(Color(0xFFEFF6FF)),
            contentAlignment = Alignment.Center
        ) {
            icon()
        }

        Spacer(modifier = Modifier.width(10.dp))

        Column {
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold
            )
            Spacer(modifier = Modifier.height(3.dp))
            Text(
                text = description,
                color = TextSecondary,
                fontSize = 11.sp,
                lineHeight = 17.sp
            )
        }
    }
}

@Composable
private fun ArticleBody(
    content: String?,
    description: String?,
    fontSize: androidx.compose.ui.unit.TextUnit
) {
    val paragraphLineHeight = fontSize * 1.75f
    val fullText = content ?: description ?: "Konten berita lengkap tidak tersedia dari penyedia API."

    Column(verticalArrangement = Arrangement.spacedBy(17.dp)) {
        Text(
            text = buildAnnotatedString {
                withStyle(
                    SpanStyle(
                        color = Navy,
                        fontWeight = FontWeight.Bold
                    )
                ) {
                    append("BERITA — ")
                }
                append(fullText)
            },
            color = TextPrimary,
            fontSize = fontSize,
            lineHeight = paragraphLineHeight,
            fontWeight = FontWeight.Normal,
            textAlign = TextAlign.Start
        )
    }
}

@Composable
private fun SourceSection(
    sourceName: String,
    onOpenSource: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(17.dp))
            .background(Color.White)
            .padding(15.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text(
                text = "VERIFIKASI SUMBER BERITA",
                color = TextPrimary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Bold,
                letterSpacing = 0.5.sp
            )
            Text(
                text = "Sumber Resmi",
                color = TextSecondary,
                fontSize = 10.sp
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        Button(
            onClick = onOpenSource,
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp),
            shape = RoundedCornerShape(11.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Navy
            )
        ) {
            Text(
                text = "Buka Artikel Asli di $sourceName",
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium
            )
            Spacer(modifier = Modifier.width(7.dp))
            Icon(
                imageVector = Icons.AutoMirrored.Filled.OpenInNew,
                contentDescription = null,
                modifier = Modifier.size(17.dp)
            )
        }
    }
}