/**
 *
 */
package org.training.storefront.filters.btg;

import de.hybris.platform.btg.events.AbstractBTGRuleDataEvent;
import de.hybris.platform.btg.events.RequestParametersUsedBTGRuleDataEvent;

import java.util.Map;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.MapUtils;


/**
 * FilterBean to produce the request scoped BTG event {@link RequestParametersUsedBTGRuleDataEvent} This is a spring
 * configured filter that is executed by the PlatformFilterChain.
 */
public class RequestParamsBtgFilter extends AbstractBtgFilter
{
	@Override
	protected AbstractBTGRuleDataEvent getEvent(final HttpServletRequest request)
	{
		RequestParametersUsedBTGRuleDataEvent result = null;
		final Map<String, String[]> params = request.getParameterMap();
		if (!MapUtils.isEmpty(params))
		{
			result = new RequestParametersUsedBTGRuleDataEvent(params);
		}
		return result;
	}

	@Override
	protected boolean isRequestScoped()
	{
		return true;
	}
}
