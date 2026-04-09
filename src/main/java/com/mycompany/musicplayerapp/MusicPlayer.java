/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;
import java.io.FileInputStream;
import javax.sound.sampled.*;
import java.io.IOException;
import javazoom.jl.player.advanced.AdvancedPlayer;

/**
 * Class MusicPlayer mengontrol pemutaran file MP3.
 * Menerapkan enkapsulasi: semua state internal bersifat private,
 * akses hanya melalui method publik yang terkontrol.
 *
 * @author iqbalagil
 */
public class MusicPlayer {

    // ── Field Private (Enkapsulasi) ──

    /** Objek player untuk decode dan memutar MP3 */
    private AdvancedPlayer player;

    /** Thread terpisah untuk pemutaran agar GUI tidak freeze */
    private Thread playThread;

    /** Clip audio (cadangan untuk format lain) */
    private Clip clip;

    /** Musik yang sedang dimuat/diputar */
    private Music currentMusic;

    /** Status jeda pemutaran */
    private boolean isPaused;

    /** Status sedang memutar */
    private boolean isPlaying;

    /** Posisi frame saat dijeda, untuk melanjutkan pemutaran */
    private int currentFrame;

    /** Level volume (0-100), default 80 */
    private int volume;

    /** Konstanta perkiraan frame rate per detik */
    private static final int ASSUMED_FRAME_RATE = 26;

    /** Batas minimum volume */
    private static final int MIN_VOLUME = 0;

    /** Batas maksimum volume */
    private static final int MAX_VOLUME = 100;

    // ── Konstruktor ──

    /**
     * Menginisialisasi MusicPlayer dengan state awal.
     * Tidak ada musik yang dimuat saat pertama kali dibuat.
     */
    public MusicPlayer() {
        this.isPaused = false;
        this.isPlaying = false;
        this.currentFrame = 0;
        this.volume = 80;
    }

    // ── Method Publik (Kontrol Pemutaran) ──

    /**
     * Memuat musik baru ke player.
     * Menghentikan musik sebelumnya jika ada.
     *
     * @param music objek Music yang akan dimuat
     */
    public void load(Music music) {
        if (music == null) {
            return;
        }
        stop();
        this.currentMusic = music;
        this.currentFrame = 0;
    }

    /**
     * Memulai pemutaran musik yang dimuat.
     * Jika dijeda sebelumnya, melanjutkan dari posisi terakhir.
     * Pemutaran berjalan di thread terpisah agar GUI tetap responsif.
     */
    public void play() {
        if (currentMusic == null || isPlaying) {
            return;
        }

        try {
            FileInputStream fis = new FileInputStream(currentMusic.getFile());
            player = new AdvancedPlayer(fis);

            isPlaying = true;
            isPaused = false;

            // Jalankan di thread terpisah agar GUI tidak macet
            playThread = new Thread(() -> {
                try {
                    player.play(currentFrame, Integer.MAX_VALUE);

                    // Jika selesai secara alami (bukan dijeda/dihentikan)
                    if (player != null && !isPaused) {
                        stop();
                    }
                } catch (Exception e) {
                    System.out.println("Gagal memutar: " + e.getMessage());
                }
            });

            playThread.start();

        } catch (Exception e) {
            System.out.println("Gagal memuat file: " + e.getMessage());
        }
    }

    /**
     * Menjeda musik yang sedang diputar.
     * Menyimpan posisi frame agar bisa dilanjutkan.
     */
    public void pause() {
        if (player != null && isPlaying) {
            isPaused = true;
            isPlaying = false;
            player.close();
        }
    }

    /**
     * Menghentikan pemutaran dan mereset posisi ke awal.
     */
    public void stop() {
        if (player != null) {
            isPlaying = false;
            isPaused = false;
            currentFrame = 0;
            player.close();
            player = null;
        }
    }

    /**
     * Melompat maju beberapa detik dari posisi sekarang.
     *
     * @param seconds jumlah detik untuk maju
     */
    public void forward(int seconds) {
        if (currentMusic != null && seconds > 0) {
            int framesToSkip = seconds * ASSUMED_FRAME_RATE;
            seek(currentFrame + framesToSkip);
        }
    }

    /**
     * Melompat mundur beberapa detik dari posisi sekarang.
     * Jika melewati awal, posisi direset ke frame 0.
     *
     * @param seconds jumlah detik untuk mundur
     */
    public void backward(int seconds) {
        if (currentMusic != null && seconds > 0) {
            int framesToSkip = seconds * ASSUMED_FRAME_RATE;
            int newFrame = currentFrame - framesToSkip;
            seek(Math.max(0, newFrame));
        }
    }

    /**
     * Melompat ke posisi frame tertentu.
     * Validasi: frame tidak boleh negatif.
     *
     * @param targetFrame posisi frame tujuan
     */
    public void seek(int targetFrame) {
        if (targetFrame < 0) {
            targetFrame = 0;
        }

        boolean wasPlaying = isPlaying;

        // Hentikan stream sementara
        if (player != null) {
            player.close();
        }

        // Perbarui posisi frame
        this.currentFrame = targetFrame;
        this.isPlaying = false;

        // Lanjutkan jika sebelumnya sedang memutar
        if (wasPlaying) {
            play();
        }
    }

    // ── Getter (Akses Terkontrol ke State) ──

    /**
     * Mengecek apakah musik sedang diputar.
     *
     * @return true jika sedang memutar
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Mengecek apakah musik sedang dijeda.
     *
     * @return true jika sedang jeda
     */
    public boolean isPaused() {
        return isPaused;
    }

    /**
     * Mengambil objek Music yang sedang dimuat.
     *
     * @return Music sekarang, atau null jika tidak ada
     */
    public Music getCurrentMusic() {
        return currentMusic;
    }

    /**
     * Mengambil posisi frame pemutaran sekarang.
     *
     * @return nomor frame saat ini
     */
    public int getCurrentFrame() {
        return currentFrame;
    }

    /**
     * Mengambil level volume saat ini.
     *
     * @return volume (0-100)
     */
    public int getVolume() {
        return volume;
    }

    // ── Setter dengan Validasi ──

    /**
     * Mengatur level volume.
     * Validasi: dibatasi antara 0 dan 100.
     *
     * @param volume level volume baru (0-100)
     */
    public void setVolume(int volume) {
        if (volume < MIN_VOLUME) {
            this.volume = MIN_VOLUME;
        } else if (volume > MAX_VOLUME) {
            this.volume = MAX_VOLUME;
        } else {
            this.volume = volume;
        }
    }
}
