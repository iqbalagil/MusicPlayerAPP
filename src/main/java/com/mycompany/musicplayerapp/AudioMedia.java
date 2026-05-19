/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 *
 * @author iqbalagil
 * @see Music
 * @see Podcast
 * @see Audiobook
 */
public abstract class AudioMedia {

    // ── Field Protected (Aksesibel oleh Subclass) ──

    /** Referensi file audio di sistem */
    protected File file;

    /** Judul media audio */
    protected String title;

    /** Total frame audio */
    protected long totalFrames;

    /** Durasi media dalam detik */
    protected int durationPerSeconds;

    /** Status favorit */
    protected boolean favorite;

    // ── Konstruktor ──

    /**
     * Konstruktor dasar AudioMedia.
     * Dipanggil oleh subclass melalui {@code super(file)} untuk
     * menginisialisasi field umum yang dimiliki semua media audio.
     *
     * @param file referensi file audio
     */
    public AudioMedia(File file) {
        this.file = file;
        this.title = parseTitle(file.getName());
        this.totalFrames = 0;
        this.durationPerSeconds = 0;
        this.favorite = false;
    }

    /**
     * [ABSTRACT] Menampilkan info ringkas media audio.
     * Setiap jenis media memiliki format info yang berbeda:
     * - Music: "Judul - Artis"
     * - Podcast: "[Podcast] Judul - Ep. X (Host: ...)"
     * - Audiobook: "[Audiobook] Judul - Bab X/Y"
     *
     * @return string info ringkas sesuai jenis media
     */
    public abstract String displayInfo();

    /**
     * [ABSTRACT] Menampilkan info media dengan opsi detail.
     * Subclass menentukan informasi apa yang ditampilkan saat
     * mode detail aktif (misalnya genre, host, penulis, dll).
     *
     * @param detailed true untuk info lengkap, false untuk ringkas
     * @return string info sesuai level detail
     */
    public abstract String displayInfo(boolean detailed);

    /**
     * [ABSTRACT] Menampilkan info media dalam format tertentu.
     * Format yang umum didukung: "short", "full", "csv".
     * Implementasi spesifik tergantung jenis media.
     *
     * @param format format output ("short", "full", "csv")
     * @return string info sesuai format yang diminta
     */
    public abstract String displayInfo(String format);

    /**
     * [ABSTRACT] Mengembalikan jenis/kategori media audio.
     * Digunakan untuk identifikasi tipe media secara programatis.
     * Contoh: "Musik", "Podcast", "Audiobook".
     *
     * @return string nama jenis media
     */
    public abstract String getMediaType();

    /**
     * [ABSTRACT] Mengembalikan deskripsi singkat media.
     * Memberikan ringkasan satu baris tentang media ini,
     * berbeda dengan displayInfo() yang lebih terstruktur.
     *
     * @return string deskripsi singkat media
     */
    public abstract String getSummary();

    // ── Method Private (Utilitas Internal) ──

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

    /**
     * Mengambil referensi file audio.
     * Method ini sudah memiliki implementasi (concrete), sehingga
     * subclass tidak wajib override — cukup mewarisinya.
     *
     * @return objek File media audio
     */
    public File getFile() {
        return file;
    }

    /**
     * Mengambil judul media audio.
     *
     * @return judul media tanpa ekstensi
     */
    public String getTitle() {
        return title;
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
     * Mengambil durasi media.
     *
     * @return durasi dalam detik
     */
    public int getDurationPerSeconds() {
        return durationPerSeconds;
    }

    /**
     * Mengecek apakah media ditandai sebagai favorit.
     *
     * @return true jika favorit
     */
    public boolean isFavorite() {
        return favorite;
    }

    /**
     * Mengambil alamat lengkap file audio di sistem.
     *
     * @return path absolut file
     */
    public String getFullPathFile() {
        return file.getAbsolutePath();
    }

    // ── Concrete Setter dengan Validasi ──

    /**
     * Mengatur judul media audio secara manual.
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
     * Mengatur durasi media.
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
     * Mengatur status favorit media.
     *
     * @param favorite true untuk menandai favorit
     */
    public void setFavorite(boolean favorite) {
        this.favorite = favorite;
    }

    // ── Concrete Method: Utilitas Umum ──

    /**
     * Mengkonversi durasi detik ke format "MM:SS".
     * Concrete method yang bisa langsung digunakan semua subclass
     * tanpa perlu override.
     *
     * @return string durasi dalam format "MM:SS"
     */
    public String getFormattedDuration() {
        int minutes = durationPerSeconds / 60;
        int seconds = durationPerSeconds % 60;
        return String.format("%02d:%02d", minutes, seconds);
    }

    /**
     * Menimpa (override) method toString() dari class Object.
     * Mengembalikan judul sebagai representasi string default.
     * Subclass bisa override lagi jika perlu format berbeda.
     *
     * @return judul media
     */
    @Override
    public String toString() {
        return title;
    }

    /**
     * Menimpa (override) method equals() dari class Object.
     * Dua objek AudioMedia dianggap sama jika path file-nya identik.
     *
     * @param obj objek pembanding
     * @return true jika path file sama
     */
    @Override
    public boolean equals(Object obj) {
        if (this == obj)
            return true;
        if (obj == null || !(obj instanceof AudioMedia))
            return false;
        AudioMedia other = (AudioMedia) obj;
        return this.getFullPathFile().equals(other.getFullPathFile());
    }

    /**
     * Menimpa (override) method hashCode() dari class Object.
     * Menghasilkan hash berdasarkan path file, konsisten dengan equals().
     *
     * @return nilai hash berdasarkan path file
     */
    @Override
    public int hashCode() {
        return getFullPathFile().hashCode();
    }
}
