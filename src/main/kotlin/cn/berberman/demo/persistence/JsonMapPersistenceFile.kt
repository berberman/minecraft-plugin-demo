package cn.berberman.demo.persistence

import cn.berberman.demo.extension.logger
import cn.berberman.demo.extension.plugin
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import java.io.File
import java.io.Serializable

@Deprecated("未实现")
class JsonMapPersistenceFile<T : Serializable, R : Serializable>(name: String) {
	private val file = File(plugin.dataFolder, name + ".json")
	val data = mutableMapOf<T, R>()

	init {
		if (!file.exists()) file.createNewFile()
		load()
	}

	fun load() {
//		data.putAll(Gson().fromJson(file.readText(), object : TypeToken<Map<T, R>>() {}.type))
		Gson().fromJson<Map<T,R>>(file.readText(), object : TypeToken<Map<T, R>>() {}.type)?.let(data::putAll)
	}

	fun save() {
		val result = Gson().toJson(data/*, object : TypeToken<Map<T, R>>() {}.type*/)
		logger.info("result=$result")
		file.writeText(result)
	}
}