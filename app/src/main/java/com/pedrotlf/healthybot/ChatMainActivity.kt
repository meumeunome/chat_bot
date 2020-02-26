package com.pedrotlf.healthybot

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.DisplayMetrics
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.LinearSmoothScroller
import androidx.recyclerview.widget.RecyclerView
import com.pedrotlf.healthybot.chatLayoutManager.ChatAdapter
import com.pedrotlf.healthybot.messageTypes.BaseMessage
import com.pedrotlf.healthybot.messageTypes.MessageReceivedText
import com.pedrotlf.healthybot.messageTypes.MessageSent
import kotlinx.android.synthetic.main.activity_chat_main.*

class ChatMainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    lateinit var chatAdapter: ChatAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chat_main)

        configureRecyclerView()
        configureInputText()

        receiveInitialMessages()
    }

    fun insertMessageAtChat(message: BaseMessage) {
        chatAdapter.addMessage(message)
        recyclerView.scrollToPosition(chatAdapter.itemCount - 1)
    }

    private fun configureInputText(){
        btn_send.setOnClickListener {
            val text: String = input_text.text.toString()

            if (text.isNotEmpty()) {
                input_text.setText("")

                val inputManager: InputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
                inputManager.hideSoftInputFromWindow(currentFocus?.windowToken, InputMethodManager.SHOW_FORCED)

                insertMessageAtChat(
                    MessageSent(
                        text
                    )
                )

                //TODO make query
            }
        }
    }

    private fun receiveInitialMessages(){
        insertMessageAtChat(MessageReceivedText("Hi, user! Thanks for logging in. My name is Healthy Bot and I'm here to help you!"))
    }

    private fun configureRecyclerView() {
        recyclerView = findViewById(R.id.bot_recycler_view)
        chatAdapter = ChatAdapter()

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
