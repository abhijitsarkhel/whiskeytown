/**
 *
 */
package org.training.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.CartSuggestionComponentModel;
import de.hybris.platform.acceleratorcms.model.components.SimpleSuggestionComponentModel;
import de.hybris.platform.commercefacades.order.CartFacade;
import de.hybris.platform.commercefacades.order.data.OrderEntryData;
import de.hybris.platform.commercefacades.product.data.ProductData;

import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.apache.commons.collections.CollectionUtils;
import org.apache.commons.collections.Transformer;
import org.apache.commons.lang.StringUtils;
import org.fest.util.Collections;
import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.training.facades.suggestion.SimpleSuggestionFacade;
import org.training.storefront.controllers.ControllerConstants;


/**
 * @author abhijit.s Controller for CMS CartSuggestionComponent
 */
@Controller("SimpleCartSuggestionComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.CartSuggestionComponent)
public class CartSuggestionComponentController extends AbstractCMSComponentController<CartSuggestionComponentModel>
{
	@Resource(name = "cartFacade")
	private CartFacade cartFacade;

	@Resource(name = "simpleStoreFrontSuggestionFacade")
	private SimpleSuggestionFacade simpleSuggestionFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final CartSuggestionComponentModel component)
	{
		if (cartFacade.hasSessionCart())
		{
			final Set<String> products = new HashSet<String>(CollectionUtils.collect(cartFacade.getSessionCart().getEntries(),
					new Transformer()
					{
						@Override
						public Object transform(final Object object)
						{
							final OrderEntryData orderEntry = (OrderEntryData) object;
							return orderEntry.getProduct().getCode();
						}
					}));

			if (!Collections.isEmpty(products))
			{
				final List<ProductData> productSuggestions = simpleSuggestionFacade.getReferencesForProducts(products,
						component.getProductReferenceTypes(), component.isFilterPurchased(), component.getMaximumNumberProducts());

				model.addAttribute("title", component.getTitle());
				model.addAttribute("suggestions", productSuggestions);
			}
		}
	}

	@Override
	protected String getView(final CartSuggestionComponentModel component)
	{
		return ControllerConstants.Views.Cms.ComponentPrefix + StringUtils.lowerCase(SimpleSuggestionComponentModel._TYPECODE);
	}
}
