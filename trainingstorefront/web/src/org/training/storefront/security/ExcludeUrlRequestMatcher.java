/**
 *
 */
package org.training.storefront.security;

import java.util.HashSet;
import java.util.Set;

import javax.servlet.http.HttpServletRequest;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.web.util.matcher.RequestMatcher;
import org.springframework.util.PathMatcher;


/**
 * @author abhijit.s
 *
 */
public class ExcludeUrlRequestMatcher implements RequestMatcher
{
	private static final Logger LOG = Logger.getLogger(ExcludeUrlRequestMatcher.class);
	private Set<String> excludeUrlSet;
	private PathMatcher pathMatcher;

	@Override
	public boolean matches(final HttpServletRequest request)
	{
		for (final String excludeUrl : getExcludeUrlSet())
		{
			if (getPathMatcher().match(excludeUrl, request.getServletPath()))
			{
				return false;
			}
		}
		return true;
	}

	protected Set<String> getExcludeUrlSet()
	{
		return excludeUrlSet;
	}

	protected PathMatcher getPathMatcher()
	{
		return pathMatcher;
	}

	@Required
	public void setExcludeUrlSet(final Set<String> excludeUrlSet)
	{
		final Set<String> validUrls = new HashSet<String>();
		for (final String url : excludeUrlSet)
		{
			if (url.charAt(0) == '/')
			{
				validUrls.add(url);
			}
			else
			{
				LOG.warn("Ignoring ExcludeUrl [" + url + "] as it is not valid");
			}
		}
		this.excludeUrlSet = validUrls;
	}

	@Required
	public void setPathMatcher(final PathMatcher pathMatcher)
	{
		this.pathMatcher = pathMatcher;
	}

}
