package deuce.by.malbona

data class UiState(
    val teamA: TeamUiState = TeamUiState(name = "Player 1", color = DefinedColor.`Blue🇺🇦`, serves = 2),
    val teamB: TeamUiState = TeamUiState(name = "Player 2", color = DefinedColor.`Yellow🇺🇦`, serves = 0)
)

data class TeamUiState(
    val color: DefinedColor,
    val name: String,
    val games: String = "",
    val score: Int = 0,
    val serves: Int
)
