package day_03;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntersectWires
{
    public static void CalculateManhattanWiresFromFile(String path)
    {
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            List<int[]> wire1 = parseWire(allLines.get(0));
            List<int[]> wire2 = parseWire(allLines.get(1));
            System.out.println(wire1.size() + " " + wire2.size());
            // List<int[]> wireIntersect = deepIntersect(wire1, wire2);
            List<int[]> wireIntersectRange = deepIntersectSignalRange(wire1, wire2);
            System.out.println("Minimal Signal Range: " + calculateSignalDelay(wireIntersectRange));

            // Part 1
            // System.out.println(wireIntersect.size());
            // System.out.println(String.format("The minimal distance of both
            // wires is %d",
            // minimalDistance(calculateManhattanDistances(wireIntersect))));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public static List<int[]> parseWire(String input)
    {
        String[] tempInput = input.split(",");
        List<int[]> wireCoordinates = new ArrayList<int[]>();
        int[] lastPosition = new int[]
        { 1, 1, 0 };
        for (String op : tempInput)
        {
            wireCoordinates.add(lastPosition);
            int tempInt = Integer.parseInt((String) op.subSequence(1, op.length()));
            // System.out.println(tempInt);
            switch (op.charAt(0))
            {
                case 'U':
                    for (int i = 0; i < tempInt; i++)
                    {
                        lastPosition[1] += 1;
                        lastPosition[2] += 1;
                        int[] tempArray = Arrays.copyOf(lastPosition, 3);
                        wireCoordinates.add(tempArray);
                    }
                    break;
                case 'D':
                    for (int i = 0; i < tempInt; i++)
                    {
                        lastPosition[1] -= 1;
                        lastPosition[2] += 1;
                        int[] tempArray = Arrays.copyOf(lastPosition, 3);
                        wireCoordinates.add(tempArray);
                    }
                    break;
                case 'R':
                    for (int i = 0; i < tempInt; i++)
                    {
                        lastPosition[0] += 1;
                        lastPosition[2] += 1;
                        int[] tempArray = Arrays.copyOf(lastPosition, 3);
                        wireCoordinates.add(tempArray);
                    }
                    break;
                case 'L':
                    for (int i = 0; i < tempInt; i++)
                    {
                        lastPosition[0] -= 1;
                        lastPosition[2] += 1;
                        int[] tempArray = Arrays.copyOf(lastPosition, 3);
                        wireCoordinates.add(tempArray);
                    }
                    break;
            }
        }
        return wireCoordinates;
    }

    public static int calculateSignalDelay(List<int[]> intersectList)
    {
        int[] signalDistances = new int[intersectList.size()];
        int minSignalDistance = Integer.MAX_VALUE;
        for (int[] intersect : intersectList)
        {
            int temp = intersect[2] + intersect[3];
            if (minSignalDistance > temp)
            {
                minSignalDistance = temp;
            }
        }
        return minSignalDistance;
    }

    public static int[] calculateManhattanDistances(List<int[]> intersectSet)
    {
        int[] distances = new int[intersectSet.size()];
        int counter = 0;
        for (int[] coord : intersectSet)
        {
            distances[counter] = Math.abs(coord[0] - 1) + Math.abs(coord[1] - 1);
            counter++;
        }
        return distances;
    }

    public static int minimalDistance(int[] distances)
    {
        // System.out.println("Distances: " + Arrays.toString(distances));
        int i = Integer.MAX_VALUE;
        for (int n : distances)
        {
            if (i > n && n > 1)
            {
                i = n;
            }
        }
        return i;
    }

    public static List<int[]> deepIntersectSignalRange(List<int[]> set1, List<int[]> set2)
    {
        List<int[]> result = new ArrayList<int[]>();
        for (int[] n : set1)
        {

            for (int[] m : set2)
            {
                if (n[0] == m[0] && n[1] == m[1])
                {
                    System.out.println("X:" + n[0] + " Y:" + n[1]);
                    int[] temp = Arrays.copyOf(n, 4);
                    temp[3] = m[2];
                    result.add(temp);
                    break;
                }
            }
        }
        return result;
    }

    public static List<int[]> deepIntersect(List<int[]> set1, List<int[]> set2)
    {
        List<int[]> result = new ArrayList<int[]>();
        for (int[] n : set1)
        {

            for (int[] m : set2)
            {
                if (n[0] == m[0] && n[1] == m[1])
                {
                    System.out.println("X:" + n[0] + " Y:" + n[1]);
                    result.add(n);
                    break;
                }
            }
        }
        return result;
    }

    public static void main(String[] args)
    {
        CalculateManhattanWiresFromFile("./src/day_03/WireInput.txt");
    }
}
