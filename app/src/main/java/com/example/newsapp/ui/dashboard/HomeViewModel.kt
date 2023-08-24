package com.example.newsapp.ui.dashboard


import android.app.Application
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.api.Resource
import com.example.newsapp.constants.Constants
import com.example.newsapp.core.BaseViewModel
import com.example.newsapp.db.repository.EventRepository
import com.example.newsapp.ui.dashboard.model.CategoryData
import com.example.newsapp.ui.savednews.events.EventDetails
import com.example.newsapp.utility.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import javax.inject.Inject

/** Created by Tejas Dani on 17-Feb-2023
 * for view model of HomeFragment
 * */


@Suppress("SpellCheckingInspection")
@HiltViewModel
class HomeViewModel @Inject constructor(
    val app: Application,
    private val repo: HomeRepo,
    private val repoDatabase: EventRepository
) :
    BaseViewModel(app) {

    private val TAG = "HomeViewModel"
    val responseList = MutableLiveData<ArrayList<CategoryData>>()
    private var searchJob: Job? = null

    @Inject
    lateinit var preferenceStore: PreferenceStore

    /* init {
         val dao = EventDatabase.getDatabase(app).getEventDao()
         repository = EventRepository(dao)
     }*/

    private fun homeCallAPI(params: String) {
        viewModelScope.launch {
            validateNetwork {
                setIsLoading(true)
                val dashboardResponse = repo.callDashboardApi(params)
                val dashboardResponseDeferred = async {
                    println("Inside dashBoard:: $this")

                    repo.callDashboardApi(params)

                    // delay(5000)
                    // println("API request for $this took ${time}ms")
                    /*val time = measureTimeMillis {
                        delay(5000)
                        println("API request for $this took ${time}ms")
                        repo.callDashboardApi(params)

                    }*/
                }

                val searchResponseDeferred = async {
                    println("Inside searchResponseDeferred:: $this")

                    // delay(10000)
                    repo.searchNewsApi(params)
                }
                /*  val time = measureTimeMillis {
                      delay(10000)
                      println("API request for $this took ${time}ms")
                      repo.searchNewsApi(params)
                  }*/
                val list = listOf(dashboardResponseDeferred, searchResponseDeferred).awaitAll()
                list.forEach {
                    println("Response::$it")

                    /* if (it is Resource.Success) {
                         Timber.d("Dashboard Response:: ${it.data}")
                         responseList.postValue(it.data!!)
                     } else {
                         Timber.d("Dashboard Response:: ${it}")
                     }*/
                }
                setIsLoading(false)
            }
        }
    }

    fun getNewsBySearch(search: String) {
        searchJob = viewModelScope.launch {
            setIsLoading(true)
            /*  val dashboardResponse = repo.searchNewsApi(search).isRequestCallSuspendSuccess(success = {dashboardResponse ->
                  dashboardResponse?.let { _loginResponse.emit(it) }
                 // categoryList.postValue(dashboardResponse.)
              },
              failure = {dashboardResponse, errorType ->
                  println(TAG+"Search Response::" +errorType)
              })*/
            //cancel job
            delay(5000)
            searchJob?.cancel()
            val dashboardResponse = repo.searchNewsApi(search)
            if (dashboardResponse is Resource.Success) {
                println(TAG + "Search Response:: " + dashboardResponse.data)
                responseList.postValue(dashboardResponse.data!!.result)
            } else if (dashboardResponse is Resource.Error) {
                println(TAG + "Search Response:: " + dashboardResponse.message)
            }
            setIsLoading(false)
        }
    }

    fun getStoreChipList(chipList: (List<String>) -> Unit) {
        viewModelScope.launch {
            val selectedList = preferenceStore.getSetStringList(Constants.CATEGORY_PREF)
            selectedList?.let { chipList(it.toList()) }

        }
    }

    /*For Storing Username*/
    fun getUserName(res: (String) -> Unit) {
        viewModelScope.launch {
            res
            preferenceStore.getString(Constants.USER_NAME) {
                it?.let { name -> res(name) }
            }
        }
    }


    fun insertEvent(event: EventDetails) {
        viewModelScope.launch(Dispatchers.IO) { repoDatabase.insertEvent(event) }
    }

    fun getNewsApi(chip: String) {
        homeCallAPI(chip)
    }

}
