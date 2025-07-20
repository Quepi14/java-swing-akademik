package akademik.gui.panels;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Font;
import java.awt.GridLayout;

import javax.swing.BorderFactory;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JScrollPane;
import javax.swing.JTable;
import javax.swing.SwingConstants;
import javax.swing.table.JTableHeader;

import com.formdev.flatlaf.FlatClientProperties;

public class DosenDashboardPanel extends JPanel {
    public DosenDashboardPanel() {
        initComponents();
        loadDosenData();
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        // Header Panel
        JPanel headerPanel = new JPanel(new BorderLayout());
        JLabel titleLabel = new JLabel("DAFTAR DOSEN");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 24));
        titleLabel.putClientProperty(FlatClientProperties.STYLE, 
            "foreground: #2c3e50");
        
        JLabel subtitleLabel = new JLabel("Dashboard Manajemen Dosen");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.putClientProperty(FlatClientProperties.STYLE, 
            "foreground: #7f8c8d");

        headerPanel.add(titleLabel, BorderLayout.NORTH);
        headerPanel.add(subtitleLabel, BorderLayout.SOUTH);
        
        // Stats Panel
        JPanel statsPanel = createStatsPanel();
        
        // Table Panel
        JPanel tablePanel = createTablePanel();

        // Add components to main panel
        add(headerPanel, BorderLayout.NORTH);
        add(statsPanel, BorderLayout.CENTER);
        add(tablePanel, BorderLayout.SOUTH);
    }

    private JPanel createStatsPanel() {
        JPanel panel = new JPanel(new GridLayout(1, 2, 15, 0));
        panel.setBorder(BorderFactory.createEmptyBorder(15, 0, 15, 0));

        // Total Dosen Card
        JPanel totalCard = createCard("TOTAL DOSEN", "7", "#3498db", "#2980b9");
        
        // Gender Distribution Card
        JPanel genderCard = createCard("GENDER", "5L / 2P", "#e74c3c", "#c0392b");

        panel.add(totalCard);
        panel.add(genderCard);

        return panel;
    }

    private JPanel createCard(String title, String value, String bgColor, String borderColor) {
        JPanel card = new JPanel(new BorderLayout());
        card.putClientProperty(FlatClientProperties.STYLE, 
            "background: " + bgColor + "; " +
            "borderColor: " + borderColor + "; " +
            "arc: 15");
        card.setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JLabel titleLabel = new JLabel(title);
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 14));
        titleLabel.setForeground(Color.WHITE);

        JLabel valueLabel = new JLabel(value);
        valueLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        valueLabel.setForeground(Color.WHITE);
        valueLabel.setHorizontalAlignment(SwingConstants.CENTER);

        card.add(titleLabel, BorderLayout.NORTH);
        card.add(valueLabel, BorderLayout.CENTER);

        return card;
    }

    private JPanel createTablePanel() {
        JPanel panel = new JPanel(new BorderLayout());
        
        // Table model
        String[] columns = {"NID", "NAMA DOSEN + GELAR", "JENIS KELAMIN", "TTL"};
        Object[][] data = {
            {"18901202", "Dr. Lesant, S.Sam., M.Sam.", "Laki-laki", "Nagaram, 12 Shareme (86)"},
            {"19280292", "Dr. Brian Madamita, S.T., A.T.", "Laki-laki", "Senwang, 5 Hawaii (89)"},
            // ... tambahkan data lainnya
        };

        JTable table = new JTable(data, columns);
        table.setRowHeight(40);
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        
        // Style header
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 14));
        header.setBackground(new Color(44, 62, 80));
        header.setForeground(Color.WHITE);
        
        // Enable scroll
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createEmptyBorder());
        
        panel.add(scrollPane, BorderLayout.CENTER);
        return panel;
    }

    private void loadDosenData() {
    }
}