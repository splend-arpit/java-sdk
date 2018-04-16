#include <jni.h>
#include <stdlib.h>
#include <stdint.h>
#include <string.h>
#include <stdio.h>
#include "org_bitcoin_NativeSecp256k1.h"
#include "include/secp256k1.h"
#include "include/secp256k1_ecdh.h"
#include "include/secp256k1_recovery.h"
#include "scalar_impl.h"

/*
 * Class:     org_bitcoin_NativeSecp256k1
 * Method:    secp256k1_sign_and_serialize_compact
 * Signature: (Ljava/nio/ByteBuffer;J)[B
 */
JNIEXPORT jbyteArray JNICALL Java_org_bitcoin_NativeSecp256k1_secp256k1_1sign_1and_1serialize_1compact(JNIEnv *env, jclass jClass, jobject byteBufferObject, jlong contextObject)
{
  secp256k1_context *context = (secp256k1_context*)(uintptr_t)contextObject;
  unsigned char* buffer = (unsigned char*) (*env)->GetDirectBufferAddress(env, byteBufferObject);
  const unsigned char* privateKey = (unsigned char*) buffer;
  const unsigned char* hash = (unsigned char*) (buffer + 32);

  // Create signature.
  secp256k1_ecdsa_recoverable_signature sigstruct;
  int ret = secp256k1_ecdsa_sign_recoverable(context, &sigstruct, hash, privateKey, NULL, NULL);
  if (ret != 1) {
    return NULL;
  }

  // Serialize and compact signature.
  unsigned char signature[65];
  int recid;
  ret = secp256k1_ecdsa_recoverable_signature_serialize_compact(context, &signature[0], &recid, &sigstruct);
  if (ret != 1) {
    return NULL;
  }
  signature[64] = recid;

  jbyteArray jByteArray = (*env)->NewByteArray(env, 65);
  (*env)->SetByteArrayRegion(env, jByteArray, 0, 65, (jbyte*)signature);
  return jByteArray;
}