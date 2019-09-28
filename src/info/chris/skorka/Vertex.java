package info.chris.skorka;

/**
 * Represents a vertex/point in 3D space in terms of z, y, z double values with an additional w term.
 */
public class Vertex {

    private float x, y, z, h;

    /**
     * Create vertex from doubles coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @param h h-parameter
     */
    public Vertex(float x, float y, float z, float h){
        this.x = x;
        this.y = y;
        this.z = z;
        this.h = h;
    }

    /**
     * Create vertex from doubles coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Vertex(float x, float y, float z){
        this(x,y,z,z);
    }

    public float getX() {
        return x;
    }

    public float getY() {
        return y;
    }

    public float getZ() {
        return z;
    }

    public float getH() {
        return h;
    }
}
