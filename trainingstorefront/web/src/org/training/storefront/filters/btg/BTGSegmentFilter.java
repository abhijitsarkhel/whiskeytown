/**
 *
 */
package org.training.storefront.filters.btg;

import de.hybris.platform.cms2.misc.CMSFilter;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;
import org.training.storefront.filters.btg.support.BTGSegmentStrategy;


/**
 * Filter that evaluates the BTG context for the current request. This is a spring configured filter that is executed by
 * the PlatformFilterChain. The segments are evaluated after the nested chain is executed.
 */
public class BTGSegmentFilter extends OncePerRequestFilter implements CMSFilter
{
	@SuppressWarnings("unused")
	private static final Logger LOG = Logger.getLogger(BTGSegmentFilter.class);

	private BTGSegmentStrategy btgSegmentStrategy;

	@Override
	protected void doFilterInternal(final HttpServletRequest httpRequest, final HttpServletResponse httpResponse,
			final FilterChain filterChain) throws ServletException, IOException
	{
		filterChain.doFilter(httpRequest, httpResponse);
		getBtgSegmentStrategy().evaluateSegment(httpRequest);
	}

	protected BTGSegmentStrategy getBtgSegmentStrategy()
	{
		return btgSegmentStrategy;
	}

	@Required
	public void setBtgSegmentStrategy(final BTGSegmentStrategy btgSegmentStrategy)
	{
		this.btgSegmentStrategy = btgSegmentStrategy;
	}
}
