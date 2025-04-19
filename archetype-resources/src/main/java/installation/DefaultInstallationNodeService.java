package ${package}.installation;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.installation.ContributionConfiguration;
import com.ur.urcap.api.contribution.installation.CreationContext;
import com.ur.urcap.api.contribution.installation.InstallationAPIProvider;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DefaultInstallationNodeService implements SwingInstallationNodeService<DefaultInstallationNodeContribution, DefaultInstallationNodeView> {

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
	}

	@Override
	public String getTitle(Locale locale) {
		return "Hello World";
	}

	@Override
	public DefaultInstallationNodeView createView(ViewAPIProvider apiProvider) {
		return new DefaultInstallationNodeView(apiProvider);
	}

	@Override
	public DefaultInstallationNodeContribution createInstallationNode(InstallationAPIProvider apiProvider, DefaultInstallationNodeView view, DataModel model, CreationContext context) {
		return new DefaultInstallationNodeContribution(apiProvider, model, view);
	}
}
