package cn.berberman.demo.command

import org.bukkit.command.CommandSender

class DSLCommandBuilder(val name: String) {
	var action: (CommandSender, String, Array<out String>) -> Unit = { _, _, _ -> }
		private set
	var description: String = ""
	var usageMessage: String = ""
	val aliases: MutableList<String> = mutableListOf()
	var result: Boolean = true

	fun action(block: (CommandSender, String, Array<out String>) -> Unit) {
		action = block
	}

	fun action(block: (CommandSender) -> Unit) {
		action = { seder, _, _ -> block(seder) }
	}

	fun addAlias(alias: String) = aliases.add(alias)


	class TargetAndSenderBlocksData
	(private val senderInstance: CommandSender,
	 private val isTarget: Boolean,
	 private val result: Boolean) {
		infix fun otherwise(block: CommandSender.() -> Boolean) =
				if (!isTarget) senderInstance.block() else result
	}

	inline fun <reified T : CommandSender> whenSenderIs(sender: CommandSender, block: T.() -> Boolean) =
			(sender is T).let { isTarget ->
				TargetAndSenderBlocksData(sender, isTarget, if (isTarget)
					(sender as T).block() else false)
			}

}

fun buildCommands(block: DSLCommandScope.() -> Unit) {
	DSLCommandScope().apply(block)
}

class DSLCommandScope {
	//	private var exceptionHandler: (Thread, Throwable) -> Unit = { _, _ -> }
	fun command(name: String, block: DSLCommandBuilder.() -> Unit) {
		DSLCommandBuilder(name).apply(block).apply {
			PackingCommand(this.name, description, usageMessage, aliases, action, result/*, exceptionHandler*/)
					.let(CommandHolder::add)
		}
	}

}