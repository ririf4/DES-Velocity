package net.ririfa.des.proxy.translation

import com.velocitypowered.api.proxy.Player
import net.kyori.adventure.text.Component
import net.ririfa.langman.def.MessageProviderDefault

class DESProxyMSGProvider(private val player: Player) : MessageProviderDefault<DESProxyMSGProvider, Component>(
	Component::class.java
) {
	override fun getLanguage(): String {
		return player.playerSettings.locale.toLanguageTag()
	}
}

fun Player.adapt(): DESProxyMSGProvider = DESProxyMSGProvider(this)