package com.app.view.adaptors

import android.content.Context
import android.media.MediaPlayer.OnCompletionListener
import android.media.MediaPlayer.OnPreparedListener
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.MediaController
import android.widget.VideoView
import androidx.recyclerview.widget.RecyclerView
import com.app.R
import com.app.model.dataclasses.FalconImageResponse
import com.bumptech.glide.Glide
import com.makeramen.roundedimageview.RoundedImageView

class AuctionImageAdapter(
    private val context: Context,
    images: List<FalconImageResponse>?,
) :  RecyclerView.Adapter<AuctionImageAdapter.ViewHolder>() {

    private val image : List<FalconImageResponse>? = images
    private var mVideoView: VideoView? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View =
            LayoutInflater.from(parent.context).inflate(R.layout.row_image_view, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindItems(image?.get(position))
    }

    override fun getItemCount(): Int {
        return if (image != null && image.isNotEmpty()) {
            image.size
        } else {
            0
        }
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){

        fun bindItems(list: FalconImageResponse?) {
            val ivImage = itemView.findViewById(R.id.ivImage) as RoundedImageView
            val ivIcon = itemView.findViewById(R.id.ivPlayIcon) as ImageView
            val llView = itemView.findViewById(R.id.llView) as View
            val videoView = itemView.findViewById(R.id.video) as VideoView
            Glide.with(context).load(list!!.docImageURL).into(ivImage)
            if (list.docType.equals("video")) {
                ivIcon.visibility = View.VISIBLE
            } else {
                ivIcon.visibility = View.GONE
            }
            videoView.setOnCompletionListener(OnCompletionListener {
                llView.visibility = View.GONE
                videoView.visibility = View.GONE
            })

            val mediaController = MediaController(context, false)
            mediaController.setAnchorView(videoView)
//            videoView.setMediaController(mediaController)
            videoView.setMediaController(null)

            videoView.setOnPreparedListener(OnPreparedListener { mp ->
                mp.isLooping = false
            })

            ivIcon.setOnClickListener(View.OnClickListener {
                mVideoView = videoView
                llView.visibility = View.VISIBLE
                videoView.visibility = View.VISIBLE
                play(adapterPosition)
            })

            videoView.setOnClickListener(View.OnClickListener {
                if (mVideoView != null)
                    mVideoView!!.resume()
            })

        }
    }

    fun pauseVideo() {
        if (mVideoView != null)
            mVideoView!!.stopPlayback()
    }

    fun play(position: Int) {
        if (mVideoView != null)
        {
            mVideoView!!.setVideoPath(image!![position].docVideoURL)
            mVideoView!!.start()
        }
    }


}