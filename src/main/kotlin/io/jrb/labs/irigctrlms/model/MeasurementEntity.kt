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
package io.jrb.labs.irigctrlms.model

import io.jrb.labs.common.model.Entity
import org.springframework.data.annotation.Id
import org.springframework.data.relational.core.mapping.Column
import org.springframework.data.relational.core.mapping.Table
import java.time.Instant

@Table(value = "t_measurement")
data class MeasurementEntity(

    @Id
    @Column(value = "me_id")
    override val id: Long? = null,

    @Column(value = "me_se_id")
    val sensorId: Long,

    @Column(value = "me_timestamp")
    val timestamp: Instant,

    @Column(value = "me_type")
    val type: String,

    @Column(value = "me_value")
    val value: Double,

    @Column(value = "me_units")
    val units: String
    
) : Entity
