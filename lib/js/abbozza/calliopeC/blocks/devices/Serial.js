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

'use strict';

goog.provide('SerialDropdown');


goog.require('Blockly.Field');
goog.require('Blockly.FieldDropdown');
goog.require('Blockly.Msg');
goog.require('goog.asserts');
goog.require('goog.dom');
goog.require('goog.userAgent');


Abbozza.baudMenu = [
   ["1200","1200"],
   ["2400","2400"],
   ["4800","4800"],
   ["9600","9600"],
   ["19200","19200"],
   ["38400","38400"],
   ["57600","57600"],
   ["76800","76800"],
   ["115200","115200"],
   ["230400","230400"],
   ["250000","250000"],
   ["460800","460800"],
   ["921600","921600"],
   ["1000000","1000000"]
];

/**
 * The serial connection device block.
 */
Abbozza.DeviceSerial = {
    devtype: DEV_TYPE_SERIAL,
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.DEVICES"));
        this.appendDummyInput()
                .appendField(new Blockly.FieldImage("img/devices/comm32.png",16,16))
                .appendField(__("dev.SERIAL",0))
                .appendField(new FieldDevNameInput("<default>", Abbozza.blockDevices, this), "NAME");
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SERIAL",1))
                .appendField(new PinDropdown(PinDropdown.DIGITAL), "RX");
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SERIAL",2))
                .appendField(new PinDropdown(PinDropdown.DIGITAL), "TX");
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

Blockly.Blocks['dev_serial'] = Abbozza.DeviceSerial;

/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialPrintLn = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.SERIAL"));
    this.appendValueInput("VALUE")
        .appendField(_("dev.SERIAL_WRITELN"))
        .appendField(new SerialDropdown(this),"DEVICE")
        .setCheck("STRING");
    this.setOutput(false);  
    this.setInputsInline(false);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
   },
   
   generateCode : function(generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       var text = generator.valueToCode(this,"VALUE");
       code = "abbozza.serialWriteLine(" + tx + "," + rx + "," + text + ");";
       
       return code;
   }
}

Blockly.Blocks['serial_println'] = Abbozza.SerialPrintLn;

/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialBaud = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.SERIAL"));
    this.appendDummyInput()
        .appendField(__("dev.SERIAL_BAUD",0))
        .appendField(new SerialDropdown(this),"DEVICE")
        .appendField(__("dev.SERIAL_BAUD",1))
        .appendField(new Blockly.FieldDropdown(Abbozza.baudMenu),"BAUD")
        .appendField(__("dev.SERIAL_BAUD",2));
    this.setOutput(false);  
    this.setInputsInline(false);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
   },
   
   generateCode : function(generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       var value = generator.fieldToCode(this,"BAUD");
       code = "abbozza.serialSetBaud(" + tx + "," + rx + "," + value + ");";
       
       return code;
   }
}

Blockly.Blocks['serial_baud'] = Abbozza.SerialBaud;

/**
 * Read a line from the serial connection.
 */
Abbozza.SerialReadln = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SERIAL"));
        this.appendDummyInput()
                .appendField(_("dev.SERIAL_READLN"))
                .appendField(new SerialDropdown(this), "DEVICE");
        this.setInputsInline(false);
        this.setOutput(true, "STRING");
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setTooltip('');
    },

    generateCode: function (generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       code = "abbozza.serialReadLine(" + tx + "," + rx + ")";
       
       return code;
    }

};

Blockly.Blocks['serial_readln'] = Abbozza.SerialReadln;


/**
 * Reads the whole buffer 
 */
Abbozza.SerialReadAll = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SERIAL"));
        this.appendDummyInput()
                .appendField(_("dev.SERIAL_READ_ALL"))
                .appendField(new SerialDropdown(this), "DEVICE");
        this.setInputsInline(false);
        this.setOutput(true, "STRING");
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setTooltip('');
    },

    generateCode: function (generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       code = "abbozza.serialReadAll(" + tx + "," + rx + ")";
       
       return code;
    }

};

Blockly.Blocks['serial_read_all'] = Abbozza.SerialReadAll;


/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialWriteByte = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.SERIAL"));
    this.appendValueInput("VALUE")
        .appendField(_("dev.SERIAL_WRITE_BYTE"))
        .appendField(new SerialDropdown(this),"DEVICE")
        .setCheck("NUMBER");
    this.setOutput(false);  
    this.setInputsInline(false);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
   },
   
   generateCode : function(generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       var value = generator.valueToCode(this,"VALUE");
       code = "abbozza.serialWriteByte(" + tx + "," + rx + "," + value + ");";
       
       return code;
   }
}

