package helper


import rest.service.loanService.enums.InterestDiscountType

import static kafka.topics.TopicName.*
import static rest.service.loanService.controllers.LoanDateTimeController.*
import static rest.service.loanService.enums.OperationType.PAYMENT
import static rest.service.utils.ReqHelper.getNewExternalId
import static rest.service.utils.ReqHelper.getNewId

class KafkaRequests {

    static Map getCustomerReceivedMoneyMsg(Map loanBody) {
        Map commonMap = getCommonMap(CUSTOMER_RECEIVED_MONEY.toCamelCase())
        if (loanBody.mistakenlyIssued == null) loanBody.mistakenlyIssued = false
        commonMap.payload = [
                "customerId"   : loanBody.customerId,
                "loanRequestId": getNewId(),
                "transferTime" : "${loanBody.loanDate}T${getServerTime()}".concat("+07:00"),
                "transferId"   : getNewId(),
                "agreement"    : [
                        "id"              : loanBody.id,
                        "loanType"        : loanBody.loanTypeName,
                        "loanDate"        : loanBody.loanDate,
                        "loanAmount"      : loanBody.amount,
                        "loanTerm"        : loanBody.loanTerm,
                        "tariffType"      : loanBody.tariffType,
                        "offline"         : loanBody.offline,
                        "mistakenlyIssued": loanBody.mistakenlyIssued
                ]]
        commonMap
    }

    static Map getCancelLoanAgreementMsg(Map loanBody) {
        Map commonMap = getCommonMap(CANCEL_LOAN_AGREEMENT.toCamelCase())
        commonMap.payload = [
                "customerId" : loanBody.customerId,
                "agreementId": loanBody.id
        ]
        commonMap
    }

    static Map getCustomerMadePaymentMsg(Map loanBody, amount = 10000) {
        Map commonMap = getCommonMap(CUSTOMER_MADE_PAYMENT.toCamelCase())
        commonMap.payload = [
                "customerId"   : loanBody.customerId,
                "loanId"       : loanBody.id,
                "amount"       : amount,
                "operationType": PAYMENT.name(),
                "paymentTime"  : getServerDateTime().concat("+07:00"),
                "paymentId"    : getNewExternalId()
        ]
        commonMap
    }

    static Map getCustomerRequestedCommissionMsg(Map commissionBody) {
        Map commonMap = getCommonMap(CUSTOMER_REQUESTED_COMMISSION.toCamelCase())
        commonMap.payload = [
                "loanId"     : commissionBody.loanId,
                "commissions": [[
                                        "fromDate": commissionBody.commissions[0].fromDate,
                                        "type"    : commissionBody.commissions[0].type,
                                        "amount"  : commissionBody.commissions[0].amount
                                ]]
        ]
        commonMap
    }

    static Map getCancelCommissionMsg(def commissionId, cancellationType = "ALL") {
        Map commonMap = getCommonMap(CANCEL_COMMISSION.toCamelCase())
        commonMap.payload = [
                "commissionId"    : commissionId,
                "cancellationType": cancellationType
        ]
        commonMap
    }

    static Map getCustomerCanceledPaymentMsg(def paymentId) {
        Map commonMap = getCommonMap(CUSTOMER_CANCELED_PAYMENT.toCamelCase())
        commonMap.payload = [
                "paymentId": paymentId
        ]
        commonMap
    }

    static Map getReserveFundsOnBalanceMsg(def customerId, def amount) {
        Map commonMap = getCommonMap(RESERVE_FUNDS_ON_BALANCE.toCamelCase())
        commonMap.payload = [
                "customerId": customerId,
                "amount"    : amount
        ]
        commonMap
    }

    static Map getCancelFundsReservationMsg(def customerId) {
        Map commonMap = getCommonMap(CANCEL_FUNDS_RESERVATION.toCamelCase())
        commonMap.payload = [
                "customerId": customerId
        ]
        commonMap
    }

    static Map getWithdrawFromBalanceMsg(def customerId, def amount) {
        Map commonMap = getCommonMap(WITHDRAW_FUNDS_FROM_BALANCE.toCamelCase())
        commonMap.payload = [
                "customerId": customerId,
                "amount"    : amount
        ]
        commonMap
    }

    static Map getAddInterestDiscountMsg(Map loanBody,
                                         InterestDiscountType interestDiscountType,
                                         def value,
                                         String fromDate = getServerDate(1)) {
        Map commonMap = getCommonMap(ADD_INTEREST_DISCOUNT.toCamelCase())
        commonMap.payload = [
                "loanId"          : loanBody.id,
                "interestDiscount": [
                        "fromDate": fromDate,
                        "type"    : interestDiscountType.name(),
                        "value"   : value
                ]
        ]
        commonMap
    }

    static Map getCustomerRequestedFreezingMsg(Map loanBody, String fromDate = getServerDate(), def term = 5, def amount = 0) {
        Map commonMap = getCommonMap(CUSTOMER_REQUESTED_FREEZING.toCamelCase())
        commonMap.payload = [
                "loanId"  : loanBody.id,
                "freezing": [
                        "fromDate": fromDate,
                        "term"    : term,
                        "amount"  : amount
                ]
        ]
        commonMap
    }

