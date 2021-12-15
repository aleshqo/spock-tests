package helper

import rest.service.loanService.response.*
import rest.service.loanService.response.freezing.GetFreezeAvailResponse
import rest.service.loanService.response.freezing.GetFreezesResponse
import rest.service.loanService.response.prolongation.CreateProlongationViaOrderResponse
import rest.service.loanService.response.prolongation.GetAvailProlongResponse
import rest.service.loanService.response.prolongation.GetProlongationResponse

import static helper.restrequests.DopoluchkinoRestRequests.getConfirmProlongBodyReq
import static helper.restrequests.DopoluchkinoRestRequests.getPaymentBodyReq
import static java.util.Objects.nonNull
import static rest.service.loanService.controllers.DebtsController.getCurrentDebs
import static rest.service.loanService.controllers.DebtsController.getCurrentDebsOnDate
import static rest.service.loanService.controllers.DiscountController.getDiscount
import static rest.service.loanService.controllers.FreezingController.*
import static rest.service.loanService.controllers.LoanController.*
import static rest.service.loanService.controllers.LoanDateTimeController.getServerDateTime
import static rest.service.loanService.controllers.LoanDateTimeController.getSrvDateTimeMinusSec
import static rest.service.loanService.controllers.PaymentController.createPayment
import static rest.service.loanService.controllers.PaymentController.getPayments
import static rest.service.loanService.controllers.ProlongationController.*
import static rest.service.loanService.controllers.TestController.getNotificationData
import static rest.service.utils.ReqHelper.*
import static dataBaseService.ManipulationDao.*

class Wrappers {

    static List<GetPaymentsResponse> sendPayment(Map paymentBody, boolean byCustomer = false) {
        createPayment(paymentBody)
        List<GetPaymentsResponse> paymentsResponses = waitForResponse(
                (byCustomer ? { getPayments(null, paymentBody.customerId) }
                        : { getPayments(paymentBody.loanId) }), { it.externalId.contains(paymentBody.externalId) }) as List<GetPaymentsResponse>
        paymentsResponses.sort { it.paymentTime }
        return paymentsResponses
    }

    static GetLoanResponse sendLoan(Map loanBody) {
        createLoan(loanBody)
        GetLoanResponse loanResponse = getLoanById(loanBody.id as Integer) as GetLoanResponse
        return loanResponse
    }

    static List<NotificationResponse> getNotifications(def loanId, def date, String status) {
        List<NotificationResponse> response = waitForResponse({ getNotificationData(loanId, date) },
                { it.data.currentStatus.contains(status) }) as List<NotificationResponse>
        response
    }

    static NotificationResponse getNotificationByStatus(def loanId, def date, String status) {
        List<NotificationResponse> response = waitForResponse({ getNotificationData(loanId, date) },
                { it.data.currentStatus.contains(status) }) as List<NotificationResponse>
        return response.find({ it.data.currentStatus == status })
    }

    @SuppressWarnings('GroovyPointlessBoolean')
    static AddFreezingResponse addLoanFreezing(Map loanBody, Map freezingBody) {

        GetFreezeAvailResponse freezeAvailResponse = waitForResponse(
                { getFreezingAvailability([loanId: loanBody.id]) }, { it.result }) as GetFreezeAvailResponse

        assert freezeAvailResponse.result == true
        assert freezeAvailResponse.maxAvailableAmount == getChargesSumDb(loanBody.id)

        AddFreezingResponse addFreezingResponse = waitForResponse(
                { addFreezing(freezingBody) }, { it.result }) as AddFreezingResponse

        return addFreezingResponse
    }

    static def moveLoanToProlongation(Map loanBody, def percent = 1, String dateTime = getSrvDateTimeMinusSec(2)) {
        // оплачиваем проценты
        int amount = (int) (calcInterest(loanBody.amount, loanBody, percent) +
                calcOverdueInterest(loanBody.amount, loanBody, percent))
        Map paymentBody = getPaymentBodyReq(amount, loanBody.customerId, loanBody.id)
        paymentBody.paymentTime = dateTime
        sendPayment(paymentBody)
        Map confirmProlongBody = getConfirmProlongBodyReq(paymentBody, loanBody.loanTerm, getSrvDateTimeMinusSec(1))
        confirmProlongation(confirmProlongBody)
    }

    static List<GetFreezesResponse> getFreezingList(def loanId, String state) {
        List res = waitForResponse({ getFreezesByLoanId(loanId) }, { it.state.contains(state) }) as List<GetFreezesResponse>
        res.sort({ it.fromDate })
    }

    static GetProlongationResponse confirmProlongation(Map body) {
        CreateProlongationViaOrderResponse response = waitForResponse(
                { createProlongationViaOrder(body) }, { nonNull(it) }) as CreateProlongationViaOrderResponse
        assert getProlongationDb(body.loanId).last().ID == response.id
        assert getProlongationOrderFromDb(body.loanId).PROLONGATION_ID == response.id
        GetProlongationResponse getProlongationResponse = getProlongations(body.loanId)[0] as GetProlongationResponse
        getProlongationResponse
    }

    static GetDebtsResponse findCurrentDebtOnDate(def loanId,
                                                  def dateTime = getServerDateTime(),
                                                  Closure condition = { nonNull(it) }) {
        waitForResponse({ getCurrentDebsOnDate(loanId, dateTime) }, condition).find({ condition }) as GetDebtsResponse
    }

    static GetDebtsResponse findCurrentDebt(def loanId, Closure condition = { nonNull(it) }) {
        waitForResponse({ getCurrentDebs(loanId) }, condition).find({ condition }) as GetDebtsResponse
    }

    static List<Discounts> findDiscounts(def loanId, Closure condition = { nonNull(it) }) {
        waitForResponse({ getDiscount(loanId) }, condition) as List<Discounts>
    }

    static List<GetAvailProlongResponse> getAvailProlong(def loanId, def beginDate, Closure condition = { !it.isEmpty() }) {
        waitForResponse({ getAvailableProlongation(loanId, beginDate) }, condition) as List<GetAvailProlongResponse>
    }

    static GetLoanDataOnDateResponse findLoanDataOnDate(def loanId,
                                                        def dateTime = getServerDateTime(),
                                                        Closure condition = { nonNull(it) }) {
        waitForResponse({ getLoanDataOnDate(loanId, dateTime) }, condition).find({ condition }) as GetLoanDataOnDateResponse
    }
}
