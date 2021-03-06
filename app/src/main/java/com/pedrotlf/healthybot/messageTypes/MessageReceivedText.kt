package com.pedrotlf.healthybot.messageTypes

import com.pedrotlf.healthybot.Constants

class MessageReceivedText(val text: String, val quickReplies: List<QuickReply>? = null) : BaseMessage() {

    override fun getType(): Int {
        return Constants.MESSAGE_TYPE_RECEIVED
    }
}