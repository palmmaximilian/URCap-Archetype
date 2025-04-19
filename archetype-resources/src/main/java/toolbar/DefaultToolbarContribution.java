package ${package}.toolbar;
import ${package}.installation.DefaultInstallationNodeContribution;

import com.ur.urcap.api.contribution.toolbar.ToolbarContext;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarContribution;

import javax.swing.BorderFactory;
import javax.swing.Box;
import javax.swing.BoxLayout;
import javax.swing.JLabel;
import javax.swing.JPanel;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.Font;
import java.util.Date;
import java.util.Locale;
import java.util.Random;

class DefaultToolbarContribution implements SwingToolbarContribution {

	private final ToolbarContext context;

	DefaultToolbarContribution(ToolbarContext context) {
		this.context = context;
	}

	@Override
	public void openView() {
	}

	@Override
	public void closeView() {
	}

	public void buildUI(JPanel jPanel) {
		jPanel.setLayout(new BoxLayout(jPanel, BoxLayout.Y_AXIS));
	}
	
	private DefaultInstallationNodeContribution getInstallation() {
		return context
		  .getAPIProvider()
		  .getApplicationAPI()
		  .getInstallationNode(DefaultInstallationNodeContribution.class);
	  }

}
