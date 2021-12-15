package rest.service.loanService.enums

enum DebtType {

    MAIN_DEBT(1),
    INTEREST(2),
    OVERDUE_INTEREST(3),
    FINE(4),
    WRITTEN_OFF_MAIN_DEBT(25),
    WRITTEN_OFF_INTEREST(26),
    WRITTEN_OFF_OVERDUE_INTEREST(27),
    COMMISSION_SMS(7),
    MISTAKENLY_ISSUED_DEBT(33)

    int id

    DebtType(int id) {
        this.id = id
    }

    static String getDebtType(int id) {
        for (DebtType e : values()) {
            if (e.id == id) return e.name().toLowerCase()
        }
        return null
    }
}
