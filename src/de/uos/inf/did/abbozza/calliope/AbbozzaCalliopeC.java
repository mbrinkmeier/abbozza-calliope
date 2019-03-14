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
 * @fileoverview The Abbozza Server for calliope C
 *
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael
 * Brinkmeier)
 */
package de.uos.inf.did.abbozza.calliope;

import de.uos.inf.did.abbozza.core.AbbozzaConfigDialog;
import de.uos.inf.did.abbozza.core.AbbozzaLocale;
import de.uos.inf.did.abbozza.core.AbbozzaLogger;
import de.uos.inf.did.abbozza.core.AbbozzaServer;
import de.uos.inf.did.abbozza.core.AbbozzaSplashScreen;
import de.uos.inf.did.abbozza.plugin.PluginConfigPanel;
import de.uos.inf.did.abbozza.tools.FileTool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.io.UnsupportedEncodingException;
import java.net.URLEncoder;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipFile;

/**
 * This class implements all system specific operations for tha Calliope Mini.
 *
 * @author mbrinkmeier
 */
public class AbbozzaCalliopeC extends AbbozzaCalliope {

    protected String _buildPath;
    protected String _hexPath;
    protected int _exitValue;
    protected String _cmdOptBuildBase = null; // the build base given by the command options
    protected boolean _cmdOptInitBuildBase = false; // the build base given by the command options

    protected boolean bluetooth = false;
    protected String configFile;

    /**
     * The main programm initilizes the server.
     *
     * @param args The command line args
     */
    public static void main(String args[]) {
        AbbozzaCalliopeC abbozza = new AbbozzaCalliopeC();
        abbozza.init("calliopeC", args);
    }

    /**
     * Initialize the abbozza! server.
     *
     * @param system The system identifier
     */
    @Override
    public void init(String system) {
        init(system, null);
    }

    /**
     * Initialize the server using the command line arguments.
     *
     * @param system The system identifier
     * @param args The command line arguments
     */
    public void init(String system, String args[]) {
        super.init(system, args);
    }

    /**
     * Upload the code to the calliope/micro:bit
     *
     * @param code The code to be compiled and uploaded
     * @return A return code ( 0 for success )
     */
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
        if (this._boardName == null) {
            this.setBoardName("calliope");
        }

        bluetooth = false;
        configFile = "config_radio.json";

        // Check for #define entries
        if (code.contains("#define ABZ_BLUETOOTH")) {
            bluetooth = true;
            configFile = "config_bluetooth.json";
            AbbozzaLogger.info("Using Bluetooth");
        }
        if (code.contains("#define ABZ_RADIO")) {
            bluetooth = false;
            configFile = "config_radio.json";
            AbbozzaLogger.info("Using Radio");
        }

        _buildPath = buildPath + "/" + this._boardName + "/"; // userPath + "/build/" + this._boardName + "/";

