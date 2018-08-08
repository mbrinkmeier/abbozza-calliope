/**
 * @license abbozza!
 *
 * Copyright 2017 Michael Brinkmeier ( michael.brinkmeier@uni-osnabrueck.de )
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the Licenseo. You may obtain a copy
 * of the License at
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
 * @fileoverview The main class for the abbozza! Calliope installer
 * @author michael.brinkmeier@uni-osnabrueck.de (Michael Brinkmeier)
 */
package de.uos.inf.did.abbozza.calliope;

import de.uos.inf.did.abbozza.core.AbbozzaLocale;
import de.uos.inf.did.abbozza.install.AbbozzaLoggingFrame;
import de.uos.inf.did.abbozza.install.InstallTool;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.file.Files;
import java.nio.file.StandardCopyOption;
import java.util.Enumeration;
import java.util.Properties;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.Caret;
import javax.swing.text.DefaultCaret;
import javax.swing.text.Document;

/**
 * The Calliope Installer frame.
 */
public class AbbozzaCalliopeInstaller extends javax.swing.JFrame {

    public Properties prefs;
    private InstallTool installTool;
    private boolean isAdmin;
    private JarFile installerJar;
    private boolean globalInstall = false;

    /**
     * Creates new form AbbozzaInstaller
     *
     * @param global This flag indicates whether abbozza! should be installed
     * globally
     */
    public AbbozzaCalliopeInstaller(boolean global) {
        // Get the correct install tool
        installTool = InstallTool.getInstallTool();
        isAdmin = installTool.isAdministrator();
        globalInstall = global & isAdmin;

        File installerFile = installTool.getInstallerJar();
        if (installerFile == null) {
            System.exit(1);
        }
        try {
            installerJar = new JarFile(installerFile);
            AbbozzaLocale.setLocale(System.getProperty("user.language"), installerJar, "languages/");
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            System.exit(1);
        }

        // Ask for global install if administrator
        if (isAdmin && !globalInstall) {
            int result = JOptionPane.showConfirmDialog(null, AbbozzaLocale.entry("GUI.ASK_FOR_GLOBAL_INSTALL"), "abbozza! Calliope", JOptionPane.YES_NO_CANCEL_OPTION);
            if (result == JOptionPane.CANCEL_OPTION) {
                System.exit(0);
            } else if (result == JOptionPane.NO_OPTION) {
                globalInstall = true;
            } else {
                globalInstall = false;
            }
        }

        initComponents();

        String osname = System.getProperty("os.name").toLowerCase();
        if (osname.contains("mac")) {
            // OsX only requires the command 'open'
            browserField.setText("open");
            browserField.setEnabled(true);
            browserButton.setEnabled(true);
        }

        // Change default settings if local install
        if (!globalInstall) {
            this.sketchbookField.setText(System.getProperty("user.home") + "/abbozza");
        }

        this.setTitle(AbbozzaLocale.entry("GUI.TITLE"));

        InstallTool.centerWindow(this);

        JRootPane rootPane = SwingUtilities.getRootPane(installButton);
        rootPane.setDefaultButton(installButton);
        installButton.requestFocusInWindow();

        String abbozzaDir = System.getProperty("user.home") + "/.abbozza/calliopeC";
        File aD = new File(abbozzaDir);

        if (aD.exists()) {
            int result = JOptionPane.showConfirmDialog(this,
                    AbbozzaLocale.entry("MSG.ALREADY_INSTALLED") + "\n"
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION") + "\n" + "( Version " + AbbozzaCalliope.SYS_VERSION + " )",
                    AbbozzaLocale.entry("GUI.TITLE"), JOptionPane.YES_NO_OPTION);
            if (result == JOptionPane.NO_OPTION) {
                System.exit(1);
            }
            File prefFile = new File(abbozzaDir + "/abbozza.cfg");
            Properties config = new Properties();
            try {
                config.load(new FileInputStream(prefFile));
                browserField.setText(config.getProperty("browserPath"));
            } catch (Exception e) {
            }
        }
    }

