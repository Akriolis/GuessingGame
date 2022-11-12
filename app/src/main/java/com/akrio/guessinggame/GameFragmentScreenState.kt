package com.akrio.guessinggame

data class GameFragmentScreenState(var secretWordDisplay: String, var incorrectGuesses: String ="", var livesLeft: Int = 8) {

}