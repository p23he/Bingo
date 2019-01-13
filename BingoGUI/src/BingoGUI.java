import java.awt.*;
import javax.swing.*;
import java.awt.event.*;
import java.util.Random;
import javax.swing.border.*;

public class BingoGUI {

    public static int[] randNumGen(int min, int max, int quantity) {//generates random non-repeating numbers between a max and min value and outputs a certain amount of numbers as an array
        Random rand = new Random();
        int[] randNumGen = new int[quantity];//initializes the array with a set amount of numbers
        for (int i = 0; i < randNumGen.length; i++) {
            randNumGen[i] = rand.nextInt(max - min + 1) + min;
            for (int n = 0; n < i; n++) {//if any of the previous generated numbers equal the current one, redo it
                if (randNumGen[n] == randNumGen[i]) {
                    i--;
                }
            }
        }
        return randNumGen;
    }

    public static String[][] callBoard () {//creates callboard taken from original bingo
        int n = 1;
        for (int row = 0; row < callBoardNumbers.length; row++) {
            for (int col = 0; col < callBoardNumbers[row].length; col++) {
                String word = "BINGO";//initializes labels
                if (col == 0) {
                    for (int i = 0; i < word.length(); i++) {
                        callBoardNumbers[i][col] = Character.toString(word.charAt(i));
                    }
                } else {
                    callBoardNumbers[row][col] = Integer.toString(n);
                    n++;
                }
            }
        }
        return callBoardNumbers;
    }

    public static void printCallBoard (String[][] callBoardNumbers, JPanel callBoardPanel) {//assigns individual labels to callboard items and adds to a panel
        for (int row = 0; row < callBoardNumbers.length; row++) {
            for (int col = 0; col < callBoardNumbers[row].length; col++) {
                JLabel callBoardLabels = new JLabel(callBoardNumbers[row][col], SwingConstants.CENTER);
                callBoardLabels.setBorder(BorderFactory.createLineBorder(Color.black));
                callBoardLabels.setForeground((col == 0) ? Color.magenta : Color.blue);
                callBoardLabels.setPreferredSize(new Dimension(50, 50));
                callBoardArray[row][col] = callBoardLabels;
                callBoardPanel.add(callBoardArray[row][col]);
            }
        }
        callBoardPanel.setLayout(new GridLayout(0, 16));
        callBoardPanel.setBorder(BorderFactory.createTitledBorder("CALL BOARD"));
    }

    public static String[][] newCard() {//generates a new random card taken from original bingo
        String[][] card = new String[6][5];
        int[] colB = randNumGen(1, 15, 5);
        int[] colI = randNumGen(16, 30, 5);
        int[] colN = randNumGen(21, 45, 5);
        int[] colG = randNumGen(46, 60, 5);
        int[] colO = randNumGen(61, 75, 5);
        for (int row = 0; row < card.length; row++) {
            if (row == 0) {//top B I N G O bar and skips the first iteration
                String word = "BINGO";
                for (int i = 0; i < word.length(); i++) {
                    card[row][i] = Character.toString(word.charAt(i));
                }
                continue;
            }
            for (int col = 0; col < card[row].length; col++) {
                switch (col) {
                    case 0:
                        card[row][col] = Integer.toString(colB[row - 1]);//B column
                        break;
                    case 1:
                        card[row][col] = Integer.toString(colI[row - 1]);//I column
                        break;
                    case 2:
                        card[row][col] = Integer.toString(colN[row - 1]);//N column
                        break;
                    case 3:
                        card[row][col] = Integer.toString(colG[row - 1]);//G column
                        break;
                    default:
                        card[row][col] = Integer.toString(colO[row - 1]);//O column
                }
                if (row == 3 && col == 2) {//the "Free" space at the middle
                    card[row][col] = "F";
                }
            }
        }
        return card;
    }

    public static void printIndividualCPUCard(String[][] card, JPanel computerPanel, JLabel[][] labelArray, JLabel label, int row, int col) {//sets colour for CPU cards
        label.setBorder(BorderFactory.createLineBorder(Color.black));
        if (row == 3 && col == 2) {
            label.setBackground(Color.blue);//if middle space, daub it
            label.setOpaque(true);
            label.setForeground(Color.white);
        } else if (row == 0){
            label.setForeground(Color.gray);//if letters set to pink
        } else {
            label.setForeground(Color.blue);//else set numbers to blue
        }
        labelArray[row][col] = label;
        computerPanel.add(labelArray[row][col]);//putting them into arrays so they can be daubed later
    }

