package sujoo.games.spacegame.gui;

import java.awt.BorderLayout;
import java.awt.Component;
import java.awt.Dimension;
import java.awt.GridBagLayout;
import java.awt.GridBagConstraints;
import java.awt.Insets;
import java.awt.Paint;
import java.awt.Rectangle;
import java.awt.Shape;

import javax.swing.border.EmptyBorder;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.JTextPane;

import sujoo.games.spacegame.datatype.general.Star;
import sujoo.games.spacegame.datatype.player.Player;
import sujoo.games.spacegame.manager.GameManager;

import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;

import javax.swing.JTextField;

import java.awt.Color;
import java.util.List;

import javax.swing.border.EtchedBorder;

import org.apache.commons.collections15.Transformer;

import edu.uci.ics.jung.algorithms.layout.FRLayout;
import edu.uci.ics.jung.graph.UndirectedSparseGraph;
import edu.uci.ics.jung.visualization.VisualizationViewer;
import edu.uci.ics.jung.visualization.decorators.ToStringLabeller;
import edu.uci.ics.jung.visualization.renderers.Renderer.VertexLabel.Position;

public class MainGui extends JFrame {

    /**
	 * 
	 */
    private static MainGui guiInstance = null;

    private static final long serialVersionUID = -2672177212106851596L;
    private JPanel contentPane;
    private JPanel upperPanel;
    private JPanel lowerPanel;
    private JPanel infoPanel;
    private JTextField textField;
    private ScrollLog errorLog;
    private ScrollLog battleLog;

    private GameManager controller;
    private Player player;