    /**
     * Standardconstructor for the installer frame
     */
    public AbbozzaCalliopeInstaller() {
        this(false);
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {
        java.awt.GridBagConstraints gridBagConstraints;

        logoPanel = new javax.swing.JPanel();
        jLabel1 = new javax.swing.JLabel();
        jLabel5 = new javax.swing.JLabel();
        jLabel4 = new javax.swing.JLabel();
        mainPanel = new javax.swing.JPanel();
        jScrollPane1 = new javax.swing.JScrollPane();
        msgPanel = new javax.swing.JTextPane();
        installField = new javax.swing.JTextField();
        jLabel2 = new javax.swing.JLabel();
        installDirButton = new javax.swing.JButton();
        jLabel3 = new javax.swing.JLabel();
        browserField = new javax.swing.JTextField();
        browserButton = new javax.swing.JButton();
        installField1 = new javax.swing.JTextField();
        jLabel6 = new javax.swing.JLabel();
        sketchbookField = new javax.swing.JTextField();
        sketchbookButton = new javax.swing.JButton();
        buttonPanel = new javax.swing.JPanel();
        cancelButton = new javax.swing.JButton();
        installButton = new javax.swing.JButton();

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setResizable(false);

        logoPanel.setLayout(new java.awt.BorderLayout());

        jLabel1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/img/abbozza200.png"))); // NOI18N
        jLabel1.setToolTipText("abbozza! logo");
        logoPanel.add(jLabel1, java.awt.BorderLayout.LINE_START);

        jLabel5.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/img/calliope_logo_small.png"))); // NOI18N
        logoPanel.add(jLabel5, java.awt.BorderLayout.EAST);

        jLabel4.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        jLabel4.setHorizontalAlignment(javax.swing.SwingConstants.RIGHT);
        jLabel4.setText(AbbozzaCalliope.SYS_VERSION);
        jLabel4.setVerticalAlignment(javax.swing.SwingConstants.TOP);
        jLabel4.setHorizontalTextPosition(javax.swing.SwingConstants.RIGHT);
        jLabel4.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        logoPanel.add(jLabel4, java.awt.BorderLayout.PAGE_START);

        getContentPane().add(logoPanel, java.awt.BorderLayout.PAGE_START);
        logoPanel.getAccessibleContext().setAccessibleName("logoPanel");
        logoPanel.getAccessibleContext().setAccessibleDescription("");

        mainPanel.setBorder(javax.swing.BorderFactory.createEmptyBorder(5, 5, 15, 5));
        java.awt.GridBagLayout mainPanelLayout = new java.awt.GridBagLayout();
        mainPanelLayout.columnWidths = new int[] {430, 50};
        mainPanelLayout.rowHeights = new int[] {180, 15, 30, 30, 30, 30, 30, 30};
        mainPanel.setLayout(mainPanelLayout);

        msgPanel.setEditable(false);
        msgPanel.setFont(new java.awt.Font("Dialog", 0, 10)); // NOI18N
        msgPanel.setText(AbbozzaLocale.entry("GUI.MESSAGE")
        );
        msgPanel.setFocusable(false);
        jScrollPane1.setViewportView(msgPanel);

        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 0;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jScrollPane1, gridBagConstraints);

        installField.setText(installTool.getInstallPath(globalInstall));
        installField.setToolTipText("Das Sketchbook-Verzeichnis");
        installField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(installField, gridBagConstraints);

        jLabel2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel2.setText(AbbozzaLocale.entry("GUI.INSTALL_DIR"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 2;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        mainPanel.add(jLabel2, gridBagConstraints);
        jLabel2.getAccessibleContext().setAccessibleName("Das Zielverzeichnis für die Installation:");

        installDirButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/img/directory24.png"))); // NOI18N
        installDirButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installDirButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(installDirButton, gridBagConstraints);

        jLabel3.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel3.setText(AbbozzaLocale.entry("GUI.BROWSER")
        );
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 6;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(jLabel3, gridBagConstraints);
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(browserField, gridBagConstraints);

        browserButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/img/directory24.png"))); // NOI18N
        browserButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                browserButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 1;
        gridBagConstraints.gridy = 7;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(browserButton, gridBagConstraints);

        installField1.setText(System.getProperty("user.home")+"/abbozza");
        installField1.setToolTipText("Das Sketchbook-Verzeichnis");
        installField1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installField1ActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 3;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(installField1, gridBagConstraints);

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText(AbbozzaLocale.entry("GUI.SKETCHBOOK_DIR"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        mainPanel.add(jLabel6, gridBagConstraints);

        sketchbookField.setText("%HOME%/abbozza");
        sketchbookField.setToolTipText("Das Sketchbook-Verzeichnis");
        sketchbookField.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sketchbookFieldActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(sketchbookField, gridBagConstraints);

        sketchbookButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/img/directory24.png"))); // NOI18N
        sketchbookButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                sketchbookButtonActionPerformed(evt);
            }
        });
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridy = 5;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        mainPanel.add(sketchbookButton, gridBagConstraints);

        getContentPane().add(mainPanel, java.awt.BorderLayout.CENTER);

        buttonPanel.setLayout(new java.awt.FlowLayout(java.awt.FlowLayout.RIGHT));

        cancelButton.setText(AbbozzaLocale.entry("GUI.CANCEL"));
        cancelButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                cancelButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(cancelButton);

        installButton.setText(AbbozzaLocale.entry("GUI.INSTALL"));
        installButton.setSelected(true);
        installButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                installButtonActionPerformed(evt);
            }
        });
        buttonPanel.add(installButton);

        getContentPane().add(buttonPanel, java.awt.BorderLayout.SOUTH);

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void cancelButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_cancelButtonActionPerformed
        System.exit(0);
    }//GEN-LAST:event_cancelButtonActionPerformed

    private void installButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installButtonActionPerformed

        Runnable runner;
        Document msgDoc = msgPanel.getDocument();

        JFrame frame = this;

        frame.setVisible(false);

        final AbbozzaLoggingFrame logFrame = new AbbozzaLoggingFrame();

        logFrame.setDocument(msgDoc);
        logFrame.setVisible(true);

        InstallWorker worker = new InstallWorker(
            logFrame,
            installerJar,
            installTool,
            installField.getText(),
            sketchbookField.getText(),
            browserField.getText(),
            msgDoc,
            globalInstall
        );
        
        JProgressBar progressBar = logFrame.getProgressBar();
        
        worker.addPropertyChangeListener( new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                if ("progress".equals(evt.getPropertyName())) {
                 progressBar.setValue((Integer)evt.getNewValue());
             }
            }
        });
        worker.execute();
       
        
