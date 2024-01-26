package com.example.viewpager2withexoplayer

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager2withexoplayer.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var adapter: VideoAdapter
    private val videos = ArrayList<Video>()
    private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        videos.add(
            Video(
                "Big Buck Bunny",
                "https://v3.cdnpk.net/videvo_files/video/free/2019-11/large_preview/190301_1_25_11.mp4"
            )
        )

        videos.add(
            Video(
                "Elephant Dream",
                "https://v3.cdnpk.net/videvo_files/video/free/2022-03/large_preview/220113_04_Dog_4k_020.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes",
                "https://v1.cdnpk.net/videvo_files/video/premium/getty_204/large_watermarked/istock-473100605_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes",
                "https://v2.cdnpk.net/videvo_files/video/premium/partners0841/large_watermarked/BB_360f1182-59bf-4749-a51f-48173c4bfbf6_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes",
                "https://v3.cdnpk.net/videvo_files/video/premium/partners0155/large_watermarked/BB_1240c534-71be-4e23-aac1-6a04df2a7f35_preview.mp4"
            )
        )

        videos.add(
            Video(
                "For Bigger Blazes",
                "https://v6.cdnpk.net/videvo_files/video/excite/premium/partners0055/large_watermarked/BB_cefa02cc-335d-4511-b13f-c6d2c45c6b63_preview.mp4"
            )
        )

        adapter = VideoAdapter(this, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
                exoPlayerItems.add(exoPlayerItem)
            }
        })
        adapter.submitList(videos)

        binding.viewPager2.adapter = adapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                val previousIndex = exoPlayerItems.indexOfFirst { it.exoPlayer?.isPlaying ?: false }
                if (previousIndex != -1) {
                    val player = exoPlayerItems[previousIndex].exoPlayer
                    player?.pause()
                    player?.playWhenReady = false
                }
                val newIndex = exoPlayerItems.indexOfFirst { it.position == position }
                if (newIndex != -1) {
                    val player = exoPlayerItems[newIndex].exoPlayer
                    player?.playWhenReady = true
                    player?.play()
                }
            }
        })
    }

    override fun onPause() {
        super.onPause()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player?.pause()
            player?.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()

        val index = exoPlayerItems.indexOfFirst { it.position == binding.viewPager2.currentItem }
        if (index != -1) {
            val player = exoPlayerItems[index].exoPlayer
            player?.playWhenReady = true
            player?.play()
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (exoPlayerItems.isNotEmpty()) {
            for (item in exoPlayerItems) {
                val player = item.exoPlayer
                player?.stop()
                player?.clearMediaItems()
            }
        }
    }
}