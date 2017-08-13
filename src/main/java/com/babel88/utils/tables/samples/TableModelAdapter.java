package com.babel88.utils.tables.samples;

import com.babel88.asciiart.raster.CharacterRaster;
import com.babel88.asciiart.raster.ExtensibleCharacterRaster;
import com.babel88.asciiart.raster.RasterContext;
import com.babel88.asciiart.widget.TableWidget;
import com.babel88.asciiart.widget.model.AbstractTableModel;
import com.babel88.asciiart.widget.model.TableModel;
import com.babel88.asciiart.widget.model.TableModelCollectionAdapter;
import com.babel88.asciiart.widget.model.TableModelMapAdapter;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.TreeMap;

/**
 * Created by edwin.njeru on 8/13/17.
 */
public class TableModelAdapter {

    public TableModelAdapter() {
    }

    public static void main(String[] args) {
        List items = new ArrayList();
        items.add("Orange");
        items.add("Apple");
        items.add("Lemon");
        TableModel tableModel = new TableModelCollectionAdapter(items, "Fruit");
        TableWidget tableWidget = new TableWidget(tableModel);
        CharacterRaster raster = new ExtensibleCharacterRaster(' ');
        tableWidget.render(new RasterContext(raster));
        System.out.println(raster);
        Map mapItems = new TreeMap();
        mapItems.put("Paris", Double.valueOf(42.0D));
        mapItems.put("London", Double.valueOf(2012.0D));
        mapItems.put("Amsterdam", Double.valueOf(3.1415D));
        TableModel mapModel = new TableModelMapAdapter(mapItems, "CITY", "VALUE");
        TableWidget mapWidget = new TableWidget(mapModel);
        CharacterRaster mapRaster = new ExtensibleCharacterRaster(' ');
        mapWidget.render(new RasterContext(mapRaster));
        System.out.println(mapRaster);
        TableWidget customWidget =
                new TableWidget(new com.babel88.utils.tables.samples.TableModelAdapter.CustomTableModelImpl());
        CharacterRaster customRaster = new ExtensibleCharacterRaster(' ');
        customWidget.render(new RasterContext(customRaster));
        System.out.println(customRaster);
    }

    private static class CustomTableModelImpl extends AbstractTableModel implements TableModel {
        private CustomTableModelImpl() {
        }

        public int getWidth() {
            return 4;
        }

        public int getHeight() {
            return 6;
        }

        public String getCellContent(int x, int y) {
            return String.format("%02d:%02d", new Object[]{Integer.valueOf(x), Integer.valueOf(y)});
        }

        public String getColumnTitle(int x) {
            return "Column " + x;
        }
    }
}
