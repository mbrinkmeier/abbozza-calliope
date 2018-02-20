/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package de.uos.inf.did.abbozza.calliope;

import com.sun.net.httpserver.HttpHandler;
import de.uos.inf.did.abbozza.core.AbbozzaLocale;
import de.uos.inf.did.abbozza.core.AbbozzaLogger;
import de.uos.inf.did.abbozza.core.AbbozzaServer;
import de.uos.inf.did.abbozza.core.AbbozzaSplashScreen;
import de.uos.inf.did.abbozza.calliope.handler.BoardChooserPanel;
import de.uos.inf.did.abbozza.calliope.handler.BoardHandler;
import de.uos.inf.did.abbozza.handler.JarDirHandler;
import de.uos.inf.did.abbozza.handler.SerialHandler;
import java.awt.AWTException;
import java.awt.MenuItem;
import java.awt.PopupMenu;
import java.awt.SystemTray;
import java.awt.TrayIcon;
import java.awt.event.ActionEvent;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.math.BigInteger;
import java.net.URL;
import java.net.URLConnection;
import java.nio.ByteBuffer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.filechooser.FileFilter;
import javax.swing.filechooser.FileSystemView;



/**
 *
 * @author mbrinkmeier
 */
public abstract class AbbozzaCalliope extends AbbozzaServer implements HttpHandler {
    
    public static final int SYS_MAJOR = 0;
    public static final int SYS_MINOR = 12;
    public static final int SYS_REV = 0;
    public static final int SYS_HOTFIX = 0;
    public static final String SYS_REMARK = "(calliope)";
    public static final String SYS_VERSION = SYS_MAJOR + "." + SYS_MINOR + "." + SYS_REV + "." + SYS_HOTFIX + " " + SYS_REMARK;
    
    
    // Additional paths
    // protected String installPath;   // The path into which abbozza was installed
    protected String toolsPath;     // The path to tools needed for compilation

    protected int _SCRIPT_ADDR = 0x3e000;
    protected String _pathToBoard = "";
    protected AbbozzaCalliopeFrame frame;
    protected TrayIcon trayIcon;

    public static int VER_REV = 1;      
    public static int VER_HOTFIX = 0;   
    public static String VER_REM = "(calliope)";  

    public void init(String system) {
        
        AbbozzaSplashScreen.showSplashScreen("de/uos/inf/did/abbozza/calliope/icons/abbozza-calliope-splash.png");
        super.init(system);

 
        /**
         * This does not seem to work
         * 
        // Add ClassLoaders for jars in <runtimePath>/lib
        File libDir = new File(runtimePath + "/lib");
        File[] jars = libDir.listFiles( new FilenameFilter() {
            public boolean accept(File dir, String name) {
                return name.endsWith(".jar");
            }
        });
        URL[] urls = new URL[jars.length];
        for ( int i = 0; i < jars.length; i++ ) {
            try {
                urls[i] = jars[i].toURI().toURL();
                AbbozzaLogger.info("Added " + urls[i].toExternalForm() + " to classpath");
            } catch (MalformedURLException ex) {
                AbbozzaLogger.err(ex.getLocalizedMessage());
            }
        }
        ClassLoader loader = new URLClassLoader(urls,AbbozzaCalliope.class.getClassLoader());   
        */
        
        setPathToBoard(this.config.getOptionStr("pathToBoard"));
        // Open Frame
        frame = new AbbozzaCalliopeFrame();
        frame.open();
        mainFrame = frame;
        
        startServer();

        try {
            if (SystemTray.isSupported()) {
                AbbozzaLogger.info("Setting system tray icon");
                ImageIcon icon = new ImageIcon(AbbozzaCalliopeC.class.getResource("/img/abbozza_icon_white.png"));
                PopupMenu trayMenu = new PopupMenu();
                MenuItem item = new MenuItem(AbbozzaLocale.entry("gui.startBrowser"));
                item.addActionListener((ActionEvent e) -> {
                    startBrowser(system + ".html");
                });
                trayMenu.add(item);
                item = new MenuItem(AbbozzaLocale.entry("gui.showFrame"));
                item.addActionListener((ActionEvent e) -> {
                    bringFrameToFront();
                });
                trayMenu.add(item);
                item = new MenuItem(AbbozzaLocale.entry("gui.quit"));
                item.addActionListener((ActionEvent e) -> {
                    quit();
                });
                trayMenu.add(item);
                trayIcon = new TrayIcon(icon.getImage(),"abbozza!",trayMenu);
                trayIcon.setImageAutoSize(true);
                SystemTray.getSystemTray().add(trayIcon);
            }
        } catch (AWTException e) {
            AbbozzaLogger.err(e.getLocalizedMessage());
        }

        startBrowser(system + ".html");
        
        AbbozzaSplashScreen.hideSplashScreen();
    }

