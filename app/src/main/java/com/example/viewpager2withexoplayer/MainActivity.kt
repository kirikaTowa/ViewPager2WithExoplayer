package com.example.viewpager2withexoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager2withexoplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    //private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos.add(
            Video(
                "Big Buck Bunny0",
                "https://v3.cdnpk.net/videvo_files/video/free/2019-11/large_preview/190301_1_25_11.mp4"
            )
        )

        videos.add(
            Video(
                "Elephant Dream1",
                "https://v3.cdnpk.net/videvo_files/video/free/2022-03/large_preview/220113_04_Dog_4k_020.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes2",
                "https://v1.cdnpk.net/videvo_files/video/premium/getty_204/large_watermarked/istock-473100605_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes3",
                "https://v2.cdnpk.net/videvo_files/video/premium/partners0841/large_watermarked/BB_360f1182-59bf-4749-a51f-48173c4bfbf6_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes4",
                "https://v3.cdnpk.net/videvo_files/video/premium/partners0155/large_watermarked/BB_1240c534-71be-4e23-aac1-6a04df2a7f35_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes5",
                "https://v6.cdnpk.net/videvo_files/video/excite/premium/partners0055/large_watermarked/BB_cefa02cc-335d-4511-b13f-c6d2c45c6b63_preview.mp4"
            )
        )

        adapter = VideoAdapter(this, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                /*val item=VideoPlayerManager.exoplayerList.find { it.dexVideo == exoPlayerItem.dexVideo }
                if (item!=null){
                    exoPlayerItems.remove(item)
                }
                exoPlayerItems.add(exoPlayerItem)*/
            }
        })
        adapter.submitList(videos)

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = VideoPlayerManager.exoplayerList.indexOfFirst { it.exoPlayer?.isPlaying ?: false }
                if (previousIndex != -1) {
                    val player = VideoPlayerManager.exoplayerList[previousIndex].exoPlayer
                    player?.pause()
                    player?.playWhenReady = false
                }
                val newIndex = VideoPlayerManager.exoplayerList.indexOfFirst { it.dexVideo == position }
                if (newIndex != -1) {
                    val player = VideoPlayerManager.exoplayerList[newIndex].exoPlayer
                    player?.playWhenReady = true
                    player?.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = VideoPlayerManager.exoplayerList.indexOfFirst { it.dexVideo == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = VideoPlayerManager.exoplayerList[index].exoPlayer
            player?.pause()
            player?.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = VideoPlayerManager.exoplayerList.indexOfFirst { it.dexVideo == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = VideoPlayerManager.exoplayerList[index].exoPlayer
            player?.playWhenReady = true
            player?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (VideoPlayerManager.exoplayerList.isNotEmpty()) {
            for (item in VideoPlayerManager.exoplayerList) {
                val player = item.exoPlayer
                player?.stop()
                player?.clearMediaItems()
            }
        }
    }
}