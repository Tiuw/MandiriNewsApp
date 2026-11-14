
package com.rakamin.newsapp.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.rakamin.newsapp.domain.model.News
import com.rakamin.newsapp.ui.detail.DetailScreen
import com.rakamin.newsapp.ui.home.HomeScreen

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
