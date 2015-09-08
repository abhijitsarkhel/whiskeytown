/**
 *
 */
package org.training.storefront.security.cookie;

import java.io.Serializable;
import java.text.DateFormat;
import java.text.FieldPosition;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;
import java.util.TimeZone;


/**
 * @author abhijit.s
 *
 */
public class ServerCookie implements Serializable
{

	private static final long serialVersionUID = 1L;
	private static final String OLD_COOKIE_PATTERN = "EEE, dd-MMM-yyyy HH:mm:ss z";

	private static final ThreadLocal<DateFormat> OLD_COOKIE_FORMAT = new ThreadLocal<DateFormat>()
	{
		@Override
		protected DateFormat initialValue()
		{
			final DateFormat df = new SimpleDateFormat(OLD_COOKIE_PATTERN, Locale.US);
			df.setTimeZone(TimeZone.getTimeZone("GMT"));
			return df;
		}
	};

	private static final String ancientDate;

	static
	{
		ancientDate = OLD_COOKIE_FORMAT.get().format(new Date(10000));
	}

	private ServerCookie()
	{

	}

	public static void appendCookieValue(final StringBuffer headerBuf, final int version, final String name, final String value,
			final String path, final String domain, final String comment, final int maxAge, final boolean isSecure,
			final boolean isHttpOnly)
	{
		final StringBuffer buf = new StringBuffer();
		buf.append(name);
		buf.append("=");
		int newVersion = version;
		if (newVersion == 0
				&& (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(value) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0
						&& CookieSupport.isV0Token(value)))
		{
			newVersion = 1;
		}
		if (newVersion == 0 && comment != null)
		{
			// Using a comment makes it a v1 cookie
			newVersion = 1;
		}
		if (newVersion == 0
				&& (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(path) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0
						&& CookieSupport.isV0Token(path)))
		{
			// HTTP token in path - need to use v1
			newVersion = 1;
		}
		if (newVersion == 0
				&& (!CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 && CookieSupport.isHttpToken(domain) || CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0
						&& CookieSupport.isV0Token(domain)))
		{
			// HTTP token in domain - need to use v1
			newVersion = 1;
		}

		maybeQuote(buf, value);
		if (newVersion == 1)
		{
			// Version=1 ... required
			buf.append("; Version=1");

			// Comment=comment
			if (comment != null)
			{
				buf.append("; Comment=");
				maybeQuote(buf, comment);
			}
		}
		if (domain != null)
		{
			buf.append("; Domain=");
			maybeQuote(buf, domain);
		}

		// Max-Age=secs ... or use old "Expires" format
		if (maxAge >= 0)
		{
			if (newVersion > 0)
			{
				buf.append("; Max-Age=");
				buf.append(maxAge);
			}
			// IE6, IE7 and possibly other browsers don't understand Max-Age.
			// They do understand Expires, even with V1 cookies!
			if (newVersion == 0 || CookieSupport.ALWAYS_ADD_EXPIRES)
			{
				// Wdy, DD-Mon-YY HH:MM:SS GMT ( Expires Netscape format )
				buf.append("; Expires=");
				// To expire immediately we need to set the time in past
				if (maxAge == 0)
				{
					buf.append(ancientDate);
				}
				else
				{
					OLD_COOKIE_FORMAT.get().format(new Date(System.currentTimeMillis() + maxAge * 1000L), buf, new FieldPosition(0));
				}
			}
		}
		// Path=path
		if (path != null)
		{
			buf.append("; Path=");
			maybeQuote(buf, path);
		}

		// Secure
		if (isSecure)
		{
			buf.append("; Secure");
		}

		// HttpOnly
		if (isHttpOnly)
		{
			buf.append("; HttpOnly");
		}
		headerBuf.append(buf);
	}

	/**
	 * Quotes values if required.
	 *
	 * @param buf
	 * @param value
	 */
	protected static void maybeQuote(final StringBuffer buf, final String value)
	{
		if (value == null || value.isEmpty())
		{
			buf.append("\"\"");
		}
		else if (CookieSupport.alreadyQuoted(value))
		{
			buf.append('"');
			buf.append(escapeDoubleQuotes(value, 1, value.length() - 1));
			buf.append('"');
		}
		else if (CookieSupport.isHttpToken(value) && !CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0 || CookieSupport.isV0Token(value)
				&& CookieSupport.ALLOW_HTTP_SEPARATORS_IN_V0)
		{
			buf.append('"');
			buf.append(escapeDoubleQuotes(value, 0, value.length()));
			buf.append('"');
		}
		else
		{
			buf.append(value);
		}
	}

