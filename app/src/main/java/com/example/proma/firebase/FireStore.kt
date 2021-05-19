package com.example.proma.firebase

import android.app.Activity
import android.util.Log
import com.example.proma.activities.MainActivity
import com.example.proma.activities.ProfileActivity
import com.example.proma.activities.SignInActivity
import com.example.proma.activities.SignUpActivity
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

    fun getCurretnUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun loadUserData(activity: Activity) {
        mFireStore.collection(Constants.USERS)
            .document(getCurretnUserId()).get()
            .addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)
                if (loggedInUser != null) {
                    when(activity){
                        is SignInActivity ->{
                            activity.signInSuccess(loggedInUser)
                        }
                        is MainActivity -> {
                            activity.updateNavigationUserDetails(loggedInUser)
                        }
                        is ProfileActivity -> {
                            activity.setUserDataInUI(loggedInUser)
                        }
                    }

                }
            }.addOnFailureListener { e ->
                when(activity){
                    is SignInActivity ->{
                        activity.hideProgressDialog()
                    }
                    is MainActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e("FireStore SignInUser", "Error writing document", e)
            }
    }

}