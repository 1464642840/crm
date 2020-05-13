package com.company.project.utils.poi;


import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.databind.exc.InvalidFormatException;
import org.apache.poi.EncryptedDocumentException;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.apache.poi.ss.util.NumberToTextConverter;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import java.io.*;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.*;

public class XssFUtils {

	private static XssFUtils utiles = new XssFUtils();

	public static XssFUtils getInstance() {

		return utiles;
	}

	public JSONObject ExcelConverTo(String path, InputStream stream)
			throws IOException, EncryptedDocumentException,
			InvalidFormatException {
		Workbook workbook = null;
		Sheet sheet = null;
		if (path == null) {
			return null;
		}
		InputStream is = null;
		if (stream == null) {
			File file = new File(path);
			is = new FileInputStream(file);
		} else {
			is = stream;
		}
		if (path.toLowerCase().endsWith("xls")) {// 2003
			workbook = new HSSFWorkbook(is);

		} else if (path.toLowerCase().endsWith("xlsx")) {// 2007
			workbook = new XSSFWorkbook(is);

		} else {

			return null;
		}
		// System.out.println(workbook.getNumberOfSheets());
		JSONObject obj = new JSONObject();
		int sheetNums = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNums; i++) {
			sheet = workbook.getSheetAt(i);
			String[][] s = getString(workbook, sheet);

			obj.put(workbook.getSheetAt(i).getSheetName(), s);
		}
		is.close();
		return obj;
	}

	public JSONObject ToConvertJson(InputStream stram, String filetype)
			throws IOException {
		Workbook workbook = null;
		Sheet sheet = null;
		if (filetype.toLowerCase().endsWith("xls")) {// 2003
			workbook = new HSSFWorkbook(stram);
		} else if (filetype.toLowerCase().endsWith("xlsx")) {// 2007
			workbook = new XSSFWorkbook(stram);
		} else {

			return null;
		}
		JSONObject obj = new JSONObject();
		int sheetNums = workbook.getNumberOfSheets();
		for (int i = 0; i < sheetNums; i++) {
			sheet = workbook.getSheetAt(i);
			String[][] s = getString(workbook, sheet);
			if(s==null) {
				continue;
			}
			//System.out.println(s.toString());
			obj.put(workbook.getSheetAt(i).getSheetName(), s);
		}
		stram.close();
		return obj;
	}

	public boolean ToConvertExcle(String[][] s, String pathAndName) {

		boolean flag = false;

		String filePath = pathAndName;
		String sheetName = pathAndName
				.substring(pathAndName.lastIndexOf("/") + 1);
		Workbook workbook = null;
		if (filePath.toLowerCase().endsWith("xls")) {// 2003
			workbook = new HSSFWorkbook();
		} else if (filePath.toLowerCase().endsWith("xlsx")) {// 2007
			workbook = new XSSFWorkbook();
		} else {

		}
		// create sheet
		Sheet sheet = workbook.createSheet(sheetName);
		try {
			// 写表头数据
			for (int i = 0; i < s.length; i++) {
				// 创建表头单元格,填值
				Row titleRow = sheet.createRow(i);
				Set<String> set = new HashSet<String>();
				for (int ii = 0; ii < s[i].length; ii++) {
					if (i > 1 && ii == 17) {
						Random rm = new Random();
						StringBuffer offlineorder = new StringBuffer(
								"offlineorder");
						for (int si = 0; si < 10; si++) {
							offlineorder.append(rm.nextInt(9));
						}

						while (!set.add(offlineorder.toString())) {
							titleRow.createCell(ii).setCellValue(
									offlineorder.toString());
						}
					} else {

						titleRow.createCell(ii).setCellValue(s[i][ii]);
					}
				}
			}

			FileOutputStream fos = new FileOutputStream(filePath);
			workbook.write(fos);
			fos.close();
			flag = true;
		} catch (FileNotFoundException e) {

		} catch (IOException e) {

		}

		return flag;

	}

	public boolean ToConvertExcle(JSONArray json, OutputStream os,
								  String filename) {

		boolean flag = false;


		Workbook workbook = null;
		if (filename.toLowerCase().endsWith("xls")) {// 2003
			workbook = new HSSFWorkbook();
		} else if (filename.toLowerCase().endsWith("xlsx")) {// 2007
			workbook = new XSSFWorkbook();
		} else {

		}
		// create sheet
		Sheet sheet = workbook.createSheet(filename);
		SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd hh:mm:ss");
		try {
			// 写表头数据
			for (int i = 0; i < json.size(); i++) {
				// 创建表头单元格,填值
				Row titleRow = sheet.createRow(i);

				for (int ii = 0; ii < json.getJSONArray(i).size(); ii++) {
					if (json.getJSONArray(i).get(ii) instanceof JSONObject) {
						JSONObject js = json.getJSONArray(i).getJSONObject(ii);
						long t = js.getLong("time");
						Date dt = new Date(t);
						titleRow.createCell(ii).setCellValue(sdf.format(dt));
					} else {
						titleRow.createCell(ii).setCellValue(
								json.getJSONArray(i).getString(ii));
					}

				}
			}
			workbook.write(os);
			flag = true;
		} catch (FileNotFoundException e) {

			flag = false;
		} catch (IOException e) {

			flag = false;
		}

		return flag;

	}

	private String[][] getString(Workbook book, Sheet sheet) {
		Integer x = sheet.getPhysicalNumberOfRows();
		Iterator<Row> its = sheet.iterator();
		String[][] strings = null;
		if (its.hasNext()) {
			Row row = its.next();
			Integer y = row.getPhysicalNumberOfCells();
			strings = new String[x][y];
			int xx = 0;
			for (int yy = 0; yy < y; yy++) {
				if (row == null) {
					continue;
				} else if (row.getCell(yy) != null) {
					Cell cell = getMergedCellValue(sheet, row.getCell(yy));
					switch (cell.getCellTypeEnum()) {
					case STRING:
						strings[xx][yy] = String.valueOf(cell
								.getStringCellValue());
						break;
					case NUMERIC:
						if (DateUtil.isCellDateFormatted(cell)) {
							SimpleDateFormat format = new SimpleDateFormat(
									"yyyy-MM-dd HH:mm:ss");
							Date date = cell.getDateCellValue();
							strings[xx][yy] = format.format(date);
						} else {
							strings[xx][yy] = NumberToTextConverter.toText(cell
									.getNumericCellValue());
						}
						break;
					case BOOLEAN:
						strings[xx][yy] = String.valueOf(cell
								.getBooleanCellValue());
						break;
					case BLANK:
						strings[xx][yy] = "";
						break;
					case FORMULA:
						CellValue value = book.getCreationHelper()
								.createFormulaEvaluator().evaluate(cell);
						switch (value.getCellTypeEnum()) {

						case STRING:

							strings[xx][yy] = cell.getStringCellValue();

							break;

						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date date = cell.getDateCellValue();
								strings[xx][yy] = format.format(date);
							} else {
								strings[xx][yy] = NumberToTextConverter
										.toText(cell.getNumericCellValue());
							}

							break;
						}
					default:
						break;
					}
				} else {
					strings[xx][yy] = "";
				}
			}
			while (its.hasNext()) {
				xx = xx + 1;
				Row rows = its.next();
				for (int yyy = 0; yyy < y; yyy++) {
					if (rows == null) {

						continue;
					} else if (rows.getCell(yyy) != null) {
						Cell cell = getMergedCellValue(sheet, rows.getCell(yyy));
						switch (cell.getCellTypeEnum()) {
						case STRING:
							strings[xx][yyy] = String.valueOf(cell
									.getStringCellValue());
							break;
						case NUMERIC:
							if (DateUtil.isCellDateFormatted(cell)) {
								SimpleDateFormat format = new SimpleDateFormat(
										"yyyy-MM-dd HH:mm:ss");
								Date date = cell.getDateCellValue();
								strings[xx][yyy] = format.format(date);
							} else {
								strings[xx][yyy] = NumberToTextConverter
										.toText(cell.getNumericCellValue());
							}
							break;
						case BOOLEAN:
							strings[xx][yyy] = String.valueOf(cell
									.getBooleanCellValue());
							break;
						case BLANK:

							strings[xx][yyy] = "";
							break;
						case FORMULA:
							// strings[xx][yyy]
							CellValue value = book.getCreationHelper()
									.createFormulaEvaluator().evaluate(cell);

							switch (value.getCellTypeEnum()) {
							case STRING:
								strings[xx][yyy] = cell.getStringCellValue();
								break;
							case NUMERIC:
								if (DateUtil.isCellDateFormatted(cell)) {
									SimpleDateFormat format = new SimpleDateFormat(
											"yyyy-MM-dd HH:mm:ss");
									Date date = cell.getDateCellValue();
									strings[xx][yyy] = format.format(date);
								} else {
									strings[xx][yyy] = NumberToTextConverter
											.toText(cell.getNumericCellValue());
								}

								break;
							}

							break;
						default:
							break;
						}
					} else {
						strings[xx][yyy] = "";
					}
				}
			}
		}

		return strings;
	}

	// 该方法是为了判断，该单元格是不是合并单元格
	public boolean isCombine(List<CellRangeAddress> listCombineCell,
                             XSSFCell cell) {

		int firstC;
		int lastC;
		int firstR;
		int lastR;
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC
						&& cell.getColumnIndex() <= lastC) {
					return true;
				}
			}
		}
		return false;
	}

	// 合并单元格的所有格的值都是首个首行的
	public Cell getMergedCellValue(Sheet sheet, Cell cell) {
		int firstC;
		int lastC;
		int firstR;
		int lastR;
		List<CellRangeAddress> listCombineCell = sheet.getMergedRegions();
		for (CellRangeAddress ca : listCombineCell) {
			// 获得合并单元格的起始行, 结束行, 起始列, 结束列
			firstC = ca.getFirstColumn();
			lastC = ca.getLastColumn();
			firstR = ca.getFirstRow();
			lastR = ca.getLastRow();
			if (cell.getRowIndex() >= firstR && cell.getRowIndex() <= lastR) {
				if (cell.getColumnIndex() >= firstC
						&& cell.getColumnIndex() <= lastC) {
					// 获取合并单元格左上角单元格值
					Row fRow = sheet.getRow(firstR);
					Cell fCell = fRow.getCell(firstC);
					return fCell;
				}
			}
		}
		return cell;
	}

	public static Workbook sqlToExcel(List<Object> ls) {

		if(ls==null||ls.size()==0){
			
			return new HSSFWorkbook();
			
		}
		
		Workbook workbook = new HSSFWorkbook();

		Sheet sheet = workbook.createSheet();
		OutputStream stream=null;
		Object ob = ls.get(0);
		Class cl = ob.getClass();
		Field[] fs = cl.getDeclaredFields();
        if(fs.length==0){
        	cl=cl.getSuperclass();
        	fs=cl.getDeclaredFields();
        }
    
		int column_num = fs.length;
		int row_num = ls.size();
		Row titleRow = sheet.createRow(0);
		for (int ri = 0; ri < column_num; ri++) {
	     titleRow.createCell(ri).setCellValue(fs[ri].getName());
		}
		try {
			// 写表头数据
			for (int i = 0; i < row_num; i++) {
				// 创建表头单元格,填值
				Row contentRow = sheet.createRow(i + 1);
				for (int ri = 0; ri < column_num; ri++) {
					String methodName = "get"
							+ toUpperCaseFirstOne(fs[ri].getName());
					Method ms = cl.getDeclaredMethod(methodName, null);
					Object value = ms.invoke(ls.get(i), null);
					if (value instanceof String || value instanceof Number) {
						contentRow.createCell(ri).setCellValue(value.toString());
					} else if (value instanceof Date) {
						SimpleDateFormat format = new SimpleDateFormat(
								"yyyy-MM-dd HH:mm:ss");
						String string = format.format(value);
						contentRow.createCell(ri).setCellValue(string);
					}

				}

			}
			return workbook;
		} catch (NoSuchMethodException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (SecurityException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (IllegalArgumentException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		} catch (InvocationTargetException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}finally{
			if(stream!=null){
				try {
					stream.close();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}
			}
		}
		return null;

	}

	public static String toUpperCaseFirstOne(String s) {
		if (Character.isUpperCase(s.charAt(0)))
			return s;
		else
			return (new StringBuilder())
					.append(Character.toUpperCase(s.charAt(0)))
					.append(s.substring(1)).toString();
	}
	
	
	


}
