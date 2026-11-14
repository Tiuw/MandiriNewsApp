
package com.rakamin.newsapp.data.remote.paging

import androidx.paging.PagingSource
import androidx.paging.PagingState
import com.rakamin.newsapp.data.remote.api.NewsApiService
import com.rakamin.newsapp.data.remote.response.ArticleResponse
import kotlinx.coroutines.CancellationException

class NewsPagingSource(
    private val newsApiService: NewsApiService
) : PagingSource<Int, ArticleResponse>() {

    override fun getRefreshKey(
        state: PagingState<Int, ArticleResponse>
    ): Int? {
        return state.anchorPosition?.let { position ->
            state.closestPageToPosition(position)?.prevKey?.plus(1)
                ?: state.closestPageToPosition(position)?.nextKey?.minus(1)
        }
    }

    override suspend fun load(
        params: LoadParams<Int>
    ): LoadResult<Int, ArticleResponse> {
        return try {
            val page = params.key ?: 1
            val pageSize = params.loadSize

            val response = newsApiService.getTopHeadlines(
                country = "us",
                page = page,
                pageSize = pageSize,
                apiKey = com.rakamin.newsapp.BuildConfig.NEWS_API_KEY
            )

            LoadResult.Page(
                data = response.articles,
                prevKey = if (page == 1) null else page - 1,
                nextKey = if (
                    response.articles.isEmpty() ||
                    page * pageSize >= response.totalResults
                ) null else page + 1
            )
        } catch (exception: CancellationException) {
            throw exception
        } catch (exception: Exception) {
            LoadResult.Error(exception)
        }
    }
}
