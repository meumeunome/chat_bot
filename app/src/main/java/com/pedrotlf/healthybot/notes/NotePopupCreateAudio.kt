package com.pedrotlf.healthybot.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.media.MediaRecorder
import android.util.Log
import android.view.View
import android.widget.Toast
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NotePopupCreateAudio(private val activity: ChatBaseActivity, private val mainPopup: NotePopupMain? = null) {
    private lateinit var mediaRecorder: MediaRecorder
    private var audioPlayer: MediaPlayer? = null

    private var AudioSavePathInDevice: String = ""

    private var willSave: Boolean = false

    fun showPopupNotesCreateAudio(dialog: Dialog){
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
        val btnStop: View = dialog.findViewById(R.id.btn_stop)
        val btnSave: View = dialog.findViewById(R.id.btn_save)
        val btnCancel: View = dialog.findViewById(R.id.btn_cancel)

        dialog.setOnDismissListener {
            try {
                audioPlayer?.stop()
            }catch (e: java.lang.IllegalStateException){
                Log.e("audioPlayer", "Already released")
            }
            audioPlayer?.release()

            if (!willSave)
                deleteFile(AudioSavePathInDevice)

            willSave = false

            mainPopup?.showPopupNotesMain(Dialog(activity))
        }

        btnPlay.setOnClickListener {
            playFile(AudioSavePathInDevice){
                btnPlay.visibility = View.VISIBLE
                btnStop.visibility = View.GONE
            }
            btnPlay.visibility = View.GONE
            btnStop.visibility = View.VISIBLE
        }

        btnStop.setOnClickListener {
            audioPlayer?.stop()
            audioPlayer?.release()
            btnPlay.visibility = View.VISIBLE
            btnStop.visibility = View.GONE
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
            val sdf = SimpleDateFormat("yyyy-MM-dd_-_HH;mm;ss", Locale.getDefault())
            val currentDateandTime: String = sdf.format(Date())

            AudioSavePathInDevice =
                (activity.getExternalFilesDir(null)?.absolutePath ?: "") +
                        File.separator +
                        "Voice_-_" +
                        currentDateandTime +
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
        mediaRecorder.release()

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

    private fun playFile(path: String, onFinishFunc: ()->Unit){
        try {
            audioPlayer = MediaPlayer()

            audioPlayer?.setDataSource(path)

            audioPlayer?.prepare()

            audioPlayer?.setOnCompletionListener {
                audioPlayer?.release()
                onFinishFunc()
            }
        }catch (e: IOException){
            e.printStackTrace()
            Toast.makeText(activity, "An error ocurred with the audio", Toast.LENGTH_SHORT).show()
        }

        audioPlayer?.start()
    }
}