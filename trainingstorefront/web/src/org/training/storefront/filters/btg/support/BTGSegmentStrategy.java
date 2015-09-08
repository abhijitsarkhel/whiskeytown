/**
 *
 */
package org.training.storefront.filters.btg.support;

import java.io.IOException;

import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;

/**
 * @author abhijit.s
 *
 */
public interface BTGSegmentStrategy
{
	void evaluateSegment(HttpServletRequest httpRequest) throws ServletException, IOException;
}
