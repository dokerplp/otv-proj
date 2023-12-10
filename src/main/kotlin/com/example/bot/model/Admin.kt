package com.example.bot.model

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "ADMINS")
class Admin {
    @Id
    @Column(name = "CHATID", nullable = false)
    var chatId: String = ""
}