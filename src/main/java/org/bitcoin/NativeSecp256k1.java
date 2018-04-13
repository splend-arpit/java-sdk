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

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import java.util.concurrent.locks.Lock;
import java.util.concurrent.locks.ReentrantReadWriteLock;

/**
 *
 */
public class NativeSecp256k1 {

    /**
     * Class level-declarations.
     */
    private static final ReentrantReadWriteLock reentrantReadWriteLock = new ReentrantReadWriteLock();
    private static final Lock readLock = reentrantReadWriteLock.readLock();
    private static final Lock writeLock = reentrantReadWriteLock.writeLock();
    private static ThreadLocal<ByteBuffer> nativeECDSABuffer = new ThreadLocal<ByteBuffer>();

    /**
     *
      * @param privateKey
     * @param hash
     * @return
     * @throws Exception
     */
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

        readLock.lock();
        try {
            return secp256k1_sign_and_serialize_compact(byteBuff, Secp256k1Context.getContext());
        } finally {
            readLock.unlock();
        }
    }

    /**
     *
     * @param byteBuff
     * @param context
     * @return
     */
    private static native byte[] secp256k1_sign_and_serialize_compact(ByteBuffer byteBuff, long context);
}
