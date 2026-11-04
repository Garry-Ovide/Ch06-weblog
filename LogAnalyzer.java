/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Store hourly access counts.
    private int[] hourCounts;
    
    // Store daily access counts.
    private int[] dayCounts;
    
    // Store monthly access counts.
    private int[] monthCounts;
    
    /**
     * Store monthly access counts for five years 2015 through 2019.
     *
     */
    private int[][] yearlyMonthCounts;

    // Use a LogfileReader to access the data.
    private LogfileReader reader;
    
    // Store the name of the log file.
    private String filename;

    /**
     * Create an object to analyze hourly web accesses.
     * Create a LogAnalyzer for the specified log file.
     * 
     * @param filename The name of the log file to analyze.
     */
    public LogAnalyzer(String filename)
    { 
        // Store the filename for future analysis.
        this.filename = filename;
        
        // Create the array to hold hourly access counts.
        hourCounts = new int[24];
        
        // Create the array to hold daily access counts
        dayCounts = new int[7];
        
         // Create the array to hold monthly access counts.
        monthCounts = new int[12];
        
        
        // Create the array for five years of monthly access counts.
        yearlyMonthCounts = new int[5][12];

        
        // Create the reader to obtain the data.
        reader = new LogfileReader(filename);
    }

    /**
     * Analyze the hourly access data from the log file.
     */
    public void analyzeHourlyData()
    {
        while(reader.hasNext()) {
            LogEntry entry = reader.next();
            int hour = entry.getHour();
            hourCounts[hour]++;
        }
    }

    /**
     * Print the hourly counts.
     * These should have been set with a prior
     * call to analyzeHourlyData.
     */
    public void printHourlyCounts()
    {
        System.out.println("Hr: Count");
        for(int hour = 0; hour < hourCounts.length; hour++) {
            System.out.println(hour + ": " + hourCounts[hour]);
        }
    }
    
    /**
     * Print the lines of data read by the LogfileReader
     */
    public void printData()
    {
        reader.printData();
    }
    
    /**
     * Return the total number of accesses recorded in the log file
     * 
     *  @return The total number of accesses.
     */
    public int numberOfAccess()
    {
        int total = 0;
        
        for (int count: hourCounts) {
            total += count;
        }
        
        return total;
    }
    
    
    /**
     * Find the hour with the highest number of accesses.
     *
     * @return The busiest hour, between 0 and 23.
     */
    public int busiestHour()
    {
        int busiest = 0;
        
        for(int hour = 1; hour < hourCounts.length; hour++) {
            if(hourCounts[hour] > hourCounts[busiest]) {
                busiest = hour;
            }
        }

        return busiest;
    }
    
    
    /**
     * Find the hour with the fewest number of accesses.
     *
     * @return The quietest hour, between 0 and 23.
     */
    public int quietestHour()
    {
        int quietest = 0;
        
        for(int hour = 1; hour < hourCounts.length; hour++) {
            if(hourCounts[hour] < hourCounts[quietest]) {
                quietest = hour;
            }
        }

        return quietest;
    }

    
    /**
     * Find the busiest consecutive two-hour period.
     * Includes periods that cross midnight.
     *
     * @return The starting hour of the busiest
     *         two-hour period, between 0 and 23.
     */
    public int busiestTwoHour()
    {
        int busiest = 0; 
        int maxCount = hourCounts[0] + hourCounts[1];
        
        for(int hour = 1; hour < hourCounts.length; hour++) {
            int nextHour = (hour + 1) % hourCounts.length;
            int total = hourCounts[hour] + hourCounts[nextHour];
            
            if(total > maxCount) {
                maxCount = total;
                busiest = hour;
            }
        }

        return busiest;
    }
    
    /**
     * Analyze the daily access data from the log file.
     * Count accesses for each day of the week.
     * Sunday is index 0 and Saturday is index 6.
     */
    public void analyzeDailyData()
    {
        dayCounts = new int[7];
        
        LogfileReader dailyReader = new LogfileReader(filename);
        
        while(dailyReader.hasNext()) {
            LogEntry entry = dailyReader.next();
            int day = entry.getDayOfWeek();
            dayCounts[day]++;
        }
    }  
        
    /**
     * Print the number of accesses for each day
     * of the week, from Sunday through Saturday.
     * Run analyzeDailyData() before calling this method.
     */
    public void printDailyCounts()
    {
        String[] days = {"Sunday", "Monday", "Tuesday",
                     "Wednesday", "Thursday", "Friday",
                     "Saturday"};
                     
        System.out.println("Day: Count");

        for(int day = 0; day < dayCounts.length; day++) {
            System.out.println(days[day] + ": " + dayCounts[day]);
        }
    }
    
    /**
     * Find the day of the week with the fewest accesses.
     * Sunday is 0 and Saturday is 6.
     *
     * @return The index of the quietest day,
     *         between 0 and 6.
     */
    public int quietestDay()
    {
        int quietest = 0;
        
        for(int day = 1; day < dayCounts.length; day++) {
            if(dayCounts[day] < dayCounts[quietest]) {
                quietest = day;
            }
        }

        return quietest;
    }
    
    /**
     * Find the day of the week with the most accesses.
     * Sunday is 0 and Saturday is 6.
     *
     * @return The index of the busiest day,
     *         between 0 and 6.
     */
    public int busiestDay()
    {
        int busiest = 0;
        
        for(int day = 1; day < dayCounts.length; day++) {
            if(dayCounts[day] > dayCounts[busiest]) {
                busiest = day;
            }
        }

        return busiest;
    }
    
    /**
     * Analyze the monthly access data from the log file.
     * Count the number of accesses for each month.
     * January is index 0 and December is index 11.
     */
    public void analyzeMonthlyData()
    {
        // Reset monthly counts before analyzing.
        monthCounts = new int[12];
        
        // Create a reader for the log file.
        LogfileReader monthlyReader = new LogfileReader(filename);

        // Read each log entry and count its month.
        while(monthlyReader.hasNext()) {
            LogEntry entry = monthlyReader.next();
            int month = entry.getMonth();

            // Convert month 1-12 to array index 0-11.
            monthCounts[month - 1]++;
        }
    }
    
    /**
     * Return the total number of accesses for a
     * specified month.
     *
     * @param month The month number, from 1 to 12.
     * @return The total accesses for that month,
     *         or -1 if the month is invalid.
     */
    public int totalAccessesPerMonth(int month)
    {
        // Check that the month is valid.
        if(month < 1 || month > 12) {
            return -1;
        }
        
        // Return the access count for the month.
        return monthCounts[month - 1];
    }
    
    /**
     * Print the number of accesses for each month.
     * Run analyzeMonthlyData() before calling this method.
     */
    public void printMonthlyCounts()
    {
        String[] months = {"January", "February", "March",
                       "April", "May", "June",
                       "July", "August", "September",
                       "October", "November", "December"};

        System.out.println("Month: Count");

        for(int month = 0; month < monthCounts.length; month++) {
            System.out.println(months[month] + ": " +
                           monthCounts[month]);
        }
    }
    
    /**
     * Find the month with the fewest website accesses.
     *
     * @return The quietest month, from 1 (January)
     *         through 12 (December).
     */
    public int quietestMonth()
    {
        // Start by assuming January is the quietest.
        int quietest = 0;

        // Compare the remaining months.
        for(int month = 1; month < monthCounts.length; month++) {
            if(monthCounts[month] < monthCounts[quietest]) {
                quietest = month;
            }
        }
        // Convert the array index to a month number.
        return quietest + 1;
    }
    
    /**
     * Find the month with the most website accesses.
     *
     * @return The busiest month, from 1 (January)
     *         through 12 (December).
     */
    public int busiestMonth()
    {
        // Start by assuming January is the busiest.
        int busiest = 0;

        // Compare the remaining months.
        for(int month = 1; month < monthCounts.length; month++) {
            if(monthCounts[month] > monthCounts[busiest]) {
                busiest = month;
            }
        }

        // Convert the array index to a month number.
        return busiest + 1;
    }
    
    /**
     * Calculate the average number of website accesses
     * per calendar month across the analyzed log entries.
     *
     * @return The average number of accesses per month.
     */
    public double averageAccessesPerMonth()
    {
        // Store the total number of accesses.
        int total = 0;

        // Add the access counts for all 12 months.
        for(int month = 0; month < monthCounts.length; month++) {
            total += monthCounts[month];
        }

        // Calculate and return the monthly average.
        return (double) total / monthCounts.length;
    }
    
    /**
     * Analyze monthly website accesses for the years
     * 2015 through 2019.
     * Store the results separately for each year and month.
     */
    public void analyzeYearlyMonthlyData()
    {
        // Reset the five-year monthly counts.
        yearlyMonthCounts = new int[5][12];

        // Create a reader for the log file.
        LogfileReader yearlyReader = new LogfileReader(filename);

        // Read each log entry.
        while(yearlyReader.hasNext()) {
            LogEntry entry = yearlyReader.next();
            
            // Get the year and month of the entry.
            int year = entry.getYear();
            int month = entry.getMonth();
            
            // Count entries from 2015 through 2019.
            if(year >= 2015 && year <= 2019) {
                yearlyMonthCounts[year - 2015][month - 1]++;
            }
        }
    }
    
    /**
     * Print monthly website access counts for each year
     * from 2015 through 2019.
     * Run analyzeYearlyMonthlyData() before this method.
     */
    public void printYearlyMonthlyCounts()
    {
        String[] months = {"January", "February", "March",
                       "April", "May", "June",
                       "July", "August", "September",
                       "October", "November", "December"};

        // Display the monthly counts for each year.
        for(int year = 0; year < yearlyMonthCounts.length; year++) {
            System.out.println("Year: " + (2015 + year));

        for(int month = 0; month < 12; month++) {
            System.out.println(months[month] + ": " +
                               yearlyMonthCounts[year][month]);
            }
             System.out.println();
        }
    }

}
