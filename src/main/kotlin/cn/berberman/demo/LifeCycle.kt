package cn.berberman.demo

import cn.berberman.demo.command.command

object LifeCycle {
	fun init() {
		command("hi") {
			action { sender, _, _ ->
				playerOrNull(sender)?.apply {
					sendMessage("你好啊！")
					giveExpLevels(1)
				}
				true
			}
		}
	}

	fun loop() {

	}

	fun stop() {}
}