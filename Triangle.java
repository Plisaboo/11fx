package task11;

public class Triangle {
    protected double a, b, c;

    public Triangle(double a, double b, double c) {
        this.a = a;
        this.b = b;
        this.c = c;
    }

    public boolean exists() {
        return a + b > c && a + c > b && b + c > a;
    }

    public double getPerimeter() {
        return a + b + c;
    }

    public double getArea() {
        double p = getPerimeter() / 2.0;
        return Math.sqrt(p * (p - a) * (p - b) * (p - c));
    }

    public boolean isRight() {
        double[] sides = {a, b, c};
        java.util.Arrays.sort(sides);
        return Math.abs(sides[2]*sides[2] - (sides[0]*sides[0] + sides[1]*sides[1])) < 1e-6;
    }
}