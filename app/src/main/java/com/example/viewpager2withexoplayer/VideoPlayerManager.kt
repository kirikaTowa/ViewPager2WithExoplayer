package com.example.viewpager2withexoplayer

import android.content.Context
import android.net.Uri
import android.util.Log
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.MediaItem
import com.google.android.exoplayer2.Player
import com.google.android.exoplayer2.source.MediaSource
import com.google.android.exoplayer2.source.ProgressiveMediaSource
import com.google.android.exoplayer2.upstream.DefaultDataSource
import kotlin.math.abs


object VideoPlayerManager {
    //找下标有两种方式，直上直下滑的可以去除【0】位；可跳页的去除最远端的；


    val maxNum = 3
    private val listExoplayer = ArrayList<ExoPlayerItem>()
    fun getPlayerInstance(context: Context,position: Int): ExoPlayer {



        return if (getExistPlayer(position) == null) {
            val exoPlayer = ExoPlayer.Builder(context).build()
            addPlayer(exoPlayer, position)
            exoPlayer
        } else {
            getExistPlayer(position)!!
        }
    }

    fun configPlayer(context: Context,exoPlayer:ExoPlayer,url:String) {
        val dataSourceFactory = DefaultDataSource.Factory(context)
        val mediaSource: MediaSource=ProgressiveMediaSource.Factory(dataSourceFactory)
            .createMediaSource(MediaItem.fromUri(Uri.parse(url)))


        exoPlayer.repeatMode = Player.REPEAT_MODE_ONE
        exoPlayer.setMediaSource(mediaSource)
        exoPlayer.prepare()

    }

    fun getExistPlayer(position: Int): ExoPlayer? {
        return listExoplayer.find { it.dexVideo == position }?.exoPlayer
    }

    fun isExistPlayer(position: Int): Boolean {
        return listExoplayer.find { it.dexVideo == position }?.exoPlayer !=null
    }

    private fun addPlayer(exoPlayer: ExoPlayer, position: Int) {
        listExoplayer.let {

            if (it.size >= maxNum) {
                removeMaxDiffElement(position)

            }

            it.add(ExoPlayerItem(exoPlayer, position))
        }
//        Log.d("yeTest", "ADD -------------------------")
//        Log.d("yeTest", "VideoPlayerManager listSize:${listExoplayer.size} ")
//        listExoplayer.forEach {
//            Log.d("yeTest", "VideoPlayerManager position:${it.dexVideo}  player:${it.exoPlayer} ")
//        }
//        Log.d("yeTest", "EDD -------------------------")
    }

    fun pausePlayer(dex: Int) {
        val targetIndex = listExoplayer.indexOfFirst { it.dexVideo == dex }
        if (targetIndex!=-1) {
            val player = listExoplayer[targetIndex].exoPlayer
            player?.pause()
            player?.playWhenReady = false
        }
    }

    fun releaseAllPlayer() {
        listExoplayer.forEach {
            it.exoPlayer?.release()
            it.exoPlayer = null
        }
        listExoplayer.clear()
    }

    //不能出0位的，边界会出问题，出远端的
    private fun removeMaxDiffElement(dex:Int) {
        var maxDiff = Int.MIN_VALUE
        var indexToRemove = -1
        for (i in listExoplayer.indices) {
            val diff = abs((listExoplayer[i].dexVideo - dex).toDouble()).toInt()
            if (diff > maxDiff) {
                maxDiff = diff
                indexToRemove = i
            }
        }
        if (indexToRemove != -1) {
            listExoplayer[indexToRemove].exoPlayer?.release()
            listExoplayer[indexToRemove].exoPlayer = null
            listExoplayer.removeAt(indexToRemove)
        }
    }

}


