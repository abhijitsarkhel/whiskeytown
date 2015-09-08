/**
 *
 */
package org.training.storefront.filters.btg.support.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.servicelayer.exceptions.SystemException;

import javax.servlet.http.HttpServletRequest;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.training.storefront.filters.btg.support.PkResolvingStrategy;
import org.training.storefront.filters.btg.support.UrlParsingStrategy;


/**
 * @author abhijit.s
 *
 */
public abstract class AbstractParsingPkResolvingStrategy implements PkResolvingStrategy
{
	private static final Logger LOG = Logger.getLogger(AbstractParsingPkResolvingStrategy.class);
	private UrlParsingStrategy urlParsingStrategy;

	/**
	 * @param urlParsingStrategy
	 *           the urlParsingStrategy to set
	 */
	public void setUrlParsingStrategy(final UrlParsingStrategy urlParsingStrategy)
	{
		this.urlParsingStrategy = urlParsingStrategy;
	}

	@Override
	public String resolvePrimaryKey(final HttpServletRequest request)
	{
		String result = null;
		final String key = urlParsingStrategy.parse(request);
		if (!StringUtils.isBlank(key))
		{
			try
			{
				final ItemModel model = retrieveModel(key);
				result = model.getPk().getLongValueAsString();
			}
			catch (final SystemException e)
			{
				LOG.warn("Could not retrieve category for " + key + ": " + e.toString());
			}
		}
		return result;
	}

	/**
	 * Retrieves the model searching by key
	 *
	 * @param key
	 *           the key
	 * @return model
	 * @throws de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException
	 *            if the model could not be found
	 * @throws de.hybris.platform.servicelayer.exceptions.AmbiguousIdentifierException
	 *            if the model cannot be uniquely identified
	 */
	protected abstract ItemModel retrieveModel(String key);
}
