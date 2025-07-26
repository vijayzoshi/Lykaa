package com.example.myapplication.ui

import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import com.example.myapplication.R
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.SignInButton
import com.google.android.gms.common.api.ApiException
import com.google.android.material.textfield.TextInputLayout
import com.google.firebase.FirebaseException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.PhoneAuthCredential
import com.google.firebase.auth.PhoneAuthOptions
import com.google.firebase.auth.PhoneAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.google.firebase.ktx.Firebase
import java.util.concurrent.TimeUnit


class LoginActivity : AppCompatActivity() {



    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInClient: GoogleSignInClient
    private val RC_SIGN_IN = 100

    val databaseReference = FirebaseDatabase.getInstance().getReference()


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_login)

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        /*
        // Check if user is already signed in
        if (auth.currentUser != null) {
            // User is already signed in, send to main activity
            startActivity(Intent(this, NameActivity::class.java))
            finish()
            return
        }


         */
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        findViewById<SignInButton>(R.id.btnVerifyOtp).setOnClickListener {
            signInWithGoogle()
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            try {
                val account = task.getResult(ApiException::class.java)!!
                firebaseAuthWithGoogle(account.idToken!!)
            } catch (e: ApiException) {
                Toast.makeText(this, "Google Sign In Failed", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    val user = auth.currentUser
                    val sharedPref = getSharedPreferences("userdetails", Context.MODE_PRIVATE)
                    val editor = sharedPref.edit()
                    editor.putString("userid", user?.uid)

                    editor.apply()




                    databaseReference.child("users").child(auth.currentUser?.uid.toString()).addListenerForSingleValueEvent(object : ValueEventListener {
                        override fun onDataChange(snapshot: DataSnapshot) {
                            if (snapshot.hasChild("username")) {
                                // Key exists
                                startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                                finish()                            }
                            else {
                                startActivity(Intent(this@LoginActivity, NameActivity::class.java))
                                finish()                                }
                        }

                        override fun onCancelled(error: DatabaseError) {
                                               }
                    })


                    /*
                               if (user != null) {
                                   databaseReference.child("users").child(auth.currentUser?.uid.toString()).child("username")
                                       .addValueEventListener(object : ValueEventListener {
                                           override fun onDataChange(snapshot: DataSnapshot) {


                                               if(snapshot.exists()){
                                                   startActivity(Intent(this@LoginActivity, MainActivity2::class.java))
                                                   finish()

                                               }else{
                                                   startActivity(Intent(this@LoginActivity, NameActivity::class.java))
                                                   finish()

                                               }


                                           }


                                           override fun onCancelled(error: DatabaseError) {
                                               TODO("Not yet implemented")

                                           }
                                       }

                                       )
                               }

                    */


                } else {
                    Toast.makeText(this, "Firebase Auth Failed: ${task.exception?.message}", Toast.LENGTH_SHORT).show()
                }
            }
    }



}