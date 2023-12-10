package com.example.bot.actions.commands

import com.example.bot.actions.CommandData
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

interface Command {
    fun handle(data: CommandData): List<PartialBotApiMethod<Message>>
    fun help(): String
    fun name(): String
}