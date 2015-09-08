/**
 *
 */
package org.training.storefront.filters.btg;

import de.hybris.platform.btg.events.AbstractBTGRuleDataEvent;
import de.hybris.platform.btg.events.CategoryVisitedBTGRuleDataEvent;

/**
 * FilterBean to produce the BTG event {@link CategoryVisitedBTGRuleDataEvent} This is a spring configured filter that
 * is executed by the PlatformFilterChain.
 */
public class CategoryVisitedBtgFilter extends AbstractPkResolvingBtgFilter
{
	@Override
	protected AbstractBTGRuleDataEvent internalGetEvent(final String pk)
	{
		return new CategoryVisitedBTGRuleDataEvent(pk);
	}
}
