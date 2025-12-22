#ifndef DESI_IMPORTANT_HACK_H
#define DESI_IMPORTANT_HACK_H

#include "socket.h"

float x, y;
Request request;
Response response;
char extra[30];
int botCount, playerCount;
Color clrEnemy, clrFilled, clrEdge, clrBox, clrSkeleton, clrHealth;
Options options { 1, -1, false, 1, false, 201, false };

bool isPositionValid(Vec2 position) {
    return (position.x > request.ScreenWidth || position.y > request.ScreenHeight || position.x < 0.0f || position.y < 0.0f);
}

bool isDotUnderCircle(Vec2 pos, float radius) {
    float diffX = abs(pos.x), diffY = abs(pos.y);
    return (diffX * diffX + diffY * diffY) <= radius;
}

int isOutsideSafezone(Vec3 pos, Vec3 screen) {
    Vec2 mSafezoneTopLeft(screen.x * 0.04f, screen.y * 0.04f);
    Vec2 mSafezoneBottomRight(screen.x * 0.96f, screen.y * 0.96f);

    int result = 0;
    if (pos.y < mSafezoneTopLeft.y) {
        result |= 1;
    }
    if (pos.x > mSafezoneBottomRight.x) {
        result |= 2;
    }
    if (pos.y > mSafezoneBottomRight.y) {
        result |= 4;
    }
    if (pos.x < mSafezoneTopLeft.x) {
        result |= 8;
    }
    return result;
}

Vec2 pushToScreenBorder(Vec2 Pos, Vec2 screen, int borders, int offset) {
    int x = (int) Pos.x;
    int y = (int) Pos.y;
    if ((borders & 1) == 1) {
        y = 0 - offset;
    }
    if ((borders & 2) == 2) {
        x = (int) screen.x + offset;
    }
    if ((borders & 4) == 4) {
        y = (int) screen.y + offset;
    }
    if ((borders & 8) == 8) {
        x = 0 - offset;
    }
    return Vec2(x, y);
}

