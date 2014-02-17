/* 
 * Copyright (c) 2007 Wayne Meissner
 * 
 * This file is part of gstreamer-java.
 *
 * This code is free software: you can redistribute it and/or modify it under
 * the terms of the GNU Lesser General Public License version 3 only, as
 * published by the Free Software Foundation.
 *
 * This code is distributed in the hope that it will be useful, but WITHOUT
 * ANY WARRANTY; without even the implied warranty of MERCHANTABILITY or
 * FITNESS FOR A PARTICULAR PURPOSE.  See the GNU Lesser General Public License
 * version 3 for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * version 3 along with this work.  If not, see <http://www.gnu.org/licenses/>.
 */

package org.gstreamer.swing;

import java.awt.Dimension;
import java.awt.Point;
import java.awt.event.ActionEvent;
import java.net.URL;

import javax.swing.AbstractAction;
import javax.swing.Action;
import javax.swing.BoundedRangeModel;
import javax.swing.BoxLayout;
import javax.swing.DefaultBoundedRangeModel;
import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.JSlider;
import javax.swing.JToggleButton;
import javax.swing.Popup;
import javax.swing.PopupFactory;
import javax.swing.SwingConstants;
import javax.swing.SwingUtilities;
import javax.swing.UIManager;
import javax.swing.event.ChangeEvent;
import javax.swing.event.ChangeListener;

import org.gstreamer.elements.PlayBin;

/**
 *
 */
public class PopupVolumeButton extends JToggleButton {

    private static final long serialVersionUID = 3957280083515502726L;

    private JPanel volumePanel;
    private JSlider volumeSlider;
    private ImageIcon lowVolumeIcon = loadIcon("status/audio-volume-low");
    private ImageIcon medVolumeIcon = loadIcon("status/audio-volume-medium");
    private ImageIcon highVolumeIcon = loadIcon("status/audio-volume-high");
    
    public PopupVolumeButton(BoundedRangeModel model) {
        /*
         * Construct the popup for the volume slider
         */
        volumePanel = new JPanel();
        volumePanel.setLayout(new BoxLayout(volumePanel, BoxLayout.Y_AXIS));
        
        volumeSlider = new JSlider(model);
        volumeSlider.addChangeListener(volumeChanged);
        volumeSlider.setOrientation(SwingConstants.VERTICAL);
        volumePanel.add(new JLabel(highVolumeIcon));
        volumeSlider.setAlignmentX(0.25f);
        volumePanel.add(volumeSlider);
        volumePanel.add(new JLabel(lowVolumeIcon));
        volumePanel.validate();
        setAction(volumeAction);
    }
    public PopupVolumeButton(final PlayBin playbin) {
        this(new DefaultBoundedRangeModel() {

            private static final long serialVersionUID = 1462054216375236024L;

            @Override
            public int getMaximum() {
                return 100;
            }

            @Override
            public int getMinimum() {
                return 0;
            }

            @Override
            public int getValue() {
                return (int) ((playbin.getVolume() * 100.0) + 0.5);
            }

            @Override
            public void setValue(int percent) {
                playbin.setVolume(Math.max(Math.min((double) percent, 100d), 0d) / 100.0d);
            }
        });
    }
    
    private ChangeListener volumeChanged = new ChangeListener() {
        public void stateChanged(ChangeEvent e) {
            JSlider s = (JSlider) e.getSource();
            if (s.getValue() < 33) {
                volumeAction.putValue(Action.SMALL_ICON, lowVolumeIcon);
            } else if (s.getValue() < 66) {
                volumeAction.putValue(Action.SMALL_ICON, medVolumeIcon);
            } else {
                volumeAction.putValue(Action.SMALL_ICON, highVolumeIcon);
            }
        }
    };
    Popup volumePopup;
    private AbstractAction volumeAction = new AbstractAction("", loadIcon("status/audio-volume-medium")) {

        private static final long serialVersionUID = 3977109421439093963L;

        public void actionPerformed(ActionEvent e) {
            JToggleButton b = (JToggleButton) e.getSource();
            if (!b.isSelected() && volumePopup != null) {
                volumePopup.hide();
                volumePopup = null;
            } else {
                Dimension panelSize = volumePanel.getPreferredSize();
                // Right-align it with the volume button, so it pops up just above it
                Point location = new Point(0 - panelSize.width + getPreferredSize().width,
                        0 - panelSize.height);
                SwingUtilities.convertPointToScreen(location, PopupVolumeButton.this);
                volumePopup = PopupFactory.getSharedInstance().getPopup(PopupVolumeButton.this,
                        volumePanel, location.x, location.y);
                // Remove the slider value from the top of the slider
                Object paintValue = UIManager.put("Slider.paintValue", Boolean.FALSE);
                volumePopup.show();
                UIManager.put("Slider.paintValue", paintValue);
            }
        }
    };
    
    private static ImageIcon loadIcon(String name) {
        return loadIcon(16, name);
    }
    private static ImageIcon loadIcon(int size, String name) {
        String path = "/org/freedesktop/tango/" + size + "x" + size + "/" + name + ".png";
        URL url = PopupVolumeButton.class.getResource(path);
        if (url != null) {
            return new ImageIcon(url);
        } else {
            throw new RuntimeException("Cannot locate icon for " + name);
        }
    }
}