	/**
	 * Escapes any double quotes in the given string.
	 *
	 * @param s
	 *           the input string
	 * @param beginIndex
	 *           start index inclusive
	 * @param endIndex
	 *           exclusive
	 * @return The (possibly) escaped string
	 */
	protected static String escapeDoubleQuotes(final String s, final int beginIndex, final int endIndex)
	{

		if (s == null || s.isEmpty() || s.indexOf('"') == -1)
		{
			return s;
		}

		final StringBuilder result = new StringBuilder();
		for (int i = beginIndex; i < endIndex; i++)
		{
			final char c = s.charAt(i);
			if (c == '\\')
			{
				result.append(c);
				//ignore the character after an escape, just append it
				if (++i >= endIndex)
				{
					throw new IllegalArgumentException("Invalid escape character in cookie value.");
				}
				result.append(s.charAt(i));
			}
			else if (c == '"')
			{
				result.append('\\').append('"');
			}
			else
			{
				result.append(c);
			}
		}

		return result.toString();
	}

	public static final class CookieSupport
	{
		/**
		 * If set to true, we parse cookies strictly according to the servlet, cookie and HTTP specs by default.
		 */
		public static final boolean STRICT_SERVLET_COMPLIANCE;

		/**
		 * If true, cookie values are allowed to contain an equals character without being quoted.
		 */
		public static final boolean ALLOW_EQUALS_IN_VALUE;

		/**
		 * If true, separators that are not explicitly dis-allowed by the v0 cookie spec but are disallowed by the HTTP
		 * spec will be allowed in v0 cookie names and values. These characters are: \"()/:<=>?@[\\]{} Note that the
		 * inclusion of / depends on the value of {@link #FWD_SLASH_IS_SEPARATOR}.
		 */
		public static final boolean ALLOW_HTTP_SEPARATORS_IN_V0;

		/**
		 * If set to false, we don't use the IE6/7 Max-Age/Expires work around. Default is usually true. If
		 * STRICT_SERVLET_COMPLIANCE==true then default is false. Explicitly setting always takes priority.
		 */
		public static final boolean ALWAYS_ADD_EXPIRES;

		/**
		 * If set to true, the <code>/</code> character will be treated as a separator. Default is usually false. If
		 * STRICT_SERVLET_COMPLIANCE==true then default is true. Explicitly setting always takes priority.
		 */
		public static final boolean FWD_SLASH_IS_SEPARATOR;

		/**
		 * If true, name only cookies will be permitted.
		 */
		public static final boolean ALLOW_NAME_ONLY;

		/**
		 * The list of separators that apply to version 0 cookies. To quote the spec, these are comma, semi-colon and
		 * white-space. The HTTP spec definition of linear white space is [CRLF] 1*( SP | HT )
		 */
		private static final char[] V0_SEPARATORS =
		{ ',', ';', ' ', '\t' };
		private static final boolean[] V0_SEPARATOR_FLAGS = new boolean[128];

		/**
		 * The list of separators that apply to version 1 cookies. This may or may not include '/' depending on the
		 * setting of {@link #FWD_SLASH_IS_SEPARATOR}.
		 */
		private static final char[] HTTP_SEPARATORS;
		private static final boolean[] HTTP_SEPARATOR_FLAGS = new boolean[128];

