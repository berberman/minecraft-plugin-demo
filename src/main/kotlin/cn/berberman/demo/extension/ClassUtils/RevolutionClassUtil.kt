package cn.berberman.demo.extension.ClassUtils

import cn.berberman.demo.DemoPlugin
import java.io.File

@Deprecated("蜜汁")
object RevolutionClassUtil {
	private fun getClassName(filePath: String, className: MutableList<String>): List<String> {
		val file = File(filePath)
		val childFiles = file.listFiles()
		for (childFile in childFiles!!) {
			if (childFile.isDirectory) {
				className.addAll(getClassName(childFile.path, className))
			} else {
				var childFilePath = childFile.path
				childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf("."))
				childFilePath = childFilePath.replace("\\", ".")
				className.add(childFilePath)
			}
		}

		return className
	}

	fun getClassName(packageName: String): List<String> {
		val filePath = DemoPlugin::class.java.classLoader.getResource("").path + packageName.replace(".", "\\")
		return getClassName(filePath, arrayListOf())
	}
}