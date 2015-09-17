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

import org.opencredo.couchdb.transformer.CouchDbUrlToDocumentTransformer;
import org.springframework.beans.factory.support.BeanDefinitionBuilder;
import org.springframework.beans.factory.xml.ParserContext;
import org.springframework.integration.config.xml.AbstractTransformerParser;
import org.springframework.util.StringUtils;
import org.w3c.dom.Element;

import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE;
import static org.opencredo.couchdb.config.CouchDbAdapterParserUtils.COUCHDB_DOCUMENT_TYPE_ATTRIBUTE;

/**
 * BeanDefinitionParser for the uri-to-document-transformer element.
 * @author Tareq Abedrabbo
 * @since 01/02/2011
 */
public class CouchDbUrlToDocumentTransformerParser extends AbstractTransformerParser {
    @Override
    protected String getTransformerClassName() {
        return CouchDbUrlToDocumentTransformer.class.getName();
    }

    @Override
    protected void parseTransformer(Element element, ParserContext parserContext, BeanDefinitionBuilder builder) {
    	System.out.println("CouchDbUrlToDocumentTransformerParser2");
        String documentType = element.getAttribute(COUCHDB_DOCUMENT_TYPE_ATTRIBUTE);
        String documentOperations = element.getAttribute(COUCHDB_DOCUMENT_OPERATIONS_ATTRIBUTE);

        if (!StringUtils.hasText(documentType)) {
            parserContext.getReaderContext().error("The '" + COUCHDB_DOCUMENT_TYPE_ATTRIBUTE +
                    "' is mandatory.", parserContext.extractSource(element));
        } else {
            builder.addConstructorArgValue(documentType);
        }

        if (StringUtils.hasText(documentOperations)) {
            builder.addConstructorArgReference(documentOperations);
        }
    }
}