        if (this._boardName.equals("microbit")) {
            // _hexPath = _buildPath + "build/bbc-microbit-classic-gcc/source/abbozza-combined.hex";
            _hexPath = _buildPath + "build/abbozza-combined.hex";
        } else {
            // _hexPath = _buildPath + "build/calliope-mini-classic-gcc/source/abbozza-combined.hex";
            _hexPath = _buildPath + "build/abbozza-combined.hex";
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

    /**
     * Do the actual compilation.
     *
     * @param buildPath The path in which the binary should be build
     * @return 0 if no error occured
     */
    public int compile(String buildPath) {
        String errMsg = "";
        String outMsg = "";

        String osName = System.getProperty("os.name");

        ProcessBuilder procBuilder = null;
        
        String _buildPath;
        
        try {
            _buildPath = URLEncoder.encode(buildPath,"IOS8859-1");
        } catch (UnsupportedEncodingException ex) {
            AbbozzaLogger.err("AbbozzaCalliopeC : Encoding in UTF8 failed");
            _buildPath = buildPath;
        }
        
        if (osName.contains("Linux")) {
            procBuilder = buildProcLinux(_buildPath);
        } else if (osName.contains("Mac")) {
            procBuilder = buildProcMac(_buildPath);
        } else if (osName.contains("Windows")) {
            procBuilder = buildProcWindows(_buildPath);
        }

        if (procBuilder == null) {
            return 2;
        }

        AbbozzaLogger.force("[compile] : ___");
        AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.compiling"));

        if (osName.contains("Windows")) {
            AbbozzaLogger.out("Compiling with path: " + procBuilder.environment().get("Path"));
        } else {
            AbbozzaLogger.out("Compiling with path: " + procBuilder.environment().get("PATH"));
        }

        String pluginSourcePath = abbozzaPath + "/build/" + this._boardName + "/source/lib/";
        String pluginTargetPath = _buildPath + "source/lib/";
        try {
            // Copy files from <abbozzaPath>/build/<system>/source/lib (plugin libraries)
            // to <buildPath>/<system>/source/lib
            AbbozzaLogger.force("[compile] : ___ Copying plugin libraries from " + pluginSourcePath + " to " + pluginTargetPath);
            FileTool.copyDirectory(new File(pluginSourcePath), new File(pluginTargetPath), false);
        } catch (IOException ex) {
            AbbozzaLogger.err("Could not copy files from " + pluginSourcePath + " to " + pluginTargetPath);
        }

        try {
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
            Process proc = procBuilder.start();

            InputStream es = proc.getErrorStream();
            InputStream os = proc.getInputStream();

            BufferedReader error = new BufferedReader(new InputStreamReader(es));
            BufferedReader out = new BufferedReader(new InputStreamReader(os));

            String line;
            while (proc.isAlive()) {
                if (error.ready()) {
                    line = error.readLine();
                    AbbozzaLogger.force("[compile] : " + line);
                    errMsg = errMsg + "\n" + line;
                }
                if (out.ready()) {
                    line = out.readLine();
                    AbbozzaLogger.force("[compile] : " + line);
                    outMsg = outMsg + "\n" + line;
                }
            }

            // Read remaining output
            while (error.ready()) {
                line = error.readLine();
                AbbozzaLogger.force("[compile] : " + line);
                errMsg = errMsg + "\n" + line;
            }
            while (out.ready()) {
                line = out.readLine();
                AbbozzaLogger.force("[compile] : " + line);
                outMsg = outMsg + "\n" + line;
            }

            return proc.exitValue();

        } catch (Exception ex) {
            AbbozzaLogger.force("[compile] : " + AbbozzaLocale.entry("msg.error_compiling"));
            AbbozzaLogger.err(ex.getLocalizedMessage());
        }

        return 1;
    }

    /**
     * Compilation on linux systems
     *
     * @param buildPath The path in which the binary should be build
     * @return 0 if no error occured
     */
    public ProcessBuilder buildProcLinux(String buildPath) {

        String ble = "BLUETOOTH=0";
        if (bluetooth) {
            ble="BLUETOOTH=1";
        }
        // ProcessBuilder procBuilder = new ProcessBuilder("yt", "-n", "--config", configFile, "build");
        // ProcessBuilder procBuilder = new ProcessBuilder(toolsDir + "/build.sh", configFile);
        ProcessBuilder procBuilder = new ProcessBuilder("sh","-c","make",ble);
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }

        return procBuilder;
    }

