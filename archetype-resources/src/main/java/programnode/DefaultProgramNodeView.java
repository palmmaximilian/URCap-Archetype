package ${package}.programnode;

import com.ur.urcap.api.contribution.ContributionProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.contribution.ViewAPIProvider;


import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DefaultProgramNodeView implements SwingProgramNodeView<DefaultProgramNodeContribution>{
	private final ViewAPIProvider apiProvider;

	public DefaultProgramNodeView(ViewAPIProvider api) {
		this.apiProvider = api;
	}

	@Override
	public void buildUI(JPanel jPanel, final ContributionProvider<DefaultProgramNodeContribution> provider) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));

	}

}
