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
import io.jrb.labs.irigctrlms.mapping.ScheduleMapper
import io.jrb.labs.irigctrlms.model.ScheduleEntity
import io.jrb.labs.irigctrlms.repository.ScheduleRepository
import io.jrb.labs.irigctrlms.resource.ScheduleRequest
import io.jrb.labs.irigctrlms.resource.ScheduleResource
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import java.time.Instant

@Service
class ScheduleService(
    private val scheduleRepository: ScheduleRepository,
    private val scheduleMapper: ScheduleMapper
) {

    private val crudServiceUtils: CrudServiceUtils<ScheduleEntity> =
        R2dbcCrudServiceUtils(ScheduleEntity::class.java, scheduleRepository)

    @Transactional
    fun createSchedule(scheduleRequest: ScheduleRequest): Mono<ScheduleResource> {
        return Mono.just(scheduleMapper.scheduleRequestToScheduleEntity(scheduleRequest))
            .map {
                val timestamp : Instant = Instant.now()
                it.copy(createdOn = timestamp, modifiedOn = timestamp)
            }
            .flatMap { crudServiceUtils.createEntity(it) }
            .map(scheduleMapper::scheduleEntityToScheduleResource)
    }

    @Transactional
    fun deleteSchedule(name: String): Mono<Void> {
        return crudServiceUtils.deleteEntity(name)
    }

    @Transactional
    fun findScheduleByName(name: String): Mono<ScheduleResource> {
        return crudServiceUtils.findEntityByName(name)
            .map(scheduleMapper::scheduleEntityToScheduleResource)
    }

    @Transactional
    fun listAllSchedules(): Flux<ScheduleResource> {
        return crudServiceUtils.listAllEntities()
            .map(scheduleMapper::scheduleEntityToScheduleResource)
    }

}
