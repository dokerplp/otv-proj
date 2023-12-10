package com.example.bot.actions.commands

import com.example.bot.actions.CommandData
import com.example.bot.service.RequestService
import com.example.bot.service.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class SendCommand(
    val requestService: RequestService
): Command {
    override fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        requestService.createRequest(data)
        return listOf(
            sendMessage(data.chatId, "Your request has been registered, use the command /show_my to track its status.")
        )
    }

    fun updateLast(data: CommandData): List<PartialBotApiMethod<Message>> {
        requestService.updateLastRequest(data)
        return listOf()
    }

    override fun help(): String {
        return "Send request"
    }

    override fun name(): String {
        return "/send"
    }
}