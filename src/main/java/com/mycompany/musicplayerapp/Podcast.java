/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 * Class Podcast merepresentasikan file audio bertipe podcast.
 * Merupakan implementasi konkret dari abstract class {@link AudioMedia}.
 *
 * <p>
 * <b>Penerapan Abstraksi:</b>
 * </p>
 * <ul>
 * <li>{@code extends AudioMedia} — mewarisi field dan method umum dari abstract
 * class</li>
 * <li>Mengimplementasikan semua abstract method: displayInfo(), getMediaType(),
 * getSummary()</li>
 * <li>Menambahkan field khusus podcast: host, episodeNumber, channelName,
 * description</li>
 * </ul>
 *
 * <p>
 * <b>Penerapan Enkapsulasi:</b> semua field tambahan bersifat private,
 * akses dilakukan melalui getter dan setter dengan validasi.
 * </p>
 *
 * @author iqbalagil
 * @see AudioMedia
 */
public class Podcast extends AudioMedia {

    // ── Field Private Tambahan (Enkapsulasi) ──

    /** Nama pembawa acara / host podcast */
    private String host;

    /** Nomor episode podcast */
    private int episodeNumber;

    /** Nama channel / saluran podcast */
    private String channelName;

    /** Deskripsi singkat episode */
    private String description;

    /** Genre podcast */
    private String genre;

    // ── Konstruktor ──

    /**
     * Membuat objek Podcast dari file audio.
     * Memanggil konstruktor abstract parent {@link AudioMedia#AudioMedia(File)}
     * untuk inisialisasi field umum (file, title, totalFrames, dll).
     * Field khusus podcast diisi dengan nilai default.
     *
     * @param file referensi file audio podcast
     */
    public Podcast(File file) {
        // Memanggil konstruktor abstract class AudioMedia
        super(file);

        // Inisialisasi field khusus podcast dengan nilai default
        this.host = "Tidak Diketahui";
        this.episodeNumber = 1;
        this.channelName = "Tidak Diketahui";
        this.description = "";
        this.genre = "Podcast";
    }

    /**
     * Konstruktor lengkap Podcast dengan informasi detail.
     * Memanggil konstruktor sederhana lalu mengatur field tambahan.
     *
     * @param file          referensi file audio podcast
     * @param host          nama pembawa acara
     * @param channelName   nama channel podcast
     * @param episodeNumber nomor episode
     */
    public Podcast(File file, String host, String channelName, int episodeNumber) {
        this(file);
        setHost(host);
        setChannelName(channelName);
        setEpisodeNumber(episodeNumber);
    }

