package deuce.by.malbona

import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.sp
import deuce.composeapp.generated.resources.Res
import deuce.composeapp.generated.resources.flip_initial_serve
import deuce.composeapp.generated.resources.flip_sides
import deuce.composeapp.generated.resources.recall_last_score
import deuce.composeapp.generated.resources.reset_current_game
import org.jetbrains.compose.resources.stringResource

@Composable
fun Scoreboard(viewModel: ScoreboardViewModel) {
    val uiState by viewModel.uiState.collectAsState()
    val (teamA, teamB) = uiState
    val focusManager = LocalFocusManager.current

    Box(contentAlignment = Alignment.Center) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = teamA.name,
                    onValueChange = { viewModel.rename(team = Team.A, newName = it) },
                    textStyle = TextStyle(color = teamA.color.color, fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions(
                        imeAction = if (teamB.name.isEmpty()) ImeAction.Next else ImeAction.Done
                    ),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true
                )
                Text("🏓".repeat(teamA.serves))
                Spacer(modifier = Modifier.weight(1f))
                Text("🏓".repeat(teamB.serves), modifier = Modifier.graphicsLayer(scaleX = -1f))
                TextField(
                    value = teamB.name,
                    onValueChange = { viewModel.rename(team = Team.B, newName = it) },
                    textStyle = TextStyle(color = teamB.color.color, fontSize = 24.sp),
                    keyboardOptions = KeyboardOptions(imeAction = ImeAction.Done),
                    keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                    singleLine = true
                )
            }
            Row {
                Text(teamA.games)
                Spacer(Modifier.weight(1f))
                Text(teamB.games)
            }
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            viewModel.updateColor(Team.A)
                        }) {
                        viewModel.score(team = Team.A)
                    }.background(color = teamA.color.color).weight(1f).fillMaxHeight()
                ) {
                    Text(
                        teamA.score.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 64.sp
                    )
                }
                Box(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            viewModel.updateColor(Team.B)
                        }
                    ) {
                        viewModel.score(team = Team.B)
                    }.background(color = teamB.color.color).weight(1f).fillMaxHeight()
                ) {
                    Text(
                        teamB.score.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 64.sp
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                { viewModel.resetCurrentGame() }
            ) {
                Text(stringResource(Res.string.reset_current_game))
            }
            if (viewModel.isNotStarted()) {
                Button(
                    { viewModel.flipInitialServe() }
                ) {
                    Text(stringResource(Res.string.flip_initial_serve))
                }
            }
            Button(
                { viewModel.recallLastScore() },
                enabled = viewModel.lastScoreUiState != null
            ) {
                Text(stringResource(Res.string.recall_last_score))
            }
            Button(
                { viewModel.flipSides() }
            ) {
                Text(stringResource(Res.string.flip_sides))
            }
        }
    }
}