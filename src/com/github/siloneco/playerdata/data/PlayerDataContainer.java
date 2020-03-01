package com.github.siloneco.playerdata.data;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

public class PlayerDataContainer {

    // プレイヤーデータを保存するフォルダ
    private final File dataFolder;
    // ロードしたプレイヤーデータを格納しておくHashMap
    private final HashMap<UUID, PlayerData> loadedPlayerData = new HashMap<>();

    public PlayerDataContainer(JavaPlugin plugin) {
        this.dataFolder = new File(plugin.getDataFolder(), "PlayerData");
    }

    /**
     * 指定されたUUIDのプレイヤーデータを取得します
     *
     * @param uuid            取得したいプレイヤーデータのUUID
     * @param loadIfNotLoaded ロードされていない場合にファイルからロードするかどうか
     * @return 取得されたプレイヤーデータ、ロードされていない場合はnull
     */
    public PlayerData getPlayerData(UUID uuid, boolean loadIfNotLoaded) {
        // loadedPlayerData に含まれている場合はそこから取得
        if ( loadedPlayerData.containsKey(uuid) ) {
            return loadedPlayerData.get(uuid);
        }

        // ロードしない設定の場合、現在ロードされていないため null を返す
        if ( !loadIfNotLoaded ) {
            return null;
        }

        // ファイルからロードする
        // ファイルを取得 (これファイルが存在していない場合でも file 変数はnullにはならない)
        File file = new File(dataFolder, uuid.toString() + ".yml");

        // YamlConfigurationとしてロードする (ファイルが存在していなくてもconfはnullにならない)
        YamlConfiguration conf = YamlConfiguration.loadConfiguration(file);

        // 各値を取得する
        int score = conf.getInt("Score", 0); // 第2引数はデフォルト値。ファイルに値が設定されていなかった場合にはこの値が返される

        // PlayerData インスタンスを作成する
        PlayerData data = new PlayerData(uuid, score);
        // mapに保存して、何度もロードするのを避ける
        loadedPlayerData.put(uuid, data);

        // returnする
        return data;
    }

    /**
     * 対象プレイヤーのプレイヤーデータを取得します <br>
     * このメソッドは {@link #getPlayerData(UUID)} を呼び出します
     *
     * @param player          対象プレイヤー
     * @param loadIfNotLoaded ロードされていない場合にファイルからロードするかどうか
     * @return 取得されたプレイヤーデータ、ロードされていない場合はnull
     */
    public PlayerData getPlayerData(Player player, boolean loadIfNotLoaded) {
        return getPlayerData(player.getUniqueId(), loadIfNotLoaded);
    }

    /**
     * プレイヤーデータを保存します
     *
     * @param data   保存したいプレイヤーデータ
     * @param unload unloadするかどうか
     * @return セーブに成功したかどうか
     */
    public boolean savePlayerData(PlayerData data, boolean unload) {
        // ファイルを取得
        File file = new File(dataFolder, data.getUUID().toString() + ".yml");
        // 空のYamlConfigurationをロードする
        YamlConfiguration conf = new YamlConfiguration();

        // 値をセットしていく
        conf.set("Score", data.getScore());

        // セーブする
        // ファイル関連のエラーになるかもしれないのでtry-catchが必要
        try {
            conf.save(file);
        } catch ( IOException e ) {
            // 失敗したのでエラーを表示してfalseを返す
            e.printStackTrace();
            return false;
        }
        // unloadがtrueの場合、loadedPlayerDataからも削除する
        if ( loadedPlayerData.containsKey(data.getUUID()) ) {
            loadedPlayerData.remove(data.getUUID());
        }

        // 成功したのでtrueを返す
        return true;
    }

    /**
     * ロードされている全てのプレイヤーデータを保存してunloadします
     *
     * @return 成功したかどうか
     */
    public boolean saveAll() {
        // 成功したかどうかを先に定義しておく
        boolean succeed = true;

        // 現在ロードされている全てのプレイヤーデータに対して、forで処理をする
        // わざわざnew ArrayListで新しいリストとして取得している理由は、
        // forでループしている状態でforの中にあるsavePlayerDataでloadedPlayerDataの中身を編集してしまうと
        // ConcurrentModificationException というエラーになってしまうため
        // 全く別のリストとして実行することで変更の影響を受けなくなりエラーが出なくなる
        for ( PlayerData data : new ArrayList<PlayerData>(loadedPlayerData.values()) ) {
            // dataを保存する
            boolean success = savePlayerData(data, true);

            // セーブに失敗していた場合succeedをfalseにする
            if ( !success ) {
                succeed = false;
            }
        }

        // 返す
        return succeed;
    }
}
