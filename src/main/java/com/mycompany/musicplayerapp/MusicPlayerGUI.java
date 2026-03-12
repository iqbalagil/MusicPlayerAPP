/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.awt.event.*;
import javax.swing.*;
import java.awt.*;
import java.io.File;
import java.util.ArrayList;

/**
 *
 * @author iqbalagil
 */
public class MusicPlayerGUI extends JFrame {

    /**
     * List untuk semua music yang di upload
     */
    private ArrayList<Music> musicList;

    /**
     * Object yang menghandle semua function dari Music Player
     */
    private MusicPlayer player;

    /**
     * Label untuk menampilkan judul lagu yang sedang di muat sekarang
     */
    private JLabel nowPlayingLabel;

    /**
     * Button untuk mentoggle pause dan start
     */
    private JButton playPauseButton;

    /**
     * Menu "Putar Musik" yang menampilkan lagu-lagu yang telah diunggah dalam
     * bentuk grid
     */
    private JMenu playMusicMenu;

    /**
     * Konstruktor untuk GUI Pemutar Musik. Menginisialisasi semua komponen,
     * menu, dan listener tombol.
     */
    public MusicPlayerGUI() {
        musicList = new ArrayList<>();
        player = new MusicPlayer();

        setTitle("🎵 Simple Music Player");
        setSize(500, 300);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupMenuBar();
        setupPlayerPanel();

        setVisible(true);
    }

    /**
     * Menyiapkan menu bar dengan menu "File" dan "Putar Musik". - File > Unggah
     * MP3: membuka pemilih file untuk menambahkan file MP3. - Putar Musik:
     * menampilkan lagu yang diunggah secara dinamis dalam tata letak grid.
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // === Menu File ===
        JMenu fileMenu = new JMenu("File");
        JMenuItem uploadItem = new JMenuItem("Upload MP3");

        /**
         * Action listener untuk mengunggah file MP3. Membuka JFileChooser,
         * menyaring file .mp3, dan menambahkan file yang dipilih ke dalam
         * daftar musik.
         */
        uploadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                JFileChooser chooser = new JFileChooser();
                chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                        "MP3 Files", "mp3"));
                int result = chooser.showOpenDialog(MusicPlayerGUI.this);

                if (result == JFileChooser.APPROVE_OPTION) {
                    File selected = chooser.getSelectedFile();
                    Music music = new Music(selected);
                    musicList.add(music);
                    refreshPlayMusicMenu();
                    JOptionPane.showMessageDialog(MusicPlayerGUI.this,
                            "Uploaded: " + music.getTitle());
                }
            }
        });

        fileMenu.add(uploadItem);
        menuBar.add(fileMenu);

        // === Menu Pemutaran Music ===
        playMusicMenu = new JMenu("Play Music");
        menuBar.add(playMusicMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Memperbarui tampilan menu dropdown "Putar Musik". Menampilkan semua lagu
     * yang diunggah sebagai grid tombol yang bisa diklik. Setiap tombol, saat
     * diklik, akan memuat dan memutar lagu tersebut.
     */
    private void refreshPlayMusicMenu() {
        playMusicMenu.removeAll();

        if (musicList.isEmpty()) {
            JMenuItem emptyItem = new JMenuItem("No music uploaded");
            emptyItem.setEnabled(false);
            playMusicMenu.add(emptyItem);
            return;
        }

        // Create a panel with grid layout for the dropdown
        int cols = 2;
        int rows = (int) Math.ceil((double) musicList.size() / cols);
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (Music music : musicList) {
            JButton songButton = new JButton(music.getTitle());
            songButton.setFont(new Font("Arial", Font.PLAIN, 12));

            /**
             * Action listener untuk setiap tombol lagu di dalam grid. Memuat
             * lagu yang dipilih ke pemutar dan memulai pemutaran.
             */
            songButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    player.load(music);
                    player.Play();
                    nowPlayingLabel.setText("Now Playing: " + music.getTitle());
                    playPauseButton.setText("Pause");
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }
            });

            gridPanel.add(songButton);
        }

        // Membuat panel dengan layout grid untuk dropdown
        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPopupMenu popup = playMusicMenu.getPopupMenu();
        popup.setLayout(new BorderLayout());
        popup.removeAll();
        popup.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Menyiapkan panel pemutar utama yang berisi: - Label "Sedang Memutar" di
     * bagian atas - Tombol Mundur, Putar/Jeda, dan Maju di bagian tengah
     */
    private void setupPlayerPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        nowPlayingLabel = new JLabel("No music playing", SwingConstants.CENTER);
        nowPlayingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(nowPlayingLabel, BorderLayout.NORTH);

        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backwardButton = new JButton("<< Backward");
        playPauseButton = new JButton("> Play");
        JButton forwardButton = new JButton(">> Forward");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        backwardButton.setFont(buttonFont);
        playPauseButton.setFont(buttonFont);
        forwardButton.setFont(buttonFont);

        /**
         * Action listener untuk tombol Mundur (Backward). Melompati lagu yang
         * sedang diputar mundur selama 5 detik.
         */
        backwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.backward(5);
            }
        });

        /**
         * Action listener untuk tombol ganti Putar/Jeda (Play/Pause). Jika
         * musik sedang diputar, maka akan dijeda dan label berubah menjadi
         * "Putar". Jika musik sedang dijeda, maka akan dilanjutkan dan label
         * berubah menjadi "Pause".
         */
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player.isPlaying()) {
                    player.pause();
                    playPauseButton.setText("> Play");
                } else {
                    player.Play();
                    if (player.getCurrentMusic() != null) {
                        playPauseButton.setText("Pause");
                    }
                }
            }
        });

        /**
         * Action listener untuk tombol Maju (Forward). Melompati lagu yang
         * sedang diputar maju selama 5 detik.
         */
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.forward(5);
            }
        });

        controlPanel.add(backwardButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(forwardButton);

        mainPanel.add(controlPanel, BorderLayout.CENTER);
        add(mainPanel, BorderLayout.CENTER);
    }

}
