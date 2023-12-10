package com.example.bot.actions.commands

import com.example.bot.actions.CommandData
import com.example.bot.service.RequestService
import com.example.bot.service.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class ShowMyCommand(
    val requestService: RequestService
): Command {
    override fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestService.showUserRequests(data).ifEmpty {
            listOf(sendMessage(data.chatId, "Nothing found"))
        }
    }

    override fun help(): String {
        return "Show your requests"
    }

    override fun name(): String {
        return "/show_my"
    }
}