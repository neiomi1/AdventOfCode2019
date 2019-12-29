package day_10;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;

public class Asteroids
{

    private List<Point> coordinates;

    public Asteroids()
    {
        this.coordinates = new ArrayList<Point>();
    };

    public void loadInput(String path)
    {
        try
        {
            List<String> lines = Files.readAllLines(Paths.get(path));
            for (int y = 0; y < lines.size(); y++)
            {
                String[] line = lines.get(y).split("");
                for (int x = 0; x < line.length; x++)
                {
                    if (line[x].contentEquals("#"))
                    {
                        coordinates.add(new Point(x, y));
                    }
                }

            }
            System.out.println("Number of Coordinates: " + coordinates.size());
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

    }

    public Point findMostViews()
    {
        Point result = null;
        int max = 0;
        for (Point origin : coordinates)
        {
            HashMap<Point, Double> viewablePoints = new HashMap<>();
            for (Point viewGoal : coordinates)
            {
                if (origin != viewGoal)
                {
                    boolean found = false;
                    Point tempVector = Point.DirectionalVector(viewGoal, origin);
                    for (Point vector : viewablePoints.keySet())
                    {
                        if (vector.getX() == tempVector.getX() && vector.getY() == tempVector.getY())
                        {
                            found = true;
                            Double tempDistance = Point.CalculateDistance(viewGoal, origin);
                            if (tempDistance < viewablePoints.get(vector))
                            {
                                viewablePoints.put(vector, tempDistance);
                            }
                        }
                    }
                    if (!found)
                    {
                        viewablePoints.put(tempVector, Point.CalculateDistance(viewGoal, origin));
                    }
                }
            }
            if (viewablePoints.size() > max)
            {
                max = viewablePoints.size();
                result = origin;
            }
        }

        System.err.println(max);
        return result;

    }

    public List<Point> phaseOrder(Point origin)
    {
        /* Creates a Map of each Point, their angle and distance */
        List<Double> angles = new ArrayList<Double>();
        HashMap<Double, List<Point>> pointAngles = new HashMap<>();
        HashMap<Point, Double> pointDistance = new HashMap<>();
        for (Point viewGoal : coordinates)
        {
            if (origin != viewGoal)
            {
                Point tempVector = Point.subtract(viewGoal, origin);
                int x = tempVector.getX();
                int y = tempVector.getY();
                double angle = 0;
                double tempDistance = Point.CalculateDistance(viewGoal, origin);
                pointDistance.put(viewGoal, tempDistance);
                if (x >= 0 && y <= 0)
                {
                    angle = Math.asin(x / tempDistance);
                }
                else if (x >= 0 && y > 0)
                {
                    angle = 180.0 - Math.asin(x / tempDistance);
                }
                if (x < 0 && y >= 0)
                {
                    angle = 180 + Math.asin(-1 * x / tempDistance);
                }
                else if (x < 0 && y < 0)
                {
                    angle = 360 - Math.asin(-1 * x / tempDistance);
                }
                if (!angles.contains(angle))
                {
                    angles.add(angle);
                }

                if (pointAngles.containsKey(angle))
                {
                    pointAngles.get(angle).add(viewGoal);
                }
                else
                {
                    ArrayList<Point> temp = new ArrayList<Point>();
                    temp.add(viewGoal);
                    pointAngles.put(angle, temp);
                }
                // if (viewGoal.getX() == 12 && viewGoal.getY() == 1)
                // {
                // System.out.println("FIND " + angle);
                // for (Point p : pointAngles.get(angle))
                // {
                // System.out.println(p);
                // }
                // }
            }
        }

        // Something is wrong with the index of the list
        Collections.sort(angles);
        for (double n : angles)
        {
            System.err.print(n + " | ");
        }
        for (double angle : angles)
        {
            // if (angle == 0.896055384571344 || angle == 0.3671738338182192 ||
            // 0.19739555984988078 == angle)
            // {
            // System.out.println("Index" + angles.indexOf(angle));
            // }
            System.out.println("Angle" + angle);
            for (Point p : pointAngles.get(angle))
            {
                System.out.println(p);
            }
        }
        List<Point> removeOrder = new ArrayList<Point>();
        while (!pointAngles.keySet().isEmpty())
        {
            for (int i = 0; i < angles.size(); i++)
            {
                if (pointAngles.containsKey(angles.get(i)))
                {
                    Point minPoint = null;
                    double minDistance = Double.MAX_VALUE;
                    for (Point p : pointAngles.get(angles.get(i)))
                    {
                        if (pointDistance.get(p) < minDistance)
                        {
                            minDistance = pointDistance.get(p);
                            minPoint = p;
                        }
                    }
                    // if (minPoint.getX() == 8 && minPoint.getY() == 2)
                    // {
                    // System.err.println("Location of result : " +
                    // removeOrder.size());
                    // for (Point p : pointAngles.get(angles.get(i)))
                    // {
                    // System.out.println(angles.get(i));
                    // System.out.println(p);
                    // System.out.println(pointDistance.get(p));
                    // }
                    // }
                    removeOrder.add(minPoint);
                    pointAngles.get(angles.get(i)).remove(minPoint);

                    if (pointAngles.get(angles.get(i)).isEmpty())
                    {
                        pointAngles.remove(angles.get(i));
                    }
                }
            }
        }
        return removeOrder;
    }

    public static void main(String[] args)
    {
        System.out.println("Part One");
        Asteroids mainTask = new Asteroids();
        mainTask.loadInput("./src/day_10/input.txt");
        System.err.println(mainTask.findMostViews());

        System.out.println("Part Two");
        List<Point> result = mainTask.phaseOrder(mainTask.findMostViews());
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(String.format("%d: %s", i, result.get(i).toString()));
        }
        System.err.println("Part Two result: " + result.get(200).toString());

        /* Testcases */
        System.out.println();

        System.out.println("Test One - Expected 210");
        Asteroids testOne = new Asteroids();
        testOne.loadInput("./src/day_10/test1.txt");
        System.err.println(testOne.findMostViews());
        System.out.println("Test One result: ");
        result = testOne.phaseOrder(new Point(11, 13)); // new Point(11, 13) is
                                                        // equivalent to
                                                        // testOne.findMostViews();
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(String.format("%d: %s", i + 1, result.get(i).toString()));
        }
        System.err.println("Test One result: " + result.get(200).toString());

