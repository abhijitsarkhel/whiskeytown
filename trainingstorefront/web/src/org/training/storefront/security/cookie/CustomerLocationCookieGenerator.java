/**
 *
 */
package org.training.storefront.security.cookie;

import de.hybris.platform.site.BaseSiteService;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;


/**
 * @author abhijit.s
 *
 */
public class CustomerLocationCookieGenerator extends EnhancedCookieGenerator
{
	public static final String LOCATION_SEPARATOR = "|";
	public static final String LATITUDE_LONGITUDE_SEPARATOR = ",";

	private BaseSiteService baseSiteService;

	@Override
	public String getCookieName()
	{
		return StringUtils.deleteWhitespace(getBaseSiteService().getCurrentBaseSite().getUid()) + "-customerLocation";
	}

	protected BaseSiteService getBaseSiteService()
	{
		return baseSiteService;
	}

	@Required
	public void setBaseSiteService(final BaseSiteService baseSiteService)
	{
		this.baseSiteService = baseSiteService;
	}
}
