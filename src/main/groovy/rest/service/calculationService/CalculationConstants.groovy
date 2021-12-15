package rest.service.calculationService

import rest.service.HttpService

class CalculationConstants {

    static final String CALCULATION_SERVICE_URI = 'http://localhost:8082'
    static final HttpService CALCULATE_SERVICE = new HttpService(CALCULATION_SERVICE_URI)

    static final String PATH_SERVICE = '/calculation-service'
    static final String PATH_CALCULATION = '/calculations'
    static final String PATH_CUSTOMER_BALANCES = '/customer-balances'
    static final String PATH_DATE = '/datetime'
}
