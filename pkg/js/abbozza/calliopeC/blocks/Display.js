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
 * The Database for handling image names
 * 
 * @returns {undefined}
 */

/**
 * A list of all images known by micropython
 * 
 * @type Array
 */
Abbozza.Images = [
// "Image.HAPPY",
"Image.SMILEY",
"Image.SAD",
"Image.SURPRISED",
"Image.BEARD",
"Image.MOUSTACHE",
"Image.YES",
"Image.NO",
"Image.CHESSBOARD",
"Image.HEART",
"Image.HEART_SMALL",
"Image.DIAMOND",
"Image.DIAMOND_SMALL",
"Image.SQUARE",
"Image.SQUARE_SMALL",
"Image.ARROW_N",
"Image.ARROW_NE",
"Image.ARROW_E",
"Image.ARROW_SE",
"Image.ARROW_S",
"Image.ARROW_SW",
"Image.ARROW_W",
"Image.ARROW_NW"
 /*,
"Image.RABBIT",
"Image.COW",
"Image.MUSIC_CROTCHET",
"Image.MUSIC_QUAVER",
"Image.MUSIC_QUAVERS",
"Image.PITCHFORK",
"Image.XMAS",
"Image.PACMAN",
"Image.TARGET",
"Image.TSHIRT",
"Image.ROLLERSKATE",
"Image.DUCK",
"Image.HOUSE",
"Image.TORTOISE",
"Image.BUTTERFLY",
"Image.STICKFIGURE",
"Image.GHOST",
"Image.SWORD",
"Image.GIRAFFE",
"Image.SKULL",
"Image.UMBRELLA",
"Image.SNAKE"

"Image.TRIANGLE",
"Image.TRIANGLE_LEFT",
"Image.TRIANGLE_RIGHT",

"Image.CLOCK12",
"Image.CLOCK11",
"Image.CLOCK10",
"Image.CLOCK9",
"Image.CLOCK8",
"Image.CLOCK7",
"Image.CLOCK6",
"Image.CLOCK5",
"Image.CLOCK4",
"Image.CLOCK3",
"Image.CLOCK2",
"Image.CLOCK1"
*/
]

/**
 * The colors for the LEDs
 * @type Array
 */
Abbozza.DisplayColors = ["#ff0000","#800000","#400000","#200000","#100000","#080000","#040000","#020000","#010000","#000000"]

/**
 * This block scrolls a text from left to right
 * @type type
 */
Abbozza.DisplayPrint = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("TEXT")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(_("block.print"))
            .setCheck(["STRING","NUMBER","DECIMAL","BOOLEAN"]);
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        var text = generator.valueToCode(this,"TEXT","STRING");
        code = "abbozza.display.print("+text+");";
        return code;
    }    
}

Blockly.Blocks['display_print'] = Abbozza.DisplayPrint;


/**
 * This block scrolls a text from left to right
 * @type type
 */
Abbozza.DisplayScroll = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("TEXT")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(_("block.scroll"))
            .setCheck(["STRING","NUMBER","DECIMAL","BOOLEAN"]);
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        var text = generator.valueToCode(this,"TEXT","STRING");
        code = "abbozza.display.scroll("+text+");";
        return code;
    }    
}

Blockly.Blocks['display_scroll'] = Abbozza.DisplayScroll;


/**
 * This block scrolls a text from left to right
 * @type type
 */
Abbozza.DisplayScrollAsync = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.appendValueInput("TEXT")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(__("block.scrollAsync",0))
            .setCheck(["STRING","NUMBER","DECIMAL","BOOLEAN"]);
        this.appendDummyInput()
            .appendField(__("block.scrollAsync",1));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        var text = generator.valueToCode(this,"TEXT","STRING");
        code = "abbozza.display.scrollAsync("+text+");";
        return code;
    }    
}

Blockly.Blocks['display_scroll_async'] = Abbozza.DisplayScrollAsync;


/**
 * This block sets the color of a single pixel
 * 
 * @type type
 */
Abbozza.DisplaySetPixel = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.setInputsInline(true);
        var colorField = new Blockly.FieldColour("#ff0000");
        colorField.setColours(Abbozza.DisplayColors)
                    .setColumns(5)
                    .setText("");        
        this.appendValueInput("COL")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(__("block.setPixel",0))
           .setCheck("NUMBER");
        this.appendValueInput("ROW")
            .appendField(__("block.setPixel",1))
            .setCheck("NUMBER");
        this.appendValueInput("COLOR")
            .appendField(__("block.setPixel",2))
            .setCheck("NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        var row = generator.valueToCode(this,"ROW");
        var col = generator.valueToCode(this,"COL");
        var color = generator.valueToCode(this,"COLOR") % 10;
        generator.addSetupCode("abbozza.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        code = code + "abbozza.display.image.setPixelValue("+col+","+row+","+color+");";
        return code;
    }
}

