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

import org.fife.ui.autocomplete.*;
import org.fife.ui.rtextarea.*;
import org.fife.ui.rsyntaxtextarea.*;
import de.uos.inf.did.abbozza.AbbozzaLocale;
import de.uos.inf.did.abbozza.AbbozzaLogger;
import de.uos.inf.did.abbozza.AbbozzaLoggerListener;
import de.uos.inf.did.abbozza.AbbozzaServer;
import de.uos.inf.did.abbozza.Tools;
import de.uos.inf.did.abbozza.tools.GUITool;
import java.awt.Color;
import java.awt.Font;
import java.awt.Window;
import java.awt.event.WindowEvent;
import java.awt.event.WindowListener;
import java.awt.event.WindowStateListener;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.ImageIcon;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.UIManager;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
/**
 *
 * @author mbrinkmeier
 */
public class AbbozzaCalliopeFrame  extends javax.swing.JFrame implements AbbozzaLoggerListener, AbbozzaCalliopeGUI, DocumentListener {

    private PrintStream _console;
    private RTextScrollPane sourcePanel;
    private RSyntaxTextArea sourceArea;
    private Highlighter sourceHighlighter;
    private AbbozzaCalliopeTooltipSupplier supplier;
    private DefaultStyledDocument consoleDoc;
    private File lastSourceFile = null;
    private boolean docChanged = false;
    
    /**
     * Creates new form AbbozzaCalliopeFrame
     */
    public AbbozzaCalliopeFrame() {
                
        Font f = new Font("sans-serif", Font.PLAIN, 12);
        UIManager.put("Menu.font", f);
        
        initComponents();

        ImageIcon icon = new ImageIcon(AbbozzaCalliopeFrame.class.getResource("/img/abbozza_icon_white.png"));
        this.setIconImage(icon.getImage());
        
        switch (AbbozzaLogger.getLevel()) {
            case AbbozzaLogger.NONE : 
                this.noneMenuItem.setSelected(true); 
                break;
            case AbbozzaLogger.ERROR : 
                this.errMenuItem.setSelected(true);
                break;
            case AbbozzaLogger.WARNING :
                this.warnMenuItem.setSelected(true);
                break;
            case AbbozzaLogger.INFO :
                this.infoMenuItem.setSelected(true);
                break;
            case AbbozzaLogger.DEBUG : 
                this.debugMenuItem.setSelected(true);
                break;
        }

        String boardPath = AbbozzaServer.getInstance().findBoard();
        if ( boardPath.equals("") ) {
            connectButton.setBackground(Color.red);
        } else {
            connectButton.setBackground(Color.green);            
        }
        sourceArea = new RSyntaxTextArea(50, 120);
        sourceArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        sourceArea.setCodeFoldingEnabled(true);        
        sourceArea.setTabSize(3);
        supplier = new AbbozzaCalliopeTooltipSupplier();
        sourceArea.setToolTipSupplier(supplier);
        
        sourceHighlighter = sourceArea.getHighlighter();

        sourcePanel = new RTextScrollPane(sourceArea);
        editorPane.add(sourcePanel, java.awt.BorderLayout.CENTER);

        DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);        
        
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(sourceArea);
        ac.setAutoActivationDelay(500);
        ac.setAutoActivationEnabled(true);        

        pack();
        
        AbbozzaLogger.addListener(this);
        
        newActionPerformed(null);
        
        sourceArea.getDocument().addDocumentListener(this);

        this.addWindowStateListener(new WindowStateListener() {
            @Override
            public void windowStateChanged(WindowEvent e) {
                AbbozzaLogger.debug(e.paramString());
                AbbozzaLogger.debug(e.toString());
            }
        });
        
