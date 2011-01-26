
package com.nayaware.dbtools.csv;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.sql.SQLException;

import com.nayaware.dbtools.api.ITableData;
import com.nayaware.dbtools.api.ITableRowData;

/**
 * Writer that writes the Table data in to CVS format file
 * 
 * @author Winston Prakash
 * @version 1.0
 */
public class CsvWriter {

	private static final String LINE_SEPARATOR = System
			.getProperty("line.separator"); //$NON-NLS-1$

	public void write(File outputFile, ITableData tableData, boolean headerRow,
			char columnSeparator, boolean quote) throws IOException,
			SQLException, ClassNotFoundException {
		FileWriter writer = new FileWriter(outputFile);
		if (headerRow) {

			writer.write(filter(tableData.getColumnNames(), columnSeparator,
					quote));
			writer.write(LINE_SEPARATOR);
		}
		try {
			while (tableData.nextRow()) {
				ITableRowData rowData = tableData.getRowData();
				writer.write(filter(rowData.getRowAsStringArray(),
						columnSeparator, quote));
				writer.write(LINE_SEPARATOR);
				writer.flush();
			}
			;
		} finally {
			tableData.close();
			writer.close();
		}
	}

	public static String filter(String[] columnValues, char columnSeparator,
			boolean quote) {
		String result = ""; //$NON-NLS-1$
		for (int i = 0; i < columnValues.length; i++) {
			if (i > 0) {
				result += columnSeparator;
			}
			result += filter(columnValues[i], columnSeparator, quote);
		}
		return result;
	}

	private static String filter(String string, char columnSeparator,
			boolean quote) {
		if (string == null)
			return ""; //$NON-NLS-1$
		if (quote) {
			return quote(string);
		} else {
			if (isClean(string, columnSeparator) && string.length() > 0) {
				return string;
			} else {
				return quote(string);
			}
		}
	}

	private static String quote(String s) {
		if (s == null) {
			return ""; //$NON-NLS-1$
		}
		return "\"" + s.replaceAll("\"", "\"\"") + "\""; //$NON-NLS-1$ //$NON-NLS-2$ //$NON-NLS-3$ //$NON-NLS-4$
	}

	private static boolean isClean(String s, char columnSeparator) {
		int len = s.length();

		for (int i = 0; i < len; i++) {
			char c = s.charAt(i);
			if (c == columnSeparator || c == '"'
					|| (i == 0 && Character.isWhitespace(c))
					|| (i == (len - 1) && Character.isWhitespace(c))) {
				return false;
			}
		}
		return (s.indexOf(LINE_SEPARATOR) < 0);
	}
}
