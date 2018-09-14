package com.fwcd.whiteboard.core;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JComponent;
import javax.swing.JPanel;

import com.fwcd.fructose.swing.View;
import com.fwcd.sketch.model.BrushProperties;
import com.fwcd.sketch.model.SketchBoardModel;
import com.fwcd.sketch.view.canvas.SketchBoardView;
import com.fwcd.sketch.view.tools.SketchTool;
import com.fwcd.whiteboard.ui.ScriptPanel;
import com.fwcd.whiteboard.ui.SidePanel;
import com.fwcd.whiteboard.ui.WMenuBar;

public class WhiteboardView implements View {
	private JComponent component;
	
	private SketchBoardView drawBoard;
	private SidePanel toolBar;
	private WMenuBar menuBar;
	private ScriptPanel scriptPanel;
	
	/**
	 * Creates a new local Whiteboard instance.
	 * 
	 * @param version - The version of Whiteboard
	 */
	public WhiteboardView() {
		component = new JPanel();
		component.setBackground(Color.WHITE);
		component.setLayout(new BorderLayout());
		
		drawBoard = new SketchBoardView(new SketchBoardModel());
		component.add(drawBoard.getComponent(), BorderLayout.CENTER);
		
		toolBar = new SidePanel(this, /* horizontal */ false);
		component.add(toolBar.getComponent(), BorderLayout.WEST);
		
		scriptPanel = new ScriptPanel(drawBoard);
		component.add(scriptPanel.getComponent(), BorderLayout.EAST);
		
		menuBar = new WMenuBar(this);
		component.add(menuBar.getComponent(), BorderLayout.NORTH);
		
		component.setVisible(true);
	}
	
	public SketchBoardView getDrawBoard() {
		return drawBoard;
	}
	
	public ScriptPanel getScriptPanel() {
		return scriptPanel;
	}
	
	public void setSelectedTool(SketchTool tool) {
		drawBoard.selectTool(tool);
		component.repaint();
	}
	
	public SketchTool getSelectedTool() {
		return drawBoard.getSelectedTool();
	}
	
	@Override
	public JComponent getComponent() {
		return component;
	}

	public BrushProperties getBrushProperties() {
		return drawBoard.getBrushProperties();
	}

	public void setBackground(Color color) {
		drawBoard.getComponent().setBackground(color);
		component.repaint();
	}

	public Color getBackground() {
		return drawBoard.getComponent().getBackground();
	}
}