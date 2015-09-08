/**
 *
 */
package org.training.storefront.controllers.misc;

import de.hybris.platform.acceleratorstorefrontcommons.controllers.AbstractController;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.CartData;

import javax.annotation.Resource;

import org.apache.log4j.Logger;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;


/**
 * @author abhijit.s Controller to handle the tax estimation on a cart.
 */
@Controller
@Scope("tenant")
public class EstimateTaxesController extends AbstractController
{
	protected static final Logger LOG = Logger.getLogger(EstimateTaxesController.class);
	@Resource(name = "cartFacade")
	private CartFacade cartFacade;


	@RequestMapping(value = "/cart/estimate", method = RequestMethod.GET, produces = "application/json")
	public @ResponseBody CartData addToCart(@RequestParam("zipCode") final String zipCode,
			@RequestParam("isocode") final String countryIsoCode, final Model model)
	{
		final CartData estimatedCart = cartFacade.estimateExternalTaxes(zipCode, countryIsoCode);
		return estimatedCart;
	}
}
