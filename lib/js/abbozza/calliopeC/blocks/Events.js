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

Abbozza.EventButtonList = [
    ["A","MICROBIT_ID_BUTTON_A"],
    ["B","MICROBIT_ID_BUTTON_B"]
];


Abbozza.EventButtonEventList = null;

Abbozza.setEventMenu = function() {
    Abbozza.EventButtonEventList = [
        [_("event.BUTTON_DOWN"),"MICROBIT_BUTTON_EVT_DOWN"],
        [_("event.BUTTON_UP"),"MICROBIT_BUTTON_EVT_UP"],
        [_("event.BUTTON_CLICK"),"MICROBIT_BUTTON_EVT_CLICK"],
        [_("event.BUTTON_LONG_CLICK"),"MICROBIT_BUTTON_EVT_LONG_CLICK"],
        [_("event.BUTTON_HOLD"),"MICROBIT_BUTTON_EVT_HOLD"],
        [_("event.BUTTON_DOUBLE_CLICK"),"MICROBIT_BUTTON_EVT_DOUBLE_CLICK"],
        [_("event.BUTTON_ANY"),"MICROBIT_BUTTON_EVT_ANY"]
    ];
    
    Abbozza.EventIOEventList = [
      [_("event.IO_ON_RISE"),"MICROBIT_PIN_EVT_RISE"],  
      [_("event.IO_ON_FALL"),"MICROBIT_PIN_EVT_FALL"],  
      [_("event.IO_ON_HI"),"MICROBIT_PIN_EVT_PULSE_HI"],  
      [_("event.IO_ON_LO"),"MICROBIT_PIN_EVT_PULSE_LO"],  
      [_("event.BUTTON_DOWN"),"MICROBIT_BUTTON_EVT_DOWN"],  
      [_("event.BUTTON_UP"),"MICROBIT_BUTTON_EVT_UP"],
      [_("event.BUTTON_CLICK"),"MICROBIT_BUTTON_EVT_CLICK"],
      [_("event.BUTTON_LONG_CLICK"),"MICROBIT_BUTTON_EVT_LONG_CLICK"],
      [_("event.BUTTON_HOLD"),"MICROBIT_BUTTON_EVT_HOLD"],
      [_("event.BUTTON_DOUBLE_CLICK"),"MICROBIT_BUTTON_EVT_DOUBLE_CLICK"],
      [_("event.BUTTON_ANY"),"MICROBIT_BUTTON_EVT_ANY"]
    ];
}

