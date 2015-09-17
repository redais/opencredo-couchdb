/*
 * Copyright 2011 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.opencredo.couchdb.config;

import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DATABASE_URL_ATTRIBUTE;
import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DOCUMENT_ID_EXPRESSION_ATTRIBUTE;
import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DOCUMENT_ID_EXPRESSION_PROPERTY;
import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE;
import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_EXCEPTIONS_COUNTER;

import org.opencredo.couchdb.outbound.CouchDbSendingMessageHandler;
import org.springframework.beans.factory.support.AbstractBeanDefinition;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractOutboundChannelAdapterParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

/**
 * BeanDefinitionParser for the outbound-channel-adapter element.
 * @author Tareq Abedrabbo (tareq.abedrabbo@opencredo.com)
 * @since 17/01/2011
 */
public class CouchDbOutboundChannelAdapterParser extends AbstractOutboundChannelAdapterParser {

    @Override
    protected AbstractBeanDefinition parseConsumer(Element element, ParserContext parserContext) {
        BeanDefinitionBuilder builder = BeanDefinitionBuilder.genericBeanDefinition(CouchDbSendingMessageHandler.class);
        String databaseUrl = element.getAttribute(COUCHDB_DATABASE_URL_ATTRIBUTE);
        String documentOperations = element.getAttribute(COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE);
        String documentIdExpression = element.getAttribute(COUCHDB_DOCUMENT_ID_EXPRESSION_ATTRIBUTE);
        String exeptionsCounter = element.getAttribute(COUCHDB_EXCEPTIONS_COUNTER);

        if (StringUtils.hasText(databaseUrl)) {
            if (StringUtils.hasText(documentOperations)) {
                parserContext.getReaderContext().error(
                        "At most one of '" + COUCHDB_DATABASE_URL_ATTRIBUTE + "' and '" +
                                COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE + "' may be provided.", element);
            } else {
                builder.addConstructorArgValue(databaseUrl);
            }
        } else if (StringUtils.hasText(documentOperations)) {
            builder.addConstructorArgReference(documentOperations);
        } else {
            parserContext.getReaderContext().error(
                    "Either '" + COUCHDB_DATABASE_URL_ATTRIBUTE + "' or '" +
                            COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE + "' must be provided.", element);
        }

        if (StringUtils.hasText(documentIdExpression)) {
            builder.addPropertyValue(COUCHDB_DOCUMENT_ID_EXPRESSION_PROPERTY, documentIdExpression);
        }
        
        if (StringUtils.hasText(exeptionsCounter)) {
        	builder.addPropertyReference("counter", exeptionsCounter);
        }

        return builder.getBeanDefinition();
    }
}
