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

/**
 * Images
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
        if ((listener-> id == id) && (listener->value==value)) {
            messageBus.remove(listener);
        }
        i++;
    }
    messageBus.listen(id,value,handler);
}

/**
 * Read teh light level measured by the led-array. The mode is changed
 * before and set to the original value afterwards.
 * 
 * @return The light level read by the led array
 */
int Abbozza::readLightLevel() {
    int value, mode;
    
    mode = display.getDisplayMode();
    display.setDisplayMode(DISPLAY_MODE_BLACK_AND_WHITE_LIGHT_SENSE);
    value = display.readLightLevel();
    display.setDisplayMode((DisplayMode) mode);
    
    return value;
}


void Abbozza::serialWriteLine(int tx, int rx, ManagedString line) {
    serialRedirect(tx,rx);
    serial.send(line);
    serial.send("\n");
}

ManagedString Abbozza::serialReadLine(int tx, int rx) {
    serialRedirect(tx,rx);
    return serial.readUntil("\r\n");
}


ManagedString Abbozza::serialReadAll(int tx, int rx) {
    serialRedirect(tx,rx);
    ManagedString text("");
    while ( serial.isReadable() ) {
        text = text + ManagedString(((char) serial.read()));
    }
    return text;
}


void Abbozza::serialWriteByte(int tx, int rx, int byte){
    serialRedirect(tx,rx);
    serial.sendChar((char) (byte % 256));
}

int Abbozza::serialReadByte(int tx, int rx) {
    serialRedirect(tx,rx);
    return serial.read();
}


bool Abbozza::serialIsAvailable(int tx, int rx) {
    serialRedirect(tx,rx);
    return serial.isReadable();
}


/**
 * Redirects the serial communication to the given pins
 */
void Abbozza::serialRedirect(int tx, int rx) {
    if ( (currentRX != rx) || (currentTX != tx) ) {
        serial.redirect((PinName) tx, (PinName) rx);
        currentTX = tx;
        currentRX = rx;
    }
}