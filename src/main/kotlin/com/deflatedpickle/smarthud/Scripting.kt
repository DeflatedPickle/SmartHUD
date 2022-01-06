/* Copyright (c) 2022 ChloeDawn,DeflatedPickle under the APACHE-2.0 license */

@file:Suppress("UNCHECKED_CAST")

package com.deflatedpickle.smarthud

import com.deflatedpickle.smarthud.impl.Section
import net.minecraft.client.MinecraftClient
import java.io.File
import java.nio.file.Files
import java.nio.file.StandardCopyOption
import kotlin.script.experimental.api.ResultValue
import kotlin.script.experimental.api.ResultWithDiagnostics
import kotlin.script.experimental.api.ScriptCompilationConfiguration
import kotlin.script.experimental.api.ScriptEvaluationConfiguration
import kotlin.script.experimental.api.defaultImports
import kotlin.script.experimental.host.toScriptSource
import kotlin.script.experimental.jvm.dependenciesFromCurrentContext
import kotlin.script.experimental.jvm.jvm
import kotlin.script.experimental.jvmhost.BasicJvmScriptingHost

object Scripting {
    private val CONFIG = "${System.getProperty("user.dir")}/config/smarthud.kts"

    val file = File(CONFIG)
    val host = BasicJvmScriptingHost()

    val cc = ScriptCompilationConfiguration {
        jvm {
            dependenciesFromCurrentContext(
                wholeClasspath = true
            )
        }
        defaultImports(
            "com.deflatedpickle.smarthud.SmartHUDReheated.DISTANCE",
            "com.deflatedpickle.smarthud.SmartHUDReheated.SIZE",
            "com.deflatedpickle.smarthud.SmartHUDReheated.BORDER",
            "com.deflatedpickle.smarthud.api.*",
            "com.deflatedpickle.smarthud.impl.*",
        )
    }

    val ec = ScriptEvaluationConfiguration {
    }

    fun createConfig() {
        val res = this::class.java.getResourceAsStream("/assets/smarthud/config/smarthud.kts")!!
        Files.copy(res, file.toPath(), StandardCopyOption.REPLACE_EXISTING)
    }

    fun reloadConfig(sendMessage: Boolean = false) {
        val eval = host.eval(file.toScriptSource(), cc, ec)
        println(eval)

        when (eval) {
            is ResultWithDiagnostics.Success -> {
                println(eval.value)
                println(eval.value.returnValue)
                println(eval.value.returnValue.scriptInstance)
                println(eval.value.returnValue.scriptClass)

                when (val result = eval.value.returnValue) {
                    is ResultValue.Value -> {
                        println(result.value)
                        SmartHUDReheated.sections = result.value as List<Section>
                    }
                    is ResultValue.Unit -> TODO()
                    is ResultValue.Error -> TODO()
                    ResultValue.NotEvaluated -> TODO()
                }
            }
            is ResultWithDiagnostics.Failure -> {}
        }

        if (sendMessage) {
            MinecraftClient.getInstance().player?.sendChatMessage("Reloaded config")
        }
    }
}
