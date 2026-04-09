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
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

/**
 * GUI utama untuk Pemutar Musik.
 * Menerapkan enkapsulasi: semua komponen UI dan data bersifat private,
 * interaksi antar komponen melalui method yang terkontrol.
 *
 * @author iqbalagil
 */
public class MusicPlayerGUI extends JFrame {

    // ── Field Private (Enkapsulasi Komponen UI) ──

    /** Daftar musik yang telah diunggah */
    private ArrayList<Music> musicList;

    /** Objek pengontrol pemutaran musik */
    private MusicPlayer player;

    /** Label penampil judul lagu aktif */
    private JLabel nowPlayingLabel;

    /** Tombol toggle putar/jeda */
    private JButton playPauseButton;

    /** Menu dropdown daftar lagu */
    private JMenu playMusicMenu;

    /** Slider progres pemutaran */
    private JSlider timeSlider;

    /** Timer pembaruan slider otomatis */
    private Timer progressTimer;

    // ── Konstanta ──

    /** Lebar default jendela */
    private static final int WINDOW_WIDTH = 500;

    /** Tinggi default jendela */
    private static final int WINDOW_HEIGHT = 350;

    /** Interval pembaruan slider dalam milidetik */
    private static final int TIMER_INTERVAL_MS = 500;

    /** Jumlah detik untuk skip maju/mundur */
    private static final int SKIP_SECONDS = 5;

    // ── Konstruktor ──

    /**
     * Membangun GUI pemutar musik.
     * Menginisialisasi semua komponen, menu, dan event listener.
     */
    public MusicPlayerGUI() {
        musicList = new ArrayList<>();
        player = new MusicPlayer();

        setTitle("🎵 Pemutar Musik Sederhana");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout());

        setupMenuBar();
        setupPlayerPanel();

