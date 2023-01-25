package com.gpd.utach.ui.message_recycler_view.view_holders

import android.os.Environment
import android.view.View
import android.widget.ImageView
import android.widget.ProgressBar
import android.widget.TextView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.recyclerview.widget.RecyclerView
import com.gpd.utach.database.AppDatabase.Companion.getFileFromStorage
import com.gpd.utach.database.CURRENT_UID
import com.gpd.utach.ui.message_recycler_view.views.MessageView
import com.gpd.utach.utilits.WRITE_FILES
import com.gpd.utach.utilits.asTime
import com.gpd.utach.utilits.checkPermission
import com.gpd.utach.utilits.showToast
import kotlinx.android.synthetic.main.message_item_file.view.*
import java.io.File

class HolderFileMessage(view: View) : RecyclerView.ViewHolder(view), MessageHolder {

    private val blockReceivedFileMessage: ConstraintLayout = view.block_received_file_message
    private val blockUserFileMessage: ConstraintLayout = view.block_user_file_message
    private val chatUserFileMessageTime: TextView = view.chat_user_file_message_time
    private val chatReceivedFileMessageTime: TextView = view.chat_received_file_message_time

    private val chatUserFileName: TextView = view.chat_user_file_name
    private val chatReceivedFileName: TextView = view.chat_received_file_name
    private val chatReceivedProgressBar: ProgressBar = view.chat_received_progress_bar
    private val chatUserProgressBar: ProgressBar = view.chat_user_progress_bar
    private val chatUserBtnDownload: ImageView = view.chat_user_btn_download
    private val chatReceivedBtnDownload: ImageView = view.chat_received_btn_download


    override fun drawMessage(view: MessageView) {
        if (view.from == CURRENT_UID) {
            blockReceivedFileMessage.visibility = View.GONE
            blockUserFileMessage.visibility = View.VISIBLE
            chatUserFileMessageTime.text = view.timeStamp.asTime()
            chatUserFileName.text = view.text
        } else {
            blockReceivedFileMessage.visibility = View.VISIBLE
            blockUserFileMessage.visibility = View.GONE
            chatReceivedFileMessageTime.text = view.timeStamp.asTime()
            chatReceivedFileName.text = view.text
        }
    }

    override fun onAttach(view: MessageView) {
        if (view.from == CURRENT_UID) chatUserBtnDownload.setOnClickListener { onBtnFileClick(view) }
        else chatReceivedBtnDownload.setOnClickListener { onBtnFileClick(view) }
    }

    private fun onBtnFileClick(view: MessageView) {
        if (view.from == CURRENT_UID) {
            chatUserProgressBar.visibility = View.VISIBLE
        } else {
            chatReceivedProgressBar.visibility = View.VISIBLE
        }

        val file = File(
            Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_DOWNLOADS),
            view.text
        )

        try {
            if (checkPermission(WRITE_FILES)){
                file.createNewFile()
                getFileFromStorage(file,view.fileUrl){
                    if (view.from == CURRENT_UID) {
                        chatUserProgressBar.visibility = View.GONE
                    } else {
                        chatReceivedProgressBar.visibility = View.GONE
                    }
                }
            }
        } catch (e: Exception){
            showToast(e.message.toString())
        }
    }


    override fun onDetach() {
        chatUserBtnDownload.setOnClickListener(null)
        chatReceivedBtnDownload.setOnClickListener(null)
    }
}