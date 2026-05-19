package com.mycompany.musicplayerapp;

import java.awt.*;
import java.awt.event.*;
import java.io.File;
import java.util.ArrayList;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;

/**
 * GUI untuk mendemonstrasikan Polimorfisme: Upcasting dan Downcasting
 * dalam konteks aplikasi Music Player.
 *
 * <p>
 * <b>Upcasting (Implisit):</b> Saat user mengunggah file MP3 dan memilih
 * tipe media (Music/Podcast/Audiobook), objek subclass yang dibuat akan
 * disimpan ke dalam {@code ArrayList<AudioMedia>} — konversi dari tipe
 * subclass ke tipe parent terjadi secara otomatis.
 * </p>
 *
 * <p>
 * <b>Downcasting (Eksplisit):</b> Saat user ingin melihat detail spesifik,
 * objek AudioMedia di-cast kembali ke tipe aslinya menggunakan
 * {@code instanceof} + explicit cast untuk mengakses method khusus subclass.
 * </p>
 *
 * <pre>
 *   AudioMedia (parent)
 *   ├── Music      → getArtist(), getGenre()
 *   ├── Podcast    → getHost(), getEpisodeNumber(), getChannelName()
 *   └── Audiobook  → getAuthor(), getNarrator(), getChapterNumber()
 * </pre>
 *
 * @author iqbalagil
 * @see AudioMedia
 * @see Music
 * @see Podcast
 * @see Audiobook
 */
public class MediaManagerGUI extends JFrame {

    // ── Field Private ──

    /**
     * Daftar media audio — disimpan sebagai tipe AudioMedia (UPCASTING).
     * Meskipun isinya bisa Music, Podcast, atau Audiobook,
     * semuanya disimpan dalam satu ArrayList bertipe parent.
     */
    private ArrayList<AudioMedia> mediaList;

    /** Referensi ke player utama untuk integrasi pemutaran */
    private MusicPlayer player;

    /** Model tabel untuk menampilkan data media */
    private DefaultTableModel tableModel;

    /** Komponen tabel */
    private JTable mediaTable;

    /** Area teks untuk menampilkan hasil casting */
    private JTextArea resultArea;

    // ── Konstanta ──

    private static final int WINDOW_WIDTH = 720;
    private static final int WINDOW_HEIGHT = 620;
    private static final String[] COLUMN_NAMES = { "No", "Judul", "Tipe Media", "Info (via Upcasting)" };

    // ── Konstruktor ──

    /**
     * Membuat GUI Demo Polimorfisme.
     * Menerima referensi daftar media dan player dari GUI utama,
     * sehingga terintegrasi dengan data musik yang sudah ada.
     *
     * @param mediaList daftar media audio yang sudah diunggah
     * @param player    referensi MusicPlayer untuk pemutaran
     */
    public MediaManagerGUI(ArrayList<AudioMedia> mediaList, MusicPlayer player) {
        this.mediaList = mediaList;
        this.player = player;

        setTitle("🔄 Demo Polimorfisme — Upcasting & Downcasting");
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLocationRelativeTo(null);
        setLayout(new BorderLayout(5, 5));

        setupTopPanel();
        setupCenterPanel();
        setupButtonPanel();

        refreshTable();
        setVisible(true);
    }

    // ── Setup UI ──

