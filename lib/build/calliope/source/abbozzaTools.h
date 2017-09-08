/*
 * This header file contains some utility function used by abbozza!
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
    public:
        int getGesture();
        int getMicrophoneLevel();
};

#endif

