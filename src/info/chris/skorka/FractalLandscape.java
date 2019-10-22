package info.chris.skorka;

import java.util.Random;

import static org.lwjgl.glfw.GLFW.*;

public class FractalLandscape {

    // Configurations and settings

    // display properties
    private static final int WIDTH = 1500;
    private static final int HEIGHT = 900;

    // map generation parameters
    private static int MAP_RADIUS = 3;
    private static int MAP_RESOLUTION = 1000;
    private static int NUM_OCTAVES = 5;
    private static long SEED = new Random().nextLong();
    private float waterHeight = 0.4f;

    // controls state
    private double lastMouseX = 0;
    private double lastMouseY = 0;
    private float moveForward = 0;
    private float moveSideways = 0;
    private float moveVertically = 0;

    // Open GL window
    private OpenGlWindow window;

    private Mesh terrain;

    /**
     * starts a window with a generated map
     * @param args
     */
    public static void main(String[] args) {

        FractalLandscape fractalLandscape = new FractalLandscape();
        fractalLandscape.open();

    }

    /**
     * window with generated terrain map, hardware accelerated graphics and controls.
     */
    public FractalLandscape(){


        // window object
        window = new OpenGlWindow(
                WIDTH,
                HEIGHT,
                "Fractal Landscape"
        );

        // sky
        window.setBackground(115/255f, 210/255f, 255/255f, 1f);

        // load landscape shaders
        Shader shader = new Shader("/landscape");

        // height based colours
        float[] heightColors = {
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                204/255f, 204/255f, 131/255f, 1f,
                188/255f, 204/255f, 131/255f, 1f,
                178/255f, 214/255f, 99/255f, 1f,
                160/255f, 207/255f, 58/255f, 1f,
                116/255f, 191/255f, 25/255f, 1f,
                87/255f, 186/255f, 0/255f, 1f,
                8/255f, 163/255f, 0/255f, 1f,
                64/255f, 163/255f, 79/255f, 1f,
                105/255f, 181/255f, 117/255f, 1f,
                133/255f, 181/255f, 105/255f, 1f,
                156/255f, 181/255f, 121/255f, 1f,
                172/255f, 181/255f, 121/255f, 1f,
                161/255f, 158/255f, 112/255f, 1f,
                138/255f, 135/255f, 96/255f, 1f,
                163/255f, 161/255f, 131/255f, 1f,
                189/255f, 187/255f, 162/255f, 1f,
                230/255f, 228/255f, 211/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
                237/255f, 236/255f, 225/255f, 1f,
        };

        // generate terrain mesh
        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
        terrain.setShader(shader);

        // generate water mesh
        Mesh water = Mesh.fromPlane(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, 0, new float[]{
                0/255f, 90/255f, 190/255f, 0.5f
        });
        water.setShader(shader);

        // create camera
        Camera camera = new Camera(0,0,1,0,0);

        // on draw event, compute transformation and draw terrain and water
        window.setDrawEventListener(new OpenGlWindow.DrawEventListener() {
            @Override
            public void onDraw(long millis, long delta) {

                // compute perspective perspectiveProjection transformation
                Transformation transformation = new Transformation();
                transformation.perspectiveProjection(55, (float)WIDTH/HEIGHT, 0.01f, 100f);
//                transformation.orthogonalProjection(-1,1,-1,1,-1f,1f);

                // compute camera transformation
                float dForward = delta / 1000f * moveForward;
                float dSideways = delta / 1000f * moveSideways;
                float dVertical = delta / 1000f * moveVertically;
                camera.moveForward(dForward);
                camera.moveSideways(dSideways);
                camera.moveVertical(dVertical);

                transformation.transform(camera.getTransformation());

                // render terrain mesh
                terrain.setTransformation(transformation);
                terrain.render();

                // adjust water level and render water mesh
                transformation.translate(0,0, waterHeight);
                water.setTransformation(transformation);
                water.render();
            }
        });

        // keyboard events, set control states
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
                    case GLFW_KEY_SPACE:
                        moveVertically = 1.0f;
                        break;
                    case GLFW_KEY_LEFT_SHIFT:
                        moveVertically = -1.0f;
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
                    case GLFW_KEY_SPACE:
                    case GLFW_KEY_LEFT_SHIFT:
                        moveVertically = 0.0f;
                        break;
                    case GLFW_KEY_R:
                        SEED = new Random().nextLong();
                        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
                        terrain.setShader(shader);
                        break;
                    case GLFW_KEY_Z:
                        NUM_OCTAVES--;
                        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
                        terrain.setShader(shader);
                        break;
                    case GLFW_KEY_X:
                        NUM_OCTAVES++;
                        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
                        terrain.setShader(shader);
                        break;
                    case GLFW_KEY_C:
                        MAP_RESOLUTION /= 2;
                        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
                        terrain.setShader(shader);
                        break;
                    case GLFW_KEY_V:
                        MAP_RESOLUTION *= 2;
                        terrain = Mesh.fromPerlinNoiseHeightMap(-MAP_RADIUS, MAP_RADIUS, -MAP_RADIUS, MAP_RADIUS, NUM_OCTAVES, MAP_RADIUS * MAP_RESOLUTION, MAP_RADIUS * MAP_RESOLUTION, heightColors, SEED);
                        terrain.setShader(shader);
                        break;
                }
            }
        });

        // mouse events
        window.setMouseEventListener(new OpenGlWindow.MouseEventListener() {
            @Override
            public void onMouseMove(double x, double y) {

                // rotate camera by change in mouse position
                camera.rotate((float)(x-lastMouseX), (float)(y-lastMouseY));

                // remember mouse position
                lastMouseX = x;
                lastMouseY = y;
            }

            @Override
            public void onMouseScroll(double x, double y) {

                // adjust water level based on scroll change
                waterHeight += y * 0.01;
            }

            @Override
            public void onMouseDown(int button) {

            }

            @Override
            public void onMouseUp(int button) {

            }
        });
    }

    /**
     * Once setup, open the window and start
     */
    public void open(){
        window.open();
    }

}
