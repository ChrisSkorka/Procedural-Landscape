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
                "Fractal Landscape"
        );

        window.setBackground(0,0,0,1);

        Shader shader = new Shader("/landscape");
        Mesh mesh = Mesh.fromStochasticFractalHeightMap(-1,1,-1,1,1,50,30,shader);


        window.setDrawEventListener(new OpenGlWindow.DrawEventListener() {
            @Override
            public void onDraw(long millis, long delta) {
                mesh.render();
            }
        });

        window.setKeyboardEventListener(new OpenGlWindow.KeyboardEventListener() {
            @Override
            public void onKeyDown(int key) {

            }

            @Override
            public void onKeyUp(int key) {

            }
        });

        window.setMouseEventListener(new OpenGlWindow.MouseEventListener() {
            @Override
            public void onMouseDown() {

            }

            @Override
            public void onMouseUp() {

            }
        });
    }

    public void open(){
        window.open();
    }

}
