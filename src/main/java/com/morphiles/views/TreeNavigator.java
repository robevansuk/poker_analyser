package com.morphiles.views;

import java.awt.Component;
import java.awt.Dimension;
import java.util.Enumeration;
import javax.swing.Icon;
import javax.swing.ImageIcon;
import javax.swing.JScrollPane;
import javax.swing.JTree;
import javax.swing.event.TreeSelectionEvent;
import javax.swing.event.TreeSelectionListener;
import javax.swing.tree.DefaultMutableTreeNode;
import javax.swing.tree.DefaultTreeCellRenderer;
import javax.swing.tree.DefaultTreeModel;
import javax.swing.tree.TreePath;

/**
 * Menu for displaying/navigating the hand histories in a more orderly fashion
 */
@org.springframework.stereotype.Component
public class TreeNavigator {

    private JScrollPane scrollPane;

    private static final DefaultMutableTreeNode rootNode = new DefaultMutableTreeNode("All");

    private static JTree tree;

    private static String CASH = "Cash/Ring";
    private static String TOURNAMENT = "Tournament";


    public TreeNavigator(){
        scrollPane = new JScrollPane(getNavigationTree());
        scrollPane.setPreferredSize(new Dimension(300, scrollPane.getHeight()));

        Icon partyIcon = new ImageIcon(this.getClass().getResource("/partypoker.png"));
        Icon starsIcon = new ImageIcon(this.getClass().getResource("/pokerstars.bmp"));

        if (partyIcon != null && starsIcon != null) {
            tree.setCellRenderer(new MyRenderer(partyIcon, starsIcon));
        }

        scrollPane.setVerticalScrollBarPolicy(JScrollPane.VERTICAL_SCROLLBAR_ALWAYS);
        tree.putClientProperty("JTree.lineStyle", "Horizontal");

        tree.addTreeSelectionListener(new TreeSelectionListener() {
            public void valueChanged(TreeSelectionEvent e) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) tree.getLastSelectedPathComponent();

                /* if nothing is selected */
                if (node == null) return;

                /* retrieve the node that was selected */
                String tabName = (String) node.getUserObject();
//                if (gui != null) {
//                    gui.setActiveTab(tabName);
//                }
            }
        });

    }

    public JTree getNavigationTree(){
        tree = new JTree(rootNode);
        addNodeTo("All", CASH);
        addNodeTo("All", TOURNAMENT);
        tree.setRootVisible(true);
        tree.setShowsRootHandles(true);
        return tree;
    }


    public void expandParentNode(DefaultMutableTreeNode node){
        tree.scrollPathToVisible(new TreePath(node.getPath()));
        //tree.makeVisible(new TreePath(rootNode.getPath()));
    }

    public JScrollPane getNavigationScrollPane(){
        return scrollPane;
    }

    /**
     * Adds a new node with the name supplied under the existing node named.
     * If the existing node name is not present it will be dynamically added on the fly.
     * @param nodeName
     * @param newNodeName
     */
    public void addNodeTo(String nodeName, String newNodeName){
        DefaultMutableTreeNode theNode = null;
        for (Enumeration e = rootNode.depthFirstEnumeration(); e.hasMoreElements() && theNode == null;) {
            DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
            if (((String)node.getUserObject()).equals(nodeName)) {
                theNode = node;
            }
        }

        if (theNode==null){
            // add the node
            String stakesLevelNodeName = nodeName.substring(nodeName.indexOf("_")+1);
            if (nodeName.contains("Ring") || nodeName.contains("Cash")) {
                addNodeTo(CASH, stakesLevelNodeName);
                addNodeTo(stakesLevelNodeName, newNodeName);
            } else if (nodeName.contains("Trny")){
                addNodeTo(TOURNAMENT, stakesLevelNodeName);
                addNodeTo(stakesLevelNodeName, newNodeName);
            } else if (nodeName.equals("Freeroll")){
                addNodeTo(TOURNAMENT, nodeName);
                addNodeTo(nodeName, newNodeName);
            }
        } else {

            DefaultMutableTreeNode nodeToAdd = new DefaultMutableTreeNode(newNodeName);

            DefaultMutableTreeNode foundNode = null;
            for (Enumeration e = theNode.children(); e.hasMoreElements() && foundNode == null;) {
                DefaultMutableTreeNode node = (DefaultMutableTreeNode) e.nextElement();
                if (((String)node.getUserObject()).equals(newNodeName)) {
                    foundNode = node;
                }
            }
            if (foundNode==null) {
                DefaultTreeModel model = (DefaultTreeModel) tree.getModel();
                model.insertNodeInto(nodeToAdd,theNode,theNode.getChildCount());
                expandParentNode(nodeToAdd);
            }
        }

        if (scrollPane != null){
            scrollPane.getHorizontalScrollBar().setValue(0);
            scrollPane.getVerticalScrollBar().setValue(scrollPane.getVerticalScrollBar().getValue());
        }
    }

    /**
     * Renderer for the tree nodes
     */
    class MyRenderer extends DefaultTreeCellRenderer {
        Icon partyIcon;
        Icon pokerStars;

        public MyRenderer(Icon partyIcon, Icon pokerStarsIcon) {
            this.partyIcon = partyIcon;
            pokerStars = pokerStarsIcon;
        }

        public Component getTreeCellRendererComponent(
                JTree tree,
                Object value,
                boolean sel,
                boolean expanded,
                boolean leaf,
                int row,
                boolean hasFocus) {

            super.getTreeCellRendererComponent(
                    tree, value, sel,
                    expanded, leaf, row,
                    hasFocus);
            if (leaf && isPokerStarsHistory(value)) {
                setIcon(pokerStars);
                setToolTipText("This is a poker stars hand history");
            } else if (leaf && isPartyPokerHistory(value)) {
                setIcon(partyIcon);
                setToolTipText(null); //no tool tip
            }

            return this;
        }

        protected boolean isPokerStarsHistory(Object value) {
            //DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

            return true;
        }
        protected boolean isPartyPokerHistory(Object value) {
            //DefaultMutableTreeNode node = (DefaultMutableTreeNode)value;

            return true;
        }

    }
}