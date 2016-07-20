/*****************************************************************************
 * Copyright (c) 2016 CEA LIST.
 *
 *
 * All rights reserved. This program and the accompanying materials
 * are made available under the terms of the Eclipse Public License v1.0
 * which accompanies this distribution, and is available at
 * http://www.eclipse.org/legal/epl-v10.html
 *
 * Contributors:
 *  David LOPEZ BETANCUR (CEA LIST)
 *  Sebastien REVOL (CEA LIST)
 *
 *****************************************************************************/
package org.eclipse.papyrus.moka.xygraph.mapping.writing;

import org.eclipse.nebula.visualization.xygraph.figures.IXYGraph;
import org.eclipse.papyrus.moka.xygraph.mapping.common.XYGraphBinder;

public interface XYGraphBuildStrategy {
	public IXYGraph buildGraph(XYGraphBinder map);
}