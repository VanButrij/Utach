package com.gpd.utach.ui.message_recycler_view.views

import com.gpd.utach.models.CommonModel
import com.gpd.utach.utilits.TYPE_MESSAGE_FILE
import com.gpd.utach.utilits.TYPE_MESSAGE_IMAGE
import com.gpd.utach.utilits.TYPE_MESSAGE_VOICE

class AppViewFactory {
    companion object {
        fun getView(message: CommonModel): MessageView {
            return when(message.type){
                TYPE_MESSAGE_IMAGE -> ViewImageMessage(
                    message.id,
                    message.from,
                    message.to,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.isRead
                )
                TYPE_MESSAGE_VOICE -> ViewVoiceMessage(
                    message.id,
                    message.from,
                    message.to,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.isRead,
                    message.text
                )
                TYPE_MESSAGE_FILE -> ViewFileMessage(
                    message.id,
                    message.from,
                    message.to,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.isRead
                )
                else -> ViewTextMessage(
                    message.id,
                    message.from,
                    message.to,
                    message.timeStamp.toString(),
                    message.fileUrl,
                    message.isRead,
                    message.text
                )
            }
        }
    }
}