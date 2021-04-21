package io.jrb.labs.irigctrlms.rest

import io.jrb.labs.irigctrlms.resource.SensorRequest
import io.jrb.labs.irigctrlms.resource.SensorResource
import io.jrb.labs.irigctrlms.service.SensorService
import mu.KotlinLogging
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
@RequestMapping("/api/sensors")
class SensorController(
    val sensorService: SensorService
) {

    private val log = KotlinLogging.logger {}

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    fun createSensor(@Valid @RequestBody sensor: SensorRequest): Mono<EntityModel<SensorResource>> {
        return sensorService.createSensor(sensor).map {
            EntityModel.of(it)
                .add(selfLink(it.name))
                .add(collectionLink())
        }
    }

    @DeleteMapping("/{sensorName}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    fun deleteDocument(@PathVariable sensorName: String): Mono<Void> {
        return sensorService.deleteSensor(sensorName)
    }

    @GetMapping("/{sensorName}")
    fun findSensorByName(@PathVariable sensorName: String): Mono<EntityModel<SensorResource>> {
        return sensorService.findSensorByName(sensorName).map {
            EntityModel.of(it)
                .add(selfLink(sensorName))
                .add(collectionLink())
        }
    }

    @GetMapping
    fun listAllSensors(): Flux<EntityModel<SensorResource>> {
        return sensorService.listAllSensors().map {
            EntityModel.of(it)
                .add(selfLink(it.name))
        }
    }

    private fun collectionLink(): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).listAllSensors()).withRel("collection")
    }

    private fun selfLink(name: String): Link {
        return WebMvcLinkBuilder.linkTo(WebMvcLinkBuilder.methodOn(javaClass).findSensorByName(name)).withSelfRel()
    }

}
