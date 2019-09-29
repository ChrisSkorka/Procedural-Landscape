package info.chris.skorka;

public class Camera {

    private float x, y, z, rx, ry;

    private static final float MAX_RY = 1.5f;
    private static final float MIN_RY = -1.5f;
    private static final float ROTATION_SPEED = 0.002f;

    public Camera(float x, float y, float z, float rx, float ry){
        this.x = x;
        this.y = y;
        this.z = z;
        this.rx = rx;
        this.ry = ry;
    }

    public void moveForward(float d){

        z -= d * Math.sin(ry);
        x += d * Math.cos(ry) * Math.sin(rx);
        y += d * Math.cos(ry) * Math.cos(rx);

    }

    public void moveSideways(float d){

        x += d * Math.cos(rx);
        y -= d * Math.sin(rx);

    }

    public void moveVertical(float d){

        z += d;

    }

    public void rotate(float dx, float dy){
        rx += ROTATION_SPEED * dx;
        ry += ROTATION_SPEED * dy;

        ry = Math.max(Math.min(ry, MAX_RY), MIN_RY);
    }

    public Transformation getTransformation(){
        Transformation transformation = new Transformation();

        transformation.rotateX((float)(ry - Math.PI / 2));
        transformation.rotateZ(rx);

        transformation.translate(-x, -y, -z);

        return transformation;
    }

}
