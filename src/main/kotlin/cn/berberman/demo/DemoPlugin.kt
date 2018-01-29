package cn.berberman.demo

import cn.berberman.demo.command.CommandHolder
import cn.berberman.demo.extension.asyncLoop
import org.bukkit.Bukkit
import org.bukkit.command.CommandMap
import org.bukkit.plugin.java.JavaPlugin

class DemoPlugin : JavaPlugin() {
	override fun onLoad() {
		logger.info("插件加载")
	}

	override fun onEnable() {
		LifeCycle.init()
		asyncLoop { LifeCycle.loop();true }
		CommandHolder.register(getCommandMap())
		logger.info("插件启用")
	}

	override fun onDisable() {
		LifeCycle.stop()
		logger.info("插件禁用")
	}

	private fun getCommandMap(): CommandMap =
			Bukkit.getServer().let {
				it::class.java.declaredMethods.firstOrNull { it.name == "getCommandMap" }
						?.invoke(it, null) as CommandMap
			}
}