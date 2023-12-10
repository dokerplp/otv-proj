package com.example.bot.service

import com.example.bot.actions.CommandData
import com.example.bot.cloud.S3
import com.example.bot.model.Request
import com.example.bot.model.RequestFile
import com.example.bot.model.Status
import com.example.bot.repository.RequestRepository
import org.springframework.stereotype.Service
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message
import java.util.*


@Service
class RequestService(
    val requestRepository: RequestRepository
) {

    fun updateFiles(request: Request, data: CommandData): List<RequestFile> {
        val files = ArrayList(request.files)
        if (data.file != null) {
            S3.putObject(data.file!!, data.fileFormat).ifPresent { key ->
                files.add(RequestFile.of(key, request))
            }
        }
        return files
    }

    fun createRequest(data: CommandData) {
        val request = Request()
        request.id = UUID.randomUUID()
        request.text = data.text
        request.chatId = data.chatId
        request.files = updateFiles(request, data)
        requestRepository.save(request)
    }

    fun updateLastRequest(data: CommandData) {
        requestRepository.findTopByChatIdOrderByCreatedAtDesc(data.chatId).ifPresent { request ->
            request.files = updateFiles(request, data)
            requestRepository.save(request)
        }
    }

    fun closeRequest(requestId: UUID) {
        changeStatus(requestId, Status.CLOSED)
    }

    fun declineRequest(requestId: UUID) {
        changeStatus(requestId, Status.DECLINED)
    }

    fun acceptRequest(requestId: UUID) {
        changeStatus(requestId, Status.ACCEPTED)
    }

    fun cancelRequest(requestId: UUID) {
        changeStatus(requestId, Status.CANCELED)
    }

    fun changeStatus(requestId: UUID, status: Status) {
        val request = requestRepository.findById(requestId)
            .orElseThrow { IllegalArgumentException("Request with id $requestId not found") }
        request.status = status
        requestRepository.save(request)
    }

    fun showUserRequests(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestRepository.findAllByChatId(data.chatId)
            .map { showMyMessage(it) }
    }

    fun showFullRequest(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestRepository.findById(UUID.fromString(data.text)).map {
            showFullMessage(it)
        }.orElse(listOf())
    }

    fun adminShowNewRequests(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestRepository.findAllByStatus(Status.NEW)
            .map { adminShowNewMessage(it) }
    }

    fun adminShowActiveRequests(data: CommandData): List<PartialBotApiMethod<Message>> {
        return requestRepository.findAllByStatus(Status.ACCEPTED)
            .map { adminShowActiveMessage(it) }
    }

    

}