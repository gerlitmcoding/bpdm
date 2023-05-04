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

package com.catenax.bpdm.bridge.dummy.controller

import com.catenax.bpdm.bridge.dummy.service.SyncService
import io.swagger.v3.oas.annotations.Operation
import mu.KotlinLogging
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/bridge")
class BridgeController(
    val syncService: SyncService
) {

    private val logger = KotlinLogging.logger { }

    @Operation(
        summary = "Start sync between Gate and Pool"
    )
    @PostMapping("/sync")
    fun triggerSync() {
        logger.info("Bridge sync started...")
        syncService.sync()
        logger.info("Bridge sync completed")
    }
}