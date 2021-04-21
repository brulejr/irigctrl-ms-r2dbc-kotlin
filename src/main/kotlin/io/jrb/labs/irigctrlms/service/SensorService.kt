/*
 * The MIT License (MIT)
 *
 * Copyright (c) 2021 Jon Brule <brulejr@gmail.com>
 *
 * Permission is hereby granted, free of charge, to any person obtaining a copy
 * of this software and associated documentation files (the "Software"), to deal
 * in the Software without restriction, including without limitation the rights
 * to use, copy, modify, merge, publish, distribute, sublicense, and/or sell
 * copies of the Software, and to permit persons to whom the Software is
 * furnished to do so, subject to the following conditions:
 *
 * The above copyright notice and this permission notice shall be included in
 * all copies or substantial portions of the Software.
 *
 * THE SOFTWARE IS PROVIDED "AS IS", WITHOUT WARRANTY OF ANY KIND, EXPRESS OR
 * IMPLIED, INCLUDING BUT NOT LIMITED TO THE WARRANTIES OF MERCHANTABILITY,
 * FITNESS FOR A PARTICULAR PURPOSE AND NONINFRINGEMENT. IN NO EVENT SHALL THE
 * AUTHORS OR COPYRIGHT HOLDERS BE LIABLE FOR ANY CLAIM, DAMAGES OR OTHER
 * LIABILITY, WHETHER IN AN ACTION OF CONTRACT, TORT OR OTHERWISE, ARISING FROM,
 * OUT OF OR IN CONNECTION WITH THE SOFTWARE OR THE USE OR OTHER DEALINGS IN THE
 * SOFTWARE.
 */
package io.jrb.labs.irigctrlms.service

import io.jrb.labs.common.service.CrudServiceUtils
import io.jrb.labs.common.service.R2dbcCrudServiceUtils
import io.jrb.labs.irigctrlms.mapping.SensorMapper
import io.jrb.labs.irigctrlms.model.SensorEntity
import io.jrb.labs.irigctrlms.repository.SensorRepository
import io.jrb.labs.irigctrlms.resource.SensorRequest
import io.jrb.labs.irigctrlms.resource.SensorResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant
import java.util.UUID

@Service
class SensorService(
    private val sensorRepository: SensorRepository,
    private val sensorMapper: SensorMapper
) {

    private val crudServiceUtils: CrudServiceUtils<SensorEntity> =
        R2dbcCrudServiceUtils(SensorEntity::class.java, sensorRepository)

    @Transactional
    fun createSensor(sensorRequest: SensorRequest): Mono<SensorResource> {
        return Mono.just(sensorMapper.sensorRequestToSensorEntity(sensorRequest))
            .map {
                val timestamp : Instant = Instant.now()
                it.copy(createdOn = timestamp, modifiedOn = timestamp)
            }
            .flatMap { crudServiceUtils.createEntity(it) }
            .map(sensorMapper::sensorEntityToSensorResource)
    }

    @Transactional
    fun deleteSensor(name: String): Mono<Void> {
        return crudServiceUtils.deleteEntity(name)
    }

    @Transactional
    fun findSensorByName(name: String): Mono<SensorResource> {
        return crudServiceUtils.findEntityByName(name)
            .map(sensorMapper::sensorEntityToSensorResource)
    }

    @Transactional
    fun listAllSensors(): Flux<SensorResource> {
        return crudServiceUtils.listAllEntities()
            .map(sensorMapper::sensorEntityToSensorResource)
    }

}
