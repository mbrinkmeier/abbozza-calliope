/**
 * @license
 * abbozza!
 *
 * Copyright 2015 Michael Brinkmeier ( michael.brinkmeier@uni-osnabrueck.de )
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

/**
 * The class Abbozza is a wrapper for the MicroBit class.
 * 
 * It provides a series of operations and constants used by abbozza!.
 * Its main purpose is keeping some things simple in the generated code.
 */
 
#ifndef _ABBOZZA_TOOLS_H
#define _ABBOZZA_TOOLS_H

#include "MicroBit.h"

/*
 * Makros for the correctly numbered pins
 */
#define TOUCH0  12
#define TOUCH1  0
#define TOUCH2  1
#define TOUCH3  16

#define P0      12
#define P1      0
#define P2      1
#define P3      16
#define P4      3
#define P5      4
#define P6      10
#define P7      13
#define P8      14
#define P9      15
#define P10     9
#define P11     7
#define P12     6
#define P16     2
#define P17     8
#define P18     17
#define P19     18
#define P21     19

#define MICROPHONE_PIN  19


/*
 * Images
 */
extern const uint8_t __heart[];
extern MicroBitImage Image_HEART;
extern const uint8_t __small_heart[];
extern MicroBitImage Image_HEART_SMALL;
extern const uint8_t __happy[];
extern MicroBitImage Image_HAPPY;
extern const uint8_t __smiley[];
extern MicroBitImage Image_SMILEY;
extern const uint8_t __sad[];
extern MicroBitImage Image_SAD;
extern const uint8_t __surprised[];
extern MicroBitImage Image_SURPRISED;
extern const uint8_t __beard[];
extern MicroBitImage Image_BEARD;
extern const uint8_t __moustache[];
extern MicroBitImage Image_MOUSTACHE;
extern const uint8_t __yes[];
extern MicroBitImage Image_YES;
extern const uint8_t __no[];
extern MicroBitImage Image_NO;
extern uint8_t __abz_image_data[31];

/*
 * Helper class
 */

class Abbozza : public MicroBit {    
    private:
        int currentRX = USBRX;
        int currentTX = USBTX;
        
    public:
        int getPin(int pin);
        int getGesture();
        int getMicrophoneLevel();
        void registerEventHandler(int id, int value, void (*handler)(MicroBitEvent));
        int readLightLevel();

        void serialWriteLine(int tx, int rx, ManagedString line);
        ManagedString serialReadLine(int tx, int rx);
        ManagedString serialReadAll(int tx, int rx);
        void serialWriteByte(int tx, int rx, int byte);
        int serialReadByte(int tx, int rx);
        bool serialIsAvailable(int tx, int rx);
        void serialRedirect(int tx, int rx);
        
};

#endif

