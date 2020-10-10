package com.danielecampogiani.gltfdemo.androidApp

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.danielecampogiani.gltfdemo.shared.Api
import com.danielecampogiani.gltfdemo.shared.FetchModelsUseCase
import com.danielecampogiani.gltfdemo.shared.Model
import kotlinx.coroutines.launch


class MainViewModel : ViewModel() {

    private val useCase =
        FetchModelsUseCase(Api.Implementation) //In a real application this must be injected

    private val _state = MutableLiveData<List<Model>>()

    val state: LiveData<List<Model>>
        get() = _state

    init {
        loadData()
    }

    private fun loadData() {
        viewModelScope.launch {
            _state.value = useCase()
        }
    }
}