    /**
     * Create the frame.
     */
    private MainGui(GameManager controller) {
        this.controller = controller;
        this.player = null;

        errorLog = new ScrollLog(new Dimension(350, 25));
        battleLog = new ScrollLog(new Dimension(350, 75));

        setResizable(false);
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setBounds(0, 0, 350, 500);
        setLocationRelativeTo(null);
        contentPane = new JPanel();
        contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
        setContentPane(contentPane);
        GridBagLayout gbl_contentPane = new GridBagLayout();
        gbl_contentPane.columnWidths = new int[] { 350 };
        gbl_contentPane.rowHeights = new int[] { 100, 100, 0, 10 };
        gbl_contentPane.columnWeights = new double[] {};
        gbl_contentPane.rowWeights = new double[] {};
        contentPane.setLayout(gbl_contentPane);

        upperPanel = new JPanel();
        upperPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
        GridBagConstraints gbc_upperPanel = new GridBagConstraints();
        gbc_upperPanel.anchor = GridBagConstraints.NORTH;
        gbc_upperPanel.fill = GridBagConstraints.BOTH;
        gbc_upperPanel.insets = new Insets(0, 0, 5, 0);
        gbc_upperPanel.gridx = 0;
        gbc_upperPanel.gridy = 0;
        contentPane.add(upperPanel, gbc_upperPanel);
        upperPanel.setLayout(new BorderLayout(0, 0));

        lowerPanel = new JPanel();
        lowerPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
        GridBagConstraints gbc_lowerPanel = new GridBagConstraints();
        gbc_lowerPanel.insets = new Insets(0, 0, 5, 0);
        gbc_lowerPanel.fill = GridBagConstraints.BOTH;
        gbc_lowerPanel.gridx = 0;
        gbc_lowerPanel.gridy = 1;
        contentPane.add(lowerPanel, gbc_lowerPanel);
        lowerPanel.setLayout(new BorderLayout(0, 0));

        infoPanel = new JPanel();
        infoPanel.setBorder(new EtchedBorder(EtchedBorder.RAISED, Color.DARK_GRAY, Color.LIGHT_GRAY));
        infoPanel.setBackground(Colors.defaultBackgroundColor);
        GridBagConstraints gbc_infoPanel = new GridBagConstraints();
        gbc_infoPanel.insets = new Insets(0, 0, 5, 0);
        gbc_infoPanel.fill = GridBagConstraints.BOTH;
        gbc_infoPanel.gridx = 0;
        gbc_infoPanel.gridy = 2;
        contentPane.add(infoPanel, gbc_infoPanel);
        infoPanel.setLayout(new BorderLayout(0, 0));

        textField = new JTextField();
        textField.setBackground(Colors.defaultBackgroundColor);
        textField.setForeground(Colors.defaultTextColor);
        textField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyPressed(KeyEvent e) {
                if (e.getKeyCode() == KeyEvent.VK_ENTER) {
                    enterCommand(textField.getText());
                }
            }
        });
        GridBagConstraints gbc_textField = new GridBagConstraints();
        gbc_textField.fill = GridBagConstraints.BOTH;
        gbc_textField.gridx = 0;
        gbc_textField.gridy = 3;
        contentPane.add(textField, gbc_textField);
    }

    public static MainGui getInstance(GameManager gameManager, Player player) {
        if (guiInstance == null) {
            guiInstance = new MainGui(gameManager);
        }
        guiInstance.setPlayer(player);
        return guiInstance;
    }

    private void enterCommand(String text) {
        textField.setText("");
        removeLog();
        controller.enterCommand(text, player);
    }

    public void displayLoss() {
        setUpperPanel(TextGuiGenerator.getLoseUpperPanel());
        setLowerPanel(TextGuiGenerator.getLoseLowerPanel());
    }

    public void displayError(String errorMessage) {
        useErrorLog(errorMessage);
    }
    
    public void displayBattleFeedback(BattleFeedbackEnum feedback) {
        if (feedback != null) {
            useBattleLog(feedback.getCode());
        }
    }

    public void displayScanSystem(Player player, String connections, List<Player> players) {
        setLowerPanel(TextGuiGenerator.getScanSystemLowerPanel(player.getCurrentStar(), connections, players));
    }

    public void displayScanPlayer(Player player) {
        setUpperPanel(TextGuiGenerator.getScanPlayerUpperPanel(player));
        setLowerPanel(TextGuiGenerator.getScanPlayerLowerPanel(player));
    }

    public void displayStatus(Player player) {
        setUpperPanel(TextGuiGenerator.getStatusUpperPanel(player));
        setLowerPanel(TextGuiGenerator.getStatusLowerPanel(player));
    }

    public void displayDockCargo(Player player) {
        setUpperPanel(TextGuiGenerator.getDockUpperPanel(player.getCurrentStar().getStation()));
        setLowerPanel(TextGuiGenerator.getDockLowerPanel(player));
    }
    
    public void displayDockStore(Player player) {
        setUpperPanel(TextGuiGenerator.getDockStoreUpperPanel(player.getCurrentStar().getStation()));
        setLowerPanel(TextGuiGenerator.getDockStoreLowerPanel(player));
    }

    public void displayScore(List<Player> players) {
        setLowerPanel(TextGuiGenerator.getScoreLowerPanel(players));
    }

    public void displayHelp(String code, List<String> listOfCommands, List<String> commandExplanation) {
        setUpperPanel(TextGuiGenerator.getHelpUpperPanel(listOfCommands));
        setLowerPanel(TextGuiGenerator.getHelpLowerPanel(code, commandExplanation));
    }

    public void displayBattle(Player user, Player other) {
        setUpperPanel(TextGuiGenerator.getScanPlayerUpperPanel(other));
        setLowerPanel(TextGuiGenerator.getStatusUpperPanel(user));
    }

    public void loadSystemMap(UndirectedSparseGraph<Star, String> graph, final Star currentStar, final Star previousStar) {
        FRLayout<Star, String> layout = new FRLayout<Star, String>(graph);
        layout.setMaxIterations(10000);
        layout.setRepulsionMultiplier(1.5);
        VisualizationViewer<Star, String> vs =
                new VisualizationViewer<Star, String>(layout, new Dimension(300, 200));
        vs.setPreferredSize(new Dimension(320, 250));
        vs.setBackground(Colors.defaultBackgroundColor);

        Transformer<Star, Paint> vertexPaint = new Transformer<Star, Paint>() {
            public Paint transform(Star star) {
                if (star.equals(currentStar)) {
                    return Colors.defaultTextColor;
                } else if (star.equals(previousStar)) {
                    return Color.LIGHT_GRAY;
                } else {
                    return Colors.paleGreen;
                }
            }
        };
        
        Transformer<Star, Paint> vertexPaint2 = new Transformer<Star, Paint>() {
            public Paint transform(Star star) {
                return Color.LIGHT_GRAY;
            }
        };
        
        Transformer<String, Paint> edgePaint = new Transformer<String, Paint>() {
            public Paint transform(String star) {
                return Colors.defaultTextColor;
            }
        };

        final Shape shape = new Rectangle(35, 20);
        Transformer<Star, Shape> vertexShapeTransformer = new Transformer<Star, Shape>() {
            public Shape transform(Star s) {
                return shape;
            }
        };

        vs.getRenderContext().setVertexFillPaintTransformer(vertexPaint);
        vs.getRenderContext().setVertexDrawPaintTransformer(vertexPaint2);
        vs.getRenderContext().setEdgeDrawPaintTransformer(edgePaint);
        vs.getRenderContext().setVertexLabelTransformer(new ToStringLabeller<Star>());
        vs.getRenderContext().setVertexShapeTransformer(vertexShapeTransformer);
        vs.getRenderer().getVertexLabelRenderer().setPosition(Position.CNTR);

        setUpperPanel(vs);
    }

    private void setPlayer(Player player) {
        this.player = player;
    }

    private void setLowerPanel(STextArea textArea) {
        JTextPane textPane = getStandardTextPane(textArea);
        setLowerPanel(textPane);
    }

    private void setUpperPanel(STextArea textArea) {
        JTextPane textPane = getStandardTextPane(textArea);
        setUpperPanel(textPane);
    }

    private JTextPane getStandardTextPane(STextArea textArea) {
        JTextPane textPane = new JTextPane(textArea);
        textPane.setEditable(false);
        textPane.setBackground(Colors.defaultBackgroundColor);
        return textPane;
    }

    private void setLowerPanel(Component panel) {
        lowerPanel.removeAll();
        lowerPanel.add(panel, BorderLayout.CENTER);
        redraw();
    }

    private void setUpperPanel(Component panel) {
        upperPanel.removeAll();
        upperPanel.add(panel, BorderLayout.CENTER);
        redraw();
    }
    
    private void useErrorLog(String text) {
        errorLog.addText(text);
        infoPanel.removeAll();
        infoPanel.add(errorLog, BorderLayout.CENTER);
        redraw();
    }
    
    private void useBattleLog(String text) {
        battleLog.addText(text);
        infoPanel.removeAll();
        infoPanel.add(battleLog, BorderLayout.CENTER);
        redraw();
    }
    
    private void removeLog() {
        infoPanel.removeAll();
    }

    private void redraw() {
        pack();
        textField.requestFocusInWindow();
    }
}
