package info.chris.skorka;

/**
 * Represents a vertex/point in 3D space in terms of z, y, z double values with an additional w term.
 */
public class Vertex {

    private float x, y, z;
    private float w = 1;

    /**
     * Create vertex from doubles coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     * @param w w-parameter
     */
    public Vertex(float x, float y, float z, float w){
        this.x = x;
        this.y = y;
        this.z = z;
        this.w = w;
    }

    /**
     * Create vertex from doubles coordinates
     * @param x x-coordinate
     * @param y y-coordinate
     * @param z z-coordinate
     */
    public Vertex(float x, float y, float z){
        this(x,y,z,1);
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

    public float getW() {
        return w;
    }
}
