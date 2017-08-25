/*
 * This header file contains definitions and declarations used by abbozza!
 */
 
#ifndef _ABBOZZA_H
#define _ABBOZZA_H

#include "MicroBit.h"

MicroBit abbozza;
        
/**
 * Define pin macros
 */

#define TOUCH0  12
#define TOUCH1  0
#define TOUCH2  1
#define TOUCH3  16

#define C0      12
#define C1      0
#define C2      1
#define C3      16
#define C4      3
#define C5      4
#define C6      10
#define C7      13
#define C8      14
#define C9      15
#define C10     9
#define C11     7
#define C12     6
#define C16     2
#define C17     8
#define C21     21


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

#endif
