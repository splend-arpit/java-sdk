#!/usr/bin/env bash

cd secp256k1
./autogen.sh
./configure --enable-experimental --enable-module-ecdh --enable-jni --enable-module-recovery
make clean
make
# sudo make
cd ..
rm -rf target
mvn install
