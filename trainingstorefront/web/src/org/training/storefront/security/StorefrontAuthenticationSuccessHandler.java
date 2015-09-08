/**
 *
 */
package org.training.storefront.security;

import de.hybris.platform.acceleratorservices.uiexperience.UiExperienceService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commerceservices.enums.UiExperienceLevel;
import de.hybris.platform.commerceservices.order.CommerceCartRestorationException;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.Map;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.Authentication;
import org.springframework.security.web.authentication.SavedRequestAwareAuthenticationSuccessHandler;


/**
 * @author abhijit.s
 *
 */
public class StorefrontAuthenticationSuccessHandler extends SavedRequestAwareAuthenticationSuccessHandler
{
	private CustomerFacade customerFacade;
	private UiExperienceService uiExperienceService;
	private CartFacade cartFacade;
	private SessionService sessionService;
	private BruteForceAttackCounter bruteForceAttackCounter;
	private Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel;

	@Override
	public void onAuthenticationSuccess(final HttpServletRequest request, final HttpServletResponse response,
			final Authentication authentication) throws ServletException, IOException
	{
		getCustomerFacade().loginSuccess();
		if (!getCartFacade().hasSessionCart() || getCartFacade().getSessionCart().getEntries().isEmpty())
		{
			try
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION, getCartFacade().restoreSavedCart(null));
			}
			catch (final CommerceCartRestorationException e)
			{
				getSessionService().setAttribute(WebConstants.CART_RESTORATION, "basket.restoration.errorMsg");
			}
		}
		getBruteForceAttackCounter().resetUserCounter(getCustomerFacade().getCurrentCustomer().getUid());
		super.onAuthenticationSuccess(request, response, authentication);
	}

	@Override
	protected boolean isAlwaysUseDefaultTargetUrl()
	{
		final UiExperienceLevel uiExperienceLevel = getUiExperienceService().getUiExperienceLevel();
		if (getForceDefaultTargetForUiExperienceLevel().containsKey(uiExperienceLevel))
		{
			return Boolean.TRUE.equals(getForceDefaultTargetForUiExperienceLevel().get(uiExperienceLevel));
		}
		else
		{
			return false;
		}
	}

	protected CartFacade getCartFacade()
	{
		return cartFacade;
	}

	@Required
	public void setCartFacade(final CartFacade cartFacade)
	{
		this.cartFacade = cartFacade;
	}

	protected SessionService getSessionService()
	{
		return sessionService;
	}

	@Required
	public void setSessionService(final SessionService sessionService)
	{
		this.sessionService = sessionService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}

	protected Map<UiExperienceLevel, Boolean> getForceDefaultTargetForUiExperienceLevel()
	{
		return forceDefaultTargetForUiExperienceLevel;
	}

	@Required
	public void setForceDefaultTargetForUiExperienceLevel(
			final Map<UiExperienceLevel, Boolean> forceDefaultTargetForUiExperienceLevel)
	{
		this.forceDefaultTargetForUiExperienceLevel = forceDefaultTargetForUiExperienceLevel;
	}

	protected BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	protected UiExperienceService getUiExperienceService()
	{
		return uiExperienceService;
	}

	@Required
	public void setUiExperienceService(final UiExperienceService uiExperienceService)
	{
		this.uiExperienceService = uiExperienceService;
	}

}