    static Map getProlongFreezingMsg(def loanId, String toDate) {
        Map commonMap = getCommonMap(PROLONG_FREEZING.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "toDate": toDate
        ]
        commonMap
    }

    static Map getCancelFreezingMsg(def loanId) {
        Map commonMap = getCommonMap(CANCEL_FREEZING.toCamelCase())
        commonMap.payload = [
                "loanId": loanId
        ]
        commonMap
    }

    static Map getCustomerSignedProlongation(Map loanBody, def externalId, String fromDate = getServerDateTime()) {
        Map commonMap = getCommonMap(CUSTOMER_SIGNED_PROLONGATION.toCamelCase())
        commonMap.payload = [
                "term"          : loanBody.loanTerm,
                "loanId"        : loanBody.id,
                "activationTime": fromDate.concat("+07:00"),
                "externalId"    : externalId
        ]
        commonMap
    }

    static Map getStopLoanByBankruptMsg(def loanId, String date) {
        Map commonMap = getCommonMap(STOP_LOAN.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "date"  : date
        ]
        commonMap
    }

    static Map getStopLoansByCustomer(def customerId, String fromDate) {
        Map commonMap = getCommonMap(STOP_LOANS_BY_BANKRUPT_CUSTOMER.toCamelCase())
        commonMap.payload = [
                "customerId": customerId,
                "date"      : fromDate
        ]
        commonMap
    }

    static Map getBankruptCustomerMsg(def customerId, String date) {
        Map commonMap = getCommonMap(BANKRUPT_CUSTOMER.toCamelCase())
        commonMap.payload = [
                "customerId": customerId,
                "date"      : date
        ]
        commonMap
    }

    static Map getWriteOffLoanMsg(def loanId, String date = getServerDate()) {
        Map commonMap = getCommonMap(LOAN_WRITTEN_OFF.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "date"  : date
        ]
        commonMap
    }

    static Map getStopLoanByFraudMsg(def customerId) {
        Map commonMap = getCommonMap(STOP_LOANS_BY_FRAUD_CUSTOMER.toCamelCase())
        commonMap.payload = [
                "customerId": customerId
        ]
        commonMap
    }

    static Map getResumeCalculationMsg(def loanId) {
        Map commonMap = getCommonMap(RESUME_LOAN_CALCULATION.toCamelCase())
        commonMap.payload = [
                "loanId": loanId
        ]
        commonMap
    }

    static Map getCustomerPromisedRefinance(def loanId) {
        Map commonMap = getCommonMap(CUSTOMER_PROMISED_REFINANCE.toCamelCase())
        commonMap."payload" = ["loanId": loanId]
        commonMap
    }

    static Map getCustomerCancelIntentMsg(def loanId) {
        Map commonMap = getCommonMap(CUSTOMER_CANCELLED_REFINANCE_INTENT.toCamelCase())
        commonMap.payload = [
                "loanId": loanId
        ]
        commonMap
    }

    static Map getApplyFixedAmountDiscountMsg(def loanId, def amount, String fromDate = getServerDate()) {
        Map commonMap = getCommonMap(APPLY_FIXED_AMOUNT_DISCOUNT.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "date"  : fromDate,
                "amount": amount
        ]
        commonMap
    }

    static Map getApplyCourtDecisionMsg(Map applyCourtDecisionBody) {
        Map commonMap = getCommonMap(APPLY_COURT_DECISION.toCamelCase())
        commonMap.payload = [
                "loanId"       : applyCourtDecisionBody.loanId,
                "courtDecision": [
                        "fromDate"       : applyCourtDecisionBody.date,
                        "interest"       : applyCourtDecisionBody.interest,
                        "overdueInterest": applyCourtDecisionBody.overdueInterest,
                        "fine"           : applyCourtDecisionBody.fine,
                        "stateFee"       : applyCourtDecisionBody.stateFee,
                        "mainDebt"       : applyCourtDecisionBody.mainDebt
                ]

        ]
        commonMap
    }

    static Map getLoanSellMsg(def loanId, String fromDate = getServerDate()) {
        Map commonMap = getCommonMap(LOAN_SOLD.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "date"  : fromDate
        ]
        commonMap
    }

    static Map getCloseBadLoanMsg(def loanId) {
        Map commonMap = getCommonMap(LOAN_SOLD.toCamelCase())
        commonMap.payload = [
                "loanId": loanId
        ]
        commonMap
    }

    static Map getPayLoanFromBalanceMsg(def loanId, def amount) {
        Map commonMap = getCommonMap(PAY_LOAN_FROM_BALANCE.toCamelCase())
        commonMap.payload = [
                "loanId": loanId,
                "amount": amount
        ]
        commonMap
    }

    static Map getCloseLoanWithDiscountMsg(def loanId) {
        Map commonMap = getCommonMap(CLOSE_LOAN_WITH_DISCOUNT.toCamelCase())
        commonMap.payload = [
                "loanId": loanId
        ]
        commonMap
    }

    static Map getCommonMap(String topicName) {
        return [
                "name"     : topicName,
                "eventTime": getEventTime(),
        ]
    }

    static String getEventTime() {
        getServerDateTime().concat("+07:00")
    }
}
