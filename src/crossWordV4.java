import java.util.*;

public class crossWordV4 {

    public static void main(String[] args) throws Exception {
        Scanner sc = new Scanner(System.in);

        boolean running = true;

        while (running) {

            clearConsole();
            System.out.println("Crossword Puzzle Generator V5");

            int choice = 0;
            while (true) {
                System.out.println("Select an option: ");
                System.out.println("1 - Generate a new puzzle");
                System.out.println("2 - Play an existing puzzle");
                System.out.println("3 - Quit");
                System.out.print("> ");
                String input = sc.nextLine().trim();

                try {
                    choice = Integer.parseInt(input);
                    if (choice == 1 || choice == 2) {
                        break; // valid input, exit loop
                    } else if (choice == 3) {
                        System.out.println("Goodbye!");
                        System.exit(0);
                    } {
                        System.out.println("Invalid input. Please enter 1 or 2.\n");
                    }
                } catch (NumberFormatException e) {
                    System.out.println("Invalid input. Please enter 1 or 2.\n");
                }
            }

            if (choice == 1) {
                generateNewPuzzle.generatePuzzle();
                System.out.print("enter any key to continue > ");
                sc.nextLine();

            } else if (choice == 2) {
                String[] savedPuzzles = puzzleManager.listSavedPuzzles();

                if (savedPuzzles.length == 0) {
                    System.out.println("No saved Puzzles found. generate one first");
                    
                    System.out.println();
                    System.out.print("enter any key to continue > ");
                    sc.nextLine();
                }

                    System.out.println("Saved puzzles:");
                for (int i = 0; i < savedPuzzles.length; i++) {
                    System.out.println((i + 1) + " - " + savedPuzzles[i]);


                }

                Scanner sc2 = new Scanner(System.in);
                int selection = 0;
                while (true) {
                    System.out.print("Select a puzzle to play (enter number): ");
                    String input = sc2.nextLine().trim();
                    try {
                        selection = Integer.parseInt(input);
                        if (selection >= 1 && selection <= savedPuzzles.length) {
                            break;
                        } else {
                            System.out.println("Invalid input. Enter a number from the list.");
                        }
                    } catch (NumberFormatException e) {
                        System.out.println("Invalid input. Enter a number from the list.");
                    }
                }

                String chosenTitle = savedPuzzles[selection - 1];
                Object[] loaded = puzzleManager.loadPuzzle(chosenTitle);
                if (loaded == null) {
                    System.out.println("Failed to load puzzle.");
                    return;
                }

                char[][] solutionGrid = (char[][]) loaded[0];
                ArrayList<placedWord> placedWordList = (ArrayList<placedWord>) loaded[1];

                // Launch GUI
                new crosswordGUI(solutionGrid, placedWordList);
                
                System.out.println();
                System.out.print("enter any key to continue > ");
                sc.nextLine();
        
            }

            }
            sc.close();
        }
        
        public static void clearConsole() {
            System.out.print("\033[H\033[2J");
            System.out.flush();
        }

    }


