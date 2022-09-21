package com.kirdevelopment.worship47andorid2.views

import android.content.Context
import android.net.Uri
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.LinearLayout
import androidx.cardview.widget.CardView
import com.kirdevelopment.worship47andorid2.ClickListeners
import com.kirdevelopment.worship47andorid2.R
import com.kirdevelopment.worship47andorid2.databinding.VideoLayoutBinding
import com.kirdevelopment.worship47andorid2.utils.Constants
import com.squareup.picasso.Picasso
import jp.wasabeef.picasso.transformations.BlurTransformation

class VideoPreviewView(context: Context, attrs: AttributeSet? = null) :
    LinearLayout(context, attrs) {

    private var binding: VideoLayoutBinding

    init {
        binding = VideoLayoutBinding.inflate(LayoutInflater.from(context))
        inflate(context, R.layout.video_layout, this)
    }

    // устанавливает контент видео
    fun setVideo(listVideos: ArrayList<String>, clickListeners: ClickListeners) {
        val video1 = findViewById<ImageView>(R.id.iv_video_background1)
        val video2 = findViewById<ImageView>(R.id.iv_video_background2)
        val video3 = findViewById<ImageView>(R.id.iv_video_background3)

        val videoContainer1 = findViewById<CardView>(R.id.video_1)
        val videoContainer2 = findViewById<CardView>(R.id.video_2)
        val videoContainer3 = findViewById<CardView>(R.id.video_3)

        videoContainer1.setOnClickListener {
            clickListeners.videoPreviewClickListener(0)
        }

        videoContainer2.setOnClickListener {
            clickListeners.videoPreviewClickListener(1)
        }

        videoContainer3.setOnClickListener {
            clickListeners.videoPreviewClickListener(2)
        }

        when (listVideos.size) {
            1 -> {
                if (listVideos[0].isNotBlank()) videoContainer1.visibility = View.VISIBLE
                videoContainer2.visibility = View.GONE
                videoContainer3.visibility = View.GONE

                setVideoPreviewImage(video1, listVideos[0])
            }
            2 -> {
                if (listVideos[0].isNotBlank())videoContainer1.visibility = View.VISIBLE
                if (listVideos[1].isNotBlank()) videoContainer2.visibility = View.VISIBLE
                videoContainer3.visibility = View.GONE

                setVideoPreviewImage(video1, listVideos[0])
                setVideoPreviewImage(video2, listVideos[1])
            }
            3 -> {
                if (listVideos[0].isNotBlank()) videoContainer1.visibility = View.VISIBLE
                if (listVideos[1].isNotBlank()) videoContainer2.visibility = View.VISIBLE
                if (listVideos[2].isNotBlank()) videoContainer3.visibility = View.VISIBLE

                setVideoPreviewImage(video1, listVideos[0])
                setVideoPreviewImage(video2, listVideos[1])
                setVideoPreviewImage(video3, listVideos[2])
            }
            else -> {
                videoContainer1.visibility = View.GONE
                videoContainer2.visibility = View.GONE
                videoContainer3.visibility = View.GONE
            }
        }
    }

    // устанавливает превью видео
    private fun setVideoPreviewImage(imageView: ImageView, link: String) {
        val thumbnailParseUri = Uri.parse(Constants.IMAGE_LINK_YOUTUBE + link + Constants.DEFAULT_IMAGE)
        Picasso.get()
            .load(thumbnailParseUri)
            .fit().centerCrop()
            .transform(BlurTransformation(context, 10, 1))
            .placeholder(R.drawable.video_background)
            .error(R.drawable.video_background)
            .into(imageView)
    }
}