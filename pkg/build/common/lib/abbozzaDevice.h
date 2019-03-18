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
#include "abbozzaPins.h"


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

extern const uint8_t __arrow_n[];
extern MicroBitImage Image_ARROW_N;
extern const uint8_t __arrow_ne[];
extern MicroBitImage Image_ARROW_NE;
extern const uint8_t __arrow_e[];
extern MicroBitImage Image_ARROW_E;
extern const uint8_t __arrow_se[];
extern MicroBitImage Image_ARROW_SE;
extern const uint8_t __arrow_s[];
extern MicroBitImage Image_ARROW_S;
extern const uint8_t __arrow_sw[];
extern MicroBitImage Image_ARROW_SW;
extern const uint8_t __arrow_w[];
extern MicroBitImage Image_ARROW_W;
extern const uint8_t __arrow_nw[];
extern MicroBitImage Image_ARROW_NW;
extern const uint8_t __arrow_chessboard[];
extern MicroBitImage Image_CHESSBOARD;
extern const uint8_t __diamond[];
extern MicroBitImage Image_DIAMOND;
extern const uint8_t __diamond_small[];
extern MicroBitImage Image_DIAMOND_SMALL;
extern const uint8_t __square[];
extern MicroBitImage Image_SQUARE;
extern const uint8_t __square_small[];
extern MicroBitImage Image_SQUARE_SMALL;


extern uint8_t __abz_image_data[31];

/*
 * Helper class
 */

class Abbozza : public MicroBit {    
    private:
        int currentRX = USBRX;
        int currentTX = USBTX;
        char i2cData[10];
        int i2cLen = 0;
        uint8_t i2cAddr = 0;
        bool __radioChecked = false;
        ManagedString __radioBuffer;
        uint8_t serialFlags = 0;
                
        
    public:
        uint8_t const SERIAL_NAN = 0x01;
        
        void init();
        
        ManagedString toString(int value);
        ManagedString toString(ManagedString value);
        ManagedString toString(bool value);
        ManagedString toString(float value);
        ManagedString toString(double value);

        ManagedString ftoa(float value);
        ManagedString dtoa(double value);
        
        int getPin(int pin);
        int getGesture();
        int getMicrophoneLevel();
        void registerEventHandler(int id, int value, void (*handler)(MicroBitEvent));
        int readLightLevel();
        int getRoll();
        int getPitch();

        void serialWriteLine(int tx, int rx, ManagedString line);
        void serialWriteLine(PinName tx, PinName rx, ManagedString line);
        ManagedString serialReadLine(int tx, int rx);
        ManagedString serialReadLine(PinName tx, PinName rx);
        ManagedString serialReadAll(int tx, int rx);
        ManagedString serialReadAll(PinName tx, PinName rx);
        void serialWriteByte(int tx, int rx, int byte);
        void serialWriteByte(PinName tx, PinName rx, int byte);
        int serialReadByte(int tx, int rx);
        int serialReadByte(PinName tx, PinName rx);
        void serialWriteInt(int tx, int rx, int value);
        void serialWriteInt(PinName tx, PinName rx, int value);
        int serialReadInt(int tx, int rx);
        int serialReadInt(PinName tx, PinName rx);
        bool serialIsAvailable(int tx, int rx);
        bool serialIsAvailable(PinName tx, PinName rx);
        void serialRedirect(int tx, int rx);
        void serialRedirect(PinName tx, PinName rx);
        void serialSetBaud(int tx, int rx, int baud);
        void serialSetBaud(PinName tx, PinName rx, int baud);
        int getSerialFlags();
        bool serialWasNAN();
        
        void i2cBeginTransmission(uint8_t addr);
        void i2cWriteByte(uint8_t value);
        void i2cWriteShort(int value);
        void i2cWriteInt(int value);
        void i2cWriteText(ManagedString text);
        void i2cEndTransmission();
        ManagedString i2cRequest(uint8_t addr, int len);                
        ManagedString i2cRequest(uint8_t addr, uint8_t reg, int len);                
        
        bool radioAvailable();
        ManagedString radioRecv();
        
        #ifdef TARGET_NRF51_CALLIOPE    
        int getTemperature();
        float getTemperatureFloat();
        #endif
        
};


// Missing Operators for ManagedStrings
// bool operator!=(ManagedString& a, const ManagedString& b);
// bool operator<=(ManagedString& a, const ManagedString& b);
// bool operator>=(ManagedString& a, const ManagedString& b);

#endif

