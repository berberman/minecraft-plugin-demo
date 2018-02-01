package cn.berberman.demo

import cn.berberman.demo.command.buildCommands
import cn.berberman.demo.event.buildEvents
import cn.berberman.demo.extension.*
import cn.berberman.demo.lifeCycle.ILifeCycle
import cn.berberman.demo.permission.permission
import org.apache.commons.lang3.RandomUtils
import org.bukkit.*
import org.bukkit.attribute.Attribute
import org.bukkit.command.ConsoleCommandSender
import org.bukkit.enchantments.Enchantment
import org.bukkit.entity.EntityType
import org.bukkit.entity.Pig
import org.bukkit.entity.Player
import org.bukkit.event.EventPriority
import org.bukkit.event.player.PlayerBedLeaveEvent
import org.bukkit.event.player.PlayerDropItemEvent
import org.bukkit.event.player.PlayerFishEvent
import org.bukkit.event.player.PlayerJoinEvent
import org.bukkit.inventory.ItemStack
import org.bukkit.permissions.PermissionDefault

object DemoLifeCycle : ILifeCycle {
	private fun registerCommands() {
		buildCommands {
			command("init") {
				action { sender ->
					sender.hasPermission("init").takeIf { it }?.let {
						Bukkit.getWorlds().flatMap { it.players }.forEach {
							it.inventory.clear()
							it.gameMode = GameMode.SURVIVAL
							it.healthScale = 80.0
						}
					}
				}
			}
			command("cure") {
				action { sender ->
					whenSenderIs<Player>(sender) {
						if (!hasPermission("cheat")) return@whenSenderIs false
						player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue
						player.sendTitle(ChatColor.GREEN * "恢满了~", "", 10, 70, 20)
						true
					}
				}
			}
			command("full") {
				action { sender ->
					whenSenderIs<Player>(sender) {
						if (!hasPermission("cheat")) return@whenSenderIs false
						foodLevel = 20
						player.sendTitle(ChatColor.GREEN * "~", "", 10, 70, 20)
						true
					}
				}
			}
			command("cheat") {
				action { sender, _, args ->
					whenSenderIs<ConsoleCommandSender>(sender) {
						if (args.size != 1) return@whenSenderIs false
						val player: Player = Bukkit.getPlayer(args[0]) ?: return@whenSenderIs false
						pluginManager.subscribeToPermission("cheat", player)
						true
					}
				}
			}
			command("respawn") {
				action { sender ->
					whenSenderIs<Player>(sender) {
						teleport(bedSpawnLocation)
						true
					}
				}
			}
			command("set") {
				action { sender, _, args ->
					whenSenderIs<Player>(sender) {
						if (args.size == 3) {
							val x = args[0].toDouble()
							val y = args[1].toDouble()
							val z = args[2].toDouble()
							setBedSpawnLocation(Location(world, x, y, z), true)
							sendMessage(ChatColor.BLUE * "设定坐标$bedSpawnLocation")
							return@whenSenderIs true
						}
						if (args.isEmpty()) {
							setBedSpawnLocation(location, true)
							sendMessage(ChatColor.BLUE * "设定坐标$bedSpawnLocation")
							return@whenSenderIs true
						}
						false
					}
				}
			}
		}
	}

	private fun registerEvents() {
		/**
		 * 欢迎消息
		 */
		buildEvents {
			event<PlayerJoinEvent>(EventPriority.HIGH) {
				player sendMessage ChatColor.GREEN * "欢迎加入!~"
			}
			/**
			 * 玩家会有20%的几率钓上一只猪
			 */
			event<PlayerFishEvent> {
				if (RandomUtils.nextInt(1, 100) > 20) return@event
				async {
					while (caught == null) {
					}
					runOnServerThread {
						val pig = player.world.spawnEntity(caught.location, EntityType.PIG) as Pig
						pig.velocity = pig.velocity.subtract(player.location.direction.multiply(0.8))
					}
					caught.remove()
				}
			}
			/**
			 * 消耗床来恢复满状态
			 */
			event<PlayerBedLeaveEvent> {
				if (!(0..50).contains(player.world.time)) return@event
				player.sendTitle(ChatColor.YELLOW * "消耗一张床", "", 10, 70, 20)
				player.giveExpLevels(5)
				player.health = player.getAttribute(Attribute.GENERIC_MAX_HEALTH).baseValue
				player.foodLevel = 20
				player.world.spawnParticle(Particle.EXPLOSION_LARGE, bed.location, 50)
				bed.type = Material.AIR
			}
			/**
			 * 通过丢东西来兑换
			 */
			event<PlayerDropItemEvent> {
				itemDrop.itemStack.let {
					if (it.amount == 64 && it.type == Material.COAL) {
						player.sendMessage(ChatColor.GREEN * "兑换！")
						player.world.dropItem(itemDrop.location, ItemStack(Material.DIAMOND))
						itemDrop.remove()
					}
					if (it.amount == 5 && it.type == Material.GOLD_INGOT) {
						player.sendMessage(ChatColor.GREEN * "兑换！")
						player.world.dropItem(itemDrop.location, ItemStack(Material.LAPIS_BLOCK))
						itemDrop.remove()
					}
					if (it.itemMeta.displayName?.toIntOrNull() != null && it.type == Material.DIAMOND_PICKAXE) {
						player.sendMessage(ChatColor.GREEN * "兑换！")
						player.world.dropItem(itemDrop.location, ItemStack(Material.DIAMOND_PICKAXE).apply {
							val level = it.itemMeta.displayName.toInt()
							addUnsafeEnchantment(Enchantment.LOOT_BONUS_BLOCKS, level)
							addUnsafeEnchantment(Enchantment.FIRE_ASPECT, level)
							addUnsafeEnchantment(Enchantment.DAMAGE_ALL, level)
							addUnsafeEnchantment(Enchantment.DIG_SPEED, level)
							addUnsafeEnchantment(Enchantment.LOOT_BONUS_MOBS, level)
						})
						itemDrop.remove()
					}
				}
			}
		}
	}

	override fun onEnable() {

		permission("init") {
			description = "初始化玩家"
			defaultValue = PermissionDefault.FALSE
		}
		permission("cheat") {
			defaultValue = PermissionDefault.FALSE
		}
		registerCommands()
		registerEvents()
	}


	override fun onDisable() {}
}