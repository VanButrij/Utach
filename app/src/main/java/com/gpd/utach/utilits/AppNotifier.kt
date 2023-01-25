package com.gpd.utach.utilits

import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.core.app.NotificationManagerCompat
import com.gpd.utach.R
import com.gpd.utach.database.AppDatabase.Companion.getCommonModel
import com.gpd.utach.database.AppDatabase.Companion.setMessageRead
import com.gpd.utach.database.CURRENT_UID
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener

class AppNotifier {

    private val CHANNEL_ID = "channel_id_example_01"
    private val notificationId = 101

    fun initNotification(refMessages: DatabaseReference) {

        var listener = AppChildEventListener{
            //уведомления на листенере
            val message = it.getCommonModel()


            if (message.to == CURRENT_UID && !message.isRead) {
                createNotificationChannel()
                sendNotification(message.from, message.text)
                setMessageRead(message.id, message.from)
            }
        }

        refMessages.addListenerForSingleValueEvent(object : ValueEventListener {
            override fun onDataChange(dataSnapshot: DataSnapshot) {
                for (snapshot in dataSnapshot.children) {
                    refMessages.child(snapshot.key.toString()).addChildEventListener(listener)
                }
            }

            override fun onCancelled(databaseError: DatabaseError) {}
        })
    }

    private fun createNotificationChannel() {

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val name = "Notification Title"
            val descriptionText = "Notification Description"
            val importance = NotificationManager.IMPORTANCE_DEFAULT
            val channel = NotificationChannel(CHANNEL_ID, name, importance).apply {
                description = descriptionText
            }
            val notificationManager: NotificationManager =
                APP_ACTIVITY.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }

    }

    private fun sendNotification(messageFrom: String, messageText: String) {

        val builder = NotificationCompat.Builder(APP_ACTIVITY, CHANNEL_ID)
            .setSmallIcon(R.drawable.ic_notify_blue)
            .setContentTitle(messageFrom)
            .setContentText(messageText)
            .setPriority(NotificationCompat.PRIORITY_DEFAULT)

        with(NotificationManagerCompat.from(APP_ACTIVITY)) {
            notify(notificationId, builder.build())
        }



  //      deleteNotificationChannel()
    }

    private fun deleteNotificationChannel() {
        //TODO: remove notification channel
    }

}