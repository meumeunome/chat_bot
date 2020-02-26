package com.pedrotlf.healthybot.chatLayoutManager.holders

import com.pedrotlf.healthybot.messageTypes.BaseMessage

interface HolderInterface {

    fun bind(message: BaseMessage)
}