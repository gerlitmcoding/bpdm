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

package org.eclipse.tractusx.bpdm.gate.service

import mu.KotlinLogging
import org.eclipse.tractusx.bpdm.common.dto.BusinessPartnerType
import org.eclipse.tractusx.bpdm.common.model.StageType
import org.eclipse.tractusx.bpdm.gate.api.exception.ChangeLogOutputError
import org.eclipse.tractusx.bpdm.gate.api.model.response.ChangelogGateDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.ErrorInfo
import org.eclipse.tractusx.bpdm.gate.api.model.response.PageChangeLogDto
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository.Specs.byBusinessPartnerTypes
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository.Specs.byCreatedAtGreaterThan
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository.Specs.byExternalIdsIn
import org.eclipse.tractusx.bpdm.gate.repository.ChangelogRepository.Specs.byStage
import org.springframework.data.domain.PageRequest
import org.springframework.data.jpa.domain.Specification
import org.springframework.stereotype.Service
import java.time.Instant

@Service
class ChangelogService(private val changelogRepository: ChangelogRepository) {

    private val logger = KotlinLogging.logger { }

    fun getChangeLogEntries(
        externalIds: Set<String>?,
        businessPartnerTypes: Set<BusinessPartnerType>?,
        createdAt: Instant?,
        stage: StageType?,
        page: Int,
        pageSize: Int
    ): PageChangeLogDto<ChangelogGateDto> {

        logger.debug { "Executing getChangeLogEntries() with parameters $externalIds // $businessPartnerTypes // $stage // $createdAt" }

        val nonNullExternalIds = externalIds ?: emptySet()

        val spec = Specification.allOf(
            byExternalIdsIn(externalIds = nonNullExternalIds),
            byCreatedAtGreaterThan(createdAt = createdAt),
            byBusinessPartnerTypes(businessPartnerTypes),
            byStage(stage)
        )

        val pageable = PageRequest.of(page, pageSize)
        val pageResponse = changelogRepository.findAll(spec, pageable)
        val setDistinctList = changelogRepository.findExternalIdsInListDistinct(nonNullExternalIds)


        val pageDto = pageResponse.map {
            it.toGateDto()
        }

        val errorList = (nonNullExternalIds - setDistinctList).map {
            ErrorInfo(
                ChangeLogOutputError.ExternalIdNotFound,
                "$it not found",
                it
            )
        }


        return PageChangeLogDto(
            page = page, totalElements = pageDto.totalElements,
            totalPages = pageDto.totalPages,
            contentSize = pageDto.content.size,
            content = pageDto.content,
            invalidEntries = errorList.size,
            errors = errorList
        )
    }

}