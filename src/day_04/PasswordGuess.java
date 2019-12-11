package day_04;

import static java.util.stream.Collectors.toList;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Predicate;
import java.util.stream.IntStream;

public class PasswordGuess
{

    public static void countPasswords(int start, int stop)
    {
        int count = 0;
        for (int n = start; n <= stop; n++)
        {
            if (checkPassword(IntegerToArray(n)))
            {
                count++;
            }
        }
        System.out.println("Number of all passwords with the given conditions are: " + count);

    }

    public static boolean checkPassword(int[] password)
    {
        boolean DoubleNum = false;
        Map<Integer, Boolean> foundDoubles = new HashMap<Integer, Boolean>();
        for (int i = 0; i < password.length - 1; ++i)
        {
            if (password[i] > password[i + 1])
            {
                return false;
            }
            if (password[i] == password[i + 1])
            {
                foundDoubles.merge(password[i], true, (a, b) -> false);

            }
        }
        return foundDoubles.containsValue(true);
    }

    public static int[] IntegerToArray(int n)
    {
        int[] result = new int[6];
        result[0] = n / 100000;
        result[1] = n / 10000 - n / 100000 * 10;
        result[2] = n / 1000 - result[1] * 10 - result[0] * 100;
        result[3] = n / 100 - result[2] * 10 - result[1] * 100 - result[0] * 1000;
        result[4] = n / 10 - result[3] * 10 - result[2] * 100 - result[1] * 1000 - result[0] * 10000;
        result[5] = n - result[4] * 10 - result[3] * 100 - result[2] * 1000 - result[1] * 10000 - result[0] * 100000;

        return result;

    }

    public static void main(String[] args)
    {
        // System.out.println(123456 / 10000 - 123456 / 100000 * 10);
        // System.out.println(Arrays.toString(IntegerToArray(123456)));
        countPasswords(171309, 643603);
        System.out.println(checkPassword(new int[]
        { 1, 1, 2, 3, 3, 3 }));
        System.out.println(checkPassword(new int[]
        { 1, 2, 3, 4, 4, 4 }));
        System.out.println(checkPassword(new int[]
        { 1, 1, 1, 2, 4, 4 }));
        System.out.println(checkPassword(new int[]
        { 1, 1, 1, 2, 4, 3 }));
        System.out.println(checkPassword(new int[]
        { 2, 2, 3, 4, 5, 0 }));
        System.out.println(checkPassword(new int[]
        { 1, 1, 1, 1, 1, 1 }));
        System.out.println(checkPassword(new int[]
        { 1, 1, 1, 1, 3, 3 }));

        List<String> inputs = IntStream.rangeClosed(171309, 643603).mapToObj(String::valueOf).collect(toList());

        System.err.println(inputs.stream().filter(partA).count());
        System.err.println(inputs.stream().filter(partB).count());
    }

    private static Predicate<String> partA = code -> meetCriteria(code, false);

    private static Predicate<String> partB = code -> meetCriteria(code, true);

    private static boolean meetCriteria(String code, boolean onlyTwoSameNumbersInRow)
    {
        Map<Integer, Boolean> sameCharacters = new HashMap<>();
        int[] split = code.chars().toArray();
        for (int i = 0; i < split.length - 1; ++i)
        {
            if (split[i] > split[i + 1])
            {
                return false;
            }
            if (split[i] == split[i + 1])
            {
                sameCharacters.merge(split[i], true, (a, b) -> !onlyTwoSameNumbersInRow);
            }
        }
        return sameCharacters.containsValue(true);
    }
}
