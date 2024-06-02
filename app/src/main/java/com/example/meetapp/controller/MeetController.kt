package com.example.meetapp.controller

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel

enum class VIEW_TYPE {
    video,
    chat
}
class MeetViewModel: ViewModel() {
    private val _selectedView = mutableStateOf<VIEW_TYPE>(VIEW_TYPE.video)
    val selectedView get() = _selectedView.value

    fun switchView(){
        if(_selectedView.value == VIEW_TYPE.video){
            _selectedView.value = VIEW_TYPE.chat
        } else {
            _selectedView.value = VIEW_TYPE.video
        }
    }

}