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
 * @fileoverview This file defines the code generated by the blocks.
 * 
 * Each code template is given in the follwoing form
 * 
 * Abbozza.Code[type] = [ <template> , [ <arg1>, <arg2>, ...] ]
 *  
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */


/**
 * Code snippets for blocks
 */

AbbozzaCode = [];

/**
 * Conditionals
 */
AbbozzaCode['cond_if'] = [ 'if (#) {\n#\n}', [ "V_CONDITION" , "S_STATEMENTS"] ];
AbbozzaCode['cond_if_else'] = [ 'if (#) {\n#\n} else {\n#\n}', [ "V_CONDITION" , "S_STATEMENTS1" , "S_STATEMENTS2" ] ];


/**
 * Loops
 */
AbbozzaCode['loop_endless'] = ["while (true) {\n#\n\n   abbozza.sleep(1);\n}",["S_STATEMENTS"]];
AbbozzaCode['loop_while'] = ["while (#) {\n#\n}",["V_CONDITION","S_STATEMENTS"]];
AbbozzaCode['loop_fixed'] = ["for (# __counter=0;__counter<#;__counter++) {\n#\n}",["K_NUMBER","V_COUNT","S_STATEMENTS"]];
AbbozzaCode['loop_count'] = ["for (#=#;#<#;#=#+#) {\n#\n}",["F_VAR","V_FROM","F_VAR","V_TO","F_VAR","F_VAR","V_STEP","S_STATEMENTS"]];
AbbozzaCode['loop_delay'] = ["abbozza.sleep(#);",["V_TIME"]];
AbbozzaCode['loop_delay_micros'] = ["abbozza.sleep(#);",["V_TIME"]];
AbbozzaCode['loop_delay_seconds'] = ["abbozza.sleep((#)/1000);",["V_TIME"]];
AbbozzaCode['loop_break'] = ["break;",[]];

AbbozzaCode['loop_count_dir'] = ["for(#=#;###;#=#+#) {\n#\n}",["F_VAR","V_FROM","F_VAR",
                function(generator) {
                    var dir = generator.fieldToCode(this, "DIR");
                    var rel = "<=";
                    if ( dir == "DESC" ) {
                        rel = ">=";
                    }
                    return rel;
                }
                ,"V_TO","F_VAR","F_VAR",
                function(generator) {
                    var dir = generator.fieldToCode(this, "DIR");
                    var step = "1";
                    if ( dir == "DESC" ) {
                        step = "-1";
                    }
                    return step;
                },
                "S_STATEMENTS"
]];

AbbozzaCode['loop_count_dir_step'] = ["for(#=#;###;#=#+#) {\n#\n}",["F_VAR","V_FROM","F_VAR",
                function(generator) {
                    var dir = generator.fieldToCode(this, "DIR");
                    var rel = "<=";
                    if ( dir == "DESC" ) {
                        rel = ">=";
                    }
                    return rel;
                }
                ,"V_TO","F_VAR","F_VAR","V_STEP",
                "S_STATEMENTS"
]];

/**
 * Math
 */
AbbozzaCode['math_number'] = [ "#", ["F_VALUE"] ];
AbbozzaCode['math_decimal'] = [ "#", ["F_VALUE"] ];
AbbozzaCode['math_calc'] = [ "#", [ function(generator) {
        var left = generator.valueToCode(this,"LEFT");
  	var right = generator.valueToCode(this,"RIGHT");
  	var op = this.getFieldValue("OP");
  	if ( op == "POWER" ) {
            return "pow(" + left + "," + right +")";
  	}
  	return "(" + left + keyword(op) + right + ")";
    } ]];
AbbozzaCode['math_round'] = [ "#(#)", ["F_OP", "V_ARG"]];
AbbozzaCode['math_unary_x'] = [ "#(#)", [ "F_OP", "V_ARG" ]];
// AbbozzaCode['math_binary'] = [ "#(#,#)", [ "F_OP", "V_FIRST", "V_SECOND" ]];
AbbozzaCode['math_random'] = [ "abbozza.random(#+1)", [ "V_MAX" ]];
AbbozzaCode['math_random2'] = [ "(abbozza.random((#+1)-#)+#)", [ "V_MAX" , "V_MIN", "V_MIN" ]];
AbbozzaCode['math_randomseed'] = [ "abbozza.seedRandom(#);", [ "V_SEED" ]];
AbbozzaCode['math_millis'] = [ "abbozza.systemTime()", []];

