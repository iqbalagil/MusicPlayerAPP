/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;

/**
 * GUI untuk menampilkan detail informasi lagu yang sedang diputar.
 * Menampilkan metadata lengkap musik dan kontrol volume.
 * Menerapkan enkapsulasi: semua komponen bersifat private.
 *
 * @author iqbalagil
 */
public class MusicInfoGUI extends JFrame {

    // ── Field Private (Enkapsulasi) ──

    /** Referensi player untuk mengambil info lagu aktif */
    private MusicPlayer player;

    /** Label penampil judul lagu */
    private JLabel titleValueLabel;

    /** Label penampil nama artis */
    private JLabel artistValueLabel;

    /** Label penampil genre */
    private JLabel genreValueLabel;

    /** Label penampil lokasi file */
    private JLabel pathValueLabel;

    /** Label penampil status favorit */
    private JLabel favoriteValueLabel;

    /** Label penampil status pemutaran */
    private JLabel statusValueLabel;

    /** Label penampil posisi frame */
    private JLabel frameValueLabel;

    /** Slider pengatur volume */
    private JSlider volumeSlider;

    /** Label penampil nilai volume */
    private JLabel volumeValueLabel;

    /** Timer untuk memperbarui informasi secara berkala */
    private Timer updateTimer;

    // ── Konstanta ──

    /** Lebar jendela */
    private static final int WINDOW_WIDTH = 450;

    /** Tinggi jendela */
    private static final int WINDOW_HEIGHT = 420;

    /** Interval pembaruan info dalam milidetik */
    private static final int UPDATE_INTERVAL_MS = 1000;

    // ── Konstruktor ──

    /**
     * Membuat GUI Info Musik.
     * Menampilkan detail lagu yang sedang diputar beserta kontrol volume.
     *
     * @param player referensi MusicPlayer untuk mengambil data
     */
    public MusicInfoGUI(MusicPlayer player) {
        this.player = player;

        setTitle("ℹ Info Musik");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        setupInfoPanel();
        setupVolumePanel();
        setupRefreshButton();
        startAutoUpdate();

        // Perbarui info saat pertama dibuka
        refreshInfo();

        setVisible(true);
    }

    // ── Method Private (Enkapsulasi Logika Internal) ──

    /**
     * Menyiapkan panel informasi lagu dengan layout grid.
     * Setiap baris menampilkan label kunci dan nilai.
     */
    private void setupInfoPanel() {
        JPanel infoPanel = new JPanel(new GridBagLayout());
        infoPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Detail Musik",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13)));

        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(6, 10, 6, 10);
        gbc.anchor = GridBagConstraints.WEST;

        // Inisialisasi label nilai
        titleValueLabel = createValueLabel();
        artistValueLabel = createValueLabel();
        genreValueLabel = createValueLabel();
        pathValueLabel = createValueLabel();
        favoriteValueLabel = createValueLabel();
        statusValueLabel = createValueLabel();
        frameValueLabel = createValueLabel();

        // Baris 0: Judul
        addInfoRow(infoPanel, gbc, 0, "Judul", titleValueLabel);

        // Baris 1: Artis
        addInfoRow(infoPanel, gbc, 1, "Artis", artistValueLabel);

        // Baris 2: Genre
        addInfoRow(infoPanel, gbc, 2, "Genre", genreValueLabel);

        // Baris 3: Lokasi File
        addInfoRow(infoPanel, gbc, 3, "Lokasi File", pathValueLabel);

        // Baris 4: Favorit
        addInfoRow(infoPanel, gbc, 4, "Favorit", favoriteValueLabel);

        // Baris 5: Status
        addInfoRow(infoPanel, gbc, 5, "Status", statusValueLabel);

        // Baris 6: Posisi Frame
        addInfoRow(infoPanel, gbc, 6, "Frame", frameValueLabel);

        add(infoPanel, BorderLayout.CENTER);
    }

    /**
     * Menambahkan satu baris informasi ke panel.
     *
     * @param panel tujuan
     * @param gbc   constraint GridBag
     * @param row   nomor baris
     * @param key   nama label kunci
     * @param value komponen label nilai
     */
    private void addInfoRow(JPanel panel, GridBagConstraints gbc, int row,
            String key, JLabel value) {
        // Kolom kiri: label kunci
        gbc.gridx = 0;
        gbc.gridy = row;
        gbc.weightx = 0.3;
        JLabel keyLabel = new JLabel(key + ":");
        keyLabel.setFont(new Font("Arial", Font.BOLD, 12));
        panel.add(keyLabel, gbc);

        // Kolom kanan: label nilai
        gbc.gridx = 1;
        gbc.weightx = 0.7;
        gbc.fill = GridBagConstraints.HORIZONTAL;
        panel.add(value, gbc);
        gbc.fill = GridBagConstraints.NONE;
    }

