/**
 *
 */
package org.training.storefront.security;

import de.hybris.platform.acceleratorservices.urlencoder.UrlEncoderService;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.commercefacades.customer.CustomerFacade;
import de.hybris.platform.commercefacades.storesession.StoreSessionFacade;
import de.hybris.platform.commerceservices.security.SecureToken;
import de.hybris.platform.commerceservices.security.SecureTokenService;
import de.hybris.platform.commerceservices.strategies.CheckoutCustomerStrategy;
import de.hybris.platform.servicelayer.exceptions.SystemException;
import de.hybris.platform.servicelayer.i18n.CommonI18NService;
import de.hybris.platform.servicelayer.user.UserService;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.RememberMeAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.rememberme.InvalidCookieException;
import org.springframework.security.web.authentication.rememberme.TokenBasedRememberMeServices;
import org.training.storefront.web.wrappers.RemoveEncodingHttpServletRequestWrapper;


/**
 * @author abhijit.s
 *
 */
public class TrainingRememberMeServices extends TokenBasedRememberMeServices
{

	private UserService userService;
	private CheckoutCustomerStrategy checkoutCustomerStrategy;
	private SecureTokenService secureTokenService;
	private UrlEncoderService urlEncoderService;
	private CustomerFacade customerFacade;
	private StoreSessionFacade storeSessionFacade;
	private CommonI18NService commonI18NService;

	public TrainingRememberMeServices(final String key, final UserDetailsService userDetailsService)
	{
		super(key, userDetailsService);
	}

	@Override
	protected void setCookie(final String[] tokens, final int maxAge, final HttpServletRequest request,
			final HttpServletResponse response)
	{
		if (!getCheckoutCustomerStartegy().isAnonymousCheckout())
		{
			super.setCookie(tokens, maxAge, new RemoveEncodingHttpServletRequestWrapper(request, getUrlEncodingPattern(request)),
					response);
		}
	}

	@Override
	protected String encodeCookie(final String[] cookieTokens)
	{
		return getSecureTokenService().encryptData(new SecureToken(super.encodeCookie(cookieTokens), System.currentTimeMillis()));
	}

	@Override
	protected String[] decodeCookie(final String cookieValue)
	{
		try
		{
			return super.decodeCookie(getSecureTokenService().decryptData(cookieValue).getData());
		}
		catch (final SystemException | IllegalArgumentException e)
		{
			throw new InvalidCookieException("Cookie value was not encrypted; value was '" + cookieValue + "'");
		}
	}

	@Override
	public void logout(final HttpServletRequest request, final HttpServletResponse response, final Authentication authentication)
	{
		super.logout(new RemoveEncodingHttpServletRequestWrapper(request, getUrlEncodingPattern(request)), response, authentication);
	}

	@Override
	protected Authentication createSuccessfulAuthentication(final HttpServletRequest request, final UserDetails user)
	{
		getUserService().setCurrentUser(getUserService().getUserForUID(user.getUsername()));
		if (StringUtils.isNotEmpty(getUrlEncoderService().getUrlEncodingPattern()))
		{
			getCustomerFacade().rememberMeLoginSuccessWithUrlEncoding(getUrlEncoderService().isLanguageEncodingEnabled(),
					getUrlEncoderService().isCurrencyEncodingEnabled());
		}
		else
		{
			getCustomerFacade().loginSuccess();
		}
		final RememberMeAuthenticationToken auth = new RememberMeAuthenticationToken(getKey(), user, user.getAuthorities());
		auth.setDetails(getAuthenticationDetailsSource().buildDetails(request));
		return auth;
	}

	@Override
	protected String makeTokenSignature(final long tokenExpiryTime, final String username, final String password)
	{
		return getUserService().getUserForUID(username).getPk().getLongValueAsString();
	}

	@Override
	protected String retrievePassword(final Authentication authentication)
	{
		return getUserService().getUserForUID(authentication.getPrincipal().toString()).getEncodedPassword();
	}

	protected CheckoutCustomerStrategy getCheckoutCustomerStartegy()
	{
		return checkoutCustomerStrategy;
	}

	protected String getUrlEncodingPattern(final HttpServletRequest request)
	{
		final String encodingAttributes = (String) request.getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
		return StringUtils.defaultString(encodingAttributes);
	}

	protected SecureTokenService getSecureTokenService()
	{
		return secureTokenService;
	}

	protected UserService getUserService()
	{
		return userService;
	}

	protected UrlEncoderService getUrlEncoderService()
	{
		return urlEncoderService;
	}

	protected CustomerFacade getCustomerFacade()
	{
		return customerFacade;
	}

	/**
	 * @return the checkoutCustomerStrategy
	 */
	public CheckoutCustomerStrategy getCheckoutCustomerStrategy()
	{
		return checkoutCustomerStrategy;
	}

	/**
	 * @param checkoutCustomerStrategy
	 *           the checkoutCustomerStrategy to set
	 */
	@Required
	public void setCheckoutCustomerStrategy(final CheckoutCustomerStrategy checkoutCustomerStrategy)
	{
		this.checkoutCustomerStrategy = checkoutCustomerStrategy;
	}

	/**
	 * @return the storeSessionFacade
	 */
	public StoreSessionFacade getStoreSessionFacade()
	{
		return storeSessionFacade;
	}

	/**
	 * @param storeSessionFacade
	 *           the storeSessionFacade to set
	 */
	@Required
	public void setStoreSessionFacade(final StoreSessionFacade storeSessionFacade)
	{
		this.storeSessionFacade = storeSessionFacade;
	}

	/**
	 * @return the commonI18NService
	 */
	public CommonI18NService getCommonI18NService()
	{
		return commonI18NService;
	}

	/**
	 * @param commonI18NService
	 *           the commonI18NService to set
	 */
	@Required
	public void setCommonI18NService(final CommonI18NService commonI18NService)
	{
		this.commonI18NService = commonI18NService;
	}

	/**
	 * @param userService
	 *           the userService to set
	 */
	@Required
	public void setUserService(final UserService userService)
	{
		this.userService = userService;
	}

	/**
	 * @param secureTokenService
	 *           the secureTokenService to set
	 */
	@Required
	public void setSecureTokenService(final SecureTokenService secureTokenService)
	{
		this.secureTokenService = secureTokenService;
	}

	/**
	 * @param urlEncoderService
	 *           the urlEncoderService to set
	 */
	@Required
	public void setUrlEncoderService(final UrlEncoderService urlEncoderService)
	{
		this.urlEncoderService = urlEncoderService;
	}

	/**
	 * @param customerFacade
	 *           the customerFacade to set
	 */
	@Required
	public void setCustomerFacade(final CustomerFacade customerFacade)
	{
		this.customerFacade = customerFacade;
	}


}
