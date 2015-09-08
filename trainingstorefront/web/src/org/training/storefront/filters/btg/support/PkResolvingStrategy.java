/**
 *
 */
package org.training.storefront.filters.btg.support;

import javax.servlet.http.HttpServletRequest;

/**
 * @author abhijit.s
 *
 */
public interface PkResolvingStrategy
{
	/**
	 * Retrieve a primary key as {@link String}
	 *
	 * @param request
	 *           the request
	 * @return the primary key or null, if no key can be retrieved
	 */
	String resolvePrimaryKey(HttpServletRequest request);
}
