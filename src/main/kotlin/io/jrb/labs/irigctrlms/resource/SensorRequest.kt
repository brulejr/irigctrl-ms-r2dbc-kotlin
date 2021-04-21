package io.jrb.labs.irigctrlms.resource

import javax.validation.constraints.NotBlank
import javax.validation.constraints.Size

data class SensorRequest(

    @field:NotBlank(message = "Name is mandatory")
    @field:Size(min = 8, max = 64, message = "Name must be between 8 and 64 characters")
    val name: String,

    @field:NotBlank(message = "FriendlyName is mandatory")
    @field:Size(min = 8, max = 64, message = "Friendly Name must be between 8 and 64 characters")
    val friendlyName: String

)
