/**
 *
 */
package org.training.storefront.controllers.cms;

import de.hybris.platform.acceleratorcms.model.components.ProductReferencesComponentModel;
import de.hybris.platform.commercefacades.product.ProductFacade;
import de.hybris.platform.commercefacades.product.ProductOption;
import de.hybris.platform.commercefacades.product.data.ProductReferenceData;
import de.hybris.platform.core.model.product.ProductModel;

import java.util.Arrays;
import java.util.List;

import javax.annotation.Resource;
import javax.servlet.http.HttpServletRequest;

import org.springframework.context.annotation.Scope;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.training.storefront.controllers.ControllerConstants;


/**
 * @author abhijit.s Controller for CMS ProductReferencesComponent
 */
@Controller("ProductReferencesComponentController")
@Scope("tenant")
@RequestMapping(value = ControllerConstants.Actions.Cms.ProductReferencesComponent)
public class ProductReferencesComponentController extends AbstractCMSComponentController<ProductReferencesComponentModel>
{
	protected static final List<ProductOption> PRODUCT_OPTIONS = Arrays.asList(ProductOption.BASIC, ProductOption.PRICE);

	@Resource(name = "accProductFacade")
	private ProductFacade productFacade;

	@Override
	protected void fillModel(final HttpServletRequest request, final Model model, final ProductReferencesComponentModel component)
	{
		final ProductModel currentProduct = getRequestContextData(request).getProduct();
		if (currentProduct != null)
		{
			final List<ProductReferenceData> productReferences = productFacade.getProductReferencesForCode(currentProduct.getCode(),
					component.getProductReferenceTypes(), PRODUCT_OPTIONS, component.getMaximumNumberProducts());

			model.addAttribute("title", component.getTitle());
			model.addAttribute("productReferences", productReferences);
		}
	}
}
