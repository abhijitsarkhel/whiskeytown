/**
 *
 */
package org.training.storefront.interceptors.beforeview;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.pages.AbstractPageController;
import de.hybris.platform.btg.events.ContentPageVisitedBTGRuleDataEvent;
import de.hybris.platform.cms2.model.pages.AbstractPageModel;
import de.hybris.platform.servicelayer.event.EventService;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.log4j.Logger;
import org.springframework.web.servlet.ModelAndView;
import org.training.storefront.interceptors.BeforeViewHandler;


/**
 * @author abhijit.s
 *
 */
public class BtgPageVisitedPublisherBeforeViewHandler implements BeforeViewHandler
{
	private static final Logger LOG = Logger.getLogger(BtgPageVisitedPublisherBeforeViewHandler.class);

	@Resource(name = "eventService")
	private EventService eventService;

	@Override
	public void beforeView(final HttpServletRequest request, final HttpServletResponse response, final ModelAndView modelAndView)
	{
		final AbstractPageModel page = (AbstractPageModel) modelAndView.getModel().get(AbstractPageController.CMS_PAGE_MODEL);
		if (page != null && page.getPk() != null)
		{
			try
			{
				eventService.publishEvent(new ContentPageVisitedBTGRuleDataEvent(page.getPk().getLongValueAsString()));
			}
			catch (final Exception e)
			{
				LOG.error("Could not publish event", e);
			}
		}
	}
}
