package com.example.bot.actions.commands

import com.example.bot.service.AdminService
import com.example.bot.service.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
abstract class AdminCommand(
    val adminService: AdminService
): Command {
    fun notEnoughRightsMessage(chatId: String): List<PartialBotApiMethod<Message>> {
        return listOf(sendMessage(chatId, "Not enough rights to run this command"))
    }

    fun runIfAdmin(userId: String, runnable: () -> List<PartialBotApiMethod<Message>>): List<PartialBotApiMethod<Message>> {
        return if (adminService.isAdmin(userId)) {
            runnable.invoke()
        } else {
            notEnoughRightsMessage(userId)
        }
    }
}