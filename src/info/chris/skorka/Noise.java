package info.chris.skorka;

import java.util.Random;

public class Noise {

    private int octaves;
    double min, max;
    double scale;
    double shift;

    int[] p = new int[512];

    /**
     * create a randomised noise map
     * @param octaves number of octaves
     * @param min lowest sample value
     * @param max highest sample value
     */
    public Noise(int octaves, double min, double max, long seed){
        this.octaves = octaves;
        this.min = min;
        this.max = max;

        scale = 0.5f/(2-Math.pow(2,1-octaves));
        shift = 0.5;

        Random random = new Random(seed);
        for (int i=0; i < 256 ; i++) p[256+i] = p[i] = random.nextInt(256);
    }

    /**
     * sample from the octave
     * @param x
     * @param y
     * @return z value corresponding to x and y
     */
    public double sample(double x, double y){
        double z = 0;
        double frequency = 1;
        double amplitude = 1;

        // iterate through the frequencies and amplitudes and add various noise maps
        for(int i = 0; i < octaves; i++){
            z += noise(x * frequency, y * frequency) * amplitude;

            frequency *= 2;
            amplitude /= 2;
        }

        return z * scale + shift;
    }

    /**
     * sample a single noise value
     * @param x
     * @param y
     * @return z value corresponding to x and y
     */
    public double noise(double x, double y){
        // find unit quad around point modulated between 0 and 255
        int xi = (int) Math.floor(x) & 255;
        int yi = (int) Math.floor(y) & 255;

        // unit quad corner gradients
        int g1 = p[p[xi] + yi];
        int g2 = p[p[xi + 1] + yi];
        int g3 = p[p[xi] + yi + 1];
        int g4 = p[p[xi + 1] + yi + 1];

        // relative coordinates to unit quad
        double xf = x - Math.floor(x);
        double yf = y - Math.floor(y);

        // gradients at quad corners
        double d1 = grad(g1, xf, yf);
        double d2 = grad(g2, xf - 1, yf);
        double d3 = grad(g3, xf, yf - 1);
        double d4 = grad(g4, xf - 1, yf - 1);

        // compute fade curves
        double u = fade(xf);
        double v = fade(yf);

        // interpolate to find points values
        double x1Inter = lerp(u, d1, d2);
        double x2Inter = lerp(u, d3, d4);
        double yInter = lerp(v, x1Inter, x2Inter);

        return yInter;

    }

    /**
     * return gradient from hash and x, y values
     * @param hash
     * @param x
     * @param y
     * @return
     */
    private static double grad(int hash, double x, double y){
        switch(hash & 3){
            case 0: return x + y;
            case 1: return -x + y;
            case 2: return x - y;
            case 3: return -x - y;
            default: return 0;
        }
    }

    /**
     * apply 6t^5 - 15t^4 + 10t^3 to smoothly interpolate values from 0 to 1
     * @param t input
     * @return output
     */
    private static double fade(double t) {
        return t * t * t * (t * (t * 6 - 15) + 10);
    }

    private static double lerp(double t, double a, double b) {
        return a + t * (b - a);
    }
}
