package cn.berberman.demo.command

import org.bukkit.command.Command
import org.bukkit.command.CommandMap

object CommandHolder {
	val commands = mutableListOf<PackingCommand>()

	fun register(commandMap: CommandMap) {
		commands.forEach {
			commandMap.register("DemoPlugin", it as Command)
		}
	}
}