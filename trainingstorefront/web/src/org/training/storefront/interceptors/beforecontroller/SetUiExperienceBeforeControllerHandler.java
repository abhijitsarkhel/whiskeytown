/**
 *
 */
package org.training.storefront.interceptors.beforecontroller;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.enumeration.EnumerationService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.method.HandlerMethod;
import org.training.storefront.interceptors.BeforeControllerHandler;


/**
 * @author abhijit.s
 *
 */
public class SetUiExperienceBeforeControllerHandler implements BeforeControllerHandler
{
	private static final Logger LOG = Logger.getLogger(SetUiExperienceBeforeControllerHandler.class);

	public static final String DEFAULT_UI_EXPERIENCE_LEVEL_PARAM = "uiel";

	@Resource(name = "uiExperienceService")
	private UiExperienceService uiExperienceService;

	@Resource(name = "enumerationService")
	private EnumerationService enumerationService;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		if (isGetMethod(request))
		{
			final String uiExperienceLevelParam = request.getParameter(DEFAULT_UI_EXPERIENCE_LEVEL_PARAM);
			if (StringUtils.isNotBlank(uiExperienceLevelParam))
			{
				try
				{
					final UiExperienceLevel uiExperienceLevel = enumerationService.getEnumerationValue(UiExperienceLevel.class,
							uiExperienceLevelParam);
					uiExperienceService.setOverrideUiExperienceLevel(uiExperienceLevel);

				}
				catch (final UnknownIdentifierException ile)
				{
					LOG.warn("Can not change uiExperienceLevel [" + uiExperienceLevelParam + "]. " + ile.getMessage());
					if (LOG.isDebugEnabled())
					{
						LOG.debug("Exception changing UiExperienceLevel", ile);
					}
				}
			}
		}
		return true;
	}

	protected boolean isGetMethod(final HttpServletRequest request)
	{
		return RequestMethod.GET.name().equalsIgnoreCase(request.getMethod());
	}
}
