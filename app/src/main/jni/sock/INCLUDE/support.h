#ifndef SUPPORT_H
#define SUPPORT_H

#include "socket.h"
#define PI 3.141592653589793238

ssize_t process_v(pid_t __pid, struct iovec* __local_iov, unsigned long __local_iov_count, struct iovec* __remote_iov, unsigned long __remote_iov_count, unsigned long __flags) {
	return syscall(process_vm_readv_syscall, __pid, __local_iov, __local_iov_count, __remote_iov, __remote_iov_count, __flags);
}

ssize_t process_v2(pid_t __pid, struct iovec* __local_iov, unsigned long __local_iov_count, struct iovec* __remote_iov, unsigned long __remote_iov_count, unsigned long __flags) {
	return syscall(process_vm_writev_syscall, __pid, __local_iov, __local_iov_count, __remote_iov, __remote_iov_count, __flags);
}

int pvm(uintptr_t address, void* buffer,int size) {
	struct iovec local[1];
	struct iovec remote[1];

	local[0].iov_base = (void*)buffer;
	local[0].iov_len = size;
	remote[0].iov_base = (void*)address;
	remote[0].iov_len = size;
	ssize_t bytes = process_v(pid, local, 1, remote, 1, 0);
	return bytes == size;
}

void pvm2(uintptr_t address, void* buffer,int size) {
	struct iovec local[1];
	struct iovec remote[1];

	local[0].iov_base = (void*)buffer;
	local[0].iov_len = size;
	remote[0].iov_base = (void*)address;
	remote[0].iov_len = size;

	process_v2(pid, local, 1, remote, 1, 0);
}

struct D3DMatrix ToMatrixWithScale(struct Vec3 translation,struct Vec3 scale,struct Vec4 rot)
{
	struct D3DMatrix m;

	m._41 = translation.X;
	m._42 = translation.Y;
	m._43 = translation.Z;

	float x2 = rot.X + rot.X;
	float y2 = rot.Y + rot.Y;
	float z2 = rot.Z + rot.Z;

	float xx2 = rot.X * x2;
	float yy2 = rot.Y * y2;
	float zz2 = rot.Z * z2;

	m._11 = (1.0f - (yy2 + zz2)) * scale.X;
	m._22 = (1.0f - (xx2 + zz2)) * scale.Y;
	m._33 = (1.0f - (xx2 + yy2)) * scale.Z;

	float wx2 = rot.W * x2;
	float yz2 = rot.Y * z2;

	m._23 = (yz2 + wx2) * scale.Y;
	m._32 = (yz2 - wx2) * scale.Z;

	float xy2 = rot.X * y2;
	float wz2 = rot.W * z2;

	m._12 = (xy2 + wz2) * scale.X;
	m._21 = (xy2 - wz2) * scale.Y;

	float xz2 = rot.X * z2;
	float wy2 = rot.W * y2;

	m._13 = (xz2 - wy2) * scale.X;
	m._31 = (xz2 + wy2) * scale.Z;

	m._14 = 0.0f;
	m._24 = 0.0f;
	m._34 = 0.0f;
	m._44 = 1.0f;

	return m;
}

struct Vec3 mat2Cord(struct D3DMatrix pM1,struct D3DMatrix pM2)
{
	struct  Vec3 pOut;

	pOut.X = pM1._41 * pM2._11 + pM1._42 * pM2._21 + pM1._43 * pM2._31 + pM1._44 * pM2._41;
	pOut.Y = pM1._41 * pM2._12 + pM1._42 * pM2._22 + pM1._43 * pM2._32 + pM1._44 * pM2._42;
	pOut.Z = pM1._41 * pM2._13 + pM1._42 * pM2._23 + pM1._43 * pM2._33 + pM1._44 * pM2._43;

	return pOut;
}

uintptr_t getBase() {
    FILE *fp;
    uintptr_t addr = 0;
    char filename[40], buffer[1024];
    snprintf(filename, sizeof(filename), "/proc/%d/maps", pid);
    fp = fopen(filename, "rt");
    if (fp != NULL) {
        while (fgets(buffer, sizeof(buffer), fp)) {
            if (strstr(buffer, "libUE4.so")) {
                addr = (uintptr_t)strtoul(buffer, NULL, 16);
                break;
            }
        }
        fclose(fp);
    }
    return addr;
}

