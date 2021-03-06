/**
 *
 */
package org.training.storefront.web.mvc;

import javax.annotation.Resource;

import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerAdapter;
import org.springframework.web.servlet.mvc.method.annotation.RequestMappingHandlerMapping;
import org.springframework.web.util.UrlPathHelper;

/**
 * This class is a bean post-processor for RequestMappingHandlerMapping. This required to set the property values in the
 * instance of RequestMappingHandlerMapping created by the spring framework when using the tag <annotation-driven/>
 */
public class RequestMappingHandlerAdapterConfigurer
{
	@Resource
	private RequestMappingHandlerMapping requestMappingHandlerMapping;

	@Resource
	private RequestMappingHandlerAdapter requestMappingHandlerAdapter;

	private UrlPathHelper urlPathHelper;

	public void init()
	{
		requestMappingHandlerMapping.setUrlPathHelper(getUrlPathHelper());
		requestMappingHandlerAdapter.setSynchronizeOnSession(true);
	}

	protected UrlPathHelper getUrlPathHelper()
	{
		return urlPathHelper;
	}

	public void setUrlPathHelper(final UrlPathHelper urlPathHelper)
	{
		this.urlPathHelper = urlPathHelper;
	}
}
