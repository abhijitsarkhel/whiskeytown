/**
 *
 */
package org.training.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorservices.config.SiteConfigService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.servlet.ModelAndView;
import org.training.storefront.interceptors.BeforeViewHandler;


/**
 * @author abhijit.s
 *
 */
public class ConfigGranuleBeforeViewHandler implements BeforeViewHandler
{
	private SiteConfigService siteConfigService;

	protected SiteConfigService getSiteConfigService()
	{
		return siteConfigService;
	}

	@Required
	public void setSiteConfigService(final SiteConfigService siteConfigService)
	{
		this.siteConfigService = siteConfigService;
	}

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
			throws Exception
	{
		modelAndView.addObject("granuleEnabled",
				Boolean.valueOf(getSiteConfigService().getBoolean("storefront.granule.enabled", false)));
	}
}
