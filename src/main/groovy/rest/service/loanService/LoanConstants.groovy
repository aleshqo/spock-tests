package rest.service.loanService

import rest.service.HttpService

class LoanConstants {

    //todo: вынести в класс connectionController
    static final String LOAN_SERVICE_URI = 'http://localhost:8080'
    static final HttpService LOAN_SERVICE = new HttpService(LOAN_SERVICE_URI)

    static final String PATH_BALANCES = '/balances'
    static final String PATH_BANKRUPT = '/bankrupt'
    static final String PATH_CALCULATE = '/calculate'
    static final String PATH_CANCEL = '/cancel'
    static final String PATH_CANCEL_RESERVATION = '/cancel-reservation'
    static final String PATH_CLEAR_DATE = '/datetime/clearDate'
    static final String PATH_COMMISSIONS = '/commissions'
    static final String PATH_CREATE = '/create'
    static final String PATH_DATE_TIME = '/datetime'
    static final String PATH_DISCOUNT = '/discounts'
    static final String PATH_FREEZINGS = '/freezings'
    static final String PATH_GET_DATE = '/datetime/getDateTime'
    static final String PATH_HISTORY = '/history'
    static final String PATH_INTEREST_DISCOUNTS = '/interest-discounts'
    static final String PATH_LOAN = '/loan-service/loans'
    static final String PATH_PAYMENTS = '/payments'
    static final String PATH_PROLONG = '/prolong'
    static final String PATH_PROLONGATION = '/prolongations'
    static final String PATH_PSK = '/psk'
    static final String PATH_RESERVE_BALANCE = '/reserve-balance'
    static final String PATH_RESUME_CALCULATION = '/resumeCalculation'
    static final String PATH_SERVICE = '/loan-service'
    static final String PATH_SCORING = '/scoring'
    static final String PATH_SCHEDULES = '/schedules'
    static final String PATH_STOP = '/stop'
    static final String PATH_TARIFFS = '/tariffs'
    static final String PATH_TEST = '/test'
    static final String PATH_WITHDRAW = '/withdraw-from-balance'
    static final String PATH_WRITE_OFF = '/write-off'
    static final String PATH_PAY_FROM_BALANCE = '/pay-from-balance'
    static final String PATH_CANCEL_UNPAID = '/cancelUnpaid'
    static final String PATH_BAD_CLOSE = '/closeBad'
    static final String PATH_TRANSACTIONS = '/transactions'
    static final String PATH_GIVE_PAYMENT_BACK = '/give-payment-back'
    static final String PATH_SELL_LOAN = '/sell'
}
