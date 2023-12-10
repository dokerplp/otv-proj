package com.example.bot.repository

import com.example.bot.model.Admin
import org.springframework.data.repository.CrudRepository
import org.springframework.stereotype.Repository
import org.springframework.transaction.annotation.Transactional

@Repository
@Transactional(readOnly = true)
interface AdminRepository: CrudRepository<Admin, String>