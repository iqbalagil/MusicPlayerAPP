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

    // ── Overloading: displayInfo() ──
    // Overloading = method dengan nama sama tapi parameter berbeda
    // dalam satu class. Compiler memilih versi yang tepat berdasarkan
    // argumen yang diberikan saat pemanggilan.

    /**
     * Menampilkan info ringkas musik (judul dan artis saja).
     * Versi tanpa parameter — bentuk paling sederhana.
     *
     * <p><b>Overloading #1:</b> displayInfo() — tanpa parameter</p>
     *
     * @return string info ringkas "Judul - Artis"
     */
    public String displayInfo() {
        return title + " - " + artist;
    }

    /**
     * Menampilkan info musik dengan opsi detail.
     * Jika detailed=true, menyertakan genre dan status favorit.
     *
     * <p><b>Overloading #2:</b> displayInfo(boolean) — 1 parameter</p>
     *
     * @param detailed true untuk info lengkap, false untuk ringkas
     * @return string info sesuai level detail
     */
    public String displayInfo(boolean detailed) {
        if (detailed) {
            return String.format("Judul: %s | Artis: %s | Genre: %s | Favorit: %s",
                    title, artist, genre, favorite ? "Ya" : "Tidak");
        }
        return displayInfo(); // Delegasi ke versi tanpa parameter
    }

    /**
     * Menampilkan info musik dalam format tertentu.
     * Format yang didukung: "short", "full", "csv".
     *
     * <p><b>Overloading #3:</b> displayInfo(String) — parameter tipe berbeda</p>
     *
     * @param format format output ("short", "full", atau "csv")
     * @return string info sesuai format yang diminta
     */
    public String displayInfo(String format) {
        switch (format.toLowerCase()) {
            case "short":
                return title;
            case "full":
                return String.format("[Musik] %s oleh %s (Genre: %s) | Favorit: %s | Durasi: %d detik",
                        title, artist, genre, favorite ? "Ya" : "Tidak", durationPerSeconds);
            case "csv":
                return String.format("%s,%s,%s,%s,%d",
                        title, artist, genre, favorite ? "Ya" : "Tidak", durationPerSeconds);
            default:
                return displayInfo(); // Default ke versi ringkas
        }
    }

    // ── Overriding: toString(), equals(), hashCode() ──
    // Overriding = subclass menimpa implementasi method yang sudah
    // didefinisikan di superclass (dalam hal ini java.lang.Object).
    // Ditandai dengan anotasi @Override.

    /**
     * Menimpa (override) method toString() dari class Object.
     * Mengembalikan judul lagu sebagai representasi string.
     *
     * <p><b>Override dari:</b> {@link Object#toString()}</p>
     *
     * @return judul lagu
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Menimpa (override) method equals() dari class Object.
     * Dua objek Music dianggap sama jika path file-nya identik.
     *
     * <p><b>Override dari:</b> {@link Object#equals(Object)}</p>
     * <p>Konsisten dengan {@link #hashCode()} — kontrak Java.</p>
     *
     * @param obj objek pembanding
     * @return true jika path file sama
     */
    @Override
    public boolean equals(Object obj) {
        // Referensi sama = pasti sama
        if (this == obj) return true;
        // Null atau tipe berbeda = pasti beda
        if (obj == null || getClass() != obj.getClass()) return false;
        Music other = (Music) obj;
        return this.getFullPathFile().equals(other.getFullPathFile());
    }

    /**
     * Menimpa (override) method hashCode() dari class Object.
     * Menghasilkan hash berdasarkan path file, konsisten dengan equals().
     *
     * <p><b>Override dari:</b> {@link Object#hashCode()}</p>
     *
     * @return nilai hash berdasarkan path file
     */
    @Override
    public int hashCode() {
        return getFullPathFile().hashCode();
    }
}
