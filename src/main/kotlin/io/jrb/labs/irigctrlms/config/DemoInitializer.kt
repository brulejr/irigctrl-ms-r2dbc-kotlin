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
package io.jrb.labs.irigctrlms.config

import io.jrb.labs.irigctrlms.resource.SensorRequest
import io.jrb.labs.irigctrlms.service.SensorService
import mu.KotlinLogging
import org.springframework.boot.context.event.ApplicationReadyEvent
import org.springframework.context.ApplicationListener
import reactor.core.publisher.Flux
import java.time.Duration

class DemoInitializer(
    private val sensorService: SensorService
) : ApplicationListener<ApplicationReadyEvent> {

    private val log = KotlinLogging.logger {}

    override fun onApplicationEvent(event: ApplicationReadyEvent) {
        createSensors()
    }

    private fun createSensors() {
        log.info("Creating Sensors")
        Flux.fromIterable(listOf(
            SensorRequest(name = "sensor1", friendlyName = "Sensor One"),
            SensorRequest(name = "sensor2", friendlyName = "Sensor Two")
        ))
            .flatMap { sensorService.createSensor(it) }
            .doOnComplete { log.info("--- Sensors created") }
            .blockLast(Duration.ofSeconds(3))
    }

}
