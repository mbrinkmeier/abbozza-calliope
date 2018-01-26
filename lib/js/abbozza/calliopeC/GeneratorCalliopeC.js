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
 * @fileoverview The Code Generator of Abbozza.
 * 
 * It provides several methods for the generation of code from
 * the blocks.
 * 
 * It allows to add erros, which are diaplayed by icons.
 * 
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */

/**
 * Each value block has to have at most one of the following types:
 * "NUMBER", "TEXT", "DECIMAL, "BOOLEAN"
 * 
 * Each block can have the following types in addition:
 * "VAR"
 * 
 * Plug types
 * "ARR_DIM", "VAR_DECL"
 */

/**
 * Definition of the dictionary of reserved words
 */

ReservedWords.list = ",abbozza,calliope,if,else,for,switch,case,while,do,break,continue,return,goto," +
        "#define,#include,HIGH,LOW,INPUT,OUTPUT,INPUT_PULLUP,LED_BUILTIN,true,false," +
        "void,boolean,char,unsigned,byte,int,word,long,short,float,double,string,String,unit8_t," +
        "sizeof,PROGMEM,accelerometer,display,sleep,wait_ms,compass,ManagedString," +
        "MicroBitEvent,MicroBitImage,PocketBuffer,MicroBitI2C,MicroBitStorage,MicroBitSerial," +
        "MicroBit," +
        "Keyboard.releaseAll,Keyboard.write";


/**
 * This operation is the entry point for code generation.
 * It Iterates through the top blocks of the workspace, generates their
 * code and combines it. In addition it adds libraries and additional
 * required statements not directly generated. 
 */

