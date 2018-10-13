package com.ghyrosoft.client.areas.settings;

import com.google.gwt.core.client.GWT;
import com.google.gwt.uibinder.client.UiBinder;
import com.google.gwt.user.client.ui.Composite;
import com.google.gwt.user.client.ui.Widget;

public class SettingsArea extends Composite {

	private static SettingsAreaUiBinder uiBinder = GWT
			.create(SettingsAreaUiBinder.class);

	interface SettingsAreaUiBinder extends UiBinder<Widget, SettingsArea> {
	}

	public SettingsArea() {
		initWidget(uiBinder.createAndBindUi(this));
	}

	public SettingsArea(String firstName) {
		initWidget(uiBinder.createAndBindUi(this));
	}
}
