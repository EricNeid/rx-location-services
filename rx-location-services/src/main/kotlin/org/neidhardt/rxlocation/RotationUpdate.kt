package org.neidhardt.rxlocation

import java.lang.IllegalArgumentException

/**
 * Created by neid_ei (eric.neidhardt@dlr.de)
 * on 06.05.2019.
 */
data class RotationUpdate(val accuracyUpdate: Int, val rotation: Int)