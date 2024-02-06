package com.example.viewpager2withexoplayer

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.viewpager2.widget.ViewPager2
import com.example.viewpager2withexoplayer.databinding.ActivityMainBinding


class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    private lateinit var videoAdapter: VideoAdapter
    private val videos = ArrayList<Video>()
    //private val exoPlayerItems = ArrayList<ExoPlayerItem>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        initView()
        initData()
        initListener()
    }


    private fun initView() {
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
    }

    private fun initData() {
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
    }

    private fun initListener() {
        videoAdapter = VideoAdapter(this, object : VideoAdapter.OnVideoPreparedListener {
            override fun onVideoPrepared(exoPlayerItem: ExoPlayerItem) {
            }
        })
        videoAdapter.submitList(videos)

        binding.viewPager2.adapter = videoAdapter

        binding.viewPager2.registerOnPageChangeCallback(object : ViewPager2.OnPageChangeCallback() {
            override fun onPageSelected(position: Int) {
                if (VideoPlayerManager.isExistPlayer(position)) {
//                    Log.d("yeTest", "已加载直接播: "+VideoPlayerManager.isExistPlayer(position))
                    getHolderByPos(position)?.setPlayer()
                } else {
//                    Log.d("yeTest", "未加载初始化配置: "+position)
                    getHolderByPos(position)?.setVideoPath(videos[position].url)
                }

                //处理预加载
                proLoad(position - 1)
                proLoad(position + 1)
            }
        })
    }

    private fun getHolderByPos(position: Int): VideoAdapter.VideoViewHolder? {
        val recyclerView = binding.viewPager2.getChildAt(0) as RecyclerView
        val layoutManager = recyclerView.layoutManager as LinearLayoutManager
        val view = layoutManager.findViewByPosition(position)
        return view?.tag as? VideoAdapter.VideoViewHolder
    }

    private fun proLoad(position: Int) {
        if (position > 0 && position < videos.size && !VideoPlayerManager.isExistPlayer(position)) {
            Log.d("yeTest", "预加载 位置: " + position)
            val exoPlayer =
                VideoPlayerManager.getPlayerInstance(this@MainActivity, position)
            VideoPlayerManager.configPlayer(
                this@MainActivity,
                exoPlayer,
                videos[position].url
            )

        }
    }


    override fun onPause() {
        super.onPause()
        val player = VideoPlayerManager.getExistPlayer(binding.viewPager2.currentItem)
        player?.let {
            player.pause()
            player.playWhenReady = false
        }
    }

    override fun onResume() {
        super.onResume()
        val player = VideoPlayerManager.getExistPlayer(binding.viewPager2.currentItem)
        player?.let {
            player.play()
            player.playWhenReady = true
        }

    }

    override fun onDestroy() {
        super.onDestroy()
//        if (VideoPlayerManager.exoplayerList.isNotEmpty()) {
//            for (item in VideoPlayerManager.exoplayerList) {
//                val player = item.exoPlayer
//                player?.stop()
//                player?.clearMediaItems()
//            }
//        }
    }
}