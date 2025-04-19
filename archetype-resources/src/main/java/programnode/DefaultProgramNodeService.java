package ${package}.programnode;

import com.ur.urcap.api.contribution.ViewAPIProvider;
import com.ur.urcap.api.contribution.program.ContributionConfiguration;
import com.ur.urcap.api.contribution.program.CreationContext;
import com.ur.urcap.api.contribution.program.ProgramAPIProvider;
import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.domain.SystemAPI;
import com.ur.urcap.api.domain.data.DataModel;

import java.util.Locale;

public class DefaultProgramNodeService implements SwingProgramNodeService<DefaultProgramNodeContribution, DefaultProgramNodeView> {

	@Override
	public String getId() {
		return "HelloWorldSwingNode";
	}

	@Override
	public void configureContribution(ContributionConfiguration configuration) {
		configuration.setChildrenAllowed(true);
	}

	@Override
	public String getTitle(Locale locale) {
		String title = "Hello World";
		return title;
	}

	@Override
	public DefaultProgramNodeView createView(ViewAPIProvider apiProvider) {
		return new DefaultProgramNodeView(apiProvider);
	}

	@Override
	public DefaultProgramNodeContribution createNode(
			ProgramAPIProvider apiProvider,
			DefaultProgramNodeView view,
			DataModel model,
			CreationContext context) {
		return new DefaultProgramNodeContribution(apiProvider, view, model);
	}
}
