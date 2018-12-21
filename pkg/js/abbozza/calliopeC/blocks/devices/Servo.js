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
 * @fileoverview Blocks for the handling of servo motors
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */

Abbozza.DeviceServo = {
    devtype: DEV_TYPE_SERVO,
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVICES"));
        // this.appendValueInput("PIN")
        // this.appendDummyInput()
        //         .appendField(_("dev.DEVICE"));
        this.appendDummyInput()
                .appendField(new Blockly.FieldImage("img/devices/servo.png",16,16))        
                .appendField(_("dev.SERVO"))
                .appendField(new FieldDevNameInput("<default>", Abbozza.blockDevices, this), "NAME")
                .appendField(_("dev.AT"))
                .appendField(new PinDropdown(PinDropdown.PWM), "PIN");
        // .appendField(new Blockly.FieldDropdown( function() { return Abbozza.board.getPWMPinMenu(); }  ), "PIN");
        // .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setOutput(false);
        this.setPreviousStatement(true, "DEVICE");
        this.setNextStatement(true, "DEVICE");
        this.setTooltip('');
        Abbozza.addDisposeHandler(this);
    },
    
    getName: function () {
        return this.getFieldValue("NAME");
    },
    
    generateCode: function (generator) {
        return "";
    },
    onDispose: function () {
        Abbozza.devices.delDevice(this.getName());
    }
};

Blockly.Blocks['dev_servo'] = Abbozza.DeviceServo;



Abbozza.DeviceServoSet = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.MOTOR"));
        this.appendValueInput("VALUE")
                .appendField(new Blockly.FieldImage("img/devices/servo.png",16,16))        
                .appendField(_("dev.SET"))
                .appendField(_("dev.SERVO"))
                .appendField(new DeviceDropdown(this, DEV_TYPE_SERVO), "NAME")
                .appendField(_("dev.TO"))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setOutput(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    /*  setName : function(name) {
     this.name = name;
     },*/


    generateCode: function (generator) {
        var device = Abbozza.blockDevices.getDevice(generator.fieldToCode(this, "NAME"));
        var value = generator.valueToCode(this, "VALUE");

        if (device == null) {
            ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
            return;
        }

        var pin = generator.fieldToCode(device,"PIN");
        var code = "abbozza.io.pin[" + pin + "].setServoValue(" + value;
        code = code + ");";
        return code;
    }

};

Blockly.Blocks['dev_servo_set'] = Abbozza.DeviceServoSet;