pid_t getPid(char * name) {
	char text[69];
	pid_t pid = 0;
	sprintf(text,"pidof %s",name);
	FILE *chkRun = popen(text, "r");
	if (chkRun) {
		char output[10];
		fgets(output ,10,chkRun);
		pclose(chkRun);
		pid= atoi(output);
	}
	if (pid < 10) {
		DIR* dir = NULL;
		struct dirent* ptr = NULL;
		FILE* fp = NULL;
		char filepath[256];
		char filetext[128];
		dir = opendir("/proc");
		if (NULL != dir) {
			while ((ptr = readdir(dir)) != NULL) {
				if ((strcmp(ptr->d_name, ".") == 0) || (strcmp(ptr->d_name, "..") == 0))
					continue;
				if (ptr->d_type != DT_DIR)
					continue;
				sprintf(filepath, "/proc/%s/cmdline", ptr->d_name);
				fp = fopen(filepath, "r");
				if (NULL != fp) {
					fgets(filetext, sizeof(filetext), fp);
					if (strcmp(filetext, name) == 0) {
						fclose(fp);
						break;
					}
					fclose(fp);
				}
			}
		}
		if (readdir(dir) == NULL) {
			closedir(dir);
			return 0;
		}
		closedir(dir);
		pid= atoi(ptr->d_name);
	}
	return pid;
}

bool isValidPlayer(int oType) {
    return (oType == 0x3ec2a00 || oType == 0x3ec2800);
}

uintptr_t getA(uintptr_t address) {
	uintptr_t buff;
    pvm(address, &buff, SIZE);
    return buff;
}

int getI(uintptr_t address) {
	int buff;
    pvm(address, &buff, SIZE);
    return buff;
}

float getF(uintptr_t address) {
	float buff;
    pvm(address, &buff, SIZE);
    return buff;
}

ssize_t process_v3(pid_t __pid, const struct iovec *__local_iov, unsigned long __local_iov_count,
				  const struct iovec *__remote_iov, unsigned long __remote_iov_count,
				  unsigned long __flags, bool iswrite)
{
	return syscall((iswrite ? process_vm_writev_syscall : process_vm_readv_syscall), __pid,
				   __local_iov, __local_iov_count, __remote_iov, __remote_iov_count, __flags);
}


bool pvm3(void *address, void *buffer, size_t size, bool iswrite)
{
	struct iovec local[1];
	struct iovec remote[1];
	local[0].iov_base = buffer;
	local[0].iov_len = size;
	remote[0].iov_base = address;
	remote[0].iov_len = size;
	if (pid < 0)
	{
		return false;
	}
	ssize_t bytes = process_v3(pid, local, 1, remote, 1, 0, iswrite);
	return bytes == size;
}

template<typename T>
T Read(unsigned long address) {
    T data;
    pvm3(reinterpret_cast < void *>(address), reinterpret_cast<void *>(&data), sizeof(T), false);
    return data;
}

bool vm_readv(unsigned long address, void *buffer, size_t size)
{
	return pvm3(reinterpret_cast < void *>(address), buffer, size, false);
}

bool vm_writev(unsigned long address, void *buffer, size_t size)
{
	return pvm3(reinterpret_cast < void *>(address), buffer, size, true);
}

int isValid(uintptr_t addr) {
    return (addr >= 0x1000000000 || addr <= 0xEFFFFFFFFF || addr % SIZE == 0);
}

bool isValidPosition(const Vec2 &location) {
    return (location.X < width && location.Y < height && location.X > 0 && location.Y > 0);
}

float getDistance3d(const Vec3 &mxyz, const Vec3 &exyz) {
    return sqrt((mxyz.X - exyz.X) * (mxyz.X - exyz.X) + (mxyz.Y - exyz.Y) * (mxyz.Y - exyz.Y) + (mxyz.Z - exyz.Z) * (mxyz.Z - exyz.Z));
}

float getDistance(struct Vec3 mxyz,struct Vec3 exyz)
{
	return sqrt ((mxyz.X-exyz.X) * (mxyz.X-exyz.X) + (mxyz.Y-exyz.Y) * (mxyz.Y-exyz.Y) + (mxyz.Z-exyz.Z) * (mxyz.Z-exyz.Z)) / 100;
}

