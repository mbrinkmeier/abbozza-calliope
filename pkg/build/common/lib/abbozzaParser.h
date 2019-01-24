/**
 * @license 
 * abbozza!
 * 
 * File: Abbozza.js
 * 
 * A parser for the serial connection
 * 
 * Copyright 2017 Michael Brinkmeier
 * 
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 * 
 * http://www.apache.org/licenses/LICENSE-2.0
 * 
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */

#ifndef _AbbozzaParser_h
#define _AbbozzaParser_h

class AbbozzaParser {
    
public:
    AbbozzaParser(Abbozza* bit);
    void check(int tx, int rx);
    ManagedString parse_word();
    int parse_int();
    long parse_long();
    float parse_float();
    double parse_double();
    ManagedString parse_string();
    
    ManagedString getCmdId();
    ManagedString getCmd();
    void execute();    

    void sendResponse(ManagedString resp);

    ManagedString replace(ManagedString str, char from, char to);
    ManagedString toUpperCase(ManagedString str);
    ManagedString trim(ManagedString str);
    int indexOf(ManagedString haystack,char needle);
    int indexOf(ManagedString haystack,char needle, int start);
    int find(ManagedString haystack,const char * needle);
    
private:
    Abbozza* abbozza;
    void setCommand(ManagedString cmd);
    ManagedString buffer;
    ManagedString currentCommand;
    ManagedString remainder;
    ManagedString cmdId;
    ManagedString cmd;
    bool debug;
    int _tx;
    int _rx;
};

#endif
