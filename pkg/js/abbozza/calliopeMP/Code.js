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

Blockly.Block.prototype.parametersToCode = function(generator) {
    if (!this.symbols || this.symbols == null ) return "";
    var parameters = this.symbols.getParameters(true);
    return generator.symbolsToCode(parameters,",");
}

Blockly.Block.prototype.variablesToCode = function(generator) {
    if (!this.symbols || this.symbols == null ) return "";
    var variables = this.symbols.getVariables(true);
    return generator.symbolsToCode(variables,";");
}


/**
 * Code snippets for blocks
 */

AbbozzaCode = [];

AbbozzaCode['cond_if'] = [ 'if (#):\n#\n', [ "V_CONDITION" , "S_STATEMENTS"] ];
AbbozzaCode['cond_if_else'] = [ 'if (#):\n#\nelse:\n#', [ "V_CONDITION" , "S_STATEMENTS1" , "S_STATEMENTS2" ] ];

AbbozzaCode['loop_endless'] = ["while True:\n#",["S_STATEMENTS"]];
AbbozzaCode['loop_while'] = ["while (#):\n#",["V_CONDITION","S_STATEMENTS"]];
AbbozzaCode['loop_fixed'] = ["for __counter in range(#):\n#",["V_COUNT","S_STATEMENTS"]];
AbbozzaCode['loop_count'] = ["for # in range(#, #, #):\n#",["F_VAR","V_FROM","V_TO","V_STEP","S_STATEMENTS"]];
AbbozzaCode['loop_count_dir'] = ["for # in range(#, #, #):\n #",["F_VAR","V_FROM","V_TO",
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
AbbozzaCode['loop_count_dir_step'] = ["for # in range(#, #, #):\n #",["F_VAR","V_FROM","V_TO","V_STEP",
                "S_STATEMENTS"
]];
AbbozzaCode['loop_delay'] = ["sleep(#)",["V_TIME"]];
AbbozzaCode['loop_break'] = ["break",[]];

AbbozzaCode['text_const'] = function(generator) {
  	var content = this.getFieldValue("CONTENT");
  	
	return goog.string.quote(content);
  };
AbbozzaCode['text_charat'] = ["#[#]",["V_TEXT","V_POS"]];
AbbozzaCode['text_concat'] = ["#+#",["V_TEXT1","V_TEXT2"]];
AbbozzaCode['text_from_number'] = ["repr(#)",["V_VALUE"]];
AbbozzaCode['text_from_ascii'] = ["chr(#)",["V_VALUE"]];
AbbozzaCode['ascii_from_text'] = ["ord(#.charAt(0))",["V_TEXT"]];
AbbozzaCode['text_length'] = ["ManagedString(#).length()",["V_TEXT"]];
// AbbozzaCode['text_substring'] = ["ManagedString(#).substring(#,#-#+1)",["V_TEXT","V_FROM","V_TO","V_FROM"]];
AbbozzaCode['text_as_string'] = ["repr(#)",["V_VALUE"]];


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
    };

AbbozzaCode['var_assign'] = ["#=#",["V_LEFT","V_RIGHT"]];

AbbozzaCode['logic_const'] = [ "#" ,[ "F_NAME" ]];
AbbozzaCode['logic_op'] = [ "(# # #)" ,[ "V_LEFT", "F_LOGOP", "V_RIGHT"]];
AbbozzaCode['logic_not'] = [ "not (#)" ,[ "V_ARG" ]];
AbbozzaCode['logic_compare'] = [ "(# # #)" ,[ "V_LEFT", "F_OP" , 
    function(generator) {
        var vtype = generator.getTypeOfValue(this,"LEFT");
  	var right = generator.valueToCode(this,"RIGHT",vtype);
        return right;
    }]];

AbbozzaCode['math_number'] = ["#", ["F_VALUE"] ];
AbbozzaCode['math_decimal'] = ["#", ["F_VALUE"] ];
AbbozzaCode['math_calc'] = function(generator) {
  	var left = generator.valueToCode(this,"LEFT");
  	var right = generator.valueToCode(this,"RIGHT");
  	var op = this.getFieldValue("OP");
  	if ( op == "POWER" ) {
  		return "(" + left + "**" + right +")";
  	}
  	return "(" + left + keyword(op) + right + ")";
  };
AbbozzaCode['math_round'] = [ "#(#)", ["F_OP", "V_ARG"] ];
AbbozzaCode['math_unary_x'] = [ "#(#)", [ "F_OP", "V_ARG" ]];

AbbozzaCode['math_binary'] = function(generator) {
  	var first = generator.valueToCode(this,"FIRST");
  	var second = generator.valueToCode(this,"SECOND");
  	var op = this.getFieldValue("OP");
  	switch (op) {
  		case "MAX":
  			return "max(" + first + "," + second + ")";
  			break;
  		case "MIN":
  			return "min(" + first + "," + second + ")";
  			break;
  	}
  	return "";
  };

AbbozzaCode['math_random'] = ["random.randrange(0,#)",["V_MAX"], function(generator) { generator.addLibrary(["","random"]); } ];
AbbozzaCode['math_random2'] = ["random.randint(#,#)",["V_MIN","V_MAX"], function(generator) { generator.addLibrary(["","random"]); } ];
AbbozzaCode['math_randomseed'] = ["random.seed(#)",["V_SEED"], function(generator) { generator.addLibrary(["","random"]); } ];
AbbozzaCode['math_millis'] = ["running_time()",[]];

AbbozzaCode['func_decl'] = ["def #:\n#\n\n",[ function(generator) {
        var signature = this.name + "(" + generator.parametersToCode(this.symbols,"    ") + ")";
        return signature;
    },"S_STATEMENTS"]];
AbbozzaCode['func_call'] = ["#(#)",["F_NAME", function(generator) {
        var code = "";        
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
        return code;
    }]];
AbbozzaCode['func_return'] = ["return #",["V_VALUE"]];


AbbozzaCode['display_scroll'] = [ 'display.scroll(#)', ["V_TEXT"]];

AbbozzaCode['serial_begin_rate'] = [ "abbozza.serial.baud(#)" , [ "F_RATE" ], function (generator) { generator.serialRequired = true; } ];
AbbozzaCode['serial_end'] = [ "" , []];
AbbozzaCode['serial_write_int'] = [ "", []];
AbbozzaCode['serial_print'] = [ "uart.write(#)", ["V_VALUE"], function (generator) { generator.serialRequired = true; } ];
AbbozzaCode['serial_println'] = [ "uart.write(#)\nuart.write(\"\\n\")", ["V_VALUE"], function (generator) { generator.serialRequired = true; } ];
AbbozzaCode['serial_readln'] = [ "str(uart.readline(),'utf-8')", [], function (generator) { generator.serialRequired = true; }];
AbbozzaCode['serial_available'] = [ "uart.any()", [], function (generator) { generator.serialRequired = true; }];
AbbozzaCode['serial_table'] = function(generator) {
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

        var code = 'uart.write("[[table ' + type + '," + str(running_time()) + "," + str(' + pin0 + ') + "," + str(' + pin1 + ') + "," ' 
            + ' + str(' + pin2 + ') + "," + str(' + pin3 + ') + "," + str(' + pin4 + ') + "]]\\n")';

  	return code;
  };
  