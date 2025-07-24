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
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.sp

@Composable
fun Scoreboard(viewModel: ScoreboardViewModel) {
    val definedColors = DefinedColor.entries
    val definedColorsNumber = definedColors.size
    var teamAName by remember { mutableStateOf("Player 1") }
    var teamBName by remember { mutableStateOf("Player 2") }
    var teamAColor by remember { mutableStateOf(DefinedColor.`Blue🇺🇦`) }
    var teamBColor by remember { mutableStateOf(DefinedColor.`Yellow🇺🇦`) }
    val uiState by viewModel.uiState.collectAsState()
    val (teamAGames, teamBGames, teamAScore, teamBScore, teamAServes, teamBServes) = uiState
    Box(contentAlignment = Alignment.Center) {
        Column {
            Row(modifier = Modifier.fillMaxWidth()) {
                TextField(
                    value = teamAName,
                    onValueChange = { teamAName = it },
                    textStyle = TextStyle(color = teamAColor.color, fontSize = 24.sp),
                    singleLine = true
                )
                Text("🏓".repeat(teamAServes))
                Spacer(modifier = Modifier.weight(1f))
                Text("🏓".repeat(teamBServes), modifier = Modifier.graphicsLayer(scaleX = -1f))
                TextField(
                    value = teamBName,
                    onValueChange = { teamBName = it },
                    textStyle = TextStyle(color = teamBColor.color, fontSize = 24.sp),
                    singleLine = true
                )
            }
            Row {
                Text(teamAGames)
                Spacer(Modifier.weight(1f))
                Text(teamBGames)
            }
            Row(modifier = Modifier.fillMaxSize()) {
                Box(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            teamAColor =
                                definedColors[(teamAColor.ordinal + 1) % definedColorsNumber]
                        }
                    ) {
                        viewModel.updateStates(scoredTeam = Team.A)
                    }.background(color = teamAColor.color).weight(1f).fillMaxHeight()
                ) {
                    Text(
                        teamAScore.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 64.sp
                    )
                }
                Box(
                    modifier = Modifier.combinedClickable(
                        onLongClick = {
                            teamBColor =
                                definedColors[(teamBColor.ordinal + 1) % definedColorsNumber]
                        }
                    ) {
                        viewModel.updateStates(scoredTeam = Team.B)
                    }.background(color = teamBColor.color).weight(1f).fillMaxHeight()
                ) {
                    Text(
                        teamBScore.toString(),
                        modifier = Modifier.align(Alignment.Center),
                        fontSize = 64.sp
                    )
                }
            }
        }
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            Button(
                {
                    viewModel.resetCurrentGame()
                }
            ) {
                Text("Reset current game")
            }
            if (viewModel.isNotStarted()) {
                Button(
                    {
                        viewModel.flipInitialServe()
                    }
                ) {
                    Text("Flip initial serve")
                }
            }
            Button(
                {
                    viewModel.recallLastScore()
                },
                enabled = viewModel.lastScoreUiState != null
            ) {
                Text("Recall last score")
            }
        }
    }
}