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

package org.eclipse.tractusx.bpdm.pool.config

import org.eclipse.tractusx.bpdm.common.config.BpdmSecurityConfigurerAdapter
import org.eclipse.tractusx.bpdm.common.config.CustomJwtAuthenticationConverter
import org.eclipse.tractusx.bpdm.common.config.SecurityConfigProperties
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpMethod
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.util.matcher.AntPathRequestMatcher.antMatcher

@Configuration
class BpdmSecurityConfigurerAdapterImpl(
    val securityConfigProperties: SecurityConfigProperties
) : BpdmSecurityConfigurerAdapter {

    override fun configure(http: HttpSecurity) {
        http.csrf { it.disable() }
        http.cors {}
        http.sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        http.authorizeHttpRequests {
            it.requestMatchers(antMatcher(HttpMethod.OPTIONS, "/api/**")).permitAll()
            it.requestMatchers(antMatcher("/")).permitAll() // forwards to swagger
            it.requestMatchers(antMatcher("/docs/api-docs/**")).permitAll()
            it.requestMatchers(antMatcher("/ui/swagger-ui/**")).permitAll()
            it.requestMatchers(antMatcher("/actuator/health/**")).permitAll()
            it.requestMatchers(antMatcher("/error")).permitAll()
            it.requestMatchers(antMatcher("/api/**")).authenticated()
        }
        http.oauth2ResourceServer {
            it.jwt { jwt ->
                jwt.jwtAuthenticationConverter(CustomJwtAuthenticationConverter(securityConfigProperties.clientId))
            }
        }
    }
}
