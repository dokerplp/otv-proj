package com.example.bot.actions.callbacks

import com.example.bot.actions.CommandData
import com.example.bot.service.RequestService
import com.example.bot.service.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.*

@Component
class AcceptCallback(
    val requestService: RequestService
): Callback {
    override fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        requestService.acceptRequest(UUID.fromString(data.text))
        return listOf(sendMessage(data.chatId, "Request accepted"))
    }

    override fun name(): String {
        return "/accept"
    }
}