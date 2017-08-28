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
import de.uos.inf.did.abbozza.install.InstallTool;
import java.awt.Color;
import java.awt.Dimension;
import java.awt.Font;
import java.awt.Toolkit;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.PrintStream;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JDialog;
import javax.swing.UIManager;
import javax.swing.filechooser.FileNameExtensionFilter;
import javax.swing.text.BadLocationException;
import javax.swing.text.DefaultCaret;
import javax.swing.text.DefaultStyledDocument;
import javax.swing.text.Highlighter;
/**
 *
 * @author mbrinkmeier
 */
public class AbbozzaCalliopeFrame  extends javax.swing.JFrame implements AbbozzaLoggerListener, AbbozzaCalliopeGUI {

    private PrintStream _console;
    private RTextScrollPane sourcePanel;
    private RSyntaxTextArea sourceArea;
    private Highlighter sourceHighlighter;
    private AbbozzaCalliopeTooltipSupplier supplier;
    private DefaultStyledDocument consoleDoc;
    private File lastSourceFile = null;
    /**
     * Creates new form AbbozzaCalliopeFrame
     */
    public AbbozzaCalliopeFrame() {
        
        /* try {
            UIManager.setLookAndFeel("com.sun.java.swing.plaf.motif.MetalLookAndFeel");
        } catch (ClassNotFoundException ex) {
        } catch (InstantiationException ex) {
        } catch (IllegalAccessException ex) {
        } catch (UnsupportedLookAndFeelException ex) {
        } */
        
        Font f = new Font("sans-serif", Font.PLAIN, 12);
        UIManager.put("Menu.font", f);
        
        initComponents();
        
        sourceArea = new RSyntaxTextArea(50, 120);
        sourceArea.setSyntaxEditingStyle(SyntaxConstants.SYNTAX_STYLE_CPLUSPLUS);
        sourceArea.setCodeFoldingEnabled(true);        
        sourceArea.setTabSize(3);
        supplier = new AbbozzaCalliopeTooltipSupplier();
        sourceArea.setToolTipSupplier(supplier);
        
        sourceHighlighter = sourceArea.getHighlighter();

        sourcePanel = new RTextScrollPane(sourceArea);
        // this.splitPane.setLeftComponent(sourcePanel);
        editorPane.add(sourcePanel, java.awt.BorderLayout.CENTER);

        // consoleDoc =  new DefaultStyledDocument();
        // consoleArea.setDocument(consoleDoc);
        DefaultCaret caret = (DefaultCaret) consoleArea.getCaret();
        caret.setUpdatePolicy(DefaultCaret.ALWAYS_UPDATE);        

        // javax.swing.text.html.HTMLEditorKit eKit;
        // eKit = new javax.swing.text.html.HTMLEditorKit();
        // consoleArea.setEditorKit(eKit);
        
        CompletionProvider provider = createCompletionProvider();
        AutoCompletion ac = new AutoCompletion(provider);
        ac.install(sourceArea);
        ac.setAutoActivationDelay(500);
        ac.setAutoActivationEnabled(true);        

        pack();
        
        AbbozzaLogger.addListener(this);
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
        splitPane = new javax.swing.JSplitPane();
        editorPane = new javax.swing.JPanel();
        toolbar = new javax.swing.JToolBar();
        compileButton = new javax.swing.JButton();
        jButton1 = new javax.swing.JButton();
        jSeparator5 = new javax.swing.JToolBar.Separator();
        compileButton1 = new javax.swing.JButton();
        logPane = new javax.swing.JScrollPane();
        consoleArea = new javax.swing.JEditorPane();
        menuBar = new javax.swing.JMenuBar();
        jMenu1 = new javax.swing.JMenu();
        startBrowserItem = new javax.swing.JMenuItem();
        jSeparator2 = new javax.swing.JPopupMenu.Separator();
        clearItem = new javax.swing.JMenuItem();
        jSeparator1 = new javax.swing.JPopupMenu.Separator();
        settingsItem = new javax.swing.JMenuItem();
        jSeparator3 = new javax.swing.JPopupMenu.Separator();
        quitItem = new javax.swing.JMenuItem();
        sketchMenu = new javax.swing.JMenu();
        compileMenuItem = new javax.swing.JMenuItem();
        uploadMenuItem = new javax.swing.JMenuItem();

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

        setDefaultCloseOperation(javax.swing.WindowConstants.EXIT_ON_CLOSE);
        setTitle("abbozza! Calliope");

        splitPane.setDividerLocation(500);
        splitPane.setOrientation(javax.swing.JSplitPane.VERTICAL_SPLIT);

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

        jButton1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        jButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/upload.png"))); // NOI18N
        jButton1.setToolTipText(AbbozzaLocale.entry("gui.upload"));
        jButton1.setFocusable(false);
        jButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        jButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        jButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadActionPerformed(evt);
            }
        });
        toolbar.add(jButton1);
        toolbar.add(jSeparator5);

        compileButton1.setFont(new java.awt.Font("Dialog", 0, 12)); // NOI18N
        compileButton1.setIcon(new javax.swing.ImageIcon(getClass().getResource("/de/uos/inf/did/abbozza/calliope/icons/load.png"))); // NOI18N
        compileButton1.setToolTipText(AbbozzaLocale.entry("gui.load"));
        compileButton1.setFocusable(false);
        compileButton1.setHorizontalTextPosition(javax.swing.SwingConstants.CENTER);
        compileButton1.setVerticalTextPosition(javax.swing.SwingConstants.BOTTOM);
        compileButton1.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                loadActionPerformed(evt);
            }
        });
        toolbar.add(compileButton1);

        editorPane.add(toolbar, java.awt.BorderLayout.NORTH);

        splitPane.setLeftComponent(editorPane);

        consoleArea.setEditable(false);
        consoleArea.setPreferredSize(new java.awt.Dimension(106, 10));
        logPane.setViewportView(consoleArea);

        splitPane.setRightComponent(logPane);

        jMenu1.setText("abbozza!");

        startBrowserItem.setText(AbbozzaLocale.entry("gui.startBrowser"));
        startBrowserItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                startBrowserItemActionPerformed(evt);
            }
        });
        jMenu1.add(startBrowserItem);
        jMenu1.add(jSeparator2);

        clearItem.setText(AbbozzaLocale.entry("gui.clear"));
        clearItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                clearItemActionPerformed(evt);
            }
        });
        jMenu1.add(clearItem);
        jMenu1.add(jSeparator1);

        settingsItem.setText(AbbozzaLocale.entry("gui.settings"));
        settingsItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                settingsItemActionPerformed(evt);
            }
        });
        jMenu1.add(settingsItem);
        jMenu1.add(jSeparator3);

        quitItem.setText(AbbozzaLocale.entry("gui.quit")
        );
        quitItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                quitItemActionPerformed(evt);
            }
        });
        jMenu1.add(quitItem);

        menuBar.add(jMenu1);

        sketchMenu.setText(AbbozzaLocale.entry("gui.sketch_menu"));
        sketchMenu.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });

        compileMenuItem.setText(AbbozzaLocale.entry("gui.compile"));
        compileMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                compileActionPerformed(evt);
            }
        });
        sketchMenu.add(compileMenuItem);

        uploadMenuItem.setText(AbbozzaLocale.entry("gui.upload"));
        uploadMenuItem.addActionListener(new java.awt.event.ActionListener() {
            public void actionPerformed(java.awt.event.ActionEvent evt) {
                uploadActionPerformed(evt);
            }
        });
        sketchMenu.add(uploadMenuItem);

        menuBar.add(sketchMenu);

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

    private void clearItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_clearItemActionPerformed
        setConsoleText("");
    }//GEN-LAST:event_clearItemActionPerformed

    private void startBrowserItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_startBrowserItemActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        abbozza.startBrowser(abbozza.getSystem()+".html");
    }//GEN-LAST:event_startBrowserItemActionPerformed

    private void uploadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_uploadActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String response = abbozza.uploadCode(this.sourceArea.getText());
        if ( response != null ) {
            if ( response.equals("")) {
                response = AbbozzaLocale.entry("gui.compilation_success");
            }
            setConsoleText(response);
        }
    }//GEN-LAST:event_uploadActionPerformed

    private void settingsItemActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_settingsItemActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        abbozza.openConfigDialog();
    }//GEN-LAST:event_settingsItemActionPerformed

    private void compileActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_compileActionPerformed
        AbbozzaServer abbozza = AbbozzaServer.getInstance();
        String response = abbozza.compileCode(this.sourceArea.getText());
        if ( response != null ) {
            if ( response.equals("")) {
                response = AbbozzaLocale.entry("gui.compilation_success");
            }
            setConsoleText(response);
        }
    }//GEN-LAST:event_compileActionPerformed

    private void loadActionPerformed(java.awt.event.ActionEvent evt) {//GEN-FIRST:event_loadActionPerformed
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
    }//GEN-LAST:event_saveActionPerformed

 
    // Variables declaration - do not modify//GEN-BEGIN:variables
    private javax.swing.JMenuItem clearItem;
    private javax.swing.JButton compileButton;
    private javax.swing.JButton compileButton1;
    private javax.swing.JButton compileButton2;
    private javax.swing.JMenuItem compileMenuItem;
    private javax.swing.JEditorPane consoleArea;
    private javax.swing.JPanel editorPane;
    private javax.swing.JButton jButton1;
    private javax.swing.JMenu jMenu1;
    private javax.swing.JPopupMenu.Separator jSeparator1;
    private javax.swing.JPopupMenu.Separator jSeparator2;
    private javax.swing.JPopupMenu.Separator jSeparator3;
    private javax.swing.JToolBar.Separator jSeparator5;
    private javax.swing.JScrollPane logPane;
    private javax.swing.JMenuBar menuBar;
    private javax.swing.JMenuItem quitItem;
    private javax.swing.JMenuItem settingsItem;
    private javax.swing.JMenu sketchMenu;
    private javax.swing.JSplitPane splitPane;
    private javax.swing.JMenuItem startBrowserItem;
    private javax.swing.JToolBar toolbar;
    private javax.swing.JMenuItem uploadMenuItem;
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
        Tools.centerWindow(this);
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
   
   private void setConsoleText(String message) {
       sourceArea.removeAllLineHighlights();
       supplier.clear();
       // this.consoleArea.setText(parseConsoleText(message));      

       String msg = parseConsoleText(message);
       this.consoleArea.setText(msg);
   }
   
   private void appendConsoleText(String message) {
       String text = this.consoleArea.getText();
       if ( text == null ) {
           text = "";
       }
       String msg = parseConsoleText(message);
       this.consoleArea.setText(text + msg);
   }
   
   
   private String parseConsoleText(String message) {
       String msg = "";
       Pattern errorPattern = Pattern.compile(".*/abbozza\\.cpp:([\\d]*):([\\d]*): error: (.*)");
       Matcher matcher = errorPattern.matcher(message);
       while ( matcher.find() ) {
           try {
               if ( msg.equals("") ) {
                   msg = "\n\nFehlermeldungen: \n";
               }
               msg = msg + "Fehler in Zeile " + matcher.group(1) + " bei Zeichen " + matcher.group(2) + "\n";
               msg = msg + "Meldung: " + matcher.group(3) + "\n\n";
               int line = Integer.parseInt(matcher.group(1))-1;
               int start = sourceArea.getLineStartOffset(line);
               int end  = sourceArea.getLineEndOffset(line);
               sourceArea.addLineHighlight(line,new Color(255,200,200));
               supplier.addTooltip(line, matcher.group(3));
           } catch (BadLocationException ex) {
           }
       }

       return message + msg;
   }

}
