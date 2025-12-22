LOCAL_PATH := $(call my-dir)
MAIN_LOCAL_PATH := $(call my-dir)
include $(CLEAR_VARS)
LOCAL_MODULE    := Starkskillz
LOCAL_CFLAGS := -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w
LOCAL_CFLAGS += -fno-rtti -fno-exceptions -fpermissive
LOCAL_CPPFLAGS := -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w -Werror -s -std=c++17
LOCAL_CPPFLAGS += -Wno-error=c++11-narrowing -fms-extensions -fno-rtti -fno-exceptions -fpermissive
LOCAL_LDFLAGS += -Wl,--gc-sections,--strip-all, -llog
LOCAL_ARM_MODE := arm
LOCAL_C_INCLUDES += $(MAIN_LOCAL_PATH)
LOCAL_SRC_FILES := desi.cpp
LOCAL_LDLIBS := -llog -landroid -lGLESv2
include $(BUILD_SHARED_LIBRARY)

include $(CLEAR_VARS)
LOCAL_MODULE    := nice

LOCAL_CFLAGS := -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w
LOCAL_CFLAGS += -fno-rtti -fno-exceptions -fpermissive
LOCAL_CPPFLAGS := -Wno-error=format-security -fvisibility=hidden -ffunction-sections -fdata-sections -w -Werror -s -std=c++17
LOCAL_CPPFLAGS += -Wno-error=c++11-narrowing -fms-extensions -fno-rtti -fno-exceptions -fpermissive
LOCAL_LDFLAGS += -Wl,--gc-sections,--strip-all, -llog
LOCAL_ARM_MODE := arm

LOCAL_C_INCLUDES += $(MAIN_LOCAL_PATH)/bypass

LOCAL_SRC_FILES 		:=  bypass/main.cpp \
        bypass/patch/KittyMemory.cpp \
        bypass/patch/MemoryPatch.cpp \
        bypass/patch/MemoryBackup.cpp \
        bypass/patch/KittyUtils.cpp \
        bypass/Substrate/hde64.c \
		bypass/Substrate/SubstrateDebug.cpp \
		bypass/Substrate/SubstrateHook.cpp \
		bypass/Substrate/SubstratePosixMemory.cpp \
        bypass/Substrate/And64InlineHook.cpp

LOCAL_CPP_FEATURES                      := exceptions
LOCAL_LDLIBS                            := -llog -landroid -lEGL -lGLESv2 -lGLESv3 -lGLESv1_CM -lz
include $(BUILD_SHARED_LIBRARY)