package tests.dopoluchkino.kafka

import kafka.dto.response.TariffGridUpdatedDto
import spock.lang.Specification

import static dataBaseService.ManipulationDao.getAllTariffByLoanType
import static dataBaseService.ManipulationDao.getNotificationMessageFromDb
import static helper.tariffScale.DopoluchkinoTariffScale.*
import static kafka.service.TariffGridUpdatedService.readFromTariffGridUpdated
import static kafka.topics.TopicPrefixName.DOPOLUCHKINO
import static rest.service.loanService.enums.LoanTypeName.PDL_DOPOLUCHKINO

class TariffGridUpdatedTest extends Specification {

    def "tariff grid updated at application start"() {
        when:
        TariffGridUpdatedDto tariffGridUpdatedRecords = readFromTariffGridUpdated(DOPOLUCHKINO, 3000)
        List tariffFromDb = getAllTariffByLoanType(PDL_DOPOLUCHKINO.name())
        Map notificationsFromDb = getNotificationMessageFromDb(0).find({it.name == 'tariffGridUpdated'})

        then:
        assert tariffGridUpdatedRecords.grid.size() == calculateTariffSizeForRuPdl(MIN_SUM, MAX_SUM, STEP_SUM, MIN_DAY, MAX_DAY)
        assert tariffFromDb.size() == tariffGridUpdatedRecords.grid.size()
        tariffFromDb.forEach({
            assert it.from_date.toString() == "2020-01-01"
            assert it.to_date.toString() == "2200-01-01"
        })
        assert notificationsFromDb.payload.grid.size() == tariffFromDb.size()
        assert notificationsFromDb.name == 'tariffGridUpdated'
    }
}
