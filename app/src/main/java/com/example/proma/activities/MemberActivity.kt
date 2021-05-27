package com.example.proma.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proma.R
import com.example.proma.adapters.MemberListItemsAdapter
import com.example.proma.firebase.FireStore
import com.example.proma.models.Board
import com.example.proma.models.User
import com.example.proma.utils.Constants
import kotlinx.android.synthetic.main.activity_member.*
import kotlinx.android.synthetic.main.activity_task_list.*

class MemberActivity : BaseActivity() {

    private lateinit var mBoardDetails: Board

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_member)

        setupActionBar()
        if(intent.hasExtra(Constants.BOARD_DETAIL)){
            mBoardDetails = intent.getParcelableExtra<Board>(Constants.BOARD_DETAIL)
        }
        showProgressDialog(resources.getString(R.string.please_wait))
        FireStore().getAssignedMemebersListDetails(this, mBoardDetails.assignTo)
    }
    fun setUpMembersList(list: ArrayList<User>){
        hideProgressDialog()

        rv_members_list.layoutManager = LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)
        val adapter = MemberListItemsAdapter(this,list)
        rv_members_list.adapter = adapter
    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_members_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = resources.getString(R.string.members)
        }
        toolbar_members_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
}