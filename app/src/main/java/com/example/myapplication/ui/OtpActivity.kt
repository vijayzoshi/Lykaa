package com.example.myapplication.ui

import android.R.attr.button
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.addTextChangedListener
import com.example.myapplication.databinding.ActivityOtpBinding
import com.zegocloud.uikit.service.defines.ZegoUIKitUser
import java.util.Collections


class OtpActivity : AppCompatActivity() {

    lateinit var binding: ActivityOtpBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOtpBinding.inflate(layoutInflater)
        setContentView(binding.root)

  //      val userid = intent.getStringExtra("userid")
  //      binding.textView.text = "hi$userid"



        binding.editText.addTextChangedListener {
            val targetUserID = binding.editText.text.toString()
            getvideocall(targetUserID)
           getaudiocall(targetUserID)


        }


        //binding.button.setInvitees(Collections.singletonList(ZegoUIKitUser(targetUserID, targetUserName)))



    }

    private fun getvideocall(targetUserID: String) {
        binding.videocallBtn.setIsVideoCall(true)
        binding.videocallBtn.setResourceID("zego_uikit_call") // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        binding.videocallBtn.setInvitees(Collections.singletonList(ZegoUIKitUser( targetUserID,targetUserID)))

    }

    private fun getaudiocall(targetUserID: String) {

        binding.audiocallBtn.setIsVideoCall(false)
        binding.audiocallBtn.setResourceID("zego_uikit_call") // Please fill in the resource ID name that has been configured in the ZEGOCLOUD's console here.
        binding.audiocallBtn.setInvitees(Collections.singletonList(ZegoUIKitUser( targetUserID,targetUserID)))

    }




}