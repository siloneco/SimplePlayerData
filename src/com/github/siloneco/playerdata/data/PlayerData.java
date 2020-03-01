package com.github.siloneco.playerdata.data;

import java.util.UUID;

/**
 * プレイヤーデータを管理するクラス
 *
 * @author siloneco
 *
 */
public class PlayerData {

    // 対象プレイヤーのUUID
    private final UUID uuid;

    // 例えばということで追加したプレイヤー別のスコア
    private int score = 0;

    /**
     * コンストラクタ
     *
     * @param uuid 対象プレイヤーのUUID
     */
    protected PlayerData(UUID uuid) {
        this.uuid = uuid;
    }

    /**
     * scoreも同時に指定するコンストラクタ
     *
     * @param uuid  対象プレイヤーのUUID
     * @param score スコア
     */
    protected PlayerData(UUID uuid, int score) {
        this.uuid = uuid;
        this.score = score;
    }

    /**
     * このプレイヤーデータを持つプレイヤーの UUID を取得する
     *
     * @return このプレイヤーデータの持ち主のUUID
     */
    public UUID getUUID() {
        return uuid;
    }

    /**
     * スコアを設定する
     *
     * @param score 設定するスコア
     */
    public void setScore(int score) {
        this.score = score;
    }

    /**
     * スコアを取得する
     *
     * @return 現在のスコア
     */
    public int getScore() {
        return score;
    }
}
