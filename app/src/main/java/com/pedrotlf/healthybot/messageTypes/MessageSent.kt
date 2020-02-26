package com.pedrotlf.healthybot.messageTypes

import com.pedrotlf.healthybot.Constants

class MessageSent(val text: String) : BaseMessage() {

    override fun getType(): Int {
        return Constants.MESSAGE_TYPE_SENT
    }
}