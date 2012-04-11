package com.mooo.sfwine;

import java.util.Vector;

import javax.swing.table.AbstractTableModel;

public class CardTableModel extends AbstractTableModel {

    private static final long serialVersionUID = -7495940408592595397L;

    private Vector<Vector<Object>> content = null;

    private String[] columnName = { "序号", "课程名称", "全称", "简称", "代码", "备注", "优先安排" };
    private String[] columnDb = { "id", "courseName", "fullName", "shortName", "code", "remark", "priority" };

    public CardTableModel() {
        content = new Vector<Vector<Object>>();
    }

    public CardTableModel(int count) {
        content = new Vector<Vector<Object>>(count);
    }

    
    public Vector<Vector<Object>> getContent() {
		return content;
	}

	public void setContent(Vector<Vector<Object>> content) {
		this.content = content;
	}

	public String[] getColumnDb() {
		return columnDb;
	}

	public void setColumnDb(String[] columnDb) {
		this.columnDb = columnDb;
	}

	public void addRow(String courseName,String fullName,String shortName, String code, String remark,Integer priority) {
        Vector<Object> v = new Vector<Object>();
        v.add(0, new Integer(content.size()));
        v.add(1, courseName);
        v.add(2, fullName);
        v.add(3, shortName);
        v.add(4, code);
        v.add(5, remark);
        v.add(6, new Integer(content.size()));
        content.add(v);
    }
	
//	public void addRow(String[] columnName) {
//		Vector v = new Vector();
//		for (int i = 0; i < columnName.length; i++) {
//			if(i==0 || i==columnName.length-2)
//				v.add(content.size());
//			
//			v.add("");
//		}
//        content.add(v);
//    }
	
    public void removeRow(int row) {
        content.remove(row);
    }

    public void removeRows(int row, int count) {
        for (int i = 0; i < count; i++) {
            if (content.size() > row) {
                content.remove(row);
            }
        }
    }

    /**
    * 让表格中某些值可修改，但需要setValueAt(Object value, int row, int col)方法配合才能使修改生效
    */
    @Override
	public boolean isCellEditable(int rowIndex, int columnIndex) {
        if (columnIndex == 0) {
            return false;
        }
        return true;
    }

    /**
    * 使修改的内容生效
    */
    @Override
	public void setValueAt(Object value, int row, int col) {
        ((Vector<?>) content.get(row)).remove(col);
        content.get(row).add(col, value);
//        fireTableCellUpdated(row, col);
    }

    @Override
	public String getColumnName(int col) {
        return columnName[col];
    }

    @Override
	public int getColumnCount() {
        return columnName.length;
    }

    @Override
	public int getRowCount() {
        return content.size();
    }

    @Override
	public Object getValueAt(int row, int col) {
    	if(content != null && (row <content.size()) && (col<columnDb.length))
    		return ((Vector<?>) content.get(row)).get(col);
    	else
    		return null;
    }

    /**
    * 返回数据类型
    */
    @Override
	public Class<? extends Object> getColumnClass(int col) {
		if (content == null) {
			return null;
		}

		if (content.size() == 0) {
			return null;
		}

		if (!(col < columnDb.length)) {
			return null;
		}

		return getValueAt(0, col).getClass();
    }
}