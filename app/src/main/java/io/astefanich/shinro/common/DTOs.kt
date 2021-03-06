package io.astefanich.shinro.common

import android.os.Parcelable
import io.astefanich.shinro.model.ResultAggregate
import kotlinx.android.parcel.Parcelize


data class Cell(val current: String, val actual: String = current)

typealias Grid = Array<Array<Cell>>

enum class Difficulty(val repr: String) {
    EASY("Easy"),
    MEDIUM("Medium"),
    HARD("Hard"),
    ANY("Any")
}

enum class TimeSeconds(val seconds: Long) {
    ONE(1L),
    FIVE(5L),
    TEN(10L),
    THIRTY(30L)
}

data class Statistic(
    val difficulty: Difficulty,
    val numPlayed: Int,
    val winPct: Float,
    val bestTimeSec: Long,
    val avgTimeSec: Long,
)

@Parcelize
data class GameSummary(val difficulty: Difficulty, val isWin: Boolean, val timeTaken: Long) : Parcelable{

    fun toResultAggregate(): ResultAggregate =
        ResultAggregate(
            difficulty = this.difficulty,
            numPlayed = 1,
            numWins = if (isWin) 1 else 0,
            bestTimeSec = timeTaken,
            totalTimeSeconds = timeTaken
        )
}

sealed class PlayRequest : Parcelable {
    @Parcelize
    object Resume : PlayRequest()

    @Parcelize
    data class NewGame(val difficulty: Difficulty) : PlayRequest()
}

@Parcelize
enum class TipChoice : Parcelable { HOWTOPLAY, PATHFINDER, BLOCKER, PIGEONHOLE }

data class Tip(val drawable: Int, val text: String)

data class Freebie(val row: Int, val col: Int)

data class NetworkTimeout(val ms: Long)
