package rest.service.loanService.enums

enum LoanTypeName {

    PDL_INDIA(0),
    PDL_DOPOLUCHKINO(4),
    PDL_ZAYMER(6),
    PDL_ROBOCREDIT(0),
    INSTALLMENT_MONTHLY(1),
    INSTALLMENT_TWICE_A_MONTH(2),
    PDL_KZ(0),
    PDL_LANKA(0)
    
    private int id

    LoanTypeName(int id) {
        this.id = id
    }

    int getId() {
        return id
    }

    static int getIdByName(String loanTypeName) {
        LoanTypeName name = LoanTypeName.find({it.name() == loanTypeName}) as LoanTypeName
        return name.getId()
    }
}
