package com.example.bot.model

import jakarta.persistence.*
import java.time.Instant
import java.time.ZoneId
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter
import java.time.format.FormatStyle
import java.util.*

@Entity
@Table(name = "REQUESTS")
class Request {

    @Id
    @GeneratedValue(strategy = GenerationType.UUID)
    @Column(name = "ID", nullable = false)
    var id: UUID = UUID.randomUUID()

    @Column(name = "CHATID", nullable = false)
    var chatId: String = ""

    @Enumerated(EnumType.STRING)
    @Column(name = "STATUS", nullable = false)
    var status: Status = Status.NEW

    @Column(name = "TEXT", nullable = false, length = 512)
    var text: String = ""

    @Column(name = "CREATEDAT", nullable = false)
    var createdAt: Instant = Instant.now()

    @OneToMany(mappedBy = "request", cascade = [CascadeType.ALL], fetch = FetchType.EAGER)
    var files: List<RequestFile> = ArrayList()

    fun getFormattedTime(): String {
        return DateTimeFormatter.ofLocalizedDateTime(FormatStyle.MEDIUM)
            .format(ZonedDateTime.ofInstant(createdAt, ZoneId.systemDefault()))
    }
}