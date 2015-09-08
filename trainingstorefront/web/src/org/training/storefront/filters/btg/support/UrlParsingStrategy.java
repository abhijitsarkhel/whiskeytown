/**
 *
 */
package org.training.storefront.filters.btg.support;

import javax.servlet.http.HttpServletRequest;

/**
 * @author abhijit.s
 *
 */
public interface UrlParsingStrategy
{
	/**
	 * Parse the request url to retrieve a key
	 *
	 * @param request
	 *           the request
	 * @return key or null, if no key could be parsed
	 */
	String parse(HttpServletRequest request);
}
