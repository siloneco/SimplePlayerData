package com.github.siloneco.playerdata.listener;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

import com.github.siloneco.playerdata.SimplePlayerData;
import com.github.siloneco.playerdata.data.PlayerData;

public class JoinAndQuitListener implements Listener {

    private final SimplePlayerData plugin;

    public JoinAndQuitListener(SimplePlayerData plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent e) {
        // プレイヤーデータを読み込む
        plugin.getPlayerDataContainer().getPlayerData(e.getPlayer(), true);
    }

    @EventHandler
    public void onQuit(PlayerQuitEvent e) {
        // プレイヤーデータをunloadする
        // プレイヤーデータを取得
        PlayerData data = plugin.getPlayerDataContainer().getPlayerData(e.getPlayer(), false);

        // nullの場合ロードされていないのでreturn
        if ( data == null ) {
            return;
        }

        // セーブする
        plugin.getPlayerDataContainer().savePlayerData(data, true);
    }
}
