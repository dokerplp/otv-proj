package com.example.bot.actions.callbacks

import com.example.bot.actions.CommandData
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

interface Callback {
    fun handle(data: CommandData): List<PartialBotApiMethod<Message>>
    fun name(): String
}