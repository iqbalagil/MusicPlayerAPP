/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 * Class Music merepresentasikan satu entitas lagu MP3.
 * Menerapkan enkapsulasi penuh: semua field bersifat private,
 * akses dilakukan melalui getter dan setter dengan validasi.
 *
 * @author iqbalagil
 */
public class Music {

    // ── Field Private (Enkapsulasi) ──

    /** Referensi file MP3 di sistem */
    private File file;

    /** Judul lagu (tanpa ekstensi .mp3) */
    private String title;

    /** Nama artis/penyanyi */
    private String artist;

    /** Genre musik */
    private String genre;

    /** Total frame audio */
    private long totalFrames;

    /** Durasi lagu dalam detik */
    private int durationPerSeconds;

    /** Status favorit lagu */
    private boolean favorite;

    // ── Konstruktor ──

    /**
     * Membuat objek Music dari file MP3 yang diunggah.
     * Judul otomatis diambil dari nama file tanpa ekstensi.
     *
     * @param file referensi file MP3
     */
    public Music(File file) {
        this.file = file;
        this.title = parseTitle(file.getName());
        this.artist = "Tidak Diketahui";
        this.genre = "Umum";
        this.totalFrames = 0;
        this.durationPerSeconds = 0;
        this.favorite = false;
    }

    /**
     * Konstruktor lengkap dengan informasi artis dan genre.
     *
     * @param file   referensi file MP3
     * @param artist nama artis lagu
     * @param genre  genre musik
     */
    public Music(File file, String artist, String genre) {
        this(file);
        setArtist(artist);
        setGenre(genre);
    }

    // ── Method Private (Enkapsulasi Internal) ──

    /**
     * Menghapus ekstensi ".mp3" dari nama file agar tampilan lebih bersih.
     *
     * @param fileName nama file asli
     * @return nama file tanpa ekstensi
     */
    private String parseTitle(String fileName) {
        if (fileName.toLowerCase().endsWith(".mp3")) {
            return fileName.substring(0, fileName.length() - 4);
        }
        return fileName;
    }

    // ── Getter (Akses Terkontrol) ──

    /**
     * Mengambil referensi file MP3.
     *
     * @return objek File musik
     */
    public File getFile() {
        return file;
    }

    /**
     * Mengambil judul lagu.
     *
     * @return judul lagu tanpa ekstensi
     */
    public String getTitle() {
        return title;
    }

    /**
     * Mengambil nama artis.
     *
     * @return nama artis
     */
    public String getArtist() {
        return artist;
    }

    /**
     * Mengambil genre musik.
     *
     * @return genre musik
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Mengambil total frame audio.
     *
     * @return jumlah total frame
     */
    public long getTotalFrames() {
        return totalFrames;
    }

    /**
     * Mengambil durasi lagu.
     *
     * @return durasi dalam detik
     */
    public int getDurationPerSeconds() {
        return durationPerSeconds;
    }

    /**
     * Mengecek apakah lagu ditandai sebagai favorit.
     *
     * @return true jika favorit
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Mengambil alamat lengkap file MP3 di sistem.
     *
     * @return path absolut file
     */
    public String getFullPathFile() {
        return file.getAbsolutePath();
    }

    // ── Setter dengan Validasi (Enkapsulasi) ──

    /**
     * Mengatur judul lagu secara manual.
     * Validasi: judul tidak boleh null atau kosong.
     *
     * @param title judul baru
     */
    public void setTitle(String title) {
        if (title != null && !title.trim().isEmpty()) {
            this.title = title.trim();
        }
    }

    /**
     * Mengatur nama artis.
     * Validasi: jika null atau kosong, diisi "Tidak Diketahui".
     *
     * @param artist nama artis baru
     */
    public void setArtist(String artist) {
        if (artist != null && !artist.trim().isEmpty()) {
            this.artist = artist.trim();
        } else {
            this.artist = "Tidak Diketahui";
        }
    }

    /**
     * Mengatur genre musik.
     * Validasi: jika null atau kosong, diisi "Umum".
     *
     * @param genre genre baru
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            this.genre = genre.trim();
        } else {
            this.genre = "Umum";
        }
    }

    /**
     * Mengatur total frame audio.
     * Validasi: tidak boleh negatif.
     *
     * @param totalFrames jumlah frame
     */
    public void setTotalFrames(long totalFrames) {
        if (totalFrames >= 0) {
            this.totalFrames = totalFrames;
        }
    }

    /**
     * Mengatur durasi lagu.
     * Validasi: tidak boleh negatif.
     *
     * @param durationPerSeconds durasi dalam detik
     */
    public void setDurationPerSeconds(int durationPerSeconds) {
        if (durationPerSeconds >= 0) {
            this.durationPerSeconds = durationPerSeconds;
        }
    }

    /**
     * Mengatur status favorit lagu.
     *
     * @param favorite true untuk menandai favorit
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // ── Override ──

    /**
     * Representasi string untuk ditampilkan di komponen UI.
     *
     * @return judul lagu
     */
    @Override
    public String toString() {
        return title;
    }
}
