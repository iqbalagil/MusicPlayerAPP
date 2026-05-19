/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp.model;

import java.io.File;

/**
 * Class Audiobook merepresentasikan file audio bertipe buku audio.
 * Merupakan implementasi konkret dari abstract class {@link AudioMedia}.
 *
 * <p><b>Penerapan Abstraksi:</b></p>
 * <ul>
 *   <li>{@code extends AudioMedia} — mewarisi field dan method umum dari abstract class</li>
 *   <li>Mengimplementasikan semua abstract method: displayInfo(), getMediaType(), getSummary()</li>
 *   <li>Menambahkan field khusus audiobook: author, narrator, chapterNumber, totalChapters</li>
 * </ul>
 *
 * <p><b>Penerapan Enkapsulasi:</b> semua field tambahan bersifat private,
 * akses dilakukan melalui getter dan setter dengan validasi.</p>
 *
 * @author iqbalagil
 * @see AudioMedia
 */
public class Audiobook extends AudioMedia {

    // ── Field Private Tambahan (Enkapsulasi) ──

    /** Nama penulis buku asli */
    private String author;

    /** Nama narator / pembaca buku audio */
    private String narrator;

    /** Nomor bab yang sedang diputar */
    private int chapterNumber;

    /** Total jumlah bab dalam audiobook */
    private int totalChapters;

    /** Genre audiobook */
    private String genre;

    // ── Konstruktor ──

    /**
     * Membuat objek Audiobook dari file audio.
     * Memanggil konstruktor abstract parent {@link AudioMedia#AudioMedia(File)}
     * untuk inisialisasi field umum (file, title, totalFrames, dll).
     * Field khusus audiobook diisi dengan nilai default.
     *
     * @param file referensi file audio audiobook
     */
    public Audiobook(File file) {
        // Memanggil konstruktor abstract class AudioMedia
        super(file);

        // Inisialisasi field khusus audiobook dengan nilai default
        this.author = "Tidak Diketahui";
        this.narrator = "Tidak Diketahui";
        this.chapterNumber = 1;
        this.totalChapters = 1;
        this.genre = "Audiobook";
    }

    /**
     * Konstruktor lengkap Audiobook dengan informasi detail.
     * Memanggil konstruktor sederhana lalu mengatur field tambahan.
     *
     * @param file          referensi file audio
     * @param author        nama penulis buku
     * @param narrator      nama narator
     * @param chapterNumber nomor bab saat ini
     * @param totalChapters total jumlah bab
     */
    public Audiobook(File file, String author, String narrator,
            int chapterNumber, int totalChapters) {
        this(file);
        setAuthor(author);
        setNarrator(narrator);
        setTotalChapters(totalChapters);
        setChapterNumber(chapterNumber);
    }

