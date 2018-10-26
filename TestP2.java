/*
Project by Lauren, Janie, Jared, and Camdyn
CSCE111
*/
//Import section
import javax.swing.*;
import java.util.*;
import java.lang.Math.*;

class TestP2 { //Start Class

  //defining global variables
  static int[] points = {0,0,0,0};
  static String[][] hand = {{"0","0","0","0","0"},{"0","0","0","0","0"},{"0","0","0","0","0"},{"0","0","0","0","0"}};
  static String[][] decks;
  static String[][][] current;
  static String[] played = {"","","",""};
  static int[] info = {0,0,0,0,0,0,0};
  static int rounds = 0;
  static int again = 1;
  static int man = 0;
  static int women = 0;
  static int temp;


  public static void main(String[] args){//Start Main

  //    Intro(); //calling intro method

//      CharacterSelect(); //calling CharacterSelect method

    do { //start of do while loop
        if ((rounds + 12)%12 == 0 ){
            decks = deal();
            current = draw(decks, 5, hand);}

        for (int x = 0; x < 4; x++){
            temp = showHand(current[0][x]);
            played[x] = current[0][x][temp];
            current[0][x][temp] = "0";}

        showPlayed(played);

        current = draw(current[1], 1, current[0]);
        info = values(played, info);

        points = scores(info, points);

        for (int x = 0; x < 4; x++){
            if (points[x] >= 10){
                again = 0;}}

        rounds += 1;

    } while (again == 1); //end do while loop

    close(points);

  }//end main

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //Method created by Camdyn Perkins: in:array - out:int
  public static int showHand(String[] hand){//start showHand method

    //initialize variables for method
    String filename; //Initializing filename variable
    ImageIcon icon; //Initializing ImageIcon
    JLabel lab; //creating JLabel
    JPanel panel = new JPanel(); //creating JPanel
    String choice; //Initializing choice variable
    int change; //Initializing change variable


    JOptionPane.showMessageDialog(null,"Pick a card to play this round(1-5): ");

    //loop through array input
    for (int x = 0; x <5; x++){ //begin for loop

      //import picture file from image folder based on cards in hand
      filename = "C:\\CARDS\\"+ hand[x] +".png";
      icon = new ImageIcon(filename);

      //add elements to panel to print out
      lab = new JLabel(Integer.toString(x+1)+")");
      lab.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(lab);
      lab = new JLabel(icon);
      lab.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(lab);
    } //end for loop


      //loop until they choose a number between 1 and 5
      do{ //start do while loop

        choice = JOptionPane.showInputDialog(panel);

        if (choice.equals("1") || choice.equals("2") || choice.equals("3") || choice.equals("4") || choice.equals("5")){ //start of the if statement
            change = Integer.parseInt(choice.trim());
          if (hand[change-1] == "0") {
            again = 0;
              JOptionPane.showMessageDialog(null, "That card has already been played, so it is upside down.\nPlease enter appropriate input (1-5)");
          }else{
            again = 1;}
        }else{ //end of if statement
            again = 0;
            JOptionPane.showMessageDialog(null, "Please enter appropriate input (1-5)");}

      }while (again == 0); //end of do while loop

      change = Integer.parseInt(choice.trim());

    return (change - 1); //return command

  }//end showHand method

/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //in: string[]"cards played" - out: int[]"who gets the point and how many points and next rounds infoes"

