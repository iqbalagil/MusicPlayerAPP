/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.io.File;

/**
 *
 * @author iqbalagil
 */
public class Music {
    /** Digunakan untuk memuat referensi alamat dari file music/mp3 */
    private File file;
    /** Unutk memuat nama music*/
    private String title;
    /**
     * Untuk mengkontruksi music object dari file yang di upload
     * @param mendapatkan alamat referensi dari file yang di upload
     */
    public Music(File file){
        this.file = file;
        this.title = file.getName().replace(".mp3", "");
    }
    /**
     * mengembalikan alamat dari music ini
     * @return mp3 file
     */
    public File getFile(){
        return file;
    }
    /**
     * mengembalikan judul lagu dari musik mp3
     * @return judul lagu
     */
    public String getTitle(){
        return title;
    }
    /**
     * mengembalikan alamat lengkap dari file mp3
     * @return alamat lengkap file mp3
     */
    public String getFullPathFile(){
        return file.getAbsolutePath();
    }
    /**
     * mengembalikan judul lagu jika objek di tampilkan
     * @return mereturn judul lagu
     */
    
    @Override
    public String toString(){
        return title;
    }
}
