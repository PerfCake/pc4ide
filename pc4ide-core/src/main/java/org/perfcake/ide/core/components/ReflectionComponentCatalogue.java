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

package org.perfcake.ide.core.components;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.TimeUnit;
import java.util.concurrent.atomic.AtomicBoolean;
import org.perfcake.PerfCakeException;
import org.reflections.Reflections;
import org.reflections.util.ClasspathHelper;
import org.reflections.util.ConfigurationBuilder;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Represents an catalogue of PerfCake components on the classpath.
 *
 * @author jknetl
 */
public class ReflectionComponentCatalogue implements ComponentCatalogue {

    static final Logger logger = LoggerFactory.getLogger(ReflectionComponentCatalogue.class);

    // maximum update timeout. If update does not finishes until this timeout, it is automatically marked as finished
    // this allows to recover situation when a update thread is killed and it never releases the lock.
    protected static final long MAX_UPDATE_TIMEOUT = 20000;

    /**
     * Map of components type and their implementations.
     */
    private Map<PerfCakeComponent, List<String>> components;

    private AtomicBoolean updateInProgress = new AtomicBoolean(false);

    /**
     * List of additional packages which will be scanned.
     */
    private Set<String> additionalPackages;
    private Reflections reflections;

    /**
     * Creates a new catalogue. It also triggers scanning for components in given packages. Therefore, creating a new
     * {@link ReflectionComponentCatalogue} is costly operation and should not be called often.
     *
     * @param additionalPackages additional packages where catalogue will look for components.
     */
    public ReflectionComponentCatalogue(String... additionalPackages) {
        this.additionalPackages = new HashSet<String>(Arrays.asList(additionalPackages));
        components = new ConcurrentHashMap<>();
    }

    @Override
    public synchronized void update() {
        updateInProgress.set(true);
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                try {
                    final Reflections reflections = createReflections();
                    for (final PerfCakeComponent componentApi : PerfCakeComponent.values()) {
                        logger.debug("Scanning for perfcake components in packages {}.", String.join(",", additionalPackages));
                        List<String> list = new ArrayList<>();
                        for (Class<?> subType : reflections.getSubTypesOf(componentApi.getApi())) {
                            if (!subType.isInterface() && !Modifier.isAbstract(subType.getModifiers())) {
                                String name;
                                if (subType.getCanonicalName().startsWith(componentApi.getDefaultPackage())) {
                                    name = subType.getSimpleName();
                                } else {
                                    name = subType.getCanonicalName();
                                }

                                logger.trace("Component found. Type: {}, Name: {}", componentApi.name(), name);
                                list.add(name);
                            }
                        }

                        components.put(componentApi, list);
                    }

                } catch (Exception e) {
                    logger.warn("Exception during component scanning", e);
                } finally {
                    updateInProgress.set(false);
                }
            }
        };

        Thread thread = new Thread(runnable);
        thread.start();
    }

    @Override
    public synchronized List<String> list(PerfCakeComponent component) {
        waitUntilUpdateFinishes();
        List<String> list = components.get(component);
        if (list == null)  {
            list = Collections.emptyList();
        }
        return Collections.unmodifiableList(list);

    }

    /**
     * If an update was triggered, then this method waits until the update finishes.
     */
    public void waitUntilUpdateFinishes() {
        // If update is in progress, then wait
        long start = System.currentTimeMillis();
        while (updateInProgress.get()) {
            if (System.currentTimeMillis() - start > MAX_UPDATE_TIMEOUT) {
                updateInProgress.set(false);
                break;
            }
            try {
                TimeUnit.MILLISECONDS.sleep(500);
            } catch (InterruptedException e) {
                logger.debug("Wait for lock interrupted", e);
            }
        }
    }

    @Override
    public void addPackage(String... packages) {
        if (packages != null) {
            for (String p : packages) {
                additionalPackages.add(p);
            }
        }
    }

    @Override
    public void removePackage(String... packages) {
        if (packages != null) {
            for (String p : packages) {
                additionalPackages.remove(p);
            }
        }
    }

    /**
     * Creates and configure instance of {@link Reflections} which will be used for scanning.
     *
     * @return Reflections instance
     */
    protected Reflections createReflections() {

        Set<String> allPackages = new HashSet<>(Arrays.asList(DEFAULT_PACKAGES));
        allPackages.addAll(additionalPackages);
        ConfigurationBuilder configuration = new ConfigurationBuilder()
                .addClassLoader(URLClassLoader.class.getClassLoader())
                .addClassLoader(Thread.currentThread().getContextClassLoader())
                .addClassLoader(this.getClass().getClassLoader())
                .addClassLoader(ClassLoader.getSystemClassLoader());

        for (String p : allPackages) {
            configuration.addUrls(ClasspathHelper.forPackage(p));
        }
        reflections = new Reflections(configuration);


        return reflections;
    }

    @Override
    public void addSoftwareLibrary(Path jar) throws PerfCakeException {
        Method method = null;
        try {
            method = URLClassLoader.class.getDeclaredMethod("addURL", new Class[] {URL.class});
            method.setAccessible(true);
            method.invoke(ClassLoader.getSystemClassLoader(), new Object[] {jar.toUri().toURL()});
        } catch (NoSuchMethodException | IllegalAccessException
                | InvocationTargetException | MalformedURLException e) {
            throw new PerfCakeException("Cannot add external jar to classpath.", e);
        }

    }
}
