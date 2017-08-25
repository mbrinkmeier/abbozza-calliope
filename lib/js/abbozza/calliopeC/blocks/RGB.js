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
 * @fileoverview Blocks for the handling of LEDs
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */


Abbozza.DeviceRGBLedSetColor = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendDummyInput()
            .appendField(__("dev.SETRGBLED",0))
            .appendField(__("dev.SETRGBLED",1));
        this.appendValueInput("RED")
            .setAlign(Blockly.ALIGN_RIGHT)
            .appendField(_("dev.RED"))
            .setCheck("NUMBER");
        this.appendValueInput("GREEN")
            .setAlign(Blockly.ALIGN_RIGHT)
            .appendField(_("dev.GREEN"))
            .setCheck("NUMBER");
        this.appendValueInput("BLUE")
            .setAlign(Blockly.ALIGN_RIGHT)       
            .appendField(_("dev.BLUE"))
            .setCheck("NUMBER");
        /* this.appendValueInput("WHITE")
            .setAlign(Blockly.ALIGN_RIGHT)       
            .appendField(_("dev.WHITE"))
            .setCheck("NUMBER"); */
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    setName: function (name) {
        this.name = name;
    },
    generateCode: function (generator) {
        
        var red = generator.valueToCode(this, "RED");
        var green = generator.valueToCode(this, "GREEN");
        var blue = generator.valueToCode(this, "BLUE");
        // var white = generator.valueToCode(this, "WHITE");
        
        var code = "";
        code = "abbozza.rgb.setColour(" + red + "," + green + "," + blue + ",0);\n";
        
        return code;
    },

};

Abbozza.DeviceRGBLedSetColor2 = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVOUT"));
        this.appendDummyInput()
            .appendField(__("dev.SETRGBLED",0))
            .appendField(__("dev.SETRGBLED",1))
            .appendField(new Blockly.FieldColour("#000000"), "COLOR");        
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    setName: function (name) {
        this.name = name;
    },
    generateCode: function (generator) {
        var color = generator.fieldToCode(this,"COLOR");
        
        var result = /^#?([a-f\d]{2})([a-f\d]{2})([a-f\d]{2})$/i.exec(color);
        var red = parseInt(result[1],16);
        var green = parseInt(result[2],16);
        var blue = parseInt(result[3],16);

        var code = "";
        var code = "";
        code = "abbozza.rgb.setColour(" + red + "," + green + "," + blue + "," + white + ");\n";
        
        return code;
    },

};
Blockly.Blocks['dev_rgb_led_set_color'] = Abbozza.DeviceRGBLedSetColor;
Blockly.Blocks['dev_rgb_led_set_color2'] = Abbozza.DeviceRGBLedSetColor2;

