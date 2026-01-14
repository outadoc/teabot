package fr.outadoc.teabot.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyListState
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.ExperimentalComposeUiApi
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.input.pointer.PointerEventType
import androidx.compose.ui.input.pointer.onPointerEvent
import androidx.compose.ui.unit.dp
import fr.outadoc.teabot.generated.Res
import fr.outadoc.teabot.generated.panties
import io.github.vinceglb.confettikit.compose.ConfettiKit
import io.github.vinceglb.confettikit.core.Party
import io.github.vinceglb.confettikit.core.Position
import io.github.vinceglb.confettikit.core.Rotation
import io.github.vinceglb.confettikit.core.emitter.Emitter
import io.github.vinceglb.confettikit.core.models.Shape
import io.github.vinceglb.confettikit.core.models.Size
import org.jetbrains.compose.resources.imageResource
import kotlin.time.Duration.Companion.milliseconds

@OptIn(ExperimentalComposeUiApi::class)
@Composable
fun MainScreen(
    state: MainViewModel.State,
    modifier: Modifier = Modifier,
    scrollState: LazyListState = rememberLazyListState(),
    onSelect: (teaId: String) -> Unit = {},
    onArchivedChange: (teaId: String, Boolean) -> Unit = { _, _ -> },
    onQueryChange: (String) -> Unit = {},
    onHelpVisibilityChange: (Boolean) -> Unit = {},
) {
    var pointerPosition: Offset? by remember { mutableStateOf(null) }
    var isShowingConfetti by remember { mutableStateOf(false) }

    Box(
        modifier =
            modifier.onPointerEvent(PointerEventType.Move) {
                pointerPosition = it.changes.first().position
            },
    ) {
        Scaffold {
            if (state.isHelpDisplayed) {
                HelpDialog(
                    onDismissRequest = { onHelpVisibilityChange(false) },
                )
            }

            Row {
                TeaList(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .width(300.dp),
                    scrollState = scrollState,
                    teaList = state.teaList,
                    selectedTea = state.selectedTea,
                    query = state.query,
                    onSelect = onSelect,
                    onArchivedChange = { teaId, isArchived ->
                        if (isArchived) {
                            isShowingConfetti = true
                        }
                        onArchivedChange(teaId, isArchived)
                    },
                    onQueryChange = onQueryChange,
                    onHelpVisibilityChange = onHelpVisibilityChange,
                )

                Column(
                    modifier =
                        Modifier
                            .fillMaxHeight()
                            .weight(1f, fill = true)
                            .background(MaterialTheme.colorScheme.surfaceContainerLow),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    horizontalAlignment = Alignment.CenterHorizontally,
                ) {
                    val tea = state.selectedTea
                    if (tea != null) {
                        TeaDetails(
                            tea = tea,
                        )
                    } else {
                        EmptyDetails(
                            modifier = Modifier.fillMaxSize(),
                        )
                    }
                }
            }
        }

        pointerPosition?.let { position ->
            val party =
                Party(
                    speed = 0f,
                    maxSpeed = 30f,
                    spread = 360,
                    timeToLive = 5000,
                    position =
                        Position.Absolute(
                            x = position.x,
                            y = position.y,
                        ),
                    size =
                        listOf(
                            Size(
                                sizeInDp = 24,
                                mass = 4f,
                            ),
                        ),
                    emitter = Emitter(duration = 50.milliseconds).max(20),
                    rotation = Rotation(enabled = false),
                    shapes =
                        listOf(
                            Shape.Image(imageResource(Res.drawable.panties)),
                        ),
                )

            if (isShowingConfetti) {
                ConfettiKit(
                    modifier = Modifier.fillMaxSize(),
                    parties = listOf(party),
                    onParticleSystemEnded = { _, _ -> isShowingConfetti = false },
                )
            }
        }
    }
}