AbbozzaGenerator.prototype.workspaceToCode = function (opt_workspace) {
    ErrorMgr.clearErrors();
    this.startGenerator = false;

    this.serialRequired = false;
    this.radioRequired = false;
    this.parserRequired = false;
    this.imageCounter = 0;

    this.startMonitor = false;

    // Fetch all blocks from the workspace
    var workspace = opt_workspace || Blockly.mainWorkspace;
    this.preSetup = "";
    this.setupHookCode = "";
    this.initHookCode = "";
    this.libraries = [];
    this.signatureCode = "";
    this.blockCode = "";
    this.mainCode = "";
    this.deviceCode = "";
    this.globalVarCode = "";
    
    var code = this.codeTemplate;
    
    // Get all top blocks from Workspace
    var origBlocks = workspace.getTopBlocks(true);

    /**
     * Rearrange the top blocks:
     * blocks[0] : device block if present
     * blocks[1] : main block
     * blocks[i] : function declarations
     * 
     * Initialize device and main block by null.
     */
    var blocks = [null, null];
    for (var i = 0; i < origBlocks.length; i++) {
        block = origBlocks[i];
        if (block.type == "devices") {
            blocks[0] = block;
        } else if (block.type.startsWith("main")) {
            blocks[1] = block;
        } else if (block.type == "func_decl") {
            blocks.push(block);
        } else if ( block.type == "int_isr") {
           blocks.push(block);
        }
    }

    // Iterate through the blocks and generate the code
    for (var x = blocks.length - 1; x >= 0; x--) {
        var block = blocks[x];
        if (block) {  // catching missing device block
            var line = this.topBlockToCode(block, "");
            if (line) {
                // Seperate operation blocks
                if ( (block.type == "func_decl") || (block.type == "int_isr") ) {
                    this.signatureCode = this.signatureCode + block.getSignature() + ";\n";
                    this.blockCode = this.blockCode + line;
                } else if (block.type == "devices") {
                    this.deviceCode = this.deviceCode + line;                    
                } else {
                   this.mainCode = this.mainCode + line;
               }
            }
        }
    }

    this.blockCode = this.signatureCode + "\n" + this.blockCode;
    
    // replace block tags
    code = code.replace(/###globalvars###/g, this.globalVarCode);
    code = code.replace(/###main###/g, this.mainCode);
    code = code.replace(/###blocks###/g, this.blockCode);
    code = code.replace(/###devices###/g, this.deviceCode);
    
    // Check if some block requires the initialization of the serial communication.
    // If yes, add the code:
    // Serial.begin( <rate> );
    if (this.serialRequired == true) {
        // this.setupHookCode = this.setupHookCode + "Serial.begin(" + Abbozza.serialRate + ");" + "\n";
    }
    
    var definesCode = "";
    if (this.radioRequired == true) {
      definesCode = definesCode + "#define ABZ_RADIO";
      this.initHookCode = this.initHookCode + "abbozza.radio.enable();\n";
    } else {
      definesCode = definesCode + "#define ABZ_BLUETOOTH";
    }
    
    definesCode = definesCode.trim();
    code = code.replace(/###defines###/g,definesCode);

    if ( this.parserRequired == true ) {
      this.addLibrary("lib/abbozzaParser.h");
      this.addPreSetup("AbbozzaParser __parser = AbbozzaParser(&abbozza);");
    }

    // Add the setuphook for other blocks.
    // Each block may add some code to the setuphook. It is added in the 
    // order the blocks are called to generate their code.
    if ( this.setupHookCode != "" ) {
       this.setupHookCode = "\n" + this.setupHookCode;
       this.setupHookCode = this.setupHookCode + "\n";
    }
    
    // Replace the setuphook by the constructed code.
    code = code.replace(/###setuphook###/g, this.setupHookCode);

    // Add the inithook inside main for other blocks
    if ( this.initHookCode != "" ) {
       this.initHookCode = "\n" + this.initHookCode;
       this.initHookCode = this.initHookCode.replace(/\n/g, "\n   ");
       this.initHookCode = this.initHookCode;
    }

    // Replace the setuphook by the constructed code.
    code = code.replace(/###inithook###/g, "\t" + this.initHookCode);
    
    // Replace the libraries
    var libCode = "";
    if (this.libraries.length != 0 ) {
      for (var i = 0; i < this.libraries.length; i++) {
        libCode = libCode + "#include <" + this.libraries[i] + ">\n";
      }
      libCode = libCode + "\n";
    }
    code = code.replace(/###libraries###/g,libCode);

    // Prepend the variable declarations
    // code = this.finish(code);

    // Final scrubbing of whitespace.
    code = code.replace(/^\s+\n/, '');
    code = code.replace(/\n\s+$/, '\n');
    code = code.replace(/[ \t]+\n/g, '\n');

    return code;
};


/**
 * Prepend the generated code with a general
 * comment, required libraries and pre setup code.
 * 
 * @param {string} code Generated code.
 * @return {string} Completed code.
 */
AbbozzaGenerator.prototype.finish = function (code) {
    // Generate the general comment    
    acode = "/**\n" +
            " *  Generated by abbozza!\n" +
            " */\n\n";

    acode = acode + "#include \"MicroBit.h\"\n";
    acode = acode + "#include \"lib/abbozzaDevice.h\"\n";
    acode = acode + "#include <string.h>\n";

    // Add required libraries. 
    if (this.libraries.length != 0) {
        acode = acode + "/*\n * Bibliotheken\n */\n";
        for (var i = 0; i < this.libraries.length; i++) {
            acode = acode + "#include <" + this.libraries[i] + ">\n";
        }
        acode = acode + "\n";
    }

    acode = acode + "\n";
    
    // Add initialization of calliope
    acode = acode + "\nAbbozza abbozza;\n\n"

    // Add pre setup code
    if (this.preSetup != "") {
        acode = acode + "/*\n * Vorbereitungen\n */\n" + this.preSetup + "\n\n";
    }
    
    return acode + code;
};


/**
 * Add a required library
 */
AbbozzaGenerator.prototype.addLibrary = function (lib) {
    for (var i = 0; i < this.libraries.length; i++) {
        if (this.libraries[i] == lib)
            return;
    }
    this.libraries.push(lib);
}



/**
 * Generates a string for a symbol of the form
 * <type> <name><arraydim> \t //<comment>
 */
AbbozzaGenerator.prototype.symbolToCode = function (symbol) {
    var name = symbol[0];
    var type = symbol[1];
    var len = symbol[2];
    var code = "";

    var code = keyword(type) + " " + name + Abbozza.lenAsString(len);

    return code;
}


AbbozzaGenerator.prototype.lenAsTuple = function (len, value) {
    if (len == null)
        return value;

    var tuple = value;
    var dim = ""

    for (var i = len.length - 1; i >= 0; i--) {
        dim = tuple;
        for (var j = 1; j < len[i]; j++) {
            dim = dim + "," + tuple;
        }
        tuple = "[" + dim + "]";
    }

    return "array(" + tuple + ")";
}



/**
 * Generates the list of variables in the symbolDB of the form
 * <type> <name><dimension>;
 * <type> <name><dimension>;
 * ...
 */
AbbozzaGenerator.prototype.variablesToCode = function (symbols, prefix) {
    var code = "";
    var variables = symbols.getVariables(true);
    for (var i = 0; i < variables.length; i++) {
        var entry = variables[i];
        code = code + prefix + this.symbolToCode(entry) + ";"; // keyword(entry[1]) + " " + entry[0] + Abbozza.lenAsString(entry[2]) + ";";
        if ((entry[4] != "") && (entry[4] != null))
            code = code + "\t// " + entry[4].replace(/\n/g, " ");
        code = code + "\n";
    }
    return code;
}

/**
 * Generates a list of parameters for functions.
 */
AbbozzaGenerator.prototype.parametersToCode = function (symbols, prefix) {
    var parameters = symbols.getParameters(true);
    var pars = "";
    var entry;

    if (parameters.length > 0) {
        entry = parameters[0];
        pars = " " + this.symbolToCode(entry); // keyword(entry[1]) + " " + entry[0] + Abbozza.lenAsString(entry[2]);
        // pars = " " + keyword(entry[1]) + " " + entry[0] + Abbozza.lenAsString(entry[2]);
        if (entry[4] != null && entry[4] != "") {
            pars = pars + "\t// " + entry[4].replace(/\n/g, " ");
        }
        for (var i = 1; i < parameters.length; i++) {
            entry = parameters[i];
            pars = pars + ",\n" + prefix + "  " + this.symbolToCode(entry); // keyword(entry[1]) + " " + entry[0] + Abbozza.lenAsString(entry[2]);
            if (entry[4] != null && entry[4] != "") {
                pars = pars + "\t// " + entry[4].replace(/\n/g, " ");
            }
        }
        pars = pars + " ) ";
    } else {
        pars = ") ";
    }

    return pars;
}



/**
 * This operation adds a type cast to the given code.
 */
AbbozzaGenerator.prototype.enforceType = function (code, enfType) {
    switch (enfType) {
        case "NUMBER":
            code = "((" + keyword("NUMBER") + ") " + code + ")";
            break;
        case "TEXT":
        case "STRING":
            code = "ManagedString(" + code + ")";
            break;
        case "DECIMAL":
            code = "((" + keyword("DECIMAL") + ") " + code + ")";
            break;
        case "BBOLEAN":
            code = "((bool) " + code + ")";
            break;
    }
    return code;
}


/**
 * The keywords for abbozza! labels
 */
__keywords = [
    ["VOID", "void"],
    ["NUMBER", "int"],
    ["STRING", "ManagedString"],
    ["DECIMAL", "double"],
    ["BOOLEAN", "bool"],
    ["TRUE", "true"],
    ["FALSE", "false"],
    ["AND", "and"],
    ["OR", "or"],
    ["EQUALS", "=="],
    ["INEQUAL", "!="],
    ["LESS", "<"],
    ["LESSEQ", "<="],
    ["GREATER", ">"],
    ["GREATEREQ", ">="],
    ["ROUND", "round"],
    ["FLOOR", "floor"],
    ["CEIL", "ceil"],
    ["ABS", "fabs"],
    ["SQRT", "sqrt"],
    ["SIN", "sin"],
    ["COS", "cos"],
    ["TAN", "tan"],
    ["ASIN", "asin"],
    ["ACOS", "acos"],
    ["ATAN", "atan"],
    ["MIN", "min"],
    ["MAX", "max"],
    ["PLUS", "+"],
    ["MINUS", "-"],
    ["MULT", "*"],
    ["DIV", "/"],
    ["MOD", "%"],
    ["POWER", "**"],
    ["HIGH","1"],
    ["LOW","0"]
];
__dict = __keywords;
