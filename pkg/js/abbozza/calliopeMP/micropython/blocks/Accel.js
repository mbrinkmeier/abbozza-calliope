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


Abbozza.AccelGet = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(_("dev.ACCEL_GET"))
            .appendField(new Blockly.FieldDropdown([[_("dev.ACCEL_X"),"getX"],[_("dev.ACCEL_Y"),"getY"],[_("dev.ACCEL_Z"),"getZ"]]),"DIR");
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        var dir = this.getFieldValue("DIR");
        if ( dir == "getX") {
            dir = "get_x";
        } else if ( dir == "getY") {
            dir = "get_y";
        } else if ( dir == "getZ") {
            dir = "get_z";
        }
        code = "accelerometer." + dir + "()";
        return code;
    }
}

Blockly.Blocks['dev_accel_get'] = Abbozza.AccelGet;



Abbozza.GestureMenu = [
    [_("dev.UP"),"up"],
    [_("dev.DOWN"),"down"],
    [_("dev.LEFT"),"left"],
    [_("dev.RIGHT"),"right"],
    [_("dev.FACE_UP"),"face up"],
    [_("dev.FACE_DOWN"),"face down"],
    [_("dev.FREEFALL"),"freefall"],
    [_("dev.3G"),"3g"],
    [_("dev.6G"),"6g"],
    [_("dev.8G"),"8g"],
    [_("dev.SHAKE"),"shake"]
]

Abbozza.AccelGesture = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"STRING");
        this.appendDummyInput()
            .appendField(_("dev.ACCEL_GESTURE"));
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        code = "accelerometer.current_gesture()";
        return code;
    }
}

Blockly.Blocks['dev_accel_gesture'] = Abbozza.AccelGesture;



Abbozza.AccelIsGesture = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"BOOLEAN");
        this.appendDummyInput()
            .appendField(_("dev.ACCEL_IS_GESTURE"))
            .appendField(new Blockly.FieldDropdown([
    [_("dev.UP"),"up"],
    [_("dev.DOWN"),"down"],
    [_("dev.LEFT"),"left"],
    [_("dev.RIGHT"),"right"],
    [_("dev.FACE_UP"),"face up"],
    [_("dev.FACE_DOWN"),"face down"],
    [_("dev.FREEFALL"),"freefall"],
    [_("dev.3G"),"3g"],
    [_("dev.6G"),"6g"],
    [_("dev.8G"),"8g"],
    [_("dev.SHAKE"),"shake"]]),"GESTURE");
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        var gesture = this.getFieldValue("GESTURE");
        code = "accelerometer.is_gesture(\"" + gesture + "\")";
        return code;
    }
}

Blockly.Blocks['dev_accel_is_gesture'] = Abbozza.AccelIsGesture;



Abbozza.AccelWasGesture = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"BOOLEAN");
        this.appendDummyInput()
            .appendField(_("dev.ACCEL_WAS_GESTURE"))
            .appendField(new Blockly.FieldDropdown([
    [_("dev.UP"),"up"],
    [_("dev.DOWN"),"down"],
    [_("dev.LEFT"),"left"],
    [_("dev.RIGHT"),"right"],
    [_("dev.FACE_UP"),"face up"],
    [_("dev.FACE_DOWN"),"face down"],
    [_("dev.FREEFALL"),"freefall"],
    [_("dev.3G"),"3g"],
    [_("dev.6G"),"6g"],
    [_("dev.8G"),"8g"],
    [_("dev.SHAKE"),"shake"]]),"GESTURE");
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        var gesture = this.getFieldValue("GESTURE");
        code = "accelerometer.was_gesture(\"" + gesture + "\")";
        return code;
    }
}

Blockly.Blocks['dev_accel_was_gesture'] = Abbozza.AccelWasGesture;


Abbozza.Gesture = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"STRING");
        this.appendDummyInput()
            .appendField(_("dev.GESTURE"))
            .appendField(new Blockly.FieldDropdown([
                [_("dev.UP"),"up"],
                [_("dev.DOWN"),"down"],
                [_("dev.LEFT"),"left"],
                [_("dev.RIGHT"),"right"],
                [_("dev.FACE_UP"),"face up"],
                [_("dev.FACE_DOWN"),"face down"],
                [_("dev.FREEFALL"),"freefall"],
                [_("dev.3G"),"3g"],
                [_("dev.6G"),"6g"],
                [_("dev.8G"),"8g"],
                [_("dev.SHAKE"),"shake"]]),"GESTURE");
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        var gesture = this.getFieldValue("GESTURE");
        code = "\"" + gesture + "\"";
        return code;
    }
}

Blockly.Blocks['dev_gesture'] = Abbozza.Gesture;


Abbozza.CompassGetFS = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(_("dev.COMPASS_GET_FS"))
            .appendField(new Blockly.FieldDropdown([[_("dev.COMPASS_X"),"get_x"],[_("dev.COMPASS_Y"),"get_x"],[_("dev.COMPASS_Z"),"get_z"],[_("dev.COMPASS_OVERALL"),"get_field_strength"]]),"DIR");
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        var dir = this.getFieldValue("DIR");
        code = "compass." + dir + "()";
        return code;
    }
}

Blockly.Blocks['dev_compass_get_fs'] = Abbozza.CompassGetFS;




Abbozza.CompassHeading = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(_("dev.COMPASS_HEADING"))
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        code = "compass.heading()";
        return code;
    }
}

Blockly.Blocks['dev_compass_heading'] = Abbozza.CompassHeading;


Abbozza.CompassCalibrate = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(true,"STATEMENT");
        this.setNextStatement(true,"STATEMENT");            
        this.setOutput(false);
        this.appendDummyInput()
            .appendField(_("dev.COMPASS_CALIBRATE"))
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        code = "compass.calibrate();";
        return code;
    }
}


Blockly.Blocks['dev_compass_calibrate'] = Abbozza.CompassCalibrate;


Abbozza.CompassIsCalibrated = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"BOOLEAN");
        this.appendDummyInput()
            .appendField(_("dev.COMPASS_IS_CALIBRATED"))
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        code = "compass.is_calibrated()";
        return code;
    }
}


Blockly.Blocks['dev_compass_is_calibrated'] = Abbozza.CompassIsCalibrated;


Abbozza.ThermometerGet = {
    init : function() {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getCatColor("cat.DEVIN"));
        this.setPreviousStatement(false);
        this.setNextStatement(false);            
        this.setOutput(true,"NUMBER");
        this.appendDummyInput()
            .appendField(_("dev.THERMOMETER_GET"))
        this.setTooltip('');
    },
    
    generateCode : function(generator) {
        var code="";
        code = "thermometer.getTemperature()";
        return code;
    }
}

Blockly.Blocks['dev_thermometer_get'] = Abbozza.ThermometerGet;

