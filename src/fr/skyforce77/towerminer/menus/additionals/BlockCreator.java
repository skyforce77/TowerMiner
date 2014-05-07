package fr.skyforce77.towerminer.menus.additionals;

import java.awt.Dimension;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.io.File;

import javax.swing.BorderFactory;
import javax.swing.JButton;
import javax.swing.JCheckBox;
import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JLabel;
import javax.swing.JPanel;
import javax.swing.filechooser.FileFilter;

import fr.skyforce77.towerminer.TowerMiner;
import fr.skyforce77.towerminer.blocks.Blocks;
import fr.skyforce77.towerminer.maps.Maps;
import fr.skyforce77.towerminer.ressources.language.LanguageManager;

public class BlockCreator extends JFrame {

    private static final long serialVersionUID = 106616954329583133L;

    public Maps map;
    public Blocks b;
    public File[] fs = null;

    public BlockCreator(Maps m, final Blocks b) {
        map = m;
        this.b = b;
        setSize(800, 300);
        setVisible(true);
        setLocationRelativeTo(TowerMiner.game);
        setTitle(LanguageManager.getText("menu.editor.create.block"));
        JPanel panel = new JPanel();
        JPanel required = new JPanel();
        required.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Champs Obligatoires"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));
        JPanel options = new JPanel();
        options.setBorder(BorderFactory.createCompoundBorder(BorderFactory.createTitledBorder("Options"), BorderFactory.createEmptyBorder(5, 5, 5, 5)));

        final JNumberTextField id = new JNumberTextField();
        id.setToolTipText(LanguageManager.getText("menu.editor.id"));
        id.setPreferredSize(new Dimension(90, 20));
        id.setText(b.getId() + "");
        JLabel idlabel = new JLabel(LanguageManager.getText("menu.editor.id") + ": ");
        idlabel.setLabelFor(id);
        required.add(idlabel);
        required.add(id);

        JButton dtexture = new JButton();
        dtexture.setText(LanguageManager.getText("menu.editor.textures"));
        dtexture.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                JFileChooser fc = new JFileChooser();
                fc.setMultiSelectionEnabled(true);
                fc.setFileFilter(new FileFilter() {

                    @Override
                    public String getDescription() {
                        return LanguageManager.getText("menu.editor.file.filter.textures");
                    }

                    @Override
                    public boolean accept(File f) {
                        if (f.isDirectory()) {
                            return true;
                        }

                        String extension = getExtension(f);
                        if (extension != null && extension.equals("png")) {
                            return true;
                        } else {
                            return false;
                        }
                    }
                });
                fc.setAcceptAllFileFilterUsed(false);

                int access = fc.showOpenDialog(TowerMiner.game);
                if (access == JFileChooser.APPROVE_OPTION) {
                    fs = fc.getSelectedFiles();
                }
            }
        });
        required.add(dtexture);

        final JCheckBox liquid = new JCheckBox();
        liquid.setSelected(b.isLiquid());
        liquid.setText(LanguageManager.getText("menu.editor.block.liquid"));
        options.add(liquid);

        final JCheckBox overlay = new JCheckBox();
        overlay.setSelected(b.isOverlay());
        overlay.setText(LanguageManager.getText("menu.editor.block.translucent"));
        options.add(overlay);

        final JCheckBox coloradapt = new JCheckBox();
        coloradapt.setSelected(b.isMapAdapted(0));
        coloradapt.setText(LanguageManager.getText("menu.editor.block.color"));
        options.add(coloradapt);

        final JCheckBox canplace = new JCheckBox();
        canplace.setSelected(b.canPlaceTurretOn());
        canplace.setText(LanguageManager.getText("menu.editor.block.turrets"));
        options.add(canplace);

        panel.add(required);
        panel.add(options);

        JPanel buttons = new JPanel();
        JButton ok = new JButton();
        ok.setText(LanguageManager.getText("menu.editor.save"));
        ok.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                b.setAdaptColor(coloradapt.isSelected());
                b.setId(id.getNumber().intValue());
                b.setLiquid(liquid.isSelected());
                b.setOverlay(overlay.isSelected());
                b.setCanPlaceOn(canplace.isSelected());

                if (fs != null) {
                    if (fs.length >= 1) {
                        b.resetTextures();
                    }
                    for (File f : fs) {
                        int data = 0;
                        while (b.getRawTexture(data) != null) {
                            data = data + 1;
                            if (data == 16)
                                break;
                        }
                        b.setTexture(data, f);
                    }
                }

                Maps.getActualMap().addBlock(b);
                Blocks.addMapBlocks(Maps.getActualMap());
                setVisible(false);
                dispose();
            }
        });
        buttons.add(ok);

        JButton cancel = new JButton();
        cancel.setText(LanguageManager.getText("menu.back"));
        cancel.addActionListener(new ActionListener() {
            @Override
            public void actionPerformed(ActionEvent arg0) {
                setVisible(false);
                dispose();
            }
        });
        buttons.add(cancel);

        panel.add(buttons);
        add(panel);
    }

    public static String getExtension(File f) {
        String ext = null;
        String s = f.getName();
        int i = s.lastIndexOf('.');

        if (i > 0 && i < s.length() - 1) {
            ext = s.substring(i + 1).toLowerCase();
        }
        return ext;
    }

}
