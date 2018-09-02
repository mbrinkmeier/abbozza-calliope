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


#include "MicroBit.h"
#include "abbozzaDevice.h"
#include <stdlib.h>


/**
 * Built in Images
 */
const uint8_t __heart[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,255,0,255,0, 255,0,255,0,255, 255,0,0,0,255, 0,255,0,255,0, 0,0,255,0,0 };
MicroBitImage Image_HEART((ImageData*) __heart);

const uint8_t __small_heart[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,255,0,255,0, 0,255,255,255,0, 0,0,255,0,0, 0,0,0,0,0 };
MicroBitImage Image_HEART_SMALL((ImageData*) __small_heart);

const uint8_t __happy[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 255,0,0,0,255, 0,255,255,255,0 };
MicroBitImage Image_HAPPY((ImageData*) __happy);

const uint8_t __smiley[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0,  0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 0,255,255,255,0, 0,0,0,0,0 };
MicroBitImage Image_SMILEY((ImageData*) __smiley);

const uint8_t __sad[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 0,255,255,255,0, 255,0,0,0,255 };
MicroBitImage Image_SAD((ImageData*) __sad);

const uint8_t __surprised[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 0,0,255,0,0, 0,0,0,0,0 };
MicroBitImage Image_SURPRISED((ImageData*) __surprised);

const uint8_t __beard[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 0,255,255,255,0, 0,255,0,255,0 };
MicroBitImage Image_BEARD((ImageData*) __beard);

const uint8_t __moustache[] __attribute__ ((aligned (4))) = { 0xff, 0xff, 5, 0, 5,0, 0,0,0,0,0, 0,255,0,255,0, 0,0,0,0,0, 255,255,0,255,255, 0,0,255,0,0 };
MicroBitImage Image_MOUSTACHE((ImageData*) __moustache);

const uint8_t __yes[] __attribute__ ((aligned (4))) = { 0xff, 0xff, 5, 0, 5,0, 0,0,0,0,0, 0,0,0,0,255, 0,0,0,255,0, 255,0,255,0,0, 0,255,0,0,0 };
MicroBitImage Image_YES((ImageData*) __yes);

const uint8_t __no[] __attribute__ ((aligned (4))) = { 0xff, 0xff, 5, 0, 5,0, 255,0,0,0,255, 0,255,0,255,0, 0,0,255,0,0, 0,255,0,255,0, 255,0,0,0,255 };
MicroBitImage Image_NO((ImageData*) __no);

uint8_t __abz_image_data[31] = { 0xff,0xff,5,0,5,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0, 0,0,0,0,0};

/**
 * Initialize the abbozza device.
 */
void Abbozza::init() {
    MicroBit::init();
    serial.setRxBufferSize(254);
}



ManagedString Abbozza::toString(int value) {
    return ManagedString(value);
}

ManagedString Abbozza::toString(ManagedString value) {
    return value;
}

ManagedString Abbozza::toString(bool value) {
    return ManagedString(value);
}

ManagedString Abbozza::toString(float value) {
    return ftoa(value);
}

ManagedString Abbozza::toString(double value) {
    return dtoa(value);
}

/**
 * Create a ManagedString from a float value
 * 
 * @param value The value to be representred as string.
 * @return The string representation of value
 */
ManagedString Abbozza::ftoa(float value) {
    return dtoa(value);
}


/**
 * Create a ManagedString from a float value
 * 
 * @param value The value to be representred as string.
 * @return The string representation of value
 */
ManagedString Abbozza::dtoa(double value) {
    // Check for NaN
    if ( isnan(value) ) {
        return ManagedString("NaN");
    }
    
    ManagedString res = ManagedString("");
    
    // Check for sign
    if ( value < 0 ) {
        value = -1.0 * value;
        res = res + '-';
    }

    // Now convert positive value
    int pre = round(floor(value));
    res = res + ManagedString(pre);
    res = res + ".";
    
    ManagedString buf = ManagedString("");
    
    double rem = value - pre;
    int pos = 5;
    while ((pos > 0) && (rem > 0.0) ) {
        rem = rem * 10.0;
        double f = floor(rem);
        int d = round(f);
        char digit = '0' + d;
        if ( digit == '0' ) {
            buf = buf + '0';
        } else {
            res = res + buf;
            res = res + digit;
            buf = ManagedString("");
        }
        rem = rem - f;
        pos--;
    }
        
    return res;        
}



