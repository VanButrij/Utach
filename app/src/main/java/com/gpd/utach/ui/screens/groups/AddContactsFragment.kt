package com.gpd.utach.ui.screens.groups

import androidx.recyclerview.widget.RecyclerView
import com.gpd.utach.R
import com.gpd.utach.database.*
import com.gpd.utach.database.AppDatabase.Companion.getCommonModel
import com.gpd.utach.models.CommonModel
import com.gpd.utach.ui.screens.base.BaseFragment
import com.gpd.utach.utilits.*
import kotlinx.android.synthetic.main.fragment_add_contacts.*


class AddContactsFragment : BaseFragment(R.layout.fragment_add_contacts) {

    private lateinit var mRecyclerView: RecyclerView
    private lateinit var mAdapter: AddContactsAdapter
    private var mListItems = listOf<CommonModel>()
    private val mRefContactsList = REF_DATABASE_ROOT.child (NODE_PHONES_CONTACTS).child(CURRENT_UID)
    private val mRefUsers = REF_DATABASE_ROOT.child(NODE_USERS)
    private val mRefMessages = REF_DATABASE_ROOT.child(NODE_MESSAGES).child(CURRENT_UID)


    override fun onResume() {
        super.onResume()
        listContacts.clear()
        APP_ACTIVITY.title = getString(R.string.add_participant)
        hideKeyboard()
        initRecyclerView()
        add_contacts_btn_next.setOnClickListener {
            if (listContacts.isEmpty()) showToast(getString(R.string.list_contacts_is_empty))
            else replaceFragment(CreateGroupFragment(listContacts))
        }
    }

    private fun initRecyclerView() {
        mRecyclerView = add_contacts_recycle_view
        mAdapter = AddContactsAdapter()

        // 1 запрос
        mRefContactsList.addListenerForSingleValueEvent(AppValueEventListener { dataSnapshot ->
            mListItems = dataSnapshot.children.map { it.getCommonModel() }
            mListItems.forEach { model ->

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
                            mAdapter.updateListItems(newModel)
                        })
                })
            }
        })

        mRecyclerView.adapter = mAdapter
    }

    companion object {
        val listContacts = mutableListOf<CommonModel>()
    }
}