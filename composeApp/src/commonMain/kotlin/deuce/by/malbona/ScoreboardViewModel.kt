package deuce.by.malbona

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ScoreboardViewModel : ViewModel() {

    private var matchInitialServe = Team.A
    private var gameInitialServe = Team.A
    var lastScoreUiState: UiState? = null
        private set

    private val _uiState = MutableStateFlow(
        UiState(
            teamAGames = "",
            teamBGames = "",
            teamAScore = 0,
            teamBScore = 0,
            teamAServes = 2,
            teamBServes = 0
        )
    )

    val uiState = _uiState.asStateFlow()

    fun recallLastScore() {
        lastScoreUiState?.run {
            _uiState.update { this }
            lastScoreUiState = null
        }
    }

    fun updateStates(scoredTeam: Team) {
        lastScoreUiState = uiState.value
        var (teamAGames, teamBGames, teamAScore, teamBScore, teamAServes, teamBServes) = uiState.value

        when (scoredTeam) {
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
            }
        }

        _uiState.update {
            it.copy(
                teamAGames = teamAGames,
                teamBGames = teamBGames,
                teamAScore = teamAScore,
                teamBScore = teamBScore,
                teamAServes = teamAServes,
                teamBServes = teamBServes
            )
        }
    }

    fun resetCurrentGame() {
        _uiState.update {
            it.copy(
                teamAScore = 0,
                teamBScore = 0,
                teamAServes = when (gameInitialServe) {
                    Team.A -> 2
                    Team.B -> 0
                },
                teamBServes = when (gameInitialServe) {
                    Team.A -> 0
                    Team.B -> 2
                }
            )
        }
    }

    fun isNotStarted(): Boolean {
        val (teamAGames, teamBGames, teamAScore, teamBScore) = uiState.value
        return teamAGames.isEmpty() && teamBGames.isEmpty() && teamAScore == 0 && teamBScore == 0
    }

    fun flipInitialServe() {
        matchInitialServe = when (matchInitialServe) {
            Team.A -> Team.B
            Team.B -> Team.A
        }
        gameInitialServe = matchInitialServe
        _uiState.update {
            it.copy(
                teamAServes = when (gameInitialServe) {
                    Team.A -> 2
                    Team.B -> 0
                },
                teamBServes = when (gameInitialServe) {
                    Team.A -> 0
                    Team.B -> 2
                }
            )
        }
    }
}