package com.dfmabbas.reactor.security

import android.content.Context
import com.dfmabbas.reactor.engine.EngineController
import java.io.Serializable

internal class SecurityController(appContext: Context, isCryptography: Boolean) {
    private var engineController = EngineController(appContext, algorithm.name)
    private val securityModel = SecurityModel(appContext, algorithm)

    internal fun <T : Serializable> put(key: String, value: String, type: T): Boolean {
        val encryptValue = securityModel.encryptValue(value.toString())
        return engineController.put(key, encryptValue, type)
    }

    internal fun <T : Serializable> get(key: String, default: T): String? {
        val value: String = engineController.get(key, default) ?: return null

        return try {
            securityModel.decryptValue(value)
        } catch (ex: Exception) {
            ex.printStackTrace()
            null
        }
    }

    fun <T : Serializable> remove(key: String, type: T): Boolean {
        return engineController.remove(key, type)
    }

    fun clearAll() {
        engineController.clearAll()
    }
}