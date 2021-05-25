package com.example.proma.activities


import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.proma.R
import com.example.proma.adapters.TaskListItemAdapter
import com.example.proma.firebase.FireStore
import com.example.proma.models.Board
import com.example.proma.models.Task
import com.example.proma.utils.Constants
import kotlinx.android.synthetic.main.activity_task_list.*

class TaskListActivity : BaseActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list)

        var boardDocumentId = ""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)
        }

        showProgressDialog(resources.getString(R.string.please_wait))
        FireStore().getBoardDetails(this, boardDocumentId)
    }


    private fun setupActionBar(title: String) {
        setSupportActionBar(toolbar_task_list_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_back_24dp)
            actionBar.title = title
        }
        toolbar_task_list_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun boardDetails(board: Board){
        hideProgressDialog()
        setupActionBar(board.name)
        val addTaskList = Task(resources.getString(R.string.action_add_list))
        board.taskList.add(addTaskList)

        rv_task_list.layoutManager =  LinearLayoutManager(this, LinearLayoutManager.HORIZONTAL, false)
        rv_task_list.setHasFixedSize(true)
        val adapter = TaskListItemAdapter(this, board.taskList)
        rv_task_list.adapter = adapter
    }
}