package day_10;

public class Point
{

    private int x;

    private int y;

    public Point(int x, int y)
    {
        super();
        this.x = x;
        this.y = y;
    }

    public int getX()
    {
        return x;
    }

    public void setX(int x)
    {
        this.x = x;
    }

    public int getY()
    {
        return y;
    }

    public void setY(int y)
    {
        this.y = y;
    }

    @Override
    public String toString()
    {
        return "Point [x=" + x + ", y=" + y + "]";
    }

    public static Point subtract(Point a, Point b)
    {
        return new Point(a.x - b.x, a.y - b.y);
    }

    public static double CalculateDistance(Point a, Point b)
    {
        // return gcd(a.x - b.x, a.y - b.y);
        return Math.sqrt(Math.pow(a.x - b.x, 2) + Math.pow(a.y - b.y, 2));
    }

    public static Point DirectionalVector(Point a, Point b)
    {
        int x = a.x - b.x;
        int y = a.y - b.y;
        int gcd = gcd(Math.abs(x), Math.abs(y));
        if (gcd > 0)
        {
            return new Point(x / gcd, y / gcd);
        }
        return new Point(x, y);
    }

    private static int gcd(int a, int b)
    {
        if (b == 0)
        {
            return a;
        }
        return gcd(b, a % b);

    }

    public static void main(String[] args)
    {
        System.out.println(gcd(3, 8));
    }

}
