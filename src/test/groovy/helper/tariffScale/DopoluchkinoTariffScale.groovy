package helper.tariffScale

class DopoluchkinoTariffScale {

    private static final INTEREST_PERCENT = 1
    private static final OVERDUE_PERCENT = 1
    private static final FINE_PERCENT = 0.2

    static final MIN_SUM = 2000
    static final MAX_SUM = 30000
    static final STEP_SUM = 500
    static final MIN_DAY = 7
    static final MAX_DAY = 31

    static Integer getInterestPercent() {
        INTEREST_PERCENT
    }

    static Integer getOverdueInterestPercent() {
        OVERDUE_PERCENT
    }

    static Double getFinePercent() {
        FINE_PERCENT
    }

    static def calculateTariffSizeForRuPdl(int minSum, int maxSum, int step, int minDay, int maxDay) {
        int count = 0
        for (int i = minSum; i <= maxSum; i += step) {
            for (int j = minDay; j <= maxDay; j++) {
                count++
            }
        }
        count
    }


}
