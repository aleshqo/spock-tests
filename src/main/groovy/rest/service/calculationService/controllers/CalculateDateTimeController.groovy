package rest.service.calculationService.controllers

import groovy.util.logging.Slf4j

import static rest.service.calculationService.CalculationConstants.*

@Slf4j
class CalculateDateTimeController {

    static void mockDateTime(String dateTime) {
        log.info("-> mockDateTime=${dateTime}")
        CALCULATE_SERVICE.put("${PATH_SERVICE}${PATH_DATE}/dateTime?dateTime=${dateTime}")
    }

    static void clearMockedDate() {
        log.info("-> clearDate")
        CALCULATE_SERVICE.get("${PATH_SERVICE}${PATH_DATE}/clearDate", null)
    }
}
