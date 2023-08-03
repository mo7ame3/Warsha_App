package com.example.warshaapp.screens.sharedScreens.setting

import androidx.lifecycle.ViewModel
import com.example.warshaapp.repository.SharedRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingScreenViewModel @Inject constructor(private val sharedRepository: SharedRepository) :
    ViewModel()