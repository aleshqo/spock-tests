package rest.service.loanService.enums

enum CommissionsType {

    SMS(1),
    CARD_INSURANCE(2),
    CUSTOMER_INSURANCE(3),
    IMPROVE_CH(4),
    IMPROVE_CH2(5),
    CREDIT_REPORT(6),
    CUSTOMER_CREDIT_REPORT(8),
    SCORING_BOOST(7),
    CREDIT_HISTORY_ANALYSIS(9),
    TELEMEDICINE(10),
    STATE_FEE(11)

    int id

    CommissionsType(int id) {
        this.id = id
    }

    static String getCommissionTypeName(int id) {
        for (CommissionsType t : values()) {
            if (t.id == id) return t.name().toLowerCase()
        }
        return null
    }
}
