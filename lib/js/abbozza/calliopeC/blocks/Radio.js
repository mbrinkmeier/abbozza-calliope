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
 * @fileoverview ... 
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */

Abbozza.RadioAvailable = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.RADIO"));
        this.appendDummyInput()
                .appendField(_("radio.available"));
        this.setInputsInline(false);
        this.setOutput(true, "BOOLEAN");
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setTooltip('');
    },
    generateCode: function (generator) {
       generator.radioRequired = true;
       var code = "( abbozza.radio.dataReady() > 0 )";       
       return code;
    }

};

Blockly.Blocks['radio_available'] = Abbozza.SerialAvailable;


/**
 * This block returns the last received message via radio interface.
 * 
 * @type type
 */
Abbozza.RadioReceive = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setInputsInline(true);
        this.setOutput(true,"STRING");
        this.appendDummyInput()
           .appendField(_("block.radioReceive"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        code = "abbozza.radio.datagram.recv()";
        return code;
    }
}

Blockly.Blocks['radio_receive'] = Abbozza.RadioReceive;


/**
 * This block returns the last received message via radio interface.
 * 
 * @type type
 */
Abbozza.RadioReceive = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setInputsInline(true);
        this.setOutput(true,"STRING");
        this.appendValueInput("BYTE")
           .appendField(__("block.radioReceiveByte",0))
           .setCheck("NUMBER");
        this.appendDummyInput()
           .appendField(__("block.radioReceiveByte",1));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var byte = generator.valueToCode(this,"BYTE")
        var code = "";
        code = "abbozza.radio.datagram.recv()[" + byte + "]";
        return code;
    }
}

Blockly.Blocks['radio_receive_byte'] = Abbozza.RadioReceive;


/**
 * This block sends a message using the radio interface.
 * @type type
 */
Abbozza.RadioSend = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true);
        this.setNextStatement(true);            
        this.appendValueInput("MSG")
            .appendField(_("block.radioSend"))
            .setCheck(["STRING","NUMBER","DECIMAL","BOOLEAN"]);
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        var msg = generator.valueToCode(this,"MSG","STRING");
        code = "abbozza.radio.datagram.send("+msg+");";
        return code;
    }    
}

Blockly.Blocks['radio_send'] = Abbozza.RadioSend;


/**
 * This block sets the transmission power.
 * @type type
 */
Abbozza.RadioPower = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true);
        this.setNextStatement(true);            
        this.appendValueInput("STR")
            .appendField(_("block.radioPower"))
            .setCheck("NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        var str = generator.valueToCode(this,"STR");
        code = "abbozza.radio.setTransmitPower("+str+");";
        return code;
    }    
}

Blockly.Blocks['radio_power'] = Abbozza.RadioPower;

/**
 * This block sets the group address of the device.
 * @type type
 */
Abbozza.RadioChannel = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true);
        this.setNextStatement(true);            
        this.appendValueInput("VAL")
            .appendField(_("block.radioChannel"))
            .setCheck("NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        var val = generator.valueToCode(this,"VAL");
        code = "abbozza.radio.setGroup("+val+");";
        return code;
    }    
}

Blockly.Blocks['radio_channel'] = Abbozza.RadioChannel;

