/**
 *
 */
package org.training.storefront.filters.btg.support.impl;

import de.hybris.platform.core.model.ItemModel;
import de.hybris.platform.product.ProductService;

/**
 * @author abhijit.s
 *
 */
public class ProductPkResolvingStrategy extends AbstractParsingPkResolvingStrategy
{
	private ProductService productService;

	/**
	 * @param productService
	 *           the productService to set
	 */
	public void setProductService(final ProductService productService)
	{
		this.productService = productService;
	}

	@Override
	protected ItemModel retrieveModel(final String key)
	{
		return productService.getProductForCode(key);
	}
}
