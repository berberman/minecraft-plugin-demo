package cn.berberman.demo

import cn.berberman.demo.command.buildCommands
import cn.berberman.demo.event.buildEvents
import cn.berberman.demo.extension.summonEntity
import cn.berberman.demo.extension.times
import cn.berberman.demo.lifeCycle.ILifeCycle
import org.bukkit.ChatColor
import org.bukkit.entity.LightningStrike
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerBedEnterEvent
import org.bukkit.event.player.PlayerJoinEvent

object DemoLifeCycle : ILifeCycle {
	private fun commands() {
		buildCommands {
			command("hi") {
				description = "向玩家发送\"hello\""
				usageMessage = "/hi或者/hello"
				addAlias("hello")

				action { sender, _, _ ->
					this `return`
							playerOrNull(sender)?.operate {
								sendMessage("你好啊！")
								giveExpLevels(1)
								true
							}
					sender.sendMessage("你似乎不是玩家")
					false
				}
			}
			command("console") {
				description = "向任何对象发送\"hello\""
				usageMessage = "/console"

				action { sender, _, _ ->
					sender.sendMessage("hello")
					true
				}
			}
			command("thunder") {
				action { sender, _, _ ->
					this `return`
							playerOrNull(sender)?.operate {
								val location = eyeLocation
								world.summonEntity<LightningStrike>(location)
								sendMessage(ChatColor.RED * "召唤成功($location)")
								true
							}
					false
				}
			}
		}
	}

	private fun events() {
		buildEvents {
			event<PlayerJoinEvent>(EventPriority.HIGH) {
				player.sendMessage("Hi!!!!!")
			}
			event<PlayerBedEnterEvent> {
				player.sendMessage(ChatColor.AQUA * "不许睡觉")
				isCancelled = true
			}
		}
	}

	override fun init() {
		commands()
		events()
	}

	override fun loop() {
	}

	override fun stop() {}
}