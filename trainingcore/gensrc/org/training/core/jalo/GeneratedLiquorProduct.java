/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11 Sep, 2015 12:44:43 PM                    ---
 * ----------------------------------------------------------------
 */
package org.training.core.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.product.Product;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

/**
 * Generated class for type {@link org.training.core.jalo.LiquorProduct LiquorProduct}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedLiquorProduct extends Product
{
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(Product.DEFAULT_INITIAL_ATTRIBUTES);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
}
