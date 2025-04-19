package com.yourcompany.thenewapp.impl.installation;

import com.ur.urcap.api.contribution.InstallationNodeContribution;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class DefaultInstallationNodeContribution implements InstallationNodeContribution {

	private final DefaultInstallationNodeView view;

	private DataModel model;

	public DefaultInstallationNodeContribution(InstallationAPIProvider apiProvider, DataModel model, DefaultInstallationNodeView view) {
		this.model = model;
		this.view = view;
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {

	}

	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
	}

	public String getModelData(String key, String defaultValue)
	{
		return model.get(key,defaultValue);
	}

}