Blockly.Blocks['serial_write_byte'] = Abbozza.SerialWriteByte;



Abbozza.SerialReadByte = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.SERIAL"));
    this.appendDummyInput()
        .appendField(_("dev.SERIAL_READ_BYTE"))
        .appendField(new SerialDropdown(this),"DEVICE");
    this.setOutput(true,"NUMBER");  
    this.setInputsInline(false);
    this.setPreviousStatement(false);
    this.setNextStatement(false);
    this.setTooltip('');
   },
   
   generateCode : function(generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "";       
       code = "abbozza.serialReadByte(" + tx + "," + rx + ")";
       
       return code;
   }
}

Blockly.Blocks['serial_read_byte'] = Abbozza.SerialReadByte;



Abbozza.SerialAvailable = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SERIAL"));
        this.appendDummyInput()
                .appendField(__("dev.SERIAL_AVAILABLE",0))
                .appendField(new SerialDropdown(this), "DEVICE")
                .appendField(__("dev.SERIAL_AVAILABLE",1));
        this.setInputsInline(false);
        this.setOutput(true, "BOOLEAN");
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setTooltip('');
    },
    generateCode: function (generator) {
       var rx = "USBRX";
       var tx = "USBTX";
       var name = generator.fieldToCode(this,"DEVICE");
       
       if ( name != "USB") {
          var device = Abbozza.blockDevices.getDevice(name);

          if (device == null) {
              ErrorMgr.addError(this, _("err.UNKNOWN_DEVICE"));
              return;
          }

          tx = generator.fieldToCode(device,"TX");
          rx = generator.fieldToCode(device,"RX");
       }

       var code = "abbozza.serialIsAvailable(" + tx + "," + rx + ")";
       
       return code;
    }

};

Blockly.Blocks['serial_available'] = Abbozza.SerialAvailable;



Abbozza.SerialTableMenu = [
   ["digital","0"],
   ["0... 1023", "1"],
   ["0 .. 65535","2"],
   ["-32768 ... 32767","3"],
   ["-1024 ... 1023","4"]   
];


/**
 * Writes the given values to the serial port. The Monitor is able to parse them.
 */
Abbozza.SerialTable = {
  init: function() {
    var thisblock = this;
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.USB"));
    this.appendDummyInput()
        .appendField(_("dev.SERIAL_TABLE"))
        .appendField(new SerialDropdown(this), "DEVICE");
    this.appendValueInput("CHANNEL0")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 1")
        .appendField(new Blockly.FieldDropdown(Abbozza.SerialTableMenu, 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL0").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                    thisblock.getInput("CHANNEL0").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL0_TYPE");
    this.appendValueInput("CHANNEL1")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 2")
        .appendField(new Blockly.FieldDropdown(Abbozza.SerialTableMenu, 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL1").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                case "4" :
                    thisblock.getInput("CHANNEL1").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL1_TYPE");
    this.appendValueInput("CHANNEL2")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 3")
        .appendField(new Blockly.FieldDropdown(Abbozza.SerialTableMenu, 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL2").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                case "4" :
                    thisblock.getInput("CHANNEL2").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL2_TYPE");
    this.appendValueInput("CHANNEL3")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 4")
        .appendField(new Blockly.FieldDropdown(Abbozza.SerialTableMenu, 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL3").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                case "4" :
                    thisblock.getInput("CHANNEL3").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL3_TYPE");
    this.appendValueInput("CHANNEL4")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 5")
        .appendField(new Blockly.FieldDropdown(Abbozza.SerialTableMenu, 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL4").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                case "4" :
                    thisblock.getInput("CHANNEL4").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL4_TYPE");
    this.setOutput(false);
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
 }
}

Blockly.Blocks['serial_table'] = Abbozza.SerialTable;


/**
 * The dropdown menu for serial connections.
 * 
 * @param {type} block
 * @returns {SerialDropdown}
 */
SerialDropdown = function(block) {
	SerialDropdown.superClass_.constructor.call(this, this.getMenu , null);
	this.EDITABLE = true;
	// console.log(block);
	this.block = block;
	this.devtype = DEV_TYPE_SERIAL;
};
goog.inherits(SerialDropdown, Blockly.FieldDropdown);


SerialDropdown.prototype.getMenu = function() {
	if ( Abbozza.blockDevices == null) return [["USB", "USB"]];
        var menu = Abbozza.blockDevices.getDevicesByType(this.devtype);
        if ( menu[0][0] == "<name>") {
            menu = [["USB","USB"]];
        } else {
            menu.unshift(["USB","USB"]);
        }
	return menu;
}