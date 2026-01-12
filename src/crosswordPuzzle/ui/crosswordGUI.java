package crosswordPuzzle.ui;

import javax.swing.*;
import javax.swing.text.*;
import java.awt.*;
import java.awt.event.*;
import java.util.ArrayList;
import crosswordPuzzle.core.*;

/**
 * GUI for playing crossword puzzles with arrow key navigation and auto-checking.
 * 
 * Features:
 * - Interactive grid with single-character cells
 * - Arrow key navigation between cells
 * - Automatic word numbering
 * - Scrollable clues panel (ACROSS and DOWN)
 * - Solution verification
 */
public class crosswordGUI extends JFrame {
    // Core data
    private final char[][] solutionGrid;
    private final ArrayList<placedWord> placedWordsList;
    
    // GUI components
    private JTextField[][] guiGrid;
    private int[][] cellNum;
    
    // Constants
    private static final int CELL_SIZE = 45;
    private static final int WINDOW_WIDTH = 1000;
    private static final int WINDOW_HEIGHT = 800;
    private static final int CLUE_PANEL_WIDTH = 200;

    /**
     * Constructs the crossword GUI with the given solution and word placements.
     * 
     * @param solutionGrid the complete solution grid with correct letter placements
     * @param placedWordsList list of all placed words with positions and clues
     */
    public crosswordGUI(char[][] solutionGrid, ArrayList<placedWord> placedWordsList) {
        this.solutionGrid = solutionGrid;
        this.placedWordsList = placedWordsList;
        
        initializeDataStructures();
        initializeFrame();
        assignWordNumbers();
        buildGUI();
        
        setVisible(true);
    }

    /**
     * Initializes the grid and cell numbering arrays based on solution dimensions.
     */
    private void initializeDataStructures() {
        int rows = solutionGrid.length;
        int cols = solutionGrid[0].length;
        this.guiGrid = new JTextField[rows][cols];
        this.cellNum = new int[rows][cols];
    }

    /**
     * Configures the main JFrame properties (title, size, close behavior, layout).
     */
    private void initializeFrame() {
        setTitle("Crossword Puzzle");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setLayout(new BorderLayout());
        setSize(WINDOW_WIDTH, WINDOW_HEIGHT);
        setLocationRelativeTo(null);
    }

    /**
     * Assigns word ID numbers to the starting cells of each word.
     * These numbers appear in the top-left corner of cells.
     */
    private void assignWordNumbers() {
        for (placedWord pw : placedWordsList) {
            cellNum[pw.row][pw.col] = pw.word.id;
        }
    }

    /**
     * Builds and assembles all GUI components into the frame.
     */
    private void buildGUI() {
        JPanel gridPanel = createGridPanel();
        JScrollPane cluesPanel = createCluesPanel();
        JButton checkButton = createCheckButton();
        
        add(gridPanel, BorderLayout.CENTER);
        add(cluesPanel, BorderLayout.EAST);
        add(checkButton, BorderLayout.SOUTH);
    }

    /**
     * Creates the main crossword grid panel with all cells.
     * 
     * @return JPanel containing the complete crossword grid
     */
    private JPanel createGridPanel() {
        int rows = solutionGrid.length;
        int cols = solutionGrid[0].length;
        JPanel gridPanel = new JPanel(new GridLayout(rows, cols));
        
        for (int r = 0; r < rows; r++) {
            for (int c = 0; c < cols; c++) {
                if (solutionGrid[r][c] == '-') {
                    gridPanel.add(createBlockedCell());
                } else {
                    gridPanel.add(createInteractiveCell(r, c));
                }
            }
        }
        
        return gridPanel;
    }

    /**
     * Creates a blocked (empty) cell that's not part of any word.
     * 
     * @return JPanel representing a blocked cell
     */
    private JPanel createBlockedCell() {
        return new JPanel();
    }