  public static int[] values(String[] cards, int[] info) {//start values method

    //initialize variables
    String num;
    float[] power = {0,0,0,0,0};

    //loop through each player and determine a number value for their card based on infoes and ruleset
    for (int x = 0; x <4; x++) { //begin for loop
      num = Character.toString(cards[x].charAt(0));

      //A=1
      if (num.equals("A")){
        power[x] = 1 + info[x];}

      //J=11
      else if (num.equals("J")){
        power[x] = 11 + info[x];}

      //Q=12
      else if (num.equals("Q")){
        power[x] = 12 + info[x];}

      //K=13
      else if (num.equals("K")){
        power[x] = 13 + info[x];}

      //7 beats all non-face cards, so I assigned it a value of 10.5
      else if (num.equals("7")){
        power[x] = (float)10.5;}

      //converts the other strings to float
      else{
        power[x] = Integer.parseInt(num) + info[x];}} //end for loop

    //calls points method to determine how many points were earned this round and by who
    info = points(cards, power, info);

    //info[4] is the value to show if this round is a war, if so, reset infoes for next round and dont calculate new ones
    if (info[4] != 1) {

      //calls nextinfo method to determine next rounds bonuses from face card rules
      info = nextinfo(power, info);
    }else{
      for (int y = 0; y <5; y++){
        info[y] = 0;}
        JOptionPane.showMessageDialog(null,"Since this round was a war, there are no bonuses for the next round.");}

    return info;}//end values

/////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //in: String[]"cards played", float[]"power for each player", int[]"infoes this round" - out: int[]"who gets the point and how many points and next rounds infoes"

  public static int[] points(String[] cards, float[] power, int[] info) {//start points method

    //initialize variables
    int max = -10;
    int who = 0;
    String suit;
    int one = 0;
    int two = 0;

    //loops through each player and determines if their card is higher than the current max
    //compares raw power

    for (int x = 0; x <4; x++) {
      if (power[x] > max) {
        max = Math.round(power[x]);
        who = x;}

      //in the case of a tie, compares suit
      if (power[x] == max) {
        suit = Character.toString(cards[x].charAt(2));

        switch(suit){
          case "C": one = 1;
            break;
          case "S": one = 2;
            break;
          case "D": one = 3;
            break;
          case "H": one = 4;
            break;}

        suit = Character.toString(cards[who].charAt(2));

        switch(suit){
          case "C": two = 1;
            break;
          case "S": two = 2;
            break;
          case "D": two = 3;
            break;
          case "H": two = 4;
            break;}

        //compares suit H->L:  Hearts, Diamonds, Spades, Clubs
        if (one > two){
          max = Math.round(power[x]);
          who = x;}

        //in case of tie, compares info recieved that round, higher info wins
        if (one == two){
          if (info[x] > info[who]){
            max = Math.round(power[x]);
            who = x;}}}}

    //sends player who wins and points to array
    info[5] = who;
    if (info[4] == 1){
      info[6] = 3;
    }else{
      info[6] = 1;}

    JOptionPane.showMessageDialog(null,"Player "+ Integer.toString(info[5]+1) +" won the round and got "+ Integer.toString(info[6]) +" points!!!"); //print out of round winner
    return info;}//end points method


/////////////////////////////////////////////////////////////////////////////////////////////////////////////

  //in: float[]"power for each player" - out: int[]"who gets the point and how many points and next rounds infoes"

  public static int[] nextinfo(float[] power, int[] info) {//start nextinfo method

    man = 0;
    women = 0;

    //loop through each player to find if any face cards were played and how many
    for (int x = 0; x <4; x++){
      if ((power[x] == 11) || (power[x] == 13)) {
        man += 1;}
      if (power[x] == 12){
        women +=1;}}

    //if there were 2+ male face cards, each player with a male face card gets -1 info next round
    if (man >= 2){
      for (int x = 0; x <4; x++){
        if ((power[x] == 11) || (power[x] == 13)) {
          info[x] -= 1;}}}

    //if there were 2+ female face cards, each player with a female face card gets -1 info next round
    if (women >= 2){
      for (int x = 0; x <4; x++){
        if (power[x] == 12){
          info[x] -= 1;}}}

    //if at least 1 male and 1 female were played, each player with a royalty, gets +1 info next round
    if ((man >=1) && (women >=1)) {
      for (int x = 0; x <4; x++){
        if ((power[x] == 11) || (power[x] == 12) || (power[x] == 13)) {
          info[x] += 1;}}}

    //if 3+ face cards were played, next round will be a war
    if ((man + women) >= 3) {
      JOptionPane.showMessageDialog(null, "WAR!");
      info[4] = 1;}

    return info;}//end nextinfo method

