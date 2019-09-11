package com.missouristate.betamen.a04_tictactoe_v0;


//In Version 0 of our TicTacToe app, we use the empty activity template and only setup the GUI.
// We use a 3 × 3 two-dimensional array of Buttons, in order to mirror the 3 × 3 two-dimensional array game in our Model
// the TicTacToe class.
// In order to keep things simple, we first place the View inside the Activity class,
// so the View and the Controller are in the same class. Later in the chapter,
// we separate the View from the Controller and place them in two different classes.

import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.Point;
import android.os.Bundle;
//import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.GridLayout;
import android.widget.TextView;

import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

public class MainActivity extends AppCompatActivity {
    private TicTacToe tttGame;
    private Button[][] buttons;                                    // creates button class with arrey
    private TextView status;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);                       // setContentView( R.layout.activity_main );
        tttGame = new TicTacToe();                                 //starts the game
        buildGuiByCode();                                          //crete the app by code to fill the screen with squares.
    }

    public void buildGuiByCode() {
        // Get width of the screen
        Point size = new Point();                                   // for example size = Point(1440,2541)
        getWindowManager().getDefaultDisplay().getSize(size);   //Standard way to get the size of the screen. returns display object with its Size and Density
        int w = size.x / TicTacToe.SIDE;                            // size.x gets a width of the display and divides by 3 (Tic Tac in the row) // for example size.x / 3 = 480

        // Create the layout manager as a GridLayout
        GridLayout gridLayout = new GridLayout(this);     //initialize GridLayout object and passing argument this to the GridLayout constructor
        gridLayout.setColumnCount(TicTacToe.SIDE);                //sets number of columns
        gridLayout.setRowCount(TicTacToe.SIDE);
        gridLayout.setRowCount(TicTacToe.SIDE + 1);

        // Create the buttons and add them to gridLayout
        buttons = new Button[TicTacToe.SIDE][TicTacToe.SIDE];
        ButtonHandler bh = new ButtonHandler();                    // new class ButtonHandler when button is pressed
        for (int row = 0; row < TicTacToe.SIDE; row++) {
            for (int col = 0; col < TicTacToe.SIDE; col++) {
                buttons[row][col] = new Button(this);     //adds button for each row and column. "this" -argument passed current context
                buttons[row][col].setTextSize((int) (w * .2));// make size of the text half of the width. we casting to int
                buttons[row][col].setOnClickListener(bh);       // register each click to our listener
                gridLayout.addView(buttons[row][col], w, w);      //We need to add them to the grid layout or otherwise it will remain blank. // for example w=480 each view will have 480 height and width
            }
        }

        // Set gridLayout as the View of this Activity
        setContentView(gridLayout);                               //Now GridLayout will take over constraint layout

        // set up layout parameters of 4th row of gridLayout    // will show how is the winner
       status =new TextView( this);
         GridLayout.Spec rowSpec = GridLayout.spec(TicTacToe.SIDE, 1);                       //selects TicTacToe 4*5 grid layout (starts selecting row after 3 ,will  be 1 row thick)
         GridLayout.Spec columnSpec = GridLayout.spec(0, TicTacToe.SIDE);                   //selects TicTacToe 4*5 grid layout (select column starting  from 0 , to 3)
         GridLayout.LayoutParams lpStatus = new GridLayout.LayoutParams(rowSpec, columnSpec);     //select the square of tiles row spec wide and column height tall.
        status.setLayoutParams(lpStatus );                                                        //permanently set layout parameters

        // set up status' characteristics
        status.setWidth(TicTacToe.SIDE *w );                    //set the same width as all buttons together
        status.setHeight(w );                                    //set the same height as the buttons
        status.setGravity(Gravity.CENTER );
        status.setBackgroundColor(Color.GREEN );
        status.setTextSize((int )(w *.15));
        status.setText(tttGame.result());                       //runs tttGame.result in TicTacToe class

        gridLayout.addView(status );                            //to show the on the display. If we do not add this line it will not appear on the screen.

    // Set gridLayout as the View of this Activity
    //   setContentView( gridLayout );
}

    public void update(int row, int col) {                                       //each time button is pressed we want to change its value .
        Log.w( "MainActivity", "Inside update: " + row + ", " + col );

        int play = tttGame.play( row, col );                                    //send it TicTacToe class to play
        if( play == 1 )
            buttons[row][col].setText( "X" );                                   // setText will write text over that button. X for the first player.
        else if( play == 2 )
            buttons[row][col].setText( "O" );
        if( tttGame.isGameOver( ) ){
            status.setBackgroundColor( Color.RED );
            enableButtons( false );                                    // game over, disable buttons
            status.setText( tttGame.result( ) );                                //will check the status of the game by running tttGame.result in the TicTacToe class
            showNewGameDialog( );	                                             // offer to play again
        }

    }
    public void enableButtons( boolean enabled ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setEnabled( enabled );                      //enable each button
    }
    private class ButtonHandler implements View.OnClickListener {             // adds ""implements View.OnClickListener"
        public void onClick( View v ) {
            Log.w( "MainActivity", "Inside onClick, v = " + v );
            for( int row = 0; row < TicTacToe.SIDE; row++ )                   //runs trough every posible button to find which one was clicked
                for( int column = 0; column < TicTacToe.SIDE; column++ )
                    if ( v == buttons[row][column] )                          //"v" is the View that we pass through the
                        update(row, column);                                  // we update the View there button was pressed
        }
    }
    public void resetButtons( ) {
        for( int row = 0; row < TicTacToe.SIDE; row++ )
            for( int col = 0; col < TicTacToe.SIDE; col++ )
                buttons[row][col].setText( "" );                               //reset the X and O from previous game
    }
    public void showNewGameDialog( ) {
        AlertDialog.Builder alert = new AlertDialog.Builder( this );  //create new AlertDialog.Builder and pass argument this.
        alert.setTitle( "This is fun" );
        alert.setMessage( "Play again?" );
        PlayDialog playAgain = new PlayDialog( );                               //Create new class to receave an answer
        alert.setPositiveButton( "YES", playAgain );
        alert.setNegativeButton( "NO", playAgain );
        alert.show( );                                                          //shows message as a pop up
    }
    //will popup after game is done.
    private class PlayDialog implements DialogInterface.OnClickListener {      //DialogInterface.OnClickListener  needed to receive answer from user.
        public void onClick( DialogInterface dialog, int id ) {                 //
            if( id == -1 ) /* YES button */ {                                   // if yes resets the game
                tttGame.resetGame( );
                enableButtons( true );
                resetButtons( );
                status.setBackgroundColor( Color.GREEN );
                status.setText( tttGame.result( ) );
            }
            else if( id == -2 )                                             // NO button
                MainActivity.this.finish( );                                // MainActivity.this.finish( ); will stop whatever goes into main activity
        }
    }
}