    // ── Implementasi Abstract Method dari AudioMedia ──
    // Method-method berikut WAJIB diimplementasikan karena AudioMedia
    // mendefinisikannya sebagai abstract.

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan jenis media.
     * Audiobook mengembalikan "Audiobook" sebagai identifikasi tipe.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#getMediaType()}</p>
     *
     * @return string "Audiobook"
     */
    @Override
    public String getMediaType() {
        return "Audiobook";
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Mengembalikan deskripsi singkat audiobook.
     * Format: "Audiobook 'Judul' oleh Penulis, dibacakan oleh Narator (Bab X/Y)"
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#getSummary()}</p>
     *
     * @return string deskripsi singkat
     */
    @Override
    public String getSummary() {
        return String.format("Audiobook '%s' oleh %s, dibacakan oleh %s (Bab %d/%d)",
                title, author, narrator, chapterNumber, totalChapters);
    }

    // ── Getter (Akses Terkontrol) ──

    /**
     * Mengambil nama penulis buku.
     *
     * @return nama penulis
     */
    public String getAuthor() {
        return author;
    }

    /**
     * Mengambil nama narator.
     *
     * @return nama narator
     */
    public String getNarrator() {
        return narrator;
    }

    /**
     * Mengambil nomor bab saat ini.
     *
     * @return nomor bab
     */
    public int getChapterNumber() {
        return chapterNumber;
    }

    /**
     * Mengambil total jumlah bab.
     *
     * @return total bab
     */
    public int getTotalChapters() {
        return totalChapters;
    }

    /**
     * Mengambil genre audiobook.
     *
     * @return genre audiobook
     */
    public String getGenre() {
        return genre;
    }

    /**
     * Menghitung persentase progres baca berdasarkan bab.
     *
     * @return persentase progres (0-100)
     */
    public int getProgressPercent() {
        if (totalChapters <= 0) {
            return 0;
        }
        return (int) ((double) chapterNumber / totalChapters * 100);
    }

    /**
     * Mengambil informasi lengkap audiobook dalam format teks.
     *
     * @return string informasi lengkap audiobook
     */
    public String getFullInfo() {
        return String.format("Audiobook: %s | Penulis: %s | Narator: %s | Bab %d/%d (%d%%)",
                getTitle(), author, narrator, chapterNumber, totalChapters, getProgressPercent());
    }

    // ── Setter dengan Validasi (Enkapsulasi) ──

    /**
     * Mengatur nama penulis buku.
     * Validasi: jika null atau kosong, diisi "Tidak Diketahui".
     *
     * @param author nama penulis baru
     */
    public void setAuthor(String author) {
        if (author != null && !author.trim().isEmpty()) {
            this.author = author.trim();
        } else {
            this.author = "Tidak Diketahui";
        }
    }

    /**
     * Mengatur nama narator audiobook.
     * Validasi: jika null atau kosong, diisi "Tidak Diketahui".
     *
     * @param narrator nama narator baru
     */
    public void setNarrator(String narrator) {
        if (narrator != null && !narrator.trim().isEmpty()) {
            this.narrator = narrator.trim();
        } else {
            this.narrator = "Tidak Diketahui";
        }
    }

    /**
     * Mengatur nomor bab saat ini.
     * Validasi: harus positif dan tidak melebihi total bab.
     *
     * @param chapterNumber nomor bab baru
     */
    public void setChapterNumber(int chapterNumber) {
        if (chapterNumber > 0 && chapterNumber <= this.totalChapters) {
            this.chapterNumber = chapterNumber;
        }
    }

    /**
     * Mengatur total jumlah bab dalam audiobook.
     * Validasi: harus bernilai positif (minimal 1).
     * Jika total bab dikurangi di bawah bab saat ini,
     * bab saat ini akan disesuaikan.
     *
     * @param totalChapters total bab baru
     */
    public void setTotalChapters(int totalChapters) {
        if (totalChapters > 0) {
            this.totalChapters = totalChapters;
            // Sesuaikan chapterNumber jika melebihi total baru
            if (this.chapterNumber > totalChapters) {
                this.chapterNumber = totalChapters;
            }
        }
    }

    /**
     * Mengatur genre audiobook.
     * Validasi: jika null atau kosong, diisi "Audiobook".
     *
     * @param genre genre baru
     */
    public void setGenre(String genre) {
        if (genre != null && !genre.trim().isEmpty()) {
            this.genre = genre.trim();
        } else {
            this.genre = "Audiobook";
        }
    }

    /**
     * Berpindah ke bab selanjutnya jika belum di bab terakhir.
     *
     * @return true jika berhasil pindah, false jika sudah di bab terakhir
     */
    public boolean nextChapter() {
        if (chapterNumber < totalChapters) {
            chapterNumber++;
            return true;
        }
        return false;
    }

    /**
     * Berpindah ke bab sebelumnya jika belum di bab pertama.
     *
     * @return true jika berhasil pindah, false jika sudah di bab pertama
     */
    public boolean previousChapter() {
        if (chapterNumber > 1) {
            chapterNumber--;
            return true;
        }
        return false;
    }

    // ── Overloading: setChapterInfo() ──
    // Overloading = method dengan nama sama tapi parameter berbeda.
    // Di Audiobook, kita overload setChapterInfo() sesuai tema buku audio:
    // - Versi 1: hanya nomor bab
    // - Versi 2: nomor bab + total bab
    // - Versi 3: nomor bab + total bab + narator

    /**
     * Mengatur informasi bab — hanya nomor bab saat ini.
     *
     * <p><b>Overloading #1:</b> setChapterInfo(int) — 1 parameter</p>
     *
     * @param chapterNumber nomor bab yang sedang dibaca
     */
    public void setChapterInfo(int chapterNumber) {
        setChapterNumber(chapterNumber);
    }

    /**
     * Mengatur informasi bab — nomor bab dan total bab.
     *
     * <p><b>Overloading #2:</b> setChapterInfo(int, int) — 2 parameter</p>
     *
     * @param chapterNumber nomor bab saat ini
     * @param totalChapters total jumlah bab dalam audiobook
     */
    public void setChapterInfo(int chapterNumber, int totalChapters) {
        setTotalChapters(totalChapters);
        setChapterNumber(chapterNumber);
    }

    /**
     * Mengatur informasi bab — nomor bab, total bab, dan narator.
     *
     * <p><b>Overloading #3:</b> setChapterInfo(int, int, String) — 3 parameter</p>
     *
     * @param chapterNumber nomor bab saat ini
     * @param totalChapters total jumlah bab
     * @param narrator      nama narator/pembaca
     */
    public void setChapterInfo(int chapterNumber, int totalChapters, String narrator) {
        setTotalChapters(totalChapters);
        setChapterNumber(chapterNumber);
        setNarrator(narrator);
    }

    // ── Implementasi Abstract: displayInfo() (Overloading) ──
    // Ketiga versi displayInfo() merupakan implementasi dari abstract method
    // yang didefinisikan di AudioMedia. Sekaligus menunjukkan overloading.

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info ringkas audiobook.
     * Format: "[Audiobook] Judul - Bab X/Y (Z%)"
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo()}</p>
     *
     * @return info ringkas audiobook
     */
    @Override
    public String displayInfo() {
        return String.format("[Audiobook] %s - Bab %d/%d (%d%%)",
                getTitle(), chapterNumber, totalChapters, getProgressPercent());
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info audiobook dengan opsi detail.
     * Jika detailed=true, menampilkan penulis, narator, dan progres baca.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo(boolean)}</p>
     *
     * @param detailed true untuk info lengkap audiobook
     * @return info audiobook sesuai level detail
     */
    @Override
    public String displayInfo(boolean detailed) {
        if (detailed) {
            return String.format(
                    "Audiobook: %s | Penulis: %s | Narator: %s | Bab: %d/%d | Progres: %d%%",
                    getTitle(), author, narrator, chapterNumber, totalChapters,
                    getProgressPercent());
        }
        return displayInfo(); // Delegasi ke versi ringkas
    }