    public static void printComputerCard(String[][] card1, String[][] card2, JPanel computerPanel) {//variation on printCard from original but CPU and player have different stuff
        for (int row = 0; row < card1.length; row++) {
            for (int col = 0; col < card1[row].length; col++) {
                JLabel card1Label = new JLabel(card1[row][col], SwingConstants.CENTER);
                printIndividualCPUCard(card1, computerPanel, card1Array, card1Label, row, col);
            }
            JLabel space = new JLabel("");//creates a gap between the cards
            computerPanel.add(space);
            for (int col = 0; col < card2[row].length; col++) {
                JLabel card2Label = new JLabel(card2[row][col], SwingConstants.CENTER);
                printIndividualCPUCard(card2, computerPanel, card2Array, card2Label, row, col);
            }
        }
        computerPanel.setBorder(BorderFactory.createTitledBorder("CPU BOARD"));
    }

    public static void printIndividualPlayerCard(String[][] card, JPanel playerPanel, JButton[][] buttonArray, JButton button, int row, int col) {//sets colour for player cards
        if (row == 3 && col == 2) {
            button.setBackground(Color.red);
            button.setEnabled(false);
        } else if (row == 0){
            button.setEnabled(false);
        } else {
            button.addActionListener(new playerDaub());//add an action listener if it's a viable spot to press
            button.setForeground(Color.red);
        }
        button.setMargin(new Insets(0, 0, 0, 0));
        buttonArray[row][col] = button;
        playerPanel.add(buttonArray[row][col]);//putting them into arrays so they can be daubed later
    }

    public static void printPlayerCard (String[][] card1, String[][] card2, JPanel playerPanel) {
        for (int row = 0; row < card1.length; row++) {//a bunch of new stuff here, but essentially the same as last method but with listeners
            for (int col = 0; col < card1[row].length; col++) {
                JButton card1Button = new JButton(card1[row][col]);
                printIndividualPlayerCard(card1, playerPanel, button1Array, card1Button, row, col);
            }
            JLabel space = new JLabel("");
            playerPanel.add(space);
            for (int col = 0; col < card2[row].length; col++) {
                JButton card2Button = new JButton(card2[row][col]);
                printIndividualPlayerCard(card2, playerPanel, button2Array, card2Button, row, col);
            }
        }
        playerPanel.setBorder(BorderFactory.createTitledBorder("PLAYER BOARD"));
    }

    public static String columnLetter(int column) {//returns column letter
        String word = "BINGO";
        return Character.toString(word.charAt((column - 1) / 15));//sets top letter label
    }

    public static boolean daubCheck(JLabel[][] card, int callNumber) {//CPU daub check
        boolean valid = false;
        for (int row = 0; row < card.length; row++) {
            for (int col = 0; col < card[row].length; col++) {//check everything on the card
                if (card[row][col].getText().equals(Integer.toString(callNumber))) {//if any of them are valid, daub it
                    card[row][col].setBackground(Color.blue);
                    card[row][col].setOpaque(true);
                    card[row][col].setForeground(Color.white);
                }
            }
        }
        return valid;
    }

    public static void endGame(JButton[][] buttons, JLabel[][] labels) {//stops game and prevents player input
        for (int row = 0; row < buttons.length; row++) {
            for (int col = 0; col < buttons[row].length; col++) {//disables everything that's not daubed
                if (buttons[row][col].getBackground() != Color.red && row != 0) {
                    buttons[row][col].setEnabled(false);
                }
                labels[row][col].setForeground(Color.gray);
            }
        }
    }

    public static String winConditionPlayer (JButton[][] playerCard) {//if any of these are true then BINGO! (modified from original bingo)
        if ((playerCard[1][0].getBackground() == Color.red && playerCard[2][1].getBackground() == Color.red && playerCard[4][3].getBackground() == Color.red && playerCard[5][4].getBackground() == Color.red && playerCard[3][2].getBackground() == Color.red)) {
            valid = "Player wins! (Diagonal Bingo!)";
            for (int i = 0; i < 5; i++) {
                playerCard[i + 1][i].setBackground(Color.green);
            }
        }
        if ((playerCard[5][0].getBackground() == Color.red && playerCard[4][1].getBackground() == Color.red && playerCard[3][2].getBackground() == Color.red && playerCard[2][3].getBackground() == Color.red && playerCard[1][4].getBackground() == Color.red)) {
            valid = "Player wins! (Diagonal Bingo!)";
            for (int i = 0; i < 5; i++) {
                playerCard[5 - i][i].setBackground(Color.green);
            }
        }
        for (int row = 1; row < 6; row++) {
            if ((playerCard[row][0].getBackground() == Color.red && playerCard[row][1].getBackground() == Color.red && playerCard[row][3].getBackground() == Color.red && playerCard[row][4].getBackground() == Color.red) && (playerCard[row][2].getBackground() == Color.red || playerCard[row][2].getBackground() == Color.red)) {
                valid = "Player wins! (Horizontal Bingo!)";
                for (int i = 0; i < 5; i++) {
                    playerCard[row][i].setBackground(Color.green);
                }
            }
        }
        for (int col = 0; col < 5; col++) {
            if ((playerCard[1][col].getBackground() == Color.red && playerCard[2][col].getBackground() == Color.red && playerCard[4][col].getBackground() == Color.red && playerCard[5][col].getBackground() == Color.red) && (playerCard[3][col].getBackground() == Color.red || playerCard[3][col].getBackground() == Color.red)) {
                valid = "Player wins! (Vertical Bingo!)";
                for (int i = 1; i < 6; i++) {
                    playerCard[i][col].setBackground(Color.green);
                }
            }
        }
        return valid;
    }

