package com.pedrotlf.healthybot

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.view.View
import android.widget.TextView
import android.widget.Toast
import java.io.File
import java.io.IOException


class NotePopups(private val activity: ChatBaseActivity) {

    private lateinit var mediaRecorder: MediaRecorder
    private lateinit var audioPlayer: MediaPlayer

    private var AudioSavePathInDevice: String = ""

    private var willSave: Boolean = false

    fun showPopupNotesMain(dialog: Dialog){
        dialog.setContentView(R.layout.popup_note_home)

        val btnCreate: TextView = dialog.findViewById(R.id.btn_create)

        btnCreate.setOnClickListener {
            showPopupNotesCreate(dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    fun showPopupNotesCreate(d: Dialog? = null){
        val dialog: Dialog = d ?: Dialog(activity)

        dialog.setContentView(R.layout.popup_note_create)

        val btnCreateText: View = dialog.findViewById(R.id.btn_create_note_text)
        val btnCreateAudio: View = dialog.findViewById(R.id.btn_create_note_audio)

        btnCreateAudio.setOnClickListener {
            showPopupNotesCreateAudio(dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showPopupNotesCreateAudio(dialog: Dialog){
        dialog.setContentView(R.layout.popup_note_create_audio)

        val btnListen: View = dialog.findViewById(R.id.btn_listen)

        btnListen.setOnClickListener {
            createAudioOnClickRecord(it, dialog)
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun showPopupNotesCreateAudioConfirmation(dialog: Dialog){
        dialog.setContentView(R.layout.popup_note_create_audio_confirmation)

        val btnPlay: View = dialog.findViewById(R.id.btn_play)
        val btnSave: View = dialog.findViewById(R.id.btn_save)
        val btnCancel: View = dialog.findViewById(R.id.btn_cancel)

        dialog.setOnDismissListener {
            if (!willSave)
                deleteFile(AudioSavePathInDevice)

            willSave = false

            showPopupNotesMain(Dialog(activity))
        }

        btnPlay.setOnClickListener {
            playFile(AudioSavePathInDevice)
        }

        btnSave.setOnClickListener {
            willSave = true
            dialog.dismiss()
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun createAudioOnClickRecord(view: View, dialog: Dialog) {
        if (activity.checkPermission()) {
            AudioSavePathInDevice =
                (activity.getExternalFilesDir(null)?.absolutePath ?: "") +
                        File.separator +
                        "Voice_Note_-_" +
                        ".3gp"

            mediaRecorderReady()

            try {
                mediaRecorder.prepare()
                mediaRecorder.start()

                dialog.setOnDismissListener {
                    mediaRecorder.stop()
                    deleteFile(AudioSavePathInDevice)
                }

                view.setOnClickListener {
                    createAudioOnClickStopRecording(it, dialog)
                }

                val hintTextStop: View = dialog.findViewById(R.id.hint_text_stop)
                val hintTextListening: View = dialog.findViewById(R.id.hint_text_listening)
                val hintText: View = dialog.findViewById(R.id.hint_text)

                hintText.visibility = View.INVISIBLE
                hintTextStop.visibility = View.VISIBLE
                hintTextListening.visibility = View.VISIBLE

                Toast.makeText(activity, "Recording started", Toast.LENGTH_SHORT).show()
            } catch (e: IllegalStateException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            } catch (e: IOException) {
                // TODO Auto-generated catch block
                e.printStackTrace()
            }

        } else {
            activity.requestPermission()
        }

    }

    private fun createAudioOnClickStopRecording(view: View, dialog: Dialog){
        mediaRecorder.stop()

        view.setOnClickListener {
            createAudioOnClickRecord(it, dialog)
        }

        val hintTextStop: View = dialog.findViewById(R.id.hint_text_stop)
        val hintTextListening: View = dialog.findViewById(R.id.hint_text_listening)
        val hintText: View = dialog.findViewById(R.id.hint_text)

        hintText.visibility = View.VISIBLE
        hintTextStop.visibility = View.INVISIBLE
        hintTextListening.visibility = View.INVISIBLE

        showPopupNotesCreateAudioConfirmation(dialog)

        Toast.makeText(activity, "Recording completed", Toast.LENGTH_SHORT).show()
    }

    private fun mediaRecorderReady() {
        mediaRecorder = MediaRecorder()
        mediaRecorder.setAudioSource(MediaRecorder.AudioSource.MIC)
        mediaRecorder.setOutputFormat(MediaRecorder.OutputFormat.THREE_GPP)
        mediaRecorder.setAudioEncoder(MediaRecorder.OutputFormat.AMR_NB)
        mediaRecorder.setOutputFile(AudioSavePathInDevice)

        Log.i("AudioSavePathInDevice", AudioSavePathInDevice)
    }

    private fun deleteFile(path: String){
        val fdelete = File(path)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Toast.makeText(activity, "Audio deleted", Toast.LENGTH_SHORT).show()
                Log.i("deleteFile", "file Deleted: $path")
            } else {
                Log.i("deleteFile", "file not Deleted: $path")
            }
        }
    }

    private fun playFile(path: String){
        try {
            audioPlayer = MediaPlayer()

            audioPlayer.setDataSource(path)

            audioPlayer.prepare()
        }catch (e: IOException){
            e.printStackTrace()
        }

        audioPlayer.start()
    }
}