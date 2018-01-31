package cn.berberman.demo.persistence

import cn.berberman.demo.extension.plugin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.Serializable

@Deprecated("未实现")
class JsonListPersistenceFile<T : Serializable>(name: String) {
	private val file = File(plugin.dataFolder.path, name)
	val data = mutableListOf<T>()

	init {
		if (!file.exists()) file.createNewFile()
		let(PersistenceFileHolder::addList)
	}

	fun load() {
		data.addAll(Gson().fromJson(file.readText(), object : TypeToken<List<T>>() {}.type))
	}

	fun save() {
		file.writeText(Gson().toJson(data))
	}
}