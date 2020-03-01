package com.github.siloneco.playerdata;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import com.github.siloneco.playerdata.data.PlayerDataContainer;
import com.github.siloneco.playerdata.listener.JoinAndQuitListener;

public class SimplePlayerData extends JavaPlugin {

    // プレイヤーデータを格納するやつ
    private PlayerDataContainer dataContainer = null;

    @Override
    public void onEnable() {
        // プレイヤーデータを格納するクラス(PlayerDataContainer)を作成する
        dataContainer = new PlayerDataContainer(this);

        // リスナーの登録
        Bukkit.getPluginManager().registerEvents(new JoinAndQuitListener(this), this);

        // Plugmanとか/reloadがされた場合、プレイヤーデータをロードしておく必要がある (しなくても次取得されたときにロードされるが、
        // 重くなるのを防ぐため早めにやったほうがいい

        // プレイヤーが0人より多い場合
        if ( Bukkit.getOnlinePlayers().size() > 0 ) {
            for ( Player player : Bukkit.getOnlinePlayers() ) {
                // 各プレイヤーのプレイヤーデータをロードする
                dataContainer.getPlayerData(player, true);
            }
        }

        Bukkit.getLogger().info(getName() + " enabled.");
    }

    @Override
    public void onDisable() {
        // 全プレイヤーのデータを保存する
        // よく分からないバグによってdataContainerが作成されなかった場合 null でNullPointerExceptionになるので
        // nullチェックをしておく
        if ( dataContainer != null ) {
            dataContainer.saveAll();
        }

        Bukkit.getLogger().info(getName() + " disabled.");
    }

    /**
     * {@link PlayerDataContainer}のGetterメソッド
     *
     * @return dataContainer
     */
    public PlayerDataContainer getPlayerDataContainer() {
        return dataContainer;
    }
}