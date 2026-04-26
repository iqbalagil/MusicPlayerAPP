/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 * Class Audiobook merepresentasikan file audio bertipe buku audio.
 * Merupakan turunan (inheritance) dari class Music, karena audiobook
 * memiliki kesamaan dasar (file, judul, durasi) namun dengan
 * atribut tambahan khusus buku audio.
 *
 * <p>
 * <b>Penerapan Inheritance:</b>
 * </p>
 * <ul>
 * <li>{@code extends Music} — mewarisi semua field dan method dari Music</li>
 * <li>{@code super(file)} — memanggil konstruktor induk untuk inisialisasi
 * dasar</li>
 * <li>{@code @Override toString()} — menimpa representasi string dari
 * Music</li>
 * <li>Menambahkan field khusus: author, narrator, chapterNumber,
 * totalChapters</li>
 * </ul>
 *
 * @author iqbalagil
 * @see Music
 */
public class Audiobook extends Music {

    // ── Field Private Tambahan (Enkapsulasi + Inheritance) ──

    /** Nama penulis buku asli */
    private String author;

    /** Nama narator / pembaca buku audio */
    private String narrator;

    /** Nomor bab yang sedang diputar */
    private int chapterNumber;

    /** Total jumlah bab dalam audiobook */
    private int totalChapters;

    // ── Konstruktor ──

    /**
     * Membuat objek Audiobook dari file audio.
     * Memanggil konstruktor induk {@link Music#Music(File)} untuk
     * menginisialisasi field dasar (file, title, artist, genre, dll).
     * Field khusus audiobook diisi dengan nilai default.
     *
     * @param file referensi file audio audiobook
     */
    public Audiobook(File file) {
        // Memanggil konstruktor parent class Music
        super(file);

        // Inisialisasi field khusus audiobook dengan nilai default
        this.author = "Tidak Diketahui";
        this.narrator = "Tidak Diketahui";
        this.chapterNumber = 1;
        this.totalChapters = 1;

        // Set genre default untuk audiobook (override dari Music)
        setGenre("Audiobook");
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
        // Memanggil konstruktor Audiobook(File) yang sudah memanggil super(file)
        this(file);
        setAuthor(author);
        setNarrator(narrator);
        setTotalChapters(totalChapters);
        setChapterNumber(chapterNumber);
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
     * Menghitung persentase progres baca berdasarkan bab.
     * Method ini khusus milik Audiobook, tidak ada di parent Music.
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
     * Method ini khusus milik Audiobook, tidak ada di parent Music.
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
     * <p>
     * <b>Overloading #1:</b> setChapterInfo(int) — 1 parameter
     * </p>
     *
     * @param chapterNumber nomor bab yang sedang dibaca
     */
    public void setChapterInfo(int chapterNumber) {
        setChapterNumber(chapterNumber);
    }

    /**
     * Mengatur informasi bab — nomor bab dan total bab.
     *
     * <p>
     * <b>Overloading #2:</b> setChapterInfo(int, int) — 2 parameter
     * </p>
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
     * <p>
     * <b>Overloading #3:</b> setChapterInfo(int, int, String) — 3 parameter
     * </p>
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

    // ── Overriding: displayInfo() dari Parent Music ──
    // Overriding = subclass menimpa implementasi method yang sudah
    // didefinisikan di superclass. Audiobook menimpa displayInfo() dari Music
    // untuk menambahkan informasi khusus audiobook (penulis, narator, bab).

    /**
     * Menimpa (override) displayInfo() dari Music.
     * Music menampilkan "Judul - Artis",
     * Audiobook menampilkan format khusus dengan penulis dan progres bab.
     *
     * <p>
     * <b>Override dari:</b> {@link Music#displayInfo()}
     * </p>
     *
     * @return info ringkas audiobook
     */
    @Override
    public String displayInfo() {
        return String.format("[Audiobook] %s - Bab %d/%d (%d%%)",
                getTitle(), chapterNumber, totalChapters, getProgressPercent());
    }

    /**
     * Menimpa (override) displayInfo(boolean) dari Music.
     * Versi Audiobook menambahkan penulis, narator, dan progres baca.
     *
     * <p>
     * <b>Override dari:</b> {@link Music#displayInfo(boolean)}
     * </p>
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
     * Menimpa (override) displayInfo(String) dari Music.
     * Format output disesuaikan untuk konteks audiobook.
     *
     * <p>
     * <b>Override dari:</b> {@link Music#displayInfo(String)}
     * </p>
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

    // ── Override: toString() dari Parent Music ──

    /**
     * Menimpa (override) method toString() dari parent class Music.
     * Music.toString() hanya mengembalikan judul,
     * sedangkan Audiobook.toString() menampilkan format khusus audiobook.
     *
     * <p>
     * <b>Override dari:</b> {@link Music#toString()}
     * </p>
     *
     * @return representasi string dengan format "[Audiobook] Judul - Bab X/Y"
     */
    @Override
    public String toString() {
        return "[Audiobook] " + getTitle() + " - Bab " + chapterNumber + "/" + totalChapters;
    }
}
