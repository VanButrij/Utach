package com.gpd.utach.ui.screens.settings

import com.gpd.utach.R
import com.gpd.utach.database.AppDatabase.Companion.setBioToDatabase
import com.gpd.utach.database.USER
import com.gpd.utach.ui.screens.base.BaseChangeFragment
import kotlinx.android.synthetic.main.fragment_change_bio.*


class ChangeBioFragment : BaseChangeFragment(R.layout.fragment_change_bio) {

    override fun onResume() {
        super.onResume()
        settings_input_bio.setText(USER.bio)
    }

    override fun change() {
        super.change()
        val newBio:String = settings_input_bio.text.toString()

        setBioToDatabase(newBio)
    }

}