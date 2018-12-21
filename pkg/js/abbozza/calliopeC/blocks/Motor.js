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

Abbozza.Motors = [
    ["A","A"],
    ["B","B"]
]


Abbozza.DeviceMotorOn = {
    init: function () {
       this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendValueInput("PERCENT")
                .appendField(new Blockly.FieldImage("img/devices/motor.png",16,16))        
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.MOTOR_ON",0))
                .appendField(new Blockly.FieldDropdown(Abbozza.Motors),"MOTOR")
                .appendField(__("dev.MOTOR_ON",1))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var motor = generator.fieldToCode(this,"MOTOR");
        var percent = generator.valueToCode(this,"PERCENT");
        return "abbozza.soundmotor.motor" + motor + "On(" + percent + ");";
    }
};


Abbozza.DeviceMotorOff = {
    init: function () {
       this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendDummyInput()
                .appendField(new Blockly.FieldImage("img/devices/motor.png",16,16))        
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.MOTOR_OFF",0))
                .appendField(new Blockly.FieldDropdown(Abbozza.Motors),"MOTOR");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var motor = generator.fieldToCode(this,"MOTOR");
        return "abbozza.soundmotor.motor" + motor + "Off();";
    }
};


Blockly.Blocks['dev_motor_on'] = Abbozza.DeviceMotorOn;
Blockly.Blocks['dev_motor_off'] = Abbozza.DeviceMotorOff;



/**
 * Blocks for a bidirectional motor connected to Motor A and Motor B
 */

/**
 * Setting the bidirectional motor to the given percentage
 * 
 * @type type
 */
Abbozza.DeviceBidiMotorOn = {
    init: function () {
       this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendValueInput("PERCENT")
                .appendField(new Blockly.FieldImage("img/devices/motor.png",16,16))        
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(_("dev.BIDI_MOTOR_ON"))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var percent = generator.valueToCode(this,"PERCENT");
        return "abbozza.soundmotor.motorOn(" + percent + ");";
    }
};



Abbozza.DeviceBidiMotorBreak = {
    init: function () {
       this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendDummyInput()
                .appendField(new Blockly.FieldImage("img/devices/motor.png",16,16))        
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(_("dev.BIDI_MOTOR_BRAKE"));
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        return "abbozza.soundmotor.motorBreak();";
    }
};


Abbozza.DeviceBidiMotorOff = {
    init: function () {
       this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendDummyInput()
                .appendField(new Blockly.FieldImage("img/devices/motor.png",16,16))        
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(_("dev.BIDI_MOTOR_OFF"));
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        return "abbozza.soundmotor.motorSleep();";
    }
};


Blockly.Blocks['dev_bidi_motor_on'] = Abbozza.DeviceBidiMotorOn;
Blockly.Blocks['dev_bidi_motor_break'] = Abbozza.DeviceBidiMotorBreak;
Blockly.Blocks['dev_bidi_motor_off'] = Abbozza.DeviceBidiMotorOff;