struct D3DMatrix getOMatrix(uintptr_t boneAddr)
{
    float oMat[11];
    pvm(boneAddr,&oMat,sizeof(oMat));
	
    rot.X=oMat[0];
	rot.Y=oMat[1];
	rot.Z=oMat[2];
	rot.W=oMat[3];
			
	tran.X=oMat[4];
	tran.Y=oMat[5];
	tran.Z=oMat[6];
			
	scale.X=oMat[8];
	scale.Y=oMat[9];
	scale.Z=oMat[10];
			
	return ToMatrixWithScale(tran,scale,rot);
}

struct Vec2 getPointingAngle(struct Vec3 camera, struct Vec3 xyz, float distance)
{
	struct Vec2 PointingAngle;
	float ytime = distance / 88000;

	xyz.Z = xyz.Z + 360 * ytime * ytime;

	float zbcx = xyz.X - camera.X;
	float zbcy = xyz.Y - camera.Y;
	float zbcz = xyz.Z - camera.Z;
	PointingAngle.Y = atan2(zbcy, zbcx) * 180 / PI;	// 57.3
	PointingAngle.X = atan2(zbcz, sqrt(zbcx * zbcx + zbcy * zbcy)) * 180 / PI;

	return PointingAngle;
}

Vec2 WorldToRadar(float yaw, const Vec3 &enemyPos, const Vec3 &localPos) {
    float theta = (yaw - 90) * ((float)M_PI / 180.0f);
    float s = (float) std::sin(theta);
    float c = (float) std::cos(theta);
    
    float radarObjectX = (localPos.X - enemyPos.X) / 170;
    float radarObjectY = (localPos.Y - enemyPos.Y) / 170;
    
    return Vec2(
        (radarObjectX * c + radarObjectY * s),
        (-radarObjectX * s + radarObjectY * c)
    );
}

struct FMatrix {
    float M[4][4];
};

struct FRotator {
    float Pitch;
    float Yaw;
    float Roll;
};

struct FMinimalViewInfo
{
    Vec3 Location;
    Vec3 LocationLocalSpace;
    FRotator Rotation;
    float FOV;

};

struct FCameraCacheEntry {
    float TimeStamp;
    char pad[0xc];
    FMinimalViewInfo POV;
};

FMatrix RotToMatrix(FRotator rotation) {
    float radPitch = rotation.Pitch * ((float)M_PI / 180.0f);
    float radYaw = rotation.Yaw * ((float)M_PI / 180.0f);
    float radRoll = rotation.Roll * ((float)M_PI / 180.0f);

    float SP = sinf(radPitch);
    float CP = cosf(radPitch);
    float SY = sinf(radYaw);
    float CY = cosf(radYaw);
    float SR = sinf(radRoll);
    float CR = cosf(radRoll);

    FMatrix matrix;

    matrix.M[0][0] = (CP * CY);
    matrix.M[0][1] = (CP * SY);
    matrix.M[0][2] = (SP);
    matrix.M[0][3] = 0;

    matrix.M[1][0] = (SR * SP * CY - CR * SY);
    matrix.M[1][1] = (SR * SP * SY + CR * CY);
    matrix.M[1][2] = (-SR * CP);
    matrix.M[1][3] = 0;

    matrix.M[2][0] = (-(CR * SP * CY + SR * SY));
    matrix.M[2][1] = (CY * SR - CR * SP * SY);
    matrix.M[2][2] = (CR * CP);
    matrix.M[2][3] = 0;

    matrix.M[3][0] = 0;
    matrix.M[3][1] = 0;
    matrix.M[3][2] = 0;
    matrix.M[3][3] = 1;

    return matrix;
}