    /**
     * Set the standard paths.
     */
    public void setPaths() {
        super.setPaths();
        localJarPath = jarPath;
        globalJarPath = jarPath;
        sketchbookPath = "/sketches";
        localPluginPath = userPath + "/plugins";
        globalPluginPath = abbozzaPath + "/plugins"; // installPath + "/tools/Abbozza/plugins";
    }

    /**
     * Set additional paths.
     */
    public void setAdditionalPaths() {
        // installPath = config.getProperty("installPath");
        sketchbookPath = expandPath(config.getProperty("sketchbookPath"));
        toolsPath = expandPath(config.getProperty("toolsPath"));
        
        AbbozzaLogger.info("jarPath = " + jarPath);
        AbbozzaLogger.info("runtimePath = " + abbozzaPath);
        AbbozzaLogger.info("toolsPath = " + toolsPath);
        AbbozzaLogger.info("userPath = " + userPath);
        AbbozzaLogger.info("sketchbookPath = " + sketchbookPath);
        AbbozzaLogger.info("localJarPath = " + localJarPath);
        AbbozzaLogger.info("localPluginPath = " + localPluginPath);
        AbbozzaLogger.info("globalPluginPath = " + globalPluginPath);
        AbbozzaLogger.info("browserPath = " + config.getBrowserPath());
    }

    /**
     * Find the jars and dirs.
     * 
     * @param jarHandler The jarHandler to be used.
     */
    public void findJarsAndDirs(JarDirHandler jarHandler) {
        jarHandler.clear();
        jarHandler.addDir(jarPath + "/", "Dir");
        jarHandler.addJar(jarPath + "/abbozza-calliope.jar", "Jar");
    }

    /**
     * Set the path at which the board should be found.
     * 
     * @param path The path
     */
    public void setPathToBoard(String path) {
        _pathToBoard = path;
        if (_pathToBoard != null) {
            this.config.setOptionStr("pathToBoard", _pathToBoard);
        }
        AbbozzaLogger.out("Path to board set to " + path, 4);
    }

    /**
     * Returns teh current path to the board.
     * 
     * @return The path to the board
     */
    public String getPathToBoard() {
        return _pathToBoard;
    }
    
    /**
     * Register system specific handlers.
     */
    @Override
    public void registerSystemHandlers() {
        AbbozzaLogger.info("Registering handlers for board and queryboard");
        httpServer.createContext("/abbozza/board", new BoardHandler(this, false));
        httpServer.createContext("/abbozza/queryboard", new BoardHandler(this, true));
        httpServer.createContext("/abbozza/serial", new SerialHandler(this));
    }

    /**
     * @deprecated ???
     */
    @Override
    public void toolToBack() {
    }

    /**
     * @deprecated ???
     * 
     * @param code The code to set in the tool
     */
    @Override
    public void toolSetCode(String code) {
    }

