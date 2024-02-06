package com.example.viewpager2withexoplayer

import android.content.Context
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer

object VideoPlayerManager {
    //找下标有两种方式，直上直下滑的可以去除【0】位；可跳页的去除最远端的；


    val maxNum = 6
    val exoplayerList = ArrayList<ExoPlayerItem>()
    fun getPlayerInstance(position: Int, context: Context): ExoPlayer {
        return if (getExistPlayer(position) == null) {
            val exoPlayer = ExoPlayer.Builder(context).build()
            addPlayer(exoPlayer, position)
            exoPlayer
        } else {
            getExistPlayer(position)!!
        }
    }

    private fun getExistPlayer(position: Int): ExoPlayer? {
        return exoplayerList.find { it.dexVideo == position }?.exoPlayer
    }

    private fun addPlayer(exoPlayer: ExoPlayer, position: Int) {
        exoplayerList.let {
            if (it.size>= maxNum){
                it[0].exoPlayer?.release()
                it[0].exoPlayer = null
                it.removeAt(0)
            }

            it.add(ExoPlayerItem(exoPlayer, position))
        }
    }


    fun releaseAllPlayer() {
        exoplayerList.forEach {
            it.exoPlayer?.release()
            it.exoPlayer = null
        }
        exoplayerList.clear()
    }

}