AbbozzaCode['logic_const'] = [ "#" ,[ "F_NAME" ]];
AbbozzaCode['logic_op'] = [ "(# # #)" ,[ "V_LEFT", "F_LOGOP", "V_RIGHT"]];
AbbozzaCode['logic_not'] = [ "(!#)" ,[ "V_ARG" ]];
/* AbbozzaCode['logic_compare'] = [ "(# # #)" ,[ "V_LEFT", "F_OP" , 
    function(generator) {
        var vtype = generator.getTypeOfValue(this,"LEFT");
        var rtype = generator.getTypeOfValue(this,"RIGHT");
        var right;
        if ( rtype == vtype ) {
            right = generator.valueToCode(this,"RIGHT");
        } else {
            right = generator.valueToCode(this,"RIGHT",vtype);
        }
        var op = generator.fieldToCode(this,"OP");
        if ( (vtype == "STRING") && ( (op == "!=" ) || ( op == "<=" ) || ( op == ">=" ) )  ) {
            ErrorMgr.addError(this,_("err.COMPARISON_NOT_ALLOWED"));
        }
        return right;
    }]]; */

AbbozzaCode['logic_compare'] =  
    function(generator) {
        var left = generator.valueToCode(this,"LEFT");
        var op = generator.fieldToCode(this,"OP");

        var vtype = generator.getTypeOfValue(this,"LEFT");
        var rtype = generator.getTypeOfValue(this,"RIGHT");

        var right;
        if ( rtype == vtype ) {
            right = generator.valueToCode(this,"RIGHT");
        } else {
            right = generator.valueToCode(this,"RIGHT",vtype);
        }
        
        if (vtype == "STRING") {
            if (op == "!=" ) {
                op = "==";
                return "!(" + left + " " + op + " " + right + ")";
            } else if ( op == "<=" ) {
                op = ">";
                return "!(" + left + " " + op + " " + right + ")";
            } else if ( op == ">=" ) {
                op = "<";
                return "!(" + left + " " + op + " " + right + ")";
            } else {
                return "(" + left + " " + op + " " + right + ")";                
            }
        } else {
            return "(" + left + " " + op + " " + right + ")";
        }
    };


AbbozzaCode['text_const'] = ["ManagedString(#)", [
        function(generator) {
            var content = this.getFieldValue("CONTENT");
            return goog.string.quote(content);            
        }
]];
AbbozzaCode['text_charat'] = ["ManagedString(#.charAt(#))",["V_TEXT","V_POS"]];
AbbozzaCode['text_concat'] = ["#+#",["V_TEXT1","V_TEXT2"]];
AbbozzaCode['text_from_number'] = ["ManagedString(#)",["V_VALUE"]];
AbbozzaCode['text_from_ascii'] = ["ManagedString(char(#))",["V_VALUE"]];
AbbozzaCode['ascii_from_text'] = ["((int) (#.charAt(0)))",["V_TEXT"]];
AbbozzaCode['ascii_from_text_pos'] = ["((int) (#.charAt(#)))",["V_TEXT","V_POS"]];
// AbbozzaCode['text_length'] = ["ManagedString(#).length()",["V_TEXT"]];
// AbbozzaCode['text_substring'] = ["ManagedString(#).substring(#,#-#+1)",["V_TEXT","V_FROM","V_TO","V_FROM"]];
AbbozzaCode['text_length'] = ["((#).length())",["V_TEXT"]];
AbbozzaCode['text_substring'] = ["((#).substring(#,#-#+1))",["V_TEXT","V_FROM","V_TO","V_FROM"]];
AbbozzaCode['text_is_empty'] = ["( # == \"\" )" , ["V_TEXT"] ];
AbbozzaCode['text_to_number'] = ["atoi((#).toCharArray())" , ["V_TEXT"] ];
AbbozzaCode['text_to_decimal'] = ["atof((#).toCharArray())" , ["V_TEXT"] ];

AbbozzaCode['var_block'] = function (generator) {
        this.getSymbol();
        var code = "";
        var name = generator.fieldToCode(this, "NAME");
        if (name == _("default.NAME")) {
            ErrorMgr.addError(this, _("err.NOVARIABLE"));
        }

        var symbols = this.getRootBlock().symbols;
        var symbol = symbols.exists(name);
        if ( !symbol || ((symbol[3] != symbols.VAR_SYMBOL) && (symbol[3] != symbols.PAR_SYMBOL)) ) {
            ErrorMgr.addError(this,_("err.WRONG_NAME")+": " + name );
        }

        var block;

        code = name;
        var no = 0;
        var index;
        while (this.getInput("DIM" + no)) {
            index = generator.valueToCode(this, 'DIM' + no);
            code = code + "[" + index + "]";
            no++;
        }
        return code;
    } 
