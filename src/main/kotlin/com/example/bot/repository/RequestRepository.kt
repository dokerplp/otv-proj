package com.example.bot.repository

import com.example.bot.model.Request
import com.example.bot.model.Status
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional
import java.util.Optional
import java.util.UUID

@Repository
@Transactional(readOnly = true)
interface RequestRepository: CrudRepository<Request, UUID> {
    fun findAllByChatId(chatId: String): List<Request>

    fun findTopByChatIdOrderByCreatedAtDesc(chatId: String): Optional<Request>

    fun findAllByStatus(status: Status): List<Request>
}