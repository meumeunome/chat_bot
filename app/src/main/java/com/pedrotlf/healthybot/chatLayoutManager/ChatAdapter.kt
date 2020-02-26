package com.pedrotlf.healthybot.chatLayoutManager

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.pedrotlf.healthybot.Constants
import com.pedrotlf.healthybot.R
import com.pedrotlf.healthybot.chatLayoutManager.holders.HolderInterface
import com.pedrotlf.healthybot.chatLayoutManager.holders.TextReceivedHolder
import com.pedrotlf.healthybot.chatLayoutManager.holders.TextSentHolder
import com.pedrotlf.healthybot.messageTypes.BaseMessage

class ChatAdapter() : RecyclerView.Adapter<RecyclerView.ViewHolder>()  {

    private val mMessageList: ArrayList<BaseMessage> = ArrayList()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val view: View

        return when(viewType){
            Constants.MESSAGE_TYPE_RECEIVED -> {
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_received, parent, false)
                TextReceivedHolder(view)
            }

            else -> { /*MessageType.SENT*/
                view = LayoutInflater.from(parent.context)
                    .inflate(R.layout.item_message_sent, parent, false)

                TextSentHolder(view)
            }
        }
    }

    override fun getItemCount(): Int {
        return mMessageList.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message: BaseMessage = mMessageList[position]

        (holder as HolderInterface).bind(message)
    }

    override fun getItemViewType(position: Int): Int {
        val message: BaseMessage = mMessageList[position]

        return message.getType()
    }

    fun addMessage(message: BaseMessage){
        mMessageList.add(message)

        notifyItemInserted(mMessageList.size)
    }

//    fun insertTypingAnimation(){
//        if(mMessageList.isEmpty() || mMessageList.last() !is TypingLayoutMessage) {
//            mMessageList.add(TypingLayoutMessage())
//            activity.runOnUiThread {
//                notifyItemInserted(mMessageList.size)
//            }
//        }
//    }
//
//    fun removeTypingAnimation(){
//        if(mMessageList.isNotEmpty() && mMessageList.last() is TypingLayoutMessage)
//            if(mMessageList.removeAll { it is TypingLayoutMessage })
//                notifyDataSetChanged()
//    }

}