        setVisible(true);
    }

    // ── Method Private (Enkapsulasi Logika Internal) ──

    /**
     * Menyiapkan menu bar dengan menu File dan Putar Musik.
     * - File > Unggah MP3: dialog pemilih file
     * - Putar Musik: grid tombol lagu yang diunggah
     */
    private void setupMenuBar() {
        JMenuBar menuBar = new JMenuBar();

        // === Menu File ===
        JMenu fileMenu = new JMenu("File");
        JMenuItem uploadItem = new JMenuItem("Unggah MP3");

        /**
         * Listener unggah file MP3.
         * Membuka JFileChooser lalu menambahkan file ke daftar.
         */
        uploadItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                uploadMusicFile();
            }
        });

        fileMenu.add(uploadItem);
        menuBar.add(fileMenu);

        // === Menu Putar Musik ===
        playMusicMenu = new JMenu("Putar Musik");
        menuBar.add(playMusicMenu);

        // === Menu Tampilan (akses ke GUI lain) ===
        JMenu viewMenu = new JMenu("Tampilan");

        JMenuItem playlistItem = new JMenuItem("📋 Manajer Playlist");
        /** Listener: membuka jendela Manajer Playlist */
        playlistItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new PlaylistManagerGUI(musicList, player);
            }
        });

        JMenuItem infoItem = new JMenuItem("ℹ Info Musik");
        /** Listener: membuka jendela Info Musik */
        infoItem.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                new MusicInfoGUI(player);
            }
        });

        viewMenu.add(playlistItem);
        viewMenu.add(infoItem);
        menuBar.add(viewMenu);

        setJMenuBar(menuBar);
    }

    /**
     * Menangani proses unggah file MP3 melalui dialog.
     * Validasi: hanya menerima file berekstensi .mp3.
     */
    private void uploadMusicFile() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "File MP3", "mp3"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            File selected = chooser.getSelectedFile();
            Music music = new Music(selected);
            addMusicToList(music);
            JOptionPane.showMessageDialog(this,
                    "Berhasil Diunggah: " + music.getTitle());
        }
    }

    /**
     * Menambahkan musik ke daftar dan memperbarui menu.
     * Mencegah duplikasi file yang sama.
     *
     * @param music objek Music yang ditambahkan
     */
    private void addMusicToList(Music music) {
        // Cek duplikasi berdasarkan path file
        for (Music m : musicList) {
            if (m.getFullPathFile().equals(music.getFullPathFile())) {
                JOptionPane.showMessageDialog(this,
                        "Musik sudah ada di daftar: " + music.getTitle());
                return;
            }
        }
        musicList.add(music);
        refreshPlayMusicMenu();
    }

    /**
     * Memperbarui menu dropdown Putar Musik.
     * Menampilkan semua lagu sebagai grid tombol yang bisa diklik.
     */
    private void refreshPlayMusicMenu() {
        playMusicMenu.removeAll();

        if (musicList.isEmpty()) {
            JMenuItem emptyItem = new JMenuItem("Belum ada musik diunggah");
            emptyItem.setEnabled(false);
            playMusicMenu.add(emptyItem);
            return;
        }

        int cols = 2;
        int rows = (int) Math.ceil((double) musicList.size() / cols);
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols, 5, 5));
        gridPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        for (Music music : musicList) {
            JButton songButton = new JButton(music.getTitle());
            songButton.setFont(new Font("Arial", Font.PLAIN, 12));

            /** Listener untuk memutar lagu yang dipilih */
            songButton.addActionListener(new ActionListener() {
                @Override
                public void actionPerformed(ActionEvent e) {
                    playSelectedMusic(music);
                    MenuSelectionManager.defaultManager().clearSelectedPath();
                }
            });

            gridPanel.add(songButton);
        }

        JScrollPane scrollPane = new JScrollPane(gridPanel);
        scrollPane.setPreferredSize(new Dimension(300, 200));

        JPopupMenu popup = playMusicMenu.getPopupMenu();
        popup.setLayout(new BorderLayout());
        popup.removeAll();
        popup.add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Memuat dan memutar musik yang dipilih.
     * Memperbarui label dan tombol sesuai status.
     *
     * @param music lagu yang akan diputar
     */
    private void playSelectedMusic(Music music) {
        player.load(music);
        player.play();
        updateNowPlayingLabel(music.getTitle());
        playPauseButton.setText("Pause");
        timeSlider.setValue(0);
    }

    /**
     * Memperbarui label judul lagu yang sedang diputar.
     *
     * @param title judul lagu aktif
     */
    private void updateNowPlayingLabel(String title) {
        nowPlayingLabel.setText("Sedang Memutar: " + title);
    }

    /**
     * Menyiapkan panel pemutaran utama.
     * Berisi label judul, tombol kontrol, dan slider progres.
     */
    private void setupPlayerPanel() {
        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // === Label Sedang Memutar ===
        nowPlayingLabel = new JLabel("Tidak ada musik yang diputar", SwingConstants.CENTER);
        nowPlayingLabel.setFont(new Font("Arial", Font.BOLD, 16));
        mainPanel.add(nowPlayingLabel, BorderLayout.NORTH);

        // === Panel Tombol Kontrol ===
        JPanel controlPanel = createControlPanel();
        mainPanel.add(controlPanel, BorderLayout.CENTER);

        // === Panel Timeline ===
        JPanel timelinePanel = createTimelinePanel();
        mainPanel.add(timelinePanel, BorderLayout.SOUTH);

        add(mainPanel, BorderLayout.CENTER);

        // Jalankan timer pembaruan slider
        startProgressTimer();
    }

    /**
     * Membuat panel berisi tombol kontrol pemutaran (mundur, putar/jeda, maju).
     *
     * @return JPanel berisi tombol kontrol
     */
    private JPanel createControlPanel() {
        JPanel controlPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 20, 10));

        JButton backwardButton = new JButton("<< Mundur");
        playPauseButton = new JButton("> Putar");
        JButton forwardButton = new JButton(">> Maju");

        Font buttonFont = new Font("Arial", Font.BOLD, 14);
        backwardButton.setFont(buttonFont);
        playPauseButton.setFont(buttonFont);
        forwardButton.setFont(buttonFont);

        /** Listener mundur: skip mundur 5 detik */
        backwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.backward(SKIP_SECONDS);
            }
        });

        /** Listener toggle putar/jeda */
        playPauseButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                togglePlayPause();
            }
        });

        /** Listener maju: skip maju 5 detik */
        forwardButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                player.forward(SKIP_SECONDS);
            }
        });

        controlPanel.add(backwardButton);
        controlPanel.add(playPauseButton);
        controlPanel.add(forwardButton);

        return controlPanel;
    }

    /**
     * Mengatur toggle antara putar dan jeda.
     * Memperbarui teks tombol sesuai status.
     */
    private void togglePlayPause() {
        if (player.isPlaying()) {
            player.pause();
            playPauseButton.setText("> Putar");
        } else {
            player.play();
            if (player.getCurrentMusic() != null) {
                playPauseButton.setText("Pause");
            }
        }
    }

    /**
     * Membuat panel timeline berisi slider progres lagu.
     *
     * @return JPanel berisi slider
     */
    private JPanel createTimelinePanel() {
        JPanel timelinePanel = new JPanel(new BorderLayout());

        timeSlider = new JSlider(0, 10000, 0);

        /** Listener slider: seek ke posisi frame saat digeser user */
        timeSlider.addChangeListener(new ChangeListener() {
            @Override
            public void stateChanged(ChangeEvent e) {
                if (timeSlider.getValueIsAdjusting()) {
                    player.seek(timeSlider.getValue());
                }
            }
        });

        timelinePanel.add(timeSlider, BorderLayout.CENTER);
        return timelinePanel;
    }

    /**
     * Menjalankan timer untuk sinkronisasi slider dengan posisi pemutaran.
     * Pembaruan terjadi setiap 500ms secara thread-safe via Swing Timer.
     */
    private void startProgressTimer() {
        progressTimer = new Timer(TIMER_INTERVAL_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                if (player != null && player.isPlaying() && !timeSlider.getValueIsAdjusting()) {
                    timeSlider.setValue(player.getCurrentFrame());
                }
            }
        });
        progressTimer.start();
    }

    // ── Getter Publik (Akses Terkontrol) ──

    /**
     * Mengambil jumlah musik dalam daftar.
     *
     * @return jumlah musik yang diunggah
     */
    public int getMusicCount() {
        return musicList.size();
    }

    /**
     * Mengambil objek MusicPlayer yang digunakan GUI ini.
     *
     * @return referensi MusicPlayer
     */
    public MusicPlayer getPlayer() {
        return player;
    }

    /**
     * Mengambil salinan daftar musik (mencegah modifikasi langsung).
     *
     * @return salinan ArrayList musik
     */
    public ArrayList<Music> getMusicList() {
        return new ArrayList<>(musicList);
    }
}
