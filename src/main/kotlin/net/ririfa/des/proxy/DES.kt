package net.ririfa.des.proxy

import com.velocitypowered.api.event.Subscribe
import com.velocitypowered.api.event.connection.DisconnectEvent
import com.velocitypowered.api.event.player.ServerPreConnectEvent
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent
import com.velocitypowered.api.plugin.Plugin
import com.velocitypowered.api.plugin.annotation.DataDirectory
import com.velocitypowered.api.proxy.Player
import com.velocitypowered.api.proxy.ProxyServer
import com.velocitypowered.api.proxy.server.RegisteredServer
import net.kyori.adventure.text.Component
import net.ririfa.des.proxy.translation.DESProxyTranslation.PreparingToChangeServer
import net.ririfa.des.proxy.translation.adapt
import org.slf4j.Logger
import java.nio.file.Path
import java.util.UUID
import java.util.concurrent.CompletableFuture
import javax.inject.Inject

@Plugin(
	id = "des-proxy",
	name = "DES Proxy",
	description = "Proxy management plugin developed for DES servers.",
	version = "1.0.0",
	authors = ["RiriFa"],
)
class DES @Inject constructor(
	private val server: ProxyServer,
	private val logger: Logger,
	@DataDirectory
	private val dataFolder: Path
) {
	private val pendingTransfers: MutableMap<UUID, RegisteredServer> = mutableMapOf()

	@Subscribe
	fun onProxyInitialization(event: ProxyInitializeEvent) {

	}

	@Subscribe
	fun onPreTransfer(event: ServerPreConnectEvent) {
		val player = event.player
		val targetServer = event.result.server.orElse(null) ?: return

		event.result = ServerPreConnectEvent.ServerResult.denied()
		pendingTransfers[player.uniqueId] = targetServer

		player.sendMessage(player.adapt().getMessage(PreparingToChangeServer))

		CompletableFuture.runAsync {
			try {
				// TODO: ここで処理する
			} catch (e: InterruptedException) {
				e.printStackTrace()
			}
		}.thenRun {
			if (pendingTransfers.containsKey(player.uniqueId)) {
				transferPlayerToStoredServer(player)
			}
		}
	}

	@Subscribe
	fun onPlayerDisconnect(event: DisconnectEvent) {
		val player = event.player
		if (pendingTransfers.containsKey(player.uniqueId)) {
			pendingTransfers.remove(player.uniqueId)
		}
	}

	private fun transferPlayerToStoredServer(player: Player) {
		val targetServer = pendingTransfers[player.uniqueId]

		if (targetServer != null) {
			player.createConnectionRequest(targetServer).connectWithIndication().thenAccept { result ->
				if (!result) {
					player.sendMessage(Component.text("サーバーへの転送に失敗しました…"))
				}
				pendingTransfers.remove(player.uniqueId)
			}
		} else {
			logger.warn("プレイヤー ${player.username} に保存されたサーバー情報が見つかりません！")
		}
	}

}