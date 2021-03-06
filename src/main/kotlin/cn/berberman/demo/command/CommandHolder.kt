package cn.berberman.demo.command

import cn.berberman.demo.extension.logger
import org.bukkit.command.Command
import org.bukkit.command.CommandMap

object CommandHolder {
	private val commands = mutableListOf<PackingCommand>()

	fun add(packingCommand: PackingCommand) = commands.add(packingCommand)

	fun register(commandMap: CommandMap) {
		commands.forEach {
			commandMap.register("DemoPlugin", it as Command)
			logger.info("注册命令：/${it.name}")
		}
	}
}