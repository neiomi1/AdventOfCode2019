package day_08;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SpaceImageFormat
{
    private int width, height, layerCount = 0;

    private Map<String, List<Integer>> imageModel = new HashMap<String, List<Integer>>();

    private List<Integer> temp = new ArrayList<Integer>();

    private static int index = 0;

    public SpaceImageFormat(int width, int height)
    {
        this.width = width;
        this.height = height;
    }

    public void loadImage(String path)
    {
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            for (String line : allLines)
            {
                System.out.println(line.chars().count());
                line.chars().forEach(i -> addToStream(Character.getNumericValue(i)));

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
    }

    public String findLeastZeros()
    {
        int minimum = Integer.MAX_VALUE;
        String result = "No value found";
        for (String layer : imageModel.keySet())
        {
            int current = (int) imageModel.get(layer).stream().filter(i -> i == 0).count();
            if (current < minimum)
            {
                minimum = current;
                result = layer;
            }
        }
        return result;
    }

    public void printMap()
    {
        for (String layer : imageModel.keySet())
        {
            System.out.println(layer);
            imageModel.get(layer).stream().forEach(n -> System.out.print(n));
            System.out.println();
        }
    }

    public long multiplyOnesAndTwos(String layer)
    {
        return imageModel.get(layer).stream().filter(i -> i == 1).count() * imageModel.get(layer).stream().filter(i -> i == 2).count();
    }

    private void addToStream(int n)
    {
        String layer = String.format("Layer %d", layerCount);
        imageModel.put(layer, temp);
        if (temp.size() < width * height)
        {
            temp.add(n);
        }
        else
        {
            layerCount++;
            temp = new ArrayList<Integer>();
            addToStream(n);
        }

    }

    public int[] decodeImage()
    {
        int layerCounter = 0;
        int[] decodedImage = new int[width * height];
        for (int i = 0; i < width * height; i++)
        {
            decodedImage[i] = -1;
        }
        List<Integer> numDecodedPixels = new ArrayList<Integer>();
        while (numDecodedPixels.size() < width * height)
        {
            index = 0;
            String layer = String.format("Layer %d", layerCounter);
            // System.err.println("Decoding " + layer);
            imageModel.get(layer).stream().forEach(n -> checkColor(n, decodedImage, numDecodedPixels));
            layerCounter++;
        }
        return decodedImage;
    }

    private void checkColor(int n, int[] decoded, List<Integer> numDecoded)
    {
        if (n != 2 && decoded[index] == -1)
        {
            decoded[index] = n;
            numDecoded.add(n);
        }
        index++;
    }

    public void printDecodedMessage()
    {
        int[] decoded = decodeImage();
        for (int n = 0; n < height; n++)
        {
            for (int i = 0; i < width; i++)
            {
                if (decoded[i + n * width] == 0)
                {
                    System.out.print(' ');
                }
                else
                {
                    System.out.print('1');
                }
            }
            System.out.println();
        }
    }

    public static void main(String[] args)
    {
        SpaceImageFormat partOne = new SpaceImageFormat(25, 6);
        partOne.loadImage("./src/day_08/Input.txt");
        System.err.println(partOne.findLeastZeros());
        System.err.println(partOne.multiplyOnesAndTwos(partOne.findLeastZeros()));
        // partOne.printMap();
        System.err.println(Arrays.toString(partOne.decodeImage()).replaceAll("\\[", "").replaceAll("\\]", "").replaceAll(", ", ""));
        partOne.printDecodedMessage();

        // SpaceImageFormat testExamplePartTwo = new SpaceImageFormat(2, 2);
        // testExamplePartTwo.loadImage("./src/day_08/testExamplePart2.txt");
        // System.err.println(testExamplePartTwo.findLeastZeros());
        // System.err.println(testExamplePartTwo.multiplyOnesAndTwos(testExamplePartTwo.findLeastZeros()));
        // testExamplePartTwo.printMap();
        // System.err.println(Arrays.toString(testExamplePartTwo.decodeImage()).replaceAll("\\[",
        // "").replaceAll("\\]", "").replaceAll(", ", ""));
        // testExamplePartTwo.printDecodedMessage();
    }
}