Vec2 World2ScreenMain(FMinimalViewInfo camViewInfo, Vec3 worldLocation) {
    FMatrix tempMatrix = RotToMatrix(camViewInfo.Rotation);
    Vec3 vAxisX(tempMatrix.M[0][0], tempMatrix.M[0][1], tempMatrix.M[0][2]);
    Vec3 vAxisY(tempMatrix.M[1][0], tempMatrix.M[1][1], tempMatrix.M[1][2]);
    Vec3 vAxisZ(tempMatrix.M[2][0], tempMatrix.M[2][1], tempMatrix.M[2][2]);
    Vec3 vDelta = worldLocation - camViewInfo.Location;
    Vec3 vTransformed(Vec3::Dot(vDelta, vAxisY), Vec3::Dot(vDelta, vAxisZ), Vec3::Dot(vDelta, vAxisX));

    if (vTransformed.Z < 1.0f) { vTransformed.Z = 1.0f;}

    float fov = camViewInfo.FOV;
    float screenCenterX = (width / 2.0f);
    float screenCenterY = (height / 2.0f);

    Vec2 vec2;
    vec2.X = screenCenterX + vTransformed.X * (screenCenterX / tanf(fov * ((float)M_PI / 360))) / vTransformed.Z;
    vec2.Y = screenCenterY - vTransformed.Y * (screenCenterX / tanf(fov * ((float)M_PI / 360))) / vTransformed.Z;
    return vec2;
}

Vec3 World2Screen(FMinimalViewInfo camViewInfo, Vec3 worldLocation) {
    FMatrix tempMatrix = RotToMatrix(camViewInfo.Rotation);
    Vec3 vAxisX(tempMatrix.M[0][0], tempMatrix.M[0][1], tempMatrix.M[0][2]);
    Vec3 vAxisY(tempMatrix.M[1][0], tempMatrix.M[1][1], tempMatrix.M[1][2]);
    Vec3 vAxisZ(tempMatrix.M[2][0], tempMatrix.M[2][1], tempMatrix.M[2][2]);
    Vec3 vDelta = worldLocation - camViewInfo.Location;
    Vec3 vTransformed(Vec3::Dot(vDelta, vAxisY), Vec3::Dot(vDelta, vAxisZ), Vec3::Dot(vDelta, vAxisX));

    if (vTransformed.Z < 1.0f) { vTransformed.Z = 1.0f;}

    float fov = camViewInfo.FOV;
    float screenCenterX = (width / 2.0f);
    float screenCenterY = (height / 2.0f);

    Vec3 vec3;

    if (tanf(fov * ((float)M_PI / 360)) < 0.01f)
        vec3.Z = 1;
    else
        vec3.Z = 0;
    vec3.X = screenCenterX + vTransformed.X * (screenCenterX / tanf(fov * ((float)M_PI / 360))) / vTransformed.Z;
    vec3.Y = screenCenterY - vTransformed.Y * (screenCenterX / tanf(fov * ((float)M_PI / 360))) / vTransformed.Z;
    return vec3;
}

char* getText(uintptr_t addr) {
	static char txt[42];
	memset(txt, 0, 42);
	char buff[41];
	pvm(addr + 4 + SIZE, &buff, 41);
	int i;
	for (i = 0; i < 41; i++) {
		if (buff[i] == 0) {
            break;
        }
		txt[i] = buff[i];
	}
	txt[i + 1] = '\0';
	return txt;
}

char *getNameByte(uintptr_t address) {
	char static lj[64];
	memset(lj, 0, 64);
	unsigned short int nameI[32];
	pvm(address, nameI, sizeof(nameI));
	char s[10];
	for (int i = 0; i < 32; i++) {
		if (nameI[i] == 0) {
			break;
        }
		sprintf(s, "%d:", nameI[i]);
		strcat(lj, s);
	}
	lj[63] = '\0';

	return lj;
}

template<typename T>
void Write(uintptr_t addr, T data) {
    iovec l_iov{&data, sizeof(T)};
    iovec r_iov{reinterpret_cast<void *>(addr), sizeof(T)};
    syscall(process_vm_writev_syscall, pid, &l_iov, 1, &r_iov, 1, 0);
}

template <typename T>
struct TArray {
    uintptr_t base;
    int32_t count;
    int32_t max;

    std::vector<T> ToVec() const {
        if (!IsValid()) {
            return {};
        }
        std::vector<T> vec{};
        vec.resize(static_cast<size_t>(count));
        iovec l_iov{&vec[0], static_cast<size_t>(count) * sizeof(T)};
        iovec r_iov{reinterpret_cast<void*>(base), static_cast<size_t>(count) * sizeof(T)};
        syscall(process_vm_readv_syscall, pid, &l_iov, 1, &r_iov, 1, 0);
        return vec;
    }

    T operator[](size_t u) const {
        return Read<T>(base + u * sizeof(T));
    }

    bool IsValid() const {
        return base && count > 0 && count <= max && max > 0;
    }
};
#endif
