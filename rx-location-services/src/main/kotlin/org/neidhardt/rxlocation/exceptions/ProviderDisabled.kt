/*
 * SPDX-FileCopyrightText: 2021 Eric Neidhardt
 * SPDX-License-Identifier: MIT
 */
package org.neidhardt.rxlocation.exceptions

/**
 * [ProviderDisabled] indicates the the user has disabled one of the location providers,
 * used for location updates.
 *
 * @param message - detailed explanation of error cause
 */
class ProviderDisabled(message: String?) : Throwable(message, null)