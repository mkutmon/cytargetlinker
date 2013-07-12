package org.cytargetlinker.app.internal;

import java.util.Properties;

import org.cytargetlinker.app.internal.gui.CyTargetLinkerPanel;
import org.cytoscape.application.CyApplicationManager;
import org.cytoscape.application.swing.CySwingApplication;
import org.cytoscape.application.swing.CytoPanelComponent;
import org.cytoscape.model.CyNetworkFactory;
import org.cytoscape.model.CyNetworkManager;
import org.cytoscape.model.events.NetworkDestroyedListener;
import org.cytoscape.service.util.AbstractCyActivator;
import org.cytoscape.view.layout.CyLayoutAlgorithmManager;
import org.cytoscape.view.model.CyNetworkViewFactory;
import org.cytoscape.view.model.CyNetworkViewManager;
import org.cytoscape.view.vizmap.VisualMappingFunctionFactory;
import org.cytoscape.view.vizmap.VisualMappingManager;
import org.cytoscape.view.vizmap.VisualStyleFactory;
import org.cytoscape.work.swing.DialogTaskManager;
import org.osgi.framework.BundleContext;

public class CyActivator extends AbstractCyActivator {

	@Override
	public void start(BundleContext context) throws Exception {
		CyApplicationManager cyApplicationManager = getService(context, CyApplicationManager.class);
		CyNetworkViewFactory networkViewFactory = getService(context, CyNetworkViewFactory.class);
		CyNetworkFactory networkFactory = getService(context, CyNetworkFactory.class);
		CyNetworkManager networkManager = getService(context, CyNetworkManager.class);
		DialogTaskManager dialogTaskManager = getService(context, DialogTaskManager.class);
		VisualMappingManager vmmServiceRef = getService(context,VisualMappingManager.class);
		VisualStyleFactory visualStyleFactoryServiceRef = getService(context,VisualStyleFactory.class);
		VisualMappingFunctionFactory vmfFactoryC = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=continuous)");
		VisualMappingFunctionFactory vmfFactoryD = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=discrete)");
		VisualMappingFunctionFactory vmfFactoryP = getService(context,VisualMappingFunctionFactory.class, "(mapping.type=passthrough)");
		CyLayoutAlgorithmManager cyAlgorithmManager = getService(context, CyLayoutAlgorithmManager.class);
		CyNetworkViewManager cyNetworkViewManager = getService(context, CyNetworkViewManager.class);
		CySwingApplication cySwingApplication = getService(context, CySwingApplication.class);
		// TODO: try with java 7
//		CyServiceRegistrar registrar = getService(context,CyServiceRegistrar.class); 
//		registrar.registerService(new RightClickMenu(), CyNodeViewContextMenuFactory.class, new Properties());
						
		Plugin plugin = new Plugin(networkFactory, networkManager, dialogTaskManager, networkViewFactory, vmmServiceRef, visualStyleFactoryServiceRef,
				vmfFactoryC, vmfFactoryD, vmfFactoryP, cyAlgorithmManager, cyApplicationManager, cyNetworkViewManager, cySwingApplication);
		QuickStartAction action = new QuickStartAction("Quick Start", plugin);
		ExtensionAction extAction = new ExtensionAction("Extend network", plugin);
		Properties properties = new Properties();

		CyTargetLinkerPanel panel = new CyTargetLinkerPanel(plugin);
		System.out.println("CREATE PANEL " + panel);
		
		registerService(context, plugin, NetworkDestroyedListener.class, new Properties());
		registerAllServices(context, action, properties);
		registerAllServices(context, extAction, properties);
		registerService(context, panel, CytoPanelComponent.class, new Properties());
	}

}