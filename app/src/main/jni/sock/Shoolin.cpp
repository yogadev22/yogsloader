#include "INCLUDE/struct.h"
#include "Offsets.h"

struct ActorsEncryption64 {
    uint64_t Enc_1, Enc_2;
    uint64_t Enc_3, Enc_4;
};

struct Encryption_Chunk64 {
    uint32_t val_1, val_2, val_3, val_4;
    uint32_t val_5, val_6, val_7, val_8;
};

uint64_t DecryptActorsArray64(uint64_t uLevel, int Actors_Offset, int EncryptedActors_Offset) {
    if (uLevel < 0x10000000)
        return 0;

    if (Read<uint64_t>(uLevel + Actors_Offset) > 0)
        return uLevel + Actors_Offset;

    if (Read<uint64_t>(uLevel + EncryptedActors_Offset) > 0)
        return uLevel + EncryptedActors_Offset;

    auto Encryption = Read<ActorsEncryption64>(uLevel + EncryptedActors_Offset + 0x10);

    if (Encryption.Enc_1 > 0) {
        auto Enc = Read<Encryption_Chunk64>(Encryption.Enc_1 + 0x80);
        return (((Read<uint8_t>(Encryption.Enc_1 + Enc.val_1)
                  | (Read<uint8_t>(Encryption.Enc_1 + Enc.val_2) << 8))
                 | (Read<uint8_t>(Encryption.Enc_1 + Enc.val_3) << 0x10)) & 0xFFFFFF
                | ((uint64_t) Read<uint8_t>(Encryption.Enc_1 + Enc.val_4) << 0x18)
                | ((uint64_t) Read<uint8_t>(Encryption.Enc_1 + Enc.val_5) << 0x20)) &
               0xFFFF00FFFFFFFFFF
               | ((uint64_t) Read<uint8_t>(Encryption.Enc_1 + Enc.val_6) << 0x28)
               | ((uint64_t) Read<uint8_t>(Encryption.Enc_1 + Enc.val_7) << 0x30)
               | ((uint64_t) Read<uint8_t>(Encryption.Enc_1 + Enc.val_8) << 0x38);
    } else if (Encryption.Enc_2 > 0) {
        auto Encrypted_Actors = Read<uint64_t>(Encryption.Enc_2);
        if (Encrypted_Actors > 0) {
            return (uint16_t) (Encrypted_Actors - 0x400) & 0xFF00
                   | (uint8_t) (Encrypted_Actors - 0x04)
                   | (Encrypted_Actors + 0xFC0000) & 0xFF0000
                   | (Encrypted_Actors - 0x4000000) & 0xFF000000
                   | (Encrypted_Actors + 0xFC00000000) & 0xFF00000000
                   | (Encrypted_Actors + 0xFC0000000000) & 0xFF0000000000
                   | (Encrypted_Actors + 0xFC000000000000) & 0xFF000000000000
                   | (Encrypted_Actors - 0x400000000000000) & 0xFF00000000000000;
        }
    } else if (Encryption.Enc_3 > 0) {
        auto Encrypted_Actors = Read<uint64_t>(Encryption.Enc_3);
        if (Encrypted_Actors > 0)
            return (Encrypted_Actors >> 0x38) | (Encrypted_Actors << (64 - 0x38));
    } else if (Encryption.Enc_4 > 0) {
        auto Encrypted_Actors = Read<uint64_t>(Encryption.Enc_4);
        if (Encrypted_Actors > 0)
            return Encrypted_Actors ^ 0xCDCD00;
    }
    return 0;
}

PlayerBone getPlayerBone(uintptr_t pBase, FMinimalViewInfo &viewMatrix) {
    PlayerBone b;
    b.isBone = true;
    struct D3DMatrix oMatrix;
    uintptr_t boneAddr = getA(pBase + Offsets::Character::Mesh);
    struct D3DMatrix baseMatrix = getOMatrix(boneAddr + Offsets::PetEntityComponent::FixAttachInfoList);
    int bones[16] = {6, 5, 5, 1, 12, 33, 13, 34, 14, 35, 53, 57, 54, 58, 55, 59};
    boneAddr = getA(boneAddr + Offsets::StaticMeshComponent::MinLOD) + 0x30;
    oMatrix = getOMatrix(boneAddr + bones[0] * 48);
    b.head = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // neck 0
    oMatrix = getOMatrix(boneAddr + 4 * 48);
    b.neck = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // cheast 1
    oMatrix = getOMatrix(boneAddr + 4 * 48);
    b.cheast = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // pelvis 2
    oMatrix = getOMatrix(boneAddr + 1 * 48);
    b.pelvis = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lSh 3
    oMatrix = getOMatrix(boneAddr + 11 * 48);
    b.lSh = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rSh 4
    oMatrix = getOMatrix(boneAddr + 32 * 48);
    b.rSh = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lElb 5
    oMatrix = getOMatrix(boneAddr + 12 * 48);
    b.lElb = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rElb 6
    oMatrix = getOMatrix(boneAddr + 33 * 48);
    b.rElb = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lWr 7
    oMatrix = getOMatrix(boneAddr + 63 * 48);
    b.lWr = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rWr 8
    oMatrix = getOMatrix(boneAddr + 62 * 48);
    b.rWr = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lTh 9
    oMatrix = getOMatrix(boneAddr + 52 * 48);
    b.lTh = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rTh 10
    oMatrix = getOMatrix(boneAddr + 56 * 48);
    b.rTh = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lKn 11
    oMatrix = getOMatrix(boneAddr + 53 * 48);
    b.lKn = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rKn 12
    oMatrix = getOMatrix(boneAddr + 57 * 48);
    b.rKn = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // lAn 13 
    oMatrix = getOMatrix(boneAddr + 54 * 48);
    b.lAn = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));
    // rAn 14
    oMatrix = getOMatrix(boneAddr + 58 * 48);
    b.rAn = World2ScreenMain(viewMatrix, mat2Cord(oMatrix, baseMatrix));

    return b;
}