AbbozzaCode['var_assign'] = [ "# = #;" ,["V_LEFT","V_RIGHT"]];

// Functions
AbbozzaCode['func_decl'] = function (generator) {
        var statements = generator.statementToCode(this, 'STATEMENTS', "   ");

        var code = "\n";

        var sig = keyword(this.rettype) + " " + this.name + "(";
        var spaces = "";
        for (var j = 0; j < sig.length - 1; j++)
            spaces = spaces + " ";
        code = code + sig;

        var pars = generator.parametersToCode(this.symbols,spaces);
       
        code = code + pars + "{\n";
        code = code + generator.variablesToCode(this.symbols,"   ");
        code = code + statements;
        
        if ( this.getInput("RETURN") ) {
            var returncode = generator.valueToCode(this,"RETURN");
            code = code + "\n   return " + returncode + ";";
        }
        code = code + "\n}\n\n";
        return code;
    };

AbbozzaCode['func_call'] = function (generator) {
        var code = "";
        var name = generator.fieldToCode(this, "NAME");
        
        var symbols = this.getRootBlock().symbols;
        var symbol = symbols.exists(name);
        if ( !symbol || ((symbol[3] != symbols.FUN_SYMBOL) && (symbol[3] != symbols.ISR_SYMBOL)) ) {
            ErrorMgr.addError(this,_("err.WRONG_NAME")+": " + name);
        }

        var block;

        code = name + "(";
        var no = 0;
        var par;
        var inp;
        while (inp = this.getInput("PAR" + no)) {
            if (inp.type == Blockly.INPUT_VALUE) {
                par = generator.valueToCode(this, "PAR" + no);
                if (no != 0)
                    code = code + ",";
                code = code + par;
            }
            no++;
        }
        code = code + ")";
        
        if ( symbol[1] == "VOID" ) {
            code = code +";";
        }
        
        return code;
    };
    
AbbozzaCode['func_return'] = function (generator) {
        var code = "";
        var result = "";
        var root = this.getRootBlock();
        if ( root && root.rettype && root.rettype != "VOID") {   
            result = " " + generator.valueToCode(this, "VALUE");
        }
        code = code + "return" + result + ";";
        if ( this.getRootBlock() ) {
            var root = this.getRootBlock();
            if ( root.type == "main" ) {
                // Ignore the return statement if in main block
                code = "";
            }
        }
        return code;
    };

AbbozzaCode['inout_pinmode'] = [ "" , []];
AbbozzaCode['inout_pin'] = [ "#" , [ "F_PIN" ]];
AbbozzaCode['inout_level'] = [ "#" , [ "F_LEVEL" ]];
AbbozzaCode['inout_digital_read'] = [ "abbozza.io.pin[#].getDigitalValue()", ["V_PIN"]];
AbbozzaCode['inout_digital_write'] = [ "abbozza.io.pin[#].setDigitalValue(#);", ["V_PIN" , "V_LEVEL"]];
AbbozzaCode['inout_analog_read'] = [ "abbozza.io.pin[#].getAnalogValue()", ["V_PIN"]];
AbbozzaCode['inout_analog_write'] = [ "abbozza.io.pin[#].setAnalogValue(#);", ["V_PIN" , "V_VALUE"]];


