package day_02;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class IntCodeComputer
{

    public static int[] intComputer(String path, Integer noun, Integer verb, boolean silent) throws ArrayIndexOutOfBoundsException
    {
        List<String> allLines = new ArrayList<String>();
        try
        {
            allLines = Files.readAllLines(Paths.get(path));
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }
        for (String line : allLines)
        {
            String[] inputData = line.split(",");
            if (allLines.size() == 1)
            {
                return calculate(inputData, noun, verb, silent);
            }
        }
        return new int[]
        {};
    }

    public static int[] calculate(String[] computer, Integer noun, Integer verb, boolean silent) throws ArrayIndexOutOfBoundsException
    {
        int[] input = parse_input(computer);
        // System.out.println("************************\nNEW
        // CODE\n************************");
        // System.out.println("Code before calculations: " +
        // Arrays.toString(input));
        int programCounter = 0;
        if (noun != null && verb != null)
        {
            input[1] = noun.intValue();
            input[2] = verb.intValue();
        }

        while (programCounter < input.length)
        {
            switch (input[programCounter])
            {
                case 1:
                    input = parseOpCode_1(input, programCounter);
                    programCounter += 4;
                    // System.out.println("Step during calculations: " +
                    // Arrays.toString(input));
                    break;
                case 2:
                    input = parseOpCode_2(input, programCounter);
                    programCounter += 4;
                    // System.out.println("Step during calculations: " +
                    // Arrays.toString(input));
                    break;
                case 99:
                default:
                    if (!silent)
                    {
                        System.out.println("Code after calculations: " + Arrays.toString(input));
                        System.out.println("Result at Position 0: " + input[0]);
                    }
                    programCounter = input.length;
                    break;
            }
        }
        return input;
    }

    public static int[] parse_input(String[] inputData)
    {
        int[] input = new int[inputData.length];
        for (int i = 0; i < inputData.length; i++)
        {
            input[i] = Integer.parseInt(inputData[i]);
        }

        return input;
    }

    public static int[] parseOpCode_1(int[] code, int counter) throws ArrayIndexOutOfBoundsException
    {
        int a = code[code[counter + 1]];
        int b = code[code[counter + 2]];
        // System.out.println(String.format("Reading %d from %d and %d from %d.
        // Putting %d in %d", a, code[counter + 1], b, code[counter + 2], a + b,
        // code[counter + 3]));
        code[code[counter + 3]] = a + b;
        return code;
    }

    public static int[] parseOpCode_2(int[] code, int counter) throws ArrayIndexOutOfBoundsException
    {
        int a = code[code[counter + 1]];
        int b = code[code[counter + 2]];
        // System.out.println(String.format("Reading %d from %d and %d from %d.
        // Putting %d in %d", a, code[counter + 1], b, code[counter + 2], a * b,
        // code[counter + 3]));
        code[code[counter + 3]] = a * b;
        return code;
    }

    public static int[] find_pair(int result)
    {
        int[] resultPair = new int[2];

        for (int noun = 0; noun < 100; noun++)
        {
            for (int verb = 0; verb < 100; verb++)
            {
                try
                {
                    int[] temp = intComputer("./src/day_02/IntCode.txt", noun, verb, true);
                    if (temp[0] == result)
                    {
                        resultPair[0] = noun;
                        resultPair[1] = verb;
                        return resultPair;
                    }
                }
                catch (ArrayIndexOutOfBoundsException e)
                {

                }

            }
        }

        return resultPair;
    }

    public static void main(String[] args)
    {
        intComputer("./src/day_02/TestExamples.txt", null, null, false);
        intComputer("./src/day_02/IntCode.txt", 12, 2, false);
        System.out.println(Arrays.toString(find_pair(19690720)));
    }
}
