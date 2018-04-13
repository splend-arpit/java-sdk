/*
 * Copyright 2013 Google Inc.
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

import dispatchlabs.utils.Utils;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 * <p>This class holds native methods to handle ECDSA verification.</p>
 *
 * <p>You can find an example library that can be used for this at https://github.com/bitcoin/secp256k1</p>
 *
 * <p>To build secp256k1 for use with bitcoinj, run
 * `./configure --enable-jni --enable-experimental --enable-module-ecdh`
 * and `make` then copy `.libs/libsecp256k1.so` to your system library path
 * or point the JVM to the folder containing it with -Djava.library.path
 * </p>
 */
public class NativeSecp256k1 {

    private static final ReentrantReadWriteLock rwl = new ReentrantReadWriteLock();
    private static final Lock r = rwl.readLock();
    private static final Lock w = rwl.writeLock();
    private static ThreadLocal<ByteBuffer> nativeECDSABuffer = new ThreadLocal<ByteBuffer>();

    public static byte[] sign(byte[] privateKey, byte[] hash) throws Exception {
        ByteBuffer byteBuff = nativeECDSABuffer.get();
        if (byteBuff == null || byteBuff.capacity() < 64) {
            byteBuff = ByteBuffer.allocateDirect(64);
            byteBuff.order(ByteOrder.nativeOrder());
            nativeECDSABuffer.set(byteBuff);
        }
        byteBuff.rewind();
        byteBuff.put(privateKey);
        byteBuff.put(hash);

        r.lock();
        try {
            byte[] ret = secp256k1_ecdsa_sign_recoverable(byteBuff, Secp256k1Context.getContext());

            System.out.println(Utils.toHexString(ret));

            int fook =0;
        } finally {
            r.unlock();
        }

        return null;
    }

    private static native byte[] secp256k1_ecdsa_sign_recoverable(ByteBuffer byteBuff, long context);
    private static native byte[] secp256k1_ecdsa_recoverable_signature_serialize_compact(ByteBuffer byteBuff, long context);
}
