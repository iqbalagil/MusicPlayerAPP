/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 * Class Podcast merepresentasikan file audio bertipe podcast.
 * Merupakan turunan (inheritance) dari class Music, karena podcast
 * memiliki kesamaan dasar (file, judul, durasi) namun dengan
 * atribut tambahan khusus podcast.
 *
 * <p><b>Penerapan Inheritance:</b></p>
 * <ul>
 *   <li>{@code extends Music} — mewarisi semua field dan method dari Music</li>
 *   <li>{@code super(file)} — memanggil konstruktor induk untuk inisialisasi dasar</li>
 *   <li>{@code @Override toString()} — menimpa representasi string dari Music</li>
 *   <li>Menambahkan field khusus: host, episodeNumber, channelName, description</li>
 * </ul>
 *
 * @author iqbalagil
 * @see Music
 */
public class Podcast extends Music {

    // ── Field Private Tambahan (Enkapsulasi + Inheritance) ──

    /** Nama pembawa acara / host podcast */
    private String host;

    /** Nomor episode podcast */
    private int episodeNumber;

    /** Nama channel / saluran podcast */
    private String channelName;

    /** Deskripsi singkat episode */
    private String description;

    // ── Konstruktor ──

    /**
     * Membuat objek Podcast dari file audio.
     * Memanggil konstruktor induk {@link Music#Music(File)} untuk
     * menginisialisasi field dasar (file, title, artist, genre, dll).
     * Field khusus podcast diisi dengan nilai default.
     *
     * @param file referensi file audio podcast
     */
    public Podcast(File file) {
        // Memanggil konstruktor parent class Music
        super(file);

        // Inisialisasi field khusus podcast dengan nilai default
        this.host = "Tidak Diketahui";
        this.episodeNumber = 1;
        this.channelName = "Tidak Diketahui";
        this.description = "";

        // Set genre default untuk podcast (override dari Music)
        setGenre("Podcast");
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
        // Memanggil konstruktor Podcast(File) yang sudah memanggil super(file)
        this(file);
        setHost(host);
        setChannelName(channelName);
        setEpisodeNumber(episodeNumber);
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
     * Mengambil informasi lengkap podcast dalam format teks.
     * Method ini khusus milik Podcast, tidak ada di parent Music.
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

    // ── Overloading: setEpisodeInfo() ──
    // Overloading = method dengan nama sama tapi parameter berbeda.
    // Di Podcast, kita overload setEpisodeInfo() sesuai tema podcast:
    // - Versi 1: hanya nomor episode
    // - Versi 2: nomor episode + deskripsi

    /**
     * Mengatur informasi episode — hanya nomor episode.
     *
     * <p><b>Overloading #1:</b> setEpisodeInfo(int) — 1 parameter</p>
     *
     * @param episodeNumber nomor episode baru
     */
    public void setEpisodeInfo(int episodeNumber) {
        setEpisodeNumber(episodeNumber);
    }

    /**
     * Mengatur informasi episode — nomor episode dan deskripsi.
     *
     * <p><b>Overloading #2:</b> setEpisodeInfo(int, String) — 2 parameter</p>
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
     * <p><b>Overloading #3:</b> setEpisodeInfo(int, String, String) — 3 parameter</p>
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

    // ── Overriding: displayInfo() dari Parent Music ──
    // Overriding = subclass menimpa implementasi method yang sudah
    // didefinisikan di superclass. Podcast menimpa displayInfo() dari Music
    // untuk menambahkan informasi khusus podcast (host, episode, channel).

    /**
     * Menimpa (override) displayInfo() dari Music.
     * Music menampilkan "Judul - Artis",
     * Podcast menampilkan format khusus dengan host dan episode.
     *
     * <p><b>Override dari:</b> {@link Music#displayInfo()}</p>
     *
     * @return info ringkas podcast
     */
    @Override
    public String displayInfo() {
        return String.format("[Podcast] %s - Ep. %d (Host: %s)",
                getTitle(), episodeNumber, host);
    }

    /**
     * Menimpa (override) displayInfo(boolean) dari Music.
     * Versi Podcast menambahkan channel, deskripsi jika detailed=true.
     *
     * <p><b>Override dari:</b> {@link Music#displayInfo(boolean)}</p>
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
     * Menimpa (override) displayInfo(String) dari Music.
     * Format output disesuaikan untuk konteks podcast.
     *
     * <p><b>Override dari:</b> {@link Music#displayInfo(String)}</p>
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

    // ── Override: toString() dari Parent Music ──

    /**
     * Menimpa (override) method toString() dari parent class Music.
     * Music.toString() hanya mengembalikan judul,
     * sedangkan Podcast.toString() menampilkan format khusus podcast.
     *
     * <p><b>Override dari:</b> {@link Music#toString()}</p>
     *
     * @return representasi string dengan format "[Podcast] Judul - Ep. X"
     */
    @Override
    public String toString() {
        return "[Podcast] " + getTitle() + " - Ep. " + episodeNumber;
    }
}
