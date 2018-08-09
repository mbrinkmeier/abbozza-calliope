/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uos.inf.did.abbozza.calliope;

import de.uos.inf.did.abbozza.core.AbbozzaLocale;
import de.uos.inf.did.abbozza.install.AbbozzaLoggingFrame;
import de.uos.inf.did.abbozza.install.InstallTool;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.List;
import java.util.Properties;
import java.util.jar.JarFile;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.SwingWorker;
import javax.swing.text.BadLocationException;
import javax.swing.text.Document;

/**
 *
 * @author michael
 */
public class InstallWorker extends SwingWorker<String, String> {

    private InstallTool installTool;
    private File userInstallDir;
    private String sketchbookPath;
    private String browserPath;
    private JarFile installerJar;
    private Document msgDoc;
    private boolean globalInstall;
    private AbbozzaLoggingFrame logFrame;

    public InstallWorker(AbbozzaLoggingFrame frame, JarFile jar, InstallTool tool, String userDir, String sketchbook, String browser, Document doc, boolean global) {
        userInstallDir = userInstallDir = new File(installTool.expandPath(userDir));
        installerJar = jar;
        installTool = tool;
        sketchbookPath = sketchbook;
        browserPath = browser;
        msgDoc = doc;
        globalInstall = global;
        logFrame = frame;
    }

    /**
     *
     * @param msgs The published nessages
     */
    @Override
    protected void process(List<String> msgs) {
        for (String msg : msgs) {
            try {
                if (msg.equals("|")) {
                    String t = msgDoc.getText(msgDoc.getLength() - 1, 1);
                    msgDoc.remove(msgDoc.getLength() - 1, 1);
                    char c = t.charAt(0);
                    switch (c) {
                        case '|':
                            c = '/';
                            break;
                        case '/':
                            c = '-';
                            break;
                        case '-':
                            c = '\\';
                            break;
                        case '\\':
                            c = '|';
                            break;
                    }
                    msgDoc.insertString(msgDoc.getLength(), "" + c, null);
                } else {
                    msgDoc.insertString(msgDoc.getLength(), msg + "\n", null);
                }
            } catch (BadLocationException ex) {
            }
        }
    }

