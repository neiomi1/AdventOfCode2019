package day_05;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class IntCodeComputer
{

    static Scanner input = new Scanner(System.in);

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
        System.out.println("Enter An Integer: ");
        int address = Integer.parseInt(computer[programCounter + 1]);
        int userInput = input.nextInt();
        computer[address] = String.valueOf(userInput);

    }

    public static void parseOpCodeOutput(String[] computer, int programCounter, int[] parameterModes)
    {
        int address = parseParams(1, 3, computer, programCounter, parameterModes)[0];
        System.out.println(String.format("The output is %d", address));
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

    public static void main(String[] args)
    {
        // intComputer("./src/day_02/TestExamples.txt", null, null, false);
        // intComputer("./src/day_02/IntCode.txt", 12, 2, false);
        // System.out.println(Arrays.toString(find_pair(19690720)));
        intComputer("./src/day_05/TestDiagnostic.txt", null, null, true);
        // intComputer("./src/day_05/TestExamples.txt", null, null, true);
    }
}