/*
 * Get the pin number.
 */
int Abbozza::getPin(int pin) {
    switch(pin) {
        case 0: return P0;
        case 1: return P1;   
        case 2: return P2;
        case 3: return P3;
        case 4: return P4;
        case 5: return P5;
        case 6: return P6;
        case 7: return P7;
        case 8: return P8;
        case 9: return P9;
        case 10: return P10;
        case 11: return P11;
        case 12: return P12;
        case 16: return P16;
        case 17: return P17;
        case 18: return P18;
        case 19: return P19;
        case 21: return P21;
        default: return P0;
    }
}


/**
 * Read the last gesture and update the stored sample.
 * 
 * @return The id of the last gesture
 */
int Abbozza::getGesture() {
    int __gesture__ = accelerometer.getGesture();
    accelerometer.updateSample();
    return __gesture__;    
}

/**
 * Get the current microphone level.
 * @return The microphone level.
 */
int Abbozza::getMicrophoneLevel() {
    int value = io.pin[MICROPHONE_PIN].getAnalogValue();
    return value;
}

/**
 * Register an event handler. If another handler was registered for the same
 * event type it is removed. This allows for dynamic changing of event handlers.
 * 
 * @param id The id of the event
 * @param value The velue of the event
 * @param handler The handler
 */
void Abbozza::registerEventHandler(int id, int value, void (*handler)(MicroBitEvent)) {
    // Check existing handler
    int i = 0;
    MicroBitListener *listener;
    
    while ( (listener = messageBus.elementAt(i)) != NULL ) {
        if ((listener-> id == id) && (listener->value == value)) {
            messageBus.remove(listener);
        }
        i++;
    }
    messageBus.listen(id,value,handler);
}

/**
 * Read the light level measured by the led-array. The mode is changed
 * before and set to the original value afterwards.
 * 
 * @return The light level read by the led array
 */
int Abbozza::readLightLevel() {
    int value, mode;
    
    mode = display.getDisplayMode();
    display.setDisplayMode(DISPLAY_MODE_BLACK_AND_WHITE_LIGHT_SENSE);
    sleep(50);
    value = display.readLightLevel();
    display.setDisplayMode((DisplayMode) mode);
    
    return value;
}

/**
 * Write a string to the serial line, followed by a newline.
 * 
 * @param tx
 * @param rx
 * @param line
 */
void Abbozza::serialWriteLine(int tx, int rx, ManagedString line) {
    serialRedirect(tx,rx);
    serial.send(line);
    serial.send("\n");
}

void Abbozza::serialWriteLine(PinName tx, PinName rx, ManagedString line) {
    serialRedirect(tx,rx);
    serial.send(line);
    serial.send("\n");
}

/**
 * Read a string from the rx buffer until the next newline
 * 
 * @param tx
 * @param rx
 * @return 
 */
ManagedString Abbozza::serialReadLine(int tx, int rx) {
serialRedirect(tx,rx);
    return serial.readUntil(ManagedString("\n"));
}


ManagedString Abbozza::serialReadLine(PinName tx, PinName rx) {
serialRedirect(tx,rx);
    return serial.readUntil(ManagedString("\n"));
}


/**
 * Read the whole rx buffer
 * 
 * @param tx
 * @param rx
 * @return 
 */
ManagedString Abbozza::serialReadAll(int tx, int rx) {
    serialRedirect(tx,rx);
    ManagedString text("");
    while ( serial.isReadable() ) {
        text = text + ManagedString(((char) serial.read()));
    }
    return text;
}


ManagedString Abbozza::serialReadAll(PinName tx, PinName rx) {
    serialRedirect(tx,rx);
    ManagedString text("");
    while ( serial.isReadable() ) {
        text = text + ManagedString(((char) serial.read()));
    }
    return text;
}


