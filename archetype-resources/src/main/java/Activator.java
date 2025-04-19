package $package;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;

import ${package}.installation.DefaultInstallationNodeService;
import ${package}.programnode.DefaultProgramNodeService;
import ${package}.toolbar.DefaultToolbarService;

import org.osgi.framework.BundleActivator;
import org.osgi.framework.BundleContext;

/**
 * Hello world activator for the OSGi bundle URCAPS contribution
 *
 */
public class Activator implements BundleActivator {
	@Override
	public void start(BundleContext bundleContext) throws Exception {
		System.out.println("Activator says Hello World!");
		bundleContext.registerService(SwingInstallationNodeService.class, new DefaultInstallationNodeService(), null);
		bundleContext.registerService(SwingProgramNodeService.class, new DefaultProgramNodeService(), null);
		bundleContext.registerService(SwingToolbarService.class, new DefaultToolbarService(), null);
	}

	@Override
	public void stop(BundleContext bundleContext) throws Exception {
		System.out.println("Activator says Goodbye World!");
	}
}