    public static String winConditionCPU (JLabel[][] computerCard) {//likewise with player win condition
        if ((computerCard[1][0].getBackground() == Color.blue && computerCard[2][1].getBackground() == Color.blue && computerCard[4][3].getBackground() == Color.blue && computerCard[5][4].getBackground() == Color.blue && computerCard[3][2].getBackground() == Color.blue)) {
            valid = "CPU wins! (Diagonal Bingo!)";
            for (int i = 0; i < 5; i++) {
                computerCard[i + 1][i].setBackground(Color.green);
            }
        }
        if (computerCard[5][0].getBackground() == Color.blue && computerCard[4][1].getBackground() == Color.blue && computerCard[3][2].getBackground() == Color.blue && computerCard[2][3].getBackground() == Color.blue && computerCard[1][4].getBackground() == Color.blue) {
            valid = "CPU wins! (Diagonal Bingo!)";
            for (int i = 0; i < 5; i++) {
                computerCard[5 - i][i].setBackground(Color.green);
            }
        }
        for (int row = 1; row < 6; row++) {
            if ((computerCard[row][0].getBackground() == Color.blue && computerCard[row][1].getBackground() == Color.blue && computerCard[row][3].getBackground() == Color.blue && computerCard[row][4].getBackground() == Color.blue) && (computerCard[row][2].getBackground() == Color.blue)) {
                valid = "CPU wins! (Horizontal Bingo!)";
                for (int i = 0; i < 5; i++) {
                    computerCard[row][i].setBackground(Color.green);
                }
            }
        }
        for (int col = 0; col < 5; col++) {
            if ((computerCard[1][col].getBackground() == Color.blue && computerCard[2][col].getBackground() == Color.blue && computerCard[4][col].getBackground() == Color.blue && computerCard[5][col].getBackground() == Color.blue) && (computerCard[3][col].getBackground() == Color.blue)) {
                valid = "CPU wins! (Vertical Bingo!)";
                for (int i = 0; i < 6; i++) {
                    computerCard[i][col].setBackground(Color.green);
                }
            }
        }
        return valid;
    }

    public static void refreshGame () {//new game, resets board and starts over
        JPanel callBoardPanel = new JPanel(new GridLayout(5, 16));
        JPanel callBoardPanel2 = new JPanel(new BorderLayout());
        JPanel computerPanel = new JPanel(new GridLayout(6, 5));
        JPanel playerPanel = new JPanel(new GridLayout(6, 5));
        JPanel cardPanel = new JPanel(new GridLayout(1, 1));
        JPanel panel = new JPanel();
        callLabel = new JLabel("Get Ready for Bingo!");
        computer1 = newCard();
        computer2 = newCard();
        player1 = newCard();
        player2 = newCard();
        numList = randNumGen(1, 75, 75);
        i = 0;

        printCallBoard(callBoard(), callBoardPanel);
        printComputerCard(computer1, computer2, computerPanel);
        printPlayerCard(player1, player2, playerPanel);
        callBoardPanel2.add(callBoardPanel, BorderLayout.WEST);
        cardPanel.add(computerPanel);
        cardPanel.add(playerPanel);
        callLabel.setFont(new Font("Arial", Font.BOLD, 26));
        callLabel.setForeground(Color.magenta);
        panel.add(callBoardPanel2);
        panel.add(cardPanel);
        panel.add(callLabel);
        panel.setPreferredSize(new Dimension (1000, 750));
        panel.setLayout(new GridLayout(3, 1));
        frame.setContentPane(panel);
        frame.revalidate();
        frame.repaint();
        timer.start();
    }

    public static void finalResults () {//stops game and displays results
        timer.stop();
        endGame(button1Array, card1Array);
        endGame(button2Array, card2Array);
    }

