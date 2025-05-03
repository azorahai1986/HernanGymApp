package com.hernan.gymapp.user_type_selected.data.remote

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.hernan.gymapp.account.domain.model.AccountData
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthService @Inject constructor(
    private val firebaseAuth: FirebaseAuth
) {
    suspend fun register(accountData: AccountData) {
        firebaseAuth.createUserWithEmailAndPassword(accountData.email, accountData.password).await()
    }

    fun getCurrentUser(): FirebaseUser? = firebaseAuth.currentUser
}