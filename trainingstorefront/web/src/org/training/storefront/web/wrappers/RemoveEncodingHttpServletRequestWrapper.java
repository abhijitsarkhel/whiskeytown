/**
 *
 */
package org.training.storefront.web.wrappers;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;

import org.apache.commons.lang.StringUtils;


/**
 * @author abhijit.s
 *
 */
public class RemoveEncodingHttpServletRequestWrapper extends HttpServletRequestWrapper
{

	private final String pattern;

	public RemoveEncodingHttpServletRequestWrapper(final HttpServletRequest request, final String pattern)
	{
		super(request);
		this.pattern = pattern;
	}

	@Override
	public String getContextPath()
	{
		return StringUtils.remove(super.getContextPath(), pattern);
	}

}
