package net.ririfa.des.proxy.translation

import net.kyori.adventure.text.Component
import net.ririfa.langman.MessageKey

sealed class DESProxyTranslation : MessageKey<DESProxyMSGProvider, Component> {
	object PreparingToChangeServer : DESProxyTranslation()
}