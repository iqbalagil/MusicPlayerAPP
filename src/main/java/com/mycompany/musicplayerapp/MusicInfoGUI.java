/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.awt.*;
import java.awt.event.*;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.table.DefaultTableModel;

/**
 * GUI untuk menampilkan dan mengedit detail informasi lagu yang sedang diputar.
 * Menggunakan JTable 2 kolom (Properti - Nilai) untuk menampilkan metadata musik.
 * Menyediakan tombol Simpan, Batal, dan Close untuk kontrol CRUD.
 * Menerapkan enkapsulasi: semua komponen bersifat private.
 *
 * @author iqbalagil
 */
public class MusicInfoGUI extends JFrame {

    // ── Field Private (Enkapsulasi) ──

    /** Referensi player untuk mengambil info lagu aktif */
    private MusicPlayer player;

    /** Model tabel untuk menampilkan data info musik */
    private DefaultTableModel tableModel;

    /** Komponen tabel info musik */
    private JTable infoTable;

    /** Slider pengatur volume */
    private JSlider volumeSlider;

    /** Label penampil nilai volume */
    private JLabel volumeValueLabel;

    /** Timer untuk memperbarui informasi secara berkala */
    private Timer updateTimer;

    /** Flag apakah sedang dalam mode edit */
    private boolean isEditMode;

    /** Backup data sebelum edit, untuk fitur Batal */
    private String backupArtist;
    private String backupGenre;
    private boolean backupFavorite;

    /** Referensi tombol agar bisa diakses saat toggle mode */
    private JButton simpanButton;
    private JButton batalButton;
    private JButton editButton;

    // ── Konstanta ──

    /** Lebar jendela */
    private static final int WINDOW_WIDTH = 500;

    /** Tinggi jendela */
    private static final int WINDOW_HEIGHT = 480;

    /** Interval pembaruan info dalam milidetik */
    private static final int UPDATE_INTERVAL_MS = 1000;

    /** Nama kolom tabel info */
    private static final String[] COLUMN_NAMES = {"Properti", "Nilai"};

    /** Indeks baris di tabel untuk setiap properti */
    private static final int ROW_JUDUL = 0;
    private static final int ROW_ARTIS = 1;
    private static final int ROW_GENRE = 2;
    private static final int ROW_LOKASI = 3;
    private static final int ROW_FAVORIT = 4;
    private static final int ROW_STATUS = 5;
    private static final int ROW_FRAME = 6;

    // ── Konstruktor ──

    /**
     * Membuat GUI Info Musik dengan tampilan tabel.
     * Menampilkan detail lagu yang sedang diputar dalam JTable
     * beserta kontrol volume dan tombol CRUD.
     *
     * @param player referensi MusicPlayer untuk mengambil data
     */
    public MusicInfoGUI(MusicPlayer player) {
        this.player = player;
        this.isEditMode = false;

        setTitle("ℹ Info Musik");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(10, 10));

        setupInfoTable();
        setupVolumePanel();
        setupButtonPanel();
        startAutoUpdate();

        // Perbarui info saat pertama dibuka
        refreshInfo();

