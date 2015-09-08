/**
 *
 */
package org.training.storefront.security;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Required;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.SimpleUrlAuthenticationFailureHandler;


/**
 * @author abhijit.s
 *
 */
public class LoginAuthenticationFailureHandler extends SimpleUrlAuthenticationFailureHandler
{

	private BruteForceAttackCounter bruteForceAttackCounter;

	@Override
	public void onAuthenticationFailure(final HttpServletRequest request, final HttpServletResponse response,
			final AuthenticationException exception) throws IOException, ServletException
	{
		bruteForceAttackCounter.registerLoginFailure(request.getParameter("j_username"));
		request.getSession().setAttribute("SPRING_SECURITY_LAST_USERNAME", request.getParameter("j_username"));
		super.onAuthenticationFailure(request, response, exception);
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
}
