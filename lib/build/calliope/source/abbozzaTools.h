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

/*
 * Images
 */
extern const uint8_t __heart[];
extern const uint8_t __small_heart[];
extern const uint8_t __happy[];
extern const uint8_t __smiley[];
extern const uint8_t __sad[];
extern const uint8_t __surprised[];
extern const uint8_t __beard[];
extern const uint8_t __moustache[];
extern const uint8_t __yes[];
extern const uint8_t __no[];
extern uint8_t __abz_image_data[31];

/*
 * Helper class
 */

class Abbozza : public MicroBit {    
    public:
        int getGesture();
};

#endif

