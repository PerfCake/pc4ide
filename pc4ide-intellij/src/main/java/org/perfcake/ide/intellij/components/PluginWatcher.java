/*
 *-----------------------------------------------------------------------------
 * pc4ide
 *
 * Copyright 2017 Jakub Knetl
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *-----------------------------------------------------------------------------
 */

package org.perfcake.ide.intellij.components;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.roots.ContentIterator;
import com.intellij.openapi.vfs.VfsUtilCore;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileCopyEvent;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileFilter;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.intellij.VirtualFileConverter;
import org.perfcake.ide.intellij.editor.ScenarioEditor;

/**
 * Plugin watcher watches a project for added extension JAR. If a JAR is added the watcher
 * adds the jar also to component manager.
 *
 * @author Jakub Knetl
 */
public class PluginWatcher implements ProjectComponent {

    static final Logger logger = Logger.getInstance(ScenarioEditor.class);

    private final Project project;
    private final ServiceManager serviceManager;

    public PluginWatcher(Project project, ServiceManager serviceManager) {
        this.serviceManager = serviceManager;
        this.project = project;
    }

    @Override
    public void projectOpened() {


    }

    @Override
    public void projectClosed() {

    }

    @Override
    public void initComponent() {
        LibraryListener listener = new LibraryListener();

        VirtualFileManager.getInstance().addVirtualFileListener(listener);

        LibraryLoader libraryLoader = new LibraryLoader(listener);
        VfsUtilCore.iterateChildrenRecursively(project.getBaseDir(), new VirtualFileFilter() {
            @Override
            public boolean accept(VirtualFile file) {
                return true;
            }
        }, libraryLoader);

        if (libraryLoader.isLibrariesModified()) {
            serviceManager.getComponentCatalogue().update();
        }

    }

    @Override
    public void disposeComponent() {

    }

    @NotNull
    @Override
    public String getComponentName() {
        return "perfcake-plugin-watcher";
    }

    private static class LibraryLoader implements ContentIterator {
        private final LibraryListener listener;
        private boolean librariesModified = false;

        public LibraryLoader(LibraryListener listener) {
            this.listener = listener;
        }

        @Override
        public boolean processFile(VirtualFile fileOrDir) {
            if (listener.isJarFile(fileOrDir) && isInLibDirectory(fileOrDir)) {
                listener.processFile(fileOrDir);
                librariesModified = true;
            }
            return true;
        }

        private boolean isInLibDirectory(VirtualFile jarFile) {
            if (jarFile.getParent() != null && "lib".equals(jarFile.getParent().getName())) {
                return true;
            }

            return false;
        }

        public boolean isLibrariesModified() {
            return librariesModified;
        }
    }

    private class LibraryListener extends VirtualFileAdapter {

        @Override
        public void fileCreated(@NotNull VirtualFileEvent event) {
            super.fileCreated(event);
            processFile(event.getFile());
        }

        @Override
        public void fileMoved(@NotNull VirtualFileMoveEvent event) {
            super.fileMoved(event);
            processFile(event.getFile());
        }

        @Override
        public void fileCopied(@NotNull VirtualFileCopyEvent event) {
            super.fileCopied(event);
            processFile(event.getFile());
        }

        public void processFile(VirtualFile file) {
            if (isJarFile(file)) {
                try {
                    ComponentCatalogue componentCatalogue = serviceManager.getComponentCatalogue();
                    componentCatalogue.addSoftwareLibrary(VirtualFileConverter.convertPath(file));
                    componentCatalogue.update();
                } catch (PerfCakeException | PerfCakeResourceException e) {
                    //TODO(show notification)
                    logger.warn("Cannot load  jar " + file, e);
                }
            }
        }

        public boolean isJarFile(VirtualFile file) {
            if (file == null) {
                return false;
            }

            if ("jar".equalsIgnoreCase(file.getExtension())) {
                return true;
            }

            return false;
        }
    }
}