int main(int argc, char *argv[]) {
    if (!Create()) {
        perror("Creation Failed");
        return 0;
    }
    if (!Connect()) {
        perror("Connection Failed");
        return 0;
    }
    
    pid = getPid("com.tencent.ig");
    if (pid == 0) {
        pid = getPid("com.pubg.krmobile");
        if (pid == 0) {
            pid = getPid("com.vng.pubgmobile");
            if (pid == 0) {
                pid = getPid("com.rekoo.pubgm");
                if (pid == 0) {
                    pid = getPid("com.pubg.imobile");
                    if (pid == 0) {
                        printf("\nGame is not running");
                        Close();
                        return 0;
                    }
                }
            }
        }
    }

    uintptr_t base = getBase();

	Vec3 exyz;
	int myteamID = 101;
	char name[100];
	
    Request request {};
    Response response {};

    while ((receive((void *) &request) > 0)) {
        height = request.ScreenHeight;
        width = request.ScreenWidth;

        response.Success = false;
		response.Identified = false;
        response.PlayerCount = 0;
        
        uintptr_t uWorld = getA(getA(getA(base + Offsets::GEngine) + Offsets::Engine::GameViewport) + Offsets::GameViewportClient::World);
        uintptr_t level = getA(uWorld + Offsets::World::PersistentLevel);
        uintptr_t playerController = getA(getA(getA(uWorld + Offsets::World::NetDriver) + Offsets::NetDriver::ServerConnection) + Offsets::Player::PlayerController);
        uintptr_t CameraManager = getA(playerController + Offsets::PlayerController::PlayerCameraManager);

        FCameraCacheEntry CameraCache = FCameraCacheEntry();
		
		if (CameraManager) {
            CameraCache = Read<FCameraCacheEntry>(CameraManager + Offsets::PlayerCameraManager::CameraCache);
        }
		
		Ulevel ulevel;
		uint64_t ActorArray = DecryptActorsArray64(level, 0xA0, 0x448);
        pvm(ActorArray, &ulevel, sizeof(ulevel));
		
		if (ulevel.size < 1 || ulevel.size > 0x1000 || !isValid(ulevel.addr)) {
            send((void *) &response, sizeof(response));
            continue;
        }

        for (int i = 0; i < ulevel.size; i++) {
        
            uintptr_t pBase = getA(ulevel.addr + i * SIZE);
            
            if (!isValid(pBase)) 
				continue;

            response.fov = tanf(CameraCache.POV.FOV * ((float) PI / 360.0f));
            
            if (getF(pBase + 0x26c8) == 479.5f) {

				PlayerData *data = &response.Players[response.PlayerCount];

				uintptr_t boneAddr = getA(pBase + Offsets::Character::Mesh);
				exyz = mat2Cord(getOMatrix(getA(boneAddr + Offsets::StaticMeshComponent::MinLOD) + 6 * 48), getOMatrix(boneAddr + Offsets::PetEntityComponent::FixAttachInfoList));
				data->HeadLocation = World2Screen(CameraCache.POV, exyz);

			    data->Distance = getDistance(CameraCache.POV.Location, exyz);

				if (data->Distance > 400.0f) {
                    continue;
    			}
                
				if (data->HeadLocation.Z != 1.0f && data->HeadLocation.X < width + 100 && data->HeadLocation.X > -50) {
				    data->Bone = getPlayerBone(pBase, CameraCache.POV);
					uintptr_t boneAddr = getA(pBase + Offsets::Character::Mesh);
					Vec3 head = mat2Cord(getOMatrix(getA(boneAddr + Offsets::StaticMeshComponent::MinLOD) + 6 * 48), getOMatrix(boneAddr + Offsets::PetEntityComponent::FixAttachInfoList));
					Vec3 precise_Head = World2Screen(CameraCache.POV, head);
                    Vec3 root = mat2Cord(getOMatrix(getA(boneAddr + Offsets::StaticMeshComponent::MinLOD)), getOMatrix(boneAddr + Offsets::PetEntityComponent::FixAttachInfoList));
                    Vec3 precise_root = World2Screen(CameraCache.POV, root);
					data->Precise = getPrecise(data->Bone, precise_Head, precise_root);
                }
                
				if (response.PlayerCount >= maxplayerCount) {
                    continue;
				}

                response.PlayerCount++;
            }
        }
        if (response.PlayerCount > 0) {
            response.Success = true;
        }
        send((void *) &response, sizeof(response));
    }
}
