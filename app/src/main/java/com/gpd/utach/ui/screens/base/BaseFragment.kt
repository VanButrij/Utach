package com.gpd.utach.ui.screens.base

import androidx.fragment.app.Fragment
import com.gpd.utach.utilits.APP_ACTIVITY


open class BaseFragment(layout:Int) : Fragment(layout) {

    override fun onStart() {
        super.onStart()
        APP_ACTIVITY.mAppDrawer.disableDrawer()
    }

}