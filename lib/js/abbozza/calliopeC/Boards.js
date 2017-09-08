/**
 * @license
 * abbozza!
 *
 * File: Boards.js
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
 * 
 * SYSTEM SPECIFIC
 */

/**
 * @fileoverview A singleton object managing the parameters of the current board.
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */

Board = {
    boardsXML: null,
    description: "Calliope Mini",
    boardId: "calliope",
    analogIn: 0,
    analogOut: 0,
    digitalPins: 21,
    pwm: [],
    internalLED: 13,
    interrupts: [3, 2],
    serialRX: [0],
    serialTX: [1]
};


Board.init = function (systemPrefix) {
    this.load(false);
    this.pwm = [];
};


Board.load = function (query) {
    if ( query == false ) {
        Connection.getText("/abbozza/board",
            function (response) {
                // var val = response.split("|");
                // Abbozza.showInfoMessage(_("msg.board_found",[val[1],[2]]));
                // Board._apply(response.split("|")[0]);
                document.getElementById("connect").style.backgroundColor = "#00d000";
            },
            function (response) {
                Abbozza.showInfoMessage(_("msg.no_board"));
                // Board._apply(response.split("|")[0]);                    
                document.getElementById("connect").style.backgroundColor = "#d00000";
            }
        );
    } else {
        Connection.getText("/abbozza/queryboard",
            function (response) {
                var val = response.split("|");
                Abbozza.showInfoMessage(_("msg.board_found",[val[1],[2]]));
                // Board._apply(response.split("|")[0]);
                document.getElementById("connect").style.backgroundColor = "#00d000";
            },
            function (response) {
                Abbozza.showInfoMessage(_("msg.no_board"));
                // Board._apply(response.split("|")[0]);
                document.getElementById("connect").style.backgroundColor = "#d00000";
            }
        );
        
    }    
}

Board._isPWM = function(pin) {
    return false;
}

Board.getTouchPinMenu = function() {
    var pins = [
        ["P0","TOUCH0"],
        ["P1","TOUCH1"],
        ["P2","TOUCH2"],
        ["P3","TOUCH3"]
    ];
    return pins;
}


Board.getDigitalPinMenu = function() {
    var pins = [
        ["P0","P0"],
        ["P1","P1"],
        ["P2","P2"],
        ["P3","P3"],
        ["P4","P4"],
        ["P5 / A","P5"],
        ["P6","P6"],
        ["P7","P7"],
        ["P8","P8"],
        ["P9","P9"],
        ["P10","P10"],
        ["P11 / B","P11"],
        ["P12","P12"],
        ["P16 / RX","P16"],
        ["P17 / TX","P17"],
        ["P18 / SDA" , "P18"],
        ["P19 / SCL" , "P19"],
        ["P21 / Mic","P21"]
    ]
    return pins;
}


Board.getAnalogPinMenu = function() {
    var pins = [
        ["Pin 0","0"],
        ["Pin 1","1"],
        ["Pin 2","2"],
        ["Pin 3","3"],
        ["Pin 4","4"],
        ["Pin 10","10"]
    ]
    return pins;
}

Board.getPinMenu = function(type) {
    if ( type == PinDropdown.TOUCH ) {
        return this.getTouchPinMenu();
    }
    return this.getDigitalPinMenu();
}