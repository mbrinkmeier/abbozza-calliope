/**
 * @license abbozza!
 *
 * Copyright 2015 Michael Brinkmeier ( michael.brinkmeier@uni-osnabrueck.de )
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
/**
 * @fileoverview ... @author michael.brinkmeier@uni-osnabrueck.de (Michael
 * Brinkmeier)
 */
package de.uos.inf.did.abbozza.calliope;

import de.uos.inf.did.abbozza.AbbozzaLocale;
import de.uos.inf.did.abbozza.AbbozzaLogger;
import de.uos.inf.did.abbozza.AbbozzaSplashScreen;
import de.uos.inf.did.abbozza.Tools;
import de.uos.inf.did.abbozza.tools.FileTool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.zip.ZipFile;

/**
 *
 * @author mbrinkmeier1G
 */
public class AbbozzaCalliopeC extends AbbozzaCalliope {

    protected String _buildPath;
    protected String _hexPath;
    protected int _exitValue;
    
    protected String errMsg;
    protected String outMsg;
    
    protected boolean bluetooth = false;
    protected String configFile;
    
    
    public static void main(String args[]) {
        AbbozzaCalliopeC abbozza = new AbbozzaCalliopeC();
        abbozza.init("calliopeC");
        
        abbozza.startServer();
        // abbozza.startBrowser("calliope.html");        
    }

    /**
     * Initialize the abbozza! server.
     *
     * @param system A string identifying the system.
     */
    public void init(String system) {
        super.init(system);        
    }

    @Override
    public int uploadCode(String code) {

        int _exitValue = compileCode(code);

        if (_exitValue == 0) {
            FileInputStream in = null;
            try {
                AbbozzaLogger.out("Copying " + _hexPath + " to " + _pathToBoard + "/abbozza.hex", 4);
                in = new FileInputStream(_hexPath);
                PrintWriter out = new PrintWriter(_pathToBoard + "/abbozza.hex");
                while (in.available() > 0) {
                    out.write(in.read());
                }
                out.flush();
                out.close();
                in.close();
            } catch (FileNotFoundException ex) {
                AbbozzaLogger.err(ex.getLocalizedMessage());
            } catch (IOException ex) {
                AbbozzaLogger.err(ex.getLocalizedMessage());
            }
        }

        return _exitValue;
    }

    
    /**
     * Copy code to &lt;buildPath&gt;/source/abbozza.cpp and compile it.
     *
     * @param code The source code to be compiled
     * 
     * @return The error message received during compilation.
     */
    @Override
    public int compileCode(String code) {
        if (this._boardName == null ) {
            this.setBoardName("calliope");
        }
        
        configFile = "config_radio.json";
        
        // Check for #define entries
        if ( code.contains("#define ABZ_BLUETOOTH") ) {
            bluetooth = true;
            configFile = "config_bluetooth.json";
            AbbozzaLogger.info("Using Bluetooth");
        }
        if ( code.contains("#define ABZ_RADIO") ) {
            bluetooth = false;
            configFile = "config_radio.json";            
            AbbozzaLogger.info("Using Radio");
        }
        

        _buildPath = userPath + "/build/" + this._boardName + "/";
                
        if ( this._boardName.equals("microbit") ) {
            _hexPath = _buildPath + "build/bbc-microbit-classic-gcc/source/abbozza-combined.hex";
        } else {
            _hexPath = _buildPath + "build/calliope-mini-classic-gcc/source/abbozza-combined.hex";            
        }
        AbbozzaLogger.out("Build path set to " + _buildPath, AbbozzaLogger.INFO);        
        AbbozzaLogger.out("Code generated", AbbozzaLogger.INFO);
       
        // Set code in frame
        this.frame.setCode(code);

        String errMsg = "";
        String stdMsg = "";
        
        _exitValue = 1;
        
        // Copy code to <buildPath>/source/abbozza.cpp
        AbbozzaLogger.out("Writing code to " + _buildPath + "source/abbozza.cpp");
        if (!code.equals("")) {
            try {
                PrintWriter out = new PrintWriter(_buildPath + "source/abbozza.cpp");
                out.write(code);
                out.flush();
                out.close();

                _exitValue = compile(_buildPath);                                
                AbbozzaLogger.info("Exit value: " + _exitValue);
                
            } catch (FileNotFoundException ex) {
                AbbozzaLogger.err(ex.getLocalizedMessage());
            }

            if (_exitValue == 0) {
                compileErrorMsg = "";
                AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.done_compiling"));
        } else {
                AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.error_compiling"));
                compileErrorMsg = AbbozzaLocale.entry("msg.error_compiling");
            }
        }
        
        return _exitValue;
    }


