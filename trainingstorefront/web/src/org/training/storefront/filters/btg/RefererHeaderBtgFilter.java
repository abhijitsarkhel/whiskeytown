/**
 *
 */
package org.training.storefront.filters.btg;

import de.hybris.platform.btg.events.AbstractBTGRuleDataEvent;
import de.hybris.platform.btg.events.RefererHeaderUsedBTGRuleDataEvent;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;


/**
 * @author abhijit.s
 *
 */
public class RefererHeaderBtgFilter extends AbstractBtgFilter
{
	private static final String REFERER_HEADER_NAME = "Referer";

	@Override
	protected AbstractBTGRuleDataEvent getEvent(final HttpServletRequest request)
	{
		RefererHeaderUsedBTGRuleDataEvent result = null;
		final String referrer = request.getHeader(REFERER_HEADER_NAME);
		if (!StringUtils.isBlank(referrer))
		{
			result = new RefererHeaderUsedBTGRuleDataEvent(referrer);
		}
		return result;
	}

	@Override
	protected boolean isRequestScoped()
	{
		return true;
	}
}
