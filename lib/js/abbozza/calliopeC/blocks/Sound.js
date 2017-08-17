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


Abbozza.DeviceSpeakerMute = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendDummyInput("FREQUENCY")
                .appendField(_("dev.SOUND_MUTE"));
        this.setInputsInline(true);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        return "abbozza.soundmotor.soundOff();";
    }
};

Abbozza.DeviceSpeakerPlay = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendValueInput("FREQUENCY")
                .appendField(_("dev.SOUND_PLAY"))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var freq = generator.valueToCode(this,"FREQUENCY");
        return "abbozza.soundmotor.soundOn(" + freq + ");";
    }
};


Abbozza.DeviceSpeakerPlayDur = {
    init: function () {
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendValueInput("FREQUENCY")
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_PLAY_DUR",0))
                .setCheck("NUMBER");
        this.appendValueInput("DURATION")
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_PLAY_DUR",1))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var freq = generator.valueToCode(this,"FREQUENCY");
        var dur = generator.valueToCode(this,"DURATION");
        var code = "abbozza.soundmotor.soundOn("+ freq + ");\n";
        code = code + "abbozza.sleep(" + dur + ");\n";
        code = code + "abbozza.soundmotor.soundOff();";
        return code;
    }
};


Abbozza.DeviceSpeakerOctaves = null;

Abbozza.DeviceSpeakerNotes = [];
Abbozza.DeviceSpeakerNames = ["C","CS","D","DS","E","F","FS","G","GS","A","AS","B"];
Abbozza.DeviceSpeakerSymbols = ["C","♯C","D","♯D","E","F","♯F","G","♯G","A","♯A","B"];
Abbozza.DeviceSpeakerFrequencies = [
    33,     35,   27,   39,   41,   44,   46,   49,   52,   55,   58,   62, 
    65,     69,   73,   78,   82,   87,   93,   98,  104,  110,  117,  123,
    131,   139,  147,  156,  165,  175,  185,  196,  208,  220,  233,  247,
    262,   277,  294,  311,  330,  349,  370,  392,  415,  440,  466,  494,
    523,   554,  587,  622,  659,  698,  740,  784,  831,  880,  932,  988,
    1047, 1109, 1175, 1245, 1319, 1397, 1480, 1568, 1661, 1760, 1865, 1976,
    2093, 2217, 2349, 2489, 2637, 2794, 2960, 3136, 3322, 3520, 3729, 3951
];

for ( var note in Abbozza.DeviceSpeakerNames ) {
    var symbol = Abbozza.DeviceSpeakerSymbols[note];
    Abbozza.DeviceSpeakerNotes.push([symbol,note]);
}

Abbozza.DeviceSpeakerNote = {
    init: function () {
        if (Abbozza.DeviceSpeakerOctaves == null ) {
            Abbozza.DeviceSpeakerOctaves = [
                [_("dev.OCTAVE_CONTRA"),"1"],
                [_("dev.OCTAVE_GREAT"),"2"],
                [_("dev.OCTAVE_SMALL"),"3"],
                [_("dev.OCTAVE_ONE_LINE"),"4"],
                [_("dev.OCTAVE_TWO_LINE"),"5"],
                [_("dev.OCTAVE_THREE_LINE"),"6"],
                [_("dev.OCTAVE_FOUR_LINE"),"7"]
            ];        
        }
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_NOTE",0))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerNotes),"NOTE");
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_NOTE",1))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerOctaves),"OCTAVE");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var note = parseInt(this.getFieldValue("NOTE"));
        var octave = parseInt(this.getFieldValue("OCTAVE"));
        var frequency = Abbozza.DeviceSpeakerFrequencies[(octave-1)*12+note];
        return "abbozza.soundmotor.soundOn(" + frequency + ");";
    }
};