    // ── Implementasi Abstract Method dari AudioMedia ──
    // Method-method berikut WAJIB diimplementasikan karena AudioMedia
    // mendefinisikannya sebagai abstract.

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan jenis media.
     * Podcast mengembalikan "Podcast" sebagai identifikasi tipe.
     *
     * <p>
     * <b>Implementasi dari:</b> {@link AudioMedia#getMediaType()}
     * </p>
     *
     * @return string "Podcast"
     */
    @Override
    public String getMediaType() {
        return "Podcast";
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan deskripsi singkat podcast.
     * Format: "Podcast 'Judul' Ep. X oleh Host di Channel"
     *
     * <p>
     * <b>Implementasi dari:</b> {@link AudioMedia#getSummary()}
     * </p>
     *
     * @return string deskripsi singkat
     */
    @Override
    public String getSummary() {
        return String.format("Podcast '%s' Ep. %d oleh %s di %s",
                title, episodeNumber, host, channelName);
    }

    // ── Getter (Akses Terkontrol) ──

    /**
     * Mengambil nama host / pembawa acara.
     *
     * @return nama host
     */
    public String getHost() {
        return host;
    }

    /**
     * Mengambil nomor episode.
     *
     * @return nomor episode
     */
    public int getEpisodeNumber() {
        return episodeNumber;
    }

    /**
     * Mengambil nama channel podcast.
     *
     * @return nama channel
     */
    public String getChannelName() {
        return channelName;
    }

    /**
     * Mengambil deskripsi episode.
     *
     * @return deskripsi episode
     */
    public String getDescription() {
        return description;
    }

    /**
     * Mengambil genre podcast.
     *
     * @return genre podcast
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Mengambil informasi lengkap podcast dalam format teks.
     *
     * @return string informasi lengkap podcast
     */
    public String getFullInfo() {
        return String.format("Podcast: %s | Episode %d | Host: %s | Channel: %s",
                getTitle(), episodeNumber, host, channelName);
    }

    // ── Setter dengan Validasi (Enkapsulasi) ──

    /**
     * Mengatur nama host podcast.
     * Validasi: jika null atau kosong, diisi "Tidak Diketahui".
     *
     * @param host nama host baru
     */
    public void setHost(String host) {
        if (host != null && !host.trim().isEmpty()) {
            this.host = host.trim();
        } else {
            this.host = "Tidak Diketahui";
        }
    }

    /**
     * Mengatur nomor episode.
     * Validasi: harus bernilai positif (minimal 1).
     *
     * @param episodeNumber nomor episode baru
     */
    public void setEpisodeNumber(int episodeNumber) {
        if (episodeNumber > 0) {
            this.episodeNumber = episodeNumber;
        }
    }

    /**
     * Mengatur nama channel podcast.
     * Validasi: jika null atau kosong, diisi "Tidak Diketahui".
     *
     * @param channelName nama channel baru
     */
    public void setChannelName(String channelName) {
        if (channelName != null && !channelName.trim().isEmpty()) {
            this.channelName = channelName.trim();
        } else {
            this.channelName = "Tidak Diketahui";
        }
    }

    /**
     * Mengatur deskripsi episode podcast.
     * Validasi: jika null, diisi string kosong.
     *
     * @param description deskripsi baru
     */
    public void setDescription(String description) {
        if (description != null) {
            this.description = description.trim();
        } else {
            this.description = "";
        }
    }

    /**
     * Mengatur genre podcast.
     * Validasi: jika null atau kosong, diisi "Podcast".
     *
     * @param genre genre baru
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            this.genre = genre.trim();
        } else {
            this.genre = "Podcast";
        }
    }

    // ── Overloading: setEpisodeInfo() ──
    // Overloading = method dengan nama sama tapi parameter berbeda.
    // Di Podcast, kita overload setEpisodeInfo() sesuai tema podcast:
    // - Versi 1: hanya nomor episode
    // - Versi 2: nomor episode + deskripsi
    // - Versi 3: nomor episode + deskripsi + host

    /**
     * Mengatur informasi episode — hanya nomor episode.
     *
     * <p>
     * <b>Overloading #1:</b> setEpisodeInfo(int) — 1 parameter
     * </p>
     *
     * @param episodeNumber nomor episode baru
     */
    public void setEpisodeInfo(int episodeNumber) {
        setEpisodeNumber(episodeNumber);
    }

    /**
     * Mengatur informasi episode — nomor episode dan deskripsi.
     *
     * <p>
     * <b>Overloading #2:</b> setEpisodeInfo(int, String) — 2 parameter
     * </p>
     *
     * @param episodeNumber nomor episode baru
     * @param description   deskripsi episode baru
     */
    public void setEpisodeInfo(int episodeNumber, String description) {
        setEpisodeNumber(episodeNumber);
        setDescription(description);
    }

    /**
     * Mengatur informasi episode — nomor, deskripsi, dan host.
     *
     * <p>
     * <b>Overloading #3:</b> setEpisodeInfo(int, String, String) — 3 parameter
     * </p>
     *
     * @param episodeNumber nomor episode baru
     * @param description   deskripsi episode baru
     * @param host          nama host episode
     */
    public void setEpisodeInfo(int episodeNumber, String description, String host) {
        setEpisodeNumber(episodeNumber);
        setDescription(description);
        setHost(host);
    }

    // ── Implementasi Abstract: displayInfo() (Overloading) ──
    // Ketiga versi displayInfo() merupakan implementasi dari abstract method
    // yang didefinisikan di AudioMedia. Sekaligus menunjukkan overloading.

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info ringkas podcast.
     * Format: "[Podcast] Judul - Ep. X (Host: ...)"
     *
     * <p>
     * <b>Implementasi dari:</b> {@link AudioMedia#displayInfo()}
     * </p>
     *
     * @return info ringkas podcast
     */
    @Override
    public String displayInfo() {
        return String.format("[Podcast] %s - Ep. %d (Host: %s)",
                getTitle(), episodeNumber, host);
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info podcast dengan opsi detail.
     * Jika detailed=true, menampilkan channel dan deskripsi.
     *
     * <p>
     * <b>Implementasi dari:</b> {@link AudioMedia#displayInfo(boolean)}
     * </p>
     *
     * @param detailed true untuk info lengkap podcast
     * @return info podcast sesuai level detail
     */
    @Override
    public String displayInfo(boolean detailed) {
        if (detailed) {
            return String.format(
                    "Podcast: %s | Episode: %d | Host: %s | Channel: %s | Deskripsi: %s",
                    getTitle(), episodeNumber, host, channelName,
                    description.isEmpty() ? "-" : description);
        }
        return displayInfo(); // Delegasi ke versi ringkas
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info podcast dalam format tertentu.
     * Format output disesuaikan untuk konteks podcast.
     *
     * <p>
     * <b>Implementasi dari:</b> {@link AudioMedia#displayInfo(String)}
     * </p>
     *
     * @param format format output ("short", "full", atau "csv")
     * @return info podcast sesuai format
     */
    @Override
    public String displayInfo(String format) {
        switch (format.toLowerCase()) {
            case "short":
                return getTitle() + " Ep." + episodeNumber;
            case "full":
                return String.format(
                        "[Podcast] %s | Ep. %d | Host: %s | Channel: %s | Deskripsi: %s",
                        getTitle(), episodeNumber, host, channelName,
                        description.isEmpty() ? "-" : description);
            case "csv":
                return String.format("%s,%d,%s,%s,%s",
                        getTitle(), episodeNumber, host, channelName, description);
            default:
                return displayInfo();
        }
    }

    // ── Override: toString() dari AudioMedia ──

    /**
     * Menimpa (override) method toString() dari abstract class AudioMedia.
     * Menampilkan format khusus podcast.
     *
     * <p>
     * <b>Override dari:</b> {@link AudioMedia#toString()}
     * </p>
     *
     * @return representasi string dengan format "[Podcast] Judul - Ep. X"
     */
    @Override
    public String toString() {
        return "[Podcast] " + getTitle() + " - Ep. " + episodeNumber;
    }
}
