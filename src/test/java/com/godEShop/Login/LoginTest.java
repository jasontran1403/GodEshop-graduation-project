package com.godEShop.Login;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.TimeUnit;

import javax.imageio.ImageIO;

import org.apache.commons.io.IOUtils;
import org.apache.poi.common.usermodel.HyperlinkType;
import org.apache.poi.ss.usermodel.Cell;
import org.apache.poi.ss.usermodel.CellStyle;
import org.apache.poi.ss.usermodel.ClientAnchor;
import org.apache.poi.ss.usermodel.CreationHelper;
import org.apache.poi.ss.usermodel.DataFormatter;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.Row;
import org.apache.poi.ss.usermodel.VerticalAlignment;
import org.apache.poi.xssf.usermodel.XSSFClientAnchor;
import org.apache.poi.xssf.usermodel.XSSFDrawing;
import org.apache.poi.xssf.usermodel.XSSFHyperlink;
import org.apache.poi.xssf.usermodel.XSSFSheet;
import org.apache.poi.xssf.usermodel.XSSFWorkbook;
import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.Test;

import io.github.bonigarcia.wdm.WebDriverManager;
import ru.yandex.qatools.ashot.AShot;
import ru.yandex.qatools.ashot.Screenshot;
import ru.yandex.qatools.ashot.shooting.ShootingStrategies;

public class LoginTest {
    public WebDriver driver;
    private XSSFWorkbook workbook;
    private XSSFSheet worksheet;
    private Map<String, Object[]> TestNGResult;
    private Map<String, String[]> dataLoginTest;

    private final String EXCEL_DIR = "D:/xxx/xxx/DU_AN_TOT_NGHIEP/Code/GodEShop/test-resources/data/";
    private final String IMAGE_DIR = "D:/xxx/xxx/DU_AN_TOT_NGHIEP/Code/GodEShop/test-resources/images/";

    // đọc dữ liệu từ file excel
    private void readDataFromExcel() {
	try {
	    dataLoginTest = new HashMap<String, String[]>();
	    worksheet = workbook.getSheet("TestData"); // tên sheet cần lấy data
	    if (worksheet == null) {
		System.out.println("Không tìm thấy worksheet : TestData");
	    } else {
		Iterator<Row> rowIterator = worksheet.iterator();
		DataFormatter dataFormat = new DataFormatter();
		while (rowIterator.hasNext()) {
		    Row row = rowIterator.next();
		    if (row.getRowNum() >= 1) {
			Iterator<Cell> cellIterator = row.cellIterator();
			String key = ""; // key - ô stt
			String username = ""; // giá trị ô username
			String password = ""; // giá trị ô password
			String expected = ""; // giá trị ô expected
			while (cellIterator.hasNext()) {
			    Cell cell = cellIterator.next();
			    if (cell.getColumnIndex() == 0) {
				key = dataFormat.formatCellValue(cell);
			    } else if (cell.getColumnIndex() == 1) {
				username = dataFormat.formatCellValue(cell);
			    } else if (cell.getColumnIndex() == 2) {
				password = dataFormat.formatCellValue(cell);
			    } else if (cell.getColumnIndex() == 3) {
				expected = dataFormat.formatCellValue(cell);
			    }
			    String[] myArr = { username, password, expected };
			    dataLoginTest.put(key, myArr);
			}
		    }
		}
	    }
	} catch (Exception e) {
	    System.out.println("readDataFromExcel() : " + e.getMessage());
	}
    }
    // ---------- Kết thúc đọc dữ liệu -------------------

    // ---------- Xử lý chụp ảnh -------------------------
    // -----Hàm chụp ảnh------

    public void takeScreenShot(WebDriver driver, String outputSrc) throws IOException {
	Screenshot screenshot = new AShot().shootingStrategy(ShootingStrategies.viewportPasting(1000))
		.takeScreenshot(driver);
	ImageIO.write(screenshot.getImage(), "PNG", new File(outputSrc));
    }

