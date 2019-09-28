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


         Mesh mesh = Mesh.fromStochasticFractalHeightMap(-1,1,-1,1,1.0f,20,10);
//        Mesh mesh = new Mesh(new Vertex[]{
//                new Vertex(-1f, -1f, 0),
//                new Vertex(1f, -1f, 0),
//                new Vertex(1f, 1f, 0),
//                new Vertex(-1f, 1f, 1f),
//        }, new int[]{
//                0,1,2,3
//        });
        mesh.setShader(shader);

        window.setDrawEventListener(new OpenGlWindow.DrawEventListener() {
            @Override
            public void onDraw(long millis, long delta) {

                Transformation transformation = new Transformation();
                transformation.orthogonalProjection(-1,1,-(float)HEIGHT/WIDTH,(float)HEIGHT/WIDTH,-1,1f);
                transformation.scale(0.4f, 0.4f, 0.4f);
                // transformation.rotateAbout(0,0,0f, 0f,0,0);
                // transformation.translate(1f,0,0);
                transformation.rotateX(1.0f);
                transformation.rotateZ(millis * 0.001f);
                // transformation.rotateAbout(0,0,0,0,0.01f,0);

                mesh.setTransformation(transformation);
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
