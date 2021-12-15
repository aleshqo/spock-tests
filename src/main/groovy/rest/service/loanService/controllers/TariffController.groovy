package rest.service.loanService.controllers

import groovy.util.logging.Slf4j
import rest.service.loanService.enums.LoanTypeName
import rest.service.loanService.response.GetTariffResponse

import static rest.service.loanService.LoanConstants.*

@Slf4j
class TariffController {


    /** Get current tariffs by loan type */
    static List<GetTariffResponse> getTariff(LoanTypeName loanTypeName) {
        log.info("-> getTariff, loanTypeName=[{}]", loanTypeName)
        def response = LOAN_SERVICE.get("${PATH_SERVICE}${PATH_TARIFFS}/${loanTypeName.name()}",
                GetTariffResponse.class) as List<GetTariffResponse>
        response
    }
}