Abbozza.EventHandler = {
    symbols: null,
    name: "<name>",
    rettype: "VOID",
    init: function () {
        var thisBlock = this;
        this.symbols = new SymbolDB(this.workspace.globalSymbols);
        this.symbols.parentBlock = this;
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.INT"));
        this.appendDummyInput()
                .appendField(this.getSignature(), "SIGNATURE");
        this.appendStatementInput("STATEMENTS")
                .setCheck("STATEMENT");
        this.setTooltip('');
        this.setMutator(new DynamicMutator(function () {
            if (Configuration.getParameter("option.noArrays") == "true") {
                return ['devices_num_noconn', 'devices_string_noconn', 'devices_decimal_noconn', 'devices_boolean_noconn'];
            } else if (Configuration.getParameter("option.linArrays") == "true") {
                return ['devices_num', 'devices_string', 'devices_decimal', 'devices_boolean', 'arr_dimension_noconn'];
            } else {
                return ['devices_num', 'devices_string', 'devices_decimal', 'devices_boolean', 'arr_dimension'];
            }
        }));
        Abbozza.addDisposeHandler(this);
    },
    
    setTitle: function (title) {
    },
    
    nameHandler: function (name, oldName) {
        if (name != oldName) {
            var block = this.sourceBlock_;
            block.symbols.parentDB.delete(oldName);
            var rettype = block.getFieldValue("TYPE");
            block.symbols.parentDB.addFunction(name, type);
        }
    },   
    setSymbolDB: function (db) {
        this.symbols = db;
        this.symbols.parentBlock = this;
    },
    generateCode: function (generator) {
        var statements = generator.statementToCode(this, 'STATEMENTS', "   ");
        var code = "";
        var sig = "void " + this.name + "(MicroBitEvent __event__)";
        code = code + sig + " {\n";
        code = code + generator.variablesToCode(this.symbols,"   ");
        code = code + statements;
        code = code + "\n}\n\n";
        return code;
    },
    getSignature: function () {
        var signature = _("event.HANDLER") + " " + this.name;
        return signature;
    },
    
    compose: function (topBlock) {
        var nameField = topBlock.getField("NAME");
        if (nameField.editing == true)
            return;

        Abbozza.composeSymbols(topBlock, this);
        this.name = topBlock.getFieldValue("NAME");
        this.setFieldValue(this.getSignature(), "SIGNATURE");

        Abbozza.collectFunctions();
    },
    
    decompose: function (workspace) {
        var topBlock = Abbozza.decomposeSymbols(workspace, this, _("LOCALVARS"), false);
    
        // deactivate validator for initialization
        var val = topBlock.getField("NAME").getValidator();
        topBlock.getField("NAME").setValidator(null);
        topBlock.setFieldValue(this.name, "NAME");
        // reactivate validator
        topBlock.getField("NAME").setValidator(val);
        
        return topBlock;
    },
    
    mutationToDom: function () {
        var mutation = document.createElement('mutation');
        var child = document.createElement('name');
        child.setAttribute("name", this.name);
        mutation.appendChild(child);
        child = document.createElement('type');
        mutation.appendChild(child);
        mutation.appendChild(this.symbols.toDOM());
        return mutation;
    },
    
    domToMutation: function (xmlElement) {
        var child;
        for (var i = 0; i < xmlElement.childNodes.length; i++) {
            child = xmlElement.childNodes[i];
            if (child.tagName == 'symbols') {
                if (this.symbols == null) {
                    this.setSymbolDB(new SymbolDB(null, this));
                }
                this.symbols.fromDOM(child);
            } else if (child.tagName == "name") {
                this.name = child.getAttribute("name");
            } else if (child.tagName == "type") {
                this.rettype = child.getAttribute("type");
            }
        }
        this.setFieldValue(this.getSignature(), "SIGNATURE");
    },
    updateLook: function () {
        var no = Abbozza.deleteInputs(this, "VAR");

        var no = 0;
        var params = "";
        var entry;
        var variables = this.symbols.getSymbols();
        for (var i = 0; i < variables.length; i++) {
            entry = variables[i];
            if (entry[3] == this.symbols.VAR_SYMBOL) {
                this.appendDummyInput("VAR" + no).appendField(_(entry[1]) + " " + entry[0] + Abbozza.lenAsString(entry[2]));
                if (this.getInput("STATEMENTS"))
                    this.moveInputBefore("VAR" + no, "STATEMENTS");
                no++;
            } else if (entry[3] == this.symbols.PAR_SYMBOL) {
                params = params + ", " + entry[1] + " " + entry[0];
            }
        }

        params = params.substring(2);
    },

    onDispose: function () {
        Abbozza.globalSymbols.delete(this.name);
    }
};


Abbozza.EventHandlerControl = {
    init: function () {
        this.setHelpUrl(Abbozza.HERLP_URL);
        this.setColour(ColorMgr.getColor("cat.INT"));
        this.appendDummyInput()
                .appendField(_("func.NAME"))
                .appendField(new FieldNameInput("<name>", Abbozza.globalSymbols, Abbozza.globalSymbols.FUN_SYMBOL), "NAME");
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(_("LOCALVARS"));
        this.appendStatementInput("VARS").setCheck("VAR_DECL");
        this.setTooltip('');
    },
    setTitle: function (title) {
    }

};



