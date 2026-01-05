package fr.outadoc.teabot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.outadoc.teabot.AppConstants
import fr.outadoc.teabot.domain.ChatSource
import kotlinx.coroutines.launch

class MainViewModel(private val source: ChatSource) : ViewModel() {

    fun onStart() {
        viewModelScope.launch {
            source.getMessages(AppConstants.CHANNEL_USERNAME)
                .collect { message ->
                    println(message)
                }
        }
    }
}
