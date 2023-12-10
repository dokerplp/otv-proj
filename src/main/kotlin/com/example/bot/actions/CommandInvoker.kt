package com.example.bot.actions

import com.example.bot.actions.callbacks.Callback
import com.example.bot.actions.commands.Command
import com.example.bot.actions.commands.SendCommand
import org.springframework.context.ApplicationContext
import org.springframework.stereotype.Component
import org.telegram.telegrambots.meta.api.methods.PartialBotApiMethod
import org.telegram.telegrambots.meta.api.objects.Message

@Component
class CommandInvoker(
    context: ApplicationContext,
    val sendCommand: SendCommand
) {

    private val commands: Map<String, Command> = context.getBeansOfType(Command::class.java)
        .mapKeys { it.value.name() }

    private val callbacks: Map<String, Callback> = context.getBeansOfType(Callback::class.java)
        .mapKeys { it.value.name() }

    fun handle(data: CommandData): List<PartialBotApiMethod<Message>> {
        return if (commands.containsKey(data.commandName)) {
            commands[data.commandName]!!.handle(data)
        } else if (callbacks.containsKey(data.commandName)) {
            callbacks[data.commandName]!!.handle(data)
        } else {
            sendCommand.updateLast(data)
        }
    }
}