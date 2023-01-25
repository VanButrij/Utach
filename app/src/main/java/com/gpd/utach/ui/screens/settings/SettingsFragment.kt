package com.gpd.utach.ui.screens.settings

import android.app.Activity.RESULT_OK
import android.content.Intent
import android.net.Uri
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import com.gpd.utach.R
import com.gpd.utach.database.*
import com.gpd.utach.database.AppDatabase.Companion.getUrlFromStorage
import com.gpd.utach.database.AppDatabase.Companion.putFileToStorage
import com.gpd.utach.database.AppDatabase.Companion.putUrlToDatabase
import com.gpd.utach.ui.screens.base.BaseFragment
import com.gpd.utach.utilits.*
import com.google.firebase.storage.StorageReference
import kotlinx.android.synthetic.main.fragment_settings.*


class SettingsFragment : BaseFragment(R.layout.fragment_settings) {


    override fun onResume() {
        super.onResume()
        setHasOptionsMenu(true)
        initFields()
    }

    private fun initFields() {
        settings_bio.text = USER.bio
        settings_full_name.text = USER.fullname
        settings_phone_number.text = USER.phone
        settings_status.text = USER.state
        settings_username.text = USER.username
        settings_btn_change_username.setOnClickListener{ replaceFragment(ChangeUsernameFragment())}
        settings_btn_change_bio.setOnClickListener{ replaceFragment(ChangeBioFragment())}
        settings_change_photo.setOnClickListener{ changePhotoUser() }
        settings_user_photo.downloadAndSetImage(USER.photoUrl)
    }

    private fun changePhotoUser() {
        /*
        CropImage.activity()
            .setAspectRatio(1, 1)
            .setRequestedSize(250,250)
            .setCropShape(CropImageView.CropShape.OVAL)
            .start(APP_ACTIVITY, this)

         */

    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        activity?.menuInflater?.inflate(R.menu.settings_action_menu, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.settings_menu_exit -> {
                AppStates.updateState(AppStates.OFFLINE)
                AUTH.signOut()
                restartActivity()
            }
            R.id.settings_menu_change_name -> replaceFragment(ChangeNameFragment())
        }
        return true
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        /*
        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE
            && resultCode == RESULT_OK && data != null) {
            val uri: Uri = CropImage.getActivityResult(data).uri
            val path: StorageReference = REF_STORAGE_ROOT.child(FOLDER_PROFILE_IMAGE)
                .child(CURRENT_UID)

            putFileToStorage(uri, path){
                getUrlFromStorage(path){
                    putUrlToDatabase(it){
                        settings_user_photo.downloadAndSetImage(it)
                        showToast(getString(R.string.toast_data_updated))
                        USER.photoUrl = it
                        APP_ACTIVITY.mAppDrawer.updateHeader()
                    }
                }
            }
        }

         */
    }

}