    public void writeImage(String imgSrc, Row row, Cell cell, XSSFSheet sheet) throws IOException {
	InputStream is = new FileInputStream(imgSrc);
	byte[] bytes = IOUtils.toByteArray(is);
	int idImg = sheet.getWorkbook().addPicture(bytes, XSSFWorkbook.PICTURE_TYPE_PNG);
	is.close();

	XSSFDrawing drawing = sheet.createDrawingPatriarch();

	ClientAnchor anchor = new XSSFClientAnchor();

	anchor.setCol1(cell.getColumnIndex() + 1);
	anchor.setRow1(row.getRowNum());
	anchor.setCol2(cell.getColumnIndex() + 2);
	anchor.setRow2(row.getRowNum() + 1);

	drawing.createPicture(anchor, idImg);

    }

    // ---------- Kết thúc Xử lý chụp ảnh ----------------

    // ------------ Before Class ------------

    @BeforeClass(alwaysRun = true)
    public void suiteTest() {
	try {
	    TestNGResult = new LinkedHashMap<String, Object[]>();
	    WebDriverManager.chromedriver().setup();
	    driver = new ChromeDriver();
	    driver.manage().window().maximize();
//	    workbook = new XSSFWorkbook(new FileInputStream(new File(EXCEL_DIR + "TEST_LOGIN.xlsx")));
	    workbook = new XSSFWorkbook(new FileInputStream(
		    new File("D:/xxx/xxx/DU_AN_TOT_NGHIEP/Code/GodEShop/test-resources/data/TEST_LOGIN.xlsx")));
	    worksheet = workbook.getSheet("TestData");
	    readDataFromExcel(); // đọc dữ liệu
	    driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);

	    workbook = new XSSFWorkbook();
	    worksheet = workbook.createSheet("TestNG Result Summary");
	    // thêm test result vào file excel ở cột header
	    CellStyle rowStyle = workbook.createCellStyle();
	    rowStyle.setAlignment(HorizontalAlignment.CENTER);
	    rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
	    rowStyle.setWrapText(true);

	    // viết header vào dòng đầu tiên
	    TestNGResult.put("1", new Object[] { "STT", "Username", "Password", "Action", "Expected", "Actual",
		    "Status", "Date Check", "LINK", "Image" });
	} catch (Exception e) {
	    System.out.println("suiteTest() : " + e.getMessage());
	}
    }

    // ----------- After Class -----------

    @AfterClass
    public void suiteTearDown() {
	Set<String> keyset = TestNGResult.keySet();
	int rownum = 0;
	for (String key : keyset) {
	    CellStyle rowStyle = workbook.createCellStyle();
	    Row row = worksheet.createRow(rownum++);
	    Object[] objArr = TestNGResult.get(key);
	    int cellnum = 0;
	    for (Object obj : objArr) {
		rowStyle.setAlignment(HorizontalAlignment.CENTER);
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		rowStyle.setWrapText(true);
		int flag = cellnum++;
		Cell cell = row.createCell(flag);
		if (obj instanceof Date) {
		    cell.setCellValue((Date) obj);
		} else if (obj instanceof Boolean) {
		    cell.setCellValue((Boolean) obj);
		} else if (obj instanceof String) {
		    cell.setCellValue((String) obj);
		} else if (obj instanceof Double) {
		    cell.setCellValue((Double) obj);
		}

		if (obj.toString().contains("failure") && obj.toString().contains(".png")) {
		    try {
			row.setHeightInPoints(80);
			writeImage(obj.toString(), row, cell, worksheet);
			CreationHelper creationHelper = worksheet.getWorkbook().getCreationHelper();
			XSSFHyperlink hyperlink = (XSSFHyperlink) creationHelper.createHyperlink(HyperlinkType.URL);
			cell.setCellValue("Full Image");
			hyperlink.setAddress(obj.toString().replace("\\", "/"));
			cell.setHyperlink(hyperlink);
			rowStyle.setAlignment(HorizontalAlignment.CENTER);
			rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
			rowStyle.setWrapText(true);
			row.setRowStyle(rowStyle);

		    } catch (Exception d) {
			System.out.println("Write Image : " + d.getMessage());
		    }
		}
		rowStyle.setAlignment(HorizontalAlignment.CENTER);
		rowStyle.setVerticalAlignment(VerticalAlignment.CENTER);
		rowStyle.setWrapText(true);
		worksheet.autoSizeColumn(cellnum);
		worksheet.setColumnWidth(9, 7000);
		row.setRowStyle(rowStyle);
	    }
	    try {
//		FileOutputStream out = new FileOutputStream(new File(EXCEL_DIR + "RESULT_TEST_LOGIN.xlsx"));
		FileOutputStream out = new FileOutputStream(new File(
			"D:/xxx/xxx/DU_AN_TOT_NGHIEP/Code/GodEShop/test-resources/data/RESULT_TEST_LOGIN.xlsx"));

		workbook.write(out);
		out.close();
		System.out.println("Successfully save to Excel File!!!");
	    } catch (Exception e) {
		System.out.println("suiteTearDown() : " + e.getMessage());
	    }
	}
    }

    // -------------- TEST CAST -------------------------

    @Test(description = "Test Login", priority = 1)
    public void LoginTest() {
	try {
	    Set<String> keySet = dataLoginTest.keySet();
	    int index = 1;
	    for (String key : keySet) {
		String[] value = dataLoginTest.get(key);
		String username = value[0];
		String password = value[1];
		String expected = value[2];

		LocalDateTime myDateObj = LocalDateTime.now();
		DateTimeFormatter myFormatObj = DateTimeFormatter.ofPattern("HH:mm:ss | dd-MM-yyyy ");
		String formattedDate = myDateObj.format(myFormatObj);

		driver.get("http://localhost:8080/account/login/form");
		driver.findElement(By.xpath("/html/body/div[1]/main/div[4]/div[2]/form/input")).sendKeys(username);
		driver.findElement(By.xpath("/html/body/div[1]/main/div[4]/div[2]/form/div[2]/input"))
			.sendKeys(password);

		Thread.sleep(1000);

		WebElement btnLogin = driver.findElement(By.xpath("/html/body/div[1]/main/div[4]/div[2]/form/button"));
		Actions actions = new Actions(driver).click(btnLogin);
		actions.build().perform();

//		String actualTitle = driver.getTitle();
		String actualTitle = driver
			.findElement(
				By.xpath("/html/body/div[1]/div[1]/header/div[1]/div/div/div[2]/div/div/div[1]/div/a"))
			.getText().trim();

		System.out.println(
			"--- " + username + " | " + password + " | " + expected + " | " + actualTitle + " ---");

		Thread.sleep(1000);

		if (actualTitle.equalsIgnoreCase(expected)) {
		    TestNGResult.put(String.valueOf(index + 1), new Object[] { String.valueOf(index), // STT
			    username, // Username
			    password, // password
			    "Test Login", // action
			    expected, // expected
			    actualTitle, // actual
			    "PASS", // status
			    formattedDate, // date check
			    "" // image
		    });
		    String currentUrl = driver.getCurrentUrl();
		    if (!currentUrl.contains("/account/login")) {
			WebElement btnLogout = driver.findElement(
				By.xpath("/html/body/div[1]/div[1]/header/div[1]/div/div/div[2]/div/div/div[1]/div/a"));
			Actions actionsLogout = new Actions(driver).click(btnLogout);
			actionsLogout.build().perform();
			WebElement btnLogoff = driver.findElement(By.xpath(
				"/html/body/div[1]/div/header/div[1]/div/div/div[2]/div/div/div[1]/div/ul/li[3]"));
			Actions actionsLogoff = new Actions(driver).click(btnLogoff);
			actionsLogoff.build().perform();
		    }
		} else {
		    driver.get("http://localhost:8080/account/login/form");
		    driver.findElement(By.xpath("/html/body/div[1]/main/div[4]/div[2]/form/input")).sendKeys(username);
		    driver.findElement(By.xpath("/html/body/div[1]/main/div[4]/div[2]/form/div[2]/input"))
			    .sendKeys(password);
//		    String path = IMAGE_DIR + "failure-" + System.currentTimeMillis() + ".png";
		    String path = "D:/xxx/xxx/DU_AN_TOT_NGHIEP/Code/GodEShop/test-resources/images/" + "failure-"
			    + System.currentTimeMillis() + ".png";

		    takeScreenShot(driver, path);
		    TestNGResult.put(String.valueOf(index + 1),
			    new Object[] { String.valueOf(index), username, password, "Test Login", expected,
				    actualTitle, "FAILED", formattedDate, path.replace("\\", "/") });
		}

		index++;
	    }
	} catch (Exception e) {
	    System.out.println("LoginTest() : " + e.getMessage());
	}
    }
}
