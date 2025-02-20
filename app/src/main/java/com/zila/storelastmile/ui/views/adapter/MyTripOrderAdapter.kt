package com.zila.storelastmile.ui.views.adapter

import android.app.Activity
import android.content.Intent
import android.content.res.AssetFileDescriptor
import android.media.MediaPlayer
import android.net.Uri
import android.os.Handler
import android.os.Looper
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import com.zila.storelastmile.R
import com.zila.storelastmile.data.model.MyTripOrderResponseModel.OrderlistEntity
import com.zila.storelastmile.databinding.MyTripOrderAdapterBinding
import com.zila.storelastmile.ui.views.main.ProductDetailsActivity
import com.zila.storelastmile.utilities.MyApplication
import com.zila.storelastmile.utilities.SharePrefs
import com.zila.storelastmile.utilities.TextUtils
import com.zila.storelastmile.utilities.Utils


class MyTripOrderAdapter(
    private val context: Activity,
    private val orderlist: ArrayList<OrderlistEntity>?
) : RecyclerView.Adapter<MyTripOrderAdapter.ViewHolder>() {
    private var mediaPlayer = MediaPlayer()
    private var wasPlaying = false
    private var handler: Handler = Handler(Looper.getMainLooper())
    private var updateSeekBar: Runnable? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            DataBindingUtil.inflate(
                LayoutInflater.from(parent.context), R.layout.my_trip_order_adapter, parent, false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, i: Int) {
        holder.mBinding.tvOrderid.text = orderlist!![i].orderid.toString() + ""
        holder.mBinding.tvAmount.text = "₹ " + orderlist[i].amount
        holder.mBinding.tvNoItems.text = "No.Item " + orderlist[i].noofitems
        holder.mBinding.tvStatus.text =   orderlist[i].status
        if (orderlist[i].reDispatchCount > 0) {
            holder.mBinding.btRedespach.visibility = View.VISIBLE
            holder.mBinding.btRedespach.text = "RD " + orderlist[i].reDispatchCount
        }
        if (orderlist[i].reAttemptCount > 0) {
            holder.mBinding.btReattamp.visibility = View.VISIBLE
            holder.mBinding.btReattamp.text = "RA " + orderlist[i].reAttemptCount
        }

        //  orderlist[i].orderType="ReturnOrder"

        if (!TextUtils.isNullOrEmpty(orderlist[i].orderType)) {
            holder.mBinding.tvOrderTypeStatus.text = "  " + orderlist[i].orderType
            if (orderlist[i].orderType == "Clearence Order") {
                holder.mBinding.tvOrderTypeStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvNoItems.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvtype.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.llMainLayout.setBackgroundResource(R.drawable.button_yellow_bottom_cl)
            }
            if (orderlist[i].orderType == "ReturnOrder") {
                holder.mBinding.tvOrderTypeStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvStatus.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvNoItems.setTextColor(
                    ContextCompat.getColor(
                        context,
                        R.color.white
                    )
                )
                holder.mBinding.tvtype.setTextColor(ContextCompat.getColor(context, R.color.white))
                holder.mBinding.llMainLayout.setBackgroundResource(R.drawable.return_card_bg)
            }
        } else {
            holder.mBinding.liOrderType.visibility = View.GONE
        }

        if (!orderlist[i].receiverName.isNullOrEmpty()) {
            holder.mBinding.liReceiverDetails.visibility = View.VISIBLE
            holder.mBinding.tvReceiverName.text = orderlist[i].receiverName
            holder.mBinding.liReceiverCall.setOnClickListener {
                if (!orderlist[i].receiverMobile.isNullOrEmpty()) {
                    val intent = Intent(
                        Intent.ACTION_DIAL,
                        Uri.parse("tel:" + "+91" + orderlist[i].receiverMobile)
                    )
                    context.startActivity(intent)
                } else {
                    Utils.setToast(context, "Receiver mobile number not available");
                }
            }
        }

        if (!orderlist[i].deliveryInstructions.isNullOrEmpty()) {
            holder.mBinding.liDeliveryInstruction.visibility = View.VISIBLE
            holder.mBinding.ViewDIn.visibility = View.VISIBLE
            holder.mBinding.liInstructionMeg.visibility = View.VISIBLE
            holder.mBinding.tvIntructionMessage.text = orderlist[i].deliveryInstructions
        }
        if (!orderlist[i].deliveryInstructionsAudioUrl.isNullOrEmpty()) {
            holder.mBinding.liDeliveryInstruction.visibility = View.VISIBLE
            holder.mBinding.ViewDIn.visibility = View.VISIBLE
            holder.mBinding.liInstructionAudio.visibility = View.VISIBLE
            if (mediaPlayer == null) {
                mediaPlayer = MediaPlayer()
            }
            holder.mBinding.playButton.setImageDrawable(
                ContextCompat.getDrawable(
                    context,
                    R.drawable.play_circle
                )
            )
            val url = SharePrefs.getInstance(context)
                .getString(SharePrefs.BASEURL) + orderlist[i].deliveryInstructionsAudioUrl
            mediaPlayer.setDataSource(url)
            mediaPlayer.prepare()
            mediaPlayer.setVolume(0.5f, 0.5f)
            mediaPlayer.isLooping = false
            holder.mBinding.seekbar.setMax(mediaPlayer.duration)
        }
        holder.mBinding.liPlayAudio.setOnClickListener { playSong(holder.mBinding, i) }
        holder.mBinding.tvOrderDetails.setOnClickListener {
            context.startActivity(
                Intent(
                    context,
                    ProductDetailsActivity::class.java
                ).putExtra("ORDER_ID", orderlist[holder.adapterPosition].orderid)
            )
        }
    }

    private fun playSong(mBinding: MyTripOrderAdapterBinding, position: Int) {
        try {
            if (mediaPlayer.isPlaying) {
                mediaPlayer.pause()
                wasPlaying = true
                mBinding.playButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.play_circle
                    )
                )
            }
            if (!wasPlaying) {
                mediaPlayer.start()
                mBinding.playButton.setImageDrawable(
                    ContextCompat.getDrawable(
                        context,
                        R.drawable.pause_circle
                    )
                )
                updateSeekBar = object : Runnable {
                    override fun run() {
                        if (mediaPlayer != null && mediaPlayer.isPlaying) {
                            val currentPosition = mediaPlayer.currentPosition / 1000 // Convert ms to seconds
                            mBinding.seekbar.progress = mediaPlayer.currentPosition
                            mBinding.tvVoiceTimer.text = formatTime(currentPosition) // Update UI with time
                            handler.postDelayed(this, 1000) // Update every 500ms
                        }
                    }
                }
                handler.post(updateSeekBar!!)
                /*Thread {
                    var currentPosition = mediaPlayer.currentPosition
                    val total = mediaPlayer.duration
                    while (mediaPlayer.isPlaying && currentPosition < total) {
                        try {
                            Thread.sleep(1000) // Pause for 1 second
                            currentPosition = mediaPlayer.currentPosition
                            // Update UI on the main thread
                            println("currentPosition>>>"+currentPosition)
                            println("total>>>"+total)
                            mBinding.seekbar.post {
                                mBinding.seekbar.progress = currentPosition
                            }
                        } catch (e: InterruptedException) {
                            return@Thread
                        } catch (e: Exception) {
                            return@Thread
                        }
                    }
                }.start()*/
                mediaPlayer.setOnCompletionListener {
                    println("STOP media")
                    handler.removeCallbacks(updateSeekBar!!) // Stop SeekBar updates
                    // clearMediaPlayer()
                    mBinding.tvVoiceTimer.text = "00:00";
                    mBinding.seekbar.progress = 0
                    mBinding.playButton.setImageDrawable(
                        ContextCompat.getDrawable(
                            context,
                            R.drawable.play_circle
                        )
                    )
                }
            }
            wasPlaying = false
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
    private fun formatTime(seconds: Int): String {
        val minutes = seconds / 60
        val secs = seconds % 60
        return String.format("%02d:%02d", minutes, secs)
    }
    private fun clearMediaPlayer() {
        mediaPlayer.stop()
        // mediaPlayer.release()
        // mediaPlayer.prepare()
    }

    override fun getItemCount(): Int {
        return orderlist?.size ?: 0
    }

    inner class ViewHolder(var mBinding: MyTripOrderAdapterBinding) : RecyclerView.ViewHolder(
        mBinding.root
    )
}