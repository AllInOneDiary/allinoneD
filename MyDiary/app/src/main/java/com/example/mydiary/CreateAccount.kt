package com.example.mydiary

import android.os.Bundle
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.auth.FirebaseAuth
import kotlinx.android.synthetic.main.activity_create_account.*

class CreateAccount : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_account)

        auth = FirebaseAuth.getInstance()

        //사용자가 입력한 값들
        val email = findViewById<EditText>(R.id.et_email)
        val password = findViewById<EditText>(R.id.et_password)


        //새로운 계정을 생성한다.
        bt_CreateAccount.setOnClickListener {
            if (email.text.toString().length == 0 || password.text.toString().length == 0) {
                Toast.makeText(this, "email 혹은 password를 반드시 입력하세요.", Toast.LENGTH_SHORT).show()
            } else {
                auth.createUserWithEmailAndPassword(email.text.toString(), password.text.toString())
                    .addOnCompleteListener(this) { task ->
                        if (task.isSuccessful) {

                            finish()
                            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
                        } else {
                            Toast.makeText(
                                baseContext, "Authentication failed.",
                                Toast.LENGTH_SHORT
                            ).show()
                            email?.setText("")
                            password?.setText("")
                            email.requestFocus()
                        }
                    }
            }
        }
        bt_cancel.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_enter, R.anim.slide_exit)
        }

    }
}