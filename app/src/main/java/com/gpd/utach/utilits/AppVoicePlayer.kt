package com.gpd.utach.utilits

import android.media.MediaPlayer
import com.gpd.utach.database.AppDatabase.Companion.getFileFromStorage
import java.io.File

class AppVoicePlayer {

    private lateinit var mMediaPlayer: MediaPlayer
    private lateinit var mFile: File

    fun play(messageKey: String, fileUrl: String, function: () -> Unit) {
        mFile = File(APP_ACTIVITY.filesDir, messageKey)
        if (mFile.exists() && mFile.length() > 0 && mFile.isFile) {
            startPlay() {
                function()
            }
        } else {
            mFile.createNewFile()
            getFileFromStorage(mFile, fileUrl) {
                startPlay {
                    function()
                }
            }
        }
    }

    private fun startPlay(function: () -> Unit) {
        try {
            mMediaPlayer.apply {
                setDataSource(mFile.absolutePath)
                prepare()
                start()
                setOnCompletionListener {
                    stop {
                        function()
                    }
                }
            }

        } catch (e: Exception) {
            showToast(e.message.toString())
        }
    }

    fun stop(function: () -> Unit) {
        try {
            mMediaPlayer.stop()
            mMediaPlayer.reset()
            function()
        } catch (e: Exception) {
            showToast(e.message.toString())
            function()
        }
    }

    fun release() {
        mMediaPlayer.release()
    }

    fun init(){
        mMediaPlayer = MediaPlayer()
    }
}