package info.chris.skorka;

public class Transformation {

    private float[][] matrix = new float[4][4];

    /**
     * Holds a 4x4 transformation matrix
     */
    public Transformation(){
        setIdentity();
    }

    /**
     * load identity matrix
     */
    public void setIdentity(){
        matrix = new float[][]{
                {1,0,0,0},
                {0,1,0,0},
                {0,0,1,0},
                {0,0,0,1}
        };
    }

    /**
     * apply a transformation
     * @param b 4x4 matrix to compounded to this transformation
     */
    public void transform(float[][] b){
        float[][] a = matrix;
        matrix = new float[4][4];

        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                for(int i = 0; i < 4; i++){
                    matrix[row][col] += a[row][i] * b[i][col];
                }
            }
        }
    }

    /**
     * apply a transformation
     * @param transformation to compounded to this transformation
     */
    public void transform(Transformation transformation){
        transform(transformation.matrix);
    }

    /**
     * orthogonal projection, maps parameters bounding box to OpenGL's unit clip box
     * @param l left
     * @param r right
     * @param b bottom
     * @param t top
     * @param n near
     * @param f far
     */
    public void orthogonalProjection(float l, float r, float b, float t, float n, float f){

        transform(new float[][]{
                {2/(r-l),   0,      0,          -(r+l)/(r-l)},
                {0,         2/(t-b),0,          -(t+b)/(t-b)},
                {0,         0,      -2/(f-n),   -(f+n)/(f-n)},
                {0,         0,      0,          1           }
        });
    }

    /**
     * perspective projection, applys perspective convergence, maps parameters perspective bounding box to OpenGL's
     * unit clip box
     * @param fov Field of view
     * @param aspect aspect ration (width / height)
     * @param f far
     * @param n near
     */
    public void perspectiveProjection(float fov, float aspect, float f, float n){

        // tan(fov/2)
        float tanFov_2 = (float)(Math.tan(Math.toRadians(fov)/2));

        transform(new float[][]{
                {1/aspect/tanFov_2,  0, 0,              0           },
                {0,         1/tanFov_2, 0,              0           },
                {0,         0,          -(f+n)/(f-n),   -2*f*n/(f-n)},
                {0,         0,          -1,             0           }
        });
    }

    /**
     * apply a translation transformation
     * @param x
     * @param y
     * @param z
     */
    public void translate(float x, float y, float z){
        transform(new float[][]{
                {1,0,0,x},
                {0,1,0,y},
                {0,0,1,z},
                {0,0,0,1}
        });
    }

    /**
     * apply a scale transformation
     * @param x
     * @param y
     * @param z
     */
    public void scale(float x, float y, float z){
        transform(new float[][]{
                {x,0,0,0},
                {0,y,0,0},
                {0,0,z,0},
                {0,0,0,1}
        });
    }

    /**
     * apply a rotation transformation about the x axis
     * @param r
     */
    public void rotateX(float r){
        transform(new float[][]{
                {1,0,0,0},
                {0,(float)Math.cos(r),(float)-Math.sin(r),0},
                {0,(float)Math.sin(r),(float)Math.cos(r),0},
                {0,0,0,1}
        });
    }

    /**
     * apply a rotation transformation about the y axis
     * @param r
     */
    public void rotateY(float r){
        transform(new float[][]{
                {(float)Math.cos(r),0,(float)Math.sin(r),0},
                {0,1,0,0},
                {(float)-Math.sin(r),0,(float)Math.cos(r),0},
                {0,0,0,1}
        });
    }

    /**
     * apply a rotation transformation about the z axis
     * @param r
     */
    public void rotateZ(float r){
        transform(new float[][]{
                {(float)Math.cos(r),(float)-Math.sin(r),0,0},
                {(float)Math.sin(r),(float)Math.cos(r),0,0},
                {0,0,1,0},
                {0,0,0,1}
        });
    }

    /**
     * apply a rotation transformation about the x, y and z axis about the ax, ay, az point
     * @param ax
     * @param ay
     * @param az
     * @param rx
     * @param ry
     * @param rz
     */
    public void rotateAbout(float ax, float ay, float az, float rx, float ry, float rz){
        translate(ax, ay, az);
        rotateX(rx);
        rotateY(ry);
        rotateZ(rz);
        translate(-ax, -ay, -az);
    }

    /**
     * convert the 4x4 matrix into a flattened array
     * @return flat array representing the 4x4 transformation matrix
     */
    public float[] toFlatArray(){
        float[] flat = new float[16];

        for(int row = 0; row < 4; row++){
            for(int col = 0; col < 4; col++){
                flat[row * 4 + col] = matrix[row][col];
            }
        }

        return flat;
    }

}
