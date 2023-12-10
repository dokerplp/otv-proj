package com.example.bot.service

import com.example.bot.cloud.S3
import com.example.bot.model.Request
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.methods.send.SendDocument
import org.telegram.telegrambots.meta.api.methods.send.SendMessage
import org.telegram.telegrambots.meta.api.objects.InputFile
import org.telegram.telegrambots.meta.api.objects.Message
import org.telegram.telegrambots.meta.api.objects.replykeyboard.InlineKeyboardMarkup
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton
import java.io.InputStream


fun button(text: String, callback: String): InlineKeyboardButton {
    val button = InlineKeyboardButton()
    button.text = text
    button.callbackData = callback
    return button
}

fun showFullMessage(request: Request): List<PartialBotApiMethod<Message>> {
    val msg = sendMessage(
        request.chatId, """
        <b>Text:</b>
        ${request.text}
        
        <b>Status:</b>        <code>${request.status}</code>
        <b>Created at:</b> <code>${request.getFormattedTime()}</code>
    """.trimIndent()
    )
    msg.enableHtml(true)

    val files = S3.getObjects(request.files.map { it.id })
        .map { sendDocument(request.chatId, it.objectContent, it.key) }

    return listOf(listOf(msg), files).flatten()
}

fun showMessage(request: Request): SendMessage {
    val msg = sendMessage(
        request.chatId, """
        <b>Title:</b>           <i>${request.text.take(20)}</i>
        <b>Status:</b>        <code>${request.status}</code>
        <b>Created at:</b> <code>${request.getFormattedTime()}</code>
            """.trimIndent()
    )
    msg.enableHtml(true)
    return msg
}

fun showMyMessage(request: Request): SendMessage {
    val msg = showMessage(request)
    val buttons: List<List<InlineKeyboardButton>> = listOf(
        listOf(
            button("⬇", "/full ${request.id}"),
            button("❌", "/cancel ${request.id}")
        )
    )
    msg.replyMarkup = InlineKeyboardMarkup(buttons)
    return msg
}

fun adminShowNewMessage(request: Request): SendMessage {
    val msg = showMessage(request)
    val buttons: List<List<InlineKeyboardButton>> = listOf(
        listOf(
            button("⬇", "/full ${request.id}"),
            button("✅", "/accept ${request.id}"),
            button("❌", "/decline ${request.id}"),
        )
    )
    msg.replyMarkup = InlineKeyboardMarkup(buttons)
    return msg
}

fun adminShowActiveMessage(request: Request): SendMessage {
    val msg = showMessage(request)
    val buttons: List<List<InlineKeyboardButton>> = listOf(
        listOf(
            button("⬇", "/full ${request.id}"),
            button("✅", "/close ${request.id}"),
        )
    )
    msg.replyMarkup = InlineKeyboardMarkup(buttons)
    return msg
}

fun sendMessage(chatId: String, text: String): SendMessage {
    val send = SendMessage()
    send.chatId = chatId
    send.text = text
    return send
}

fun sendDocument(chatId: String, inputStream: InputStream, fileName: String): SendDocument {
    val send = SendDocument()
    send.chatId = chatId
    send.document = InputFile(inputStream, fileName)
    return send
}