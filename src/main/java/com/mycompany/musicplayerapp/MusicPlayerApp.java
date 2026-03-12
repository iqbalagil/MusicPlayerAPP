/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 */

package com.mycompany.musicplayerapp;

import javax.swing.SwingUtilities;

/**
 *
 * @author iqbalagil
 */
public class MusicPlayerApp {

    public static void main(String[] args) {
        /**
         * Penggunaan SwingUtilites pada program ini
         * berfungsi untuk mencegah terjadinya tabrakan antara
         * GUI dengan logika program yang dijalankan nantinya.
         */
        SwingUtilities.invokeLater(new Runnable(){
        @Override
        public void run(){
            new MusicPlayerGUI();
        }
        });
    }
}
