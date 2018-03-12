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
                .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
                .appendField(_("radio.available"));
        this.setInputsInline(false);
        this.setOutput(true, "BOOLEAN");
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setTooltip('');
    },
    generateCode: function (generator) {
       generator.radioRequired = true;
       var code = "abbozza.radioAvailable()";       
       return code;
    }

};

Blockly.Blocks['radio_available'] = Abbozza.RadioAvailable;


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
           .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
           .appendField(_("block.radioReceive"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        // code = "ManagedString(abbozza.radio.datagram.recv())";
        code = "abbozza.radioRecv())";
        return code;
    }
}

Blockly.Blocks['radio_receive'] = Abbozza.RadioReceive;

/**
 * This block returns the last received message via radio interface.
 * 
 * @type type
 */
Abbozza.RadioReceiveStore = {
    init : function() {
        var thisBlock = this;
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.setInputsInline(true);
        this.setOutput(false);
        this.appendDummyInput()
           .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
           .appendField(_("block.radioReceiveStore"))
           .appendField(new VariableTypedDropdown(thisBlock, "STRING", null), "VAR")   
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var name = generator.fieldToCode(this,"VAR");       
        var code = "";
        code = name + " = ManagedString(abbozza.radio.datagram.recv());";
        return code;
    }
}

Blockly.Blocks['radio_receive_store'] = Abbozza.RadioReceiveStore;

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
           .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
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
 * This block returns the last received message via radio interface.
 * 
 * @type type
 */
Abbozza.RadioReceiveByte = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setInputsInline(true);
        this.setOutput(true,"NUMBER");
        this.appendValueInput("BYTE")
           .setCheck("NUMBER")
           .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
           .appendField(__("block.radioReceiveByte",0));
        this.appendDummyInput()
           .appendField(__("block.radioReceiveByte",1));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var byte = generator.valueToCode(this,"BYTE");
        generator.radioRequired = true;
        var code = "";
        code = "abbozza.radio.datagram.recv()[" + byte + "]";
        return code;
    }
}

Blockly.Blocks['radio_receive_byte'] = Abbozza.RadioReceiveByte;

/**
 * This block sends a message using the radio interface.
 * @type type
 */
Abbozza.RadioSend = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("MSG")
            .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
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
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("STR")
            .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
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
 * This block sets the frequency band of the device.
 * @type type
 */
Abbozza.RadioChannel = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("VAL")
            .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
            .appendField(_("block.radioChannel"))
            .setCheck("NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        generator.radioRequired = true;
        var code = "";
        var val = generator.valueToCode(this,"VAL");
        code = "abbozza.radio.setFrequencyBand("+val+");";
        return code;
    }    
}

Blockly.Blocks['radio_channel'] = Abbozza.RadioChannel;

/**
 * This block sets the group of the device.
 * @type type
 */
Abbozza.RadioGroup = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("VAL")
            .appendField(new Blockly.FieldImage("img/devices/radio.png",16,16))     
            .appendField(_("block.radioGroup"))
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

Blockly.Blocks['radio_group'] = Abbozza.RadioGroup;

