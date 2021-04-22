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
package io.jrb.labs.irigctrlms.rest

import io.jrb.labs.irigctrlms.resource.ScheduleRequest
import io.jrb.labs.irigctrlms.resource.ScheduleResource
import io.jrb.labs.irigctrlms.service.ScheduleService
import org.springframework.hateoas.EntityModel
import org.springframework.hateoas.Link
import org.springframework.hateoas.server.mvc.WebMvcLinkBuilder
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.ResponseStatus
import org.springframework.web.bind.annotation.RestController
import reactor.core.publisher.Flux
import reactor.core.publisher.Mono
import javax.validation.Valid

@RestController
@RequestMapping("/api/schedules")
class ScheduleController(
    val scheduleService: ScheduleService
) {

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSchedule(@Valid @RequestBody schedule: ScheduleRequest): Mono<EntityModel<ScheduleResource>> {
        return scheduleService.createSchedule(schedule).map {
            EntityModel.of(it)
                .add(selfLink(it.name))
                .add(collectionLink())
        }
    }

    @DeleteMapping("/{scheduleName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDocument(@PathVariable scheduleName: String): Mono<Void> {
        return scheduleService.deleteSchedule(scheduleName)
    }

    @GetMapping("/{scheduleName}")
    fun findScheduleByName(@PathVariable scheduleName: String): Mono<EntityModel<ScheduleResource>> {
        return scheduleService.findScheduleByName(scheduleName).map {
            EntityModel.of(it)
                .add(selfLink(scheduleName))
                .add(collectionLink())
        }
    }

    @GetMapping
    fun listAllSchedules(): Flux<EntityModel<ScheduleResource>> {
        return scheduleService.listAllSchedules().map {
            EntityModel.of(it)
                .add(selfLink(it.name))
        }
    }

    private fun collectionLink(): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).listAllSchedules()).withRel("collection")
    }

    private fun selfLink(name: String): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).findScheduleByName(name)).withSelfRel()
    }

}
