/**
 *
 */
package org.training.storefront.filters.btg;

import de.hybris.platform.btg.events.AbstractBTGRuleDataEvent;
import de.hybris.platform.btg.events.ProductVisitedBTGRuleDataEvent;

/**
 * @author abhijit.s
 *
 */
public class ProductVisitedBtgFilter extends AbstractPkResolvingBtgFilter
{
	@Override
	protected AbstractBTGRuleDataEvent internalGetEvent(final String pk)
	{
		return new ProductVisitedBTGRuleDataEvent(pk);
	}
}
