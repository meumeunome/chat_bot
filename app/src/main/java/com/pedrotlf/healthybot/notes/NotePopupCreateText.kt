package com.pedrotlf.healthybot.notes

import android.app.Dialog
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.util.Log
import android.view.View
import android.widget.EditText
import android.widget.Toast
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.Utils.containsWordFromList
import java.io.File
import java.io.FileWriter
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class NotePopupCreateText(private val activity: ChatBaseActivity, private val mainPopup: NotePopupMain? = null) {

    private var fileName: String = ""

    fun showPopupNotesCreateText(dialog: Dialog, file: File? = null){
        dialog.setContentView(R.layout.popup_note_create_text)

        val sdf = SimpleDateFormat("yyyy-MM-dd_-_HH;mm;ss", Locale.getDefault())
        val currentDateandTime: String = sdf.format(Date())

        fileName = file?.name?.removeSuffix(".txt")?: "Text - $currentDateandTime"

        dialog.setOnDismissListener {
            mainPopup?.showPopupNotesMain(Dialog(activity))
        }

        val title: EditText = dialog.findViewById(R.id.title)
        val inputText: EditText = dialog.findViewById(R.id.input_text)
        val btnCancel: View = dialog.findViewById(R.id.btn_cancel)
        val btnSave: View = dialog.findViewById(R.id.btn_save)

        title.setText(fileName)

        if(file != null) {
            if(mainPopup != null)
                inputText.setText(mainPopup.readFileAsString(file.name))
        }

        btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        btnSave.setOnClickListener {
            when {
                inputText.text.toString().isEmpty() -> {
                    Toast.makeText(activity, "Can't save an empty file!", Toast.LENGTH_SHORT).show()
                }

                containsWordFromList(title.text.toString(), listOf("\\", "/", ":", "*", "?", "\"", "<", ">", "|")) -> {
                    Toast.makeText(activity, "A file name can't contain any of the following characters:\n\\ / : * ? \" < > |", Toast.LENGTH_SHORT).show()
                }

                title.text.toString().contains( "\n") -> {
                    Toast.makeText(activity, "A file name can't contain line breaks.", Toast.LENGTH_SHORT).show()
                }

                else -> {
                    if (activity.checkStoragePermission()) {
                        if (title.text.toString().isNotEmpty())
                            fileName = title.text.toString()
                        if (file != null) {
                            mainPopup?.deleteFile(file.absolutePath)
                        }
                        writeTextToFile(inputText.text.toString())
                        dialog.dismiss()
                    } else {
                        activity.requestStoragePermission()
                    }
                }
            }
        }

        dialog.window?.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        dialog.show()
    }

    private fun writeTextToFile(fileContents: String) {
        try {
            val out = FileWriter(File(activity.filesDir, "$fileName.txt"))
            out.write(fileContents)
            out.close()
            Toast.makeText(activity, "Text note saved!", Toast.LENGTH_SHORT).show()
        } catch (e: IOException) {
            Log.e("Exception", "File write failed: $e")
            Toast.makeText(activity, "File write failed!", Toast.LENGTH_SHORT).show()
        }
    }
}