    //String[][] decks = deal();
//String[][][] current = draw(decks, 5, hand);

//in: none - out: String[][]"each players personal draw pile"
public static String[][] deal() {//start deal method

  //initialize variables
  String[][] decks = new String[4][13];
  String[] cards = {"A-C", "2-C", "3-C", "4-C", "5-C", "6-C", "7-C", "8-C", "9-C", "10-C", "J-C", "Q-C", "K-C",
                 "A-S", "2-S", "3-S", "4-S", "5-S", "6-S", "7-S", "8-S", "9-S", "10-S", "J-S", "Q-S", "K-S",
                 "A-D", "2-D", "3-D", "4-D", "5-D", "6-D", "7-D", "8-D", "9-D", "10-D", "J-D", "Q-D", "K-D",
                 "A-H", "2-H", "3-H", "4-H", "5-H", "6-H", "7-H", "8-H", "9-H", "10-H", "J-H", "Q-H", "K-H"};

  //shuffle deck
  ArrayList<String> temp = new ArrayList<String>(Arrays.asList(cards));
  Collections.shuffle(temp);
  for (int x=0; x<52;x++){
    cards[x] = temp.get(x);}

  //give each player a personal 13 card draw pile
  for (int x=0; x<4;x++) {
    for (int y=0; y<13;y++) {
      decks[x][y] = cards[(13*x)+y];}}

  //returns the draw piles
  return decks;}//end deal method


/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

//in: String[][]"draw piles", int"number of cards to draw", String[][]"player hands"
public static String[][][] draw(String[][] decks, int num, String[][] hand) {//start draw method

  //loop each player
  for (int x=0; x<4;x++){

    //loop number of cards to draw
    for (int y=0; y<num;y++){

      //loop number of empty spots to fill
      for (int z=0; z<5;z++){
        if (hand[x][z].equals("0")){

          //fill empty spots with a card from drawpile and set it to "0"
          hand[x][z] = decks[x][0];
          decks[x][0] = "0";

          //shifts the "0" in decks[x][0] to the end of array
          for(int i = 0; i < 13; i++) {
            if (i != 12){
              decks[x][i] = decks[x][i+1];}
            decks[x][12] = "0";}}}}}

  //returns player hands and remaining draw piles
  String[][][] current = {hand, decks};
  return current;}//end draw method

  /////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////

  public static int[] scores(int[] info, int[] score) { //start scores method

    points[info[5]] += info[6];

JOptionPane.showMessageDialog(null,"SCORE BOARD\n\n"+ //Setting up scoreboard
    "Player 1: "+ Integer.toString(score[0]) +" points\n"+
    "Player 2: "+ Integer.toString(score[1]) +" points\n"+
    "Player 3: "+ Integer.toString(score[2]) +" points\n"+
    "Player 4: "+ Integer.toString(score[3]) +" points\n");
    return points; //return points

  } //end scores method

  public static void Intro() { //start Intro method

    JOptionPane.showMessageDialog(null, "Welcome to Card of Thrones!\n" +
                                        "Summers span decades. Winters can last a lifetime, and the struggle for the Iron Throne begins.\n" +
                                        "If you follow the rules and play your cards carefully, maybe the Throne will be yours...");

    JOptionPane.showMessageDialog(null, "Rule 1: To begin, let's go over the rules");
    JOptionPane.showMessageDialog(null, "Rule 2: This game requires 4 players, who will all choose a character");
    JOptionPane.showMessageDialog(null, "Rule 3: Each player gets 13 cards and keeps 5 in their hand (No Jokers)");
    JOptionPane.showMessageDialog(null, "Rule 4: The highest card value wins, but a 7 beats all numerical cards");
    JOptionPane.showMessageDialog(null, "Rule 5: In the event of a tie, compare the suits (Hearts > Diamonds > Spades > Clubs)");
    JOptionPane.showMessageDialog(null, "Rule 6: In the event of two male face cards being played a rivalry is created, both players in the rivalry get a -1 to their next played card\n"+
                                        "Rule 7: In the event of a male and female face card being played an alliance is formed and both players get a +1 to their next played card");
    JOptionPane.showMessageDialog(null, "Rule 8: In the event that three or more face cards are played a triple point round is initiated, the winner of this round gets 3 points\n"+
                                        "During a triple round no new alliances or rivalries can be made");
    JOptionPane.showMessageDialog(null, "Rule 9: The winner is whichever player gets to 10 points first.");

  } //end scores method