        this.addWindowListener(new WindowListener() {
            @Override
            public void windowOpened(WindowEvent e) {
            }

            @Override
            public void windowClosing(WindowEvent e) {
                JFrame win = (JFrame) e.getWindow();
                int state = win.getExtendedState();
                win.setExtendedState(state | JFrame.ICONIFIED);
            }

            @Override
            public void windowClosed(WindowEvent e) {
            }

            @Override
            public void windowIconified(WindowEvent e) {
            }

            @Override
            public void windowDeiconified(WindowEvent e) {
            }

            @Override
            public void windowActivated(WindowEvent e) {
            }

            @Override
            public void windowDeactivated(WindowEvent e) {
            }
        });
    }

    /**
     * This method is called from within the constructor to initialize the form.
     * WARNING: Do NOT modify this code. The content of this method is always
     * regenerated by the Form Editor.
     */
    @SuppressWarnings("unchecked")
    // <editor-fold defaultstate="collapsed" desc="Generated Code">//GEN-BEGIN:initComponents
    private void initComponents() {

        compileButton2 = new javax.swing.JButton();
        popupMenu = new javax.swing.JPopupMenu();
        clearMenuItem = new javax.swing.JMenuItem();
        jSeparator4 = new javax.swing.JPopupMenu.Separator();
        noneMenuItem = new javax.swing.JRadioButtonMenuItem();
        errMenuItem = new javax.swing.JRadioButtonMenuItem();
        warnMenuItem = new javax.swing.JRadioButtonMenuItem();
        infoMenuItem = new javax.swing.JRadioButtonMenuItem();
        debugMenuItem = new javax.swing.JRadioButtonMenuItem();
        logLevelGroup = new javax.swing.ButtonGroup();
        jMenuItem1 = new javax.swing.JMenuItem();
        splitPane = new javax.swing.JSplitPane();
        jTabbedPane1 = new javax.swing.JTabbedPane();
        messagePane = new javax.swing.JScrollPane();
        messageArea = new javax.swing.JEditorPane();
        consolePane = new javax.swing.JScrollPane();
        consoleArea = new javax.swing.JEditorPane();
        editorPane = new javax.swing.JPanel();
        toolbar = new javax.swing.JToolBar();
        compileButton = new javax.swing.JButton();
        uploadButton = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        loadButton = new javax.swing.JButton();
        saveButton = new javax.swing.JButton();
        jSeparator7 = new javax.swing.JToolBar.Separator();
        serialButton = new javax.swing.JButton();
        connectButton = new javax.swing.JButton();
        menuBar = new javax.swing.JMenuBar();
        sketchMenu = new javax.swing.JMenu();
        newMenuItem = new javax.swing.JMenuItem();
        jSeparator8 = new javax.swing.JPopupMenu.Separator();
        loadSketchMenuItem = new javax.swing.JMenuItem();
        saveSketchMenuItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        compileMenuItem = new javax.swing.JMenuItem();
        uploadMenuItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        quitItem = new javax.swing.JMenuItem();
        abbozzaMenu = new javax.swing.JMenu();
        startBrowserItem = new javax.swing.JMenuItem();
        jSeparator6 = new javax.swing.JPopupMenu.Separator();
        settingsItem = new javax.swing.JMenuItem();

        compileButton2.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        compileButton2.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/save.png"))); // NOI18N
        compileButton2.setToolTipText(AbbozzaLocale.entry("gui.save"));
        compileButton2.setFocusable(false);
        compileButton2.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compileButton2.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compileButton2.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });

        clearMenuItem.setText(AbbozzaLocale.entry("gui.clear"));
        clearMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearMenuActionPerformed(evt);
            }
        });
        popupMenu.add(clearMenuItem);
        popupMenu.add(jSeparator4);

        logLevelGroup.add(noneMenuItem);
        noneMenuItem.setSelected(true);
        noneMenuItem.setText(AbbozzaLocale.entry("gui.log_none"));
        noneMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                noneMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(noneMenuItem);

        logLevelGroup.add(errMenuItem);
        errMenuItem.setText(AbbozzaLocale.entry("gui.log_err"));
        errMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                errMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(errMenuItem);

        logLevelGroup.add(warnMenuItem);
        warnMenuItem.setText(AbbozzaLocale.entry("gui.log_warn"));
        warnMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                warnMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(warnMenuItem);

        logLevelGroup.add(infoMenuItem);
        infoMenuItem.setText(AbbozzaLocale.entry("gui.log_info"));
        infoMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                infoMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(infoMenuItem);

        logLevelGroup.add(debugMenuItem);
        debugMenuItem.setText(AbbozzaLocale.entry("gui.log_debug"));
        debugMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                debugMenuItemActionPerformed(evt);
            }
        });
        popupMenu.add(debugMenuItem);

        jMenuItem1.setText("jMenuItem1");

        setDefaultCloseOperation(javax.swing.WindowConstants.DO_NOTHING_ON_CLOSE);
        setTitle("abbozza! Calliope");

        splitPane.setDividerLocation(500);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);
        splitPane.setToolTipText("");

        messagePane.setViewportView(messageArea);

        jTabbedPane1.addTab(AbbozzaLocale.entry("gui.messages"), messagePane);

        consolePane.setComponentPopupMenu(popupMenu);

        consoleArea.setEditable(false);
        consoleArea.setInheritsPopupMenu(true);
        consoleArea.setPreferredSize(new java.awt.Dimension(106, 10));
        consolePane.setViewportView(consoleArea);

        jTabbedPane1.addTab(AbbozzaLocale.entry("gui.log"), consolePane);

        splitPane.setBottomComponent(jTabbedPane1);

        editorPane.setLayout(new java.awt.BorderLayout());

        toolbar.setFloatable(false);
        toolbar.setRollover(true);
        toolbar.setMinimumSize(new java.awt.Dimension(18, 18));

        compileButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        compileButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/generate.png"))); // NOI18N
        compileButton.setToolTipText(AbbozzaLocale.entry("gui.compile"));
        compileButton.setFocusable(false);
        compileButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compileButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compileButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });
        toolbar.add(compileButton);

        uploadButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        uploadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/upload.png"))); // NOI18N
        uploadButton.setToolTipText(AbbozzaLocale.entry("gui.upload"));
        uploadButton.setFocusable(false);
        uploadButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        uploadButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        uploadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadActionPerformed(evt);
            }
        });
        toolbar.add(uploadButton);
        toolbar.add(jSeparator5);

        loadButton.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        loadButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/load.png"))); // NOI18N
        loadButton.setToolTipText(AbbozzaLocale.entry("gui.load_sketch"));
        loadButton.setFocusable(false);
        loadButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        loadButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        loadButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        toolbar.add(loadButton);

        saveButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/save.png"))); // NOI18N
        saveButton.setToolTipText(AbbozzaLocale.entry("gui.save_sketch")
        );
        saveButton.setFocusable(false);
        saveButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        saveButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        saveButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        toolbar.add(saveButton);
        toolbar.add(jSeparator7);

        serialButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/serial.png"))); // NOI18N
        serialButton.setToolTipText(AbbozzaLocale.entry("gui.serial_button"));
        serialButton.setFocusable(false);
        serialButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        serialButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        serialButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                serialActionPerformed(evt);
            }
        });
        toolbar.add(serialButton);

        connectButton.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/connect.png"))); // NOI18N
        connectButton.setToolTipText(AbbozzaLocale.entry("gui.connect_button"));
        connectButton.setFocusable(false);
        connectButton.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        connectButton.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        connectButton.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                connectActionPerformed(evt);
            }
        });
        toolbar.add(connectButton);

        editorPane.add(toolbar, java.awt.BorderLayout.NORTH);

        splitPane.setLeftComponent(editorPane);

        sketchMenu.setText(AbbozzaLocale.entry("gui.sketch_menu"));
        sketchMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });

        newMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_N, java.awt.event.InputEvent.CTRL_MASK));
        newMenuItem.setText(AbbozzaLocale.entry("gui.new_button"));
        newMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                newActionPerformed(evt);
            }
        });
        sketchMenu.add(newMenuItem);
        sketchMenu.add(jSeparator8);

        loadSketchMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_L, java.awt.event.InputEvent.CTRL_MASK));
        loadSketchMenuItem.setText(AbbozzaLocale.entry("gui.load_button"));
        loadSketchMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        sketchMenu.add(loadSketchMenuItem);

        saveSketchMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_S, java.awt.event.InputEvent.CTRL_MASK));
        saveSketchMenuItem.setText(AbbozzaLocale.entry("gui.save_button"));
        saveSketchMenuItem.setToolTipText("");
        saveSketchMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                saveActionPerformed(evt);
            }
        });
        sketchMenu.add(saveSketchMenuItem);
        sketchMenu.add(jSeparator2);

        compileMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_C, java.awt.event.InputEvent.CTRL_MASK));
        compileMenuItem.setText(AbbozzaLocale.entry("gui.compile"));
        compileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });
        sketchMenu.add(compileMenuItem);

        uploadMenuItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_U, java.awt.event.InputEvent.CTRL_MASK));
        uploadMenuItem.setText(AbbozzaLocale.entry("gui.upload"));
        uploadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadActionPerformed(evt);
            }
        });
        sketchMenu.add(uploadMenuItem);
        sketchMenu.add(jSeparator3);

        quitItem.setText(AbbozzaLocale.entry("gui.quit")
        );
        quitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitItemActionPerformed(evt);
            }
        });
        sketchMenu.add(quitItem);

        menuBar.add(sketchMenu);

        abbozzaMenu.setText("abbozza!");

        startBrowserItem.setAccelerator(javax.swing.KeyStroke.getKeyStroke(java.awt.event.KeyEvent.VK_B, java.awt.event.InputEvent.CTRL_MASK));
        startBrowserItem.setText(AbbozzaLocale.entry("gui.startBrowser"));
        startBrowserItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBrowserItemActionPerformed(evt);
            }
        });
        abbozzaMenu.add(startBrowserItem);
        abbozzaMenu.add(jSeparator6);

        settingsItem.setText(AbbozzaLocale.entry("gui.settings"));
        settingsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsItemActionPerformed(evt);
            }
        });
        abbozzaMenu.add(settingsItem);

        menuBar.add(abbozzaMenu);

        setJMenuBar(menuBar);

        javax.swing.GroupLayout layout = new javax.swing.GroupLayout(getContentPane());
        getContentPane().setLayout(layout);
        layout.setHorizontalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addGroup(layout.createSequentialGroup()
                .addGap(0, 0, 0)
                .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 973, Short.MAX_VALUE))
        );
        layout.setVerticalGroup(
            layout.createParallelGroup(javax.swing.GroupLayout.Alignment.LEADING)
            .addComponent(splitPane, javax.swing.GroupLayout.DEFAULT_SIZE, 628, Short.MAX_VALUE)
        );

        pack();
    }// </editor-fold>//GEN-END:initComponents

    private void quitItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_quitItemActionPerformed
        // TODO add your handling code here:
        System.exit(0);
    }//GEN-LAST:event_quitItemActionPerformed

    private void startBrowserItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBrowserItemActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        abbozza.startBrowser(abbozza.getSystem()+".html");
    }//GEN-LAST:event_startBrowserItemActionPerformed

    private void uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadActionPerformed
        messageArea.setText("");
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String response = abbozza.uploadCode(this.sourceArea.getText());
        if ( response != null ) {
            if ( response.equals("")) {
                response = AbbozzaLocale.entry("gui.compilation_success");
            }
            appendConsoleText(response);
        }
    }//GEN-LAST:event_uploadActionPerformed

    private void settingsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsItemActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        abbozza.openConfigDialog();
    }//GEN-LAST:event_settingsItemActionPerformed

    private void compileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileActionPerformed
        messageArea.setText("");
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String response = abbozza.compileCode(this.sourceArea.getText());
        if ( response != null ) {
            if ( response.equals("")) {
                response = AbbozzaLocale.entry("gui.compilation_success");
            }
            appendConsoleText(response);
        }
    }//GEN-LAST:event_compileActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
        if ( !checkSave() ) return;
        
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String path;
        try {
            path = ((lastSourceFile != null) ? lastSourceFile.getCanonicalPath() : abbozza.getSketchbookPath());
        } catch (IOException ex) {
            path = System.getProperty("user.home");
        }
        JFileChooser chooser = new JFileChooser(path);
        
        chooser.setFileFilter(new FileNameExtensionFilter("abbozza! (*.cpp)", "cpp"));
        if ( (lastSourceFile != null) && (lastSourceFile.isDirectory()) ) {
            chooser.setCurrentDirectory(lastSourceFile);            
        } else {
            chooser.setSelectedFile(lastSourceFile);
        }
        
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();                        
            lastSourceFile = file;
            loadCode(file);
        }
        
    }//GEN-LAST:event_loadActionPerformed

    private void saveActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_saveActionPerformed
        if (saveSketch()) {
            docChanged = false;
        }
        /*
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String path;
        try {
            path = ((lastSourceFile != null) ? lastSourceFile.getCanonicalPath() : abbozza.getSketchbookPath());
        } catch (IOException ex) {
            path = System.getProperty("user.home");
        }
        JFileChooser chooser = new JFileChooser(path);
        
        chooser.setFileFilter(new FileNameExtensionFilter("abbozza! (*.cpp)", "cpp"));
        if ( (lastSourceFile != null) && (lastSourceFile.isDirectory()) ) {
            chooser.setCurrentDirectory(lastSourceFile);            
        } else {
            chooser.setSelectedFile(lastSourceFile);
        }
        
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();                        
            lastSourceFile = file;
            saveCode(file);
        }
        */
    }//GEN-LAST:event_saveActionPerformed

    private void clearMenuActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearMenuActionPerformed
        this.consoleArea.setText("");
    }//GEN-LAST:event_clearMenuActionPerformed

    private void noneMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_noneMenuItemActionPerformed
        AbbozzaLogger.setLevel(AbbozzaLogger.NONE);
    }//GEN-LAST:event_noneMenuItemActionPerformed

    private void errMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_errMenuItemActionPerformed
        AbbozzaLogger.setLevel(AbbozzaLogger.ERROR);
    }//GEN-LAST:event_errMenuItemActionPerformed

    private void warnMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_warnMenuItemActionPerformed
        AbbozzaLogger.setLevel(AbbozzaLogger.WARNING);
    }//GEN-LAST:event_warnMenuItemActionPerformed

    private void infoMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_infoMenuItemActionPerformed
        AbbozzaLogger.setLevel(AbbozzaLogger.INFO);
    }//GEN-LAST:event_infoMenuItemActionPerformed

    private void debugMenuItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_debugMenuItemActionPerformed
        AbbozzaLogger.setLevel(AbbozzaLogger.DEBUG);
    }//GEN-LAST:event_debugMenuItemActionPerformed

    private void serialActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_serialActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        abbozza.monitorHandler.open();
    }//GEN-LAST:event_serialActionPerformed

    private void connectActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_connectActionPerformed
        AbbozzaCalliope abbozza = (AbbozzaCalliope) AbbozzaServer.getInstance();
        String boardPath = abbozza.findBoard();
        if ( boardPath.equals("") ) {
            File board = abbozza.queryPathToBoard(abbozza._pathToBoard);
            this.connectButton.setBackground(Color.red);
        } else {
            abbozza.setPathToBoard(boardPath);
            this.connectButton.setBackground(Color.green);
        }
    }//GEN-LAST:event_connectActionPerformed

    private void newActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_newActionPerformed
        if ( !checkSave() ) return;
        this.sourceArea.setText("/**\n *  Generated by abbozza!\n */\n\n" +
            "#include \"MicroBit.h\"\n" +
            "#include \"abbozzaTools.h\"\n" +
            "#include <string.h>\n\n\n" +
            "Abbozza abbozza;\n\n\n" +
            "/*\n"+
            " * Das Hauptprogramm\n" +
            " */\n" +
            "int main() {\n" + 
            "   abbozza.init();\n\n" +
            "   release_fiber();\n" +
            "}"
        );
    }//GEN-LAST:event_newActionPerformed

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenu abbozzaMenu;
    private javax.swing.JMenuItem clearMenuItem;
    private javax.swing.JButton compileButton;
    private javax.swing.JButton compileButton2;
    private javax.swing.JMenuItem compileMenuItem;
    private javax.swing.JButton connectButton;
    private javax.swing.JEditorPane consoleArea;
    private javax.swing.JScrollPane consolePane;
    private javax.swing.JRadioButtonMenuItem debugMenuItem;
    private javax.swing.JPanel editorPane;
    private javax.swing.JRadioButtonMenuItem errMenuItem;
    private javax.swing.JRadioButtonMenuItem infoMenuItem;
    private javax.swing.JMenuItem jMenuItem1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JPopupMenu.Separator jSeparator4;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JPopupMenu.Separator jSeparator6;
    private javax.swing.JToolBar.Separator jSeparator7;
    private javax.swing.JPopupMenu.Separator jSeparator8;
    private javax.swing.JTabbedPane jTabbedPane1;
    private javax.swing.JButton loadButton;
    private javax.swing.JMenuItem loadSketchMenuItem;
    private javax.swing.ButtonGroup logLevelGroup;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JEditorPane messageArea;
    private javax.swing.JScrollPane messagePane;
    private javax.swing.JMenuItem newMenuItem;
    private javax.swing.JRadioButtonMenuItem noneMenuItem;
    private javax.swing.JPopupMenu popupMenu;
    private javax.swing.JMenuItem quitItem;
    private javax.swing.JButton saveButton;
    private javax.swing.JMenuItem saveSketchMenuItem;
    private javax.swing.JButton serialButton;
    private javax.swing.JMenuItem settingsItem;
    private javax.swing.JMenu sketchMenu;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JMenuItem startBrowserItem;
    private javax.swing.JToolBar toolbar;
    private javax.swing.JButton uploadButton;
    private javax.swing.JMenuItem uploadMenuItem;
    private javax.swing.JRadioButtonMenuItem warnMenuItem;
    // End of variables declaration//GEN-END:variables

    public void loadCode(File file) {
        String code;
        try {
            byte[] bytes = Tools.readBytes(file);
            code = new String(bytes);
            setCode(code);
        } catch (IOException ex) {
        }
    }
    
    public void saveCode(File file) {
        String code = this.sourceArea.getText();
        try {
            file.createNewFile();
            PrintStream ps = new PrintStream(file);
            ps.print(code);
        } catch (Exception ex) {
        }
    }

    
    @Override
    public void logged(String msg) {
        appendConsoleText(msg + "\n");
    }
    
    public void setCode(String code) {
        this.sourceArea.setText(code);
    }

    @Override
    public void open() {
        GUITool.centerWindow(this);
        /* 
        Dimension dimension = Toolkit.getDefaultToolkit().getScreenSize();
        int x = (int) ((dimension.getWidth() - this.getWidth()) / 2);
        int y = (int) ((dimension.getHeight() - this.getHeight()) / 2);
        this.setLocation(x, y);
        */
        this.setVisible(true);
        this.setState(JFrame.ICONIFIED);
    }
    
    /**
    * Create a simple provider that adds some calliope related completions.
    */
   private CompletionProvider createCompletionProvider() {
      AbbozzaServer abbozza = AbbozzaServer.getInstance();
      
      DefaultCompletionProvider provider = new DefaultCompletionProvider();
      // provider.addCompletion(new BasicCompletion(provider, "abbozza.rgb.setColour"));
      // provider.addCompletion(new BasicCompletion(provider, "abbozza.soundmotor.soundOn"));

      
      try {
        InputStream autoCompleteXML = abbozza.getJarHandler().getInputStream("/lib/ac_" + abbozza.getSystem() + ".xml");
        provider.loadFromXML(autoCompleteXML);  
          AbbozzaLogger.out("Loaded autocomplete file: /lib/" + abbozza.getSystem() + "_ac.xml");
      } catch (IOException ex) {
          AbbozzaLogger.err("Could not load autocomplete file: /lib/" + abbozza.getSystem() + "_ac.xml");
      }
      return provider;
   }
   
   public void setConsoleText(String message) {
       sourceArea.removeAllLineHighlights();
       supplier.clear();
       // this.consoleArea.setText(parseConsoleText(message));      

       String msg = parseConsoleText(message);
       this.consoleArea.setText(message);
       this.messageArea.setText(msg);
   }
   
   public void appendConsoleText(String message) {
       String text = this.consoleArea.getText();
       String text2 = this.messageArea.getText();
       if ( text == null ) {
           text = "";
       }
       String msg = parseConsoleText(message);
       this.consoleArea.setText(text + message);
       this.messageArea.setText(text2 + msg);
   }
   
   
   private String parseConsoleText(String message) {
       String msg = "";
       Pattern errorPattern = Pattern.compile(".*/abbozza\\.cpp:([\\d]*):([\\d]*): error: (.*)");
       Pattern successPattern = Pattern.compile("Compilation successful");
       Matcher matcher = errorPattern.matcher(message);
       while ( matcher.find() ) {
           if ( msg == "" ) {
               msg = AbbozzaLocale.entry("msg.error_compiling");
           }
           try {
               msg = msg + AbbozzaLocale.entry("gui.error_in_line",matcher.group(1)) + matcher.group(2) + "\n";
               msg = msg + AbbozzaLocale.entry("gui.error_msg") + " : " + matcher.group(3) + "\n\n";
               int line = Integer.parseInt(matcher.group(1))-1;
               int start = sourceArea.getLineStartOffset(line);
               int end  = sourceArea.getLineEndOffset(line);
               sourceArea.addLineHighlight(line,new Color(255,200,200));
               supplier.addTooltip(line, matcher.group(3));
           } catch (BadLocationException ex) {
           }
       }
       matcher = successPattern.matcher(message);
       while ( matcher.find() ) {
           msg = msg + AbbozzaLocale.entry("msg.done_compiling");
       }

       return msg;
   }

    @Override
    public void insertUpdate(DocumentEvent e) {
        docChanged = true;
    }

    @Override
    public void removeUpdate(DocumentEvent e) {
        docChanged = true;
    }

    @Override
    public void changedUpdate(DocumentEvent e) {
        docChanged = true;
    }

    public boolean checkSave() {
        if ( !docChanged ) return true;
        int result = JOptionPane.showConfirmDialog(this, AbbozzaLocale.entry("msg.save_question"), "abbozza!", JOptionPane.YES_NO_CANCEL_OPTION);
        if ( result == JOptionPane.YES_OPTION) {
            // Shoud be saved
            boolean saved = saveSketch();
            if ( saved ) {
                docChanged = false;
                return true;
            }
        } else if ( result == JOptionPane.NO_OPTION) {
            // Do not save
            docChanged = false;
            return true;
        }
        return false;
    }
    
    public boolean saveSketch() {
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String path;
        try {
            path = ((lastSourceFile != null) ? lastSourceFile.getCanonicalPath() : abbozza.getSketchbookPath());
        } catch (IOException ex) {
            path = System.getProperty("user.home");
        }
        JFileChooser chooser = new JFileChooser(path);
        
        chooser.setFileFilter(new FileNameExtensionFilter("abbozza! (*.cpp)", "cpp"));
        if ( (lastSourceFile != null) && (lastSourceFile.isDirectory()) ) {
            chooser.setCurrentDirectory(lastSourceFile);            
        } else {
            chooser.setSelectedFile(lastSourceFile);
        }
        
        int choice = chooser.showOpenDialog(null);
        if (choice == JFileChooser.APPROVE_OPTION) {
            File file = chooser.getSelectedFile();                        
            lastSourceFile = file;
            saveCode(file);
            return true;
        }
        
        return false;
    }
    
}

