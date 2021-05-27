package com.example.proma.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.example.proma.activities.*
import com.example.proma.models.Board
import com.example.proma.models.User
import com.example.proma.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FireStore {
    private val mFireStore = FirebaseFirestore.getInstance()

    fun registerUser(activity: SignUpActivity, userInfo: User) {
        mFireStore.collection(Constants.USERS)
            .document(getCurretnUserId()).set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "Error")
            }
    }

    fun getBoardDetails(activity: TaskListActivity, documentId: String) {
        mFireStore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                val board = document.toObject(Board::class.java)!!
                board.documentId = document.id
                activity.boardDetails(board)

            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board) {
        mFireStore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board has created successfully")
                Toast.makeText(activity, "Board has created successfully", Toast.LENGTH_SHORT)
                    .show()
                activity.createBoardSuccessfully()
            }
            .addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Board has created failed", exception)
            }
    }

    fun getBoardsList(activity: MainActivity) {
        mFireStore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurretnUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()
                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }

                activity.populateBoardsListToUI(boardList)
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a board.", e)
            }
    }

    fun addUpdateTaskList(activity: TaskListActivity, board: Board) {
        val taskListHashMap = HashMap<String, Any>()
        taskListHashMap[Constants.TASK_LIST] = board.taskList

        mFireStore.collection(Constants.BOARDS)
            .document(board.documentId)
            .update(taskListHashMap)
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Task list updated successfully")
                activity.addUpdateTaskListSuccess()
            }
            .addOnFailureListener { exception ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while creating a task.", exception)
            }
    }

    fun getCurretnUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun updateUserProfileData(activity: ProfileActivity, userHashMap: HashMap<String, Any>) {
        mFireStore.collection(Constants.USERS)
            .document(getCurretnUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "Profile Data updated successfully!")
                Toast.makeText(activity, "Profile Data updated successfully", Toast.LENGTH_SHORT)
                    .show()
                activity.updateProfileSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while creating a board",
                    e
                )
                Toast.makeText(activity, "Profile data update failed", Toast.LENGTH_SHORT).show()
            }
    }

    fun loadUserData(activity: Activity, readBoardList: Boolean = false) {
        mFireStore.collection(Constants.USERS)
            .document(getCurretnUserId()).get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    when (activity) {
                        is SignInActivity -> {
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser, readBoardList)
                        }
                        is ProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }

                }
            }.addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("FireStore SignInUser", "Error writing document", e)
            }
    }

    fun getAssignedMemebersListDetails(activity: MemberActivity, assignedTo: ArrayList<String>){
        mFireStore.collection(Constants.USERS)
            .whereIn(Constants.ID, assignedTo)
            .get()
            .addOnSuccessListener {
                document ->
                Log.e(activity.javaClass.simpleName, document.documents.toString())
                val usersList: ArrayList<User> = ArrayList()

                for (i in document.documents){
                    val user = i.toObject(User::class.java)!!
                    usersList.add(user)
                }
                activity.setUpMembersList(usersList)
            }
            .addOnFailureListener {
                e ->
                activity.hideProgressDialog()
                Log.e(activity.javaClass.simpleName, "Error while get members list", e)
            }
    }
}