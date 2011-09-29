/*
 * Copyright © 2011 Ejwa Software. All rights reserved.
 *
 * This file is part of maven-gitdep-plugin. maven-gitdep-plugin
 * enables the use of git dependencies in Maven 3.
 *
 * maven-gitdep-plugin is free software; you can redistribute it and/or
 * modify it under the terms of the GNU Lesser General Public
 * License as published by the Free Software Foundation; either
 * version 3 of the License, or (at your option) any later version.
 *
 * maven-gitdep-plugin is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
 * GNU General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General
 * Public License along with maven-gitdep-plugin. If not, see
 * <http://www.gnu.org/licenses/>.
 */
package com.ejwa.mavengitdepplugin.util;

import com.ejwa.mavengitdepplugin.GitDependency;
import com.ejwa.mavengitdepplugin.model.POM;
import java.util.List;
import org.jdom.Element;
import org.jdom.Namespace;

public class GitDependencyHandler {
	private final GitDependency dependency;

	public GitDependencyHandler(GitDependency dependency) {
		this.dependency = dependency;
	}

	private Element findDependencyElement(POM pom) {
		final Namespace ns = pom.getProject().getNamespace();
		final Element dependenciesElement = pom.getProject().getChild("dependencies", ns);

		if (dependenciesElement != null) {
			final List<Element> dependencies = dependenciesElement.getChildren("dependency", ns);

			for (Element e : dependencies) {
				final String groupIdFound = e.getChild("groupId", ns).getTextTrim();
				final String artifactIdFound = e.getChild("artifactId", ns).getTextTrim();

				if (dependency.getGroupId().equals(groupIdFound) &&
				    dependency.getArtifactId().equals(artifactIdFound)) {
					return e;
				}
			}
		}

		return null;
	}

	public String getDependencyVersion(POM pom) {
		final Element dependencyElement = findDependencyElement(pom);

		if (dependencyElement != null) {
			final Namespace ns = pom.getProject().getNamespace();
			return dependencyElement.getChild("version", ns).getTextTrim();
		}

		throw new IllegalStateException("Failed to find version tag for the given dependency.");
	}

	public void setDependencyVersion(POM pom, String version) {
		final Element dependencyElement = findDependencyElement(pom);

		if (dependencyElement != null) {
			final Namespace ns = pom.getProject().getNamespace();
			dependencyElement.getChild("version", ns).setText(version);
		}
	}
}