    /**
     * Panel atas: tombol unggah file MP3 sebagai tipe media tertentu.
     * Di sinilah UPCASTING terjadi — user memilih tipe subclass,
     * lalu objek disimpan ke ArrayList bertipe AudioMedia (parent).
     */
    private void setupTopPanel() {
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 10));
        topPanel.setBorder(BorderFactory.createTitledBorder(
                "Unggah MP3 sebagai Tipe Media (Upcasting: Subclass → AudioMedia)"));

        Font btnFont = new Font("Arial", Font.BOLD, 12);

        JButton addMusicBtn = new JButton("🎵 Unggah sebagai Music");
        JButton addPodcastBtn = new JButton("🎙 Unggah sebagai Podcast");
        JButton addAudiobookBtn = new JButton("📖 Unggah sebagai Audiobook");

        addMusicBtn.setFont(btnFont);
        addPodcastBtn.setFont(btnFont);
        addAudiobookBtn.setFont(btnFont);

        addMusicBtn.addActionListener(e -> unggahSebagaiMusic());
        addPodcastBtn.addActionListener(e -> unggahSebagaiPodcast());
        addAudiobookBtn.addActionListener(e -> unggahSebagaiAudiobook());

        topPanel.add(addMusicBtn);
        topPanel.add(addPodcastBtn);
        topPanel.add(addAudiobookBtn);

        add(topPanel, BorderLayout.NORTH);
    }

    /**
     * Panel tengah: tabel daftar media dan area hasil downcasting.
     */
    private void setupCenterPanel() {
        // ── Tabel ──
        tableModel = new DefaultTableModel(COLUMN_NAMES, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };

        mediaTable = new JTable(tableModel);
        mediaTable.setFont(new Font("Arial", Font.PLAIN, 12));
        mediaTable.setRowHeight(25);
        mediaTable.getTableHeader().setFont(new Font("Arial", Font.BOLD, 12));
        mediaTable.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);

        mediaTable.getColumnModel().getColumn(0).setPreferredWidth(40);
        mediaTable.getColumnModel().getColumn(1).setPreferredWidth(150);
        mediaTable.getColumnModel().getColumn(2).setPreferredWidth(80);
        mediaTable.getColumnModel().getColumn(3).setPreferredWidth(250);

        JScrollPane tableScroll = new JScrollPane(mediaTable);
        tableScroll.setBorder(BorderFactory.createTitledBorder(
                "Daftar Media (Tersimpan sebagai AudioMedia)"));

        // ── Area Hasil ──
        resultArea = new JTextArea(8, 50);
        resultArea.setEditable(false);
        resultArea.setFont(new Font("Monospaced", Font.PLAIN, 12));
        resultArea.setText("Pilih media di tabel, lalu klik 'Lihat Detail' untuk demo Downcasting.\n\n");

        JScrollPane resultScroll = new JScrollPane(resultArea);
        resultScroll.setBorder(BorderFactory.createTitledBorder("Hasil Downcasting"));

        // ── Split Pane ──
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT,
                tableScroll, resultScroll);
        splitPane.setDividerLocation(200);

        add(splitPane, BorderLayout.CENTER);
    }

    /**
     * Panel bawah: tombol aksi downcasting dan pemutaran.
     */
    private void setupButtonPanel() {
        JPanel buttonPanel = new JPanel(new FlowLayout(FlowLayout.CENTER, 10, 8));
        buttonPanel.setBorder(BorderFactory.createTitledBorder(
                "Aksi Downcasting (AudioMedia → Subclass)"));

        Font btnFont = new Font("Arial", Font.BOLD, 12);

        JButton detailBtn = new JButton("🔍 Lihat Detail (Downcasting)");
        JButton allCastBtn = new JButton("📋 Cast Semua Media");
        JButton playBtn = new JButton("▶ Putar");
        JButton closeBtn = new JButton("✖ Close");

        detailBtn.setFont(btnFont);
        allCastBtn.setFont(btnFont);
        playBtn.setFont(btnFont);
        closeBtn.setFont(btnFont);

        detailBtn.addActionListener(e -> downcastSelected());
        allCastBtn.addActionListener(e -> downcastSemua());
        playBtn.addActionListener(e -> putarSelected());
        closeBtn.addActionListener(e -> dispose());

        buttonPanel.add(detailBtn);
        buttonPanel.add(allCastBtn);
        buttonPanel.add(playBtn);
        buttonPanel.add(closeBtn);

        add(buttonPanel, BorderLayout.SOUTH);
    }

    // ── Logika Unggah Media (UPCASTING) ──

    /**
     * Mengunggah file MP3 dan membuat objek Music.
     *
     * UPCASTING terjadi saat objek Music dimasukkan ke ArrayList<AudioMedia>:
     * {@code mediaList.add(music);} — Music (subclass) → AudioMedia (parent)
     */
    private void unggahSebagaiMusic() {
        File file = pilihFileMP3();
        if (file == null)
            return;

        // Cek duplikasi
        if (isDuplicate(file))
            return;

        // Input metadata khusus Music
        JPanel panel = new JPanel(new GridLayout(2, 2, 5, 5));
        JTextField artistField = new JTextField("Tidak Diketahui");
        JTextField genreField = new JTextField("Umum");

        panel.add(new JLabel("Artis:"));
        panel.add(artistField);
        panel.add(new JLabel("Genre:"));
        panel.add(genreField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Info Music — " + parseTitle(file.getName()),
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION)
            return;

        // Buat objek subclass Music
        Music music = new Music(file, artistField.getText(), genreField.getText());

        // ══════════════════════════════════════════════════
        // UPCASTING: Music (subclass) → AudioMedia (parent)
        // Terjadi secara IMPLISIT saat ditambahkan ke ArrayList<AudioMedia>
        // ══════════════════════════════════════════════════
        mediaList.add(music);

        appendResult("✅ [UPCASTING] File '" + music.getTitle() + "' diunggah sebagai Music.\n"
                + "   Tipe asli: " + music.getClass().getSimpleName()
                + " → Disimpan sebagai: AudioMedia\n\n");
        refreshTable();
    }

    /**
     * Mengunggah file MP3 dan membuat objek Podcast.
     * UPCASTING: Podcast (subclass) → AudioMedia (parent).
     */
    private void unggahSebagaiPodcast() {
        File file = pilihFileMP3();
        if (file == null)
            return;
        if (isDuplicate(file))
            return;

        JPanel panel = new JPanel(new GridLayout(3, 2, 5, 5));
        JTextField hostField = new JTextField("Tidak Diketahui");
        JTextField channelField = new JTextField("Tidak Diketahui");
        JTextField episodeField = new JTextField("1");

        panel.add(new JLabel("Host:"));
        panel.add(hostField);
        panel.add(new JLabel("Channel:"));
        panel.add(channelField);
        panel.add(new JLabel("Nomor Episode:"));
        panel.add(episodeField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Info Podcast — " + parseTitle(file.getName()),
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION)
            return;

        int episode = 1;
        try {
            episode = Integer.parseInt(episodeField.getText().trim());
        } catch (NumberFormatException ex) {
            /* default */ }

        Podcast podcast = new Podcast(file, hostField.getText(),
                channelField.getText(), episode);

        // ══════════════════════════════════════════════════
        // UPCASTING: Podcast (subclass) → AudioMedia (parent)
        // ══════════════════════════════════════════════════
        mediaList.add(podcast);

        appendResult("✅ [UPCASTING] File '" + podcast.getTitle() + "' diunggah sebagai Podcast.\n"
                + "   Tipe asli: " + podcast.getClass().getSimpleName()
                + " → Disimpan sebagai: AudioMedia\n\n");
        refreshTable();
    }

    /**
     * Mengunggah file MP3 dan membuat objek Audiobook.
     * UPCASTING: Audiobook (subclass) → AudioMedia (parent).
     */
    private void unggahSebagaiAudiobook() {
        File file = pilihFileMP3();
        if (file == null)
            return;
        if (isDuplicate(file))
            return;

        JPanel panel = new JPanel(new GridLayout(4, 2, 5, 5));
        JTextField authorField = new JTextField("Tidak Diketahui");
        JTextField narratorField = new JTextField("Tidak Diketahui");
        JTextField chapterField = new JTextField("1");
        JTextField totalField = new JTextField("10");

        panel.add(new JLabel("Penulis:"));
        panel.add(authorField);
        panel.add(new JLabel("Narator:"));
        panel.add(narratorField);
        panel.add(new JLabel("Bab Saat Ini:"));
        panel.add(chapterField);
        panel.add(new JLabel("Total Bab:"));
        panel.add(totalField);

        int result = JOptionPane.showConfirmDialog(this, panel,
                "Info Audiobook — " + parseTitle(file.getName()),
                JOptionPane.OK_CANCEL_OPTION);
        if (result != JOptionPane.OK_OPTION)
            return;

        int chapter = 1, total = 10;
        try {
            chapter = Integer.parseInt(chapterField.getText().trim());
        } catch (NumberFormatException ex) {
            /* default */ }
        try {
            total = Integer.parseInt(totalField.getText().trim());
        } catch (NumberFormatException ex) {
            /* default */ }

        Audiobook audiobook = new Audiobook(file, authorField.getText(),
                narratorField.getText(), chapter, total);

        // ══════════════════════════════════════════════════
        // UPCASTING: Audiobook (subclass) → AudioMedia (parent)
        // ══════════════════════════════════════════════════
        mediaList.add(audiobook);

        appendResult("✅ [UPCASTING] File '" + audiobook.getTitle() + "' diunggah sebagai Audiobook.\n"
                + "   Tipe asli: " + audiobook.getClass().getSimpleName()
                + " → Disimpan sebagai: AudioMedia\n\n");
        refreshTable();
    }

    // ── Logika Downcasting ──

    /**
     * Melakukan DOWNCASTING pada media yang dipilih di tabel.
     *
     * Downcasting bersifat EKSPLISIT: harus menggunakan operator cast
     * dan pengecekan {@code instanceof} untuk keamanan.
     *
     * Pattern:
     * 
     * <pre>
     * AudioMedia media = mediaList.get(i); // tipe parent
     * if (media instanceof Music) { // cek tipe asli
     *     Music m = (Music) media; // DOWNCASTING eksplisit
     *     m.getArtist(); // akses method subclass
     * }
     * </pre>
     */
    private void downcastSelected() {
        int selectedRow = mediaTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih media di tabel terlebih dahulu!");
            return;
        }

        // Ambil objek dari list — tipenya AudioMedia (parent)
        AudioMedia media = mediaList.get(selectedRow);

        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════\n");
        sb.append("🔍 DOWNCASTING: ").append(media.getTitle()).append("\n");
        sb.append("════════════════════════════════════════\n\n");

        // Info yang bisa diakses TANPA downcasting (method parent AudioMedia)
        sb.append("── Akses via AudioMedia (tanpa casting) ──\n");
        sb.append("  getTitle()      : ").append(media.getTitle()).append("\n");
        sb.append("  getMediaType()  : ").append(media.getMediaType()).append("\n");
        sb.append("  displayInfo()   : ").append(media.displayInfo()).append("\n");
        sb.append("  getSummary()    : ").append(media.getSummary()).append("\n");
        sb.append("  isFavorite()    : ").append(media.isFavorite() ? "Ya" : "Tidak").append("\n\n");

        // ══════════════════════════════════════════════════
        // DOWNCASTING: AudioMedia → Subclass (instanceof + explicit cast)
        // ══════════════════════════════════════════════════

        sb.append("── DOWNCASTING: AudioMedia → tipe asli ──\n");

        if (media instanceof Music) {
            Music music = (Music) media; // DOWNCASTING eksplisit

            sb.append("  ✅ instanceof Music = TRUE\n");
            sb.append("  ✅ Downcast: AudioMedia → Music\n\n");
            sb.append("  Method spesifik Music (perlu downcasting):\n");
            sb.append("    music.getArtist()         = ").append(music.getArtist()).append("\n");
            sb.append("    music.getGenre()          = ").append(music.getGenre()).append("\n");
            sb.append("    music.displayInfo(true)   = ").append(music.displayInfo(true)).append("\n");
            sb.append("    music.displayInfo(\"full\") = ").append(music.displayInfo("full")).append("\n");

        } else if (media instanceof Podcast) {
            Podcast podcast = (Podcast) media; // DOWNCASTING eksplisit

            sb.append("  ✅ instanceof Podcast = TRUE\n");
            sb.append("  ✅ Downcast: AudioMedia → Podcast\n\n");
            sb.append("  Method spesifik Podcast (perlu downcasting):\n");
            sb.append("    podcast.getHost()           = ").append(podcast.getHost()).append("\n");
            sb.append("    podcast.getEpisodeNumber()  = ").append(podcast.getEpisodeNumber()).append("\n");
            sb.append("    podcast.getChannelName()    = ").append(podcast.getChannelName()).append("\n");
            sb.append("    podcast.getDescription()    = ").append(podcast.getDescription()).append("\n");
            sb.append("    podcast.getFullInfo()       = ").append(podcast.getFullInfo()).append("\n");

        } else if (media instanceof Audiobook) {
            Audiobook audiobook = (Audiobook) media; // DOWNCASTING eksplisit

            sb.append("  ✅ instanceof Audiobook = TRUE\n");
            sb.append("  ✅ Downcast: AudioMedia → Audiobook\n\n");
            sb.append("  Method spesifik Audiobook (perlu downcasting):\n");
            sb.append("    audiobook.getAuthor()          = ").append(audiobook.getAuthor()).append("\n");
            sb.append("    audiobook.getNarrator()        = ").append(audiobook.getNarrator()).append("\n");
            sb.append("    audiobook.getChapterNumber()   = ").append(audiobook.getChapterNumber()).append("\n");
            sb.append("    audiobook.getTotalChapters()   = ").append(audiobook.getTotalChapters()).append("\n");
            sb.append("    audiobook.getProgressPercent() = ").append(audiobook.getProgressPercent()).append("%\n");
            sb.append("    audiobook.getFullInfo()        = ").append(audiobook.getFullInfo()).append("\n");
        }

        sb.append("\n");
        appendResult(sb.toString());
    }

    /**
     * Melakukan DOWNCASTING pada SEMUA media dalam daftar.
     * Menunjukkan bagaimana satu loop bisa memproses berbagai tipe
     * subclass melalui instanceof check dan explicit casting.
     */
    private void downcastSemua() {
        if (mediaList.isEmpty()) {
            showWarning("Belum ada media. Unggah file MP3 terlebih dahulu!");
            return;
        }

        StringBuilder sb = new StringBuilder();
        sb.append("════════════════════════════════════════════\n");
        sb.append("📋 DOWNCASTING SEMUA MEDIA (").append(mediaList.size()).append(" item)\n");
        sb.append("════════════════════════════════════════════\n\n");

        int musicCount = 0, podcastCount = 0, audiobookCount = 0;

        for (int i = 0; i < mediaList.size(); i++) {
            AudioMedia media = mediaList.get(i); // Tipe parent (Upcasting)

            sb.append("── Media #").append(i + 1).append(" ──\n");
            sb.append("  Tersimpan sebagai : AudioMedia\n");
            sb.append("  Tipe asli         : ").append(media.getClass().getSimpleName()).append("\n");

            // DOWNCASTING dengan instanceof check
            if (media instanceof Music) {
                Music m = (Music) media;
                sb.append("  [DOWNCAST → Music]\n");
                sb.append("    Judul: ").append(m.getTitle());
                sb.append(" | Artis: ").append(m.getArtist());
                sb.append(" | Genre: ").append(m.getGenre()).append("\n");
                musicCount++;

            } else if (media instanceof Podcast) {
                Podcast p = (Podcast) media;
                sb.append("  [DOWNCAST → Podcast]\n");
                sb.append("    Judul: ").append(p.getTitle());
                sb.append(" | Host: ").append(p.getHost());
                sb.append(" | Ep: ").append(p.getEpisodeNumber());
                sb.append(" | Channel: ").append(p.getChannelName()).append("\n");
                podcastCount++;

            } else if (media instanceof Audiobook) {
                Audiobook a = (Audiobook) media;
                sb.append("  [DOWNCAST → Audiobook]\n");
                sb.append("    Judul: ").append(a.getTitle());
                sb.append(" | Penulis: ").append(a.getAuthor());
                sb.append(" | Narator: ").append(a.getNarrator());
                sb.append(" | Bab: ").append(a.getChapterNumber()).append("/").append(a.getTotalChapters())
                        .append("\n");
                audiobookCount++;
            }
            sb.append("\n");
        }

        sb.append("── Ringkasan ──\n");
        sb.append("  Music: ").append(musicCount);
        sb.append(" | Podcast: ").append(podcastCount);
        sb.append(" | Audiobook: ").append(audiobookCount);
        sb.append(" | Total: ").append(mediaList.size()).append("\n\n");

        appendResult(sb.toString());
    }

    /**
     * Memutar media yang dipilih di tabel.
     * Menggunakan method AudioMedia (parent) — tidak perlu downcasting
     * karena player.load() menerima tipe AudioMedia.
     */
    private void putarSelected() {
        int selectedRow = mediaTable.getSelectedRow();
        if (selectedRow < 0) {
            showWarning("Pilih media di tabel terlebih dahulu!");
            return;
        }

        AudioMedia media = mediaList.get(selectedRow);
        player.load(media);
        player.play();

        JOptionPane.showMessageDialog(this,
                "Memutar: " + media.getTitle() + " (" + media.getMediaType() + ")",
                "Putar", JOptionPane.INFORMATION_MESSAGE);
    }

    // ── Utilitas ──

    /**
     * Membuka dialog pemilih file MP3.
     *
     * @return file MP3 yang dipilih, atau null jika dibatalkan
     */
    private File pilihFileMP3() {
        JFileChooser chooser = new JFileChooser();
        chooser.setFileFilter(new javax.swing.filechooser.FileNameExtensionFilter(
                "File MP3", "mp3"));
        int result = chooser.showOpenDialog(this);

        if (result == JFileChooser.APPROVE_OPTION) {
            return chooser.getSelectedFile();
        }
        return null;
    }

    /**
     * Mengecek apakah file sudah ada di daftar (duplikasi).
     *
     * @param file file yang akan dicek
     * @return true jika duplikat ditemukan
     */
    private boolean isDuplicate(File file) {
        for (AudioMedia m : mediaList) {
            if (m.getFullPathFile().equals(file.getAbsolutePath())) {
                showWarning("File sudah ada di daftar: " + m.getTitle());
                return true;
            }
        }
        return false;
    }

    /**
     * Menghapus ekstensi .mp3 dari nama file untuk ditampilkan di dialog.
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
     * Memperbarui tabel sesuai data mediaList.
     * Kolom "Info" menggunakan method displayInfo() dari AudioMedia (Upcasting) —
     * meskipun objek aslinya berbeda tipe, Java memanggil versi override
     * dari subclass masing-masing (polimorfisme).
     */
    private void refreshTable() {
        tableModel.setRowCount(0);
        for (int i = 0; i < mediaList.size(); i++) {
            AudioMedia media = mediaList.get(i); // Tipe parent (Upcasting)
            tableModel.addRow(new Object[] {
                    i + 1,
                    media.getTitle(),
                    media.getMediaType(),
                    media.displayInfo()
            });
        }
    }

    /**
     * Menambahkan teks ke area hasil.
     *
     * @param text teks yang ditambahkan
     */
    private void appendResult(String text) {
        resultArea.append(text);
        resultArea.setCaretPosition(resultArea.getDocument().getLength());
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
}
