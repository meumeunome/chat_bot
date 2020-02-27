package com.pedrotlf.healthybot.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R
import java.io.File
import java.io.IOException


class NotePopupMain(private val activity: ChatBaseActivity) {

    private var audioPlayer: MediaPlayer? = null

    private var playingFile: String = ""

    private var playingPlayLayout: View? = null
    private var playingStopLayout: View? = null

    private val createPopup: NotePopupCreate =
        NotePopupCreate(activity, this)

    fun showPopupNotesMain(dialog: Dialog){
        dialog.setContentView(R.layout.popup_note_main)

        val btnCreate: TextView = dialog.findViewById(R.id.btn_create)
        val notesList: LinearLayout = dialog.findViewById(R.id.notes_list)

        btnCreate.setOnClickListener {
            createPopup.showPopupNotesCreate(dialog)
            stopPlayingCurrent()
        }

        dialog.setOnDismissListener {
            stopPlayingCurrent()
        }

        populateList(notesList)

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun populateList(list: LinearLayout) {
        list.removeAllViews()
        val path: String? = activity.getExternalFilesDir(null)?.absolutePath

        if (path != null){
            val directory = File(path)
            if (directory.exists()){
                val files = directory.listFiles()

                if (files != null){
                    for (file in files){
                        Log.i("audioFileFound", file.name)
                        when{
                            file.name.contains(".3gp") -> addAudioToList(file, list)
                        }
                    }
                } else {
                    Log.i("fileFound", "NO FILE FOUND")
                }
            }
        }
    }

    private fun addAudioToList(file: File, list: LinearLayout) {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_note_saved, list, false)

        val title: TextView = view.findViewById(R.id.title)
        val btnPlay: View = view.findViewById(R.id.btn_play)
        val btnStop: View = view.findViewById(R.id.btn_stop)
        val btnDelete: View = view.findViewById(R.id.btn_delete)

        btnPlay.setOnClickListener {
            stopPlayingCurrent()

            playingStopLayout = btnStop
            playingPlayLayout = btnPlay

            playingStopLayout?.visibility = View.VISIBLE
            playingPlayLayout?.visibility = View.GONE

            playFile(file.absolutePath) {
                stopPlayingCurrent()
            }
        }

        btnStop.setOnClickListener {
            stopPlayingCurrent()
        }

        btnDelete.setOnClickListener {
            stopPlayingCurrent()
            deleteFile(file.absolutePath)
            list.removeView(view)
        }

        title.text = file.name.removeSuffix(".3gp").replace("_", " ").replace(";", ":")

        list.addView(view)
    }

    private fun stopPlayingCurrent(){
        audioPlayer?.stop()
        audioPlayer?.release()
        audioPlayer = null

        playingStopLayout?.visibility = View.GONE
        playingPlayLayout?.visibility = View.VISIBLE

        playingStopLayout = null
        playingPlayLayout = null
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
                onFinishFunc()
            }
        }catch (e: IOException){
            e.printStackTrace()
            Toast.makeText(activity, "An error ocurred with the audio", Toast.LENGTH_SHORT).show()
        }

        audioPlayer?.start()
    }
}