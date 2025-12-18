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
    static int[][][] data=new int[MONTHS][COMMS][DAYS];

    // ======== REQUIRED METHOD LOAD DATA (Students fill this) ========
    public static void loadData() {
        Scanner sc=null;
        for (int mIndex = 0; mIndex <MONTHS;mIndex++) {
            String fName = "Data_Files/"+months[mIndex]+".txt"; //Path to the file
            try {
                sc=new Scanner(Paths.get(fName)); //Ready for getting input from file

                while(sc.hasNextLine()){ //As long as there is a new line continue scanning
                    String line = sc.nextLine(); //read a line from file

                    //check to skip first(title) line and empty lines
                    if(line.substring(0,3).equals("Day") || line.trim().equals("")){ //if(line.startsWith("Day") || line.trim().isEmpty()){
                        continue;
                    }
                String[] info=line.split(","); //splitting info into string array, ex: "1,Gold,2312" -> "1","Gold","2312"
                int day=Integer.parseInt(info[0].trim());
                int commodity=0;
                for (int i=0;i<COMMS;i++){
                    if(commodities[i].equals(info[1].trim())){
                        commodity=i;
                        break;
                    }
                }

                int profit=Integer.parseInt(info[2].trim());
                data[mIndex][commodity][day-1]=profit; //ex: data[0][0][0]=2312
                 //System.out.println("Profit:"+data[mIndex][commodity][day-1]);

                }
            } catch (FileNotFoundException e) {
                System.err.println("The file that was search could not found!");
            } catch (IOException e) {
                System.err.println("The file that was search could not be readed!");
            }finally{
                sc.close();
            }

        }

    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if(month<0 || month>=MONTHS){
            return  "INVALID_MONTH";
        }
        int[] profits={0,0,0,0,0}; //Array to sum of each commodities' profit!
        for (int i = 0; i <COMMS; i++) {
            for(int j=0;j<DAYS;j++){
                profits[i]+=data[month][i][j];
            }
        }

        int mostProfitableIndex=0;
        int mostProfit=0;
        for (int i = 0; i < COMMS; i++) {
            if(profits[i]>mostProfit){
                mostProfit=profits[i];
                mostProfitableIndex=i;
            }
        }
        return commodities[mostProfitableIndex]+ " "+mostProfit;
    }

    public static int totalProfitOnDay(int month, int day) {
        if(month<0 || month>MONTHS || day<0 || day>DAYS){
            return -99999;
        }
        int totalProfit=0;
        for (int i = 0; i <COMMS; i++) {
            totalProfit+=data[month][i][day-1];
        }
        return totalProfit;
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
       /* int totalProfitInRange=0;
        for (int i = 0; i <COMMS; i++) {
            if(commodity.equals(commodities[i]) && from<=1 && to<=28){
                for (int j=from;j<to;j++) {
                    totalProfitInRange+=data[][][j]

                }
            }
        }*/
        return 1234;
    }

    public static int bestDayOfMonth(int month) {
        int bestDay=0;
        for (int i = 0; i <COMMS ; i++) {
            for (int j = 0; j < DAYS; j++) {
                if(data[month][i][j]>bestDay){
                    bestDay=data[month][i][j];
                }
            }
        }
        return bestDay;
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