    /**
     *
     * @throws Exception thrown is exection occurred during execution
     */
    @Override
    protected String doInBackground() throws Exception {

        Document msgDoc = null;
        publish("\n\n\n" + AbbozzaLocale.entry("MSG.STARTING_INSTALLATION"));

        /**
         * 0th step: Fetch the directories
         *
         * installDir : The directory to which abbozza! is installed, expand
         * %HOME% sketchbookDir: The default directory for sketches userDir:
         * $HOME/.abbozza/ browserFile: The executable of the browser to be used
         *
         */
        // File userInstallDir = new File(installTool.expandPath(installField.getText()));
        // String sketchbookPath = sketchbookField.getText();
        // String browserPath = browserField.getText();
        File userDir = new File(System.getProperty("user.home") + "/.abbozza");

        // Adapt the install dir, depending on the system
        File installDir = installTool.adaptUserInstallDir(userInstallDir);

        /**
         * 1st step: Check if yotta is installed
         */
        // publish(AbbozzaLocale.entry("MSG.CHECKING_PREREQUISITES"));
        // boolean yottaInstalled = checkPrerequisites();
        boolean yottaInstalled = true;

        /**
         * 2nd step: Detect the jar from which installation is done
         */
        if (installerJar == null) {
            // System.err.println("Could not detect installer jar!");
            throw new Exception("Could not detect installer jar!");
        }

        /**
         * 3rd step: Create directories
         */
        if (!createDir(installDir.getAbsolutePath(), msgDoc)) {
            throw new Exception("Could not create " + installDir.getAbsolutePath());
        }

        // Do NOT create user or sketchbook dir. 
        // This is done during the first start
        // if (!createDir(userDir.getAbsolutePath(),msgDoc)) return;
        // if (!createDir(sketchbookDir.getAbsolutePath(),msgDoc)) return;
        /**
         * 4th step: Install subdirs lib and build
         */
        if (!createDir(installDir.getAbsolutePath() + "/lib/", msgDoc)) {
            throw new Exception("Could not create " + installDir.getAbsolutePath() + "/lib/");
        }
        if (!createDir(installDir.getAbsolutePath() + "/plugins/", msgDoc)) {
            throw new Exception("Could not create " + installDir.getAbsolutePath() + "/plugins/");
        }
        if (!createDir(installDir.getAbsolutePath() + "/bin/", msgDoc)) {
            throw new Exception("Could not create " + installDir.getAbsolutePath() + "/bin/");
        }
        if (!createDir(installDir.getAbsolutePath() + "/build/", msgDoc)) {
            throw new Exception("Could not create " + installDir.getAbsolutePath() + "/build/");
        }
        // Do NOT create directories in user dir.

        /**
         * 5th step: Backup previous version
         */
        File targetFile = new File(installDir.getAbsolutePath() + "/lib/abbozza-calliope.jar");
        File backup = new File(installDir.getAbsolutePath() + "/lib/abbozza-calliope_" + System.currentTimeMillis() + ".jar_");
        if (targetFile.exists()) {
            publish(AbbozzaLocale.entry("MSG.BACKUP", backup.getAbsolutePath()));
            try {
                Files.move(targetFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ex) {
                int opt = JOptionPane.showConfirmDialog(null,
                        AbbozzaLocale.entry("ERR.CANNOT_BACKUP") + "\n"
                        + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
                        AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
                if (opt == JOptionPane.NO_OPTION) {
                    throw new Exception();
                }
            }
        }

        /**
         * 6th step: Copy installer jar to abbozza dir
         */
        try {
            targetFile.createNewFile();
            publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza-calliope.jar"));
            installTool.copyFromJar(installerJar, "lib/abbozza-calliope.jar", installDir + "/lib/abbozza-calliope.jar");
        } catch (IOException ex) {
            throw new Exception(AbbozzaLocale.entry("ERR.CANNOT_WRITE", targetFile.getAbsolutePath()));
        }

        /**
         * 7th step: copy jars and script from installerJar to their locations
         */
        // installTool.copyDirFromJar(installerJar, "lib/srecord/", installDir + "/lib/srecord/");
        // buildbase
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/buildbase.jar"));
        installTool.copyFromJar(installerJar, "lib/buildbase.jar", installDir + "/lib/buildbase.jar");

        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/jssc-2.8.0.jar"));
        installTool.copyFromJar(installerJar, "lib/jssc-2.8.0.jar", installDir + "/lib/jssc-2.8.0.jar");
        installTool.copyFromJar(installerJar, "lib/license_jssc.txt", installDir + "/lib/license_jssc.txt");

        // apache commons-io
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/commons-io-2.5.jar"));
        installTool.copyFromJar(installerJar, "lib/commons-io-2.5.jar", installDir + "/lib/commons-io-2.5.jar");
        installTool.copyFromJar(installerJar, "lib/license_commons-io.txt", installDir + "/lib/license_commons-io.txt");

        // rsyntaxarea
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/rsyntaxtextarea.jar"));
        installTool.copyFromJar(installerJar, "lib/rsyntaxtextarea.jar", installDir + "/lib/rsyntaxtextarea.jar");
        installTool.copyFromJar(installerJar, "lib/license_rsyntaxtextarea.txt", installDir + "/lib/license_rsyntaxtextarea.txt");

        // autocomplete
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/autocomplete.jar"));
        installTool.copyFromJar(installerJar, "lib/autocomplete.jar", installDir + "/lib/autocomplete.jar");
        installTool.copyFromJar(installerJar, "lib/license_autocomplete.txt", installDir + "/lib/license_autocomplete.txt");

        // srecord
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/srecord/"));
        installTool.copyDirFromJar(installerJar, "lib/srecord/", installDir + "/lib/srecord/");

        // The build system
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/build/"));
        installTool.copyDirFromJar(installerJar, "build/", installDir + "/build/", true);

        // String osname = System.getProperty("os.name").toLowerCase();
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/tools.zip"));

        ZipFile zip = null;

        if (!installTool.copyFromJar(installerJar, "tools.zip", installDir + "/tools.zip")) {
            publish("... tools.zip not found in " + installerJar.getName());
            publish("... Checking if tools.zip exists in " + installDir);
        }
        
        File zipFile = new File(installDir + "/tools.zip");
        if (zipFile.exists()) {
            publish("... Extracting " + installDir + "/tools.zip");

            zip = new ZipFile(installDir + "/tools.zip");

            if (zip != null) {
                int progress = 0;
                int progressMax = zip.size();
                Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();

                while (entries.hasMoreElements()) {
                    ZipEntry entry = entries.nextElement();
                    String name = entry.getName();
                    progress++;

                    // publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/" + name));
                    File target = new File(installDir + "/" + name);
                    setProgress(progress * 100 / progressMax);

                    // publish(AbbozzaLocale.entry("MSG.WRITING", name));
                    // publish("|");
                    if (entry.isDirectory()) {
                        target.mkdir();
                    } else {
                        Files.copy(zip.getInputStream(entry), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
                    }
                }
                publish(AbbozzaLocale.entry("MSG.SET_PERMISSIONS"));
                BufferedReader execs = new BufferedReader(new FileReader(installDir + "/tools/.executables"));
                while (execs.ready()) {
                    String exe = execs.readLine();
                    File exef = new File(installDir + "/" + exe);
                    exef.setExecutable(true);
                }
            }
        } else {
            publish("... No tools.zip in " + installDir + ". Checking for " +installDir + "/tools ..");
            File toolsDir = new File(installDir + "/tools");
            if (!toolsDir.exists()) {
                publish("Tools not installed!");
            }
        }
        
        // Scripts
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/bin/abbozzaC.[sh|bat]"));
        installTool.copyFromJar(installerJar, "bin/abbozzaC.sh", installDir + "/bin/abbozzaC.sh");
        installTool.copyFromJar(installerJar, "bin/abbozzaC.bat", installDir + "/bin/abbozzaC.bat");
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/bin/abbozzaMonitor.[sh|bat]"));
        installTool.copyFromJar(installerJar, "bin/abbozzaMonitor.sh", installDir + "/bin/abbozzaMonitor.sh");
        installTool.copyFromJar(installerJar, "bin/abbozzaMonitor.bat", installDir + "/bin/abbozzaMonitor.bat");

        // Icons
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza_icon_white"));
        installTool.copyFromJar(installerJar, "lib/abbozza_icon_white.png", installDir + "/lib/abbozza_icon_white.png");
        installTool.copyFromJar(installerJar, "lib/abbozza_icon_white.ico", installDir + "/lib/abbozza_icon_white.ico");
        publish(AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza_icon_monitor"));
        installTool.copyFromJar(installerJar, "lib/abbozza_icon_monitor.png", installDir + "/lib/abbozza_icon_monitor.png");
        installTool.copyFromJar(installerJar, "lib/abbozza_icon_monitor.ico", installDir + "/lib/abbozza_icon_monitor.ico");
        // Do not copy the build template to the users dir! Wait for compilation!
        // publish(AbbozzaLocale.entry("MSG.WRITING",userDir + "/calliopeC/build/"));
        // installTool.copyDirFromJar(installerJar, "build/", userDir + "/calliopeC/build/");
        /**
         * 8th step: Add application to menus
         */
        publish(AbbozzaLocale.entry("MSG.ADDING_MENU"));
        String scriptSuffix = installTool.getScriptSuffix();
        String iconSuffix = installTool.getIconSuffix();

        if (installTool.getSystem()
                .equals("Mac")) {
            // Create Contents/MacOS
            String installDir2 = userInstallDir.getAbsolutePath() + "/Contents/MacOS/";
            String installDir3 = userInstallDir.getAbsolutePath() + "/Contents/";
            createDir(installDir2, msgDoc);
            installTool.copyFromJar(installerJar, "lib/abbozza.icns", installDir.getAbsolutePath() + "/abbozza.icns");
            installTool.copyFromJar(installerJar, "lib/Info.plist", installDir3 + "/Info.plist");
            createFile(installDir2 + "abbozza", msgDoc);
            File starter = new File(installDir2 + "abbozza");
            FileWriter writer;
            try {
                writer = new FileWriter(starter);
                writer.append("#!/bin/bash\n\n");
                writer.append("CWD=" + installDir.getAbsolutePath() + "\n");
                writer.append("YOTTA_PATH=\"$CWD/prerequisites:$CWD/prerequisites/gcc-arm-none-eabi-4_9-2015q3/bin:$CWD/prerequisites/CMake.app/Contents/bin:$CWD/workspace/bin\"\n");
                writer.append("export PATH=$YOTTA_PATH:$PATH\n");
                writer.append("export YOTTA_CWD=\"$CWD\"\n");
                writer.append("cd " + installDir.getAbsolutePath() + "/bin\n");
                writer.append("./abbozzaC.sh");
                starter.setExecutable(true);
                writer.close();
            } catch (IOException ex) {
                publish("Could not create " + starter.getAbsolutePath());
            }

            installDir2 = userInstallDir.getParent() + "/abbozzaMonitor.app/Contents/MacOS/";
            installDir3 = userInstallDir.getParent() + "/abbozzaMonitor.app/Contents/";
            createDir(installDir2, msgDoc);
            createDir(installDir3 + "Resources", msgDoc);
            installTool.copyFromJar(installerJar, "lib/abbozza_monitor.icns", installDir3 + "Resources/abbozza_monitor.icns");
            installTool.copyFromJar(installerJar, "lib/Info.monitor.plist", installDir3 + "/Info.plist");
            createFile(installDir2 + "abbozzaMonitor", msgDoc);
            starter = new File(installDir2 + "abbozzaMonitor");
            try {
                writer = new FileWriter(starter);
                writer.append("#!/bin/bash\n\n");
                writer.append("cd " + installDir.getAbsolutePath() + "/bin\n");
                writer.append("./abbozzaMonitor.sh");
                starter.setExecutable(true);
                writer.close();
            } catch (IOException ex) {
                publish("Could not create " + starter.getAbsolutePath());
            }

        } else {
            installTool.addAppToMenu("abbozzaCalliopeC", "abbozza! Calliope C",
                    "abbozza! Calliope C",
                    installDir + "/bin/abbozzaC" + scriptSuffix, installDir + "/lib/abbozza_icon_white" + iconSuffix, globalInstall);

            // installTool.addAppToMenu("abbozzaCalliopeMicroPython", "abbozza! Calliope MicroPython",
            //     "abbozza! Calliope MicroPython",
            //     installDir + "/bin/abbozzaMicroPython" + scriptSuffix, installDir + "/lib/abbozza_icon_white" + iconSuffix, globalInstall);
            installTool.addAppToMenu("abbozzaMonitor", "abbozza! Monitor",
                    "abbozza! Monitor",
                    installDir + "/bin/abbozzaMonitor" + scriptSuffix, installDir + "/lib/abbozza_icon_monitor" + iconSuffix, globalInstall);
        }
        /**
         * Write configuration file
         */
        Properties config = new Properties();

        config.setProperty(
                "freshInstall", "true");
        config.setProperty(
                "browserPath", browserPath);
        config.setProperty(
                "installPath", userInstallDir.getAbsolutePath());
        config.setProperty(
                "sketchbookPath", sketchbookPath);

        // Do NOT write config file to user dir. Instead write template to lib
        // File prefFile = new File(userDir.getAbsolutePath() + "/calliopeMP/abbozza.cfg");
        File prefFile = new File(installDir.getAbsolutePath() + "/lib/calliopeMP.cfg");

        try {
            prefFile.getParentFile().mkdirs();
            prefFile.createNewFile();

            config.store(new FileOutputStream(prefFile), "abbozza! preferences");
            publish(AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
        } catch (IOException ex) {
            publish(AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
            throw new Exception();
        }

        prefFile = new File(installDir.getAbsolutePath() + "/lib/calliopeC.cfg");

        try {
            prefFile.getParentFile().mkdirs();
            prefFile.createNewFile();

            config.store(new FileOutputStream(prefFile), "abbozza! preferences");
            publish(AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
        } catch (IOException ex) {
            publish(AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
            throw new Exception();
        }

        /**
         * 9th step: first compile for microbit and calliope
         */
        boolean microbitCompiled = false;
        boolean calliopeCompiled = false;

        /*
        if (yottaInstalled) {
            int opt = JOptionPane.showConfirmDialog(frame,
                    AbbozzaLocale.entry("MSG.PRECOMPILE", "Calliope Mini"),
                    AbbozzaLocale.entry("GUI.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                publish(AbbozzaLocale.entry("MSG.COMPILE_CALLIOPE"));
                if (build(installDir.getAbsolutePath() + "/build/calliope/", installDir.getAbsolutePath(), msgDoc) != 0) {
                    publish(AbbozzaLocale.entry("MSG.COMPILE_FAILED"));
                } else {
                    publish(AbbozzaLocale.entry("MSG.COMPILE_SUCCESS"));
                    calliopeCompiled = true;
                }
            }

            opt = JOptionPane.showConfirmDialog(frame,
                    AbbozzaLocale.entry("MSG.PRECOMPILE", "micro:bit"),
                    AbbozzaLocale.entry("GUI.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                publish(AbbozzaLocale.entry("MSG.COMPILE_MICROBIT"));
                if (build(installDir.getAbsolutePath() + "/build/microbit/", installDir.getAbsolutePath(), msgDoc) != 0) {
                    publish(AbbozzaLocale.entry("MSG.COMPILE_FAILED"));
                } else {
                    publish(AbbozzaLocale.entry("MSG.COMPILE_SUCCESS"));
                    microbitCompiled = true;
                }
            }
        }
         */
        publish(AbbozzaLocale.entry("MSG.SUCCESS"));

        logFrame.enableButton();

        return "";
    }

    private boolean createDir(String path, Document msgDoc) {
        File buildDir = new File(path);
        publish(AbbozzaLocale.entry("MSG.CREATING_DIR", path));
        buildDir.mkdirs();
        if (!buildDir.canWrite()) {
            publish(AbbozzaLocale.entry("ERR.CANNOT_WRITE", path));
            return false;
        }
        return true;
    }

    private boolean createFile(String path, Document msgDoc) {
        try {
            File file = new File(path);
            file.delete();
            file.createNewFile();
            return true;
        } catch (IOException ex) {
            publish(AbbozzaLocale.entry("ERR.CANNOT_WRITE", path));
            return false;
        }
    }

}
