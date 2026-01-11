import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.util.ArrayList;

/**
 * Handles crossword GUI logic with arrow key navigation and 1-char cells
 */
public class crosswordGUI extends JFrame {
    private char[][] solutionGrid;
    private JTextField[][] guiGrid;
    private int[][] cellNum;

    public crosswordGUI(char[][] solutionGrid, ArrayList<placedWord> placedWordsList) {
        this.solutionGrid = solutionGrid;
        int rows = solutionGrid.length;
        int cols = solutionGrid[0].length;
        this.guiGrid = new JTextField[rows][cols];
        this.cellNum = new int[rows][cols];

        setTitle("Crossword Puzzle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());

        // Assign word IDs to cells
        for (placedWord pw : placedWordsList) {
            cellNum[pw.row][pw.col] = pw.word.id;
        }

        // -------- GRID PANEL --------
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                char cellValue = solutionGrid[r][c];

                if (cellValue == '-') {
                    gridPanel.add(new JPanel()); // blocked cell
                } else {
                    JLayeredPane cellPanel = new JLayeredPane();
                    cellPanel.setPreferredSize(new Dimension(45, 45));
                    cellPanel.setLayout(null);

                    JTextField cell = new JTextField();
                    cell.setHorizontalAlignment(JTextField.CENTER);
                    cell.setBounds(0, 0, 45, 45);

                    // 1-char limit + uppercase
                    ((AbstractDocument) cell.getDocument()).setDocumentFilter(new DocumentFilter() {
                        @Override
                        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) throws BadLocationException {
                            if (text == null) return;
                            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                            if ((currentText.length() - length + text.length()) <= 1) {
                                super.replace(fb, offset, length, text.toUpperCase(), attrs);
                            }
                        }

                        @Override
                        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) throws BadLocationException {
                            if (string == null) return;
                            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
                            if ((currentText.length() + string.length()) <= 1) {
                                super.insertString(fb, offset, string.toUpperCase(), attr);
                            }
                        }
                    });

                    guiGrid[r][c] = cell;
                    cellPanel.add(cell, JLayeredPane.DEFAULT_LAYER);

                    // Arrow key navigation
                    final int row = r, col = c;
                    cell.addKeyListener(new java.awt.event.KeyAdapter() {
                        @Override
                        public void keyPressed(java.awt.event.KeyEvent e) {
                            int newRow = row;
                            int newCol = col;
                            switch (e.getKeyCode()) {
                                case java.awt.event.KeyEvent.VK_UP: newRow--; break;
                                case java.awt.event.KeyEvent.VK_DOWN: newRow++; break;
                                case java.awt.event.KeyEvent.VK_LEFT: newCol--; break;
                                case java.awt.event.KeyEvent.VK_RIGHT: newCol++; break;
                                default: return;
                            }

                            if (newRow >= 0 && newRow < solutionGrid.length &&
                                newCol >= 0 && newCol < solutionGrid[0].length &&
                                solutionGrid[newRow][newCol] != '-') {
                                guiGrid[newRow][newCol].requestFocus();
                            }
                        }
                    });

                    // Display word number
                    if (cellNum[r][c] > 0) {
                        JLabel numLabel = new JLabel(String.valueOf(cellNum[r][c]));
                        numLabel.setFont(new Font("Arial", Font.BOLD, 14));
                        numLabel.setForeground(Color.BLACK);
                        numLabel.setBounds(1, 1, 24, 23);
                        cellPanel.add(numLabel, JLayeredPane.PALETTE_LAYER);
                        cellPanel.moveToFront(numLabel);
                    }

                    gridPanel.add(cellPanel);
                }
            }
        }

        // -------- CLUE PANEL --------
        JPanel cluesPanel = new JPanel();
        cluesPanel.setLayout(new BoxLayout(cluesPanel, BoxLayout.Y_AXIS));
        cluesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));

        JLabel acrossLabel = new JLabel("ACROSS");
        acrossLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cluesPanel.add(acrossLabel);

        for (placedWord pw : placedWordsList) {
            if (!pw.isVertical) {
                JLabel clueLabel = new JLabel(pw.word.id + ". " + pw.word.getClue());
                clueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                cluesPanel.add(clueLabel);
            }
        }

        JLabel downLabel = new JLabel("DOWN");
        downLabel.setFont(new Font("Arial", Font.BOLD, 14));
        cluesPanel.add(Box.createVerticalStrut(10));
        cluesPanel.add(downLabel);

        for (placedWord pw : placedWordsList) {
            if (pw.isVertical) {
                JLabel clueLabel = new JLabel(pw.word.id + ". " + pw.word.getClue());
                clueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                cluesPanel.add(clueLabel);
            }
        }

        JScrollPane clueScrollPane = new JScrollPane(cluesPanel);
        clueScrollPane.setPreferredSize(new Dimension(200, 0));

        // -------- CHECK BUTTON --------
        JButton checkButton = new JButton("Check Puzzle");
        checkButton.addActionListener(e -> {
            if (isSolved()) {
                JOptionPane.showMessageDialog(this, "Congratulations! Puzzle solved!");
            } else {
                JOptionPane.showMessageDialog(this, "Not solved yet. Keep going!");
            }
        });

        // -------- ADD TO FRAME --------
        add(gridPanel, BorderLayout.CENTER);
        add(clueScrollPane, BorderLayout.EAST);
        add(checkButton, BorderLayout.SOUTH);

        setSize(1000, 800);
        setLocationRelativeTo(null);
        setVisible(true);
    }

    private boolean isSolved() {
        for (int r = 0; r < solutionGrid.length; r++) {
            for (int c = 0; c < solutionGrid[r].length; c++) {
                char solutionChar = solutionGrid[r][c];
                if (solutionChar != '#' && solutionChar != '-') {
                    String input = guiGrid[r][c].getText().trim().toUpperCase();
                    if (input.isEmpty() || input.charAt(0) != solutionChar) return false;
                }
            }
        }
        return true;
    }
}
