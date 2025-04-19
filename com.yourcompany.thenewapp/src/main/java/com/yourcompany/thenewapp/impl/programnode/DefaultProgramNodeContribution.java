package com.yourcompany.thenewapp.impl.programnode;

import com.yourcompany.thenewapp.impl.installation.DefaultInstallationNodeContribution;

import com.ur.urcap.api.contribution.ProgramNodeContribution;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.domain.ProgramAPI;
import com.ur.urcap.api.domain.data.DataModel;
import com.ur.urcap.api.domain.script.ScriptWriter;
import com.ur.urcap.api.domain.undoredo.UndoRedoManager;
import com.ur.urcap.api.domain.undoredo.UndoableChanges;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputCallback;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardInputFactory;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;

public class DefaultProgramNodeContribution implements ProgramNodeContribution {

	private final ProgramAPI programAPI;

	private final DefaultProgramNodeView view;
	private final DataModel model;

	public DefaultProgramNodeContribution(ProgramAPIProvider apiProvider, DefaultProgramNodeView view, DataModel model) {
		this.programAPI = apiProvider.getProgramAPI();
		this.view = view;
		this.model = model;
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	@Override
	public String getTitle() {
		return "Hello World: ";
	}

	@Override
	public boolean isDefined() {
		return true;
	}

	@Override
	public void generateScript(ScriptWriter writer) {
	}


	private DefaultInstallationNodeContribution getInstallation() {
		return programAPI.getInstallationNode(DefaultInstallationNodeContribution.class);
	}
	
	private void setModelValue(final String key, final String value) {
		programAPI
		  .getUndoRedoManager()
		  .recordChanges(
			new UndoableChanges() {
			  @Override
			  public void executeChanges() {
				model.set(key, value);
			  }
			}
		  );
	  }

}
