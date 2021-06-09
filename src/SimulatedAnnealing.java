import java.util.Random;

public class SimulatedAnnealing {
    private double t;
    private final double c;
    public int a0;
    public int b0;

    public SimulatedAnnealing(double t, double c) {
        this.t = t;
        this.c = c;
    }

    public int solve(Problem p, int[] x, int[] y) {
        Random r = new Random();

        a0 = r.nextInt(100);
        b0 = r.nextInt(1000) - 500;

        int mse0 = p.fit(a0, b0, x, y);

        while (t > 0.001) {
            int kt = (int) Math.round(t * 30);
            for (int j = 0; j < kt; j++) {
                int a1 = r.nextInt(100);
                int b1 = r.nextInt(1000) - 500;

                int mse1 = p.fit(a1, b1, x, y);

                if (mse0 > mse1) {
                    a0 = a1;
                    b0 = b1;
                    mse0 = mse1;
                } else {
                    int d = mse1 - mse0;
                    double p0 = Math.exp(-d / t);
                    if (r.nextDouble() < p0) {
                        a0 = a1;
                        b0 = b1;
                        mse0 = mse1;
                    }
                }
            }
            t *= c;
        }
        return mse0;
    }
}
