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
 * @fileoverview Blocks for in and output control
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */


/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialPrintLn = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.USB"));
    this.appendValueInput("VALUE")
        .appendField(__("serial.PRINTLN",0))
        .setCheck("STRING");
    this.appendDummyInput()
        .appendField(__("serial.PRINTLN",1));
    this.setOutput(false);  
    this.setInputsInline(true);
    this.setPreviousStatement(true,"STATEMENT");
    this.setNextStatement(true,"STATEMENT");
    this.setTooltip('');
 },
  
  
  generateCode : function(generator) {
  	var value = generator.valueToCode(this,'VALUE');
        value = value;
        
  	// generator.checkValue(this,pin,0,Abbozza.board.digitalPins-1,"PIN_ERROR");
  
        if (AbbozzaGenerator.ERROR) return null;

        generator.serialRequired = true;

  	var code = "uart.write(" + value + ");\n";
  	var code = code + "uart.write(\"\\n\");";
  	return code;
  }
}

Blockly.Blocks['serial_println'] = Abbozza.SerialPrintLn;

/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialReadLn = {
   init: function() {
     this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.USB"));
    this.appendDummyInput()
        .appendField(_("serial.READLN"));
    this.setOutput(true,"STRING");  
    this.setTooltip('');
 },
  
  
  generateCode : function(generator) {

        generator.serialRequired = true;

  	var code = "uart.readLine()";
  	return code;
    }
}

Blockly.Blocks['serial_readln'] = Abbozza.SerialReadLn;

/**
 * Writes a string with a newline to the serial port.
 */
Abbozza.SerialAvailable = {
   init: function() {
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.USB"));
    this.appendDummyInput()
        .appendField(_("serial.AVAILABLE"));
    this.setOutput(true,"BOOLEAN");  
    this.setTooltip('');
    },
    
    generateCode_ : function(generator) {
        generator.serialRequired = true;
  	var code = "uart.any()";
  	return code;
    }
}

Blockly.Blocks['serial_available'] = Abbozza.SerialAvailable;

/**
 * Writes the given values to the serial port. The Monitor is able to parse them.
 */
Abbozza.SerialTable = {
  init: function() {
    var thisblock = this;
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getColor("cat.USB"));
    this.appendDummyInput()
        .appendField("Sende Werte an Plotter");
    this.appendValueInput("CHANNEL0")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 1")
        .appendField(new Blockly.FieldDropdown([["digital","0"],["0 .. 1023","1"],["0 .. 65535","2"],["-32768 ... 32,767","3"]], 
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
        .appendField(new Blockly.FieldDropdown([["digital","0"],["0 .. 1023","1"],["0 .. 65535","2"],["-32768 ... 32,767","3"]], 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL1").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                    thisblock.getInput("CHANNEL1").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL1_TYPE");
    this.appendValueInput("CHANNEL2")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 3")
        .appendField(new Blockly.FieldDropdown([["digital","0"],["0 .. 1023","1"],["0 .. 65535","2"],["-32768 ... 32,767","3"]], 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL2").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                    thisblock.getInput("CHANNEL2").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL2_TYPE");
    this.appendValueInput("CHANNEL3")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 4")
        .appendField(new Blockly.FieldDropdown([["digital","0"],["0 .. 1023","1"],["0 .. 65535","2"],["-32768 ... 32,767","3"]], 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL3").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                    thisblock.getInput("CHANNEL3").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL3_TYPE");
    this.appendValueInput("CHANNEL4")
        .setCheck(["BOOLEAN","NUMBER"])
        .setAlign(Blockly.ALIGN_RIGHT)
        .appendField("Kanal 5")
        .appendField(new Blockly.FieldDropdown([["digital","0"],["0 .. 1023","1"],["0 .. 65535","2"],["-32768 ... 32,767","3"]], 
            function(value) {
              switch (value) {
                case "0" :
                    thisblock.getInput("CHANNEL4").setCheck(["BOOLEAN","NUMBER"]);
                    break;
                case "1" :
                case "2" :
                case "3" :
                    thisblock.getInput("CHANNEL4").setCheck(["NUMBER","BOOLEAN"]);
                }                
            }
        ),"CHANNEL4_TYPE");
    this.setOutput(false);
    this.setPreviousStatement(true,"STATEMENT");
    this.setNextStatement(true,"STATEMENT");
    this.setTooltip('');
 },
  
  generateCode : function(generator) {
        // generator.addLibrary("#include <abbozza.h>");
        
        var pin0 = generator.valueToCodeUnchecked(this,"CHANNEL0",-1);
        var pin1 = generator.valueToCodeUnchecked(this,"CHANNEL1",-1);
        var pin2 = generator.valueToCodeUnchecked(this,"CHANNEL2",-1);
        var pin3 = generator.valueToCodeUnchecked(this,"CHANNEL3",-1);
        var pin4 = generator.valueToCodeUnchecked(this,"CHANNEL4",-1);
        
        var type0 = generator.fieldToCode(this,"CHANNEL0_TYPE");
        var type1 = generator.fieldToCode(this,"CHANNEL1_TYPE");
        var type2 = generator.fieldToCode(this,"CHANNEL2_TYPE");
        var type3 = generator.fieldToCode(this,"CHANNEL3_TYPE");
        var type4 = generator.fieldToCode(this,"CHANNEL4_TYPE");

        if ( type0 == "2" ) pin0 = "word("+pin0+")";
        if ( type1 == "2" ) pin1 = "word("+pin1+")";
        if ( type2 == "2" ) pin2 = "word("+pin2+")";
        if ( type3 == "2" ) pin3 = "word("+pin3+")";
        if ( type4 == "2" ) pin4 = "word("+pin4+")";
        
        var type = "" + type0 + type1 + type2 + type3 + type4;
        
        if (AbbozzaGenerator.ERROR) return null;

        generator.serialRequired = true;

  	// var code = "__measure("+pin0+","+pin1+","+pin2+","+pin3+","+pin4+");";
        var code = 'uart.write("[[table ' + type + '," + String(millis()) + "," + String(' + pin0 + ') + "," + String(' + pin1 + ') + "," ' 
            + ' + String(' + pin2 + ') + "," + String(' + pin3 + ') + "," + String(' + pin4 + ') + "]]");\n';
  	return code;
  }
}

Blockly.Blocks['serial_table'] = Abbozza.SerialTable;
