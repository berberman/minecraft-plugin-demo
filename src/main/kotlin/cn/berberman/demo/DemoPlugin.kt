package cn.berberman.demo

import cn.berberman.demo.command.CommandHolder
import cn.berberman.demo.event.EventHolder
import cn.berberman.demo.extension.getCommandMap
import cn.berberman.demo.permission.PermissionHolder
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class DemoPlugin : JavaPlugin() {
	override fun onLoad() {
		logger.info("插件已加载")
	}

	override fun onEnable() {
		if (!dataFolder.exists()) dataFolder.mkdir()
		DemoLifeCycle.onEnable()
		CommandHolder.register(getCommandMap())
		EventHolder.register()
		PermissionHolder.register()
		logger.info("插件已启用")
	}

	override fun onDisable() {
		DemoLifeCycle.onDisable()
		HandlerList.unregisterAll()
		logger.info("插件已禁用")
	}

}
