package com.babel88.paycal.view.tables;

/**
 * Created by edwin.njeru on 10/07/2017.
 */
import com.google.common.base.Objects;
import com.google.common.base.Strings;
import com.babel88.paycal.api.view.Tables;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.HashMap;
import java.util.Map;

public class TableMaker implements Tables {

    private final Logger log = LoggerFactory.getLogger(TableMaker.class);

    Map<Index, String> _strings = new HashMap();
    Map<Integer, Integer> _columSizes = new HashMap();
    int _numRows = 0;
    int _numColumns = 0;
    private static Tables instance = new TableMaker();

    public TableMaker() {

        log.debug("Creating a new TableMaker object");
    }

    public static Tables getInstance() {
        return instance;
    }

    @Override
    public void addString(int row, int colum, String content) {
        this._numRows = Math.max(this._numRows, row + 1);
        this._numColumns = Math.max(this._numColumns, colum + 1);
        Index index = new Index(row, colum);
        this._strings.put(index, content);
        this.setMaxColumnSize(colum, content);
    }

    private void setMaxColumnSize(int colum, String content) {
        int size = content.length();
        Integer currentSize = (Integer)this._columSizes.get(Integer.valueOf(colum));
        if(currentSize == null || currentSize != null && currentSize.intValue() < size) {
            this._columSizes.put(Integer.valueOf(colum), Integer.valueOf(size));
        }

    }

    public int getColumSize(int colum) {
        Integer size = (Integer)this._columSizes.get(Integer.valueOf(colum));
        return size == null?0:size.intValue();
    }

    public String getString(int row, int colum) {
        Index index = new Index(row, colum);
        String string = (String)this._strings.get(index);
        return string == null?"":string;
    }

    public String getTableAsString(int padding) {
        String out = "";

        for(int r = 0; r < this._numRows; ++r) {
            for(int c = 0; c < this._numColumns; ++c) {
                int columSize = this.getColumSize(c);
                String content = this.getString(r, c);
                int pad = c == this._numColumns - 1?0:padding;
                out = out + Strings.padEnd(content, columSize + pad, ' ');
            }

            if(r < this._numRows - 1) {
                out = out + "\n";
            }
        }

        return out;
    }

    @Override
    public String toString() {
        return this.getTableAsString(1);
    }

    private class Index {
        int _row;
        int _colum;

        public Index(int r, int c) {
            this._row = r;
            this._colum = c;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Index index = (Index) o;
            return _row == index._row &&
                    _colum == index._colum;
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(_row, _colum);
        }
    }
}

