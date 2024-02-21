package com.iambenbradley.p151.model

import com.iambenbradley.p151.model.domain.PokeColor
import com.iambenbradley.p151.model.domain.Type
import com.iambenbradley.p151.model.serial.SerialColor
import com.iambenbradley.p151.model.serial.SerialType
import javax.inject.Inject

class PokeMapper @Inject constructor() {

    fun serialToDomainColor(serial: SerialColor): PokeColor {
        return PokeColor.values().firstOrNull { color ->
            color.serialName == serial.name
        } ?: PokeColor.Unknown
    }

    fun serialToDomainType(serial: SerialType): Type {
        return Type.values().firstOrNull { type ->
            type.serialName == serial.name
        } ?: Type.Unknown
    }


}