Abbozza.EventButtonListen = {
  init: function() {
    if ( Abbozza.EventButtonEventList == null ) Abbozza.setEventMenu();
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getCatColor("cat.INT"));
    this.appendDummyInput()
    	.appendField(__("event.BUTTON_LISTEN",0))
        .appendField(new Blockly.FieldDropdown(Abbozza.EventButtonList), "BUTTON")
    	.appendField(__("event.BUTTON_LISTEN",1))
        .appendField(new Blockly.FieldDropdown(Abbozza.EventButtonEventList), "EVENT")
    	.appendField(__("event.BUTTON_LISTEN",2))
        .appendField(new FunctionDropdown(this, null,true), "ISR")
    	.appendField(__("event.BUTTON_LISTEN",3));
    this.setInputsInline(true);   
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
  },
  
  generateCode : function(generator) {
    var button = generator.fieldToCode(this,"BUTTON"); 
    var event =  generator.fieldToCode(this,"EVENT");
    var name =  generator.fieldToCode(this,"ISR");
    

    var symbols = this.getRootBlock().symbols;
    var symbol = symbols.exists(name);
    if ( !symbol || symbol[3] != symbols.ISR_SYMBOL) {
        ErrorMgr.addError(this,_("err.WRONG_NAME")+": " + name );
    }
    
    return "abbozza.messageBus.listen(" + button + "," + event + "," + name + ");";
  }
}

Abbozza.EventIOListen = {
  init: function() {
    if ( Abbozza.EventIOEventList == null ) Abbozza.setEventMenu();
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getCatColor("cat.INT"));
    this.appendDummyInput()
    	.appendField(__("event.IO_LISTEN",0))
        .appendField(new PinDropdown(), "PIN")
    	.appendField(__("event.IO_LISTEN",1))
        .appendField(new Blockly.FieldDropdown(Abbozza.EventIOEventList), "EVENT")
    	.appendField(__("event.IO_LISTEN",2))
        .appendField(new FunctionDropdown(this, null,true), "ISR")
    	.appendField(__("event.IO_LISTEN",3));
    this.setInputsInline(true);   
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
  },
  
  generateCode : function(generator) {
    var pin = generator.fieldToCode(this,"PIN"); 
    console.log(pin);
    var event =  generator.fieldToCode(this,"EVENT");
    var name =  generator.fieldToCode(this,"ISR");
    

    var symbols = this.getRootBlock().symbols;
    var symbol = symbols.exists(name);
    if ( !symbol || symbol[3] != symbols.ISR_SYMBOL) {
        ErrorMgr.addError(this,_("err.WRONG_NAME")+": " + name );
    }
    
    var type;
    if ((event == "MICROBIT_PIN_EVT_RISE") || ( event == "MICROBIT_PIN_EVT_FALL"))
        type = "MICROBIT_PIN_EVENT_ON_EDGE";
    else if ((event == "MICROBIT_PIN_EVT_HI") || ( event == "MICROBIT_PIN_EVT_LO"))
        type = "MICROBIT_PIN_EVENT_ON_PULSE";
    else if (event == "NONE")
        type = "MICROBIT_PIN_EVENT_NONE";
    else    
        type = "MICROBIT_PIN_EVENT_ON_TOUCH";
    
    var code = "abbozza.io.pin[" + pin +"].eventOn("+ type + ");";
    if ( event != "NONE" ) {
        code = code + "\nabbozza.messageBus.listen(MICROBIT_ID_IO_" + pin + " ," + event + " ," + name + ");";
    }
    return code;
  }
}

Abbozza.EventIOIgnore = {
  init: function() {
    if ( Abbozza.EventIOEventList == null ) Abbozza.setEventMenu();
    this.setHelpUrl(Abbozza.HELP_URL);
    this.setColour(ColorMgr.getCatColor("cat.INT"));
    this.appendDummyInput()
    	.appendField(_("event.IO_IGNORE"))
        .appendField(new PinDropdown(), "PIN");
    this.setInputsInline(true);   
    this.setPreviousStatement(true);
    this.setNextStatement(true);
    this.setTooltip('');
  },
  
  generateCode : function(generator) {
    var pin = generator.fieldToCode(this,"PIN"); 
        
    var code = "abbozza.io.pin[" + pin +"].eventOn(MICROBIT_PiN_EVENT_NONE);";
    return code;
  }
}

Blockly.Blocks['int_isr_control'] = Abbozza.EventHandlerControl;
Blockly.Blocks['int_isr'] = Abbozza.EventHandler;
Blockly.Blocks['event_button_listen'] = Abbozza.EventButtonListen;
Blockly.Blocks['event_io_listen'] = Abbozza.EventIOListen;
Blockly.Blocks['event_io_ignore'] = Abbozza.EventIOIgnore;
