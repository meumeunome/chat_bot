package com.pedrotlf.healthybot

import android.Manifest.permission.RECORD_AUDIO
import android.Manifest.permission.WRITE_EXTERNAL_STORAGE
import android.annotation.SuppressLint
import android.content.pm.PackageManager
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.pedrotlf.healthybot.notes.NotePopupMain


@SuppressLint("Registered")
open class ChatBaseActivity : AppCompatActivity() {

    protected val RequestPermissionCode: Int = 1

    protected lateinit var notePopup: NotePopupMain

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        notePopup = NotePopupMain(this)
    }

    fun requestPermission() {
        ActivityCompat.requestPermissions(this,
            arrayOf(WRITE_EXTERNAL_STORAGE, RECORD_AUDIO),
            RequestPermissionCode
        )
    }

    fun checkPermission(): Boolean {
        val storagePermission = ContextCompat.checkSelfPermission(
            applicationContext,
            WRITE_EXTERNAL_STORAGE
        )
        val recordAudioPermission = ContextCompat.checkSelfPermission(
            applicationContext,
            RECORD_AUDIO
        )
        return storagePermission == PackageManager.PERMISSION_GRANTED &&
                recordAudioPermission == PackageManager.PERMISSION_GRANTED
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        when(requestCode){
            RequestPermissionCode -> {
                if (grantResults.isNotEmpty()) {
                    val storagePermission: Boolean = grantResults[0] == PackageManager.PERMISSION_GRANTED
                    val recordPermission: Boolean = grantResults[1] == PackageManager.PERMISSION_GRANTED

                    if (storagePermission && recordPermission) {
                        Toast.makeText(this, "Permission Granted", Toast.LENGTH_LONG).show()
                    } else {
                        Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
                    }
                }
            }
        }
    }
}