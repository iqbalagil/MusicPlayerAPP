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
 *
 * @author iqbalagil
 */
public class MusicPlayer {

    private AdvancedPlayer player;
    private Thread playThread;
    /* Audio file yang sedang dimuat sekarang */
    private Clip clip;
    /* Musik yang sedang di mainkan sekarang */
    private Music currentMusic;
    /* Mentracking apakah music yang dijalankan sedang 
    di pause atau masih berjalan */
    private boolean isPaused;
    private boolean isPlaying;
    /* Menyimpan posisi frame jika musik sedang di pause, sehingga
    bisa di jalankan kembali*/
    private int currentFrame;
    private final int ASSUMED_FRAME_RATE = 26;

    public MusicPlayer() {
        this.isPaused = false;
        this.currentFrame = 0;
    }

    public void load(Music music) {
        stop();
        this.currentMusic = music;
        this.currentFrame = 0;
    }

    /**
     * Memulai music yang sedang dimuat Jika di pause maka, mulai pada posisi
     * pause sebelumnya Jika tidak, mulai dari awal
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

            // Run playback in a separate thread to keep GUI responsive
            playThread = new Thread(() -> {
                try {
                    // Play starting from the saved frame
                    player.play(currentFrame, Integer.MAX_VALUE);

                    // If it finishes naturally (not paused/stopped)
                    if (player != null && !isPaused) {
                        stop();
                    }
                } catch (Exception e) {
                    System.out.println("Playback error: " + e.getMessage());
                }
            });

            playThread.start();

        } catch (Exception e) {
            System.out.println("Failed to load file: " + e.getMessage());
        }
    }

    /**
     * Untuk memberhentikan music sementara music Menyimpan posisi frame
     * sekarang saat di berhentikan sementara
     */
    public void pause() {
        if (player != null && isPlaying) {
            isPaused = true;
            isPlaying = false;
            player.close();
        }
    }

    /**
     * Untuk menskip perframe music. Jika frame terskip sampai akhir maka music
     * berhenti sementara
     */
    public void forward(int seconds) {
        if (currentMusic != null) {
            int framesToSkip = seconds * ASSUMED_FRAME_RATE;
            seek(currentFrame + framesToSkip);
        }
    }

    /**
     * Untuk memberhentikan music yang sedang berjalan
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

    public void seek(int targetFrame) {
        boolean wasPlaying = isPlaying;
        // Memberhentikan streamline sementara
        if (player != null) {
            player.close();
        }
        // Update posisi timeline
        this.currentFrame = targetFrame;
        this.isPlaying = false;

        if (wasPlaying) {
            play();
        }
    }

    /**
     * Skip frame ke belakang. mengkalkulasi frame sekarang jika skip sampai
     * posisi start maka frame kembali ke 0
     *
     * @param seconds bilangan number untuk menskip music
     */
    public void backward(int seconds) {
        if (currentMusic != null) {
            int framesToSkip = seconds * ASSUMED_FRAME_RATE;
            int newFrame = currentFrame - framesToSkip;
            seek(Math.max(0, newFrame));
        }
    }

    /**
     * mencheck apakah clip masih berjalan.
     *
     * @return benar jika music masih berjalan, selain itu false
     */
    public boolean isPlaying() {
        return isPlaying;
    }

    /**
     * Mengembalikan object music yang dijalankan.
     *
     * @return Music yang sedang mulai sekarang, atau null
     */
    public Music getCurrentMusic() {
        return currentMusic;
    }
    // Mengambil posisi frame sekarang
    public int getCurrentFrame() {
        return currentFrame;
    }
}
