package com.yourcompany.thenewapp.impl;

import com.ur.urcap.api.contribution.program.swing.SwingProgramNodeService;
import com.ur.urcap.api.contribution.installation.swing.SwingInstallationNodeService;
import com.ur.urcap.api.contribution.toolbar.swing.SwingToolbarService;

import com.yourcompany.thenewapp.impl.installation.DefaultInstallationNodeService;
import com.yourcompany.thenewapp.impl.programnode.DefaultProgramNodeService;
import com.yourcompany.thenewapp.impl.toolbar.DefaultToolbarService;

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

