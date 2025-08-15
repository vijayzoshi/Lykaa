package com.example.myapplication.ui

import android.app.Application
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import com.example.myapplication.R
import com.zegocloud.uikit.prebuilt.call.ZegoUIKitPrebuiltCallService
import com.zegocloud.uikit.prebuilt.call.invite.ZegoUIKitPrebuiltCallInvitationConfig
import com.zegocloud.uikit.prebuilt.call.invite.widget.ZegoSendCallInvitationButton
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections

class NewActivity : AppCompatActivity() {




    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_new)

        val loginBtn = findViewById<Button>(R.id.button)
        val useridEt = findViewById<EditText>(R.id.userideditText)




        val  application = application // Android's application context
        val  appID =1128845963L    // yourAppID
        val appSign ="7ed70bc4ef4935a75f27ed561414f69f26bde8e574fec4b505b1ec47b6397eb3";  // yourAppSign




        loginBtn.setOnClickListener {
            val userID = useridEt.text.toString()// yourUserID, userID should only contain numbers, English characters, and '_'.
            val userName  =  "jaja"// yourUserName


            val callInvitationConfig =  ZegoUIKitPrebuiltCallInvitationConfig();
            ZegoUIKitPrebuiltCallService.init(
                application,
                appID,
                appSign,
                userID,
                userName,
                callInvitationConfig);

            val intent = Intent(this, OtpActivity::class.java)
            intent.putExtra("userid", userID)
            startActivity(intent)

        }



    }


}