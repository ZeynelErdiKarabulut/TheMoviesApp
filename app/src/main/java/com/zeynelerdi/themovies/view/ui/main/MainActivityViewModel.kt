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

package com.zeynelerdi.themovies.view.ui.main

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.switchMap
import com.zeynelerdi.themovies.models.Resource
import com.zeynelerdi.themovies.models.entity.Movie
import com.zeynelerdi.themovies.models.entity.Person
import com.zeynelerdi.themovies.models.entity.Tv
import com.zeynelerdi.themovies.repository.DiscoverRepository
import com.zeynelerdi.themovies.repository.PeopleRepository
import com.zeynelerdi.themovies.utils.AbsentLiveData
import javax.inject.Inject
import timber.log.Timber

class MainActivityViewModel @Inject constructor(
  private val discoverRepository: DiscoverRepository,
  private val peopleRepository: PeopleRepository
) : ViewModel() {

  private var moviePageLiveData: MutableLiveData<Int> = MutableLiveData()
  val movieListLiveData: LiveData<Resource<List<Movie>>>

  private var tvPageLiveData: MutableLiveData<Int> = MutableLiveData()
  val tvListLiveData: LiveData<Resource<List<Tv>>>

  private var peoplePageLiveData: MutableLiveData<Int> = MutableLiveData()
  val peopleLiveData: LiveData<Resource<List<Person>>>

  init {
    Timber.d("injection MainActivityViewModel")

    movieListLiveData = moviePageLiveData.switchMap {
      moviePageLiveData.value?.let { discoverRepository.loadMovies(it) }
        ?: AbsentLiveData.create()
    }

    tvListLiveData = tvPageLiveData.switchMap {
      tvPageLiveData.value?.let { discoverRepository.loadTvs(it) } ?: AbsentLiveData.create()
    }

    peopleLiveData = peoplePageLiveData.switchMap {
      peoplePageLiveData.value?.let { peopleRepository.loadPeople(it) }
        ?: AbsentLiveData.create()
    }
  }

  fun getMovieListValues() = movieListLiveData.value
  fun postMoviePage(page: Int) = moviePageLiveData.postValue(page)

  fun getTvListValues() = tvListLiveData.value
  fun postTvPage(page: Int) = tvPageLiveData.postValue(page)

  fun getPeopleValues() = peopleLiveData.value
  fun postPeoplePage(page: Int) = peoplePageLiveData.postValue(page)
}
