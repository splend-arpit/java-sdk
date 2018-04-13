/*
 * Copyright 2014-2016 the libsecp256k1 contributors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package org.bitcoin;

/**
 * This class holds the context reference used in native methods
 * to handle ECDSA operations.
 */
public class Secp256k1Context {

    /**
     * Class level-declarations.
     */
    private static boolean enabled;
    private static long context = -1;

    /**
     *
     */
    static {
        try {
            System.loadLibrary("secp256k1");
            context = secp256k1_init_context();
            enabled = true;
        } catch (UnsatisfiedLinkError e) {
            System.out.println(e.toString());
            System.out.println("make sure to set -Djava.library.path to the the secp256k1 library path (e.e.g, .../secp256k1/.libs");
            enabled = false;
        }
    }

    /**
     *
     * @return
     */
    public static boolean isEnabled() {
        return enabled;
    }

    /**
     *
     * @return
     */
    public static long getContext() {
        return context;
    }

    /**
     *
     * @return
     */
    private static native long secp256k1_init_context();
}