    public int compile(String buildPath) {
        errMsg = "";
        outMsg = "";
        
        String osName = System.getProperty("os.name");
        
        ProcessBuilder procBuilder = null;
        
        if (osName.contains("Linux")) {
            procBuilder = buildProcLinux(buildPath);
        } else if (osName.contains("Mac")) {
            procBuilder = buildProcMac(buildPath);
        } else if (osName.contains("Windows")) {
            procBuilder = buildProcWindows(buildPath);
        }
        
        if ( procBuilder == null) return 2;
        
        AbbozzaLogger.force("[compile] : ___");
        AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.compiling"));
        
        AbbozzaLogger.out("Compiling with path " + procBuilder.environment().get("PATH"));
        
        try {
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
            Process proc = procBuilder.start();
            
            InputStream es = proc.getErrorStream();
            InputStream os = proc.getInputStream();
            
            BufferedReader error = new BufferedReader(new InputStreamReader(es));
            BufferedReader out = new BufferedReader(new InputStreamReader(os));

            String line;
            while ( proc.isAlive() ) {
                if ( error.ready() ) {
                    line = error.readLine();
                    AbbozzaLogger.force(line);
                    errMsg = errMsg + "\n" + line;
                }
                if ( out.ready() ) {
                    line = out.readLine();
                    AbbozzaLogger.force(line);
                    outMsg = outMsg + "\n" + line;
                }
            }
            
            // Read remaining output
            while  ( error.ready() ) {
                line = error.readLine();
                AbbozzaLogger.force(line);
                errMsg = errMsg + "\n" + line;
            }
            while ( out.ready() ) {
                line = out.readLine();
                AbbozzaLogger.force(line);
                outMsg = outMsg + "\n" + line;
            }
            
            return proc.exitValue();
            
        } catch (Exception ex) {
            AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.error_compiling"));
            AbbozzaLogger.err(ex.getLocalizedMessage());
        }
        
        return 1;
    }
    
    
    public ProcessBuilder buildProcLinux(String buildPath) {        

        ProcessBuilder procBuilder = new ProcessBuilder("yt","-n","--config",configFile,"build");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }
        
