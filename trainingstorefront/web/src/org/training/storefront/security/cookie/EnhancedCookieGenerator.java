/**
 *
 */
package org.training.storefront.security.cookie;

import javax.servlet.http.Cookie;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpServletResponseWrapper;

import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.util.CookieGenerator;


/**
 * @author abhijit.s
 *
 */
public class EnhancedCookieGenerator extends CookieGenerator
{
	public static final String HEADER_COOKIE = "Set-Cookie";
	public static final boolean DEFAULT_HTTP_ONLY = false;
	public static final boolean DEFAULT_COOKIE_PATH = true;

	private boolean useDefaultPath = DEFAULT_COOKIE_PATH;
	private boolean httpOnly = DEFAULT_HTTP_ONLY;

	@Override
	public void addCookie(final HttpServletResponse response, final String cookieValue)
	{
		super.addCookie(new HttpServletResponseWrapper(response)
		{
			@Override
			public void addCookie(final Cookie cookie)
			{
				setEnhancedCookiePath(cookie);
				if (isHttpOnly())
				{
					final StringBuffer headerBuffer = new StringBuffer(100);
					ServerCookie.appendCookieValue(headerBuffer, cookie.getVersion(), cookie.getName(), cookie.getValue(),
							cookie.getPath(), cookie.getDomain(), cookie.getComment(), cookie.getMaxAge(), cookie.getSecure(), true);
				}
			}
		}, cookieValue);


	}

	protected void setEnhancedCookiePath(final Cookie cookie)
	{
		if (!canUseDefaultPath())
		{
			final HttpServletRequest request = ((ServletRequestAttributes) RequestContextHolder.currentRequestAttributes())
					.getRequest();
			cookie.setPath(request.getContextPath());
		}
	}

	protected boolean isHttpOnly()
	{
		return httpOnly;
	}

	/**
	 * Marker to choose between only cookie based session and http header as addition
	 */
	public void setHttpOnly(final boolean httpOnly)
	{
		this.httpOnly = httpOnly;
	}

	protected boolean canUseDefaultPath()
	{
		return useDefaultPath;
	}

	/**
	 * Adjusts either dynamic {@link Cookie#setPath(String)} or static assignment. If true a cookie path is calculated by
	 * {@link #setEnhancedCookiePath(Cookie)} method.
	 */
	public void setUseDefaultPath(final boolean useDefaultPath)
	{
		this.useDefaultPath = useDefaultPath;
	}

}
