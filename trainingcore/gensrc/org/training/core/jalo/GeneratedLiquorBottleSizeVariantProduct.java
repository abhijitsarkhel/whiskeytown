/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 8 Sep, 2015 4:22:35 PM                      ---
 * ----------------------------------------------------------------
 */
package org.training.core.jalo;

import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.enumeration.EnumerationValue;
import de.hybris.platform.variants.jalo.VariantProduct;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;
import org.training.constants.TrainingcoreConstants;

/**
 * Generated class for type {@link org.training.core.jalo.LiquorBottleSizeVariantProduct LiquorBottleSizeVariantProduct}.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedLiquorBottleSizeVariantProduct extends VariantProduct
{
	/** Qualifier of the <code>LiquorBottleSizeVariantProduct.bottleSize</code> attribute **/
	public static final String BOTTLESIZE = "bottleSize";
	protected static final Map<String, AttributeMode> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, AttributeMode> tmp = new HashMap<String, AttributeMode>(VariantProduct.DEFAULT_INITIAL_ATTRIBUTES);
		tmp.put(BOTTLESIZE, AttributeMode.INITIAL);
		DEFAULT_INITIAL_ATTRIBUTES = Collections.unmodifiableMap(tmp);
	}
	@Override
	protected Map<String, AttributeMode> getDefaultAttributeModes()
	{
		return DEFAULT_INITIAL_ATTRIBUTES;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LiquorBottleSizeVariantProduct.bottleSize</code> attribute.
	 * @return the bottleSize - A normalized bottle size mapping to a standardized front-end navigable name.
	 */
	public Set<EnumerationValue> getBottleSize(final SessionContext ctx)
	{
		Set<EnumerationValue> coll = (Set<EnumerationValue>)getProperty( ctx, BOTTLESIZE);
		return coll != null ? coll : Collections.EMPTY_SET;
	}
	
	/**
	 * <i>Generated method</i> - Getter of the <code>LiquorBottleSizeVariantProduct.bottleSize</code> attribute.
	 * @return the bottleSize - A normalized bottle size mapping to a standardized front-end navigable name.
	 */
	public Set<EnumerationValue> getBottleSize()
	{
		return getBottleSize( getSession().getSessionContext() );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LiquorBottleSizeVariantProduct.bottleSize</code> attribute. 
	 * @param value the bottleSize - A normalized bottle size mapping to a standardized front-end navigable name.
	 */
	public void setBottleSize(final SessionContext ctx, final Set<EnumerationValue> value)
	{
		setProperty(ctx, BOTTLESIZE,value == null || !value.isEmpty() ? value : null );
	}
	
	/**
	 * <i>Generated method</i> - Setter of the <code>LiquorBottleSizeVariantProduct.bottleSize</code> attribute. 
	 * @param value the bottleSize - A normalized bottle size mapping to a standardized front-end navigable name.
	 */
	public void setBottleSize(final Set<EnumerationValue> value)
	{
		setBottleSize( getSession().getSessionContext(), value );
	}
	
}
