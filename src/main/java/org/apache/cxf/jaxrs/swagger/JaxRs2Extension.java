package org.apache.cxf.jaxrs.swagger;

import io.swagger.jaxrs.ext.AbstractSwaggerExtension;

// just to make the classloaders happy,
// cxf provides it in a bad module which enforces tomee to provide it as well
// should be fixed for cxf 3.2 (issue opened)
// workaround is to provide the file from the app
public class JaxRs2Extension extends AbstractSwaggerExtension {
}
