package rest.service.loanService.enums

enum LoanActionName {

    LOAN_CREATION(0),
    LOAN_CLOSE(2),
    LOAN_CANCELLATION(0),
    OVERDUE_START(0),
    OVERDUE_END(0),
    DISCOUNT_CANCELATION(0),
    PROLONGATION_START(6),
    PROLONGATION_END(0),
    FREEZING_START(9),
    FREEZING_END(0),
    BANKRUPTCY(0),
    WRITE_OFF(12),
    CLOSE_WITH_DEBT(0),
    APPLY_COURT_DECISION(0),
    STOP_CALCULATION_BY_FRAUD(16),
    STOP_CALCULATION_BY_LIMIT(22),
    STOP_CALCULATION_BY_BANKRUPT(24),
    CLOSE_WITH_DISCOUNT(0),
    MORATORIUM_START(0),
    MORATORIUM_END(0),
    BAD_CLOSE(18),
    REFINANCE(0),
    FAKE_CLOSE(0),
    FRAUD_CLOSE(0),
    SALE(0),
    RESTRUCTURING_START(0),
    RESTRUCTURING_END(0)

    int id

    LoanActionName(int id) {
        this.id = id
    }

    static String getActionById(def id) {
        values().find({if(it.id == id) return it.name()})
    }
}