Blockly.Blocks['display_set_pixel'] = Abbozza.DisplaySetPixel;


/**
 * This block returns the color value of a pixel
 * 
 * @type type
 */
Abbozza.DisplayGetPixel = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setInputsInline(true);
        this.setOutput(true,"NUMBER");
        this.appendValueInput("COL")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(__("block.getPixel",0))
           .setCheck("NUMBER");
        this.appendValueInput("ROW")
            .appendField(__("block.getPixel",1))
            .setCheck("NUMBER");
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        var row = generator.valueToCode(this,"ROW");
        var col = generator.valueToCode(this,"COL");
        code = "abbozza.display.image.getPixelValue(" + col + "," + row + ")";
        return code;
    }
}

Blockly.Blocks['display_get_pixel'] = Abbozza.DisplayGetPixel;


/**
 * This block sets all pixel to black
 * 
 * @type type
 */

Abbozza.DisplayClear = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.appendDummyInput()
           .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(_("block.displayClear"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        code = "abbozza.display.clear();";
        return code;
    }
}

Blockly.Blocks['display_clear'] = Abbozza.DisplayClear;


/**
 * This blockk switches the display on
 * 
 * @type type
 */

Abbozza.DisplayOn = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(_("block.displayOn"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        code = "abbozza.display.enable();";
        return code;
    }
}

Blockly.Blocks['display_on'] = Abbozza.DisplayOn;


/**
 * This block switches the display off
 * 
 * @type type
 */

Abbozza.DisplayOff = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(_("block.displayOff"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        code = "abbozza.display.disable();";
        return code;
    }
}

Blockly.Blocks['display_off'] = Abbozza.DisplayOff;


/**
 * This block returns true if the display is on, false otherwise
 * 
 * @type type
 */
Abbozza.DisplayIsOn = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setOutput(true,"BOOLEAN");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
           .appendField(_("block.displayIsOn"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        code = "display.is_on()";
        return code;
    }
}

Blockly.Blocks['display_is_on'] = Abbozza.DisplayIsOn;


/**
 * This block provides a dropdown menu for a display coordinate
 * 
 * @type type
 */
Abbozza.DisplayCoord = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
                .appendField(new Blockly.FieldDropdown([["0","0"],["1","1"],["2","2"],["3","3"],["4","4"]]),"VALUE");
        this.setTooltip('');        
    },
    generateCode : function(generator) {
        var code = this.getFieldValue("VALUE");
        return code;
    },
  
  confirm: function(flag) {
      if ( flag ) {
         this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
         this.confirmed = true;
     } else {
         this.setColour("#F0F0F0");
         this.confirmed = false;
     }
  },
  mutationToDom: function () {
    var mutation = null;
    if ( this.confirmed == false) {
       mutation = document.createElement('mutation');
       mutation.setAttribute("confirmed","false");
    }
    return mutation;
  },   
  domToMutation: function (xmlElement) {
     if ( xmlElement.hasAttribute("confirmed") ) {
        var state = xmlElement.getAttribute("confirmed");
        if ( state == "true" ) {
            this.confirm(true);
        } else {
            this.confirm(false);
        }
     } else {
        this.confirm(true);
     }
  }
}

Blockly.Blocks['display_coord'] = Abbozza.DisplayCoord;


/**
 * This block provides a color picker for the leds
 * 
 * @type type
 */
Abbozza.DisplayColor = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        var colorField = new Blockly.FieldColour("#ff0000");
        colorField.setColours(Abbozza.DisplayColors)
                    .setColumns(5)
                    .setText("");        
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(colorField,"COLOR");        
        this.setTooltip('');        
    },
    generateCode : function(generator) {
        var color = this.getFieldValue("COLOR");
        switch (color) {
            case "#ff0000": return 9;
            case "#800000": return 8;
            case "#400000": return 7;
            case "#200000": return 6;
            case "#100000": return 5;
            case "#080000": return 4;
            case "#040000": return 3;
            case "#020000": return 2;
            case "#010000": return 1;
            case "#000000": return 0;
        }        
        return 0;
    }
}

Blockly.Blocks['display_color'] = Abbozza.DisplayColor;


/**
 * This block displays one of the predefined images
 * 
 * @type type
 */

