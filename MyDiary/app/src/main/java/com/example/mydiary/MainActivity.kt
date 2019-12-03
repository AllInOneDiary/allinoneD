package com.example.mydiary

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.EditText
import android.widget.Toast

import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import kotlinx.android.synthetic.main.activity_main.*

class MainActivity : AppCompatActivity() {

    private lateinit var auth: FirebaseAuth
    private val TAG : String = "MainActivity"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        auth = FirebaseAuth.getInstance()

        val email = findViewById<EditText>(R.id.emailText)
        val password = findViewById<EditText>(R.id.passwd)

        loginbtn.setOnClickListener {
            if(email.text.toString().length == 0 || password.text.toString().length==0) {
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            }else{
                auth.signInWithEmailAndPassword(email.text.toString(), password.text.toString()).addOnCompleteListener(this){
                    task -> if(task.isSuccessful){
                    Log.d(TAG,"signInWithEmail:success")
                    val user = auth.currentUser
                    UserModel.email = email.text.toString()
                    UserModel.password = password.text.toString()
                    if (user != null) {
                        UserModel.uid = user.uid
                    }
                    Log.d(TAG, "UserModel.email = ${UserModel.email}\nUserModel.password = ${UserModel.password}")
                    updateUI(user)
                    val intent = Intent(this, ViewPage::class.java)
                    startActivity(intent)
                }else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed.",
                        Toast.LENGTH_SHORT).show()
                    updateUI(null)
                    }

                }
            }

        }
        btn_create.setOnClickListener {
            val intent = Intent(this, CreateAccount::class.java)
            startActivity(intent)
            overridePendingTransition(R.anim.slide_in, R.anim.slide_out)
        }

    }

    override fun onResume() {
        super.onResume()
        val currentUser =auth.currentUser
        updateUI(currentUser)

    }

    override fun onStart(){
        super.onStart()
        val  currentUser = auth?.currentUser
        updateUI(currentUser)
    }
    fun updateUI(cUser : FirebaseUser?= null){
        if(cUser != null){

        }
    }
}