// Main.java — Students version
import java.io.*;
import java.nio.file.Paths;
import java.util.*;

public class Main {
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;
    static String[] commodities = {"Gold", "Oil", "Silver", "Wheat", "Copper"};
    static String[] months = {"January","February","March","April","May","June",
                              "July","August","September","October","November","December"};
    static int[][][] data=null;

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        Scanner sc=null;
        for (int mIndex = 0; mIndex <MONTHS;mIndex++) {
            String fName = "Data_Files/"+months[mIndex]+".txt";
            try {
                sc=new Scanner(Paths.get(fName));

                while(sc.hasNextLine()){
                    String line = sc.nextLine();

                    if(line.contains("Day")){
                        continue;
                    }
                String[] info=line.split(",");
                int day=Integer.parseInt(info[0].trim());
                int commodity=0;
                for (int i=0;i<COMMS;i++){
                    if(commodities[i].equals(info[1].trim())){
                        commodity=i;
                    }
                }


                }
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        return "DUMMY"; 
    }

    public static int totalProfitOnDay(int month, int day) {
        return 1234;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        return 1234;
    }

    public static int bestDayOfMonth(int month) { 
        return 1234; 
    }
    
    public static String bestMonthForCommodity(String comm) { 
        return "DUMMY"; 
    }

    public static int consecutiveLossDays(String comm) { 
        return 1234; 
    }
    
    public static int daysAboveThreshold(String comm, int threshold) { 
        return 1234; 
    }

    public static int biggestDailySwing(int month) { 
        return 1234; 
    }
    
    public static String compareTwoCommodities(String c1, String c2) { 
        return "DUMMY is better by 1234"; 
    }
    
    public static String bestWeekOfMonth(int month) { 
        return "DUMMY"; 
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}