package rest.service.loanService.enums

enum LoanStatus {

    OPENED(1),
    CLOSED(2),
    OVERDUE(3),
    PROLONGATION(4),
    COMPLETED(5),
    CANCELLED(6),
    FREEZING(7),
    WRITTEN_OFF(9),
    CLOSED_WITH_DEBT(10),
    BANKRUPT(11),
    COURT_DECISION(12),
    STOPPED(13),
    MORATORIUM(14),
    SOLD(15),
    NOT_CALCULATED(16),
    RESTRUCTURED(17),
    FRAUDSTER(8),
    MISTAKENLY_ISSUED(19)

    int id

    LoanStatus(int id) {
        this.id = id
    }

    static String getStatusById(def id) {
        values().find({if(it.id == id) return it.name()})
    }
}
