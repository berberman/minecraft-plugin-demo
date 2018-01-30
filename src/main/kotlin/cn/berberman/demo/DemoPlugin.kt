package cn.berberman.demo

import cn.berberman.demo.command.CommandHolder
import cn.berberman.demo.event.EventHolder
import cn.berberman.demo.extension.asyncLoop
import cn.berberman.demo.extension.getCommandMap
import org.bukkit.event.HandlerList
import org.bukkit.plugin.java.JavaPlugin

class DemoPlugin : JavaPlugin() {
	override fun onLoad() {
		logger.info("插件加载")
	}

	override fun onEnable() {
		DemoLifeCycle.init()
		CommandHolder.register(getCommandMap())
		EventHolder.register()
		asyncLoop { DemoLifeCycle.loop();true }
		logger.info("插件启用")
	}

	override fun onDisable() {
		DemoLifeCycle.stop()
		HandlerList.unregisterAll()
		logger.info("插件禁用")
	}

	@Deprecated("未实现")
	private fun registerLifeCycle() {
//		val classes = ClassUtil.getClass()
////		val revolution=RevolutionClassUtil.getClassName("kotlin")
//		logger.info("raw")
//		logger.info(classes.joinToString())
////		logger.info(revolution.joinToString())
//		val resultClasses = classes.filter {
//			it.isAnnotationPresent(LifeCycle::class.java) &&
//					ClassUtils.isAssignable(it, ILifeCycle::class.java)
//		}
//		logger.info("result")
//		logger.info(resultClasses.joinToString())
	}
}