Abbozza.DisplayShowInternalImage = {
    init: function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.setOutput(false);
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
                .appendField(_("block.displayImage"))
                .appendField(new Blockly.FieldDropdown(this.getMenu),"IMAGE");
        this.setTooltip('');
    },
    getMenu : function() {
        var menu = [];
        for (var idx = 0; idx < Abbozza.Images.length; idx++) {
            menu.push([_(Abbozza.Images[idx]),Abbozza.Images[idx].replace(".","_")]);
        }
        return menu;
    },
    generateCode : function(generator) {
        var code = "abbozza.display.print(" + this.getFieldValue("IMAGE") +",0);";
        return code;
    }
}

Blockly.Blocks['display_show_internal_image'] = Abbozza.DisplayShowInternalImage;


Abbozza.DisplayImage = {
    init : function() {
        this.colors = ["#FF0000","#E60000","#D20000","#B40000","#960000","#780000","#5A0000","#3C0000","#1E0000","#000000"];
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(_("block.displayMatrix"));
            for (var row = 0; row < 5; row++) {
            var input = this.appendDummyInput();
            for (var col = 0; col < 5; col++) {
                var field = new Blockly.FieldColour("#000000");
                field.setColours(this.colors)
                     .setColumns(5)
                     .setText("");
                input.appendField(field,"F"+col+row)
                     .setAlign(Blockly.ALIGN_CENTRE);
            }
        }
        this.setOutput(false);
        this.setTooltip('');
    },
    
    generateCode: function(generator) {
        var row;
        var col;
        var code = "";
        var image = "";
        var unchanged = false;
        var imgcode = "";
        // check if an unchanged led occurs
        for (row=0; row <5; row++) {
            for (col=0; col < 5; col++) {
                var field = "F"+col+row;
                var value = this.getFieldValue(field);
                switch (value) {
                    case "#000000": image = "0"; break;
                    case "#1e0000": image = "1"; break;
                    case "#3c0000": image = "2"; break;
                    case "#5a0000": image = "4"; break;
                    case "#780000": image = "8"; break;
                    case "#960000": image = "16"; break;
                    case "#b40000": image = "32"; break;
                    case "#d20000": image = "64"; break;
                    case "#e60000": image = "128"; break;
                    case "#ff0000": image = "255"; break;
                    default:
                           image = "";
                }
                imgcode = imgcode + image;
                if ((col != 4) || (row != 4)) {
                    imgcode = imgcode + ",";                    
                }
                // if (image != "") {
                //     code = code + "abbozza.display.image.setPixelValue(" + col + "," + row + "," + image + ");\n";
                // }
            }
        }
        var counter = generator.imageCounter;
        generator.imageCounter++;
        generator.addSetupCode("abbozza.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        generator.addInitCode("const uint8_t __image_" + counter + "_data__[] __attribute__ ((aligned (4))) = { 0xff,0xff,5,0,5,0," + imgcode + " };");
        generator.addInitCode("MicroBitImage __image_" + counter + "__((ImageData*)__image_" + counter + "_data__);");
        return code + "abbozza.display.image.paste(__image_" + counter + "__);";
        // return code;
    }
}

Blockly.Blocks['display_image'] = Abbozza.DisplayImage;

/**
 * This block shows a given image
 * 
 * @type type
 */
Abbozza.DisplayShowImage = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DISPLAY"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");
        this.appendValueInput("IMAGE")
            .appendField(new Blockly.FieldImage("img/devices/output32.png",16,16))     
            .appendField(_("block.displayMatrix"))
            .setCheck(["STRING","IMAGE"]);
        this.setTooltip('');
    },
    
    generateCode: function(generator) {
        var code ="";
        var image = generator.valueToCode(this,"IMAGE");
        code = ""
        code = "abbozza.display.show(Image(\""+image+"\"))";
        generator.addSetupCode("abbozza.display.setDisplayMode(DISPLAY_MODE_GREYSCALE);");
        return code;
    }
}

Blockly.Blocks['display_show_image'] = Abbozza.DisplayShowImage;

/**
 * This block returns the color value of a pixel
 * 
 * @type type
 */
Abbozza.DisplayLightLevel = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setInputsInline(true);
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(new Blockly.FieldImage("img/devices/input32.png",16,16))     
           .appendField(_("block.getLightLevel"));
        this.setTooltip('');
    },
    generateCode : function(generator) {
        var code = "";
        code = "abbozza.display.readLightLevel()";
        
        return code;
    }
}

Blockly.Blocks['display_light_level'] = Abbozza.DisplayLightLevel;
