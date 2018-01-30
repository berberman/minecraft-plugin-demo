package cn.berberman.demo.event

import cn.berberman.demo.extension.plugin
import cn.berberman.demo.extension.pluginManager
import org.bukkit.event.Event
import org.bukkit.event.EventPriority
import org.bukkit.event.Listener

data class PackingEvent<in T : Event>(private val type: Class<out Event>, private val eventPriority: EventPriority, private val block: (T) -> Unit) {
	@Suppress("UNCHECKED_CAST")
	fun register() {
		pluginManager.registerEvent(type, object : Listener {}, eventPriority,
				{ _: Listener, event ->
					block(event as T)
				}, plugin)
	}
}