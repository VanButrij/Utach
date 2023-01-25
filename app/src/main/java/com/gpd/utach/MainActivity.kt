package com.gpd.utach

import android.content.pm.PackageManager
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.appcompat.widget.Toolbar
import androidx.core.content.ContextCompat
import com.gpd.utach.database.*
import com.gpd.utach.database.AppDatabase.Companion.initFirebase
import com.gpd.utach.database.AppDatabase.Companion.initUser
import com.gpd.utach.databinding.ActivityMainBinding
import com.gpd.utach.ui.objects.AppDrawer
import com.gpd.utach.ui.screens.auth.AuthFragment
import com.gpd.utach.ui.screens.main_list.MainListFragment
import com.gpd.utach.utilits.*
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch


class MainActivity : AppCompatActivity() {

    private lateinit var mBinding: ActivityMainBinding
    private var mNotifier: AppNotifier = AppNotifier()
    lateinit var mAppDrawer: AppDrawer
    lateinit var mToolbar: Toolbar
    private val mRefMessages = FirebaseDatabase.getInstance()
        .reference.child(NODE_MESSAGES).child(FirebaseAuth
            .getInstance().currentUser?.uid.toString())

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        mBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(mBinding.root)
        APP_ACTIVITY = this

        initFirebase()
        initUser {
            CoroutineScope(Dispatchers.IO).launch {
                initContacts()
            }
            initFields()
            initFunc()
        }
    }

    override fun onResume() {
        super.onResume()
        mNotifier.initNotification(mRefMessages)
    }

    override fun onStart() {
        super.onStart()
        AppStates.updateState(AppStates.ONLINE)

    }

    override fun onStop() {
        super.onStop()
        AppStates.updateState(AppStates.OFFLINE)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (ContextCompat.checkSelfPermission(
                APP_ACTIVITY,
                READ_CONTACTS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            initContacts()
        }
    }

    private fun initFunc() {
        setSupportActionBar(mToolbar)
        if (AUTH.currentUser != null) {
            mAppDrawer.create()
            replaceFragment(MainListFragment(), false)
        } else {
            replaceFragment(AuthFragment())
        }
    }


    private fun initFields() {
        mToolbar = mBinding.mainToolbar
        mAppDrawer = AppDrawer()
    }


}