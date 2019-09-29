package info.chris.skorka;

public class Camera {

    private float x, y, z, rx, ry;

    private static final float MAX_RY = 1.5f;
    private static final float MIN_RY = -1.5f;
    private static final float ROTATION_SPEED = 0.002f;

    /**
     * Represents a camera within the world
     * @param x
     * @param y
     * @param z
     * @param rx
     * @param ry
     */
    public Camera(float x, float y, float z, float rx, float ry){
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
    }

    /**
     * fly in the direction you are looking
     * @param d
     */
    public void flyForward(float d){

        z -= d * Math.sin(ry);
        x += d * Math.cos(ry) * Math.sin(rx);
        y += d * Math.cos(ry) * Math.cos(rx);

    }

    /**
     * Move along the cameras z axis
     * @param d
     */
    public void moveForward(float d){

        x += d * Math.sin(rx);
        y += d * Math.cos(rx);

    }

    /**
     * move along the cameras y axis
     * @param d
     */
    public void moveSideways(float d){

        x += d * Math.cos(rx);
        y -= d * Math.sin(rx);

    }

    /**
     * move along the cameras z axis
     * @param d
     */
    public void moveVertical(float d){

        z += d;

    }

    /**
     * rotate the camera
     * @param dx
     * @param dy
     */
    public void rotate(float dx, float dy){
        rx += ROTATION_SPEED * dx;
        ry += ROTATION_SPEED * dy;

        // apply vertical rotation limits
        ry = Math.max(Math.min(ry, MAX_RY), MIN_RY);
    }

    /**
     * compute the transformation that positions the world into the cameras reference frame
     * @return view transformation
     */
    public Transformation getTransformation(){
        Transformation transformation = new Transformation();

        transformation.rotateX((float)(ry - Math.PI / 2));
        transformation.rotateZ(rx);

        transformation.translate(-x, -y, -z);

        return transformation;
    }

}