    /**
     * Compilation for macos systems.
     *
     * @param buildPath The path in which the code should be compiled
     * @return 0 if no error occured
     */
    public ProcessBuilder buildProcMac(String buildPath) {
        String ble = "BLUETOOTH=0";
        if (bluetooth) {
            ble="BLUETOOTH=1";
        }
        // ProcessBuilder procBuilder = new ProcessBuilder("yt", "-n", "--config", configFile, "build");
        // ProcessBuilder procBuilder = new ProcessBuilder(toolsDir + "/build.sh", configFile);
        ProcessBuilder procBuilder = new ProcessBuilder("sh","-c","make", ble);
        
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":/usr/local/bin:" + path);
        } else {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", "/usr/local/bin:" + path);
        }

        AbbozzaLogger.info("SYSTEM PATH: " + System.clearProperty("PATH"));
        AbbozzaLogger.info("BUILDER PATH: " + procBuilder.environment().get("PATH"));

        return procBuilder;
    }

    /**
     * Compilation on Windows systems
     *
     * @param buildPath The path in which the code should be compiled
     * @return 0 if no error occured
     */
    public ProcessBuilder buildProcWindows(String buildPath) {
        // String yottaPath = System.getenv("YOTTA_PATH");
        // String yottaInstall = System.getenv("YOTTA_INSTALL_LOCATION");
        // if (yottaPath == null) {
        //     yottaPath = "";
        // }
        // if (yottaInstall == null) {
        //    yottaInstall = "";
        // }

        String ble = "BLUETOOTH=0";
        if (bluetooth) {
            ble="BLUETOOTH=1";
        }
        // ProcessBuilder procBuilder = new ProcessBuilder("cmd", "/C", "yt", "-n", "--config", configFile, "build");
        ProcessBuilder procBuilder = new ProcessBuilder("cmd", "/C", "make", ble);
        // ProcessBuilder procBuilder = new ProcessBuilder("cmd","/C",toolsDir + "/build.bat");
        procBuilder.directory(new File(buildPath));

        // procBuilder.environment().put("PATH",  yottaPath + ";" + abbozzaPath + "\\lib\\srecord\\" + ";" + yottaInstall + "\\workspace\\Scripts\\" + ";" + System.getenv("PATH"));
        procBuilder.environment().put("Path", System.getenv("Path"));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("Path");
            procBuilder.environment().put("Path", toolsPath + ";" + path);
        }

        return procBuilder;
    }

    /**
     * Clean the build system.
     *
     * @return The exit state of the cleaning process (0 for success)
     */
    public int cleanBuildSystem() {

        if (this._boardName == null) {
            this.setBoardName("calliope");
        }

        String _buildPath = buildPath + "/" + this._boardName + "/";

        String errMsg = "";
        String outMsg = "";

        String osName = System.getProperty("os.name");

        ProcessBuilder procBuilder = null;

        if (osName.contains("Linux")) {
            procBuilder = cleanProcLinux(_buildPath);
        } else if (osName.contains("Mac")) {
            procBuilder = cleanProcMac(_buildPath);
        } else if (osName.contains("Windows")) {
            procBuilder = cleanProcWindows(_buildPath);
        }

        if (procBuilder == null) {
            return 2;
        }

        AbbozzaLogger.force("[clean] : ___");
        AbbozzaLogger.force("[clean] : " + AbbozzaLocale.entry("msg.cleaning_buildsystem") + " " + _buildPath);

        try {
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            procBuilder.redirectError(ProcessBuilder.Redirect.PIPE);
            Process proc = procBuilder.start();

            InputStream es = proc.getErrorStream();
            InputStream os = proc.getInputStream();

            BufferedReader error = new BufferedReader(new InputStreamReader(es));
            BufferedReader out = new BufferedReader(new InputStreamReader(os));

            String line;
            while (proc.isAlive()) {
                if (error.ready()) {
                    line = error.readLine();
                    AbbozzaLogger.force(line);
                    errMsg = errMsg + "\n" + line;
                }
                if (out.ready()) {
                    line = out.readLine();
                    AbbozzaLogger.force(line);
                    outMsg = outMsg + "\n" + line;
                }
            }

            // Read remaining output
            while (error.ready()) {
                line = error.readLine();
                AbbozzaLogger.force(line);
                errMsg = errMsg + "\n" + line;
            }
            while (out.ready()) {
                line = out.readLine();
                AbbozzaLogger.force(line);
                outMsg = outMsg + "\n" + line;
            }

            AbbozzaLogger.force("[clean] : Cleaning of buildsystem finished");

            return proc.exitValue();

        } catch (IOException ex) {
        }

        return 1;
    }

    /**
     * Execute yt clean on Linux based systems.
     *
     * @param buildPath The path in which the code should be compiled
     * @return 0 if no error occured
     */
    public ProcessBuilder cleanProcLinux(String buildPath) {
        // ProcessBuilder procBuilder = new ProcessBuilder(toolsDir + "/clean.sh");
        ProcessBuilder procBuilder = new ProcessBuilder("make","clean");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }

        return procBuilder;
    }

    
    /**
     * Execute yt clean on Mac OS.
     *
     * @param buildPath The path in which the code should be compiled
     * @return 0 if no error occured
     */
    public ProcessBuilder cleanProcMac(String buildPath) {
        // ProcessBuilder procBuilder = new ProcessBuilder(toolsDir + "/clean.sh");
        ProcessBuilder procBuilder = new ProcessBuilder("make","clean");
        procBuilder.directory(new File(buildPath));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("PATH");
            procBuilder.environment().put("PATH", toolsPath + ":" + path);
        }

        return procBuilder;
    }

    
    /**
     * Execute yt clean on Windows.
     *
     * @param buildPath The path in which the code should be compiled
     * @return 0 if no error occured
     */
    public ProcessBuilder cleanProcWindows(String buildPath) {

        // ProcessBuilder procBuilder = new ProcessBuilder("cmd", "/C", "yt", "-n", "clean");
        ProcessBuilder procBuilder = new ProcessBuilder("cmd", "/C", "make", "clean");
        procBuilder.directory(new File(buildPath));

        // procBuilder.environment().put("PATH",  yottaPath + ";" + abbozzaPath + "\\lib\\srecord\\" + ";" + yottaInstall+"\\workspace\\Scripts\\" + ";" + System.getenv("PATH"));
        procBuilder.environment().put("Path", System.getenv("Path"));

        if (toolsPath != null) {
            String path = procBuilder.environment().get("Path");
            procBuilder.environment().put("Path", toolsPath + ";" + path);
        }

        return procBuilder;
    }

    
    /**
     * Do some additonal initialization: 
     * 1) Update/initialize from &lt;buildinit&gt;/lib/buildbase.jar 
     * 2) Copy sources from &lt;buildinit&gt;/build
     * 
     * &lt;buildinit&gt; is &lt;abbozzapath&gt;.
     * It may be overriden by the command line option -B
     */
    @Override
    public void additionalInitialization() {

        AbbozzaSplashScreen.setText("Updating build directory.  This may take a while!");

        // Check if build system has to be initialized
        boolean initBuild = this._cmdOptInitBuildBase;

        // Check installed version
        String installedVersion = this.config.getProperty("version");
        if ( !this.isNewerThan(installedVersion) ) {
            initBuild = true;
            AbbozzaLogger.info("Installed version (" + installedVersion + ") is older than available version (" + this.getSystemVersion() + ")");
        }
        
        // Set path to buildbase.jar
        String buildbaseJarPath = AbbozzaServer.getConfig().getProperty("buildbase");

        // Determine the <buildinit> path
        if (this._cmdOptBuildBase != null) {
            buildbaseJarPath = this._cmdOptBuildBase;
        } else if (buildbaseJarPath == null) {
            // Default path
            buildbaseJarPath = abbozzaPath + "/lib/buildbase.jar";
        }
        AbbozzaLogger.info("Using " + buildbaseJarPath + " for initialization of build system");

        // Check if buildPath exists
        File buildDir = new File(buildPath);
        AbbozzaLogger.out("Checking build directory " + buildDir.getAbsolutePath() + " ...");

        if (!buildDir.exists()) {
            // Create the directory
            AbbozzaLogger.err("Build directory " + buildPath + " doesn't exist.");
            buildDir.mkdirs();
            initBuild = true;
        } else {
            // Check for init file in build directory
            File initFile = new File(buildPath + "/abz_init");
            if (initFile.exists()) {
                AbbozzaLogger.out("Initialization of build directory " + buildPath + " required.");
                initFile.delete();
                buildDir.delete();
                initBuild = true;
            }
        }

        // The source files
        // File original = new File(buildInitPath + "/build/");
        try {
            if (initBuild) {
                AbbozzaLogger.out("Initializing buildsystem from " + buildbaseJarPath);
            } else {
                AbbozzaLogger.out("Updating buildsystem from " + buildbaseJarPath);
            }

            // Extract <abbozzapath>/lib/buildbase.jar
            File buildbasefile = new File(buildbaseJarPath);
            if (!buildbasefile.exists()) {
                AbbozzaLogger.err("Could not find buildbase " + buildbaseJarPath);
                buildbasefile = null;
            } else {
                if ((buildDir.lastModified() < buildbasefile.lastModified()) || (initBuild)) {
                    AbbozzaSplashScreen.setText("Initializing build system. This may take a while!");
                    AbbozzaLogger.out("Copying " + buildbasefile.getAbsolutePath() + " to " + buildDir);
                    FileTool.copyDirectory(buildbasefile, new File(buildDir,"/buildbase.jar"), true);
                    buildbasefile = new File(buildDir + "/buildbase.jar");
                    AbbozzaLogger.out("Extracting " + buildbasefile.getAbsolutePath());
                    // Extract buildbase.jar if newer or initialization required
                    ZipFile buildbase = new ZipFile(buildbasefile);
                    FileTool.extractJar(buildbase, buildDir);
                    this.cleanBuildSystem();
                    this.config.setProperty("version",this.getSystemVersion());
                } else {
                    AbbozzaSplashScreen.setText("Build system up to date!");
                }
            }

        } catch (IOException ex) {
            AbbozzaLogger.err("[FATAL] " + ex.getLocalizedMessage());
            System.exit(1);
        }

        AbbozzaSplashScreen.setText("");
    }

    
    /**
     *
     * @param stream The stream from which the plugin is read
     * @param name The name of the plugin
     * @return a flag inidicating if installation was successfull
     */
    @Override
    public boolean installPluginFile(InputStream stream, String name) {
        try {
            File targetDir = new File(abbozzaPath + "/build/calliope/source/lib/");
            if ( !targetDir.exists()) {
                Files.createDirectories(targetDir.toPath());
            }
        } catch (IOException ex) {
            AbbozzaLogger.err(abbozzaPath + "/build/calliope/source/lib/ couldn't be created");
        }
        
        try {
            File targetDir = new File(abbozzaPath + "/build/microbit/source/lib/");
            if ( !targetDir.exists()) {
                Files.createDirectories(targetDir.toPath());
            }
        } catch (IOException ex) {
            AbbozzaLogger.err(abbozzaPath + "/build/microbit/source/lib/ couldn't be created");
        }
        
        File target = new File(abbozzaPath + "/build/calliope/source/lib/" + name);
        try {
            AbbozzaLogger.info("Copying " + name + " to " + target.toString());
            Files.copy(stream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            AbbozzaLogger.err("Could not copy " + name + " to " + target.toString());
            AbbozzaLogger.err(ex.getLocalizedMessage());
            return false;
        }
        target = new File(abbozzaPath + "/build/microbit/source/lib/" + name);
        try {
            AbbozzaLogger.info("Copying " + name + " to " + target.toString());
            Files.copy(stream, target.toPath(), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            AbbozzaLogger.err("Could not copy " + name + " to " + target.toString());
            AbbozzaLogger.err(ex.getLocalizedMessage());
            return false;
        }
        return true;
    }
    
    

    public void adaptConfigDialog(AbbozzaConfigDialog dialog) {
        dialog.addPanel(new PluginConfigPanel());
    }

    /**
     * Apply a command line option This adds -B buildbase to the list of
     * possible options. It explicitly sets the buildbase to the given path.
     *
     * @param option The option to be applied
     * @param par A parameter for the option
     */
    protected void applyCommandlineOption(String option, String par) {
        if (option.equals("-B")) {
            this._cmdOptBuildBase = par;
            AbbozzaLogger.info("Using path " + this._cmdOptBuildBase + " as base for build initialization");
        } else {
            super.applyCommandlineOption(option, par);
        }
    }

}
