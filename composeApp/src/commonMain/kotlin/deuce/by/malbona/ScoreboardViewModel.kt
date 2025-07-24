package deuce.by.malbona

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScoreboardViewModel : ViewModel() {

    private val definedColors = DefinedColor.entries
    private val definedColorsNumber = definedColors.size

    private var matchInitialServe = Team.A
    private var gameInitialServe = Team.A
    var lastScoreUiState: UiState? = null
        private set

    private val _uiState = MutableStateFlow(
        UiState()
    )

    val uiState = _uiState.asStateFlow()

    fun recallLastScore() {
        lastScoreUiState?.run {
            _uiState.update { this }
            lastScoreUiState = null
        }
    }

    fun flipSides() {
        matchInitialServe = when (matchInitialServe) {
            Team.A -> Team.B
            Team.B -> Team.A
        }
        gameInitialServe = when (gameInitialServe) {
            Team.A -> Team.B
            Team.B -> Team.A
        }
        _uiState.update {
            UiState(
                teamA = it.teamB,
                teamB = it.teamA
            )
        }
    }

    fun updateColor(team: Team) {
        when (team) {
            Team.A -> _uiState.update { it.copy(teamA = it.teamA.copy(color = nextDefinedColor(it.teamA.color.ordinal))) }
            Team.B -> _uiState.update { it.copy(teamB = it.teamB.copy(color = nextDefinedColor(it.teamB.color.ordinal))) }
        }
    }

    private fun nextDefinedColor(currentOrdinal: Int): DefinedColor {
        return definedColors[(currentOrdinal + 1) % definedColorsNumber]
    }

    fun rename(team: Team, newName: String) {
        when (team) {
            Team.A -> _uiState.update { it.copy(teamA = it.teamA.copy(name = newName)) }
            Team.B -> _uiState.update { it.copy(teamB = it.teamB.copy(name = newName)) }
        }
    }

    fun score(team: Team) {
        lastScoreUiState = uiState.value
        val (teamA, teamB) = uiState.value
        var (_, _, teamAGames, teamAScore, teamAServes) = teamA
        var (_, _, teamBGames, teamBScore, teamBServes) = teamB
        var shouldFlipSides = false

        when (team) {
            Team.A -> teamAScore++
            Team.B -> teamBScore++
        }
        // Update serve
        if (teamAServes > 0) {
            teamAServes--
            when {
                teamAScore >= 10 && teamBScore >= 10 -> {
                    teamBServes = 1
                }

                teamAServes == 0 -> {
                    teamBServes = 2
                }
            }
        } else {
            teamBServes--
            when {
                teamAScore >= 10 && teamBScore >= 10 -> {
                    teamAServes = 1
                }

                teamBServes == 0 -> {
                    teamAServes = 2
                }
            }
        }

        // Check win
        when {
            teamAScore >= 11 && teamAScore - teamBScore >= 2 -> {
                teamAGames += "W"
                teamBGames += "L"
                teamAScore = 0
                teamBScore = 0
                when (gameInitialServe) {
                    Team.A -> {
                        gameInitialServe = Team.B
                        teamAServes = 0
                        teamBServes = 2
                    }

                    Team.B -> {
                        gameInitialServe = Team.A
                        teamAServes = 2
                        teamBServes = 0
                    }
                }
                shouldFlipSides = true
            }

            teamBScore >= 11 && teamBScore - teamAScore >= 2 -> {
                teamAGames += "L"
                teamBGames += "W"
                teamAScore = 0
                teamBScore = 0
                when (gameInitialServe) {
                    Team.A -> {
                        gameInitialServe = Team.B
                        teamAServes = 0
                        teamBServes = 2
                    }

                    Team.B -> {
                        gameInitialServe = Team.A
                        teamAServes = 2
                        teamBServes = 0
                    }
                }
                shouldFlipSides = true
            }
        }

        _uiState.update {
            it.copy(
                teamA = it.teamA.copy(
                    games = teamAGames,
                    score = teamAScore,
                    serves = teamAServes
                ),
                teamB = it.teamB.copy(
                    games = teamBGames,
                    score = teamBScore,
                    serves = teamBServes
                )
            )
        }

        if (shouldFlipSides) {
            flipSides()
        }
    }

    fun resetCurrentGame() {
        _uiState.update {
            it.copy(
                teamA = it.teamA.copy(
                    score = 0,
                    serves = when (gameInitialServe) {
                        Team.A -> 2
                        Team.B -> 0
                    }
                ),
                teamB = it.teamB.copy(
                    score = 0,
                    serves = when (gameInitialServe) {
                        Team.A -> 0
                        Team.B -> 2
                    }
                )
            )
        }
    }

    fun isNotStarted(): Boolean {
        val (teamA, teamB) = uiState.value
        return teamA.games.isEmpty() && teamB.games.isEmpty() && teamA.score == 0 && teamB.score == 0
    }

    fun flipInitialServe() {
        matchInitialServe = when (matchInitialServe) {
            Team.A -> Team.B
            Team.B -> Team.A
        }
        gameInitialServe = matchInitialServe
        _uiState.update {
            it.copy(
                teamA = it.teamA.copy(
                    serves = when (gameInitialServe) {
                        Team.A -> 2
                        Team.B -> 0
                    }
                ),
                teamB = it.teamB.copy(
                    serves = when (gameInitialServe) {
                        Team.A -> 0
                        Team.B -> 2
                    }
                )
            )
        }
    }
}