package cn.berberman.demo.command

import org.bukkit.command.Command
import org.bukkit.command.CommandSender

class PackingCommand
(name: String,
 description: String,
 usageMessage: String,
 aliases: List<String>,
 private val action: (CommandSender, String, Array<out String>) -> Boolean,
 private val result: Boolean/*,
 private val exceptionHandler: (Thread, Throwable) -> Unit*/
) : Command(name,
		description,
		usageMessage,
		aliases)
//Command(name,
//		description.takeIf { !it.isEmpty() },
//		usageMessage.takeIf { !it.isEmpty() },
//		aliases.takeIf { it.isNotEmpty() })
{
	override fun execute(p0: CommandSender, p1: String, p: Array<out String>): Boolean {
//		if (exceptionHandler != { _: Thread, _: Throwable -> })
//			Thread.currentThread().setUncaughtExceptionHandler { t, e ->
//				exceptionHandler(t, e)
//			}
//		Thread.setDefaultUncaughtExceptionHandler{t, e -> exceptionHandler(t,e) }
		//TODO 为啥抢不到未检异常处理器
		return result || action(p0, p1, p)
	}

}