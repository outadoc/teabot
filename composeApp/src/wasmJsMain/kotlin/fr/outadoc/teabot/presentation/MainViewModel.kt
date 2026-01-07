package fr.outadoc.teabot.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import fr.outadoc.teabot.AppConstants
import fr.outadoc.teabot.data.db.DbSource
import fr.outadoc.teabot.domain.ChatSource
import fr.outadoc.teabot.presentation.model.UiMessage
import fr.outadoc.teabot.presentation.model.UiTea
import fr.outadoc.teabot.presentation.model.UiUser
import kotlinx.collections.immutable.ImmutableList
import kotlinx.collections.immutable.persistentListOf
import kotlinx.collections.immutable.toPersistentList
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlin.time.Duration.Companion.seconds

class MainViewModel(
    private val chatSource: ChatSource,
    private val dbSource: DbSource,
) : ViewModel() {
    data class State(
        val selectedTea: UiTea? = null,
        val teaList: ImmutableList<UiTea> = persistentListOf(),
    )

    private val selectedTeaFlow = MutableStateFlow<UiTea?>(null)

    val state: StateFlow<State> =
        combine(
            dbSource.getAll(),
            selectedTeaFlow,
        ) { teaList, selectedTea ->
            State(
                selectedTea = selectedTea,
                teaList =
                    teaList
                        .map { tea ->
                            UiTea(
                                teaId = tea.teaId,
                                sentAt = tea.sentAt,
                                isArchived = tea.isArchived,
                                user =
                                    UiUser(
                                        userId = tea.userId,
                                        userName = tea.userName,
                                    ),
                                messages =
                                    tea.messages
                                        .map { message ->
                                            UiMessage(
                                                messageId = message.messageId,
                                                sentAt = message.sentAt,
                                                text = message.text,
                                            )
                                        }.toPersistentList(),
                            )
                        }.toPersistentList(),
            )
        }.stateIn(
            viewModelScope,
            started = SharingStarted.WhileSubscribed(),
            initialValue = State(),
        )

    fun onStart() {
        viewModelScope.launch {
            while (isActive) {
                try {
                    chatSource
                        .getMessages(AppConstants.CHANNEL_USERNAME)
                        .collect { message ->
                            println(message)
                            dbSource.saveMessage(message)
                        }
                } catch (e: Exception) {
                    // Auto-reconnect on exception
                    e.printStackTrace()
                    delay(2.seconds)
                }
            }
        }
    }

    fun onSelect(tea: UiTea) {
        viewModelScope.launch {
            selectedTeaFlow.emit(tea)
        }
    }

    fun onArchivedChange(
        tea: UiTea,
        isArchived: Boolean,
    ) {
        viewModelScope.launch {
            dbSource.setTeaArchived(tea.teaId, isArchived)
        }
    }
}
