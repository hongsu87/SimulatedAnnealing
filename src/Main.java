public class Main {
    public static void main(String[] args) {
        SimulatedAnnealing sa = new SimulatedAnnealing(1000, 0.95);

        int[] x = {24, 27, 31, 35, 41, 46, 51, 53, 61, 65};
        int[] y = {121, 127, 133, 139, 146, 152, 157, 160, 167, 171};

        int mse_value = sa.solve((a, b, x1, y1) -> {
            int mse = 0;
            for (int i = 0; i < x1.length; i++) {
                mse += Math.pow((a * x1[i] + b) - y1[i], 2);
            }
            return mse;
        }, x, y);

        System.out.println("회귀선 : " + sa.a0 + "x+" + sa.b0);
        System.out.println("MSE : " + mse_value);
    }
}
