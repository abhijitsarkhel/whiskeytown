/**
 *
 */
package org.training.storefront.filters.btg.support.impl;

import de.hybris.platform.commerceservices.category.CommerceCategoryService;
import de.hybris.platform.core.model.ItemModel;

/**
 * @author abhijit.s
 *
 */
public class CategoryPkResolvingStrategy extends AbstractParsingPkResolvingStrategy
{
	private CommerceCategoryService categoryService;

	/**
	 * @param categoryService
	 *           the categoryService to set
	 */
	public void setCategoryService(final CommerceCategoryService categoryService)
	{
		this.categoryService = categoryService;
	}


	@Override
	protected ItemModel retrieveModel(final String key)
	{
		return categoryService.getCategoryForCode(key);
	}
}
