package io.jrb.labs.irigctrlms.mapping

import io.jrb.labs.irigctrlms.model.SensorEntity
import io.jrb.labs.irigctrlms.resource.SensorRequest
import io.jrb.labs.irigctrlms.resource.SensorResource
import org.mapstruct.Mapper
import org.mapstruct.ReportingPolicy

@Mapper(componentModel = "spring", unmappedTargetPolicy = ReportingPolicy.IGNORE)
interface SensorMapper {

    fun sensorEntityToSensorResource(
        sensorEntity: SensorEntity
    ) : SensorResource

    fun sensorRequestToSensorEntity(
        sensorRequest: SensorRequest
    ) : SensorEntity

    fun sensorResourceToSensorEntity(
        sensoryResource: SensorResource
    ) : SensorEntity

}
