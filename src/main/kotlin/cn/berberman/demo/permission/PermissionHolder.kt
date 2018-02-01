package cn.berberman.demo.permission

import cn.berberman.demo.extension.logger
import cn.berberman.demo.extension.pluginManager

object PermissionHolder {
	private val simplePermissionList = mutableListOf<DSLPermissionBuilder>()


	fun addPermission(permissionBuilder: DSLPermissionBuilder) = simplePermissionList.add(permissionBuilder)


	fun register() {
		simplePermissionList.flatMap { it.getChildPermissionInstances() }.forEach(pluginManager::addPermission)
		simplePermissionList.flatMap { it.getChildPermissionInstances() }.forEach {
			logger.info("注册子权限：${it.name}")
		}
		simplePermissionList.forEach { it.build().let(pluginManager::addPermission) }
		simplePermissionList.forEach { it.build().let { logger.info("注册权限：${it.name}") } }
	}
}