/*
 * The MIT License (MIT)
 *
 * Designed and developed by 2020 Zeynel Erdi Karabulut
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN
 * THE SOFTWARE.
 */

package com.zeynelerdi.themovies.repository

import androidx.arch.core.executor.testing.InstantTaskExecutorRule
import androidx.lifecycle.Observer
import com.nhaarman.mockitokotlin2.mock
import com.nhaarman.mockitokotlin2.verify
import com.nhaarman.mockitokotlin2.verifyNoMoreInteractions
import com.nhaarman.mockitokotlin2.whenever
import com.zeynelerdi.themovies.api.ApiUtil.successCall
import com.zeynelerdi.themovies.api.MovieService
import com.zeynelerdi.themovies.models.Keyword
import com.zeynelerdi.themovies.models.Resource
import com.zeynelerdi.themovies.models.Review
import com.zeynelerdi.themovies.models.Video
import com.zeynelerdi.themovies.models.network.KeywordListResponse
import com.zeynelerdi.themovies.models.network.ReviewListResponse
import com.zeynelerdi.themovies.models.network.VideoListResponse
import com.zeynelerdi.themovies.room.MovieDao
import com.zeynelerdi.themovies.utils.MockTestUtil.Companion.mockKeywordList
import com.zeynelerdi.themovies.utils.MockTestUtil.Companion.mockMovie
import com.zeynelerdi.themovies.utils.MockTestUtil.Companion.mockReviewList
import com.zeynelerdi.themovies.utils.MockTestUtil.Companion.mockVideoList
import org.junit.Before
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith
import org.junit.runners.JUnit4

@RunWith(JUnit4::class)
class MovieRepositoryTest {
  private lateinit var repository: MovieRepository
  private val movieDao = mock<MovieDao>()
  private val service = mock<MovieService>()

  @Rule
  @JvmField
  val instantExecutorRule = InstantTaskExecutorRule()

  @Before
  fun init() {
    repository = MovieRepository(service, movieDao)
  }

  @Test
  fun loadKeywordListFromNetwork() {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

    val mockResponse = KeywordListResponse(123, mockKeywordList())
    val call = successCall(mockResponse)
    whenever(service.fetchKeywords(123)).thenReturn(call)

    val data = repository.loadKeywordList(123)
    verify(movieDao).getMovie(123)
    verifyNoMoreInteractions(service)

    val observer = mock<Observer<Resource<List<Keyword>>>>()
    data.observeForever(observer)
    verify(observer).onChanged(Resource.success(mockKeywordList()))

    val updatedMovie = mockMovie()
    updatedMovie.keywords = mockKeywordList()
    verify(movieDao).updateMovie(updatedMovie)
  }

  @Test
  fun loadVideoListFromNetwork() {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

    val mockResponse = VideoListResponse(123, mockVideoList())
    val call = successCall(mockResponse)
    whenever(service.fetchVideos(123)).thenReturn(call)

    val data = repository.loadVideoList(123)
    verify(movieDao).getMovie(123)
    verifyNoMoreInteractions(service)

    val observer = mock<Observer<Resource<List<Video>>>>()
    data.observeForever(observer)
    verify(observer).onChanged(Resource.success(mockVideoList()))

    val updatedMovie = mockMovie()
    updatedMovie.videos = mockVideoList()
    verify(movieDao).updateMovie(updatedMovie)
  }

  @Test
  fun loadReviewListFromNetwork() {
    val loadFromDB = mockMovie()
    whenever(movieDao.getMovie(123)).thenReturn(loadFromDB)

    val mockResponse = ReviewListResponse(123, 1, mockReviewList(), 100, 100)
    val call = successCall(mockResponse)
    whenever(service.fetchReviews(123)).thenReturn(call)

    val data = repository.loadReviewsList(123)
    verify(movieDao).getMovie(123)
    verifyNoMoreInteractions(service)

    val observer = mock<Observer<Resource<List<Review>>>>()
    data.observeForever(observer)
    verify(observer).onChanged(Resource.success(mockReviewList()))

    val updatedMovie = mockMovie()
    updatedMovie.reviews = mockReviewList()
    verify(movieDao).updateMovie(updatedMovie)
  }
}
