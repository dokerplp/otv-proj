package com.example.bot.actions.callbacks

import com.example.bot.actions.CommandData
import com.example.bot.service.RequestService
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class ShowFullCallback(
    val requestService: RequestService
): Callback {
    override fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestService.showFullRequest(data)
    }

    override fun name(): String {
        return "/full"
    }
}