        setVisible(true);
    }

    // ── Method Private (Enkapsulasi Logika Internal) ──

    /**
     * Menyiapkan tabel informasi musik dengan 2 kolom: Properti dan Nilai.
     * Kolom Properti tidak bisa diedit, kolom Nilai bisa diedit saat mode edit aktif.
     * Menggantikan tampilan label key-value sebelumnya menjadi JTable.
     */
    private void setupInfoTable() {
        // Buat model tabel dengan kontrol edit per kolom
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            /**
             * Mengontrol sel mana yang bisa diedit.
             * Hanya kolom "Nilai" (index 1) pada baris Artis, Genre, dan Favorit
             * yang bisa diedit, dan hanya saat mode edit aktif.
             */
            @Override
            public boolean isCellEditable(int row, int column) {
                // Kolom Properti (0) tidak pernah bisa diedit
                if (column == 0) return false;
                // Kolom Nilai (1) hanya bisa diedit di mode edit dan baris tertentu
                if (isEditMode && (row == ROW_ARTIS || row == ROW_GENRE || row == ROW_FAVORIT)) {
                    return true;
                }
                return false;
            }
        };

        // Isi baris awal tabel dengan label properti dan nilai default
        tableModel.addRow(new Object[]{"🎵 Judul", "-"});
        tableModel.addRow(new Object[]{"🎤 Artis", "-"});
        tableModel.addRow(new Object[]{"🎼 Genre", "-"});
        tableModel.addRow(new Object[]{"📁 Lokasi File", "-"});
        tableModel.addRow(new Object[]{"⭐ Favorit", "-"});
        tableModel.addRow(new Object[]{"📊 Status", "-"});
        tableModel.addRow(new Object[]{"🔢 Frame", "-"});

        // Konfigurasi komponen tabel
        infoTable = new JTable(tableModel);
        infoTable.setFont(new Font("Arial", Font.PLAIN, 13));
        infoTable.setRowHeight(30);
        infoTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 13));
        infoTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Atur lebar kolom proporsional
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(280);

        // Bungkus dalam scroll pane dengan border bertitel
        JScrollPane scrollPane = new JScrollPane(infoTable);
        scrollPane.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createEtchedBorder(),
                "Detail Musik",
                TitledBorder.LEFT,
                TitledBorder.TOP,
                new Font("Arial", Font.BOLD, 13)));

        add(scrollPane, BorderLayout.CENTER);
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

        // Gabungkan volume panel ke panel bawah
        JPanel southPanel = new JPanel(new BorderLayout());
        southPanel.add(volumePanel, BorderLayout.CENTER);
        add(southPanel, BorderLayout.SOUTH);
    }

    /**
     * Menyiapkan panel tombol CRUD: Edit, Simpan, Batal, Close.
     * - Edit: masuk ke mode edit (sel tabel bisa diedit)
     * - Simpan: menyimpan perubahan ke objek Music
     * - Batal: membatalkan perubahan dan kembali ke data semula
     * - Close: menutup jendela
     */
    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        Font btnFont = new Font("Arial", Font.BOLD, 12);

        // Tombol Edit — mengaktifkan mode edit
        editButton = new JButton("✏ Edit");
        editButton.setFont(btnFont);

        // Tombol Simpan — menyimpan perubahan dari tabel ke objek Music
        simpanButton = new JButton("💾 Simpan");
        simpanButton.setFont(btnFont);
        simpanButton.setEnabled(false); // Nonaktif sampai mode edit

        // Tombol Batal — membatalkan edit, restore data backup
        batalButton = new JButton("↩ Batal");
        batalButton.setFont(btnFont);
        batalButton.setEnabled(false); // Nonaktif sampai mode edit

        // Tombol Close — menutup jendela
        JButton closeButton = new JButton("✖ Close");
        closeButton.setFont(btnFont);

        /** Listener Edit: masuk mode edit, backup data saat ini */
        editButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                enterEditMode();
            }
        });

        /** Listener Simpan: simpan perubahan dari tabel ke model Music */
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                saveChanges();
            }
        });

        /** Listener Batal: kembalikan data semula, keluar mode edit */
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelEdit();
            }
        });

        /** Listener Close: menutup jendela dan menghentikan timer */
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        buttonPanel.add(editButton);
        buttonPanel.add(simpanButton);
        buttonPanel.add(batalButton);
        buttonPanel.add(closeButton);

        add(buttonPanel, BorderLayout.NORTH);
    }

    /**
     * Masuk ke mode edit: backup data dan aktifkan tombol Simpan/Batal.
     * Kolom Artis, Genre, dan Favorit menjadi bisa diedit di tabel.
     */
    private void enterEditMode() {
        Music currentMusic = player.getCurrentMusic();
        if (currentMusic == null) {
            showWarning("Tidak ada musik yang dimuat untuk diedit.");
            return;
        }

        // Backup data sebelum edit untuk fitur Batal
        backupArtist = currentMusic.getArtist();
        backupGenre = currentMusic.getGenre();
        backupFavorite = currentMusic.isFavorite();

        isEditMode = true;
        editButton.setEnabled(false);
        simpanButton.setEnabled(true);
        batalButton.setEnabled(true);

        // Hentikan auto-update agar tidak menimpa edit user
        if (updateTimer != null) updateTimer.stop();

        // Refresh agar tabel mengenali perubahan isCellEditable
        tableModel.fireTableStructureChanged();
        // Re-set lebar kolom setelah fireTableStructureChanged
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(280);

        JOptionPane.showMessageDialog(this,
                "Mode Edit aktif. Edit kolom Artis, Genre, atau Favorit (Ya/Tidak) lalu klik Simpan.",
                "Mode Edit", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Menyimpan perubahan dari tabel ke objek Music.
     * Membaca nilai dari sel tabel dan memanggil setter yang tervalidasi.
     */
    private void saveChanges() {
        Music currentMusic = player.getCurrentMusic();
        if (currentMusic == null) return;

        // Hentikan editing sel jika masih aktif
        if (infoTable.isEditing()) {
            infoTable.getCellEditor().stopCellEditing();
        }

        // Ambil nilai dari tabel dan simpan ke objek Music via setter
        String newArtist = (String) tableModel.getValueAt(ROW_ARTIS, 1);
        String newGenre = (String) tableModel.getValueAt(ROW_GENRE, 1);
        String favValue = (String) tableModel.getValueAt(ROW_FAVORIT, 1);

        currentMusic.setArtist(newArtist);
        currentMusic.setGenre(newGenre);
        currentMusic.setFavorite(
                favValue.toLowerCase().contains("ya") || favValue.contains("⭐"));

        exitEditMode();
        refreshInfo();

        JOptionPane.showMessageDialog(this,
                "Perubahan berhasil disimpan!",
                "Simpan", JOptionPane.INFORMATION_MESSAGE);
    }

    /**
     * Membatalkan edit: mengembalikan data dari backup dan keluar mode edit.
     */
    private void cancelEdit() {
        Music currentMusic = player.getCurrentMusic();
        if (currentMusic != null) {
            // Kembalikan data dari backup
            currentMusic.setArtist(backupArtist);
            currentMusic.setGenre(backupGenre);
            currentMusic.setFavorite(backupFavorite);
        }

        // Hentikan editing sel jika masih aktif
        if (infoTable.isEditing()) {
            infoTable.getCellEditor().cancelCellEditing();
        }

        exitEditMode();
        refreshInfo();
    }

    /**
     * Keluar dari mode edit: nonaktifkan tombol Simpan/Batal,
     * aktifkan kembali tombol Edit dan auto-update timer.
     */
    private void exitEditMode() {
        isEditMode = false;
        editButton.setEnabled(true);
        simpanButton.setEnabled(false);
        batalButton.setEnabled(false);

        // Restart auto-update
        if (updateTimer != null) updateTimer.start();

        // Refresh agar tabel mengenali perubahan isCellEditable
        tableModel.fireTableStructureChanged();
        infoTable.getColumnModel().getColumn(0).setPreferredWidth(120);
        infoTable.getColumnModel().getColumn(1).setPreferredWidth(280);
    }

    /**
     * Memperbarui semua baris tabel sesuai data lagu aktif.
     * Jika tidak ada lagu, menampilkan tanda "-".
     */
    private void refreshInfo() {
        Music currentMusic = player.getCurrentMusic();

        if (currentMusic != null) {
            tableModel.setValueAt(currentMusic.getTitle(), ROW_JUDUL, 1);
            tableModel.setValueAt(currentMusic.getArtist(), ROW_ARTIS, 1);
            tableModel.setValueAt(currentMusic.getGenre(), ROW_GENRE, 1);
            tableModel.setValueAt(shortenPath(currentMusic.getFullPathFile()), ROW_LOKASI, 1);
            tableModel.setValueAt(currentMusic.isFavorite() ? "⭐ Ya" : "Tidak", ROW_FAVORIT, 1);
        } else {
            tableModel.setValueAt("-", ROW_JUDUL, 1);
            tableModel.setValueAt("-", ROW_ARTIS, 1);
            tableModel.setValueAt("-", ROW_GENRE, 1);
            tableModel.setValueAt("-", ROW_LOKASI, 1);
            tableModel.setValueAt("-", ROW_FAVORIT, 1);
        }

        // Status pemutaran
        if (player.isPlaying()) {
            tableModel.setValueAt("▶ Sedang Memutar", ROW_STATUS, 1);
        } else if (player.isPaused()) {
            tableModel.setValueAt("⏸ Dijeda", ROW_STATUS, 1);
        } else {
            tableModel.setValueAt("⏹ Berhenti", ROW_STATUS, 1);
        }

        tableModel.setValueAt(String.valueOf(player.getCurrentFrame()), ROW_FRAME, 1);
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
     * Menampilkan pesan peringatan.
     *
     * @param message isi pesan peringatan
     */
    private void showWarning(String message) {
        JOptionPane.showMessageDialog(this, message,
                "Peringatan", JOptionPane.WARNING_MESSAGE);
    }

    /**
     * Memulai timer pembaruan otomatis info setiap 1 detik.
     * Timer berhenti saat jendela ditutup atau mode edit aktif.
     */
    private void startAutoUpdate() {
        updateTimer = new Timer(UPDATE_INTERVAL_MS, new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                // Hanya update jika tidak dalam mode edit
                if (!isEditMode) {
                    refreshInfo();
                }
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
