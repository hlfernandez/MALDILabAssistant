package es.uvigo.ei.sing.mla.view.converters;

import java.awt.Color;

import org.zkoss.bind.BindContext;
import org.zkoss.bind.Converter;
import org.zkoss.zk.ui.Component;

import es.uvigo.ei.sing.mla.model.entities.ConditionGroup;

public class ConditionToSClassConverter implements Converter<String, ConditionGroup, Component>{
	@Override
	public ConditionGroup coerceToBean(String beanProp, Component component, BindContext ctx) {
		throw new UnsupportedOperationException();
	}

	@Override
	public String coerceToUi(ConditionGroup compAttr, Component component, BindContext ctx) {
		final Color bgColor = Color.decode(compAttr.getColor());
		final Color fgColor = ColorUtils.getBestContrast(bgColor);
		
		return String.format("background-color: %s; color: %s;", compAttr.getColor(), ColorUtils.colorToHexString(fgColor));
	}
}
