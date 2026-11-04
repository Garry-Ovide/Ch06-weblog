/**
 * Read web server data and analyse hourly access patterns.
 * 
 * @author David J. Barnes and Michael Kölling.
 * @version    2016.02.29
 */
public class LogAnalyzer
{
    // Where to calculate the hourly access counts.
    private int[] hourCounts;
    // Use a LogfileReader to access the data.
    private LogfileReader reader;

    /**
     * Create an object to analyze hourly web accesses.
     */
    public LogAnalyzer(String filename)
    { 
        // Create the array object to hold the hourly
        // access counts.
        hourCounts = new int[24];
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

}
