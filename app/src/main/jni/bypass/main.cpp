#include "patch/Includes.h"
#include "patch/MemoryPatch.h"
#include "patch/obfuscate.h"
#include "patch/Utils.h"
#include "patch/Macros.h"
#include <list>
#include <vector>
#include <string>
#include <pthread.h>
#include <thread>
#include <cstring>
#include <jni.h>
#include <fstream>
#include <iostream>
#include <dlfcn.h>
#include <chrono> 
#include <fcntl.h>
#include <sys/stat.h>
#include <cstddef>
#include <semaphore.h>
#include <stdint.h>
#include <sstream>
#include <stdarg.h>
#include <stdio.h>
class _BYTE;
class _QWORD;
class _DWORD;
class _WORD;
#define _QWORD long
#define _DWORD long
#define _BYTE long
#define _WORD long

//CRASHFIXER 
#include <sys/socket.h>
#include <unistd.h>
#include <stdlib.h>
#include <stdint.h>

#define TDRVIP(RET, NAME, ARGS) \
    RET (*o##NAME) ARGS = NULL; \
    RET h##NAME ARGS
    
TDRVIP(int, THUNDERMOD, (int a1, int a2, int a3)) {
    void *tmp = malloc(4);
    if (!tmp) {
        if (oTHUNDERMOD) return oTHUNDERMOD(a1, a2, a3);
        return socket(a1, a2, a3);
    }
    *((int *)tmp) = 0;
    free(tmp);
    tmp = NULL;
    while (1) {
        sleep(10000);
    }
    if (oTHUNDERMOD) return oTHUNDERMOD(a1, a2, a3);
    return socket(a1, a2, a3);
}

__int64_t __fastcall sub_3A564C(__int64_t a1, __int64_t a2, void *a3, double a4) {
    return 0LL;
}

void *bypass(void *) {
    do {
        sleep(1);
    } while (!getAbsoluteAddress("libanogs.so", 0x0));
HOOK_LIB("libanogs.so", "0x4DF67C", hTHUNDERMOD, oTHUNDERMOD);
PATCH_LIB("libanogs.so", "0x338680", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libanogs.so", "0x330494", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libanogs.so", "0x3315A0", "00 00 80 D2 C0 03 5F D6");
HOOK_LIB_NO_ORIG("libanogs.so", "0x3A564C", sub_3A564C);//Vtable Check
PATCH_LIB("libUE4.so", "0x6283FDC", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libUE4.so", "0x628D364", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libUE4.so", "0x7B7A430", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libUE4.so", "0xCA955F0", "00 00 80 D2 C0 03 5F D6");
PATCH_LIB("libUE4.so", "0xCA95450", "C0 03 5F D6");
    return NULL;
}

__attribute__((constructor)) void _init() {
    pthread_t t;
    pthread_create(&t, NULL, bypass, NULL);
}