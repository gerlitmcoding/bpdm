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

import org.eclipse.tractusx.bpdm.common.dto.PageDto
import org.eclipse.tractusx.bpdm.common.exception.BpdmNotFoundException
import org.eclipse.tractusx.bpdm.common.model.StageType
import org.eclipse.tractusx.bpdm.gate.api.model.request.SiteGateInputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.request.SiteGateOutputRequest
import org.eclipse.tractusx.bpdm.gate.api.model.response.SiteGateInputDto
import org.eclipse.tractusx.bpdm.gate.api.model.response.SiteGateOutputResponse
import org.eclipse.tractusx.bpdm.gate.entity.Site
import org.eclipse.tractusx.bpdm.gate.repository.SiteRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Service

@Service
class SiteService(
    private val sitePersistenceService: SitePersistenceService,
    private val siteRepository: SiteRepository,
) {

    fun getSites(page: Int, size: Int, externalIds: Collection<String>? = null): PageDto<SiteGateInputDto> {

        val sitesPage = if (externalIds != null) {
            siteRepository.findByExternalIdInAndStage(externalIds, StageType.Input, PageRequest.of(page, size))
        } else {
            siteRepository.findByStage(StageType.Input, PageRequest.of(page, size))
        }

        return PageDto(
            page = page,
            totalElements = sitesPage.totalElements,
            totalPages = sitesPage.totalPages,
            contentSize = sitesPage.content.size,
            content = toValidSite(sitesPage)
        )
    }

    private fun toValidSite(sitePage: Page<Site>): List<SiteGateInputDto> {
        return sitePage.content.map { site ->
            site.toSiteGateInputResponse(site)
        }
    }

    fun getSiteByExternalId(externalId: String): SiteGateInputDto {
        val siteRecord = siteRepository.findByExternalIdAndStage(externalId, StageType.Input) ?: throw BpdmNotFoundException("Site", externalId)

        return siteRecord.toSiteGateInputResponse(siteRecord)
    }

    /**
     * Get output sites by first fetching sites from the database
     */
    fun getSitesOutput(externalIds: Collection<String>?, page: Int, size: Int): PageDto<SiteGateOutputResponse> {

        val sitePage = if (!externalIds.isNullOrEmpty()) {
            siteRepository.findByExternalIdInAndStage(externalIds, StageType.Output, PageRequest.of(page, size))
        } else {
            siteRepository.findByStage(StageType.Output, PageRequest.of(page, size))
        }

        return PageDto(
            page = page,
            totalElements = sitePage.totalElements,
            totalPages = sitePage.totalPages,
            contentSize = sitePage.content.size,
            content = toValidOutputSites(sitePage),
        )

    }

    private fun toValidOutputSites(sitePage: Page<Site>): List<SiteGateOutputResponse> {
        return sitePage.content.map { sites ->
            sites.toSiteGateOutputResponse(sites)
        }
    }

    /**
     * Upsert sites input to the database
     **/
    fun upsertSites(sites: Collection<SiteGateInputRequest>) {

        sitePersistenceService.persistSitesBP(sites, StageType.Input)

    }

    /**
     * Upsert sites output to the database
     **/
    fun upsertSitesOutput(sites: Collection<SiteGateOutputRequest>) {

        sitePersistenceService.persistSitesOutputBP(sites, StageType.Output)
    }

}