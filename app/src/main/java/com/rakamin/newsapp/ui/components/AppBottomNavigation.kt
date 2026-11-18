package com.rakamin.newsapp.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bookmark
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.rakamin.newsapp.ui.theme.MandiriBlue

@Composable
fun AppBottomNavigation(
    currentRoute: String,
    onHomeClick: () -> Unit,
    onSearchClick: () -> Unit,
    onSavedClick: () -> Unit
) {
    NavigationBar(
        containerColor = Color.White,
        contentColor = MandiriBlue,
        tonalElevation = 3.dp
    ) {
        NavigationBarItem(
            selected = currentRoute == "home",
            onClick = onHomeClick,
            icon = { Icon(Icons.Default.Home, contentDescription = "Beranda") },
            label = { Text("Beranda", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiriBlue,
                selectedTextColor = MandiriBlue,
                indicatorColor = MandiriBlue.copy(alpha = 0.12f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            )
        )

        NavigationBarItem(
            selected = currentRoute == "search",
            onClick = onSearchClick,
            icon = { Icon(Icons.Default.Search, contentDescription = "Pencarian") },
            label = { Text("Pencarian", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiriBlue,
                selectedTextColor = MandiriBlue,
                indicatorColor = MandiriBlue.copy(alpha = 0.12f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            )
        )

        NavigationBarItem(
            selected = currentRoute == "saved",
            onClick = onSavedClick,
            icon = { Icon(Icons.Default.Bookmark, contentDescription = "Tersimpan") },
            label = { Text("Tersimpan", fontSize = 10.sp) },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = MandiriBlue,
                selectedTextColor = MandiriBlue,
                indicatorColor = MandiriBlue.copy(alpha = 0.12f),
                unselectedIconColor = Color(0xFF64748B),
                unselectedTextColor = Color(0xFF64748B)
            )
        )
    }
}