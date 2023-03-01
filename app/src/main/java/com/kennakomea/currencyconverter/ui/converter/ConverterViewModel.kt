package com.kennakomea.currencyconverter.ui.converter

import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

class ConverterViewModel : ViewModel() {

    private val _text = MutableLiveData<String>().apply {
        value = "This is Converter Fragment"
    }
    val text: LiveData<String> = _text

}