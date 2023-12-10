package com.example.bot.actions.commands

import com.example.bot.actions.CommandData
import com.example.bot.service.AdminService
import com.example.bot.service.RequestService
import com.example.bot.service.sendMessage
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class AdminShowActiveCommand(
    val requestService: RequestService,
    adminService: AdminService
) : AdminCommand(adminService) {
    override fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        return runIfAdmin(data.chatId) {
            requestService.adminShowActiveRequests(data).ifEmpty {
                listOf(sendMessage(data.chatId, "Nothing found"))
            }
        }
    }

    override fun help(): String {
        return "Review confirmed applications"
    }

    override fun name(): String {
        return "/show_active"
    }
}