    /**
     * Creates an interactive cell that can accept user input.
     * 
     * @param row the row position of this cell
     * @param col the column position of this cell
     * @return JLayeredPane containing the text field and optional word number
     */
    private JLayeredPane createInteractiveCell(int row, int col) {
        JLayeredPane cellPanel = new JLayeredPane();
        cellPanel.setPreferredSize(new Dimension(CELL_SIZE, CELL_SIZE));
        cellPanel.setLayout(null);
        
        // Add the text input field
        JTextField textField = createTextField(row, col);
        cellPanel.add(textField, JLayeredPane.DEFAULT_LAYER);
        guiGrid[row][col] = textField;
        
        // Add word number label if this cell starts a word
        if (cellNum[row][col] > 0) {
            JLabel numberLabel = createNumberLabel(cellNum[row][col]);
            cellPanel.add(numberLabel, JLayeredPane.PALETTE_LAYER);
            cellPanel.moveToFront(numberLabel);
        }
        
        return cellPanel;
    }

    /**
     * Creates a text field for a single cell with input restrictions and navigation.
     * 
     * @param row the row position of this cell
     * @param col the column position of this cell
     * @return JTextField configured for single-character uppercase input
     */
    private JTextField createTextField(int row, int col) {
        JTextField field = new JTextField();
        field.setHorizontalAlignment(JTextField.CENTER);
        field.setBounds(0, 0, CELL_SIZE, CELL_SIZE);
        
        // Restrict to single uppercase character
        ((AbstractDocument) field.getDocument()).setDocumentFilter(new SingleCharDocumentFilter());
        
        // Add arrow key navigation
        field.addKeyListener(new CellNavigationListener(row, col));
        
        return field;
    }

    /**
     * Creates a label displaying the word number for a cell.
     * 
     * @param number the word number to display
     * @return JLabel positioned in the top-left corner of the cell
     */
    private JLabel createNumberLabel(int number) {
        JLabel label = new JLabel(String.valueOf(number));
        label.setFont(new Font("Arial", Font.BOLD, 14));
        label.setForeground(Color.BLACK);
        label.setBounds(1, 1, 24, 23);
        return label;
    }

    /**
     * Creates the scrollable panel containing all clues (ACROSS and DOWN).
     * 
     * @return JScrollPane containing the clues panel
     */
    private JScrollPane createCluesPanel() {
        JPanel cluesPanel = new JPanel();
        cluesPanel.setLayout(new BoxLayout(cluesPanel, BoxLayout.Y_AXIS));
        cluesPanel.setBorder(BorderFactory.createEmptyBorder(5, 5, 5, 5));
        
        // Add ACROSS clues
        cluesPanel.add(createSectionHeader("ACROSS"));
        addCluesForOrientation(cluesPanel, false);
        
        // Add spacing
        cluesPanel.add(Box.createVerticalStrut(10));
        
        // Add DOWN clues
        cluesPanel.add(createSectionHeader("DOWN"));
        addCluesForOrientation(cluesPanel, true);
        
        JScrollPane scrollPane = new JScrollPane(cluesPanel);
        scrollPane.setPreferredSize(new Dimension(CLUE_PANEL_WIDTH, 0));
        return scrollPane;
    }

    /**
     * Creates a section header label (e.g., "ACROSS" or "DOWN").
     * 
     * @param text the header text
     * @return JLabel styled as a section header
     */
    private JLabel createSectionHeader(String text) {
        JLabel header = new JLabel(text);
        header.setFont(new Font("Arial", Font.BOLD, 14));
        return header;
    }

    /**
     * Adds clues for a specific orientation (horizontal or vertical) to the panel.
     * 
     * @param panel the panel to add clues to
     * @param isVertical true for DOWN clues, false for ACROSS clues
     */
    private void addCluesForOrientation(JPanel panel, boolean isVertical) {
        for (placedWord pw : placedWordsList) {
            if (pw.isVertical == isVertical) {
                JLabel clueLabel = new JLabel(pw.word.id + ". " + pw.word.getClue());
                clueLabel.setFont(new Font("Arial", Font.PLAIN, 12));
                panel.add(clueLabel);
            }
        }
    }

