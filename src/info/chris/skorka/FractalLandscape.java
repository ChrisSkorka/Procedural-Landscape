package info.chris.skorka;
import java.util.Random;

public class FractalLandscape {
    private static Random random = new Random();

    // Configurations and settings

    // display properties
    private int WIDTH = 1500;
    private int HEIGHT = 900;

    OpenGlWindow window;

    public static void main(String[] args) {

        FractalLandscape fractalLandscape = new FractalLandscape();
        fractalLandscape.open();

    }

    public FractalLandscape(){

        // window object
        window = new OpenGlWindow(
                WIDTH,
                HEIGHT,
                "Fractal Landscape",
                new OpenGlWindow.DrawEventListener() {
                    @Override
                    public void onDraw(long millis, long delta) {
                        // System.out.println(delta);

//                        // clear screen
//                        c.fill(new Color(0x000000));
//                        c.stroke(null);
//                        c.clear(0,0,0,0);


                    }
                },
                new OpenGlWindow.KeyboardEventListener() {
                    @Override
                    public void onKeyDown(int key) {
                        switch(key){
                        }
                    }

                    @Override
                    public void onKeyUp(int key) {
                        switch(key){
                        }
                    }
                },
                new OpenGlWindow.MouseEventListener() {
                    @Override
                    public void onMouseDown() {

                    }

                    @Override
                    public void onMouseUp() {

                    }
                }
        );
    }

    public void open(){
        window.open();
    }

}
