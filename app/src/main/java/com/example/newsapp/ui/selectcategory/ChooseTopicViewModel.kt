package com.example.newsapp.ui.selectcategory

import android.app.Application
import androidx.core.content.ContextCompat
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.viewModelScope
import com.example.newsapp.R
import com.example.newsapp.selectnews.model.ChooseTopics
import com.example.newsapp.constants.Constants
import com.example.newsapp.core.BaseViewModel
import com.example.newsapp.utility.PreferenceStore
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import timber.log.Timber
import javax.inject.Inject

@HiltViewModel
class ChooseTopicViewModel @Inject constructor(val app: Application) : BaseViewModel(app) {

    var newList = arrayListOf<ChooseTopics>()


    @Inject
    lateinit var prefStore: PreferenceStore

    fun addTopics(): ArrayList<ChooseTopics> {
        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.world),
                ContextCompat.getDrawable(app, R.drawable.ic_world), true
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.animal),
                ContextCompat.getDrawable(app, R.drawable.ic_animal), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.education),
                ContextCompat.getDrawable(app, R.drawable.ic_education), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.sport),
                ContextCompat.getDrawable(app, R.drawable.ic_sports), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.games),
                ContextCompat.getDrawable(app, R.drawable.ic_games), true
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.plant),
                ContextCompat.getDrawable(app, R.drawable.ic_plant), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.vacation),
                ContextCompat.getDrawable(app, R.drawable.ic_vacation), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.fashion),
                ContextCompat.getDrawable(app, R.drawable.ic_fashion), false
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.food),
                ContextCompat.getDrawable(app, R.drawable.ic_food), false
            )
        )
        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.film),
                ContextCompat.getDrawable(app, R.drawable.ic_film), false
            )
        )
        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.tech),
                ContextCompat.getDrawable(app, R.drawable.ic_laptop), true
            )
        )

        newList.add(
            ChooseTopics(
                app.resources.getString(R.string.music),
                ContextCompat.getDrawable(app, R.drawable.ic_music), false
            )
        )
        return newList
    }

    fun isEnableButton(): Boolean {
        var isEnable = false
        val topicsList = newList.filter { s -> s.isSelected }
        isEnable = topicsList.size >= 3
        Timber.d("Size:: $isEnable")

        return isEnable
    }

    /*For storing values in Data Store*/
    fun setChipsPref() {
        var topicsList = listOf<String>()
        viewModelScope.launch {
            for (item in newList) {
                if (newList.size > 0) {
                    topicsList = (newList.filter { s -> s.isSelected }.map { item ->
                        item.topics.toString()
                    })
                }
            }
            prefStore.setSetStringList(Constants.CATEGORY_PREF, topicsList.toSet())
        }
    }

    /*For retrieving values in Data Store*/
    fun getChipsName(chipList: (List<String>) -> Unit) {
        viewModelScope.launch {
            val selectedList = prefStore.getSetStringList(Constants.CATEGORY_PREF)
            selectedList?.let { chipList(it.toList()) }
        }
    }

    /*For Storing Username*/

    fun setUserName(uName: String) {
        viewModelScope.launch {
            prefStore.saveString(Constants.USER_NAME, uName)
        }
    }

}