Abbozza.DeviceSpeakerNoteDur = {
    init: function () {
        if (Abbozza.DeviceSpeakerOctaves == null ) {
            Abbozza.DeviceSpeakerOctaves = [
                [_("dev.OCTAVE_CONTRA"),"1"],
                [_("dev.OCTAVE_GREAT"),"2"],
                [_("dev.OCTAVE_SMALL"),"3"],
                [_("dev.OCTAVE_ONE_LINE"),"4"],
                [_("dev.OCTAVE_TWO_LINE"),"5"],
                [_("dev.OCTAVE_THREE_LINE"),"6"],
                [_("dev.OCTAVE_FOUR_LINE"),"7"]
            ];        
        }
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_NOTE_DUR",0))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerNotes),"NOTE");
        this.appendDummyInput()
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_NOTE_DUR",1))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerOctaves),"OCTAVE");
        this.appendValueInput("DURATION")
                .setAlign(Blockly.ALIGN_RIGHT)
                .appendField(__("dev.SOUND_NOTE_DUR",2))
                .setCheck("NUMBER");
        this.setInputsInline(false);
        this.setPreviousStatement(true, "STATEMENT");
        this.setNextStatement(true, "STATEMENT");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var note = parseInt(this.getFieldValue("NOTE"));
        var octave = parseInt(this.getFieldValue("OCTAVE"));
        var duration = generator.valueToCode(this,"DURATION");
        var frequency = Abbozza.DeviceSpeakerFrequencies[(octave-1)*12+note];
        var code = "abbozza.soundmotor.soundOn("+ frequency + ");\n";
        code = code + "abbozza.sleep(" + duration + ");\n";
        code = code + "abbozza.soundmotor.soundOff();";
        return code;
    }
};

Abbozza.DeviceSpeakerNoteInt = {
    init: function () {
        if (Abbozza.DeviceSpeakerOctaves == null ) {
            Abbozza.DeviceSpeakerOctaves = [
                [_("dev.OCTAVE_CONTRA"),"1"],
                [_("dev.OCTAVE_GREAT"),"2"],
                [_("dev.OCTAVE_SMALL"),"3"],
                [_("dev.OCTAVE_ONE_LINE"),"4"],
                [_("dev.OCTAVE_TWO_LINE"),"5"],
                [_("dev.OCTAVE_THREE_LINE"),"6"],
                [_("dev.OCTAVE_FOUR_LINE"),"7"]
            ];        
        }
        this.setHelpUrl(Abbozza.HELP_URL);
        this.setColour(ColorMgr.getColor("cat.SOUND"));
        this.appendDummyInput()
                .appendField(__("dev.SOUND_NOTE_INT",0))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerNotes),"NOTE")
                .appendField(__("dev.SOUND_NOTE_INT",1))
                .appendField(new Blockly.FieldDropdown(Abbozza.DeviceSpeakerOctaves),"OCTAVE");
        this.setInputsInline(true);
        this.setPreviousStatement(false);
        this.setNextStatement(false);
        this.setOutput(true,"NUMBER");
        this.setTooltip('');
    },
    generateCode: function (generator) {
        var note = parseInt(this.getFieldValue("NOTE"));
        var octave = parseInt(this.getFieldValue("OCTAVE"));
        var frequency = Abbozza.DeviceSpeakerFrequencies[(octave-1)*12+note];
        return "" + frequency;
    }
};


// Blockly.Blocks['dev_speaker'] = Abbozza.DeviceSpeaker;
Blockly.Blocks['dev_speaker_mute'] = Abbozza.DeviceSpeakerMute;
Blockly.Blocks['dev_speaker_play'] = Abbozza.DeviceSpeakerPlay;
Blockly.Blocks['dev_speaker_play_dur'] = Abbozza.DeviceSpeakerPlayDur;
Blockly.Blocks['dev_speaker_note'] = Abbozza.DeviceSpeakerNote;
Blockly.Blocks['dev_speaker_note_dur'] = Abbozza.DeviceSpeakerNoteDur;
Blockly.Blocks['dev_speaker_note_int'] = Abbozza.DeviceSpeakerNoteInt;
