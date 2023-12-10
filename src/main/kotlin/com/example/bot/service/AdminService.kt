package com.example.bot.service

import com.example.bot.repository.AdminRepository
import org.springframework.stereotype.Service

@Service
class AdminService(
    val adminRepository: AdminRepository
) {
    fun getAllAdminIds(): Set<String> {
        return adminRepository.findAll().map { it.chatId }.toSet()
    }

    fun isAdmin(chatId: String): Boolean {
        return chatId in getAllAdminIds()
    }
}