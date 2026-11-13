package com.rakamin.newsapp

import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import com.rakamin.newsapp.ui.home.HomeScreen

@Composable
fun NewsApp() {
    Surface(
        color = MaterialTheme.colorScheme.background
    ) {
        HomeScreen()
    }
}
