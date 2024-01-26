package com.example.viewpager2withexoplayer

import com.google.android.exoplayer2.ExoPlayer

object VideoPlayerManager {
    val maxNum=3
    val exoplayerList= ArrayList<ExoPlayerItem>()

    fun getExistPlayer(position: Int): ExoPlayer? {
        return exoplayerList.find { it.position == position }?.exoPlayer
    }

    fun addPlayer(exoPlayer: ExoPlayer,position: Int) {
         exoplayerList.add(ExoPlayerItem(exoPlayer,position))
    }


    fun releaseAllPlayer() {
        exoplayerList.forEach {
            it.exoPlayer?.release()
            it.exoPlayer = null
        }
        exoplayerList.clear()
    }

}


