package com.example.bot.actions

import com.example.bot.core.Bot
import org.telegram.telegrambots.meta.api.methods.GetFile
import org.telegram.telegrambots.meta.api.objects.CallbackQuery
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.Update
import java.io.File

data class CommandData (
    val chatId: String,
    val commandName: String,
    val text: String,
    var file: File?,
    var fileFormat: String
) {
    companion object {
        private fun downloadFile(fid: String, bot: Bot): Pair<File, String> {
            val getFile = GetFile()
            getFile.fileId = fid
            val filePath = bot.execute(getFile).filePath
            val file = bot.downloadFile(filePath)
            val fileFormat = filePath.replaceBeforeLast(".", "")
            return Pair(file, fileFormat)
        }

        fun of(message: Message, bot: Bot): CommandData {
            if (!message.hasText()) {
                message.text = message.caption
            }

            val commandName = message.text?.split(" ")?.get(0) ?: ""
            val text = message.text?.replace(commandName, "")?.trim() ?: ""

            val (file, format) = if (message.hasPhoto()) {
                downloadFile(message.photo[3].fileId, bot)
            } else if (message.hasVideo()) {
                downloadFile(message.video.fileId, bot)
            } else {
                Pair(null, "")
            }

            return CommandData(message.chatId.toString(), commandName, text, file, format)
        }

        fun of(callback: CallbackQuery): CommandData {
            val commandName = callback.data?.split(" ")?.get(0) ?: ""
            val text = callback.data?.replace(commandName, "")?.trim() ?: ""

            return CommandData(callback.message.chatId.toString(), commandName, text, null, "")
        }

        fun of(update: Update, bot: Bot): CommandData {
            return if (update.hasMessage()) {
                of(update.message, bot)
            } else {
                of(update.callbackQuery)
            }
        }
    }
}