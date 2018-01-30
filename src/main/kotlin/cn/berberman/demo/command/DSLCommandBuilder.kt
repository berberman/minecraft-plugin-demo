package cn.berberman.demo.command

import org.bukkit.command.CommandSender
import org.bukkit.entity.Player

class DSLCommandBuilder(val name: String) {
	var action: (CommandSender, String, Array<out String>) -> Boolean = { _, _, _ -> true }
	var description: String = ""
	var usageMessage: String = ""
	val aliases: MutableList<String> = mutableListOf()
	var result: Boolean = false
	fun action(block: (CommandSender, String, Array<out String>) -> Boolean) {
		action = block
	}

	fun addAlias(alias: String) = aliases.add(alias)

	fun playerOrNull(sender: CommandSender) = sender as? Player

	fun Player.operate(block: Player.() -> Boolean) = block()


	infix fun `return`(boolean: Boolean?) {
		result = boolean ?: false
	}
}

fun buildCommands(block: DSLCommandScope.() -> Unit) {
	DSLCommandScope().apply(block)
}

class DSLCommandScope {
	fun command(name: String, block: DSLCommandBuilder.() -> Unit) {
		DSLCommandBuilder(name).apply(block).apply {
			PackingCommand(this.name, description, usageMessage, aliases, action, result)
					.let(CommandHolder::add)
		}
	}
}