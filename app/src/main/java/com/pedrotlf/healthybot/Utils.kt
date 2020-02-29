package com.pedrotlf.healthybot

import java.util.*

object Utils {
    fun containsWordFromList(string: String, words: List<String>): Boolean{
        for (word in words){
            if (string.toLowerCase(Locale.getDefault()).contains(word.toLowerCase(Locale.getDefault())))
                return true
        }

        return false
    }
}