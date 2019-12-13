package day_06;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;

public class Orbits
{

    private static HashMap<String, String> allOrbits;

    public static void readOrbits(String path)
    {
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            allOrbits = new HashMap<String, String>();
            for (String line : allLines)
            {
                String[] planets = line.split("\\)");
                allOrbits.put(planets[1], planets[0]);
            }
            System.out.println(String.format("Number of planets %d", allOrbits.size()));
            int allOrbitCount = 0;
            for (String plt : allOrbits.keySet())
            {
                int currentOrbitCount = countOrbits(plt, 0);
                System.out.println(String.format("Planet %s orbits %d Planets", plt, currentOrbitCount));
                allOrbitCount += currentOrbitCount;
            }
            System.out.println(String.format("Number of total Orbits in the System is: %d", allOrbitCount));
            System.out.println(String.format("Distance between Santa and YOU: %d", countDistance("SAN", "YOU")));
        }
        catch (

        IOException e)
        {
            e.printStackTrace();
        }
    }

    public static int countOrbits(String planet, int result)
    {
        if (allOrbits.containsKey(planet))
        {
            return countOrbits(allOrbits.get(planet), result + 1);
        }
        return result;
    }

    public static int countDistance(String startPlanet, String endPlanet)
    {
        List<String> startPlanetRoute = pathToRoot(startPlanet, null);
        System.out.println(Arrays.deepToString(startPlanetRoute.toArray()));
        List<String> leastCommonAncestorRoute = new ArrayList<String>();
        String lastPlanet = endPlanet;
        while (allOrbits.containsKey(lastPlanet) && !startPlanetRoute.contains(lastPlanet))
        {
            lastPlanet = allOrbits.get(lastPlanet);
            leastCommonAncestorRoute.add(lastPlanet);
        }
        System.out.println(Arrays.deepToString(leastCommonAncestorRoute.toArray()));
        return leastCommonAncestorRoute.size() - 1 + startPlanetRoute.indexOf(lastPlanet);
    }

    public static List<String> pathToRoot(String planet, List<String> list)
    {
        if (list == null)
        {
            list = new ArrayList<String>();
        }
        if (allOrbits.containsKey(planet))
        {
            list.add(allOrbits.get(planet));
        }
        else
        {
            return list;
        }

        return pathToRoot(allOrbits.get(planet), list);
    }

    public static void main(String[] args)
    {
        // TestCase
        // readOrbits("./src/day_06/TestCase1.txt");
        // Part 1 and 2
        readOrbits("./src/day_06/OrbitMap.txt");
    }
}
