package com.pedrotlf.healthybot.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.media.MediaPlayer
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.Utils
import java.io.*


class NotePopupMain(private val activity: ChatBaseActivity) {

    private var audioPlayer: MediaPlayer? = null

    private var playingFile: String = ""

    private var playingPlayLayout: View? = null
    private var playingStopLayout: View? = null

    private val createPopup: NotePopupCreate =
        NotePopupCreate(activity, this)

    private lateinit var dialog: Dialog

    fun showPopupNotesMain(d: Dialog){
        dialog = d
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
        val path: String = activity.filesDir.absolutePath

        Log.i("fileFindingAt", path)

        val directory = File(path)
        if (directory.exists()){
            val files = directory.listFiles()

            if (files != null){
                for (file in files){
                    Log.i("audioFileFound", file.name)
                    when{
                        file.name.contains(".3gp") -> addAudioToList(file, list)
                        file.name.contains(".txt") -> addTextToList(file, list)
                    }
                }
            } else {
                Log.i("fileFound", "NO FILE FOUND")
            }
        }
    }

    private fun addTextToList(file: File, list: LinearLayout) {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_note_text_saved, list, false)

        val title: TextView = view.findViewById(R.id.title)
        val previewText: TextView = view.findViewById(R.id.preview_text)
        val btnEdit: View = view.findViewById(R.id.btn_edit)
        val btnDelete: View = view.findViewById(R.id.btn_delete)

        btnEdit.setOnClickListener {
            stopPlayingCurrent()
            createPopup.editTextNote(dialog, file)
        }

        btnDelete.setOnClickListener {
            stopPlayingCurrent()
            deleteFile(file.absolutePath)
            list.removeView(view)
        }

        title.text = file.name.removeSuffix(".txt").replace("_", " ").replace(";", ":")

        previewText.text = readFileAsString(file.name)

        list.addView(view)
    }

    private fun addAudioToList(file: File, list: LinearLayout) {
        val view = LayoutInflater.from(activity).inflate(R.layout.item_note_audio_saved, list, false)

        val title: TextView = view.findViewById(R.id.title)
        val btnPlay: View = view.findViewById(R.id.btn_play)
        val btnStop: View = view.findViewById(R.id.btn_stop)
        val btnEdit: View = view.findViewById(R.id.btn_edit)
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

        btnEdit.setOnClickListener {
            showPopupNotesEditTitle(file)
        }

        btnDelete.setOnClickListener {
            stopPlayingCurrent()
            deleteFile(file.absolutePath)
            list.removeView(view)
        }

        title.text = file.name.removeSuffix(".3gp").replace("_", " ").replace(";", ":")

        list.addView(view)
    }

    private fun showPopupNotesEditTitle(file: File){
        dialog.setContentView(R.layout.popup_note_edit_title)

        val title: EditText = dialog.findViewById(R.id.title)
        val btnCancel: View = dialog.findViewById(R.id.btn_cancel)
        val btnSave: View = dialog.findViewById(R.id.btn_save)

        title.setText(file.name.removeSuffix(".3gp"))

        btnCancel.setOnClickListener {
            showPopupNotesMain(dialog)
        }
        
        btnSave.setOnClickListener {
            when{
                Utils.containsWordFromList(title.text.toString(), listOf("\\", "/", ":", "*", "?", "\"", "<", ">", "|")) -> {
                    Toast.makeText(activity, "A file name can't contain any of the following characters:\n\\ / : * ? \" < > |", Toast.LENGTH_SHORT).show()
                }

                title.text.toString().isEmpty() -> {
                    Toast.makeText(activity, "File name can't be empty", Toast.LENGTH_SHORT).show()
                }

                title.text.toString().contains( "\n") -> {
                    Toast.makeText(activity, "A file name can't contain line breaks.", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    val dir: File = activity.filesDir
                    val to = File(dir, title.text.toString() + ".3gp")
                    file.renameTo(to)
                    showPopupNotesMain(dialog)
                }
            }
        }
    }

    private fun stopPlayingCurrent(){
        audioPlayer?.stop()
        audioPlayer?.reset()
        audioPlayer?.release()
        audioPlayer = null

        playingStopLayout?.visibility = View.GONE
        playingPlayLayout?.visibility = View.VISIBLE

        playingStopLayout = null
        playingPlayLayout = null
    }

    fun deleteFile(path: String){
        val fdelete = File(path)
        if (fdelete.exists()) {
            if (fdelete.delete()) {
                Toast.makeText(activity, "Note deleted!", Toast.LENGTH_SHORT).show()
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

    fun readFileAsString(fileName: String): String? {
        val stringBuilder = StringBuilder()
        var line: String?
        val buffReader: BufferedReader?
        try {
            buffReader = BufferedReader(FileReader(File(activity.filesDir, fileName)))
            while (buffReader.readLine().also { line = it } != null) stringBuilder.append(line)
        } catch (e: FileNotFoundException) {
            Log.e("Exception", "File read failed: $e")
        } catch (e: IOException) {
            Log.e("Exception", "File read failed: $e")
        }
        return stringBuilder.toString()
    }
}