package fr.outadoc.teabot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.outadoc.teabot.AppConstants
import fr.outadoc.teabot.data.db.DbSource
import fr.outadoc.teabot.domain.ChatSource
import fr.outadoc.teabot.domain.model.User
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flowOn
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

class MainViewModel(
    private val chatSource: ChatSource,
    private val dbSource: DbSource,
) : ViewModel() {
    data class State(
        val users: ImmutableList<User> = persistentListOf(),
    )

    val state: StateFlow<State> =
        dbSource
            .getAll()
            .map { users -> State(users = users) }
            .flowOn(Dispatchers.Unconfined)
            .stateIn(
                viewModelScope,
                started = SharingStarted.WhileSubscribed(),
                initialValue = State(),
            )

    fun onStart() {
        viewModelScope.launch {
            chatSource
                .getMessages(AppConstants.CHANNEL_USERNAME)
                .collect { message ->
                    println(message)
                    dbSource.saveMessage(message)
                }
        }
    }
}
