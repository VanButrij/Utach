package com.gpd.utach.utilits

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.provider.ContactsContract
import android.provider.OpenableColumns
import android.view.inputmethod.InputMethodManager
import android.widget.ImageView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.gpd.utach.MainActivity
import com.gpd.utach.R
import com.gpd.utach.database.*
import com.gpd.utach.database.AppDatabase.Companion.updatePhonesToDatabase
import com.gpd.utach.models.CommonModel
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.squareup.picasso.Picasso
import java.text.SimpleDateFormat
import java.util.*

fun showToast(message: String) {
    Toast.makeText(APP_ACTIVITY, message, Toast.LENGTH_SHORT).show()
}

fun restartActivity() {

    val intent = Intent(APP_ACTIVITY, MainActivity::class.java)
    APP_ACTIVITY.startActivity(intent)
    APP_ACTIVITY.finish()

}

fun replaceFragment(fragment: Fragment, addStack: Boolean = true) {
    if (addStack) {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .addToBackStack(null)
            .replace(R.id.data_container, fragment)
            .commit()
    } else {
        APP_ACTIVITY.supportFragmentManager.beginTransaction()
            .replace(R.id.data_container, fragment)
            .commit()
    }
}

fun hideKeyboard() {
    /* Функция скрывает клавиатуру */
    val imm: InputMethodManager =
        APP_ACTIVITY.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(APP_ACTIVITY.window.decorView.windowToken, 0)
}

fun ImageView.downloadAndSetImage(url: String) {
    /* Функция расширения ImageView, скачивает и устанавлиевает картинку */
    Picasso.get()
        .load(url)
        .fit()
        .placeholder(com.gpd.utach.R.drawable.default_photo)
        .into(this)
}

@SuppressLint("Range")
fun initContacts() {
    if (checkPermission(READ_CONTACTS)) {
        var arrayContacts = arrayListOf<CommonModel>()
        val cursor = APP_ACTIVITY.contentResolver.query(
            ContactsContract.CommonDataKinds.Phone.CONTENT_URI,
            null,
            null,
            null,
            null
        )
        cursor?.let {
            while (it.moveToNext()) {
                val fullName =
                    it.getString(it.getColumnIndex(ContactsContract.Contacts.DISPLAY_NAME))
                val phone =
                    it.getString(it.getColumnIndex(ContactsContract.CommonDataKinds.Phone.NUMBER))
                val newModel = CommonModel()
                newModel.fullname = fullName
                newModel.phone = phone.replace(Regex("[\\s,-]"), "")
                arrayContacts.add(newModel)
            }
        }

        cursor?.close()
        updatePhonesToDatabase(arrayContacts)
    }
}

fun String.asTime(): String {
    val time = Date(this.toLong())
    val timeFormat = SimpleDateFormat("HH:mm", Locale.getDefault())
    return timeFormat.format(time)
}

@SuppressLint("Range")
fun getFileFromUri(uri: Uri): String {
    var result = ""
    val cursor = APP_ACTIVITY.contentResolver.query(uri, null, null, null, null)
    try {
        if (cursor != null && cursor.moveToFirst()) {
            result =
                cursor.getString(cursor.getColumnIndex(OpenableColumns.DISPLAY_NAME)).toString()
        }
    } catch (e: Exception) {
        showToast(e.message.toString())
    } finally {
        cursor?.close()
        return result
    }
}

fun getPlurals(count: Int) = APP_ACTIVITY.resources.getQuantityString(
    R.plurals.count_members, count, count
)

// общий блок после успешной авторизации в приложении (по номеру телефона)
fun authUtilPhone(phoneNumber: String) {
    val uid: String = AUTH.currentUser?.uid.toString()
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_PHONE] = phoneNumber

    REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->

            if (!dataSnapshot.hasChild(CHILD_USERNAME)) {
                dateMap[CHILD_USERNAME] = uid
            }

            REF_DATABASE_ROOT.child(NODE_PHONES).child(phoneNumber).setValue(uid)
                .addOnFailureListener { showToast(it.message.toString()) }
                .addOnSuccessListener {
                    REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                        .addOnSuccessListener {
                            showToast(APP_ACTIVITY.getString(R.string.welcome))
                            restartActivity()
                        }
                        .addOnFailureListener { showToast(it.message.toString()) }
                }
        })
}

fun authUtilGoogle(account: GoogleSignInAccount) {

    val uid: String = AUTH.currentUser?.uid.toString()
    val name: String = account.displayName!!
    val mail: String = account.email!!
    val dateMap = mutableMapOf<String, Any>()
    dateMap[CHILD_ID] = uid
    dateMap[CHILD_USERNAME] = name
    dateMap[CHILD_FULLNAME] = name
    dateMap[CHILD_EMAIL] = mail

    REF_DATABASE_ROOT.child(NODE_USERS).child(uid)
        .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->

            if (!dataSnapshot.hasChild(CHILD_USERNAME)) {
                dateMap[CHILD_USERNAME] = uid
            }

            REF_DATABASE_ROOT.child(NODE_USERS).child(uid).updateChildren(dateMap)
                .addOnSuccessListener {
                    showToast(APP_ACTIVITY.getString(R.string.welcome))
                    restartActivity()
                }
                .addOnFailureListener { showToast(it.message.toString()) }

        })
}
