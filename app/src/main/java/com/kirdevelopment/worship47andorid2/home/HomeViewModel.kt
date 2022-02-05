package com.kirdevelopment.worship47andorid2.home

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.kirdevelopment.worship47andorid2.models.Result

class HomeViewModel: ViewModel() {
    val songsList: MutableLiveData<List<Result>> = MutableLiveData()
}