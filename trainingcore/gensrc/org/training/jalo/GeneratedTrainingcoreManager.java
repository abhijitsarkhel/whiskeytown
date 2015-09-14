/*
 * ----------------------------------------------------------------
 * --- WARNING: THIS FILE IS GENERATED AND WILL BE OVERWRITTEN! ---
 * --- Generated at 11 Sep, 2015 12:44:43 PM                    ---
 * ----------------------------------------------------------------
 */
package org.training.jalo;

import de.hybris.platform.jalo.Item;
import de.hybris.platform.jalo.Item.AttributeMode;
import de.hybris.platform.jalo.JaloBusinessException;
import de.hybris.platform.jalo.JaloSystemException;
import de.hybris.platform.jalo.SessionContext;
import de.hybris.platform.jalo.extension.Extension;
import de.hybris.platform.jalo.type.ComposedType;
import de.hybris.platform.jalo.type.JaloGenericCreationException;
import java.util.HashMap;
import java.util.Map;
import org.training.constants.TrainingcoreConstants;
import org.training.core.jalo.LiquorBottleSizeVariantProduct;
import org.training.core.jalo.LiquorProduct;

/**
 * Generated class for type <code>TrainingcoreManager</code>.
 */
@SuppressWarnings({"deprecation","unused","cast","PMD"})
public abstract class GeneratedTrainingcoreManager extends Extension
{
	protected static final Map<String, Map<String, AttributeMode>> DEFAULT_INITIAL_ATTRIBUTES;
	static
	{
		final Map<String, Map<String, AttributeMode>> ttmp = new HashMap();
		DEFAULT_INITIAL_ATTRIBUTES = ttmp;
	}
	@Override
	public Map<String, AttributeMode> getDefaultAttributeModes(final Class<? extends Item> itemClass)
	{
		Map<String, AttributeMode> ret = new HashMap<>();
		final Map<String, AttributeMode> attr = DEFAULT_INITIAL_ATTRIBUTES.get(itemClass.getName());
		if (attr != null)
		{
			ret.putAll(attr);
		}
		return ret;
	}
	
	public LiquorBottleSizeVariantProduct createLiquorBottleSizeVariantProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( TrainingcoreConstants.TC.LIQUORBOTTLESIZEVARIANTPRODUCT );
			return (LiquorBottleSizeVariantProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating LiquorBottleSizeVariantProduct : "+e.getMessage(), 0 );
		}
	}
	
	public LiquorBottleSizeVariantProduct createLiquorBottleSizeVariantProduct(final Map attributeValues)
	{
		return createLiquorBottleSizeVariantProduct( getSession().getSessionContext(), attributeValues );
	}
	
	public LiquorProduct createLiquorProduct(final SessionContext ctx, final Map attributeValues)
	{
		try
		{
			ComposedType type = getTenant().getJaloConnection().getTypeManager().getComposedType( TrainingcoreConstants.TC.LIQUORPRODUCT );
			return (LiquorProduct)type.newInstance( ctx, attributeValues );
		}
		catch( JaloGenericCreationException e)
		{
			final Throwable cause = e.getCause();
			throw (cause instanceof RuntimeException ?
			(RuntimeException)cause
			:
			new JaloSystemException( cause, cause.getMessage(), e.getErrorCode() ) );
		}
		catch( JaloBusinessException e )
		{
			throw new JaloSystemException( e ,"error creating LiquorProduct : "+e.getMessage(), 0 );
		}
	}
	
	public LiquorProduct createLiquorProduct(final Map attributeValues)
	{
		return createLiquorProduct( getSession().getSessionContext(), attributeValues );
	}
	
	@Override
	public String getName()
	{
		return TrainingcoreConstants.EXTENSIONNAME;
	}
	
}
