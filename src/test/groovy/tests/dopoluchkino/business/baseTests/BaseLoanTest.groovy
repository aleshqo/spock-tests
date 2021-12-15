package tests.dopoluchkino.business.baseTests

import rest.service.loanService.enums.LoanTypeName
import rest.service.loanService.response.GetDebtsResponse
import rest.service.loanService.response.GetLoanDataOnDateResponse
import rest.service.loanService.response.GetTariffResponse
import spock.lang.Specification
import spock.lang.Unroll

import static dataBaseService.ManipulationDao.getChargesSumDb
import static dataBaseService.ManipulationDao.getTariffFromDb
import static helper.VerifyResponse.checkDebts
import static helper.VerifyResponse.checkLoan
import static helper.Wrappers.*
import static helper.restrequests.DopoluchkinoRestRequests.getLoanBodyReq
import static helper.restrequests.DopoluchkinoRestRequests.getPaymentBodyReq
import static java.util.Objects.nonNull
import static rest.service.calculationService.controllers.CalculationController.calculateLoan
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDateTime
import static rest.service.loanService.controllers.TariffController.getTariff
import static rest.service.loanService.enums.LoanStatus.OPENED
import static rest.service.loanService.enums.LoanStatus.OVERDUE
import static rest.service.loanService.enums.LoanTypeName.PDL_DOPOLUCHKINO
import static rest.service.utils.ReqHelper.*


class BaseLoanTest extends Specification {


    private static Map loanBody
    private static Map paymentBody

    def setup() {
        loanBody = getLoanBodyReq()
        paymentBody = getPaymentBodyReq(null, loanBody.customerId, loanBody.id)
    }

    @Unroll
    def "check tariff scale with amount: #amount, term: #term"() {
        setup:
        loanBody.loanTerm = term
        loanBody.amount = amount

        when:
        sendLoan(loanBody)

        then:
        checkLoan(loanBody, OPENED.name())

        and:
        mockDate(getDateTime(term + 1))
        calculateLoan(loanBody.id)

        and:
        checkLoan(loanBody, OVERDUE.name())
        assert getChargesSumDb(loanBody.id) == calcCharges(amount, loanBody)
        checkDebts(amount, loanBody, true)
        checkTariff(amount, term)

        cleanup:
        clearDate()

        where:
        term | amount
        7    | 200000
        15   | 700000
        21   | 1550000
        24   | 2150000
        31   | 3000000
    }

    @Unroll
    def "loan in past. check tariff scale with amount: #amount, term: #term"() {
        setup:
        loanBody.loanDate = getPastDate(term + 1)
        loanBody.loanTerm = term
        loanBody.amount = amount

        when:
        sendLoan(loanBody)

        then:
        checkLoan(loanBody, OVERDUE.name())
        assert getChargesSumDb(loanBody.id) == calcCharges(amount, loanBody)
        checkDebts(amount, loanBody, true)

        where:
        term | amount
        7    | 200000
        15   | 700000
        21   | 1550000
        24   | 2150000
        31   | 3000000
    }

    def "check correct accruals of I, OI and F, rounding of accruals"() {
        setup:
        loanBody.loanDate = getPastDate(6)

        when:
        sendLoan(loanBody)

        then:
        checkLoan(loanBody, OPENED.name())
        sleep 1000
        assert getChargesSumDb(loanBody.id) == calcCharges(loanBody.amount, loanBody)

        when: "оплачиваем сумму с копейками"
        paymentBody.amount = 65432
        sendPayment(paymentBody)

        and:
        BigDecimal mainDebt = loanBody.amount - (paymentBody.amount - calcInterest(loanBody.amount, loanBody))

        and: "двигаем займ в просрочку"
        mockDate(getServerDateTime(5))
        calculateLoan(loanBody.id)
        def interest = calcInterest(mainDebt, loanBody)
        def overdueInterest = calcOverdueInterest(mainDebt, loanBody)
        def fine = calculateFine(mainDebt, loanBody)

        and:
        GetDebtsResponse debtsResponse = findCurrentDebtOnDate(loanBody.id)
        GetLoanDataOnDateResponse loanDataOnDateResponse = findLoanDataOnDate(loanBody.id, getServerDateTime(),
                { nonNull(it.categorizations) })

        then: "проверяем корректность начислений"
        assert debtsResponse.debts.mainDebt == mainDebt
        assert debtsResponse.debts.interest == interest / 7
        assert debtsResponse.debts.overdueInterest == overdueInterest
        assert debtsResponse.debts.fine == fine

        assert loanDataOnDateResponse.charges.interest == (loanBody.amount * 0.01 * 6) + (interest / 7) // за 7ой день процент с учетом платежа
        assert loanDataOnDateResponse.charges.overdueInterest == overdueInterest
        assert loanDataOnDateResponse.charges.fine == fine

        cleanup:
        clearDate()
    }

    static boolean checkTariff(def amount, def term, LoanTypeName loanTypeName = PDL_DOPOLUCHKINO) {
        GetTariffResponse tariff = getTariff(loanTypeName).find({ it.amount == amount && it.loanTerm == term })
        Map tariffInBd = getTariffFromDb(amount, term, loanTypeName.name())

        assert tariff.id == tariffInBd.id
        assert tariff.loanTerm == tariffInBd.loan_term
        assert tariff.amount == tariffInBd.amount * 100
        assert tariff.interest == tariffInBd.interest
        assert tariff.yearlyInterest == 365
        assert tariff.overdueInterest == tariffInBd.options.overdue_interest.rate[0]
        assert tariff.fine == tariffInBd.options.fine.rate[0]
        assert tariff.paymentAmount == calcPaymentAmount(loanBody.amount, 1, loanBody.loanTerm)
        true
    }
}
