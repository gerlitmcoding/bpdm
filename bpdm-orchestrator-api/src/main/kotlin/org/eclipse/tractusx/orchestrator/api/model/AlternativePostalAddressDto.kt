/*******************************************************************************
 * Copyright (c) 2021,2023 Contributors to the Eclipse Foundation
 *
 * See the NOTICE file(s) distributed with this work for additional
 * information regarding copyright ownership.
 *
 * This program and the accompanying materials are made available under the
 * terms of the Apache License, Version 2.0 which is available at
 * https://www.apache.org/licenses/LICENSE-2.0.
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations
 * under the License.
 *
 * SPDX-License-Identifier: Apache-2.0
 ******************************************************************************/

package org.eclipse.tractusx.orchestrator.api.model

import com.neovisionaries.i18n.CountryCode
import org.eclipse.tractusx.bpdm.common.dto.GeoCoordinateDto
import org.eclipse.tractusx.bpdm.common.dto.IBaseAlternativePostalAddressDto
import org.eclipse.tractusx.bpdm.common.model.DeliveryServiceType

data class AlternativePostalAddressDto(
    override val geographicCoordinates: GeoCoordinateDto? = null,
    override val country: CountryCode? = null,
    override val administrativeAreaLevel1: String? = null,
    override val postalCode: String? = null,
    override val city: String? = null,
    override val deliveryServiceType: DeliveryServiceType? = null,
    override val deliveryServiceQualifier: String? = null,
    override val deliveryServiceNumber: String? = null

) : IBaseAlternativePostalAddressDto {
    override fun adminLevel1Key(): String? {
        return administrativeAreaLevel1
    }
    override fun countryCode(): CountryCode? {
        return country
    }
}
