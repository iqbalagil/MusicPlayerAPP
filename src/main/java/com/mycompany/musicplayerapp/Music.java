/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 * Class Music merepresentasikan satu entitas lagu MP3.
 * Merupakan implementasi konkret dari abstract class {@link AudioMedia}.
 *
 * <p><b>Penerapan Abstraksi:</b></p>
 * <ul>
 *   <li>{@code extends AudioMedia} — mewarisi field dan method umum dari abstract class</li>
 *   <li>Mengimplementasikan semua abstract method: displayInfo(), getMediaType(), getSummary()</li>
 *   <li>Menambahkan field khusus musik: artist, genre</li>
 * </ul>
 *
 * <p><b>Penerapan Enkapsulasi:</b> semua field bersifat private,
 * akses dilakukan melalui getter dan setter dengan validasi.</p>
 *
 * @author iqbalagil
 * @see AudioMedia
 */
public class Music extends AudioMedia {

    // ── Field Private (Enkapsulasi) ──

    /** Nama artis/penyanyi */
    private String artist;

    /** Genre musik */
    private String genre;

    // ── Konstruktor ──

    /**
     * Membuat objek Music dari file MP3 yang diunggah.
     * Memanggil konstruktor abstract parent {@link AudioMedia#AudioMedia(File)}
     * untuk inisialisasi field umum (file, title, totalFrames, dll).
     * Judul otomatis diambil dari nama file tanpa ekstensi.
     *
     * @param file referensi file MP3
     */
    public Music(File file) {
        // Memanggil konstruktor abstract class AudioMedia
        super(file);
        this.artist = "Tidak Diketahui";
        this.genre = "Umum";
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

    // ── Implementasi Abstract Method dari AudioMedia ──
    // Method-method berikut WAJIB diimplementasikan karena AudioMedia
    // mendefinisikannya sebagai abstract. Tanpa implementasi ini,
    // class Music tidak akan bisa di-compile.

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan jenis media.
     * Music mengembalikan "Musik" sebagai identifikasi tipe.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#getMediaType()}</p>
     *
     * @return string "Musik"
     */
    @Override
    public String getMediaType() {
        return "Musik";
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan deskripsi singkat musik.
     * Format: "Lagu 'Judul' oleh Artis"
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#getSummary()}</p>
     *
     * @return string deskripsi singkat
     */
    @Override
    public String getSummary() {
        return String.format("Lagu '%s' oleh %s", title, artist);
    }

    // ── Getter (Akses Terkontrol) ──

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

    // ── Setter dengan Validasi (Enkapsulasi) ──

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

    // ── Implementasi Abstract: displayInfo() (Overloading) ──
    // Ketiga versi displayInfo() merupakan implementasi dari abstract method
    // yang didefinisikan di AudioMedia. Sekaligus menunjukkan overloading
    // (method nama sama, parameter berbeda) di dalam satu class.

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info ringkas musik (judul dan artis saja).
     * Versi tanpa parameter — bentuk paling sederhana.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo()}</p>
     * <p><b>Overloading #1:</b> displayInfo() — tanpa parameter</p>
     *
     * @return string info ringkas "Judul - Artis"
     */
    @Override
    public String displayInfo() {
        return title + " - " + artist;
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info musik dengan opsi detail.
     * Jika detailed=true, menyertakan genre dan status favorit.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo(boolean)}</p>
     * <p><b>Overloading #2:</b> displayInfo(boolean) — 1 parameter</p>
     *
     * @param detailed true untuk info lengkap, false untuk ringkas
     * @return string info sesuai level detail
     */
    @Override
    public String displayInfo(boolean detailed) {
        if (detailed) {
            return String.format("Judul: %s | Artis: %s | Genre: %s | Favorit: %s",
                    title, artist, genre, favorite ? "Ya" : "Tidak");
        }
        return displayInfo(); // Delegasi ke versi tanpa parameter
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info musik dalam format tertentu.
     * Format yang didukung: "short", "full", "csv".
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo(String)}</p>
     * <p><b>Overloading #3:</b> displayInfo(String) — parameter tipe berbeda</p>
     *
     * @param format format output ("short", "full", atau "csv")
     * @return string info sesuai format yang diminta
     */
    @Override
    public String displayInfo(String format) {
        switch (format.toLowerCase()) {
            case "short":
                return title;
            case "full":
                return String.format("[Musik] %s oleh %s (Genre: %s) | Favorit: %s | Durasi: %s",
                        title, artist, genre, favorite ? "Ya" : "Tidak", getFormattedDuration());
            case "csv":
                return String.format("%s,%s,%s,%s,%d",
                        title, artist, genre, favorite ? "Ya" : "Tidak", durationPerSeconds);
            default:
                return displayInfo(); // Default ke versi ringkas
        }
    }

    // ── Overriding: toString() dari AudioMedia ──

    /**
     * Menimpa (override) method toString() dari abstract class AudioMedia.
     * Mengembalikan judul lagu sebagai representasi string.
     *
     * <p><b>Override dari:</b> {@link AudioMedia#toString()}</p>
     *
     * @return judul lagu
     */
    @Override
    public String toString() {
        return title;
    }
}
