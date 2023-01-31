/*******************************************************************************
 * Copyright (c) 2021,2022 Contributors to the Eclipse Foundation
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

package org.eclipse.tractusx.bpdm.common.config

import io.swagger.v3.oas.models.Components
import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Info
import io.swagger.v3.oas.models.security.*
import mu.KotlinLogging
import org.springdoc.core.customizers.OpenApiCustomizer
import org.springdoc.core.models.GroupedOpenApi
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration


@Configuration
class OpenApiConfig(
    val securityProperties: SecurityConfigProperties,
    val infoProperties: AppInfoProperties
) {
    private val logger = KotlinLogging.logger { }

    @Bean
    fun customOpenAPI(): OpenAPI? {
        return OpenAPI().info(Info().title(infoProperties.name).description(infoProperties.description).version(infoProperties.version))
            .also { if (securityProperties.enabled) it.withSecurity() }
    }

    @Bean
    fun openApiDefinition(): GroupedOpenApi {
        return GroupedOpenApi.builder()
            .group("docs")
            .pathsToMatch("/**")
            .displayName("Docs")
            .addOpenApiCustomizer(sortSchemaCustomiser())
            .build()
    }

    fun sortSchemaCustomiser(): OpenApiCustomizer {
        return OpenApiCustomizer { openApi: OpenAPI ->
            openApi.components(with(openApi.components) { schemas(schemas.values.sortedBy { it.name }.associateBy { it.name }) })
        }
    }

    private fun OpenAPI.withSecurity(): OpenAPI {
        logger.info { "Add security schemes to OpenAPI definition" }
        return this.components(
            Components()
                .addSecuritySchemes(
                    "open_id_scheme",
                    SecurityScheme().type(SecurityScheme.Type.OAUTH2).flows(
                        OAuthFlows().authorizationCode(
                            OAuthFlow().authorizationUrl(securityProperties.authUrl)
                                .tokenUrl(securityProperties.tokenUrl)
                                .refreshUrl(securityProperties.refreshUrl)
                                .scopes(Scopes())
                        )
                    )
                )
        )
            .addSecurityItem(SecurityRequirement().addList("open_id_scheme", emptyList()))
    }
}
