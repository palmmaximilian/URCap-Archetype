package com.yourcompany.thenewapp.impl.installation;

import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeView;
import com.ur.urcap.api.domain.userinteraction.keyboard.KeyboardTextInput;
import com.ur.urcap.api.contribution.ViewAPIProvider;


import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JTextField;
import javax.swing.JTextPane;
import javax.swing.text.SimpleAttributeSet;
import javax.swing.text.StyleConstants;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

public class DefaultInstallationNodeView implements SwingInstallationNodeView<DefaultInstallationNodeContribution> {

	private final ViewAPIProvider apiProvider;


	public DefaultInstallationNodeView(ViewAPIProvider api) {
		this.apiProvider = api;
	}

	@Override
	public void buildUI(JPanel jPanel, final DefaultInstallationNodeContribution installationNode) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));


	}
	

}