        return procBuilder;
    }
    
    
    public ProcessBuilder buildProcMac(String buildPath) {
        ProcessBuilder procBuilder = new ProcessBuilder("yt","-n","--config",configFile,"build");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }
        
        return procBuilder;
    }

    
    public ProcessBuilder buildProcWindows(String buildPath) {
        String yottaPath = System.getenv("YOTTA_PATH");
        String yottaInstall = System.getenv("YOTTA_INSTALL_LOCATION");

        ProcessBuilder procBuilder = new ProcessBuilder("cmd","/C","yt","-n","--config",configFile,"build");
        procBuilder.directory(new File(buildPath));
        
        procBuilder.environment().put("PATH",  yottaPath + ";" + abbozzaPath + "\\lib\\srecord\\" + ";" + yottaInstall+"\\workspace\\Scripts\\" + ";" + System.getenv("PATH"));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ";" + path);
        }
                
        return procBuilder;
    }
    
    
    public int cleanBuildSystem() {

        if (this._boardName == null ) {
            this.setBoardName("calliope");
        }
        
        String buildPath = userPath + "/build/" + this._boardName + "/";

        
        errMsg = "";
        outMsg = "";
        
        String osName = System.getProperty("os.name");
        
        ProcessBuilder procBuilder = null;
        
        if (osName.contains("Linux")) {
            procBuilder = cleanProcLinux(buildPath);
        } else if (osName.contains("Mac")) {
            procBuilder = cleanProcMac(buildPath);
        } else if (osName.contains("Windows")) {
            procBuilder = cleanProcWindows(buildPath);
        }
        
        if ( procBuilder == null) return 2;
        
        AbbozzaLogger.force("[compile] : ___");
        AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.cleaning_buildsystem") + " " + buildPath);
        
        try {
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
            Process proc = procBuilder.start();
            
            InputStream es = proc.getErrorStream();
            InputStream os = proc.getInputStream();
            
            BufferedReader error = new BufferedReader(new InputStreamReader(es));
            BufferedReader out = new BufferedReader(new InputStreamReader(os));

            String line;
            while ( proc.isAlive() ) {
                if ( error.ready() ) {
                    line = error.readLine();
                    AbbozzaLogger.force(line);
                    errMsg = errMsg + "\n" + line;
                }
                if ( out.ready() ) {
                    line = out.readLine();
                    AbbozzaLogger.force(line);
                    outMsg = outMsg + "\n" + line;
                }
            }
            
            // Read remaining output
            while  ( error.ready() ) {
                line = error.readLine();
                AbbozzaLogger.force(line);
                errMsg = errMsg + "\n" + line;
            }
            while ( out.ready() ) {
                line = out.readLine();
                AbbozzaLogger.force(line);
                outMsg = outMsg + "\n" + line;
            }
            
            return proc.exitValue();
            
        } catch (IOException ex) {
        }
        
        return 1;
    }
    
    
            
    public ProcessBuilder cleanProcLinux(String buildPath) {        
        ProcessBuilder procBuilder = new ProcessBuilder("yt","-n","clean");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }
        
        return procBuilder;
    }
    
    
    public ProcessBuilder cleanProcMac(String buildPath) {
        ProcessBuilder procBuilder = new ProcessBuilder("yt","-n","clean");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }
        
        return procBuilder;
    }

    
    public ProcessBuilder cleanProcWindows(String buildPath) {
        String yottaPath = System.getenv("YOTTA_PATH");
        String yottaInstall = System.getenv("YOTTA_INSTALL_LOCATION");

        ProcessBuilder procBuilder = new ProcessBuilder("cmd","/C","yt","-n","clean");
        procBuilder.directory(new File(buildPath));
        
        procBuilder.environment().put("PATH",  yottaPath + ";" + abbozzaPath + "\\lib\\srecord\\" + ";" + yottaInstall+"\\workspace\\Scripts\\" + ";" + System.getenv("PATH"));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ";" + path);
        }
        
        AbbozzaLogger.out(procBuilder.environment().get("PATH"));
        
        return procBuilder;
    }
    

    public void additionalInitialization() {
        
        AbbozzaSplashScreen.setText("Updating build directory ...");
        
        // Check if build system has to be initialized
        boolean initBuild = false;
        // Check if <userPath>/build/ exists
        File buildDir = new File(userPath + "/build/");
        if ( !buildDir.exists() ) {
            AbbozzaLogger.err("Build directory " + userPath + "/build/ doesn't exist.");
            buildDir.mkdirs();
            initBuild = true;
        } else {
            File initFile = new File(userPath + "/build/abz_init");
            if ( initFile.exists() ) {
                AbbozzaLogger.out("Initialization of build directory " + userPath + "/build required.");
                initFile.delete();
                buildDir.delete();
                initBuild = true;
            }
        }
        
        AbbozzaLogger.out("Checking build directory " + buildDir.getAbsolutePath() + " ...");
            
        File original = new File(abbozzaPath + "/build/");
        try {
            // InstallTool.getInstallTool().copyDirFromJar(new JarFile(jarPath + "/abbozza-calliope.jar"), "build/", buildDir.getAbsolutePath()+"/");
            if ( initBuild ) {
                AbbozzaLogger.out("Initializing buildsystem from " + original.getAbsolutePath());
            } else {
                AbbozzaLogger.out("Updating buildsystem from " + original.getAbsolutePath());
            }
            // Extract buildbase.jar
            File buildbasefile = new File(abbozzaPath + "/lib/buildbase.jar");
            ZipFile buildbase = new ZipFile(buildbasefile);
            if ( (buildDir.lastModified() < buildbasefile.lastModified()) || (initBuild) ) {
                // Extract buildbase.jar if newer or initialization required
                FileTool.extractJar(buildbase,buildDir);
            }
            
            // Delete source diretories
            FileTool.removeDirectory(new File(buildDir,"calliope/source"));
            FileTool.removeDirectory(new File(buildDir,"microbit/source"));  
            FileTool.copyDirectory(original, buildDir,!initBuild);
            
        } catch (IOException ex) {
            AbbozzaLogger.err("[FATAL] " + ex.getLocalizedMessage());
            System.exit(1);
        }            
            
        AbbozzaSplashScreen.setText("");
    }
    
}
