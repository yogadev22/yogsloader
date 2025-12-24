cmake_minimum_required(VERSION 3.22.1)

project(Starkskillz)

add_library(
        Starkskillz
        SHARED
        desi.cpp
)

target_include_directories(
        Starkskillz
        PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}
)

target_link_libraries(
        Starkskillz
        log
        android
        GLESv2
)



add_library(
        nice
        SHARED
        bypass/main.cpp
        bypass/patch/KittyMemory.cpp
        bypass/patch/MemoryPatch.cpp
        bypass/patch/MemoryBackup.cpp
        bypass/patch/KittyUtils.cpp
        bypass/Substrate/hde64.c
		bypass/Substrate/SubstrateDebug.cpp
		bypass/Substrate/SubstrateHook.cpp
		bypass/Substrate/SubstratePosixMemory.cpp
        bypass/Substrate/And64InlineHook.cpp
)

target_include_directories(
        nice
        PRIVATE
        ${CMAKE_CURRENT_SOURCE_DIR}/bypass
)

target_link_libraries(
        nice
        log
        android
)

add_executable(libsock64.so sock/Shoolin.cpp)
target_link_libraries(libsock64.so log dl)
target_compile_definitions(libsock64.so PRIVATE DEBUG)

add_custom_command(TARGET libsock64.so POST_BUILD
		COMMAND ${CMAKE_COMMAND} -E make_directory "${CMAKE_CURRENT_SOURCE_DIR}/../assets"
		COMMAND ${CMAKE_COMMAND} -E copy
		$<TARGET_FILE:libsock64.so>
		"${CMAKE_CURRENT_SOURCE_DIR}/../assets/sock64"
		COMMENT "Moving libsock64.so to assets folder"
)