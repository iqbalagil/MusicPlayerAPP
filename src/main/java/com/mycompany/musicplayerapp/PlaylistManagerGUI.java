/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.musicplayerapp;

import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * GUI untuk mengelola playlist musik.
 * Menampilkan daftar lagu dalam tabel dan menyediakan kontrol
 * untuk menambah, menghapus, serta menandai lagu favorit.
 * Menerapkan enkapsulasi penuh pada semua komponen dan data.
 *
 * @author iqbalagil
 */
public class PlaylistManagerGUI extends JFrame {

    // ── Field Private (Enkapsulasi) ──

    /** Daftar musik dalam playlist */
    private ArrayList<Music> playlist;

    /** Referensi ke player utama untuk integrasi pemutaran */
    private MusicPlayer player;

    /** Model tabel untuk menampilkan data playlist */
    private DefaultTableModel tableModel;

    /** Komponen tabel playlist */
    private JTable playlistTable;

    /** Label penampil jumlah lagu */
    private JLabel statusLabel;

    /** Field input pencarian lagu */
    private JTextField searchField;

    // ── Konstanta ──

    /** Lebar jendela */
    private static final int WINDOW_WIDTH = 550;

    /** Tinggi jendela */
    private static final int WINDOW_HEIGHT = 450;

    /** Nama kolom tabel */
    private static final String[] COLUMN_NAMES = {"No", "Judul", "Artis", "Genre", "Favorit"};

    // ── Konstruktor ──

    /**
     * Membuat GUI Playlist Manager.
     * Menerima referensi playlist dan player dari GUI utama.
     *
     * @param playlist daftar musik yang sudah diunggah
     * @param player   referensi MusicPlayer untuk pemutaran
     */
    public PlaylistManagerGUI(ArrayList<Music> playlist, MusicPlayer player) {
        this.playlist = new ArrayList<>(playlist);
        this.player = player;

        setTitle("📋 Manajer Playlist");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        setupSearchPanel();
        setupTable();
        setupButtonPanel();
        setupStatusBar();

        refreshTable();
        setVisible(true);
    }

    // ── Method Private (Enkapsulasi Logika Internal) ──

