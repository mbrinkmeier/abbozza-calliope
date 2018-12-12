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
import de.uos.inf.did.abbozza.core.AbbozzaVersion;
import de.uos.inf.did.abbozza.install.AbbozzaLoggingFrame;
import de.uos.inf.did.abbozza.install.InstallTool;
import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.util.Properties;
import java.util.jar.JarFile;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.JProgressBar;
import javax.swing.JRootPane;
import javax.swing.SwingUtilities;
import javax.swing.filechooser.FileFilter;
import javax.swing.text.BadLocationException;
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
            this.sketchbookField.setText(System.getProperty("user.home") + "/abbozzaCalliope");
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
                    + AbbozzaLocale.entry("MSG.CONTINUE_INSTALLATION") + "\n" + "( Version " + AbbozzaVersion.asString() + " )",
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
        jLabel4.setText(AbbozzaVersion.asString());
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

        installField.setText(installTool.getInstallPath(globalInstall,"Calliope"));
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

        jLabel6.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jLabel6.setText(AbbozzaLocale.entry("GUI.SKETCHBOOK_DIR"));
        gridBagConstraints = new java.awt.GridBagConstraints();
        gridBagConstraints.gridx = 0;
        gridBagConstraints.gridy = 4;
        gridBagConstraints.gridwidth = 2;
        gridBagConstraints.fill = java.awt.GridBagConstraints.BOTH;
        gridBagConstraints.anchor = java.awt.GridBagConstraints.SOUTH;
        mainPanel.add(jLabel6, gridBagConstraints);

        sketchbookField.setText("%HOME%/abbozzaCalliope");
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