AbbozzaCode['serial_begin_rate'] = [ "abbozza.serial.baud(#);" , [ "F_RATE" ], function (generator) { generator.serialRequired = true; } ];
AbbozzaCode['serial_end'] = [ "" , []];
AbbozzaCode['serial_write_int'] = [ "", []];
AbbozzaCode['serial_print'] = [ "abbozza.serial.send(#,ASYNC);", ["V_VALUE"], function (generator) { generator.serialRequired = true; } ];
// AbbozzaCode['serial_println'] = [ "abbozza.serial.send(#,ASYNC);\nabbozza.serial.send(\"\\n\");", ["V_VALUE"], function (generator) { generator.serialRequired = true; } ];
AbbozzaCode['serial_readln'] = [ "abbozza.serial.read(abbozza.serial.rxBufferedSize(),ASYNC)", [], function (generator) { generator.serialRequired = true; }];
AbbozzaCode['serial_available'] = [ "abbozza.serial.isReadable()", [], function (generator) { generator.serialRequired = true; }];
AbbozzaCode['serial_table'] = function(generator) {
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

        if ( type0 == "2" ) pin0 = pin0;
        if ( type1 == "2" ) pin1 = pin1;
        if ( type2 == "2" ) pin2 = pin2;
        if ( type3 == "2" ) pin3 = pin3;
        if ( type4 == "2" ) pin4 = pin4;
        
        var type = "" + type0 + type1 + type2 + type3 + type4;
        
        if (AbbozzaGenerator.ERROR) return null;

        generator.serialRequired = true;

        var code = "abbozza.serialRedirect(" + tx + "," + rx + ");\n";
        code = code + 'abbozza.serial.printf("[[table %s,%d,%d,%d,%d,%d,%d]]\\n","'+ type +'",abbozza.systemTime(),';
        code = code + pin0 + ',' + pin1 + ',' + pin2 + ',' + pin3 + ',' + pin4;
        code = code + ',ASYNC);\n';

  	return code;
  };
  
  
  AbbozzaCode['message_parse'] = function (generator) {
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
        
        generator.serialParserRequired = true;
        
        code = "__serial_parser.check(" + tx + "," + rx +");\n";
        var checkStdMsg = this.getFieldValue("STANDARD");
        if ( checkStdMsg == "TRUE") {
            code = code + "__serial_parser.execute();"
        }
        
        var commands = generator.statementToCode(this,"COMMANDS","");

        code = code + "\n" + commands;

        return code;
    };
    
AbbozzaCode['message_cmd_decl'] = 
        [ "if ( __serial_parser.getCmd() == \"#\" ) {\n#\n}" , ["F_COMMAND","S_STATEMENTS"] , function (generator) { generator.serialParserRequired = true; }];
AbbozzaCode['message_number'] = [ "__serial_parser.parse_#()" , ["K_NUMBER"] , function (generator) { generator.serialParserRequired = true; }];
AbbozzaCode['message_decimal'] = [ "__serial_parser.parse_#()" , ["K_DECIMAL"] , function (generator) { generator.serialParserRequired = true; }];
AbbozzaCode['message_word'] = [ "__serial_parser.parse_string()" , [] , function (generator) { generator.serialParserRequired = true; }];
AbbozzaCode['message_string'] = [ "__serial_parser.parse_word()" , [] , function (generator) { generator.serialParserRequired = true; }];
AbbozzaCode['message_response'] = ["__serial_parser.sendResponse(#);" , [
   function(generator) {
            var content = this.getFieldValue("MSG");
            return goog.string.quote(content);
            generator.serialParserRequired = true;
        }
] ];

AbbozzaCode['parse_set_line'] = [ "__parser.setLine(#);" , ["V_LINE"] , 
    function(generator) { generator.parserRequired = true; } ];
AbbozzaCode['parse_number'] = [ "__parser.parse_#()" , ["K_NUMBER"] , 
    function(generator) { generator.parserRequired = true; } ];
AbbozzaCode['parse_decimal'] = [ "__parser.parse_#()" , ["K_DECIMAL"] , 
    function(generator) { generator.parserRequired = true; } ];
AbbozzaCode['parse_word'] = [ "__parser.parse_word()" , [] , 
    function(generator) { generator.parserRequired = true; } ];
AbbozzaCode['parse_word'] = [ "__parser.parse_string()" , [] , 
    function(generator) { generator.parserRequired = true; } ];
AbbozzaCode['parse_eol'] = [ "__parser.endOfLine()" , [] , 
    function(generator) { generator.parserRequired = true; } ];

AbbozzaCode['i2c_write'] = [ "abbozza.i2cBeginTransmission(#);\n#\nabbozza.i2cEndTransmission();" , ["V_ADDR","S_BYTES"] ];
AbbozzaCode['i2c_write_byte'] = [ "abbozza.i2cWriteByte(#);" , ["V_VALUE"] ];
AbbozzaCode['i2c_write_short'] = [ "abbozza.i2cWriteShort(#);" , ["V_VALUE"] ];
AbbozzaCode['i2c_write_int'] = [ "abbozza.i2cWriteInt(#);" , ["V_VALUE"] ];
AbbozzaCode['i2c_write_text'] = [ "abbozza.i2cWriteText(#);" , ["V_VALUE"] ];
AbbozzaCode['i2c_read'] = [ "abbozza.i2cRequest(#,#)" , ["V_ADDR","V_LENGTH"] ];
AbbozzaCode['i2c_request'] = [ "abbozza.i2cRequest(#,#,#)" , ["V_ADDR","V_REGISTER","V_LENGTH"] ];