    /**
     * [IMPLEMENTASI ABSTRACT] Menampilkan info audiobook dalam format tertentu.
     * Format output disesuaikan untuk konteks audiobook.
     *
     * <p><b>Implementasi dari:</b> {@link AudioMedia#displayInfo(String)}</p>
     *
     * @param format format output ("short", "full", atau "csv")
     * @return info audiobook sesuai format
     */
    @Override
    public String displayInfo(String format) {
        switch (format.toLowerCase()) {
            case "short":
                return getTitle() + " Bab " + chapterNumber;
            case "full":
                return String.format(
                        "[Audiobook] %s | Penulis: %s | Narator: %s | Bab %d/%d | Progres: %d%%",
                        getTitle(), author, narrator, chapterNumber, totalChapters,
                        getProgressPercent());
            case "csv":
                return String.format("%s,%s,%s,%d,%d,%d",
                        getTitle(), author, narrator, chapterNumber, totalChapters,
                        getProgressPercent());
            default:
                return displayInfo();
        }
    }

    // ── Override: toString() dari AudioMedia ──

    /**
     * Menimpa (override) method toString() dari abstract class AudioMedia.
     * Menampilkan format khusus audiobook.
     *
     * <p><b>Override dari:</b> {@link AudioMedia#toString()}</p>
     *
     * @return representasi string dengan format "[Audiobook] Judul - Bab X/Y"
     */
    @Override
    public String toString() {
        return "[Audiobook] " + getTitle() + " - Bab " + chapterNumber + "/" + totalChapters;
    }
}
