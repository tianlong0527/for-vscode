import javax.swing.JFrame;

public class mainClass {
    public static final int SCREEN_WIDTH = 300;
    public static final int SCREEN_HEIGHT = 500;
    public static void main(String[] args) {
        EnterScreen enterFrame = new EnterScreen();
        enterFrame.setSize(SCREEN_WIDTH, SCREEN_HEIGHT);   //set width and height
        enterFrame.setTitle("Music Player");   //name name of screen

        enterFrame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);    //set screen of close
        enterFrame.setVisible(true);    //can view the screen
    }
}