package com.pedrotlf.healthybot.chatLayoutManager.holders

import android.app.Activity
import android.view.LayoutInflater
import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.RegistrationRuleBaseActivity
import com.pedrotlf.healthybot.messageTypes.BaseMessage
import com.pedrotlf.healthybot.messageTypes.MessageReceivedText

class TextReceivedHolder(itemView: View, private val activity: Activity) : RecyclerView.ViewHolder(itemView), HolderInterface {
    private val messageText: TextView = itemView.findViewById(R.id.message_text)
    private val quickReplyList: LinearLayout = itemView.findViewById(R.id.quickreply_list)

    override fun bind(message: BaseMessage) {
        message as MessageReceivedText

        messageText.text = message.text

        if(message.quickReplies != null){
            quickReplyList.removeAllViews()
            for (reply in message.quickReplies){
                val view = LayoutInflater.from(activity).inflate(R.layout.item_quickreply, quickReplyList, false)

                val textView: TextView = view.findViewById(R.id.text)

                textView.text = reply.text
                textView.setOnClickListener {
                    reply.function()
                }

                quickReplyList.addView(view)
            }
            quickReplyList.visibility = View.VISIBLE
        } else {
            quickReplyList.visibility = View.GONE
        }
    }
}