    /**
     * Menyiapkan panel pencarian di bagian atas.
     */
    private void setupSearchPanel() {
        JPanel searchPanel = new JPanel(new BorderLayout(5, 5));
        searchPanel.setBorder(BorderFactory.createEmptyBorder(10, 10, 5, 10));

        JLabel searchLabel = new JLabel("🔍 Cari: ");
        searchLabel.setFont(new Font("Arial", Font.BOLD, 13));

        searchField = new JTextField();
        searchField.setFont(new Font("Arial", Font.PLAIN, 13));

        /** Listener pencarian: filter tabel saat user mengetik */
        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                filterTable(searchField.getText());
            }
        });

        searchPanel.add(searchLabel, BorderLayout.WEST);
        searchPanel.add(searchField, BorderLayout.CENTER);

        add(searchPanel, BorderLayout.NORTH);
    }

    /**
     * Menyiapkan tabel playlist dengan kolom yang sudah ditentukan.
     * Kolom tabel tidak bisa diedit langsung oleh user.
     */
    private void setupTable() {
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            /** Mencegah pengeditan langsung sel tabel */
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        playlistTable = new JTable(tableModel);
        playlistTable.setFont(new Font("Arial", Font.PLAIN, 12));
        playlistTable.setRowHeight(25);
        playlistTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        playlistTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        // Atur lebar kolom proporsional
        playlistTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        playlistTable.getColumnModel().getColumn(1).setPreferredWidth(180);
        playlistTable.getColumnModel().getColumn(2).setPreferredWidth(120);
        playlistTable.getColumnModel().getColumn(3).setPreferredWidth(100);
        playlistTable.getColumnModel().getColumn(4).setPreferredWidth(60);

        JScrollPane scrollPane = new JScrollPane(playlistTable);
        scrollPane.setBorder(BorderFactory.createEmptyBorder(5, 10, 5, 10));

        add(scrollPane, BorderLayout.CENTER);
    }

    /**
     * Menyiapkan panel tombol aksi di bagian bawah.
     * Terdapat dua baris tombol:
     * - Baris 1: Putar, Favorit, Edit Info (kontrol pemutaran & metadata)
     * - Baris 2: Simpan, Hapus, Batal, Close (kontrol CRUD)
     */
    private void setupButtonPanel() {
        JPanel buttonContainer = new JPanel();
        buttonContainer.setLayout(new BoxLayout(buttonContainer, BoxLayout.Y_AXIS));
        buttonContainer.setBorder(BorderFactory.createEmptyBorder(0, 10, 5, 10));

        Font btnFont = new Font("Arial", Font.BOLD, 12);

        // ── Baris 1: Kontrol Pemutaran & Metadata ──
        JPanel controlRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton playButton = new JButton("▶ Putar");
        JButton favoriteButton = new JButton("⭐ Favorit");

        playButton.setFont(btnFont);
        favoriteButton.setFont(btnFont);

        /** Listener putar: memainkan lagu yang dipilih di tabel */
        playButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                playSelectedSong();
            }
        });

        /** Listener favorit: toggle status favorit lagu */
        favoriteButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                toggleFavoriteSelected();
            }
        });

        controlRow.add(playButton);
        controlRow.add(favoriteButton);

        // ── Baris 2: Tombol CRUD (Simpan, Hapus, Batal, Close) ──
        JPanel crudRow = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 5));

        JButton simpanButton = new JButton("💾 Simpan");
        JButton hapusButton = new JButton("🗑 Hapus");
        JButton batalButton = new JButton("↩ Batal");
        JButton closeButton = new JButton("✖ Close");

        simpanButton.setFont(btnFont);
        hapusButton.setFont(btnFont);
        batalButton.setFont(btnFont);
        closeButton.setFont(btnFont);

        /**
         * Listener Simpan: membuka dialog edit info lagu (artis & genre),
         * lalu menyimpan perubahan ke objek Music dan memperbarui tabel.
         */
        simpanButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                editSelectedSong();
            }
        });

        /** Listener Hapus: menghapus lagu yang dipilih dari playlist */
        hapusButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                removeSelectedSong();
            }
        });

        /**
         * Listener Batal: membatalkan seleksi pada tabel dan mereset
         * field pencarian. Berguna untuk membatalkan aksi yang sedang
         * dipilih sebelum dikonfirmasi.
         */
        batalButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                cancelSelection();
            }
        });

        /** Listener Close: menutup jendela Playlist Manager */
        closeButton.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent e) {
                dispose();
            }
        });

        crudRow.add(simpanButton);
        crudRow.add(hapusButton);
        crudRow.add(batalButton);
        crudRow.add(closeButton);

        buttonContainer.add(controlRow);
        buttonContainer.add(crudRow);

        add(buttonContainer, BorderLayout.SOUTH);
    }

    /**
     * Menyiapkan status bar penampil jumlah lagu.
     */
    private void setupStatusBar() {
        statusLabel = new JLabel("Total: 0 lagu");
        statusLabel.setFont(new Font("Arial", Font.ITALIC, 11));
        statusLabel.setBorder(BorderFactory.createEmptyBorder(0, 15, 10, 10));
        add(statusLabel, BorderLayout.PAGE_END);
    }

    /**
     * Memperbarui isi tabel sesuai data playlist terkini.
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < playlist.size(); i++) {
            Music m = playlist.get(i);
            tableModel.addRow(new Object[]{
                i + 1,
                m.getTitle(),
                m.getArtist(),
                m.getGenre(),
                m.isFavorite() ? "⭐" : "-"
            });
        }
        updateStatusLabel();
    }

    /**
     * Memfilter tabel berdasarkan kata kunci pencarian.
     *
     * @param keyword kata kunci pencarian (case-insensitive)
     */
    private void filterTable(String keyword) {
        tableModel.setRowCount(0);
        String lowerKeyword = keyword.toLowerCase();
        int num = 1;

        for (Music m : playlist) {
            // Cocokkan dengan judul, artis, atau genre
            if (m.getTitle().toLowerCase().contains(lowerKeyword)
                    || m.getArtist().toLowerCase().contains(lowerKeyword)
                    || m.getGenre().toLowerCase().contains(lowerKeyword)) {
                tableModel.addRow(new Object[]{
                    num++,
                    m.getTitle(),
                    m.getArtist(),
                    m.getGenre(),
                    m.isFavorite() ? "⭐" : "-"
                });
            }
        }
    }

    /**
     * Memutar lagu yang dipilih di tabel.
     * Validasi: memastikan ada baris yang terpilih.
     */
    private void playSelectedSong() {
        int selectedRow = playlistTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih lagu terlebih dahulu!");
            return;
        }
        Music music = findMusicByTitle((String) tableModel.getValueAt(selectedRow, 1));
        if (music != null) {
            player.load(music);
            player.play();
            JOptionPane.showMessageDialog(this,
                    "Memutar: " + music.getTitle());
        }
    }

    /**
     * Toggle status favorit pada lagu yang dipilih.
     */
    private void toggleFavoriteSelected() {
        int selectedRow = playlistTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih lagu terlebih dahulu!");
            return;
        }
        Music music = findMusicByTitle((String) tableModel.getValueAt(selectedRow, 1));
        if (music != null) {
            music.setFavorite(!music.isFavorite());
            refreshTable();
        }
    }

    /**
     * Menghapus lagu yang dipilih dari playlist.
     * Menampilkan konfirmasi sebelum menghapus.
     */
    private void removeSelectedSong() {
        int selectedRow = playlistTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih lagu terlebih dahulu!");
            return;
        }
        Music music = findMusicByTitle((String) tableModel.getValueAt(selectedRow, 1));
        if (music != null) {
            int confirm = JOptionPane.showConfirmDialog(this,
                    "Hapus \"" + music.getTitle() + "\" dari playlist?",
                    "Konfirmasi Hapus", JOptionPane.YES_NO_OPTION);
            if (confirm == JOptionPane.YES_OPTION) {
                playlist.remove(music);
                refreshTable();
            }
        }
    }

    /**
     * Membuka dialog untuk mengedit informasi lagu (artis & genre).
     */
    private void editSelectedSong() {
        int selectedRow = playlistTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih lagu terlebih dahulu!");
            return;
        }
        Music music = findMusicByTitle((String) tableModel.getValueAt(selectedRow, 1));
        if (music == null) {
            return;
        }

        // Panel form input
        JPanel editPanel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField artistField = new JTextField(music.getArtist());
        JTextField genreField = new JTextField(music.getGenre());

        editPanel.add(new JLabel("Artis:"));
        editPanel.add(artistField);
        editPanel.add(new JLabel("Genre:"));
        editPanel.add(genreField);

        int result = JOptionPane.showConfirmDialog(this, editPanel,
                "Edit Info: " + music.getTitle(),
                JOptionPane.OK_CANCEL_OPTION, JOptionPane.PLAIN_MESSAGE);

        if (result == JOptionPane.OK_OPTION) {
            music.setArtist(artistField.getText());
            music.setGenre(genreField.getText());
            refreshTable();
        }
    }

    /**
     * Mencari objek Music berdasarkan judul.
     *
     * @param title judul lagu yang dicari
     * @return objek Music atau null jika tidak ditemukan
     */
    private Music findMusicByTitle(String title) {
        for (Music m : playlist) {
            if (m.getTitle().equals(title)) {
                return m;
            }
        }
        return null;
    }

    /**
     * Membatalkan seleksi saat ini pada tabel dan mereset pencarian.
     * Menghapus highlight baris yang dipilih dan mengosongkan field pencarian
     * sehingga tabel kembali menampilkan semua data.
     */
    private void cancelSelection() {
        // Hapus seleksi baris di tabel
        playlistTable.clearSelection();

        // Reset field pencarian
        searchField.setText("");

        // Refresh tabel ke tampilan penuh
        refreshTable();
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
     * Memperbarui label status jumlah lagu.
     */
    private void updateStatusLabel() {
        long favCount = 0;
        for (Music m : playlist) {
            if (m.isFavorite()) {
                favCount++;
            }
        }
        statusLabel.setText("Total: " + playlist.size() + " lagu | Favorit: " + favCount);
    }

    // ── Getter Publik ──

    /**
     * Mengambil salinan playlist saat ini.
     *
     * @return salinan ArrayList playlist
     */
    public ArrayList<Music> getPlaylist() {
        return new ArrayList<>(playlist);
    }

    /**
     * Mengambil jumlah lagu dalam playlist.
     *
     * @return jumlah lagu
     */
    public int getPlaylistSize() {
        return playlist.size();
    }
}
