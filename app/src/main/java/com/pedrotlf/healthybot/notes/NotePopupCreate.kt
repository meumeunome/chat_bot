package com.pedrotlf.healthybot.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.view.View
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R

class NotePopupCreate(activity: ChatBaseActivity, mainPopup: NotePopupMain? = null) {

    private val createAudioPopup: NotePopupCreateAudio =
        NotePopupCreateAudio(activity, mainPopup)

    fun showPopupNotesCreate(dialog: Dialog){
        dialog.setContentView(R.layout.popup_note_create)

        val btnCreateText: View = dialog.findViewById(R.id.btn_create_note_text)
        val btnCreateAudio: View = dialog.findViewById(R.id.btn_create_note_audio)

        btnCreateAudio.setOnClickListener {
            createAudioPopup.showPopupNotesCreateAudio(dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }
}