  public static void CharacterSelect() { //start CharacterSelect method

    //Player Selection (Robb Stark, Daenerys Targaryen, King Joffrey, and Stannis Baratheon)

    String player1, player2, player3, player4; //Declares strings for the players

    ArrayList<String> arr = new ArrayList<String>();

      //Adds Characters to selection array

      arr.add("Robb Stark"); //Adding Robb Stark to the array
      arr.add("Daenerys Targaryen"); //Adding Daenerys Targaryen to the array
      arr.add("King Joffrey"); //Adding King Joffrey to the array
      arr.add("Stannis Baratheon"); //Adding Stannis Baratheon to the array

      System.out.println(); //Pretty formatting line

      String inp1, inp2, inp3, inp4; //Setting up strings for th inputs to assign players

      inp1 = JOptionPane.showInputDialog("Player, choose your character from the following \n Robb Stark, Daenerys Targaryen, King Joffrey, and Stannis Baratheon");
      arr.remove(inp1); //Removing the selected character from the list

      JOptionPane.showMessageDialog(null, "Remaininig available characters " + arr);
      inp2 = JOptionPane.showInputDialog("Next player, choose your character from the following " +arr );
      arr.remove(inp2); //Removing the selected character from the list

      JOptionPane.showMessageDialog(null, "Remaininig available characters " +arr);
      inp3 = JOptionPane.showInputDialog("Next player, choose your character");
      arr.remove(inp3); //Removing the selected character from the list

      JOptionPane.showMessageDialog(null, "The remaining availble character is " +arr);
      inp4 = JOptionPane.showInputDialog("Next player, choose your character");
      arr.remove(inp4); //Removing the selected character from the list

      player1 = inp1; //player one gets their character
      player2 = inp2; //player two gets their character
      player3 = inp3; //player three gets their character
      player4 = inp4; //player four gets their character

      JOptionPane.showMessageDialog(null, "So Player 1 has selected "+player1); //print out what character player one choose
      JOptionPane.showMessageDialog(null, "So Player 2 has selected "+player2); //print out what character player two choose
      JOptionPane.showMessageDialog(null, "So Player 3 has selected "+player3); //print out what character player three choose
      JOptionPane.showMessageDialog(null, "So Player 4 has selected "+player4); //print out what character player four choose

      System.out.println(player1 + "" + player2 + "" + player3 + "" + player4); //Print of the players as test (Removable)

      System.out.println("");//Pretty Formatting line

      System.out.println("Players have been selected"); //Ending character assignment

  } //end CharacterSelect method

  public static void close(int[] points) { //start close method

    int winner = 0;
    for (int x = 0; x < 4; x++){
      if (points[x] >= 10){
        winner = x + 1;}}

    JOptionPane.showMessageDialog(null,"CONGRATULATIONS PLAYER "+ Integer.toString(winner) +"!!!!!");

  }//end close method

  public static void showPlayed(String[] played) { //start showPlayed method
    String filename;
    ImageIcon icon;
    JLabel lab;
    JPanel panel = new JPanel();

    //loop through array input
    for (int x = 0; x <4; x++){

      //import picture file from image folder based on cards in hand
      filename = "C:\\CARDS\\"+ played[x] +".png";
      icon = new ImageIcon(filename);

      //add elements to panel to print out
      lab = new JLabel(Integer.toString(x+1)+")");
      lab.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(lab);
      lab = new JLabel(icon);
      lab.setHorizontalAlignment(SwingConstants.RIGHT);
      panel.add(lab);}

      JOptionPane.showMessageDialog(null, "Cards played this round ");
      JOptionPane.showMessageDialog(null,panel);

    } //end showPlayed method



} //end class
