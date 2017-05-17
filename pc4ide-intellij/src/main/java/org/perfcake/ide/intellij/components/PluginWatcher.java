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

import com.intellij.notification.Notification;
import com.intellij.notification.NotificationType;
import com.intellij.notification.Notifications;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.vfs.VirtualFile;
import com.intellij.openapi.vfs.VirtualFileAdapter;
import com.intellij.openapi.vfs.VirtualFileCopyEvent;
import com.intellij.openapi.vfs.VirtualFileEvent;
import com.intellij.openapi.vfs.VirtualFileManager;
import com.intellij.openapi.vfs.VirtualFileMoveEvent;
import java.util.Arrays;
import java.util.List;
import org.jetbrains.annotations.NotNull;
import org.perfcake.PerfCakeException;
import org.perfcake.ide.core.components.ComponentCatalogue;
import org.perfcake.ide.core.exception.PerfCakeResourceException;
import org.perfcake.ide.editor.ServiceManager;
import org.perfcake.ide.intellij.IntellijUtils;
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
    private List<String> libraryDirs = Arrays.asList("lib", "src/main/lib");

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
        long start = System.currentTimeMillis();

        LibraryLoader libraryLoader = new LibraryLoader(project, serviceManager);

        // load libraries in PROJECT/lib and PROJECT/src/main/resources/lib directories
        findLibraries(libraryLoader);

        // attach listener which monitors lib directories for new components
        LibraryListener listener = new LibraryListener(libraryLoader);
        VirtualFileManager.getInstance().addVirtualFileListener(listener);

        long time = System.currentTimeMillis() - start;
        logger.debug("Plugin watcher initialization. Took " + time);

    }

    private void findLibraries(LibraryLoader libraryLoader) {
        boolean librariesModified = false;
        for (String dir : libraryDirs) {

            VirtualFile libDir = project.getBaseDir().findFileByRelativePath(dir);

            if (libDir != null && libDir.isDirectory()) {
                VirtualFile[] files = libDir.getChildren();
                for (VirtualFile f : files) {
                    if (libraryLoader.isJarFile(f)) {
                        libraryLoader.addLibrary(f);
                        librariesModified = true;
                    }
                }
            }
        }

        if (librariesModified) {
            libraryLoader.scanLibraries();
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

    private class LibraryLoader {
        private Project project;
        private ServiceManager serviceManager;

        public LibraryLoader(Project project, ServiceManager serviceManager) {
            this.project = project;
            this.serviceManager = serviceManager;
        }

        public void addLibrary(VirtualFile library) {
            try {
                ComponentCatalogue componentCatalogue = serviceManager.getComponentCatalogue();
                componentCatalogue.addSoftwareLibrary(VirtualFileConverter.convertPath(library));
            } catch (PerfCakeException | PerfCakeResourceException e) {
                Notification notification = new Notification(IntellijUtils.PERFCAKE_NOTIFICATION_ID, "Cannot load a library",
                        String.format("Library %s cannot be loaded. See log for more details.", library.getName()),
                        NotificationType.WARNING);
                Notifications.Bus.notify(notification);
                logger.warn("Cannot load  jar " + library, e);
            }
        }

        public boolean isInLibDirectory(VirtualFile jarFile) {
            if (jarFile == null || jarFile.getParent() == null) {
                return false;
            }

            for (String dir : libraryDirs) {
                VirtualFile libDir = project.getBaseDir().findFileByRelativePath(dir);

                if (libDir != null && libDir.equals(jarFile.getParent())) {
                    return true;
                }
            }

            return false;
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

        public void scanLibraries() {
            serviceManager.getComponentCatalogue().update();
        }
    }

    private class LibraryListener extends VirtualFileAdapter {

        private LibraryLoader loader;

        public LibraryListener(LibraryLoader loader) {
            this.loader = loader;
        }

        @Override
        public void fileCreated(@NotNull VirtualFileEvent event) {
            super.fileCreated(event);
            processByLoader(event.getFile());
        }

        @Override
        public void fileDeleted(@NotNull VirtualFileEvent event) {
            super.fileDeleted(event);
            processByLoader(event.getFile());
        }

        @Override
        public void fileMoved(@NotNull VirtualFileMoveEvent event) {
            super.fileMoved(event);
            processByLoader(event.getFile());
        }

        @Override
        public void fileCopied(@NotNull VirtualFileCopyEvent event) {
            super.fileCopied(event);
            processByLoader(event.getFile());
        }

        private void processByLoader(VirtualFile file) {
            if (loader.isInLibDirectory(file) && loader.isJarFile(file)) {
                loader.addLibrary(file);
                loader.scanLibraries();
            }
        }
    }
}
