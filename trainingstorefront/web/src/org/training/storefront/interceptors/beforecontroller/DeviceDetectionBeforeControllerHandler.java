/**
 *
 */
package org.training.storefront.interceptors.beforecontroller;

import de.hybris.platform.acceleratorfacades.device.DeviceDetectionFacade;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.web.method.HandlerMethod;
import org.training.storefront.interceptors.BeforeControllerHandler;

/**
 * @author abhijit.s
 *
 */
public class DeviceDetectionBeforeControllerHandler implements BeforeControllerHandler
{
	@Resource(name = "deviceDetectionFacade")
	private DeviceDetectionFacade deviceDetectionFacade;

	@Override
	public boolean beforeController(final HttpServletRequest request, final HttpServletResponse response,
			final HandlerMethod handler)
	{
		// Detect the device information for the current request
		deviceDetectionFacade.initializeRequest(request);
		return true;
	}
}