		static
		{
			STRICT_SERVLET_COMPLIANCE = Boolean
					.valueOf(System.getProperty("org.apache.catalina.STRICT_SERVLET_COMPLIANCE", "false")).booleanValue();
			ALLOW_EQUALS_IN_VALUE = Boolean.valueOf(
					System.getProperty("org.apache.tomcat.util.http.ServerCookie.ALLOW_EQUALS_IN_VALUE", "false")).booleanValue();
			ALLOW_HTTP_SEPARATORS_IN_V0 = Boolean.valueOf(
					System.getProperty("org.apache.tomcat.util.http.ServerCookie.ALLOW_HTTP_SEPARATORS_IN_V0", "false"))
					.booleanValue();

			final String alwaysAddExpires = System.getProperty("org.apache.tomcat.util.http.ServerCookie.ALWAYS_ADD_EXPIRES");
			if (alwaysAddExpires == null)
			{
				ALWAYS_ADD_EXPIRES = !STRICT_SERVLET_COMPLIANCE;
			}
			else
			{
				ALWAYS_ADD_EXPIRES = Boolean.valueOf(alwaysAddExpires).booleanValue();
			}

			final String fwdSlashIsSeparator = System.getProperty("org.apache.tomcat.util.http.ServerCookie.FWD_SLASH_IS_SEPARATOR");
			if (fwdSlashIsSeparator == null)
			{
				FWD_SLASH_IS_SEPARATOR = STRICT_SERVLET_COMPLIANCE;
			}
			else
			{
				FWD_SLASH_IS_SEPARATOR = Boolean.valueOf(fwdSlashIsSeparator).booleanValue();
			}

			ALLOW_NAME_ONLY = Boolean.valueOf(
					System.getProperty("org.apache.tomcat.util.http.ServerCookie.ALLOW_NAME_ONLY", "false")).booleanValue();

			/*
			 * Excluding the '/' char by default violates the RFC, but it looks like a lot of people put '/' in unquoted
			 * values: '/': ; //47 '\t':9 ' ':32 '\"':34 '(':40 ')':41 ',':44 ':':58 ';':59 '<':60 '=':61 '>':62 '?':63
			 * '@':64 '[':91 '\\':92 ']':93 '{':123 '}':125
			 */
			if (CookieSupport.FWD_SLASH_IS_SEPARATOR)
			{
				HTTP_SEPARATORS = new char[]
				{ '\t', ' ', '\"', '(', ')', ',', '/', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '{', '}' };
			}
			else
			{
				HTTP_SEPARATORS = new char[]
				{ '\t', ' ', '\"', '(', ')', ',', ':', ';', '<', '=', '>', '?', '@', '[', '\\', ']', '{', '}' };
			}
			for (int i = 0; i < 128; i++)
			{
				V0_SEPARATOR_FLAGS[i] = false;
				HTTP_SEPARATOR_FLAGS[i] = false;
			}
			for (final char V0_SEPARATOR : V0_SEPARATORS)
			{
				V0_SEPARATOR_FLAGS[V0_SEPARATOR] = true;
			}
			for (final char HTTP_SEPARATOR : HTTP_SEPARATORS)
			{
				HTTP_SEPARATOR_FLAGS[HTTP_SEPARATOR] = true;
			}
		}

		private CookieSupport()
		{

		}

		public static final boolean isV0Separator(final char c)
		{
			if (c < 0x20 || c >= 0x7f)
			{
				if (c != 0x09)
				{
					throw new IllegalArgumentException("Control character in cookie value or attribute.");
				}
			}
			return V0_SEPARATOR_FLAGS[c];
		}

		public static boolean isV0Token(final String value)
		{
			if (value == null)
			{
				return false;
			}
			int i = 0;
			int len = value.length();
			if (alreadyQuoted(value))
			{
				i++;
				len--;
			}
			for (; i < len; i++)
			{
				final char c = value.charAt(i);
				if (isV0Separator(c))
				{
					return true;
				}
			}
			return false;
		}

		public static boolean alreadyQuoted(final String value)
		{
			if (value == null || value.length() < 2)
			{
				return false;
			}
			return (value.charAt(0) == '\"' && value.charAt(value.length() - 1) == '\"');
		}

		public static final boolean isHttpSeparator(final char c)
		{
			if (c < 0x20 || c >= 0x7f)
			{
				if (c != 0x09)
				{
					throw new IllegalArgumentException("Control character in cookie value or attribute.");
				}
			}
			return HTTP_SEPARATOR_FLAGS[c];
		}

		public static boolean isHttpToken(final String value)
		{
			if (value == null)
			{
				return false;
			}
			int i = 0;
			int len = value.length();
			if (alreadyQuoted(value))
			{
				i++;
				len--;
			}
			for (; i < len; i++)
			{
				final char c = value.charAt(i);
				if (isHttpSeparator(c))
				{
					return true;
				}
			}
			return false;
		}
	}
}