    /**
     * @deprecated ???
     */
    @Override
    public void toolIconify() {
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    /**
     * Copile the code.
     * 
     * @param code The code to be compiled.
     * @return 
     */
    @Override
    public int compileCode(String code) {
        AbbozzaLogger.out("Code generated", 4);
        this.frame.setCode(code);
        return 0;
    }

    /**
     * Upload the code
     * 
     * @param code The code to be uploaded
     * @return 
     */
    @Override
    public int uploadCode(String code) {
        this.frame.setCode(code);
        String hex = embed(hexlify(code));
        AbbozzaLogger.out("Writing hex code to " + _pathToBoard + "/abbozza.hex", 4);
        if (hex != "") {
            try {
                PrintWriter out = new PrintWriter(_pathToBoard + "/abbozza.hex");
                out.write(hex);
                out.flush();
                out.close();
            } catch (FileNotFoundException ex) {
                Logger.getLogger(AbbozzaCalliope.class.getName()).log(Level.SEVERE, null, ex);
            }
        } else {
        }
        return 0;
        // throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    protected String hexlify(String code) {
        if (code == null || code == "") {
            return "";
        }
        // Correct the line ends from MacOS and Windows is neccessary
        code.replace("\r\n", "\n");
        code.replace("\r", "\n");
        ByteBuffer bytes = ByteBuffer.allocate(2);
        bytes.putShort((short) code.length());
        bytes.put(0, (byte) (code.length() % 256));
        bytes.put(1, (byte) ((code.length() & 0x0000ff00) / 256));
        String len = String.format("%x", new BigInteger(1, bytes.array())).toUpperCase();
        while (len.length() < 4) {
            len = "0" + len;
        }
        String data = "MP##" + code;
        // padding
        while (data.length() % 16 != 0) {
            data = data + ((char) 0);
        }
        // @TODO check length of code
        String output = ":020000040003F7";
        int addr = _SCRIPT_ADDR;
        String chunk = "";
        String hexline = "";
        int checksum;
        bytes = ByteBuffer.allocate(4);
        for (int chunkPos = 0; chunkPos < data.length(); chunkPos = chunkPos + 16) {
            chunk = data.substring(chunkPos, chunkPos + 16 < data.length() ? chunkPos + 16 : data.length());
            bytes.clear();
            bytes.put(0, (byte) (chunk.length() % 256));
            bytes.putShort(1, (short) addr);
            bytes.put(3, (byte) 0);
            byte[] ch = chunk.getBytes();
            if (chunkPos == 0) {
                ch[2] = (byte) (code.length() % 256);
                ch[3] = (byte) ((code.length() & 0x0000ff00) / 256);
                // hexline = hexline.replaceFirst("4D502323","4D50" + len);
            }
            String second = String.format("%x", new BigInteger(1, ch)).toUpperCase();
            while (second.length() < 32) {
                second = "0" + second;
            }
            hexline = String.format("%x", new BigInteger(1, bytes.array())).toUpperCase() + second; // String.format("%x", new BigInteger(1, chunk.getBytes())).toUpperCase();
            checksum = 0;
            byte[] by = bytes.array();
            for (int i = 0; i < by.length; i++) {
                checksum += by[i];
            }
            for (int i = 0; i < ch.length; i++) {
                checksum += ch[i];
            }
            byte[] by2 = new byte[1];
            by2[0] = (byte) ((-checksum) & 0xff);
            String check = String.format("%x", new BigInteger(1, by2)).toUpperCase();
            while (check.length() < 2) {
                check = "0" + check;
            }
            hexline = ":" + hexline + check;
            output = output + "\n" + hexline;
            addr += 16;
        }
        output = output.replace("####", len);
        return output;
    }

    protected String embed(String hexcode) {
        // the embedded hexcode should be inserted before the last two lines
        // of the runtime code.
        String runtime = "";
        try {
            runtime = new String(this.jarHandler.getBytes("/js/abbozza/calliopeMP/runtimes/calliope.hex"));
        } catch (Exception ex) {
            return "";
        }
        runtime = runtime.replace("######", hexcode);
        return runtime;
    }
    
    
    /**
     * This method tries to detect a connected CalliopeMINI or the micro:bit
     *
     * @return The path to the board as string or an empty string
     */
    public String findBoard() {
        String os = System.getProperty("os.name").toLowerCase();
        if (os.contains("win")) {
            // In windows system the drives are scanned for their volume name
            File[] roots = File.listRoots();
            for (int i = 0; i < roots.length; i++) {
                try {
                    String volume = FileSystemView.getFileSystemView().getSystemDisplayName(roots[i]);
                    if (volume.contains("MINI") || volume.contains("MICROBIT")) {
                        AbbozzaLogger.info("Board found at " + roots[i].getCanonicalPath());
                        return roots[i].getCanonicalPath();
                    }
                } catch (IOException ex) {
                    Logger.getLogger(BoardHandler.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
            return "";
        } else if (os.contains("linux") || os.contains("mac")) {
            try {
                // In posix systems (linux and Mac OsX) the system command 'mount' 
                // is used to detect the volume
                Process process = Runtime.getRuntime().exec("mount");
                process.waitFor();
                InputStreamReader reader = new InputStreamReader(process.getInputStream());
                BufferedReader volumes = new BufferedReader(reader);
                String volume;
                while (volumes.ready()) {
                    volume = volumes.readLine();
                    if (volume.contains("MINI") || volume.contains("MICROBIT")) {
                        volume = volume.split(" ")[2];
                        AbbozzaLogger.info("[BoardHandler.findBoard] Board found at " + volume);
                        if (volume.contains("MINI")) {
                            setBoardName("calliope");
                        } else {
                            setBoardName("microbit");
                        }
                        return volume;
                    }
                }
                volumes.close();
                AbbozzaLogger.debug("No board found");
            } catch (Exception ex) {
                AbbozzaLogger.err(ex.getMessage());
                return "";
            }
        } else {
            // Currently no other system is supported
            AbbozzaLogger.err("Operating system " + os + " not supported");
        }
        return "";
    }


    public File queryPathToBoard(String path) {
        File selectedDir = null;
        JFileChooser chooser = new JFileChooser();
        if (path != null) {
            chooser.setCurrentDirectory(new File(path));
        }
        chooser.setDialogTitle(AbbozzaLocale.entry("gui.CalliopePath"));
        BoardChooserPanel boardPanel = new BoardChooserPanel();
        chooser.setAccessory(boardPanel);
        if ((getBoardName()!=null) && (getBoardName().equals("microbit"))) {
            boardPanel.setMicrobit();
        } else {
            boardPanel.setCalliope();
        }
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        chooser.setFileFilter(new FileFilter() {
            @Override
            public boolean accept(File f) {
                return f.isDirectory();
            }

            @Override
            public String getDescription() {
                return "Select readable directory";
            }
        });
        
        bringFrameToFront();
        setDialogOpen(true);

        if (chooser.showOpenDialog(null) == JFileChooser.APPROVE_OPTION) {
            selectedDir = chooser.getSelectedFile();
            if (boardPanel.isCalliope()) {
                setBoardName("calliope");
            } else {
                setBoardName("microbit");
            }
        } else {
        }

        setDialogOpen(false);
        resetFrame();
        toolIconify();
        
        return selectedDir;
    }

    public TrayIcon getTrayIcon() {
        return trayIcon;
    }
    
    protected void quit() {
        System.exit(0);
    }
    
    public String getSystemVersion() {
        return SYS_VERSION;
    };

    
        public void installUpdate(String version, String updateUrl) {
        try {
            // 1st step: Rename current jar
            // Find current jar
            URL curUrl = AbbozzaServer.class.getProtectionDomain().getCodeSource().getLocation();
            File cur = new File(curUrl.toURI());
            AbbozzaLogger.out("Current jar found at " + cur.getAbsolutePath(), AbbozzaLogger.INFO);
            SimpleDateFormat format = new SimpleDateFormat("yyyyMMdd");
            String today = format.format(new Date());
            File dir = new File(cur.getParentFile().getAbsolutePath());
            if (!dir.exists()) {
                AbbozzaLogger.out("Creating directory " + dir.getPath(), AbbozzaLogger.INFO);
                dir.mkdir();
            }
            AbbozzaLogger.out("Moving old version to " + dir.getPath() + "/abbozza." + today + ".jar", AbbozzaLogger.INFO);
            cur.renameTo(new File(dir.getPath() + "/abbozza." + today + ".jar"));
            
            // 2nd step: Download new version
            AbbozzaLogger.out("Downloading version " + version, AbbozzaLogger.INFO);
            URL url = new URL(updateUrl + "abbozza-calliope.jar");
            URLConnection conn = url.openConnection();
            byte buffer[] = new byte[4096];
            int n = -1;
            InputStream ir = conn.getInputStream();
            FileOutputStream ow = new FileOutputStream(new File(curUrl.toURI()));
            while ((n = ir.read(buffer)) != -1) {
                ow.write(buffer, 0, n);
            }
            ow.close();
            ir.close();
            AbbozzaLogger.out("Stopping abbozza!", AbbozzaLogger.INFO);
            System.exit(0);
        } catch (Exception ex) {
            Logger.getLogger(AbbozzaServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

        
    /**
     * @deprecated ???
     * @return 
     */
    public String getPluginInstallPath() {
        return userPath + "/build/" + this._boardName + "/source/lib/";        
    }

}
