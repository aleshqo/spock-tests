package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonAlias

import java.lang.reflect.Field

import static java.util.Objects.isNull
import static java.util.Objects.nonNull

class Debts {

    @JsonAlias(["mainDebtPay", "mainDebt", "MAIN_DEBT_PAY"])
    BigDecimal mainDebt

    @JsonAlias(["mistakenlyIssuedDebt", "MISTAKENLY_ISSUED_DEBT_PAY", "mistakenlyIssuedDebtPay"])
    BigDecimal mistakenlyIssuedDebt

    @JsonAlias(["interestPay", "interest", "INTEREST_PAY", "INTEREST_ACCRUE"])
    BigDecimal interest

    @JsonAlias(["overdueInterestPay", "overdueInterest", "OVERDUE_INTEREST_PAY"])
    BigDecimal overdueInterest

    @JsonAlias(["finePay", "fine", "FINE_PAY"])
    BigDecimal fine

    @JsonAlias(["writtenOffMainDebt", "writtenOffMainDebtPay", "WRITTEN_OFF_MAIN_DEBT_PAY"])
    BigDecimal writtenOffMainDebt

    @JsonAlias(["writtenOffInterest", "writtenOffInterestPay", "WRITTEN_OFF_INTEREST_PAY"])
    BigDecimal writtenOffInterest

    @JsonAlias(["writtenOffOverdueInterest", "writtenOffOverdueInterestPay", "WRITTEN_OFF_OVERDUE_INTEREST_PAY"])
    BigDecimal writtenOffOverdueInterest

    @JsonAlias(["WRITTEN_OFF_FINE_PAY"])
    BigDecimal writtenOffFinePay

    @JsonAlias(["commissionTelemedicine", "commissionTelemedicinePay", "COMMISSION_TELEMEDICINE_PAY"])
    BigDecimal commissionTelemedicine

    @JsonAlias(["commissionImproveCh", "commissionImproveChPay", "COMMISSION_IMPROVE_CH_PAY"])
    BigDecimal commissionImproveCh

    @JsonAlias(["commissionCreditHistoryAnalysis", "commissionCreditHistoryAnalysisPay", "COMMISSION_CREDIT_HISTORY_ANALYSIS_PAY"])
    BigDecimal commissionCreditHistoryAnalysis

    @JsonAlias(["commissionScoringBoost", "commissionScoringBoostPay", "COMMISSION_SCORING_BOOST_PAY"])
    BigDecimal commissionScoringBoost

    @JsonAlias(["commissionCardInsurance", "commissionCardInsurancePay", "COMMISSION_CARD_INSURANCE_PAY"])
    BigDecimal commissionCardInsurance

    @JsonAlias(["commissionSms", "commissionSmsPay", "COMMISSION_SMS_PAY"])
    BigDecimal commissionSms

    @JsonAlias(["commissionCustomerInsurance", "commissionCustomerInsurancePay", "COMMISSION_CUSTOMER_INSURANCE_PAY"])
    BigDecimal commissionCustomerInsurance

    boolean fieldsIsNull() {
        return isNull(this.mainDebt) && isNull(this.interest) && isNull(this.overdueInterest) && isNull(this.fine)
                && isNull(this.writtenOffMainDebt) && isNull(this.writtenOffInterest)
                && isNull(this.writtenOffOverdueInterest) && isNull(this.writtenOffFinePay)
    }

    boolean debtsIsNull() {
        return isNull(this.mainDebt) && isNull(this.interest) && isNull(this.overdueInterest) && isNull(this.fine)
    }

    boolean equals(o) {
        if (this.is(o)) return true
        if (getClass() != o.class) return false

        Debts debts = (Debts) o

        if (fine != debts.fine) return false
        if (interest != debts.interest) return false
        if (mainDebt != debts.mainDebt) return false
        if (overdueInterest != debts.overdueInterest) return false

        return true
    }

    int hashCode() {
        int result
        result = (mainDebt != null ? mainDebt.hashCode() : 0)
        result = 31 * result + (interest != null ? interest.hashCode() : 0)
        result = 31 * result + (overdueInterest != null ? overdueInterest.hashCode() : 0)
        result = 31 * result + (fine != null ? fine.hashCode() : 0)
        return result
    }

    @Override
    String toString() {
        return "Debts{" +
                "mainDebt=" + mainDebt +
                ", interest=" + interest +
                ", overdueInterest=" + overdueInterest +
                ", fine=" + fine +
                ", writtenOffMainDebt=" + writtenOffMainDebt +
                ", writtenOffInterest=" + writtenOffInterest +
                ", writtenOffOverdueInterest=" + writtenOffOverdueInterest +
                ", writtenOffFinePay=" + writtenOffFinePay +
                '}';
    }

    int getCountOfDeclaredFields() {
        int countOfDeclaredFields = 0
        Field[] fields = Debts.class.getDeclaredFields();
        for (item in fields) {
            item.setAccessible(true)
            if (isFieldDeclared(item)) {
                countOfDeclaredFields++
            }
        }
        countOfDeclaredFields
    }

    int getCountOfDeclaredCommissions() {
        int countOfDeclaredCommissions = 0
        Field[] fields = Debts.class.getDeclaredFields();
        for (item in fields) {
            item.setAccessible(true)
            if (item.getName().startsWith("commissions") && isFieldDeclared(item)) {
                countOfDeclaredFields++
            }
        }
        countOfDeclaredCommissions
    }

    private boolean isFieldDeclared(Field field) {
        return (field.getType().getName() == BigDecimal.getTypeName())
                && (nonNull(field.get(this)))
                && (field.get(this) > 0)
    }
}
