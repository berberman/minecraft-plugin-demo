package cn.berberman.demo

import cn.berberman.demo.extension.plugin
import cn.berberman.demo.extension.pluginManager
import cn.berberman.demo.extension.sendMessage
import cn.berberman.demo.extension.times
import org.bukkit.ChatColor
import org.bukkit.event.Event
import org.bukkit.event.EventHandler
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener
import org.bukkit.event.player.PlayerJoinEvent

object DemoListener : Listener {
	@EventHandler
	fun onPlayJoin(event: PlayerJoinEvent) {
		event.player sendMessage ChatColor.AQUA * "Hi!"
	}

	init {
		pluginManager.registerEvent(PlayerJoinEvent::class.java, object : Listener {},
				EventPriority.NORMAL,
				{ _: Listener, e: Event -> (e as PlayerJoinEvent).player sendMessage "你好！" },
				plugin
		)
	}
}