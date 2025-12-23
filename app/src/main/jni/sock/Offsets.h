namespace Offsets {
    uintptr_t GEngine = 0xea59fa8;

    namespace Engine {
        uintptr_t GameViewport = 0x810;
    }

    namespace GameViewportClient {
        uintptr_t World = 0x78;
    }

    namespace World {
        uintptr_t PersistentLevel = 0x30;
        uintptr_t NetDriver = 0x38;
    }

    namespace NetDriver {
        uintptr_t ServerConnection = 0x78;
    }

    namespace Player {
        uintptr_t PlayerController = 0x30;
    }

    namespace PlayerController {
        uintptr_t PlayerCameraManager = 0x4f0;
    }

    namespace PlayerCameraManager {
        uintptr_t CameraCache = 0x4d0;
    }

    namespace Character {
        uintptr_t Mesh = 0x4b8;
    }

    namespace StaticMeshComponent {
        uintptr_t MinLOD = 0x8c4;
    }

    namespace PetEntityComponent {
        uintptr_t FixAttachInfoList = 0x1a0 + 0x10;
    }
}