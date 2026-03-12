/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;
import javax.sound.sampled.*;
import java.io.IOException;

/**
 *
 * @author iqbalagil
 */
public class MusicPlayer {

    /* Audio file yang sedang dimuat sekarang */
    private Clip clip;
    /* Musik yang sedang di mainkan sekarang */
    private Music currentMusic;
    /* Mentracking apakah music yang dijalankan sedang 
    di pause atau masih berjalan */
    private boolean isPaused;
    /* Menyimpan posisi frame jika musik sedang di pause, sehingga
    bisa di jalankan kembali*/
    private int pausePosition;

    public MusicPlayer() {
        this.clip = null;
        this.currentMusic = null;
        this.isPaused = false;
        this.pausePosition = 0;
    }

    public void load(Music music) {
        stop();
        try {
            File file = music.getFile();
            AudioInputStream originalStream = AudioSystem.getAudioInputStream(file);
            AudioFormat originalFormat = originalStream.getFormat();

            AudioFormat decodedFormat = new AudioFormat(
                    AudioFormat.Encoding.PCM_SIGNED,
                    originalFormat.getSampleRate(),
                    16,
                    originalFormat.getChannels(),
                    originalFormat.getChannels() * 2,
                    originalFormat.getSampleRate(),
                    false
            );

            AudioInputStream decodedStream = AudioSystem.getAudioInputStream(decodedFormat, originalStream);
            clip = AudioSystem.getClip();
            clip.open(decodedStream);
            currentMusic = music;
            isPaused = false;
            pausePosition = 0;

        } catch (UnsupportedAudioFileException e) {
            System.err.println("Format not supported: " + e.getMessage());
        } catch (IOException e) {
            System.err.println("Error reading file: " + e.getMessage());
        } catch (LineUnavailableException e) {
            System.err.println("Audio line unavailable: " + e.getMessage());
        }
    }
    
    /**
     * Memulai music yang sedang dimuat
     * Jika di pause maka, mulai pada posisi pause sebelumnya
     * Jika tidak, mulai dari awal 
     */

    public void Play() {
        if (clip == null) {
            return;
        }
        if (isPaused) {
            clip.setFramePosition(pausePosition);
            clip.start();
            isPaused = false;
        } else {
            clip.setFramePosition(0);
            clip.start();
        }
    }

    
    /**
     * Untuk memberhentikan music sementara music
     * Menyimpan posisi frame sekarang saat di berhentikan sementara
     */
    public void pause() {
        if (clip != null && clip.isRunning()) {
            pausePosition = clip.getFramePosition();
            clip.stop();
            isPaused = true;
        }
    }
    /**
     * Untuk menskip perframe music.
     * Jika frame terskip sampai akhir maka music berhenti sementara
     */
    public void forward(int seconds) {
        if (clip == null) {
            return;
        }

        int frameRate = (int) clip.getFormat().getFrameRate();
        int currentFrame = clip.getFramePosition();
        int newFrame = currentFrame + (frameRate * seconds);

        if (newFrame >= clip.getFrameLength()) {
            newFrame = clip.getFrameLength() - 1;
        }

        boolean wasRunning = clip.isRunning();
        clip.stop();
        clip.setFramePosition(newFrame);
        if (wasRunning) {
            clip.start();
        }
    }
    /**
     * Untuk memberhentikan music yang sedang berjalan
     */
    public void stop() {
        if (clip != null) {
            clip.stop();
            clip.close();
            clip = null;
            isPaused = false;
            pausePosition = 0;
        }
    }

    /**
     * Skip frame ke belakang. mengkalkulasi frame sekarang
     * jika skip sampai posisi start maka frame kembali ke 0
     *
     * @param seconds bilangan number untuk menskip music
     */
    public void backward(int seconds) {
        if (clip == null) {
            return;
        }

        int frameRate = (int) clip.getFormat().getFrameRate();
        int currentFrame = clip.getFramePosition();
        int newFrame = currentFrame - (frameRate * seconds);

        if (newFrame < 0) {
            newFrame = 0;
        }

        boolean wasRunning = clip.isRunning();
        clip.stop();
        clip.setFramePosition(newFrame);
        if (wasRunning) {
            clip.start();
        }
    }

    /**
     * mencheck apakah clip masih berjalan.
     *
     * @return benar jika music masih berjalan, selain itu false
     */
    public boolean isPlaying() {
        return clip != null && clip.isRunning();
    }

    /**
     * Returns the currently loaded music object.
     *
     * @return Music yang sedang mulai sekarang, atau null
     */
    public Music getCurrentMusic() {
        return currentMusic;
    }

    /**
     * Mengembalikan posisi playback sekarang.
     *
     * @return posisi sekarang
     */
    public long getCurrentSeconds() {
        if (clip == null) {
            return 0;
        }
        return clip.getMicrosecondPosition() / 1_000_000;
    }

    /**
     * Mengembalikan durasi music yang berjalan.
     *
     * @return total durasi music
     */
    public long getTotalSeconds() {
        if (clip == null) {
            return 0;
        }
        return clip.getMicrosecondLength() / 1_000_000;
    }

}