    /**
     * Creates the "Check Puzzle" button with solution verification logic.
     * 
     * @return JButton that checks if the puzzle is solved correctly
     */
    private JButton createCheckButton() {
        JButton button = new JButton("Check Puzzle");
        button.addActionListener(e -> checkSolution());
        return button;
    }

    /**
     * Checks if the puzzle is solved and displays an appropriate message.
     */
    private void checkSolution() {
        if (isSolved()) {
            JOptionPane.showMessageDialog(this, 
                "Congratulations! Puzzle solved!", 
                "Success", 
                JOptionPane.INFORMATION_MESSAGE);
        } else {
            JOptionPane.showMessageDialog(this, 
                "Not solved yet. Keep going!", 
                "Keep Trying", 
                JOptionPane.INFORMATION_MESSAGE);
        }
    }

    /**
     * Checks if all cells have been filled with correct letters.
     * 
     * @return true if the puzzle is completely and correctly solved; false otherwise
     */
    private boolean isSolved() {
        for (int r = 0; r < solutionGrid.length; r++) {
            for (int c = 0; c < solutionGrid[r].length; c++) {
                char solutionChar = solutionGrid[r][c];
                
                // Skip blocked cells
                if (solutionChar == '#' || solutionChar == '-') {
                    continue;
                }
                
                // Check if user input matches solution
                String input = guiGrid[r][c].getText().trim().toUpperCase();
                if (input.isEmpty() || input.charAt(0) != solutionChar) {
                    return false;
                }
            }
        }
        return true;
    }

    /**
     * Document filter that restricts text fields to a single uppercase character.
     */
    private class SingleCharDocumentFilter extends DocumentFilter {
        @Override
        public void replace(FilterBypass fb, int offset, int length, String text, AttributeSet attrs) 
                throws BadLocationException {
            if (text == null) return;
            
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            int newLength = currentText.length() - length + text.length();
            
            if (newLength <= 1) {
                super.replace(fb, offset, length, text.toUpperCase(), attrs);
            }
        }

        @Override
        public void insertString(FilterBypass fb, int offset, String string, AttributeSet attr) 
                throws BadLocationException {
            if (string == null) return;
            
            String currentText = fb.getDocument().getText(0, fb.getDocument().getLength());
            int newLength = currentText.length() + string.length();
            
            if (newLength <= 1) {
                super.insertString(fb, offset, string.toUpperCase(), attr);
            }
        }
    }

    /**
     * Key listener that handles arrow key navigation between cells.
     */
    private class CellNavigationListener extends KeyAdapter {
        private final int row;
        private final int col;

        public CellNavigationListener(int row, int col) {
            this.row = row;
            this.col = col;
        }

        @Override
        public void keyPressed(KeyEvent e) {
            int newRow = row;
            int newCol = col;
            
            // Calculate new position based on arrow key
            switch (e.getKeyCode()) {
                case KeyEvent.VK_UP:    newRow--; break;
                case KeyEvent.VK_DOWN:  newRow++; break;
                case KeyEvent.VK_LEFT:  newCol--; break;
                case KeyEvent.VK_RIGHT: newCol++; break;
                default: return; // Not an arrow key
            }
            
            // Move focus if the new position is valid
            if (isValidCell(newRow, newCol)) {
                guiGrid[newRow][newCol].requestFocus();
            }
        }

        /**
         * Checks if a cell position is valid (in bounds and not blocked).
         * 
         * @param r the row to check
         * @param c the column to check
         * @return true if the cell is valid and interactive
         */
        private boolean isValidCell(int r, int c) {
            return r >= 0 && r < solutionGrid.length &&
                   c >= 0 && c < solutionGrid[0].length &&
                   solutionGrid[r][c] != '-';
        }
    }
}