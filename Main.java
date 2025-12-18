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
        if(month<0 || month>MONTHS || day<1 || day>DAYS){//Checking the values first
            return -99999;
        }
        int totalProfit=0;//it should be 0 at first so that we can add all to profits of the particular day one by one in the for loop.
        for (int i = 0; i <COMMS; i++) {
            totalProfit+=data[month][i][day-1];//which means ı am adding the values to totalProfit one by one e.g. data[0][0][0]=1234 so first ı add this and now
        }                                      //it's time for the second data(data[0][1][0]=4312) so it goes...
        return totalProfit;//and for the last we need to return the totalProfit obviously
    }

    public static int commodityProfitInRange(String commodity, int from, int to) {
        int commIndex=-1;
        for (int i = 0; i < COMMS; i++) {
            if(commodities[i].equals(commodity)){
                commIndex=i;
                break;
            }
        }
        if(commIndex==-1 || from<0 || to>28){
            return -99999;
        }
        int totalProfit=0;
        for (int i = from-1; i <to-1 ; i++) {
            totalProfit+=data[0][commIndex][i];
        }
        return totalProfit;
    }

    public static int bestDayOfMonth(int month) {
        if(month<0 || month>=MONTHS){
            return -1;
        }

        int bestDay=0;//so we don't know anything about the best day so ı first initialize it to a number ı decided to initialize it to 0.
        for (int i = 0; i <COMMS ; i++) {//so we have a nested for loop here the reason why we use this nested for loop is that we need to go through all
            for (int j = 0; j < DAYS; j++) {//the commodities and the days and then decide which day is the bestDay
                if(data[month][i][j]>bestDay){//and in here at first i and j is 0 so which means if(data[month][0][0]>bestDay) we need to initialize the data
                    bestDay=data[month][i][j];//to bestDay and for now the bestDay is data[month][0][0] but if the second data is bigger than the first data we need to
                }//initialize the second data to bestDay so it goes like this...
            }
        }
        return bestDay;
    }
    
    public static String bestMonthForCommodity(String comm) {
        int commIndex=-1;//in the 133. line,ı need to add the profits of all the months and decide which one is the best month so ı need an index for it.
        for (int i = 0; i < COMMS; i++) {//in this for loop,ı first check if the user entered a correct value or not.
            if(commodities[i].equals(comm)){
                commIndex=i;
                break;
            }
        }
        if(commIndex==-1){
            return "INVALID_COMMODITY";
        }
        int maxProfit=0;//and here ı first created an integer called maxProfit,with this integer ı can decide which value is the max in a basic if condition.
        int IndexofTheBestMonth=-1;//and also like ı said in the 118. line,ı need an index to use the data array.
        for (int i = 0; i <MONTHS ; i++) {//here starts a nested for loop because ı want to get all of the
            int totalProfitofTheMonth=0;
            for (int j = 0; j < DAYS; j++) {
                totalProfitofTheMonth+=data[i][commIndex][j];
            }
            if(totalProfitofTheMonth>maxProfit){
                maxProfit=totalProfitofTheMonth;
                IndexofTheBestMonth=i;
            }
        }
        return months[IndexofTheBestMonth];
    }

    public static int consecutiveLossDays(String comm) {
        int commIndex=-1;
        for (int i = 0; i < COMMS; i++) {
            if(commodities[i].equals(comm)){
                commIndex=i;
                break;
            }
        }
        if(commIndex==-1){
            return -1;
        }
        int currentStreak=0,longestStreak=0;
        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                if(data[i][commIndex][j]<0){
                    currentStreak++;
                }else{
                    if(currentStreak>longestStreak){
                        longestStreak=currentStreak;
                    }
                    currentStreak=0;
                }
            }
        }
        return longestStreak;
    }
    
    public static int daysAboveThreshold(String comm, int threshold) {
        int commIndex=-1;
        for (int i = 0; i < COMMS; i++) {
            if(commodities[i].equals(comm)){
                commIndex=i;
                break;
            }
        }
        if(commIndex==-1){
            return -1;
        }

        int numOfDays=0;
        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                if(data[i][commIndex][j]>threshold){
                    numOfDays++;
                }
            }
        }
        return numOfDays;
    }

    public static int biggestDailySwing(int month) {
        if(month<0 || month>=MONTHS){
            return -99999;
        }
        int previousDay=0,diffDay=0,currentDay;
        for (int i = 0; i <DAYS ; i++) {
            currentDay=0;
            for (int j = 0; j < COMMS; j++) {
                currentDay+=data[month][j][i];
            }
            if(previousDay-currentDay<diffDay){
                diffDay=previousDay-currentDay;
            }
            previousDay=currentDay;
        }

        return diffDay;
    }
    
    public static String compareTwoCommodities(String c1, String c2) {
        int c1Index=-1,c2Index=-1;
        for (int i = 0; i < COMMS; i++) {
            if(commodities[i].equals(c1)){
                c1Index=i;
            }else if(commodities[i].equals(c2)){
                c2Index=i;
            }
        }
        if(c1Index==-1 || c2Index==-1){
            return "INVALID_COMMODITY";
        }
        int c1Profit=0,c2Profit=0;
        for (int i = 0; i <MONTHS ; i++) {
            for (int j = 0; j < DAYS; j++) {
                c1Profit+=data[i][c1Index][j];
                c2Profit+=data[i][c2Index][j];
            }
        }
        if(c1Profit>c2Profit){
            return commodities[c1Index]+" is better by"+(c1Profit-c2Index);
        }else if(c1Profit<c2Profit){
            return commodities[c2Index]+" is better by"+(c2Profit-c1Index);
        }else{
            return "Equal";
        }
    }
    
    public static String bestWeekOfMonth(int month) { 
        return "DUMMY"; 
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");

    }
}