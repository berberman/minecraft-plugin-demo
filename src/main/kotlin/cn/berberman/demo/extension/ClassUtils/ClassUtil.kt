package cn.berberman.demo.extension.ClassUtils

import cn.berberman.demo.DemoPlugin
import java.io.File
import java.net.URL
import java.net.URLClassLoader
import java.util.*
import java.util.jar.JarFile

@Deprecated("蜜汁")
object ClassUtil {
	/**
	 * 获取某包下（包括该包的所有子包）所有类
	 * @param packageName 包名
	 * @return 类的完整名称
	 */
	private fun getClassName(packageName: String): List<String> {
		return getClassName(packageName, true)
	}

	/**
	 * 获取某包下所有类
	 * @param packageName 包名
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	private fun getClassName(packageName: String, childPackage: Boolean): List<String> {
		var fileNames = listOf<String>()
		val loader = DemoPlugin::class.java.classLoader
		val packagePath = packageName.replace("", "/")
		val url = loader.getResource(packagePath)
		if (url != null) {
			val type = url.protocol
			if (type == "file") {
				fileNames = getClassNameByFile(url.path, childPackage)
			} else if (type == "jar") {
				fileNames = getClassNameByJar(url.path, childPackage)
			}
		} else {
			fileNames = getClassNameByJars((loader as URLClassLoader).urLs, packagePath, childPackage)
		}
		return fileNames
	}

	/**
	 * 从项目文件获取某包下所有类
	 * @param filePath 文件路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	private fun getClassNameByFile(filePath: String, childPackage: Boolean): List<String> {
		val myClassName = ArrayList<String>()
		val file = File(filePath)
		val childFiles = file.listFiles()
		childFiles.forEach { childFile ->
			if (childFile.isDirectory) {
				if (childPackage) {
					myClassName.addAll(getClassNameByFile(childFile.path, childPackage))
				}
			} else {
				var childFilePath = childFile.path
				if (childFilePath.endsWith(".class")) {
					childFilePath = childFilePath.substring(childFilePath.indexOf("\\classes") + 9, childFilePath.lastIndexOf(""))
					childFilePath = childFilePath.replace("\\", "")
					myClassName.add(childFilePath)
				}
			}
		}

		return myClassName
	}

	/**
	 * 从jar获取某包下所有类
	 * @param jarPath jar文件路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	private fun getClassNameByJar(jarPath: String, childPackage: Boolean): List<String> {
		val myClassName = ArrayList<String>()
		val jarInfo = jarPath.split("!".toRegex()).dropLastWhile { it.isEmpty() }.toTypedArray()
		val jarFilePath = jarInfo[0].substring(jarInfo[0].indexOf("/"))
		val packagePath = jarInfo[1].substring(1)
		try {
			val jarFile = JarFile(jarFilePath)
			val entries = jarFile.entries()
			while (entries.hasMoreElements()) {
				val jarEntry = entries.nextElement()
				var entryName = jarEntry.name
				if (entryName.endsWith(".class")) {
					if (childPackage) {
						if (entryName.startsWith(packagePath)) {
							entryName = entryName.replace("/", "").substring(0, entryName.lastIndexOf(""))
							myClassName.add(entryName)
						}
					} else {
						val index = entryName.lastIndexOf("/")
						val myPackagePath: String
						myPackagePath = if (index != -1) {
							entryName.substring(0, index)
						} else {
							entryName
						}
						if (myPackagePath == packagePath) {
							entryName = entryName.replace("/", "").substring(0, entryName.lastIndexOf(""))
							myClassName.add(entryName)
						}
					}
				}
			}
		} catch (e: Exception) {
			e.printStackTrace()
		}

		return myClassName
	}

	/**
	 * 从所有jar中搜索该包，并获取该包下所有类
	 * @param urls URL集合
	 * @param packagePath 包路径
	 * @param childPackage 是否遍历子包
	 * @return 类的完整名称
	 */
	private fun getClassNameByJars(urls: Array<URL>, packagePath: String, childPackage: Boolean): List<String> {
		val myClassName = ArrayList<String>()
		urls.indices.map { urls[it] }.map {
			it.path
		}.filterNot { it.endsWith("classes/") }.map { it + "!/" + packagePath }.forEach { myClassName.addAll(getClassNameByJar(it, childPackage)) }
		return myClassName
	}

	/**
	 * 获取所有类
	 * @param packageName 包名
	 * @return 类的实例
	 */
	fun getClass(packageName: String) =
			getClassName(packageName).map { Class.forName(it) }

	/**
	 * 获取所有类
	 * @return 类的实例
	 */
	fun getClass() =
			getClassName("").map { Class.forName(it) }

}