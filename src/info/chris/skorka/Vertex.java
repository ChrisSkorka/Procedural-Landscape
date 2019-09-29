package info.chris.skorka;

/**
 * Represents a vertex/point in 3D space in terms of z, y, z double values with an additional w term.
 */
public class Vertex {

    private float x, y, z;

    /**
     * Create vertex from doubles coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Vertex(float x, float y, float z){
        this.x = x;
        this.y = y;
        this.z = z;
    }

    /**
     * get x component
     * @return x component
     */
    public float getX() {
        return x;
    }

    /**
     * get y component
     * @return y component
     */
    public float getY() {
        return y;
    }

    /**
     * get z component
     * @return z component
     */
    public float getZ() {
        return z;
    }
}
