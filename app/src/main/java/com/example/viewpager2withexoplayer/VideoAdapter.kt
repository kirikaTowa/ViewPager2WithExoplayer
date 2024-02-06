package com.example.viewpager2withexoplayer

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.example.viewpager2withexoplayer.databinding.ListVideoBinding
import com.google.android.exoplayer2.ExoPlayer
import com.google.android.exoplayer2.PlaybackException
import com.google.android.exoplayer2.Player
import kotlin.math.log

class VideoAdapter(
    private val context: Context,
    private val videoPreparedListener: OnVideoPreparedListener
) : ListAdapter<Video, VideoAdapter.VideoViewHolder>(VideoDiffCallback()) {

    companion object{
        private var playerListener: Player.Listener? = null
    }


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): VideoViewHolder {

        val view = ListVideoBinding.inflate(LayoutInflater.from(context), parent, false)
        return VideoViewHolder(view, context, videoPreparedListener)
    }

    override fun onBindViewHolder(holder: VideoViewHolder, position: Int) {
        Log.d("VideoAdapter", "onBindViewHolder:$holder position:$position ")
        val model = getItem(position)
        holder.binding.tvTitle.text = model.title
    }

    class VideoViewHolder(
        val binding: ListVideoBinding,
        private val context: Context,
        private val videoPreparedListener: OnVideoPreparedListener
    ) : androidx.recyclerview.widget.RecyclerView.ViewHolder(binding.root) {
        init {
            binding.root.tag = this
            Log.d("VideoAdapter", "onCreateViewHolder: "+this)
        }

        private lateinit var exoPlayer: ExoPlayer


        fun setVideoPath(url: String) {
            exoPlayer = VideoPlayerManager.getPlayerInstance(context,bindingAdapterPosition)
            VideoPlayerManager.configPlayer(context,exoPlayer, url)
            addListenerAndPlaying()
        }

        fun setPlayer() {
            exoPlayer = VideoPlayerManager.getPlayerInstance(context,bindingAdapterPosition)
            addListenerAndPlaying()
        }

        private fun addListenerAndPlaying() {
            // 移除旧监听器
            playerListener?.let { exoPlayer.removeListener(it) }

            // 创建新的监听器实例
            playerListener = object : Player.Listener {
                override fun onPlayerError(error: PlaybackException) {
                    super.onPlayerError(error)
                    Toast.makeText(context, "Can't play this video", Toast.LENGTH_SHORT).show()
                }

                override fun onPlayerStateChanged(playWhenReady: Boolean, playbackState: Int) {
                    if (playbackState == Player.STATE_BUFFERING) {
                        binding.pbLoading.visibility = View.VISIBLE
                    } else if (playbackState == Player.STATE_READY) {
                        binding.pbLoading.visibility = View.GONE
                    }
                }
            }

            // 添加新监听器并设置给 ExoPlayer
            exoPlayer.addListener(playerListener as Player.Listener)
            binding.playerView.player = exoPlayer


            exoPlayer.playWhenReady = true
            exoPlayer.play()

            Log.d("yeTest", "adapter内: "+exoPlayer)
            videoPreparedListener.onVideoPrepared(ExoPlayerItem(exoPlayer, absoluteAdapterPosition))
        }

    }

    override fun onDetachedFromRecyclerView(recyclerView: RecyclerView) {
        VideoPlayerManager.releaseAllPlayer()
        super.onDetachedFromRecyclerView(recyclerView)
    }

    override fun onViewDetachedFromWindow(holder: VideoViewHolder) {
        VideoPlayerManager.pausePlayer(holder.bindingAdapterPosition)
        super.onViewDetachedFromWindow(holder)
    }


    interface OnVideoPreparedListener {
        fun onVideoPrepared(exoPlayerItem: ExoPlayerItem)
    }

    private class VideoDiffCallback : DiffUtil.ItemCallback<Video>() {
        override fun areItemsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.url == newItem.url
        }

        @SuppressLint("DiffUtilEquals")
        override fun areContentsTheSame(oldItem: Video, newItem: Video): Boolean {
            return oldItem.title == newItem.title && oldItem.url == newItem.url
        }
    }
}