void DrawESP(ESP esp, int screenWidth, int screenHeight) {
    botCount = 0;
    playerCount = 0;
    request.ScreenHeight = screenHeight;
    request.ScreenWidth = screenWidth;
    request.options = options;
    request.Mode = InitMode;

    send((void *) &request, sizeof(request));
    receive((void *) &response);

    float mScaleX = screenWidth / (float) 2340;
    float mScaleY = screenHeight / (float) 1080;
    Vec2 screen = Vec2(screenWidth, screenHeight);
    Vec2 center = Vec2(screenWidth / 2, screenHeight / 2);

    esp.DrawTextName(Color(255, 0, 0, 255), Vec2(210, 50), (mScaleY * 35), response.Identified);

    if (response.Success) {
        if (options.openState == 0) {
            esp.DrawCircle(Color(250, 0, 0), center, request.options.aimingRange, (mScaleY * 1.4f));
        }

        if (options.tracingStatus) {
            float py = screenHeight / 2;
            float px = screenWidth / 2;
            esp.DrawFilledRect(Color::Green(50),
                               Vec2(request.options.touchY - request.options.touchSize / 2,
                                    py * 2 - request.options.touchX +
                                    request.options.touchSize / 2),
                               Vec2(request.options.touchY + request.options.touchSize / 2,
                                    py * 2 - request.options.touchX -
                                    request.options.touchSize / 2));
        }

        float textsize = screenHeight / 50;

        for (int i = 0; i < response.PlayerCount; i++) {
            PlayerData player = response.Players[i];

            x = response.Players[i].HeadLocation.x;
            y = response.Players[i].HeadLocation.y;

            if (player.isBot) {
                botCount++;
                clrFilled = Color(255, 255, 255, 40);
                clrEnemy = Color(255, 255, 255, 255);
                clrEdge = Color(255, 255, 255, 150);
                clrSkeleton = Color(255, 255, 255, 200);
                clrBox = Color(255, 255, 255, 200);
            } else {
                playerCount++;
                if (response.Players[i].isVisible == true) {
                    clrEnemy = Color(0, 255, 0, 255);
                    clrSkeleton = Color(0, 255, 0, 200);
                    clrBox = Color(0, 255, 0, 200);
                    clrFilled = Color(0, 255, 0, 40);
                } else {
                    clrFilled = Color(255, 0, 0, 40);
                    clrEnemy = Color(255, 0, 0, 255);
                    clrEdge = Color(255, 0, 0, 150);
                    clrSkeleton = Color(255, 0, 0, 200);
                    clrBox = Color(255, 0, 0, 200);
                }
            }

            if (isNoBot && player.isBot) continue;

            float magic_number = (response.Players[i].Distance * response.fov);
            float mx = (screenWidth / 4) / magic_number;
            float my = (screenWidth / 1.38) / magic_number;
            float top = y - my + (screenWidth / 1.7) / magic_number;
            float bottom = response.Players[i].Bone.lAn.y + my - (screenWidth / 1.7) / magic_number;

            if (response.Players[i].HeadLocation.z != 1) {

                if (x > -50 && x < screenWidth + 50) {

                    if (isPlayerLinee) {
                        if (isPlayerLine == 0) {
                            esp.DrawLine(Color(255, 255, 255, 255), (mScaleY * 1.6f),
                                         Vec2(center.x, (mScaleY * 118)),
                                         Vec2(player.Bone.head.x, player.Bone.head.y));
                        } else if (isPlayerLine == 1) {
                            esp.DrawLine(Color(255, 255, 255, 255), (mScaleY * 1.6f), center,
                                         Vec2(player.Bone.head.x, player.Bone.head.y));
                        } else if (isPlayerLine == 2) {
                            esp.DrawLine(Color(255, 255, 255, 255), (mScaleY * 1.6f),
                                         Vec2(center.x, screenHeight),
                                         Vec2(player.Bone.head.x, player.Bone.head.y));
                        }
                    }
                }
            }
            if (response.Players[i].HeadLocation.z == 1.0f) {
                if (!isr360Alert)
                    continue;

                if (x > screenWidth - screenWidth / 12)
                    x = screenWidth - screenWidth / 120;
                else if (x < screenWidth / 120)
                    x = screenWidth / 12;

                if (y < screenHeight / 1) {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(screenWidth - x - 100, screenHeight - 48),
                                 Vec2(screenWidth - x + 100, screenHeight + 2));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(screenWidth - x - 100, screenHeight - 48),
                                       Vec2(screenWidth - x + 100, screenHeight + 2));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(screenWidth - x, screenHeight - 20),
                                 textsize);
                } else {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(screenWidth - x - 100, 48),
                                 Vec2(screenWidth - x + 100, -2));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(screenWidth - x - 100, 48),
                                       Vec2(screenWidth - x + 100, -2));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(screenWidth - x, 25), textsize);
                }
            } else if (x < -screenWidth / 10 || x > screenWidth + screenWidth / 10) {
                if (!isr360Alert)
                    continue;

                if (y > screenHeight - screenHeight / 12)
                    y = screenHeight - screenHeight / 120;
                else if (y < screenHeight / 120)
                    y = screenHeight / 12;

                if (x > screenWidth / 2) {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(screenWidth - 88, y - 35),
                                 Vec2(screenWidth + 2, y + 35));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(screenWidth - 88, y - 35),
                                       Vec2(screenWidth + 2, y + 35));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(screenWidth - screenWidth / 80, y + 10),
                                 textsize);
                } else {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(0 + 88, y - 35), Vec2(0 - 2, y + 35));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(0 + 88, y - 35), Vec2(0 - 2, y + 35));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(screenWidth / 80, y + 10), textsize);
                }
            } else if (y < -screenHeight / 10 || y > screenHeight + screenHeight / 10) {
                if (!isr360Alert)
                    continue;

                if (x > screenWidth - screenWidth / 12)
                    x = screenWidth - screenWidth / 120;
                else if (x < screenWidth / 120)
                    x = screenWidth / 12;

                if (y > screenHeight / 2.5) {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(x - 100, screenHeight - 48), Vec2(x + 100,
                                                                        screenHeight + 2));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(x - 100, screenHeight - 48),
                                       Vec2(x + 100, screenHeight + 2));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(x, screenHeight - 20), textsize);
                } else {
                    esp.DrawRect(Color(255, 255, 255), 2,
                                 Vec2(x - 100, 48), Vec2(x + 100, -2));
                    esp.DrawFilledRect(Color(255, 0, 0, 140),
                                       Vec2(x - 100, 48), Vec2(x + 100, -2));
                    sprintf(extra, "%0.0f m", response.Players[i].Distance);
                    esp.DrawText(Color(255, 255, 255, 255), extra,
                                 Vec2(x, 25), textsize);
                }
            }
        }
    }

    //if (botCount + playerCount > 0) {
    int ENEM_ICON = 2;
    int BOT_ICON = 3;

    if (playerCount == 0) {
        ENEM_ICON = 0;
    }
    if (botCount == 0) {
        BOT_ICON = 1;
    }

    char cn[10];
    sprintf(cn, "%d", playerCount);

    char bt[10];
    sprintf(bt, "%d", botCount);


    esp.DrawOTH(Vec2(screenWidth / 2 - (80), 60), ENEM_ICON);
    esp.DrawOTH(Vec2(screenWidth / 2, 60), BOT_ICON);

    esp.DrawText(Color(255, 255, 255, 255), cn, Vec2(screenWidth / 2 - (20), 87), 23);

    esp.DrawText(Color(255, 255, 255, 255), bt, Vec2(screenWidth / 2 + (50), 87), 23);
}
#endif // DESI_IMPORTANT_HACK_H
