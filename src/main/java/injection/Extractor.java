package injection;

import java.io.File;
import java.io.FileInputStream;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.apache.log4j.Logger;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.xssf.usermodel.XSSFCell;
import org.apache.poi.xssf.usermodel.XSSFRow;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;

import com.mongodb.BasicDBObject;

public class Extractor {
	public static final String ATTRIBUTES = "attributes";
	public static final String KEY = "key";
	public static final String VALUE = "value";
	/***
	 * Format de date pour convertir les dates excel
	 */
	public static final String DATE_FORMAT = "ddMMyyyy";
	private static final Logger LOGGER = Logger.getLogger(Extractor.class);

	public static List<BasicDBObject> excelExtract(File file) {

		List<BasicDBObject> list = new ArrayList<BasicDBObject>();

		try {

			// ouverture du flux
			FileInputStream fis = new FileInputStream(file.getAbsolutePath());
			// ouverture du fichier excel
			XSSFWorkbook wb = new XSSFWorkbook(fis);
			// fermeture du flux
			fis.close();
			// parcours des feuilles du fichier, bas�e sur le nombre de feuilles
			// fourni
			for (int i = 0; i < wb.getNumberOfSheets(); i++) {
				XSSFSheet activeSheet = wb.getSheetAt(i);
				Iterator<Row> rowIter = activeSheet.rowIterator();
				// passage du curseur � la premi�re ligne, consid�r�e comme la
				// ligne d'entete
				if (!rowIter.hasNext()) {
					continue;
				}
				XSSFRow enteteRow = (XSSFRow) rowIter.next();
				Iterator<Cell> cellIter = enteteRow.cellIterator();
				int nbCol = 0;
				// r�cup�ration du nombre de colonnes � exploiter
				while (cellIter.hasNext()) {
					nbCol++;
					cellIter.next();
				}
				XSSFRow activeRow = null;
				/*
				 * parcours des lignes de la feuille. Si la premi�re cellule
				 * d'une ligne est vide, on arrete le parcours.
				 */
				while (rowIter.hasNext()) {
					activeRow = (XSSFRow) rowIter.next();
					if (isEmpty(activeRow)) {
						break;
					}
					BasicDBObject dbo = new BasicDBObject();
					dbo.append("folderSource", new String(file.getParent()));
					dbo.append("fileName", new String(file.getName()));
					dbo.append("sheetName",
							new String(activeSheet.getSheetName()));
					dbo.append("rowNumber", activeRow.getRowNum() + 1);
					List<BasicDBObject> attributes = new ArrayList<BasicDBObject>();
					for (int numCol = 0; numCol < nbCol; numCol++) {
						BasicDBObject attribute = new BasicDBObject();
						attribute.append(KEY,
								getCellData(enteteRow.getCell(numCol)));
						attribute.append(VALUE,
								getCellData(activeRow.getCell(numCol)));
						attributes.add(attribute);
					}
					dbo.append(ATTRIBUTES, attributes);

					list.add(dbo);
				}
			}

		} catch (Exception e) {
			LOGGER.error(e.getStackTrace().toString());
			e.printStackTrace();
		}
		return list;

	}

	private static boolean isEmpty(XSSFRow activeRow) {
		for (Cell cell : activeRow) {
			if (cell != null && !cell.toString().isEmpty()) {
				return false;
			}
		}
		return true;
	}

	private static String getCellData(XSSFCell myCell) {
		String cellData;
		if (myCell == null) {
			return null;

		} else {
			switch (myCell.getCellType()) {
			case XSSFCell.CELL_TYPE_STRING:
				cellData = myCell.getStringCellValue();
				cellData = cellData.replaceAll(
						System.getProperty("line.separator"), " ");
				break;
			case XSSFCell.CELL_TYPE_NUMERIC:
				cellData = getNumericValue(myCell);
				break;
			case XSSFCell.CELL_TYPE_FORMULA:
				switch (myCell.getCachedFormulaResultType()) {
				case XSSFCell.CELL_TYPE_STRING:
					cellData = myCell.getStringCellValue();
					cellData = cellData.replaceAll(
							System.getProperty("line.separator"), " ");
					break;
				case XSSFCell.CELL_TYPE_NUMERIC:
					cellData = getNumericValue(myCell);
					break;
				default:
					cellData = null;
				}
				break;
			default:
				cellData = null;
			}
		}
		return cellData;
	}

	private static String getNumericValue(XSSFCell myCell) {
		if (myCell == null) {
			return null;
		}
		String cellData = "";

		if (HSSFDateUtil.isCellDateFormatted(myCell)) {
			SimpleDateFormat sdf = new SimpleDateFormat(DATE_FORMAT);
			cellData = sdf.format(myCell.getDateCellValue());
		} else {
			DataFormatter df = new DataFormatter();
			cellData = df.formatCellValue(myCell);
		}
		return cellData;
	}

}
