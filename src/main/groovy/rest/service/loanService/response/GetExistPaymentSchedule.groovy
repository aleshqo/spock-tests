package rest.service.loanService.response

import com.fasterxml.jackson.annotation.JsonProperty

class GetExistPaymentSchedule {


    @JsonProperty("paymentDate")
    String paymentDate // дата планового платежа

    @JsonProperty("paymentAmount")
    BigDecimal paymentAmount // величина планового платежа

    @JsonProperty("interest")
    BigDecimal interest // доля процентной части в плановом платеже

    @JsonProperty("moratoriumInterest")
    BigDecimal moratoriumInterest // проценты, начисленные за льготный период, в платеже

    @JsonProperty("mainDebt")
    BigDecimal mainDebt // доля основного долга в платеже

    @JsonProperty("remainMainDebt")
    BigDecimal remainMainDebt // остаток основного долга после совершения платежа

    @JsonProperty("closedTime")
    String closedTime
    // дата закрытия платежного периода, null если период еще открыт (передаем последнюю дату/время распределения средств по данному платежному расписанию, если долг по данному платежному расписанию (за исключением комиссии) отсутствует  или вообще нет долгов). Т.е. если долг остался только по комиссии, платежный период считается закрытым (для корректного отображения в ЛК клиента).

    @JsonProperty("overdueInterestAccrued")
    BigDecimal overdueInterestAccrued // начислено просроченных процентов за период

    @JsonProperty("fineAccrued")
    BigDecimal fineAccrued // начислено пени за период

    @JsonProperty("overdueInterestPayed")
    BigDecimal overdueInterestPayed // оплачено начисленных просроченных процентов

    @JsonProperty("finePayed")
    BigDecimal finePayed // оплачено начисленной пени


    @Override
    String toString() {
        return "ExistPaymentSchedule{" +
                "paymentDate='" + paymentDate + '\'' +
                ", paymentAmount=" + paymentAmount +
                ", interest=" + interest +
                ", moratoriumInterest=" + moratoriumInterest +
                ", mainDebt=" + mainDebt +
                ", remainMainDebt=" + remainMainDebt +
                ", closedTime='" + closedTime + '\'' +
                ", overdueInterestAccrued=" + overdueInterestAccrued +
                ", fineAccrued=" + fineAccrued +
                ", overdueInterestPayed=" + overdueInterestPayed +
                ", finePayed=" + finePayed +
                '}'
    }

}