        System.out.println("Test Two - Expected 8");
        Asteroids testTwo = new Asteroids();
        testTwo.loadInput("./src/day_10/test2.txt");
        System.err.println(testTwo.findMostViews());
        System.out.println();

        System.out.println("Test Three - Expected 33");
        Asteroids testThree = new Asteroids();
        testThree.loadInput("./src/day_10/test3.txt");
        System.err.println(testThree.findMostViews());
        System.out.println();

        System.out.println("Test Four - Expected 35");
        Asteroids testFour = new Asteroids();
        testFour.loadInput("./src/day_10/test4.txt");
        System.err.println(testFour.findMostViews());
        System.out.println();

        System.out.println("Test Five - Expected 41");
        Asteroids testFive = new Asteroids();
        testFive.loadInput("./src/day_10/test5.txt");
        System.err.println(testFive.findMostViews());
        System.out.println();

        System.out.println("Test Six - ");
        Asteroids testSix = new Asteroids();
        testSix.loadInput("./src/day_10/test6.txt");
        System.err.println(testSix.findMostViews());
        result = testSix.phaseOrder(new Point(8, 3));
        for (int i = 0; i < result.size(); i++)
        {
            System.out.println(String.format("%d: %s", i + 1, result.get(i).toString()));
        }

        System.out.println(Point.DirectionalVector(new Point(12, 1), new Point(11, 13)));
        System.out.println(Point.CalculateDistance(new Point(12, 1), new Point(11, 13)));
        Point tempVector = Point.subtract(new Point(17, 1), new Point(11, 13));
        double tempDistance = Point.CalculateDistance(new Point(17, 1), new Point(11, 13));
        System.out.println(tempDistance);

        int x = tempVector.getX();
        System.out.println(x);
        int y = tempVector.getY();
        double angle = 0;
        if (x >= 0 && y <= 0)
        {
            angle = Math.asin(x / tempDistance);
        }
        if (x >= 0 && y > 0)
        {
            angle = 180.0 - Math.asin(x / tempDistance);
        }
        if (x < 0 && y >= 0)
        {
            angle = 180 + Math.asin(-1 * x / tempDistance);
        }
        if (x < 0 && y < 0)
        {
            angle = 360 - Math.asin(-1 * x / tempDistance);
        }
        System.out.println(angle);

    }
}
