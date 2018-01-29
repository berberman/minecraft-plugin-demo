package cn.berberman.demo.extension

import cn.berberman.demo.DemoPlugin
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable

fun asyncLoop(block: () -> Boolean) {
	object : BukkitRunnable() {
		override fun run() {
			var result = block()
			while (result)
				result = block()
			this.cancel()
		}
	}.runTaskAsynchronously(JavaPlugin.getPlugin(DemoPlugin::class.java))
}

fun getCommandMap(): CommandMap =
		Bukkit.getServer().let {
			it::class.java.declaredMethods.firstOrNull { it.name == "getCommandMap" }
					?.invoke(it, null) as CommandMap
		}