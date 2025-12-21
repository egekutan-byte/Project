import java.lang.reflect.Field;
import java.util.Random;

public class AutoGrader1 {

    // Spec constants
    static final int MONTHS = 12;
    static final int DAYS = 28;
    static final int COMMS = 5;

    // Pulled from Main
    static int[][][] data;
    static String[] commodities;
    static String[] months;

    // Counters
    static int pass = 0;
    static int fail = 0;
    static int testNo = 1;

    public static void main(String[] args) {

        // 1) Run loadData (must never crash)
        try {
            Main.loadData();
        } catch (Throwable t) {
            System.out.println("[CRASH] Main.loadData() threw: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace();
            return;
        }

        // 2) Access fields from Main
        try {
            data = Main.data;
            commodities = getStringArrayField(Main.class, "commodities");
            months = getStringArrayField(Main.class, "months");
        } catch (Throwable t) {
            System.out.println("[CRASH] Could not access Main fields: " + t.getClass().getSimpleName() + " - " + t.getMessage());
            t.printStackTrace();
            return;
        }

        System.out.println("Running 100 tests (verbose) ...\n");

        // ----------------------------
        // A) FIXED EDGE/INVALID TESTS
        // ----------------------------

        // 1 mostProfitableCommodityInMonth invalid
        expectStr("mostProfitable invalid month -1", "INVALID_MONTH", safeMostProfitable(-1));
        expectStr("mostProfitable invalid month 12", "INVALID_MONTH", safeMostProfitable(12));

        // 2 totalProfitOnDay invalid combos
        expectInt("totalProfitOnDay invalid month -1", -99999, safeTotalProfitOnDay(-1, 1));
        expectInt("totalProfitOnDay invalid month 12", -99999, safeTotalProfitOnDay(12, 1));
        expectInt("totalProfitOnDay invalid day 0", -99999, safeTotalProfitOnDay(0, 0));
        expectInt("totalProfitOnDay invalid day 29", -99999, safeTotalProfitOnDay(0, 29));

        // 3 commodityProfitInRange invalid
        expectInt("commodityProfitInRange invalid commodity (case-sensitive)", -99999, safeCommodityProfitInRange("GOLD", 1, 7));
        expectInt("commodityProfitInRange invalid fromDay 0", -99999, safeCommodityProfitInRange("Gold", 0, 7));
        expectInt("commodityProfitInRange invalid toDay 29", -99999, safeCommodityProfitInRange("Gold", 1, 29));
        expectInt("commodityProfitInRange invalid from>to", -99999, safeCommodityProfitInRange("Gold", 10, 2));

        // 4 bestDayOfMonth invalid
        expectInt("bestDayOfMonth invalid month -1", -1, safeBestDayOfMonth(-1));
        expectInt("bestDayOfMonth invalid month 12", -1, safeBestDayOfMonth(12));

        // 5 bestMonthForCommodity invalid
        expectStr("bestMonthForCommodity invalid commodity", "INVALID_COMMODITY", safeBestMonthForCommodity("GOLD"));

        // 6 consecutiveLossDays invalid
        expectInt("consecutiveLossDays invalid commodity", -1, safeConsecutiveLossDays("GOLD"));

        // 7 daysAboveThreshold invalid
        expectInt("daysAboveThreshold invalid commodity", -1, safeDaysAboveThreshold("GOLD", 1000));

        // 8 biggestDailySwing invalid
        expectInt("biggestDailySwing invalid month -1", -99999, safeBiggestDailySwing(-1));
        expectInt("biggestDailySwing invalid month 12", -99999, safeBiggestDailySwing(12));

        // 9 compareTwoCommodities invalid
        expectStr("compareTwoCommodities invalid commodity #1", "INVALID_COMMODITY", safeCompareTwo("GOLD", "Gold"));
        expectStr("compareTwoCommodities invalid commodity #2", "INVALID_COMMODITY", safeCompareTwo("Gold", "OIL"));

        // 10 bestWeekOfMonth invalid
        expectStr("bestWeekOfMonth invalid month -1", "INVALID_MONTH", safeBestWeekOfMonth(-1));
        expectStr("bestWeekOfMonth invalid month 12", "INVALID_MONTH", safeBestWeekOfMonth(12));

        // A few fixed VALID sanity checks (oracle-driven)
        expectStr("mostProfitable m=0 (oracle)", oracleMostProfitable(0), safeMostProfitable(0));
        expectInt("totalProfitOnDay m=0 d=1 (oracle)", oracleTotalProfitOnDay(0, 1), safeTotalProfitOnDay(0, 1));
        expectInt("commodityProfitInRange Gold 1-7 (oracle)", oracleCommodityProfitInRange("Gold", 1, 7), safeCommodityProfitInRange("Gold", 1, 7));
        expectInt("bestDayOfMonth m=0 (oracle)", oracleBestDayOfMonth(0), safeBestDayOfMonth(0));
        expectStr("bestMonthForCommodity Gold (oracle)", oracleBestMonthForCommodity("Gold"), safeBestMonthForCommodity("Gold"));
        expectInt("consecutiveLossDays Gold (oracle)", oracleConsecutiveLossDays("Gold"), safeConsecutiveLossDays("Gold"));
        expectInt("daysAboveThreshold Gold thr=0 (oracle)", oracleDaysAboveThreshold("Gold", 0), safeDaysAboveThreshold("Gold", 0));
        expectInt("biggestDailySwing m=0 (oracle)", oracleBiggestDailySwing(0), safeBiggestDailySwing(0));
        expectStr("compareTwoCommodities Gold vs Oil (oracle)", oracleCompareTwo("Gold", "Oil"), safeCompareTwo("Gold", "Oil"));
        expectStr("bestWeekOfMonth m=0 (oracle)", oracleBestWeekOfMonth(0), safeBestWeekOfMonth(0));

        // Count how many tests we already ran:
        int ranSoFar = (testNo - 1);

        // ----------------------------
        // B) RANDOM TESTS (seeded)
        // ----------------------------
        // Fill until we reach 100 total tests
        Random rnd = new Random(1152025);

        while ((testNo - 1) < 100) {
            int methodPick = 1 + rnd.nextInt(10); // 1..10
            switch (methodPick) {
                case 1: { // mostProfitableCommodityInMonth
                    int m = rnd.nextInt(12);
                    String exp = oracleMostProfitable(m);
                    expectStr("mostProfitable m=" + m, exp, safeMostProfitable(m));
                    break;
                }
                case 2: { // totalProfitOnDay
                    int m = rnd.nextInt(12);
                    int d = 1 + rnd.nextInt(28);
                    int exp = oracleTotalProfitOnDay(m, d);
                    expectInt("totalProfitOnDay m=" + m + " d=" + d, exp, safeTotalProfitOnDay(m, d));
                    break;
                }
                case 3: { // commodityProfitInRange
                    String c = commodities[rnd.nextInt(commodities.length)];
                    int a = 1 + rnd.nextInt(28);
                    int b = 1 + rnd.nextInt(28);
                    int from = Math.min(a, b);
                    int to = Math.max(a, b);
                    int exp = oracleCommodityProfitInRange(c, from, to);
                    expectInt("commodityProfitInRange c=" + c + " " + from + "-" + to, exp, safeCommodityProfitInRange(c, from, to));
                    break;
                }
                case 4: { // bestDayOfMonth
                    int m = rnd.nextInt(12);
                    int exp = oracleBestDayOfMonth(m);
                    expectInt("bestDayOfMonth m=" + m, exp, safeBestDayOfMonth(m));
                    break;
                }
                case 5: { // bestMonthForCommodity
                    String c = commodities[rnd.nextInt(commodities.length)];
                    String exp = oracleBestMonthForCommodity(c);
                    expectStr("bestMonthForCommodity c=" + c, exp, safeBestMonthForCommodity(c));
                    break;
                }
                case 6: { // consecutiveLossDays
                    String c = commodities[rnd.nextInt(commodities.length)];
                    int exp = oracleConsecutiveLossDays(c);
                    expectInt("consecutiveLossDays c=" + c, exp, safeConsecutiveLossDays(c));
                    break;
                }
                case 7: { // daysAboveThreshold
                    String c = commodities[rnd.nextInt(commodities.length)];
                    int thr = rnd.nextInt(10001) - 5000; // -5000..5000
                    int exp = oracleDaysAboveThreshold(c, thr);
                    expectInt("daysAboveThreshold c=" + c + " thr=" + thr, exp, safeDaysAboveThreshold(c, thr));
                    break;
                }
                case 8: { // biggestDailySwing
                    int m = rnd.nextInt(12);
                    int exp = oracleBiggestDailySwing(m);
                    expectInt("biggestDailySwing m=" + m, exp, safeBiggestDailySwing(m));
                    break;
                }
                case 9: { // compareTwoCommodities (allow equal)
                    String c1 = commodities[rnd.nextInt(commodities.length)];
                    String c2 = commodities[rnd.nextInt(commodities.length)];
                    String exp = oracleCompareTwo(c1, c2);
                    expectStr("compareTwoCommodities " + c1 + " vs " + c2, exp, safeCompareTwo(c1, c2));
                    break;
                }
                case 10: { // bestWeekOfMonth
                    int m = rnd.nextInt(12);
                    String exp = oracleBestWeekOfMonth(m);
                    expectStr("bestWeekOfMonth m=" + m, exp, safeBestWeekOfMonth(m));
                    break;
                }
            }
        }

        // Summary
        System.out.println("\nRESULT: PASS=" + pass + " FAIL=" + fail);
        if (fail == 0) System.out.println("All good. Don’t touch return formats.");
    }

    // -----------------------------
    // SAFE CALLS (never crash grader)
    // -----------------------------

    static String safeMostProfitable(int month) {
        try { return Main.mostProfitableCommodityInMonth(month); }
        catch (Throwable t) { return crashStr("mostProfitableCommodityInMonth", t); }
    }
    static int safeTotalProfitOnDay(int month, int day) {
        try { return Main.totalProfitOnDay(month, day); }
        catch (Throwable t) { crash("totalProfitOnDay", t); return Integer.MIN_VALUE; }
    }
    static int safeCommodityProfitInRange(String c, int from, int to) {
        try { return Main.commodityProfitInRange(c, from, to); }
        catch (Throwable t) { crash("commodityProfitInRange", t); return Integer.MIN_VALUE; }
    }
    static int safeBestDayOfMonth(int month) {
        try { return Main.bestDayOfMonth(month); }
        catch (Throwable t) { crash("bestDayOfMonth", t); return Integer.MIN_VALUE; }
    }
    static String safeBestMonthForCommodity(String c) {
        try { return Main.bestMonthForCommodity(c); }
        catch (Throwable t) { return crashStr("bestMonthForCommodity", t); }
    }
    static int safeConsecutiveLossDays(String c) {
        try { return Main.consecutiveLossDays(c); }
        catch (Throwable t) { crash("consecutiveLossDays", t); return Integer.MIN_VALUE; }
    }
    static int safeDaysAboveThreshold(String c, int thr) {
        try { return Main.daysAboveThreshold(c, thr); }
        catch (Throwable t) { crash("daysAboveThreshold", t); return Integer.MIN_VALUE; }
    }
    static int safeBiggestDailySwing(int month) {
        try { return Main.biggestDailySwing(month); }
        catch (Throwable t) { crash("biggestDailySwing", t); return Integer.MIN_VALUE; }
    }
    static String safeCompareTwo(String c1, String c2) {
        try { return Main.compareTwoCommodities(c1, c2); }
        catch (Throwable t) { return crashStr("compareTwoCommodities", t); }
    }
    static String safeBestWeekOfMonth(int month) {
        try { return Main.bestWeekOfMonth(month); }
        catch (Throwable t) { return crashStr("bestWeekOfMonth", t); }
    }

    // -----------------------------
    // EXPECT / PRINT (verbose)
    // -----------------------------

    static void expectInt(String name, int expected, int got) {
        if (expected == got) {
            pass++;
            System.out.println("[PASS #" + (testNo++) + "] " + name + " -> " + got);
        } else {
            fail++;
            System.out.println("[FAIL #" + (testNo++) + "] " + name + " | Expected: " + expected + " | Got: " + got);
        }
    }

    static void expectStr(String name, String expected, String got) {
        if ((expected == null && got == null) || (expected != null && expected.equals(got))) {
            pass++;
            System.out.println("[PASS #" + (testNo++) + "] " + name + " -> \"" + got + "\"");
        } else {
            fail++;
            System.out.println("[FAIL #" + (testNo++) + "] " + name + " | Expected: \"" + expected + "\" | Got: \"" + got + "\"");
        }
    }

    static void crash(String where, Throwable t) {
        fail++;
        System.out.println("[FAIL #" + (testNo++) + "] " + where + " -> CRASH: " + t.getClass().getSimpleName() + " : " + t.getMessage());
    }

    static String crashStr(String where, Throwable t) {
        crash(where, t);
        return "__CRASH__";
    }

    // -----------------------------
    // ORACLE (correct answers)
    // -----------------------------

    static int commIndexExact(String commodity) {
        for (int i = 0; i < commodities.length; i++) {
            if (commodities[i].equals(commodity)) return i; // exact match per spec
        }
        return -1;
    }

    static String oracleMostProfitable(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        long max = Long.MIN_VALUE;
        int best = -1;
        for (int c = 0; c < COMMS; c++) {
            long sum = 0;
            for (int d = 0; d < DAYS; d++) sum += data[month][d][c];
            if (sum > max) { max = sum; best = c; }
        }
        return commodities[best] + " " + max;
    }

    static int oracleTotalProfitOnDay(int month, int day) {
        if (month < 0 || month >= MONTHS || day < 1 || day > DAYS) return -99999;
        int di = day - 1;
        long sum = 0;
        for (int c = 0; c < COMMS; c++) sum += data[month][di][c];
        return (int) sum;
    }

    static int oracleCommodityProfitInRange(String commodity, int fromDay, int toDay) {
        int ci = commIndexExact(commodity);
        if (ci == -1 || fromDay < 1 || toDay > DAYS || fromDay > toDay) return -99999;
        long sum = 0;
        int f = fromDay - 1;
        int t = toDay - 1;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = f; d <= t; d++) sum += data[m][d][ci];
        }
        return (int) sum;
    }

    static int oracleBestDayOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return -1;
        long best = Long.MIN_VALUE;
        int bestDay = -1;
        for (int d = 0; d < DAYS; d++) {
            long sum = 0;
            for (int c = 0; c < COMMS; c++) sum += data[month][d][c];
            if (sum > best) { best = sum; bestDay = d + 1; }
        }
        return bestDay;
    }

    static String oracleBestMonthForCommodity(String commodity) {
        int ci = commIndexExact(commodity);
        if (ci == -1) return "INVALID_COMMODITY";
        long best = Long.MIN_VALUE;
        int bestMonth = -1;
        for (int m = 0; m < MONTHS; m++) {
            long sum = 0;
            for (int d = 0; d < DAYS; d++) sum += data[m][d][ci];
            if (sum > best) { best = sum; bestMonth = m; }
        }
        return months[bestMonth];
    }

    static int oracleConsecutiveLossDays(String commodity) {
        int ci = commIndexExact(commodity);
        if (ci == -1) return -1;
        int maxStreak = 0;
        int streak = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (data[m][d][ci] < 0) {
                    streak++;
                    if (streak > maxStreak) maxStreak = streak;
                } else {
                    streak = 0;
                }
            }
        }
        return maxStreak;
    }

    static int oracleDaysAboveThreshold(String commodity, int threshold) {
        int ci = commIndexExact(commodity);
        if (ci == -1) return -1;
        int count = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                if (data[m][d][ci] > threshold) count++;
            }
        }
        return count;
    }

    static int oracleBiggestDailySwing(int month) {
        if (month < 0 || month >= MONTHS) return -99999;
        long prev = 0;
        for (int c = 0; c < COMMS; c++) prev += data[month][0][c];
        long maxSwing = 0;
        for (int d = 1; d < DAYS; d++) {
            long cur = 0;
            for (int c = 0; c < COMMS; c++) cur += data[month][d][c];
            long swing = cur - prev;
            if (swing < 0) swing = -swing;
            if (swing > maxSwing) maxSwing = swing;
            prev = cur;
        }
        return (int) maxSwing;
    }

    static String oracleCompareTwo(String c1, String c2) {
        int i1 = commIndexExact(c1);
        int i2 = commIndexExact(c2);
        if (i1 == -1 || i2 == -1) return "INVALID_COMMODITY";

        long s1 = 0, s2 = 0;
        for (int m = 0; m < MONTHS; m++) {
            for (int d = 0; d < DAYS; d++) {
                s1 += data[m][d][i1];
                s2 += data[m][d][i2];
            }
        }
        long diff = s1 - s2;
        if (diff > 0) return c1 + " is better by " + diff;
        if (diff < 0) return c2 + " is better by " + (-diff);
        return "Equal";
    }

    static String oracleBestWeekOfMonth(int month) {
        if (month < 0 || month >= MONTHS) return "INVALID_MONTH";
        long[] w = new long[4];
        for (int d = 0; d < DAYS; d++) {
            long sum = 0;
            for (int c = 0; c < COMMS; c++) sum += data[month][d][c];
            int wi = d / 7; // 0..3
            w[wi] += sum;
        }
        long best = Long.MIN_VALUE;
        int bestIdx = -1;
        for (int i = 0; i < 4; i++) {
            if (w[i] > best) { best = w[i]; bestIdx = i; }
        }
        return "Week " + (bestIdx + 1);
    }

    // -----------------------------
    // Reflection helper
    // -----------------------------
    static String[] getStringArrayField(Class<?> cls, String fieldName) throws Exception {
        Field f = cls.getDeclaredField(fieldName);
        f.setAccessible(true);
        return (String[]) f.get(null);
    }
}