/**
 * Write a byte to the serial line
 * 
 * @param tx
 * @param rx
 * @param byte
 */
void Abbozza::serialWriteByte(int tx, int rx, int byte){
    serialRedirect(tx,rx);
    serial.sendChar((char) (byte % 256),ASYNC);
}

void Abbozza::serialWriteByte(PinName tx, PinName rx, int byte){
    serialRedirect(tx,rx);
    serial.sendChar((char) (byte % 256),ASYNC);
}

/**
 * Read a byte from the serial line
 * 
 * @param tx
 * @param rx
 * @return 
 */
int Abbozza::serialReadByte(int tx, int rx) {
    serialRedirect(tx,rx);
    return serial.read(ASYNC);
}

int Abbozza::serialReadByte(PinName tx, PinName rx) {
    serialRedirect(tx,rx);
    return serial.read(ASYNC);
}


/**
 * Write a 4-byte int the serial line
 * 
 * @param tx
 * @param rx
 * @param byte
 */
void Abbozza::serialWriteInt(int tx, int rx, int value){
    uint8_t buf[4];
    buf[0] = (value >> 24) % 256;
    buf[1] = (value >> 16) % 256;
    buf[2] = (value >> 8) % 256;
    buf[3] = value % 256;
    
    serialRedirect(tx,rx);
    serial.send(buf,4,ASYNC);
}

void Abbozza::serialWriteInt(PinName tx, PinName rx, int value){
    uint8_t buf[4];
    buf[0] = (value >> 24) % 256;
    buf[1] = (value >> 16) % 256;
    buf[2] = (value >> 8) % 256;
    buf[3] = value % 256;

    serialRedirect(tx,rx);
    serial.send(buf,4,ASYNC);
}

/**
 * Read a 4-byte int from the serial line
 * 
 * @param tx
 * @param rx
 * @return 
 */
int Abbozza::serialReadInt(int tx, int rx) {
    uint8_t buf[4];
    serialRedirect(tx,rx);
    serial.read(buf,4,ASYNC);
    return ( buf[0] << 24 ) | ( buf[1] << 16 ) | ( buf[2] << 8 ) | buf[3];
}

int Abbozza::serialReadInt(PinName tx, PinName rx) {
    uint8_t buf[4];
    serialRedirect(tx,rx);
    serial.read(buf,4,ASYNC);
    return ( buf[0] << 24 ) | ( buf[1] << 16 ) | ( buf[2] << 8 ) | buf[3];
}



/**
 * Check if the rx buffer contains bytes
 * 
 * @param tx
 * @param rx
 * @return 
 */
bool Abbozza::serialIsAvailable(int tx, int rx) {
    serialRedirect(tx,rx);
    return ( serial.isReadable() != 0);
}


/**
 * Check if the rx buffer contains bytes
 * 
 * @param tx
 * @param rx
 * @return 
 */
bool Abbozza::serialIsAvailable(PinName tx, PinName rx) {
    serialRedirect(tx,rx);
    return ( serial.isReadable() != 0);
}


/**
 * Redirects the serial communication to the given pins
 */
void Abbozza::serialRedirect(int tx, int rx) {
    if ( (currentRX != rx) || (currentTX != tx) ) {
        serial.clearTxBuffer();
        serial.redirect( io.pin[tx].name, io.pin[rx].name);
        currentTX = tx;
        currentRX = rx;
    }
}


/**
 * Redirects the serial communication to the given pins
 */
void Abbozza::serialRedirect(PinName tx, PinName rx) {
    if ( (currentRX != rx) || (currentTX != tx) ) {
        serial.clearTxBuffer();
        serial.redirect(tx,rx);
        currentTX = tx;
        currentRX = rx;
    }
}


/**
 * Sets the baud rate
 */
void Abbozza::serialSetBaud(int tx, int rx, int baud) {
    serialRedirect(tx,rx);
    serial.baud(baud);
}


/**
 * Sets the baud rate
 */
void Abbozza::serialSetBaud(PinName tx, PinName rx, int baud) {
    serialRedirect(tx,rx);
    serial.baud(baud);
}