//        runner = new Runnable() {
//            @Override
//            public void run() {
//
//        
//                /* 
//        SwingUtilities.invokeLater(new Runnable() {
//            public void run() {
//                logFrame.setDocument(msgDoc);
//                logFrame.setVisible(true);
//            }
//        });
//                 */
//                // Thread loggerThread = new Thread(logFrame);
//                // loggerThread.start();
//                addMsg(msgDoc, "\n\n\n" + AbbozzaLocale.entry("MSG.STARTING_INSTALLATION"));
//
//                /**
//                 * 0th step: Fetch the directories
//                 *
//                 * installDir : The directory to which abbozza! is installed,
//                 * expand %HOME% sketchbookDir: The default directory for
//                 * sketches userDir: $HOME/.abbozza/ browserFile: The executable
//                 * of the browser to be used
//                 *
//                 */
//                File userInstallDir = new File(installTool.expandPath(installField.getText()));
//                String sketchbookPath = sketchbookField.getText();
//                String browserPath = browserField.getText();
//                File userDir = new File(System.getProperty("user.home") + "/.abbozza");
//
//                // Adapt the install dir, depending on the system
//                File installDir = installTool.adaptUserInstallDir(userInstallDir);
//
//                /**
//                 * 1st step: Check if yotta is installed
//                 */
//                // addMsg(msgDoc, AbbozzaLocale.entry("MSG.CHECKING_PREREQUISITES"));
//                // boolean yottaInstalled = checkPrerequisites();
//                boolean yottaInstalled = true;
//
//                /**
//                 * 2nd step: Detect the jar from which installation is done
//                 */
//                if (installerJar == null) {
//                    System.err.println("Could not detect installer jar!");
//                    System.exit(1);
//                }
//
//                /**
//                 * 3rd step: Create directories
//                 */
//                if (!createDir(installDir.getAbsolutePath(), msgDoc)) {
//                    return;
//                }
//
//                // Do NOT create user or sketchbook dir. 
//                // This is done during the first start
//                // if (!createDir(userDir.getAbsolutePath(),msgDoc)) return;
//                // if (!createDir(sketchbookDir.getAbsolutePath(),msgDoc)) return;
//                /**
//                 * 4th step: Install subdirs lib and build
//                 */
//                if (!createDir(installDir.getAbsolutePath() + "/lib/", msgDoc)) {
//                    return;
//                }
//                if (!createDir(installDir.getAbsolutePath() + "/plugins/", msgDoc)) {
//                    return;
//                }
//                if (!createDir(installDir.getAbsolutePath() + "/bin/", msgDoc)) {
//                    return;
//                }
//                if (!createDir(installDir.getAbsolutePath() + "/build/", msgDoc)) {
//                    return;    // used as template for users
//                }
//                // Do NOT create directories in user dir.
//
//                /**
//                 * 5th step: Backup previous version
//                 */
//                File targetFile = new File(installDir.getAbsolutePath() + "/lib/abbozza-calliope.jar");
//                File backup = new File(installDir.getAbsolutePath() + "/lib/abbozza-calliope_" + System.currentTimeMillis() + ".jar_");
//                if (targetFile.exists()) {
//                    addMsg(msgDoc, AbbozzaLocale.entry("MSG.BACKUP", backup.getAbsolutePath()));
//                    try {
//                        Files.move(targetFile.toPath(), backup.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                    } catch (IOException ex) {
//                        int opt = JOptionPane.showConfirmDialog(frame,
//                                AbbozzaLocale.entry("ERR.CANNOT_BACKUP") + "\n"
//                                + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
//                                AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
//                        if (opt == JOptionPane.NO_OPTION) {
//                            frame.setVisible(false);
//                            System.exit(1);
//                        }
//                    }
//                }
//
//                /**
//                 * 6th step: Copy installer jar to abbozza dir
//                 */
//                try {
//                    targetFile.createNewFile();
//                    addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza-calliope.jar"));
//                    // runner = new Runnable() {
//                    //     @Override
//                    //     public void run() {
//                    installTool.copyFromJar(installerJar, "lib/abbozza-calliope.jar", installDir + "/lib/abbozza-calliope.jar");
//                    //     }
//                    // };
//                    // runner.run();
//                    // Files.copy(installerFile.toPath(), targetFile.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(frame,
//                            AbbozzaLocale.entry("ERR.CANNOT_WRITE", targetFile.getAbsolutePath()),
//                            AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.ERROR_MESSAGE);
//                    return;
//                }
//
//                /**
//                 * 7th step: copy jars and script from installerJar to their
//                 * locations
//                 */
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyDirFromJar(installerJar, "lib/srecord/", installDir + "/lib/srecord/");
//                //     }
//                // };
//                // runner.run();
//
//                // buildbase
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/buildbase.jar"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyFromJar(installerJar, "lib/buildbase.jar", installDir + "/lib/buildbase.jar");
//                //    }
//                // };
//                // runner.run();
//
//                // jssc-2.8.0
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/jssc-2.8.0.jar"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyFromJar(installerJar, "lib/jssc-2.8.0.jar", installDir + "/lib/jssc-2.8.0.jar");
//                installTool.copyFromJar(installerJar, "lib/license_jssc.txt", installDir + "/lib/license_jssc.txt");
//                //     }
//                // };
//                // runner.run();
//
//                // apache coomons-io
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/commons-io-2.5.jar"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyFromJar(installerJar, "lib/commons-io-2.5.jar", installDir + "/lib/commons-io-2.5.jar");
//                installTool.copyFromJar(installerJar, "lib/license_commons-io.txt", installDir + "/lib/license_commons-io.txt");
//                //     }
//                // };
//                // runner.run();
//
//                // rsyntaxarea
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/rsyntaxtextarea.jar"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyFromJar(installerJar, "lib/rsyntaxtextarea.jar", installDir + "/lib/rsyntaxtextarea.jar");
//                installTool.copyFromJar(installerJar, "lib/license_rsyntaxtextarea.txt", installDir + "/lib/license_rsyntaxtextarea.txt");
//                //     }
//                // };
//                // runner.run();
//
//                // autocomplete
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/autocomplete.jar"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyFromJar(installerJar, "lib/autocomplete.jar", installDir + "/lib/autocomplete.jar");
//                installTool.copyFromJar(installerJar, "lib/license_autocomplete.txt", installDir + "/lib/license_autocomplete.txt");
//                //     }
//                // };
//                // runner.run();
//
//                // srecord
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/srecord/"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyDirFromJar(installerJar, "lib/srecord/", installDir + "/lib/srecord/");
//                //     }
//                // };
//                // runner.run();
//
//                // The build system
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/build/"));
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                installTool.copyDirFromJar(installerJar, "build/", installDir + "/build/", true);
//                //     }
//                // };
//                // runner.run();
//
//                // Copy common libraries into buildsystem
//                // addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/build/calliope/source/lib/"));
//                // installTool.copyDirFromJar(installerJar, "build/common/lib/", installDir + "/build/calliope/source/lib/",false);
//                // addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/build/microbit/source/lib/"));
//                // installTool.copyDirFromJar(installerJar, "build/common/lib/", installDir + "/build/microbit/source/lib/",false);
//                // Tools
//                String osname = System.getProperty("os.name").toLowerCase();
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/tools.zip"));
//
//                // runner = new Runnable() {
//                //     @Override
//                //     public void run() {
//                ZipFile zip = null;
//
//                try {
//                    installTool.copyFromJar(installerJar, "tools.zip", installDir + "/tools.zip");
//                    zip = new ZipFile(installDir + "/tools.zip");
//                } catch (Exception ex) {
//                    addMsg(msgDoc, installDir + "/tools.zip not found");
//                }
//
//                if (zip != null) {
//                    try {
//                        Enumeration<ZipEntry> entries = (Enumeration<ZipEntry>) zip.entries();
//                        while (entries.hasMoreElements()) {
//                            ZipEntry entry = entries.nextElement();
//                            String name = entry.getName();
//
//                            // addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/" + name));
//                            File target = new File(installDir + "/" + name);
//                            addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", name));
//
//                            if (entry.isDirectory()) {
//                                target.mkdir();
//                            } else {
//                                Files.copy(zip.getInputStream(entry), target.toPath(), StandardCopyOption.REPLACE_EXISTING);
//                            }
//                        }
//
//                        addMsg(msgDoc, AbbozzaLocale.entry("MSG.SET_PERMISSIONS"));
//                        BufferedReader execs = new BufferedReader(new FileReader(installDir + "/tools/.executables"));
//                        while (execs.ready()) {
//                            String exe = execs.readLine();
//                            File exef = new File(installDir + "/" + exe);
//                            exef.setExecutable(true);
//                        }
//                    } catch (Exception ex) {
//                        addMsg(msgDoc, ex.getLocalizedMessage());
//                    }
//                }
//                //     }
//                // };
//                // runner.run();
//
//                // Scripts
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/bin/abbozzaC.[sh|bat]"));
//                installTool.copyFromJar(installerJar, "scripts/abbozzaC.sh", installDir + "/bin/abbozzaC.sh");
//                installTool.copyFromJar(installerJar, "scripts/abbozzaC.bat", installDir + "/bin/abbozzaC.bat");
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/bin/abbozzaMicroPython.[sh|bat]"));
//                installTool.copyFromJar(installerJar, "scripts/abbozzaMicroPython.sh", installDir + "/bin/abbozzaMicroPython.sh");
//                installTool.copyFromJar(installerJar, "scripts/abbozzaMicroPython.bat", installDir + "/bin/abbozzaMicroPython.bat");
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/bin/abbozzaMonitor.[sh|bat]"));
//                installTool.copyFromJar(installerJar, "scripts/abbozzaMonitor.sh", installDir + "/bin/abbozzaMonitor.sh");
//                installTool.copyFromJar(installerJar, "scripts/abbozzaMonitor.bat", installDir + "/bin/abbozzaMonitor.bat");
//
//                // Icons
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza_icon_white"));
//                installTool.copyFromJar(installerJar, "lib/abbozza_icon_white.png", installDir + "/lib/abbozza_icon_white.png");
//                installTool.copyFromJar(installerJar, "lib/abbozza_icon_white.ico", installDir + "/lib/abbozza_icon_white.ico");
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING", installDir + "/lib/abbozza_icon_monitor"));
//                installTool.copyFromJar(installerJar, "lib/abbozza_icon_monitor.png", installDir + "/lib/abbozza_icon_monitor.png");
//                installTool.copyFromJar(installerJar, "lib/abbozza_icon_monitor.ico", installDir + "/lib/abbozza_icon_monitor.ico");
//                // Do not copy the build template to the users dir! Wait for compilation!
//                // addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING",userDir + "/calliopeC/build/"));
//                // installTool.copyDirFromJar(installerJar, "build/", userDir + "/calliopeC/build/");
//                /**
//                 * 8th step: Add application to menus
//                 */
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.ADDING_MENU"));
//                String scriptSuffix = installTool.getScriptSuffix();
//                String iconSuffix = installTool.getIconSuffix();
//
//                if (installTool.getSystem()
//                        .equals("Mac")) {
//                    // Create Contents/MacOS
//                    String installDir2 = userInstallDir.getAbsolutePath() + "/Contents/MacOS/";
//                    String installDir3 = userInstallDir.getAbsolutePath() + "/Contents/";
//                    createDir(installDir2, msgDoc);
//                    installTool.copyFromJar(installerJar, "lib/abbozza.icns", installDir.getAbsolutePath() + "/abbozza.icns");
//                    installTool.copyFromJar(installerJar, "lib/Info.plist", installDir3 + "/Info.plist");
//                    createFile(installDir2 + "abbozza", msgDoc);
//                    File starter = new File(installDir2 + "abbozza");
//                    FileWriter writer;
//                    try {
//                        writer = new FileWriter(starter);
//                        writer.append("#!/bin/bash\n\n");
//                        writer.append("CWD=" + installDir.getAbsolutePath() + "\n");
//                        writer.append("YOTTA_PATH=\"$CWD/prerequisites:$CWD/prerequisites/gcc-arm-none-eabi-4_9-2015q3/bin:$CWD/prerequisites/CMake.app/Contents/bin:$CWD/workspace/bin\"\n");
//                        writer.append("export PATH=$YOTTA_PATH:$PATH\n");
//                        writer.append("export YOTTA_CWD=\"$CWD\"\n");
//                        writer.append("cd " + installDir.getAbsolutePath() + "/bin\n");
//                        writer.append("./abbozzaC.sh");
//                        starter.setExecutable(true);
//                        writer.close();
//                    } catch (IOException ex) {
//                        addMsg(msgDoc, "Could not create " + starter.getAbsolutePath());
//                    }
//
//                    installDir2 = userInstallDir.getParent() + "/abbozzaMonitor.app/Contents/MacOS/";
//                    installDir3 = userInstallDir.getParent() + "/abbozzaMonitor.app/Contents/";
//                    createDir(installDir2, msgDoc);
//                    createDir(installDir3 + "Resources", msgDoc);
//                    installTool.copyFromJar(installerJar, "lib/abbozza_monitor.icns", installDir3 + "Resources/abbozza_monitor.icns");
//                    installTool.copyFromJar(installerJar, "lib/Info.monitor.plist", installDir3 + "/Info.plist");
//                    createFile(installDir2 + "abbozzaMonitor", msgDoc);
//                    starter = new File(installDir2 + "abbozzaMonitor");
//                    try {
//                        writer = new FileWriter(starter);
//                        writer.append("#!/bin/bash\n\n");
//                        writer.append("cd " + installDir.getAbsolutePath() + "/bin\n");
//                        writer.append("./abbozzaMonitor.sh");
//                        starter.setExecutable(true);
//                        writer.close();
//                    } catch (IOException ex) {
//                        addMsg(msgDoc, "Could not create " + starter.getAbsolutePath());
//                    }
//
//                } else {
//                    installTool.addAppToMenu("abbozzaCalliopeC", "abbozza! Calliope C",
//                            "abbozza! Calliope C",
//                            installDir + "/bin/abbozzaC" + scriptSuffix, installDir + "/lib/abbozza_icon_white" + iconSuffix, globalInstall);
//
//                    // installTool.addAppToMenu("abbozzaCalliopeMicroPython", "abbozza! Calliope MicroPython",
//                    //     "abbozza! Calliope MicroPython",
//                    //     installDir + "/bin/abbozzaMicroPython" + scriptSuffix, installDir + "/lib/abbozza_icon_white" + iconSuffix, globalInstall);
//                    installTool.addAppToMenu("abbozzaMonitor", "abbozza! Monitor",
//                            "abbozza! Monitor",
//                            installDir + "/bin/abbozzaMonitor" + scriptSuffix, installDir + "/lib/abbozza_icon_monitor" + iconSuffix, globalInstall);
//                }
//                /**
//                 * Write configuration file
//                 */
//                Properties config = new Properties();
//
//                config.setProperty(
//                        "freshInstall", "true");
//                config.setProperty(
//                        "browserPath", browserPath);
//                config.setProperty(
//                        "installPath", userInstallDir.getAbsolutePath());
//                config.setProperty(
//                        "sketchbookPath", sketchbookPath);
//
//                // Do NOT write config file to user dir. Instead write template to lib
//                // File prefFile = new File(userDir.getAbsolutePath() + "/calliopeMP/abbozza.cfg");
//                File prefFile = new File(installDir.getAbsolutePath() + "/lib/calliopeMP.cfg");
//
//                try {
//                    prefFile.getParentFile().mkdirs();
//                    prefFile.createNewFile();
//
//                    config.store(new FileOutputStream(prefFile), "abbozza! preferences");
//                    addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(frame, AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()),
//                            AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.ERROR_MESSAGE);
//                    frame.setVisible(false);
//                    System.exit(1);
//                }
//
//                prefFile = new File(installDir.getAbsolutePath() + "/lib/calliopeC.cfg");
//
//                try {
//                    prefFile.getParentFile().mkdirs();
//                    prefFile.createNewFile();
//
//                    config.store(new FileOutputStream(prefFile), "abbozza! preferences");
//                    addMsg(msgDoc, AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()));
//                } catch (IOException ex) {
//                    JOptionPane.showMessageDialog(frame, AbbozzaLocale.entry("MSG.WRITING_CONFIGURATION", prefFile.getAbsolutePath()),
//                            AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.ERROR_MESSAGE);
//                    frame.setVisible(false);
//                    System.exit(1);
//                }
//
//                /**
//                 * 9th step: first compile for microbit and calliope
//                 */
//                boolean microbitCompiled = false;
//                boolean calliopeCompiled = false;
//
//                if (yottaInstalled) {
//                    int opt = JOptionPane.showConfirmDialog(frame,
//                            AbbozzaLocale.entry("MSG.PRECOMPILE", "Calliope Mini"),
//                            AbbozzaLocale.entry("GUI.TITLE"), JOptionPane.YES_NO_OPTION);
//                    if (opt == JOptionPane.YES_OPTION) {
//                        addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_CALLIOPE"));
//                        if (build(installDir.getAbsolutePath() + "/build/calliope/", installDir.getAbsolutePath(), msgDoc) != 0) {
//                            addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_FAILED"));
//                        } else {
//                            addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_SUCCESS"));
//                            calliopeCompiled = true;
//                        }
//                    }
//
//                    opt = JOptionPane.showConfirmDialog(frame,
//                            AbbozzaLocale.entry("MSG.PRECOMPILE", "micro:bit"),
//                            AbbozzaLocale.entry("GUI.TITLE"), JOptionPane.YES_NO_OPTION);
//                    if (opt == JOptionPane.YES_OPTION) {
//                        addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_MICROBIT"));
//                        if (build(installDir.getAbsolutePath() + "/build/microbit/", installDir.getAbsolutePath(), msgDoc) != 0) {
//                            addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_FAILED"));
//                        } else {
//                            addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_SUCCESS"));
//                            microbitCompiled = true;
//                        }
//                    }
//                }
//
//                addMsg(msgDoc, AbbozzaLocale.entry("MSG.SUCCESS"));
//                logFrame.enableButton();
//            }
//        };
//        runner.run();

    }//GEN-LAST:event_installButtonActionPerformed


    private void installDirButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installDirButtonActionPerformed
        JFileChooser chooser = new JFileChooser(installField.getText());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            installField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_installDirButtonActionPerformed

    private void browserButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_browserButtonActionPerformed
        JFileChooser chooser = new JFileChooser(browserField.getText());
        int returnVal = chooser.showOpenDialog(this);
        chooser.setFileFilter(new FileFilter() {

            @Override
            public boolean accept(File f) {
                return f.canExecute();
            }

            @Override
            public String getDescription() {
                return AbbozzaLocale.entry("GUI.SELECT_EXECUTABLES");
            }
        });
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            if (chooser.getSelectedFile() != null) {
                browserField.setText(chooser.getSelectedFile().getAbsolutePath());
            }
        }
    }//GEN-LAST:event_browserButtonActionPerformed

    private void installFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_installFieldActionPerformed

    private void installField1ActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_installField1ActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_installField1ActionPerformed

    private void sketchbookFieldActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sketchbookFieldActionPerformed
        // TODO add your handling code here:
    }//GEN-LAST:event_sketchbookFieldActionPerformed

    private void sketchbookButtonActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_sketchbookButtonActionPerformed
        JFileChooser chooser = new JFileChooser(installField.getText());
        chooser.setFileSelectionMode(JFileChooser.DIRECTORIES_ONLY);
        int returnVal = chooser.showOpenDialog(this);
        if (returnVal == JFileChooser.APPROVE_OPTION) {
            installField.setText(chooser.getSelectedFile().getAbsolutePath());
        }
    }//GEN-LAST:event_sketchbookButtonActionPerformed

    /**
     * @param args the command line arguments
     */
    public static void main(String args[]) {
        try {
            for (javax.swing.UIManager.LookAndFeelInfo info : javax.swing.UIManager.getInstalledLookAndFeels()) {
                if ("Nimbus".equals(info.getName())) {
                    javax.swing.UIManager.setLookAndFeel(info.getClassName());
                    break;

                }
            }
        } catch (ClassNotFoundException | InstantiationException | IllegalAccessException | javax.swing.UnsupportedLookAndFeelException ex) {
            java.util.logging.Logger.getLogger(AbbozzaCalliopeInstaller.class
                    .getName()).log(java.util.logging.Level.SEVERE, null, ex);
        }

        /* Read options */
        boolean global = false;
        for (int i = 0; i < args.length; i++) {
            if (args[i].equals("-global")) {
                global = true;
            }
        }

        AbbozzaCalliopeInstaller installer = new AbbozzaCalliopeInstaller(global);

        /* Create and display the form */
        java.awt.EventQueue.invokeLater(new Runnable() {
            public void run() {
                installer.setVisible(true);
            }
        });

    }

    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JButton browserButton;
    private javax.swing.JTextField browserField;
    private javax.swing.JPanel buttonPanel;
    private javax.swing.JButton cancelButton;
    private javax.swing.JButton installButton;
    private javax.swing.JButton installDirButton;
    private javax.swing.JTextField installField;
    private javax.swing.JTextField installField1;
    private javax.swing.JLabel jLabel1;
    private javax.swing.JLabel jLabel2;
    private javax.swing.JLabel jLabel3;
    private javax.swing.JLabel jLabel4;
    private javax.swing.JLabel jLabel5;
    private javax.swing.JLabel jLabel6;
    private javax.swing.JScrollPane jScrollPane1;
    private javax.swing.JPanel logoPanel;
    private javax.swing.JPanel mainPanel;
    private javax.swing.JTextPane msgPanel;
    private javax.swing.JButton sketchbookButton;
    private javax.swing.JTextField sketchbookField;
    // End of variables declaration//GEN-END:variables

    /**
     * Check if yotta is installed
     */
    private boolean checkPrerequisites() {
        if (installTool.getSystem().equals("Win")) {
            return checkPrerequisitesWin();
        } else if (installTool.getSystem().equals("Mac")) {
            return checkPrerequisitesMac();
        }
        return checkPrerequisitesLinux();
    }

    private boolean checkPrerequisitesLinux() {
        try {
            ProcessBuilder procBuilder = new ProcessBuilder("yt", "--version");
            Process proc = procBuilder.start();
            proc.waitFor();
        } catch (IOException ex) {
            int opt = JOptionPane.showConfirmDialog(this,
                    AbbozzaLocale.entry("ERR.YOTTA_MISSING") + "\n"
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                return false;
            }
        } catch (InterruptedException ex) {
            System.out.println(ex.getLocalizedMessage());
        }
        return true;
    }

    private boolean checkPrerequisitesMac() {
        try {
            ProcessBuilder procBuilder = new ProcessBuilder("yt", "--version");
            Process proc = procBuilder.start();
            proc.waitFor();
        } catch (IOException ex) {
            System.err.println(ex.getLocalizedMessage());
            int opt = JOptionPane.showConfirmDialog(this,
                    AbbozzaLocale.entry("ERR.YOTTA_MISSING") + "\n"
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                return false;
            }
        } catch (InterruptedException ex) {
        }
        return true;
    }

    private boolean checkPrerequisitesWin() {
        String yottaPath = System.getenv("YOTTA_PATH");
        String yottaInstall = System.getenv("YOTTA_INSTALL_LOCATION");
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(yottaInstall + "\\workspace\\Scripts\\yt", "--version");
            procBuilder.environment().put("PATH", yottaPath + ";" + System.getenv("PATH"));
            procBuilder.inheritIO();
            Process proc = procBuilder.start();
            proc.waitFor();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            int opt = JOptionPane.showConfirmDialog(this,
                    AbbozzaLocale.entry("ERR.YOTTA_MISSING") + "\n"
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                return false;
            }
        } catch (InterruptedException ex) {
        }
        return true;
    }

    private int build(String buildPath, String installPath, Document msgDoc) {
        addMsg(msgDoc, AbbozzaLocale.entry("MSG.COMPILE_IN") + buildPath);
        if (installTool.getSystem().equals("Win")) {
            return buildWin(buildPath, installPath, msgDoc);
        }
        return buildLinux(buildPath);
    }

    private int buildLinux(String buildPath) {
        try {
            ProcessBuilder procBuilder = new ProcessBuilder("yt", "-n", "update");
            procBuilder.directory(new File(buildPath));
            procBuilder.inheritIO();
            Process proc = procBuilder.start();
            proc.waitFor();
            return proc.exitValue();
        } catch (IOException ex) {
            return 2;
        } catch (InterruptedException ex) {
            return 3;
        }
    }

    private int buildWin(String buildPath, String installPath, Document msgDoc) {
        String yottaPath = System.getenv("YOTTA_PATH");
        String yottaInstall = System.getenv("YOTTA_INSTALL_LOCATION");
        try {
            ProcessBuilder procBuilder = new ProcessBuilder(yottaInstall + "\\workspace\\Scripts\\yt", "-n", "update");
            procBuilder.directory(new File(buildPath));
            procBuilder.environment().put("PATH", installPath + "\\lib\\srecord\\" + ";" + yottaPath + ";" + System.getenv("PATH"));
            // procBuilder.inheritIO();
            procBuilder.redirectOutput(ProcessBuilder.Redirect.PIPE);
            Process proc = procBuilder.start();
            InputStream inp = proc.getInputStream();
            BufferedReader buf = new BufferedReader(new InputStreamReader(inp));
            while (proc.isAlive()) {
                if (buf.ready()) {
                    String line = buf.readLine();
                    addMsg(msgDoc, line);
                }
            }
            // proc.waitFor();
            return proc.exitValue();
        } catch (IOException ex) {
            ex.printStackTrace(System.out);
            int opt = JOptionPane.showConfirmDialog(this,
                    AbbozzaLocale.entry("ERR.YOTTA_MISSING") + "\n"
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION"),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.YES_NO_OPTION);
            if (opt == JOptionPane.YES_OPTION) {
                return 2;
            }
        }
        return 4;
    }

    private synchronized void addMsg(Document msgDoc, String text) {
        try {
            msgDoc.insertString(msgDoc.getLength(), text + "\n", null);
        } catch (BadLocationException ex) {
        }
    }

    
    private boolean createDir(String path, Document msgDoc) {
        File buildDir = new File(path);
        addMsg(msgDoc, AbbozzaLocale.entry("MSG.CREATING_DIR", path));
        buildDir.mkdirs();
        if (!buildDir.canWrite()) {
            JOptionPane.showMessageDialog(this,
                    AbbozzaLocale.entry("ERR.CANNOT_WRITE", path),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
        return true;
    }

    private boolean createFile(String path, Document msgDoc) {
        try {
            File file = new File(path);
            // addMsg(msgDoc, AbbozzaLocale.entry("MSG.CREATING_DIR",path));
            file.delete();
            file.createNewFile();
            return true;
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this,
                    AbbozzaLocale.entry("ERR.CANNOT_WRITE", path),
                    AbbozzaLocale.entry("ERR.TITLE"), JOptionPane.ERROR_MESSAGE);
            return false;
        }
    }

    public void setGlobal(boolean global) {
        globalInstall = global;
    }

}
