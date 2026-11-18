package com.rakamin.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.bookmark.BookmarkScreen
import com.rakamin.newsapp.ui.detail.DetailScreen
import com.rakamin.newsapp.ui.home.HomeScreen
import com.rakamin.newsapp.ui.search.SearchScreen

@Composable
fun AppNavigation() {
    val navController = rememberNavController()

    NavHost(
        navController = navController,
        startDestination = "home"
    ) {
        composable("home") {
            HomeScreen(
                onNewsClick = { news ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_news", news)

                    navController.navigate("detail")
                },
                onSearchClick = {
                    navController.navigate("search")
                },
                onSavedNewsClick = {
                    navController.navigate("saved")
                }
            )
        }

        composable("search") {
            SearchScreen(
                onNewsClick = { news ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_news", news)

                    navController.navigate("detail")
                },
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSavedClick = {
                    navController.navigate("saved")
                }
            )
        }

        composable("saved") {
            BookmarkScreen(
                onNewsClick = { news ->
                    navController.currentBackStackEntry
                        ?.savedStateHandle
                        ?.set("selected_news", news)

                    navController.navigate("detail")
                },
                onBackClick = {
                    navController.popBackStack()
                },
                onHomeClick = {
                    navController.navigate("home") {
                        popUpTo("home") { inclusive = true }
                    }
                },
                onSearchClick = {
                    navController.navigate("search")
                }
            )
        }

        composable("detail") {
            val news = navController.previousBackStackEntry
                ?.savedStateHandle
                ?.get<News>("selected_news")

            if (news != null) {
                DetailScreen(
                    news = news,
                    onBackClick = {
                        navController.popBackStack()
                    }
                )
            }
        }
    }
}
