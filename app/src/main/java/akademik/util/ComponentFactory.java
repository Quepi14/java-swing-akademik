package akademik.util;

import java.awt.BorderLayout;
import java.awt.Color;
import java.awt.Cursor;
import java.awt.Font;

import javax.swing.JButton;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.SwingConstants;
import javax.swing.border.CompoundBorder;
import javax.swing.border.EmptyBorder;
import javax.swing.border.LineBorder;

public class ComponentFactory {

    public static JPanel createCard(String number, String label) {
        JPanel panel = new JPanel(new BorderLayout());
        panel.setBackground(ColorConstants.CARD_COLOR);
        panel.setBorder(new CompoundBorder(
            new LineBorder(ColorConstants.BORDER_COLOR, 1, true),
            new EmptyBorder(20, 20, 20, 20)
        ));

        JLabel angka = new JLabel(number, SwingConstants.CENTER);
        angka.setFont(new Font("Segoe UI", Font.BOLD, 32));
        angka.setForeground(ColorConstants.PRIMARY_COLOR);

        JLabel judul = new JLabel(label, SwingConstants.CENTER);
        judul.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        judul.setForeground(ColorConstants.TEXT_SECONDARY);

        panel.add(angka, BorderLayout.CENTER);
        panel.add(judul, BorderLayout.SOUTH);

        return panel;
    }

    public static JLabel createHeader(String text) {
        JLabel header = new JLabel(text, SwingConstants.CENTER);
        header.setFont(new Font("Segoe UI", Font.BOLD, 24));
        header.setForeground(ColorConstants.TEXT_PRIMARY);
        header.setOpaque(true);
        header.setBackground(ColorConstants.BACKGROUND_COLOR);
        header.setBorder(new EmptyBorder(20, 0, 20, 0));
        return header;
    }

    public static JButton createSidebarButton(String text) {
        JButton button = new JButton(text);
        button.setFocusPainted(false);
        button.setBackground(ColorConstants.SIDEBAR_COLOR);
        button.setForeground(Color.WHITE);
        button.setBorderPainted(false);
        button.setFont(new Font("Segoe UI", Font.PLAIN, 12));
        button.setHorizontalAlignment(SwingConstants.LEFT);
        button.setBorder(new EmptyBorder(15, 20, 15, 20));
        button.setCursor(Cursor.getPredefinedCursor(Cursor.HAND_CURSOR));

        Color defaultColor = ColorConstants.SIDEBAR_COLOR;
        Color hoverColor = ColorConstants.BUTTON_HOVER;

        button.addMouseListener(new java.awt.event.MouseAdapter() {
            @Override
            public void mouseEntered(java.awt.event.MouseEvent evt) {
                button.setBackground(hoverColor);
            }

            @Override
            public void mouseExited(java.awt.event.MouseEvent evt) {
                button.setBackground(defaultColor);
            }
        });

        return button;
    }
}