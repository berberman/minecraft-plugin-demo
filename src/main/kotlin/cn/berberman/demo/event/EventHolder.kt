package cn.berberman.demo.event


object EventHolder {
	private val events = mutableListOf<PackingEvent<*>>()

	fun add(packingEvent: PackingEvent<*>) = events.add(packingEvent)
	fun register() =
			events.forEach(PackingEvent<*>::register)

}