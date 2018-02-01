package cn.berberman.demo.persistence

import cn.berberman.demo.extension.plugin
import org.sqlite.SQLiteConfig
import org.sqlite.SQLiteDataSource
import org.sqlite.SQLiteOpenMode
import org.sqlite.javax.SQLiteConnectionPoolDataSource
import org.sqlite.javax.SQLitePooledConnection
import java.sql.Connection
import java.sql.DriverManager

class DataBase private constructor() {

	lateinit var connection: Connection

	companion object {
		val instance by lazy(mode = LazyThreadSafetyMode.SYNCHRONIZED) { DataBase() }
	}

	fun init() {
		Class.forName("org.sqlite.JDBC")
		val dbPath = "${plugin.dataFolder.path}/${plugin.name}.db"
		connection = DriverManager.getConnection("jdbc:sqlite:$dbPath")
	}

}