package com.example.bot.core

import com.example.bot.actions.CommandData
import com.example.bot.actions.CommandInvoker
import org.slf4j.Logger
import org.slf4j.LoggerFactory
import org.springframework.beans.factory.annotation.Value
import org.springframework.stereotype.Service
import org.telegram.telegrambots.bots.TelegramLongPollingBot
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.*
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import org.telegram.telegrambots.meta.exceptions.TelegramApiException

@Service
class Bot(
    @Value("\${telegram.token}")
    token: String,
    val commandInvoker: CommandInvoker
) : TelegramLongPollingBot(token) {

    private val logger: Logger = LoggerFactory.getLogger(Bot::class.java)

    @Value("\${telegram.botName}")
    private val botName: String = ""

    override fun getBotUsername(): String = botName

    override fun onUpdateReceived(update: Update) {
        val data = CommandData.of(update, this)
        commandInvoker.handle(data).forEach { run(it) }
    }

    fun run(msg: PartialBotApiMethod<Message>) {
        try {
            when (msg) {
                is SendSticker -> execute(msg)
                is SendMessage -> execute(msg)
                is SendAudio -> execute(msg)
                is SendAnimation -> execute(msg)
                is SendDice -> execute(msg)
                is SendPhoto -> execute(msg)
                is SendDocument -> execute(msg)
                is SendVideo -> execute(msg)
                is SendVoice -> execute(msg)
            }
        } catch (e: TelegramApiException) {
            logger.error("TelegramApiException", e)
        } catch (e: Exception) {
            logger.error("UnknownException", e)
        }
    }
}