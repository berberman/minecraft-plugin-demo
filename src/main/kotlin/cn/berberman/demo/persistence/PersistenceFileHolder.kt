package cn.berberman.demo.persistence

@Deprecated("未实现")
object PersistenceFileHolder {
	private val persistenceListFileList = mutableListOf<JsonListPersistenceFile<*>>()
	private val persistenceMapFileList = mutableListOf<JsonMapPersistenceFile<*, *>>()

	fun addList(jsonListPersistenceFile: JsonListPersistenceFile<*>) = persistenceListFileList.add(jsonListPersistenceFile)
	fun addMap(jsonMapPersistenceFile: JsonMapPersistenceFile<*, *>) = persistenceMapFileList.add(jsonMapPersistenceFile)

	fun load() {
		persistenceListFileList.forEach(JsonListPersistenceFile<*>::load)
		persistenceMapFileList.forEach(JsonMapPersistenceFile<*, *>::load)

	}

	fun save() {
		persistenceListFileList.forEach(JsonListPersistenceFile<*>::save)
		persistenceMapFileList.forEach(JsonMapPersistenceFile<*, *>::save)

	}
}