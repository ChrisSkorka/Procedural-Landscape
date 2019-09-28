package info.chris.skorka;
import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class FractalLandscape {
    private static Random random = new Random();

    // Configurations and settings

    // display properties
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 900;

    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private float moveForward = 0;
    private float moveSideways = 0;

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

        Camera camera = new Camera(0,0,1,0,0);

        window.setDrawEventListener(new OpenGlWindow.DrawEventListener() {
            @Override
            public void onDraw(long millis, long delta) {

                Transformation transformation = new Transformation();
                transformation.projection(90, (float)WIDTH/HEIGHT, 0.1f, 100f);
                // transformation.orthogonal(-1,1,-1,1,-1f,1f);
                // transformation.scale(0.4f, 0.4f, 0.4f);
                // transformation.rotateAbout(0,0,0f, 0f,0,0);
                // transformation.rotateX(1.0f);
                // transformation.rotateZ(millis * 0.001f);
                // transformation.rotateAbout(0,0,0,0,0.01f,0);

                float dForward = delta / 1000f * moveForward;
                float dSideways = delta / 1000f * moveSideways;
                camera.moveForward(dForward);
                camera.moveSideways(dSideways);

                transformation.transform(camera.getTransformation());

                mesh.setTransformation(transformation);
                mesh.render();
            }
        });

        window.setKeyboardEventListener(new OpenGlWindow.KeyboardEventListener() {
            @Override
            public void onKeyDown(int key) {
                switch (key){
                    case GLFW_KEY_UP:
                    case GLFW_KEY_W:
                        moveForward = 1.0f;
                        break;
                    case GLFW_KEY_DOWN:
                    case GLFW_KEY_S:
                        moveForward = -1.0f;
                        break;
                    case GLFW_KEY_RIGHT:
                    case GLFW_KEY_D:
                        moveSideways = 1.0f;
                        break;
                    case GLFW_KEY_LEFT:
                    case GLFW_KEY_A:
                        moveSideways = -1.0f;
                        break;
                }
            }

            @Override
            public void onKeyUp(int key) {
                switch (key){
                    case GLFW_KEY_UP:
                    case GLFW_KEY_W:
                    case GLFW_KEY_DOWN:
                    case GLFW_KEY_S:
                        moveForward = 0.0f;
                        break;
                    case GLFW_KEY_LEFT:
                    case GLFW_KEY_A:
                    case GLFW_KEY_RIGHT:
                    case GLFW_KEY_D:
                        moveSideways = 0.0f;
                        break;
                }
            }
        });

        window.setMouseEventListener(new OpenGlWindow.MouseEventListener() {
            @Override
            public void onMouseMove(double x, double y) {
                camera.rotate((float)(x-lastMouseX), (float)(y-lastMouseY));

                lastMouseX = x;
                lastMouseY = y;
            }

            @Override
            public void onMouseDown(int button) {

            }

            @Override
            public void onMouseUp(int button) {

            }
        });
    }

    public void open(){
        window.open();
    }

}
