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


Abbozza.RadioParse = {
    symbols: null,
    stdCmdActive : true,
    debug: false,
    commands: [],
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.RADIO"));
        this.appendDummyInput()
                .appendField(_("dev.RADIO_PARSE_MSG"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.setTooltip('');
    }
};

Blockly.Blocks['radio_parse'] = Abbozza.RadioParse;


Abbozza.RadioNumber = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        thisBlock = this;
        this.appendDummyInput()
                .appendField(_("radio.NUMBER"));
        this.setOutput(true, "NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        generator.radioRequired = true;
        code = "__radio_parser.parse_" + keyword("NUMBER") + "()";
        return code;
    }
};


Abbozza.RadioDecimal = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        thisBlock = this;
        this.appendDummyInput()
                .appendField(_("radio.DECIMAL"));
        this.setOutput(true, "DECIMAL");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        generator.radioRequired = true;
        code = "__radio_parser.parse_" + keyword("DECIMAL") + "()";
        return code;
    }
};


Abbozza.RadioString = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        thisBlock = this;
        this.appendDummyInput()
                .appendField(_("radio.STRING"));
        this.setOutput(true, "STRING");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        generator.radioRequired = true;
        code = "__radio_parser.parse_string()";
        return code;
    }
};


Abbozza.RadioWord = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.RADIO"));
        thisBlock = this;
        this.appendDummyInput()
                .appendField(_("radio.WORD"));
        this.setOutput(true, "STRING");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        generator.parserRequired = true;
        code = "__radio_parser.parse_word()";
        return code;
    }
};

Blockly.Blocks['radio_number'] = Abbozza.RadioNumber;
Blockly.Blocks['radio_decimal'] = Abbozza.RadioDecimal;
Blockly.Blocks['radio_word'] = Abbozza.RadioWord;
Blockly.Blocks['radio_string'] = Abbozza.RadioString;
