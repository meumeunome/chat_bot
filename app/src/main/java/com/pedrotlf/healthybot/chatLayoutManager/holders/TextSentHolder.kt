package com.pedrotlf.healthybot.chatLayoutManager.holders

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.messageTypes.BaseMessage
import com.pedrotlf.healthybot.messageTypes.MessageSent

class TextSentHolder(itemView: View) : RecyclerView.ViewHolder(itemView), HolderInterface {
    private var messageText: TextView = itemView.findViewById(R.id.message_text)

    override fun bind(message: BaseMessage) {
        message as MessageSent

        messageText.text = message.text
    }
}