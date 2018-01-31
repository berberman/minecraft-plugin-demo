@file:Suppress("UNCHECKED_CAST")

package cn.berberman.demo.extension

import cn.berberman.demo.DemoPlugin
import org.bukkit.Bukkit
import org.bukkit.ChatColor
import org.bukkit.Location
import org.bukkit.World
import org.bukkit.command.CommandMap
import org.bukkit.command.CommandSender
import org.bukkit.entity.Entity
import org.bukkit.event.Listener
import org.bukkit.plugin.PluginManager
import org.bukkit.plugin.java.JavaPlugin
import org.bukkit.scheduler.BukkitRunnable
import java.util.logging.Logger

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
					?.invoke(it) as CommandMap
		}

operator fun ChatColor.times(s: String) =
		toString() + s

inline fun <reified T : Entity> World.summonEntity(location: Location) =
		spawn(location, T::class.java) as T

val logger: Logger = Bukkit.getLogger()

val plugin: DemoPlugin = JavaPlugin.getPlugin(DemoPlugin::class.java)

val pluginManager: PluginManager = Bukkit.getPluginManager()

infix fun CommandSender.sendMessage(message: String) = sendMessage(message)

val emptyListener = object : Listener {}