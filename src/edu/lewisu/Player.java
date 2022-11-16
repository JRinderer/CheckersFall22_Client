package edu.lewisu;

import java.util.Scanner;

public class Player {
    boolean turn;
    String color_str = "";
    String color;
    String x_input;
    String y_input;
    String piece_input;
    String server_messg;
    String client_messg;
    Scanner player_scanner = new Scanner(System.in);
    String board;

    public void set_send_message(String message){
        System.out.println(message);
        this.client_messg = "type:move," + message;
    }

    public boolean isTurn() {
        return turn;
    }

    public String getColor() {
        return color;
    }

    public String getX_input() {
        return x_input;
    }

    public String getY_input() {
        return y_input;
    }

    public String getPiece_input() {
        return piece_input;
    }

    public String getServer_messg() {
        return server_messg;
    }

    public String getClient_messg() {
        return client_messg;
    }

    public Scanner getPlayer_scanner() {
        return player_scanner;
    }


    public String getBoard() {
        return board;
    }

    public void setBoard(String board) {
        this.board = board;
    }

    public String getColor_str() {
        return color_str;
    }

    public void setColor_str(String color_str) {
        this.color_str = color_str;
    }

    public void ask_color(){
        String color = "";
        while(color.equals("")) {
            System.out.println("Please select R or W to play as:");
            color = player_scanner.nextLine();
            if (!color.equals("R") && !color.equals("W")) {
                color = "";
            } else {
                this.color = String.valueOf("type:color_set" + "," + "color:" + color);
                this.color_str = color;
            }
        }
    }

    public void ask_x(){
        String x = "";
        //!x.equals("0") ||
        while(x.equals("")) {
            System.out.println("Please enter a valid X coordinate");
            x = player_scanner.nextLine();
            if(!x.equals("0") && !x.equals("1") && !x.equals("2") && !x.equals("3") && !x.equals("4") && !x.equals("5") && !x.equals("6") && !x.equals("7")){
                x = "";
            }else{
                this.x_input = "x_cord:"  + String.valueOf(x);
            }

        }

    }

    public void ask_y(){
        String y = "";
        while(y.equals("")) {
            System.out.println("Please enter a valid Y coordinate");
            y = player_scanner.nextLine();
            if(!y.equals("0") && !y.equals("1") && !y.equals("2") && !y.equals("3") && !y.equals("4") && !y.equals("5") && !y.equals("6") && !y.equals("7")){
                y = "";
            }else{
                this.y_input = "y_cord:" +  String.valueOf(y);
            }
        }
    }

    public void ask_piece(){
        System.out.println("Please type a piece name:");
        this.piece_input = "piece_name:" + player_scanner.nextLine();
    }


    public Player(){

    }



}
