package display;

import javafx.application.Application;
import javafx.scene.Group;
import javafx.scene.Scene;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.paint.Color;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Optional;

import observing.Observer;
import model.DrawingApp;
import model.Info;
import observing.AbstractSubject;
import observing.Subject;

/**
 * Part of Drawing_Java project.
 * This single-class JavaFX application draws squares on a window.
 * If a blank spot is clicked, a new rectangle appears. (There is a rotation
 * through a fixed set of colors.)
 * If a square is clicked, a subsequent right-click on a blank spot indicates
 * that the chosen square is supposed to move there.
 * Created July 2016
 *
 * @author James Heliotis
 */
public class ShapeGUI extends Application implements Observer< DrawingApp, Info > {
	
    private static final double DIM = 100;
    private static final double INITIAL_HEIGHT = 500;
    private static final double INITIAL_WIDTH = 700;

    private static final Color[] colors = {
            Color.RED, Color.ORANGE, Color.YELLOW, Color.GREEN,
            Color.BLUE, Color.PURPLE, Color.CYAN, Color.MAGENTA,
            Color.BLACK
    };
    private final static int NUM_COLORS = colors.length;
    private int colorChoice = 0;

    private Group root;

    private Optional< Rectangle > chosen = Optional.empty();
    private DrawingApp model;
    private HashSet<String> rectangles = new HashSet<String>();

    public static void main( String[] args ) {
        launch( args );
    }

    /**
     * Sets up the windowing stuff, prior to running the event loop.
     * Called from the Application launch.
     * @param primaryStage the first Stage object to appear on the screen
     */
    @Override
    public void start( Stage primaryStage ) {
        primaryStage.setTitle( "model Area" );

        root = new Group();

        Scene scene =  new Scene( root );
        scene.addEventHandler( MouseEvent.MOUSE_CLICKED, this::bkgdSelect );
        primaryStage.setScene( scene );
        primaryStage.setHeight( INITIAL_HEIGHT );
        primaryStage.setWidth( INITIAL_WIDTH );
        primaryStage.show();
        
        model = new DrawingApp();
        //model.subscribe( this );
    }

    /**
     * If this is the primary ("left") mouse button, remember what shape
     * was selected.
     * @param mEvent the JavaFX info for this event
     */
    private void chooseShape( MouseEvent mEvent ) {
        MouseButton button = mEvent.getButton();
        if ( button == MouseButton.PRIMARY ) {
            Rectangle sourceRect = (Rectangle)mEvent.getSource();
            chosen = Optional.of( sourceRect );
            mEvent.consume(); // Don't pass up to Scene.
        }
    }

    /**
     * If this is the primary ("left") mouse button, make a new square. If
     * this is the secondary ("right") mouse button, and a shape was
     * previously chosen move the square to this new location.
     * @param mEvent the JavaFX info for this event
     */
    private void bkgdSelect( MouseEvent mEvent ) {
        MouseButton button = mEvent.getButton();
        switch ( button ) {
            case PRIMARY:
            	model.newRectangle( mEvent.getX(), mEvent.getY() );
                break;
            case SECONDARY:
            	if( !chosen.equals( Optional.empty() ) ){
            		model.moveRectangle( chosen.get().getX(), chosen.get().getY(), mEvent.getX(), mEvent.getY());
            	}
                break;
            default:
                System.err.println( "Other mouse button pushed." );
        }
    }
    
    @Override
    public void notify( DrawingApp subject, Info info ){
		
    	if( info.oldX != 0.0d && info.oldY != 0.0d ){
    		rectangles.remove( Double.toString(info.oldX) + "," + Double.toString(info.oldY) );
    		
            chosen.ifPresent( rect -> {
                rect.setX( info.newX );
                rect.setY( info.newY );
                chosen = Optional.empty();
            });
            
            rectangles.add( Double.toString( info.newX ) + "," + Double.toString( info.newY ) );
    	}else{
            Rectangle square = new Rectangle( info.newX, info.newY, DIM, DIM );
            square.setFill( colors[ colorChoice ] );
            colorChoice = ( colorChoice + 1 ) % NUM_COLORS;
            square.addEventHandler( MouseEvent.MOUSE_CLICKED, this::chooseShape );
            root.getChildren().add( square );
            square.toFront();
            rectangles.add(Double.toString( info.newX ) + "," + Double.toString( info.newY ) );
    	}
    }
}