    private static Timer timer = new Timer(4000, new displayCall());
    private static int[] numList = randNumGen(1, 75, 75);//from original bingo, creates a random list of numbers for the calls
    private static String[][] callBoardNumbers = new String[5][16];//these 5 arrays will put the values into the JLabels/JButtons
    private static String[][] computer1 = newCard();
    private static String[][] computer2 = newCard();
    private static String[][] player1 = newCard();
    private static String[][] player2 = newCard();
    private static JLabel callLabel;
    private static JLabel[][] card1Array = new JLabel[6][5];//these 5 arrays contains the JLabels and the JButtons to be daubed later
    private static JLabel[][] card2Array = new JLabel[6][5];
    private static JButton[][] button1Array = new JButton[6][5];
    private static JButton[][] button2Array = new JButton[6][5];
    private static JLabel[][] callBoardArray = new JLabel[5][16];
    private static int i = 0;
    private static JFrame frame = new JFrame("Bingo GUI");
    private static JMenuItem newGame = new JMenuItem("New Game");//menu stuff
    private static JMenuItem callBingo = new JMenuItem("Bingo!");
    private static JMenuItem exitGame = new JMenuItem("Exit Game");
    private static JMenuItem[] menuArray = {newGame, callBingo, exitGame};
    private static String valid = "";

    public static void main(String[] args) {//the main stuff is actually in refreshGame method
        JMenuBar menuBar = new JMenuBar();//menu stuff
        JMenu fileMenu = new JMenu("File");
        refreshGame();
        newGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_N, KeyEvent.CTRL_DOWN_MASK));//set hotkeys for menus
        callBingo.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_B, KeyEvent.CTRL_DOWN_MASK));
        exitGame.setAccelerator(KeyStroke.getKeyStroke(KeyEvent.VK_E, KeyEvent.CTRL_DOWN_MASK));
        for (int i = 0; i < menuArray.length; i++) {
            menuArray[i].setActionCommand(Character.toString(menuArray[i].getText().charAt(0)));//set an action command according to their name
            menuArray[i].addActionListener(new menuActionListener());//add an actionListener to every menuItem
            fileMenu.add(menuArray[i]);//add each item to the fileMenu
        }
        menuBar.add(fileMenu);

        frame.setVisible(true);
        frame.setResizable(false);
        frame.setSize(1000, 500);
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setJMenuBar(menuBar);
        frame.pack();
    }
    private static class menuActionListener implements ActionListener {

        public void actionPerformed(ActionEvent e) {
            String event = e.getActionCommand();
            if (event.equals("N")) {//if player selects "new game"
                refreshGame();
            } else if (event.equals("B")) {//if player selects "call bingo"
                if ((!(winConditionPlayer(button1Array).equals("")) || !(winConditionPlayer(button2Array).equals("")))) {//if the win conditions don't equal a loss
                    finalResults();
                    String dialogue = (winConditionPlayer(button1Array).equals("")) ? winConditionPlayer(button2Array) : winConditionPlayer(button1Array);//black magic
                    JOptionPane.showMessageDialog(null, dialogue);//ok if either of the win conditions don't return "" (meaning they're true) then return the correct dialogue
                } else {
                    finalResults();
                    JOptionPane.showMessageDialog(null, "False Bingo! You Lose!");
                }
            } else if (event.equals("E")) {//if player selects "exit game"
                System.exit(0);
            }
        }
    }

    private static class playerDaub implements ActionListener {//activates every time player daubs and checks if valid

        public void actionPerformed(ActionEvent e) {
            String event = e.getActionCommand();
            JButton source = (JButton)e.getSource();
            if (event.equals(Integer.toString(numList[i]))) {//if it's a correct button, daub it
                source.setBackground(Color.red);
                source.setEnabled(false);
            } else {//otherwise, player loses
                finalResults();
                JOptionPane.showMessageDialog(null, "False Daub! You have lost the game!");
            }
        }
    }

    private static class displayCall implements ActionListener {//timer for next call

        public void actionPerformed(ActionEvent event) {
            i++;
            callLabel.setText("Next Call: " + columnLetter(numList[i]) + numList[i]);//same thing as original bingo
            daubCheck(callBoardArray, numList[i]);//check for the 2 CPU cards and the call board
            daubCheck(card1Array, numList[i]);
            daubCheck(card2Array, numList[i]);
            if (!(winConditionCPU(card1Array).equals("")) || !(winConditionCPU(card2Array).equals(""))) {//check explaination for player cards
                finalResults();
                String dialogue = winConditionCPU(card1Array).equals("") ? winConditionCPU(card2Array) : winConditionCPU(card1Array);//see above in menuActionListener
                JOptionPane.showMessageDialog(null, dialogue);
            }
        }
    }
}