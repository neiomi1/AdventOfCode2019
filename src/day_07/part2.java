package day_07;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class part2
{

    static Scanner input = new Scanner(System.in);

    static int[] inputArray = new int[5];

    static boolean[] firstRun = new boolean[5];

    static boolean[] finished = new boolean[5];

    static int[] phaseSetting = new int[5];

    static int counter = 0;

    static int amplifierNumber = 0;

    public static int[] intComputer(String path, Integer noun, Integer verb, boolean silent) throws ArrayIndexOutOfBoundsException
    {
        List<String> allLines = new ArrayList<String>();
        try
        {
            allLines = Files.readAllLines(Paths.get(path));
            if (!silent)
            {
                System.out.println(allLines.size());
            }
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
            calculate(inputData, null, null, silent);
        }
        return new int[]
        {};
    }

    public static int[] calculate(String[] computer, Integer noun, Integer verb, boolean silent) throws ArrayIndexOutOfBoundsException
    {
        counter = 0;
        int[] input = parse_input(computer);
        if (!silent)
        {
            System.out.println("************************\nNEWCODE\n************************");
            System.out.println("Code before calculations: " + Arrays.toString(input));
        }
        int programCounter = 0;
        int[] parameterModes = new int[3];

        if (noun != null && verb != null)
        {
            input[1] = noun.intValue();
            input[2] = verb.intValue();
        }

        while (programCounter < input.length)
        {
            computer[programCounter] = addLeadingZeros(computer[programCounter]);
            parameterModes = parseParameterModes(computer[programCounter]);
            switch (computer[programCounter].substring(3))
            {
                case "01":
                    parseOpCodeAddition(computer, programCounter, parameterModes);
                    programCounter += 4;
                    // System.out.println("Step during calculations: " +
                    // Arrays.toString(input));
                    break;
                case "02":
                    parseOpCodeMultiplication(computer, programCounter, parameterModes);
                    programCounter += 4;
                    // System.out.println("Step during calculations: " +
                    // Arrays.toString(input));
                    break;
                case "03":
                    parseOpCodeInput(computer, programCounter, parameterModes);
                    programCounter += 2;
                    break;
                case "04":
                    parseOpCodeOutput(computer, programCounter, parameterModes);
                    programCounter += 2;
                    break;
                case "05":
                    programCounter = parseOpCodeJumpIf(computer, programCounter, parameterModes, true);
                    break;
                case "06":
                    programCounter = parseOpCodeJumpIf(computer, programCounter, parameterModes, false);
                    break;
                case "07":
                    parseOpCodeLessThan(computer, programCounter, parameterModes);
                    programCounter += 4;
                    break;
                case "08":
                    parseOpCodeEquals(computer, programCounter, parameterModes);
                    programCounter += 4;
                    break;
                case "99":
                    finished[amplifierNumber] = true;
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

    public static void parseOpCodeAddition(String[] computer, int programCounter, int[] parameterModes) throws ArrayIndexOutOfBoundsException
    {
        int[] parameters = parseParams(3, 2, computer, programCounter, parameterModes);
        computer[parameters[2]] = String.valueOf(parameters[0] + parameters[1]);
    }

    public static void parseOpCodeMultiplication(String[] computer, int programCounter, int[] parameterModes) throws ArrayIndexOutOfBoundsException
    {
        int[] parameters = parseParams(3, 2, computer, programCounter, parameterModes);
        computer[parameters[2]] = String.valueOf(parameters[0] * parameters[1]);
    }

    public static void parseOpCodeInput(String[] computer, int programCounter, int[] parameterModes)
    {
        // System.out.println("Enter An Integer: ");
        int address = Integer.parseInt(computer[programCounter + 1]);
        // int userInput = input.nextInt();
        // computer[address] = String.valueOf(userInput);
        if (firstRun[amplifierNumber] == true)
        {
            firstRun[amplifierNumber] = false;
            computer[address] = String.valueOf(phaseSetting[amplifierNumber]);
        }
        else
        {
            computer[address] = String.valueOf(inputArray[amplifierNumber]);
        }
    }

    public static void parseOpCodeOutput(String[] computer, int programCounter, int[] parameterModes)
    {
        int address = parseParams(1, 3, computer, programCounter, parameterModes)[0];
        // System.out.println(String.format("The output is %d", address));
        if (amplifierNumber == 4)
        {
            inputArray[0] = address;

        }
        else
        {
            inputArray[amplifierNumber + 1] = address;
        }
    }

    public static int parseOpCodeJumpIf(String[] computer, int programCounter, int[] parameterModes, boolean isTrue)
    {
        int toTest;
        if (parameterModes[0] == 0)
        {
            toTest = Integer.parseInt(computer[Integer.parseInt(computer[programCounter + 1])]);
        }
        else
        {
            toTest = Integer.parseInt(computer[programCounter + 1]);
        }
        if (isTrue && toTest != 0 || !isTrue && toTest == 0)
        {
            if (parameterModes[1] == 0)
            {
                programCounter = Integer.parseInt(computer[Integer.parseInt(computer[programCounter + 2])]);
            }
            else
            {
                programCounter = Integer.parseInt(computer[programCounter + 2]);
            }
        }
        else
        {
            programCounter += 3;
        }
        return programCounter;
    }

    public static void parseOpCodeLessThan(String[] computer, int programCounter, int[] parameterModes)
    {
        int[] parameters = parseParams(3, 2, computer, programCounter, parameterModes);
        if (parameters[0] < parameters[1])
        {
            computer[parameters[2]] = "1";
        }
        else
        {
            computer[parameters[2]] = "0";
        }
    }

    public static void parseOpCodeEquals(String[] computer, int programCounter, int[] parameterModes)
    {
        int[] parameters = parseParams(3, 2, computer, programCounter, parameterModes);
        if (parameters[0] == parameters[1])
        {
            computer[parameters[2]] = "1";
        }
        else
        {
            computer[parameters[2]] = "0";
        }
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

    public static int IntLength(int n)
    {
        if (n < 10000)
        {
            if (n < 1000)
            {
                if (n < 100)
                {
                    if (n < 10)
                    {
                        return 1;
                    }
                    return 2;
                }
                return 3;
            }
            return 4;
        }
        return 5;
    }

    public static String addLeadingZeros(String opcode)
    {
        while (opcode.length() < 5)
        {
            opcode = new String("0").concat(opcode);
        }
        return opcode;
    }

    public static int[] parseParameterModes(String opcode)
    {
        // System.out.println(opcode);
        int[] result = new int[3];
        result[0] = Integer.parseInt(opcode.substring(2, 3));
        result[1] = Integer.parseInt(opcode.substring(1, 2));
        result[2] = Integer.parseInt(opcode.substring(0, 1));
        // System.out.println(Arrays.toString(result));
        return result;
    }

    public static int[] parseParams(int n, int outputIndex, String[] computer, int programCounter, int[] parameterMode)
    {
        int[] parsedParams = new int[n];
        for (int i = 0; i < n; i++)
        {
            if (i != outputIndex && parameterMode[i] == 0)
            {
                parsedParams[i] = Integer.parseInt(computer[Integer.parseInt(computer[programCounter + i + 1])]);
            }
            else
            {
                parsedParams[i] = Integer.parseInt(computer[programCounter + i + 1]);
            }
        }
        return parsedParams;
    }

    public static int runAmplifier(int[] setting)
    {
        while (finished[0] != true && finished[1] != true && finished[2] != true && finished[3] != true && finished[4] != true)
        {
            for (int n = 0; n < 5; n++)
            {
                amplifierNumber = n;
                intComputer("./src/day_07/example2.txt", null, null, true);
            }
        }
        System.out.println(Arrays.toString(inputArray));
        return inputArray[0];
    }

    public static void getMaxAmplifier()
    {
        int max = 0;
        int[] maxPhase = new int[5];
        for (int a = 5; a < 10; a++)
        {
            for (int b = 5; b < 10; b++)
            {
                for (int c = 5; c < 10; c++)
                {
                    for (int d = 5; d < 10; d++)
                    {
                        for (int e = 5; e < 10; e++)
                        {
                            if (a != b && a != c && a != d && a != e && b != c && b != d && b != e && c != d && c != e && d != e)
                            {
                                finished = new boolean[]
                                { false, false, false, false, false };
                                firstRun = new boolean[]
                                { true, true, true, true, true };
                                phaseSetting = new int[]
                                { a, b, c, d, e };
                                int result = runAmplifier(new int[]
                                { a, b, c, d, e });
                                System.out.println(String.format(" Settings [%d,%d,%d,%d,%d] result in %d", a, b, c, d, e, result));
                                if (max < result)
                                {
                                    max = result;
                                    maxPhase = new int[]
                                    { a, b, c, d, e };
                                }
                            }
                        }

                    }
                }
            }
        }
        System.out.println(max);
        System.out.println(Arrays.toString(maxPhase));
    }

    public static void main(String[] args)
    {
        // intComputer("./src/day_07/AmplifierCode.txt", null, null, false);
        // intComputer("./src/day_02/IntCode.txt", 12, 2, false);
        // System.out.println(Arrays.toString(find_pair(19690720)));
        // intComputer("./src/day_07/AmplifierCode.txt", null, null, true);
        // intComputer("./src/day_05/TestExamples.txt", null, null, true);
        getMaxAmplifier();
        // runAmplifier(new int[]
        // { 4, 3, 2, 1, 0 });
    }
}
