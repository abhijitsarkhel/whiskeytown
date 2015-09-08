/**
 *
 */
package org.training.storefront.security;

import de.hybris.platform.core.Constants;
import de.hybris.platform.core.model.user.UserModel;
import de.hybris.platform.order.CartService;
import de.hybris.platform.servicelayer.exceptions.UnknownIdentifierException;
import de.hybris.platform.servicelayer.model.ModelService;
import de.hybris.platform.servicelayer.user.UserService;
import de.hybris.platform.spring.security.CoreAuthenticationProvider;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;


/**
 * @author abhijit.s
 *
 */
public class TrainingAuthenticationProvider extends CoreAuthenticationProvider
{
	private static final Logger LOG = Logger.getLogger(TrainingAuthenticationProvider.class);
	private static final String ROLE_ADMIN_GROUP = "ROLE_" + Constants.USER.ADMIN_USERGROUP.toUpperCase();

	private BruteForceAttackCounter bruteForceAttackCounter;
	private UserService userService;
	private ModelService modelService;
	private final GrantedAuthority adminAuthority = new SimpleGrantedAuthority(ROLE_ADMIN_GROUP);
	private CartService cartService;

	@SuppressWarnings("finally")
	@Override
	public Authentication authenticate(final Authentication authentication) throws AuthenticationException
	{
		final String username = (authentication.getPrincipal() == null) ? "NONE_PROVIDED" : authentication.getName();
		if (getBruteForceAttackCounter().isAttack(username))
		{
			try
			{
				final UserModel userModel = getUserService().getUserForUID(StringUtils.lowerCase(username));
				userModel.setLoginDisabled(true);
				getModelService().save(userModel);
				bruteForceAttackCounter.resetUserCounter(userModel.getUid());
			}
			catch (final UnknownIdentifierException e)
			{
				LOG.warn("Brute force attack attempt for non existing user name " + username);
			}
			finally
			{
				throw new BadCredentialsException(messages.getMessage("CoreAuthenticationProvider.badCredentials"));
			}
		}
		checkCartForUser(username);
		return super.authenticate(authentication);
	}

	/**
	 * check if the user of the cart matches the current user and if the user is not anonymous. If otherwise, remove
	 * delete the session cart as it might be stolen / from another user
	 *
	 * @param username
	 *           the username of the current user
	 */
	public void checkCartForUser(final String username)
	{
		final String sessionCartUserId = getCartService().getSessionCart().getUser().getUid();
		if (!username.equals(sessionCartUserId) && !sessionCartUserId.equals(userService.getAnonymousUser().getUid()))
		{
			getCartService().setSessionCart(null);
		}
	}

	/**
	 * @return the bruteForceAttackCounter
	 */
	public BruteForceAttackCounter getBruteForceAttackCounter()
	{
		return bruteForceAttackCounter;
	}

	/**
	 * @param bruteForceAttackCounter
	 *           the bruteForceAttackCounter to set
	 */
	@Required
	public void setBruteForceAttackCounter(final BruteForceAttackCounter bruteForceAttackCounter)
	{
		this.bruteForceAttackCounter = bruteForceAttackCounter;
	}

	/**
	 * @return the userService
	 */
	public UserService getUserService()
	{
		return userService;
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
	 * @return the modelService
	 */
	public ModelService getModelService()
	{
		return modelService;
	}

	/**
	 * @param modelService
	 *           the modelService to set
	 */
	@Required
	public void setModelService(final ModelService modelService)
	{
		this.modelService = modelService;
	}

	/**
	 * @return the cartService
	 */
	public CartService getCartService()
	{
		return cartService;
	}

	/**
	 * @param cartService
	 *           the cartService to set
	 */
	public void setCartService(final CartService cartService)
	{
		this.cartService = cartService;
	}

	/**
	 * @return the adminAuthority
	 */
	public GrantedAuthority getAdminAuthority()
	{
		return adminAuthority;
	}


}
