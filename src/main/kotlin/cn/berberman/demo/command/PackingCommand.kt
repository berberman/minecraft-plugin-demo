package cn.berberman.demo.command

import cn.berberman.demo.extension.sendMessage
import cn.berberman.demo.extension.times
import org.bukkit.ChatColor
import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PackingCommand
(name: String,
 description: String,
 usageMessage: String,
 aliases: List<String>,
 private val action: (CommandSender, String, Array<out String>) -> Unit,
 private val result: Boolean
) : Command(name,
		description,
		usageMessage,
		aliases) {
	override fun execute(p0: CommandSender, p1: String, p: Array<out String>) =
			result.apply {
				if (!this) p0 sendMessage ChatColor.RED * "命令执行错误"
				action(p0, p1, p)
			}

}