/**
 * 
 */
package day_01;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.List;

/**
 * @author Admin
 *
 */
public class FuelCalculator
{
    /**
     * Returns a Double based on the formula used in
     * {@linkplain #calculate_module(double)} for each Number in the specified
     * input file.
     * 
     * @param path
     *            the Path of the file the input is to be read from. The input
     *            has to be one Number per row, separated by a newline.
     * @return the double value sum of all numbers from the input file
     *         calculated with {@linkplain #calculate_module(double)}
     * 
     */
    public static void calculate_fuel(String path)
    {
        double result = 0;
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            for (String line : allLines)
            {

                result += calculate_module(Double.parseDouble(line.trim()));

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println(String.format("The required fuel is calculated to be %.0f units", result));
    }

    /**
     * Returns a Double based on the formula used in
     * {@linkplain #calculate_module(double)} for each Number in the specified
     * input file.
     * 
     * @param path
     *            the Path of the file the input is to be read from. The input
     *            has to be one Number per row, separated by a newline.
     * @return the double value sum of all numbers from the input file
     *         calculated with {@linkplain #calculate_module(double)}
     * 
     */
    public static void calculate_fuel_all(String path)
    {
        double result = 0;
        try
        {
            List<String> allLines = Files.readAllLines(Paths.get(path));
            for (String line : allLines)
            {
                double fuel_temp = calculate_module(Double.parseDouble(line.trim()));
                result += fuel_temp;
                while (calculate_module(fuel_temp) > 0)
                {
                    fuel_temp = calculate_module(fuel_temp);
                    result += fuel_temp;
                }

            }
        }
        catch (IOException e)
        {
            e.printStackTrace();
        }

        System.out.println(String.format("The required fuel including it's own weight is calculated to be %.0f units", result));
    }

    /**
     * Calculates a the required fuel of a given module with the Formula Input x
     * -> y : Math.floor(x / 3) - 2
     * 
     * @param weight
     *            the weight of the given module to which the required fuel
     *            needs to be calculated
     * @return the calculated required amount of fuel for the specified module
     * 
     **/
    public static double calculate_module(double weight)
    {
        return Math.floor((weight / 3.0)) - 2;
    }

    /**
     * @param args
     */
    public static void main(String[] args)
    {
        // TODO Auto-generated method stub
        calculate_fuel("./src/day_01/FuelInput.txt");
        calculate_fuel_all("./src/day_01/FuelInput.txt");

    }

}