void Abbozza::i2cBeginTransmission(uint8_t addr) {
    i2cAddr = addr;
    i2cLen = 0;
}

void Abbozza::i2cWriteByte(uint8_t value) {
    i2cData[i2cLen] = value;
    i2cLen++;
}

void Abbozza::i2cWriteShort(int value) {
    if (i2cLen < 99 ) {
      uint8_t lo = value % 256;
      uint8_t hi = (value / 256) % 256;
      i2cData[i2cLen] = hi;
      i2cData[i2cLen+1] = lo;
      i2cLen += 2;    
    }
}


void Abbozza::i2cWriteInt(int value) {
    if (i2cLen < 97 ) {
      i2cData[i2cLen+3] = value % 256;
      value = value / 256;
      i2cData[i2cLen+2] = value % 256;
      value = value / 256;
      i2cData[i2cLen+1] = value % 256;
      value = value / 256;
      i2cData[i2cLen] = value % 256;
      i2cLen += 4;    
    }    
}


void Abbozza::i2cWriteText(ManagedString text) {
    if ( i2cLen < 100 - text.length() ) {
        for (int i = 0; i < text.length(); i++ ) {
            i2cData[i2cLen] = text.charAt(i);
            i2cLen++;
        }
    }
}

void Abbozza::i2cEndTransmission() {
    if ( i2cLen > 0 ) {
        i2c.write((i2cAddr*2) ,i2cData,i2cLen);
    }
}


ManagedString Abbozza::i2cRequest(uint8_t addr, int len) {
    i2c.read((addr*2)+1,i2cData,len);
    ManagedString result(i2cData,len);
    return result;
}

ManagedString Abbozza::i2cRequest(uint8_t addr, uint8_t reg, int len) {
    i2cData[0] = reg;
    i2c.write((addr*2),i2cData,1);
    i2c.read((addr*2)+1,i2cData,len);
    ManagedString result(i2cData,len);
    return result;
}


bool Abbozza::radioAvailable() {
    PacketBuffer buf = radio.datagram.recv();
    __radioChecked = true;
    if ( buf == PacketBuffer::EmptyPacket ) {
       __radioBuffer = ManagedString("");
       return false;
    } else {
       __radioBuffer = ManagedString(buf);
       return true;
    }
    
}


ManagedString Abbozza::radioRecv() {
    if ( __radioChecked ) {
        return __radioBuffer;
    } else {
        return ManagedString(radio.datagram.recv());
    }
}


int Abbozza::getTemperature() {
    int res;
    char cmd = BMX055_ACC_D_TEMP;
    char val = 255;
    res = i2c.write(BMX055_ACC_ADDRESS*2, &cmd, 1);
    if ( res != 0 ) return 255;
    
    res = i2c.read(BMX055_ACC_ADDRESS*2+1, &val, 1);
    // The actual tempreture is 23 + (val/2), with val in two's complement
    if ( val < 128 ) {
      val = 23 + (val/2);
    } else {
      val = 23 - ((256 - val)/2);  // ????  
    }
    if ( res != 0 ) return 255;
    
    return val;
}

float Abbozza::getTemperatureFloat() {
    int res;
    char cmd = BMX055_ACC_D_TEMP;
    char val = 255;
    float temp;
    res = i2c.write(BMX055_ACC_ADDRESS*2, &cmd, 1);
    if ( res != 0 ) return 255;
    
    res = i2c.read(BMX055_ACC_ADDRESS*2+1, &val, 1);
    // The actual tempreture is 23 + (val/2), with val in two's complement
    if ( val < 128 ) {
      temp = (46.0 + val)/2.0;
    } else {
      temp = (46.0 - (256.0 - val))/2.0;  // ????  
    }
    if ( res != 0 ) return 255.0;
    
    return temp;
}

// Missing operators for Managed Strings
// bool operator!=(ManagedString& a, const ManagedString& b) {
//   return !( a == b );
// }

// bool operator<=(ManagedString& a, const ManagedString& b) {
//   return !( a > b );
// }

// bool operator>=(ManagedString& a, const ManagedString& b) {
//   return !( a < b );
// }
