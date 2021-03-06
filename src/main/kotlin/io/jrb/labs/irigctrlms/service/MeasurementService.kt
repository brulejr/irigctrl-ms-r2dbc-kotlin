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

import io.jrb.labs.irigctrlms.mapping.MeasurementMapper
import io.jrb.labs.irigctrlms.repository.MeasurementRepository
import io.jrb.labs.irigctrlms.repository.SensorRepository
import io.jrb.labs.irigctrlms.resource.MeasurementRequest
import io.jrb.labs.irigctrlms.resource.MeasurementResource
import io.jrb.labs.irigctrlms.resource.SensorRequest
import io.jrb.labs.irigctrlms.resource.SensorResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class MeasurementService(
    private val measurementRepository: MeasurementRepository,
    private val measurementMapper: MeasurementMapper,
    private val sensorRepository: SensorRepository
) {

    @Transactional
    fun createMeasurement(
        sensorName: String?,
        measurementRequest: MeasurementRequest
    ): Mono<MeasurementResource> {
        return Mono.just(sensorName ?: measurementRequest.sensorName!!)
            .flatMap { sensorRepository.findByName(it) }
            .flatMap {
                val entity = measurementMapper.measurementRequestToMeasurementEntity(measurementRequest)
                    .copy(sensorId = it.id!!)
                measurementRepository.save(entity)
            }.map(measurementMapper::measurementEntityToMeasurementResource)
    }

    @Transactional
    fun listMeasurementsBySensor(sensorName: String): Flux<MeasurementResource> {
        return sensorRepository.findByName(sensorName)
            .flatMapMany { measurementRepository.findAllBySensorId(it.id!!) }
            .map(measurementMapper::measurementEntityToMeasurementResource)
    }

}
