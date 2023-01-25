package com.gpd.utach.utilits

import com.gpd.utach.MainActivity

lateinit var APP_ACTIVITY:MainActivity

const val TYPE_MESSAGE_IMAGE = "image"
const val TYPE_MESSAGE_TEXT = "text"
const val TYPE_MESSAGE_VOICE = "voice"
const val TYPE_MESSAGE_FILE = "file"

const val TYPE_CHAT = "chat"
const val TYPE_GROUP = "group"
const val TYPE_CHANNEL = "channel"

const val PICK_FILE_REQUEST_CODE = 301

// типы авторизации, переделать в интерфейс или дата класс
const val TYPE_AUTH_PHONE = 1
const val TYPE_AUTH_GIT = 2
const val TYPE_AUTH_GOOGLE = 3