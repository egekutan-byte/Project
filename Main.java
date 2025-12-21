// Main.java — Students version
import javax.swing.plaf.basic.BasicSplitPaneUI;
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


    /* Github Link:https://github.com/egekutan-byte/Project */

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
                for (int i=0;i<COMMS;i++){//Time to check the commodities if they are whether equal to the info[1] or not
                    if(commodities[i].equals(info[1].trim())){
                        commodity=i;
                        break;
                    }
                }

                int profit=Integer.parseInt(info[2].trim());
                data[mIndex][commodity][day-1]=profit; //ex: data[0][0][0]=2312


                }
            } catch (FileNotFoundException e) {

            } catch (IOException e) {

            }finally{
                if(sc!=null){//If the file never opens,the sc value will be null and we get an nullPointerException error thats why we need to check it first.
                    sc.close();
                }

            }

        }

    }

    // ======== 10 REQUIRED METHODS (Students fill these) ========

    public static String mostProfitableCommodityInMonth(int month) {
        if(month<0 || month>=MONTHS){
            return  "INVALID_MONTH";
        }
        int[] profits={0,0,0,0,0}; //Array to sum of each commodities' profit!
        for (int i = 0; i <COMMS; i++) {//In this for loops,we add the profits of each day of each commodity seperately
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
        if(month<0 || month>=MONTHS || day<1 || day>DAYS){//Checking the values first
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
        if(commIndex==-1 || from<1 || to>DAYS || from>to ){
            return -99999;
        }
        int totalProfit=0;
        for (int j = 0; j < MONTHS; j++) {
            for (int i = from-1; i <=to-1 ; i++) {
                totalProfit+=data[j][commIndex][i];
            }

        }
        return totalProfit;
    }

    public static int bestDayOfMonth(int month) {
        if(month<0 || month>=MONTHS){
            return -1;
        }
        int maxValue=0;
        int bestDay=0;//so we don't know anything about the best day so ı first initialize it to a number ı decided to initialize it to 0.
        for (int i = 0; i <DAYS ; i++) {//so we have a nested for loop here the reason why we use this nested for loop is that we need to go through all
            int totalProfit=0;
            for (int j = 0; j <COMMS; j++) {
                totalProfit+=data[month][j][i];
                if(totalProfit>maxValue){
                    maxValue=totalProfit;
                    bestDay=i+1;//In the description,it was said that the days should be like this:Day=1-28 so i added 1 to "i" because the "i" itself is actually
                }//               the index of the days.
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
        for (int i = 0; i <MONTHS ; i++) {
            int totalProfitofTheMonth=0;
            for (int j = 0; j < DAYS; j++) {
                totalProfitofTheMonth+=data[i][commIndex][j];
            }
            if(totalProfitofTheMonth>maxProfit){//It is a basic control of the maximum profit every time the value of i and j increases the if block will continue
                maxProfit=totalProfitofTheMonth;//to be checked all over again and updates the maxProfit value if a bigger number occures.
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
        int currentStreak=0,longestStreak=0;//At first,there are no streaks thats why its 0.
        for (int i = 0; i < MONTHS; i++) {
            for (int j = 0; j < DAYS; j++) {
                if(data[i][commIndex][j]<0){//But after this if block,we check that if whether there is a negative value or not.If there is,
                    currentStreak++;//the currentStreak value will increase by 1 and updates to 1 so it goes like this.
                }else{//And also we want to find the longest streak so in order to that we add an if block inside of the else block just like i did
                    if(currentStreak>longestStreak){//in the bestMonthForCommodity method.
                        longestStreak=currentStreak;
                    }
                    currentStreak=0;//And if the if statement is false at some point,the currentStreak needs to be zero because literally if the value is greater than
                }//0 the current streak will end and it will be 0 again.
            }
        }
        return longestStreak;
    }
    
    public static int daysAboveThreshold(String comm, int threshold) {/* daysAboveThreshold(String commodity, int threshold)
                                                                            → Return: number of days where profit > threshold
                                                                            → Invalid commodity: return -1 */
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
            if(Math.abs(previousDay-currentDay)>diffDay){
                diffDay=Math.abs(previousDay-currentDay);
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
            return commodities[c1Index]+" is better by "+(c1Profit-c2Profit);
        }else if(c1Profit<c2Profit){
            return commodities[c2Index]+" is better by "+(c2Profit-c1Profit);
        }else{
            return "Equal ";
        }
    }
    
    public static String bestWeekOfMonth(int month) {
        if(month<0 || month>=MONTHS){
            return "INVALID_MONTH";
        }
        int currentProfit =0,bestWeek=1,bestProfit=0,currentWeek=1;


            for (int j = 0; j <DAYS; j=j+7) {
                for (int k = j; k <=j+6; k++) {
                    for (int l = 0; l < COMMS; l++) {
                        currentProfit +=data[month][l][k];
                    }
                }

                if(currentProfit>bestProfit){
                    bestProfit=currentProfit;
                    bestWeek=currentWeek;
                }
                currentProfit=0;
                currentWeek++;
            }

        return "Week "+bestWeek;
    }

    public static void main(String[] args) {
        loadData();
        System.out.println("Data loaded – ready for queries");
    }
}