    /**
     * Membuat label nilai dengan font standar.
     *
     * @return JLabel baru dengan format konsisten
     */
    private JLabel createValueLabel() {
        JLabel label = new JLabel("-");
        label.setFont(new Font("Arial", Font.PLAIN, 12));
        return label;
    }

    /**
     * Menyiapkan panel kontrol volume dengan slider.
     */
    private void setupVolumePanel() {
        JPanel volumePanel = new JPanel(new BorderLayout(5, 5));
        volumePanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Volume",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13)));

        JPanel sliderContainer = new JPanel(new BorderLayout(10, 0));
        sliderContainer.setBorder(BorderFactory.createEmptyBorder(5, 10, 10, 10));

        JLabel volIcon = new JLabel("🔊");
        volIcon.setFont(new Font("Arial", Font.PLAIN, 16));

        volumeSlider = new JSlider(0, 100, player.getVolume());
        volumeSlider.setMajorTickSpacing(25);
        volumeSlider.setMinorTickSpacing(5);
        volumeSlider.setPaintTicks(true);
        volumeSlider.setPaintLabels(true);
        volumeSlider.setFont(new Font("Arial", Font.PLAIN, 10));

        volumeValueLabel = new JLabel(player.getVolume() + "%");
        volumeValueLabel.setFont(new Font("Arial", Font.BOLD, 13));
        volumeValueLabel.setHorizontalAlignment(SwingConstants.CENTER);
        volumeValueLabel.setPreferredSize(new Dimension(45, 20));

        /** Listener slider volume: mengatur volume player */
        volumeSlider.addChangeListener(e -> {
            int vol = volumeSlider.getValue();
            player.setVolume(vol);
            volumeValueLabel.setText(vol + "%");
        });

        sliderContainer.add(volIcon, BorderLayout.WEST);
        sliderContainer.add(volumeSlider, BorderLayout.CENTER);
        sliderContainer.add(volumeValueLabel, BorderLayout.EAST);

        volumePanel.add(sliderContainer, BorderLayout.CENTER);

        add(volumePanel, BorderLayout.SOUTH);
    }

    /**
     * Menyiapkan tombol refresh manual di bagian atas.
     */
    private void setupRefreshButton() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 10, 5));
        topPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 0, 5));

        JButton refreshButton = new JButton("🔄 Perbarui");
        refreshButton.setFont(new Font("Arial", Font.BOLD, 12));

        /** Listener tombol refresh: memperbarui info secara manual */
        refreshButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshInfo();
            }
        });

        topPanel.add(refreshButton);
        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Memperbarui semua label informasi sesuai data lagu aktif.
     * Jika tidak ada lagu, menampilkan tanda "-".
     */
    private void refreshInfo() {
        Music currentMusic = player.getCurrentMusic();

        if (currentMusic != null) {
            titleValueLabel.setText(currentMusic.getTitle());
            artistValueLabel.setText(currentMusic.getArtist());
            genreValueLabel.setText(currentMusic.getGenre());
            pathValueLabel.setText(shortenPath(currentMusic.getFullPathFile()));
            pathValueLabel.setToolTipText(currentMusic.getFullPathFile());
            favoriteValueLabel.setText(currentMusic.isFavorite() ? "⭐ Ya" : "Tidak");
        } else {
            titleValueLabel.setText("-");
            artistValueLabel.setText("-");
            genreValueLabel.setText("-");
            pathValueLabel.setText("-");
            pathValueLabel.setToolTipText(null);
            favoriteValueLabel.setText("-");
        }

        // Status pemutaran
        if (player.isPlaying()) {
            statusValueLabel.setText("▶ Sedang Memutar");
            statusValueLabel.setForeground(new Color(0, 128, 0));
        } else if (player.isPaused()) {
            statusValueLabel.setText("⏸ Dijeda");
            statusValueLabel.setForeground(new Color(200, 150, 0));
        } else {
            statusValueLabel.setText("⏹ Berhenti");
            statusValueLabel.setForeground(Color.GRAY);
        }

        frameValueLabel.setText(String.valueOf(player.getCurrentFrame()));
    }

    /**
     * Mempersingkat path file jika terlalu panjang.
     * Menampilkan maksimal 35 karakter terakhir.
     *
     * @param fullPath path lengkap file
     * @return path yang dipotong dengan prefix "..."
     */
    private String shortenPath(String fullPath) {
        if (fullPath.length() > 35) {
            return "..." + fullPath.substring(fullPath.length() - 35);
        }
        return fullPath;
    }

    /**
     * Memulai timer pembaruan otomatis info setiap 1 detik.
     * Timer berhenti saat jendela ditutup.
     */
    private void startAutoUpdate() {
        updateTimer = new Timer(UPDATE_INTERVAL_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                refreshInfo();
            }
        });
        updateTimer.start();

        /** Hentikan timer saat jendela ditutup */
        addWindowListener(new WindowAdapter() {
            @Override
            public void windowClosing(WindowEvent e) {
                if (updateTimer != null) {
                    updateTimer.stop();
                }
            }
        });
    }
}
