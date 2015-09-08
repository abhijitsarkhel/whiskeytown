/**
 *
 */
package org.training.storefront.interceptors.beforeview;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.training.storefront.filters.btg.support.BTGSegmentStrategy;
import org.training.storefront.interceptors.BeforeViewHandler;

/**
 * @author abhijit.s
 *
 */
public class BtgSegmentBeforeViewHandler implements BeforeViewHandler
{
	private BTGSegmentStrategy btgSegmentStrategy;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		getBtgSegmentStrategy().evaluateSegment(request);
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
