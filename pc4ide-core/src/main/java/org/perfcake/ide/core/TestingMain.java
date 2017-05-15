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

package org.perfcake.ide.core;

import org.perfcake.ide.core.components.PerfCakeComponent;
import org.perfcake.ide.core.components.ReflectionComponentCatalogue;

/**
 * @author Jakub Knetl
 */
public class TestingMain {

    public static void main(String[] args) {

        ReflectionComponentCatalogue catalogue = new ReflectionComponentCatalogue();
        printSenders(catalogue);
        System.out.println("=====================================");
        printHasBobSender(catalogue);


    }

    public static void printSenders(ReflectionComponentCatalogue catalogue) {
        for (String s : catalogue.list(PerfCakeComponent.SENDER)) {
            System.out.println(s);
        }
    }

    public static void printHasBobSender(ReflectionComponentCatalogue catalogue) {
        System.out.println("BobSender: " + hasBobSender(catalogue));
    }

    public static boolean hasBobSender(ReflectionComponentCatalogue catalogue) {
        for (String s : catalogue.list(PerfCakeComponent.SENDER)) {
            if (s.contains("BobSender")) {
                return true;
            }
        }

        return false;
    }

}
