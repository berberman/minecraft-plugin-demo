package cn.berberman.demo.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DSLCommandBuilder(val name: String) {
	var action: (CommandSender, String, Array<out String>) -> Boolean = { _, _, _ -> true }
	var description: String = ""
	var usageMessage: String = ""
	var aliases: List<String> = emptyList()

	fun action(block: (CommandSender, String, Array<out String>) -> Boolean) {
		action = block
	}

	fun playerOrNull(sender: CommandSender) = sender as? Player
}

fun command(name: String, block: DSLCommandBuilder.() -> Unit) {
	DSLCommandBuilder(name).apply(block).apply {
		PackingCommand(this.name, description, usageMessage, aliases, action)
				.let(CommandHolder.commands::add)
	}
}
