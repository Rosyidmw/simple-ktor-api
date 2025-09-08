package org.study.rotibow.database

import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.study.rotibow.models.Users

object DatabaseFactory {
    fun init() {
        Database.connect("jdbc:sqlite:ktor_users.db", driver = "org.sqlite.JDBC")

        transaction {
            SchemaUtils.create(Users) // bikin tabel kalau belum ada
        }
    }
}
