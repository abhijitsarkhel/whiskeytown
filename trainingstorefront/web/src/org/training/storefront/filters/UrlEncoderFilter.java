/**
 *
 */
package org.training.storefront.filters;

import de.hybris.platform.acceleratorfacades.urlencoder.UrlEncoderFacade;
import de.hybris.platform.acceleratorfacades.urlencoder.data.UrlEncoderData;
import de.hybris.platform.acceleratorfacades.urlencoder.data.UrlEncoderPatternData;
import de.hybris.platform.acceleratorstorefrontcommons.constants.WebConstants;
import de.hybris.platform.servicelayer.session.SessionService;

import java.io.IOException;
import java.util.List;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;

import org.apache.commons.lang.StringUtils;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Required;
import org.springframework.web.filter.OncePerRequestFilter;
import org.training.storefront.web.wrappers.UrlEncodeHttpRequestWrapper;


/**
 * @author abhijit.s
 *
 */
public class UrlEncoderFilter extends OncePerRequestFilter
{
	private static final Logger LOG = Logger.getLogger(UrlEncoderFilter.class.getName());

	private UrlEncoderFacade urlEncoderFacade;
	private SessionService sessionService;

	@Override
	protected void doFilterInternal(final HttpServletRequest request, final HttpServletResponse response,
			final FilterChain filterChain) throws ServletException, IOException
	{
		if (LOG.isDebugEnabled())
		{
			LOG.debug(" The incoming URL : [" + request.getRequestURL().toString() + "]");
		}
		final List<UrlEncoderData> urlEncodingAttributes = getUrlEncoderFacade().variablesForUrlEncoding();
		if (urlEncodingAttributes != null && !urlEncodingAttributes.isEmpty())
		{
			final HttpSession session = request.getSession(false);
			final UrlEncoderPatternData patternData = getUrlEncoderFacade().patternForUrlEncoding(
					request.getRequestURI().toString(), request.getContextPath(), (session != null && session.isNew()));
			final String patternBeforeProcessing = getSessionService().getAttribute(WebConstants.URL_ENCODING_ATTRIBUTES);
			final String pattern = "/" + patternData.getPattern();
			if (!StringUtils.equalsIgnoreCase(patternBeforeProcessing, pattern))
			{
				if (patternData.isRedirectRequired())
				{
					String redirectUrl = StringUtils.replace(request.getRequestURL().toString(), patternBeforeProcessing + "/",
							pattern + "/");
					final String queryString = request.getQueryString();
					if (StringUtils.isNotBlank(queryString))
					{
						redirectUrl = redirectUrl + "?" + queryString;
					}
					response.sendRedirect(redirectUrl);
				}
				else
				{
					getUrlEncoderFacade().updateUrlEncodingData();
				}
			}
			getSessionService().setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, pattern);
			final UrlEncodeHttpRequestWrapper wrappedRequest = new UrlEncodeHttpRequestWrapper(request, patternData.getPattern());
			wrappedRequest.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, pattern);
			wrappedRequest.setAttribute("originalContextPath",
					StringUtils.isBlank(request.getContextPath()) ? "/" : request.getContextPath());
			if (LOG.isDebugEnabled())
			{
				LOG.debug("ContextPath=[" + wrappedRequest.getContextPath() + "]" + " Servlet Path= ["
						+ wrappedRequest.getServletPath() + "]" + " Request Url= [" + wrappedRequest.getRequestURL() + "]");
			}
			filterChain.doFilter(wrappedRequest, response);
		}
		else
		{
			if (LOG.isDebugEnabled())
			{
				LOG.debug(" No URL attributes defined");
			}
			request.setAttribute(WebConstants.URL_ENCODING_ATTRIBUTES, "");
			filterChain.doFilter(request, response);
		}
	}

	protected UrlEncoderFacade getUrlEncoderFacade()
	{
		return urlEncoderFacade;
	}

	@Required
	public void setUrlEncoderFacade(final UrlEncoderFacade urlEncoderFacade)
	{
		this.urlEncoderFacade = urlEncoderFacade;
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
}
