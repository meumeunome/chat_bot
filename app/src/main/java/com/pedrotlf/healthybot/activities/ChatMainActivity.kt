package com.pedrotlf.healthybot.activities

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.pedrotlf.healthybot.ChatBaseActivity
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.chatLayoutManager.ChatAdapter
import com.pedrotlf.healthybot.messageTypes.BaseMessage
import com.pedrotlf.healthybot.messageTypes.MessageReceivedText
import com.pedrotlf.healthybot.messageTypes.MessageSent
import com.pedrotlf.healthybot.messageTypes.QuickReply
import kotlinx.android.synthetic.main.activity_chat_main.*

class ChatMainActivity : ChatBaseActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter

    private lateinit var dialog: Dialog

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        configureRecyclerView()
        configureInputText()

        dialog = Dialog(this)

        receiveInitialMessages()
    }

    fun insertMessageAtChat(message: BaseMessage, scroll: Boolean = false) {
        chatAdapter.addMessage(message)
        if (scroll)
            recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun configureInputText(){
        btn_send.setOnClickListener {
            val text: String = input_text.text.toString()

            if (text.isNotEmpty()) {
                input_text.setText("")

                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                insertMessageAtChat(MessageSent(text), true)

                //TODO make query
            }
        }
    }

    private fun receiveInitialMessages(){
        insertMessageAtChat(MessageReceivedText("Hi, user! Thanks for logging in. My name is Healthy Bot and I'm here to help you!"))

        val quickreplies: List<QuickReply> = listOf(
            QuickReply("Differential Diagnosis"){/*TODO*/},
            QuickReply("Patient Note"){notePopups.showPopupNotesMain(dialog)},
            QuickReply("Drug Information"){/*TODO*/},
            QuickReply("Pill Identifier"){/*TODO*/},
            QuickReply("Medical Calculator"){/*TODO*/},
            QuickReply("Laboratory Medicine"){/*TODO*/},
            QuickReply("Disease Library"){/*TODO*/}
        )

        insertMessageAtChat(MessageReceivedText("You can ask me anything about the following content:", quickreplies))
    }

    private fun configureRecyclerView() {
        recyclerView = findViewById(R.id.bot_recycler_view)
        chatAdapter = ChatAdapter(this)

        recyclerView.adapter = chatAdapter

        recyclerView.layoutManager = object: LinearLayoutManager(this){
            override fun smoothScrollToPosition(
                recyclerView: RecyclerView?,
                state: RecyclerView.State?,
                position: Int
            ) {
                val smoothScroller: LinearSmoothScroller = object : LinearSmoothScroller(this@ChatMainActivity){
                    override fun calculateSpeedPerPixel(displayMetrics: DisplayMetrics): Float {
                        return 300f / displayMetrics.densityDpi
                    }
                }
                smoothScroller.targetPosition = position
                startSmoothScroll(smoothScroller)
            }
        }

        recyclerView.addOnLayoutChangeListener{ _: View, _: Int, _: Int, _: Int, button: Int,
                                                _: Int, _: Int, _: Int, oldButton: Int ->
            if (button < oldButton) {
                recyclerView.postDelayed({
                    recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
                }, 75)

            }
        }
    }
}
