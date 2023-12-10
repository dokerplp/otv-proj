package com.example.bot.model

import jakarta.persistence.*

@Entity
@Table(name = "REQUESTFILES")
class RequestFile {
    @Id
    @Column(name = "ID", nullable = false)
    var id: String = ""

    @ManyToOne
    @JoinColumn(name = "REQUESTID")
    var request: Request? = null

    companion object {
        fun of(key: String, request: Request): RequestFile {
            val requestFile = RequestFile()
            requestFile.id = key
            requestFile.request = request
            return requestFile
        }
    }
}