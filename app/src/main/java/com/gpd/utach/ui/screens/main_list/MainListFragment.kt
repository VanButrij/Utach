package com.gpd.utach.ui.screens.main_list

import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import com.gpd.utach.R
import com.gpd.utach.database.*
import com.gpd.utach.database.AppDatabase.Companion.getCommonModel
import com.gpd.utach.models.CommonModel
import com.gpd.utach.utilits.*
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.android.synthetic.main.fragment_main_list.*


class MainListFragment : Fragment(R.layout.fragment_main_list) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: MainListAdapter
    private var mListItems = listOf<CommonModel>()

    private val mRefMainList = REF_DATABASE_ROOT.child (NODE_MAIN_LIST).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)




    override fun onResume() {
        super.onResume()
        APP_ACTIVITY.title = getString(R.string.app_title)
        APP_ACTIVITY.mAppDrawer.enableDrawer()
        hideKeyboard()
        initRecyclerView()

    }

    private fun initRecyclerView() {
        mRecyclerView = main_list_recycle_view
        mAdapter = MainListAdapter()

        // 1 запрос
        mRefMainList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

                when(model.type){
                    TYPE_CHAT -> showChat(model)
                    TYPE_GROUP -> showGroup(model)
                }

            }
        })

        mRecyclerView.adapter = mAdapter

    }

    private fun showGroup(model: CommonModel) {
        // 2 запрос
        REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id)
            .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
            val newModel = dataSnapshot1.getCommonModel()

            // 3 запрос
                REF_DATABASE_ROOT.child(NODE_GROUPS).child(model.id).child(NODE_MESSAGES)
                    .limitToLast(1)
                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                    val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                    if (tempList.isEmpty()){
                        newModel.lastMessage = getString(R.string.chat_is_empty)
                    } else {
                        newModel.lastMessage = tempList[0].text
                    }
                    newModel.type = TYPE_GROUP
                    mAdapter.updateListItems(newModel)
                })
        })
    }

    private fun showChat(model: CommonModel) {
        // 2 запрос
        mRefUsers.child(model.id).addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot1 ->
            val newModel = dataSnapshot1.getCommonModel()

            // 3 запрос
            mRefMessages.child(model.id).limitToLast(1)
                .addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot2 ->
                    val tempList = dataSnapshot2.children.map { it.getCommonModel() }

                    if (tempList.isEmpty()){
                        newModel.lastMessage = getString(R.string.chat_is_empty)
                    } else {
                        newModel.lastMessage = tempList[0].text
                    }


                    if (newModel.fullname.isEmpty()) {
                        newModel.fullname = newModel.phone
                    }

                    newModel.type = TYPE_CHAT
                    mAdapter.updateListItems(newModel)
                })
        })
    }
}