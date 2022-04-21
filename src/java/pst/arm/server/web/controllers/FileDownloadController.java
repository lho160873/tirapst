package pst.arm.server.web.controllers;

import javax.servlet.ServletOutputStream;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import org.apache.log4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.server.common.service.UserService;

/**
 *
 * @author Sergey Smirnov
 */
@Controller
public class FileDownloadController {

    private static final Logger statLog = Logger.getLogger("StatisticLogger");
    private static Logger log = Logger.getLogger("DDataGridStoreExtractor");
    public static final String CAPTCHA_IMAGE_FORMAT = "jpeg";
//    @Autowired
//    private SafetyFundService safetyFundService;
//    @Autowired
//    public SearchService searchService;
//    @Autowired
//    public StatService statService;
    @Autowired
    public UserService userService;
//    @Autowired
//    public ArchiveStoreService archiveStoreService;
//    @Autowired
//    public RequestsService requestsService;
//    @Autowired
//    public RhUsersService rhUserService;

    /**
     * <p>Download report from session.</p> <p>In session report stored as
     * FileObjectDescriptor. To put it into session used GWT service. To
     * identify report use uuid</p>
     *
     * @see
     * ru.spb.iac.archives.arm.client.common.domain.generated.FileObjectDescriptor
     * @param request
     * @param response
     * @param fileId uuid received from GWT service
     * @throws Exception
     */
    @RequestMapping("/secure/download.htm")
    public void getFile(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "fileId", required = false) String fileId) throws Exception {
        log.warn("/secure/download.htm");
        if (fileId == null || fileId.length() == 0) {
            return;
        }
        FileObjectDescriptor descriptor = (FileObjectDescriptor) request.getSession().getAttribute(fileId);

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);

        if (descriptor != null) {
            setContentType(descriptor);
            response.setContentType(descriptor.getFileContentType());

            String disposition = "inline";

            if (descriptor.getFileExt().toUpperCase().equals("JPG") || descriptor.getFileExt().toUpperCase().equals("PNG") || descriptor.getFileExt().toUpperCase().equals("TXT") || descriptor.getFileExt().toUpperCase().equals("ZIP") || descriptor.getFileExt().toUpperCase().equals("RAR"))
                disposition = "attachment";
            response.addHeader("Content-Disposition", disposition + "; filename=\"" + descriptor.getFileName() + "." + descriptor.getFileExt() + "\"");

            ServletOutputStream responseOutputStream = response.getOutputStream();
            responseOutputStream.write(descriptor.getFileContent());
            responseOutputStream.flush();
            responseOutputStream.close();

            request.getSession().removeAttribute(fileId);
        }
    }

    private void setContentType(FileObjectDescriptor fod) {
        String s = fod.getFileExt();

        fod.setFileContentType("octet-stream");

        if (s.equals("pdf"))
            fod.setFileContentType("application/pdf");

        if (s.equals("rtf"))
            fod.setFileContentType("application/rtf");

        if (s.equals("jpg"))
            fod.setFileContentType("image/jpeg");

        if (s.equals("docx"))
            fod.setFileContentType("application/vnd.openxmlformats-officedocument.wordprocessingml.documen");

        if (s.equals("doc"))
            fod.setFileContentType("application/msword");

        if (s.equals("xls"))
            fod.setFileContentType("application/vnd.ms-excel");
        
        if (s.equals("csv"))
            fod.setFileContentType("application/vnd.ms-excel");

        if (s.equals("xlsx"))
            fod.setFileContentType("application/vnd.openxmlformats-officedocument.spreadsheetml.sheet");

        if (s.equals("txt"))
            fod.setFileContentType("text/plain");
    }

    @RequestMapping("/secure/report.htm")
    public void showReport(HttpServletRequest request, HttpServletResponse response,
            @RequestParam(value = "fileId", required = false) String fileId) throws Exception {
        log.warn("showReport /secure/report.htm BEGIN");
        if (fileId == null || fileId.length() == 0) {
            log.warn("showReport 0");
            return;
        }
        log.warn("showReport 1");
        FileObjectDescriptor descriptor = (FileObjectDescriptor) request.getSession().getAttribute(fileId);
        log.warn("showReport 2");
        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        log.warn("showReport 3");
        if (descriptor != null && descriptor.getFileContentType() != null) {
            log.warn("showReport 4");
            ServletOutputStream os = response.getOutputStream();
            os.write(descriptor.getFileContent());
            os.flush();
            os.close();
        }
        log.warn("showReport 5");
        request.getSession().removeAttribute(fileId);
        log.warn("showReport /secure/report.htm END");

    }
/*
    @RequestMapping("/secure/report1.htm")
    public void Test(HttpServletRequest request, HttpServletResponse response) throws Exception {
        byte[] bytes = null;
        log(request);
        HttpSession session = request.getSession();
        log.warn("1");
        UniversalGenerator generator = new UniversalGenerator();
        String reportId = "test";
        ReportDescriptor rd = new ReportDescriptor(reportId);//(ReportDescriptor) session.getAttribute(reportId);
        rd.addColumn("1", "1");
        rd.addColumn("2", "2");
        rd.setArchiveName("sdfsd");
        rd.setCaption("sdfsdfs");

        response.setHeader("Cache-Control", "no-store");
        response.setHeader("Pragma", "no-cache");
        response.setDateHeader("Expires", 0);
        log.warn("2");
        if (rd != null) {
            log.warn("3");
            bytes = generator.generateReport_test(rd);// generateReport_test(rd);
            //session.removeAttribute(reportId);
            log.warn("4");
            String name = Translit.toTranslit(rd.getCaption());
            if (rd.isPdfReport()) {
                response.setContentType("application/pdf");
                if (name != null) {
                    name += ".pdf";
                }
            }
            if (rd.isRtfReport()) {
                response.setContentType("application/msword");
                if (name != null) {
                    name += ".doc";
                }
            }
            log.warn("5");
            if (name != null) {
                response.addHeader("Content-Disposition", "attachment; filename=\"" + name + "\"");
            }
            log.warn("6");
            //session.removeAttribute(reportId);
        }

        // flush it in the response
        ServletOutputStream responseOutputStream = response.getOutputStream();
        responseOutputStream.write(bytes);
        responseOutputStream.flush();
        responseOutputStream.close();

    }*/

//    @RequestMapping("/secure/report2.htm")
//    public void showReport2(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "reportId", required = false) String reportId,
//            @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//
//        log(request);
//
//        HttpSession session = request.getSession();
//        //FileObjectDescriptor fod = new FileObjectDescriptor((byte[]) session.getAttribute(reportId));
//        FileObjectDescriptor fod = (FileObjectDescriptor) session.getAttribute(reportId);
//
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType(fod.getFileContentType());
//        String strDate = DateFormatUtils.format(System.currentTimeMillis(), "dd-MM-yy_HH-mm-ss");
//
//        if ((fileName == null) || (fileName.equals(""))) {
//            response.addHeader("Content-Disposition", "attachment; filename=\"Report_" + strDate + "." + fod.getFileExt() + "\"");
//        } else {
//            response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "." + fod.getFileExt() + "\"");
//        }
//
//        session.removeAttribute(reportId);
//
//        // flush it in the response
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(fod.getFileContent());
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }
//
//    @RequestMapping("/secure/alldata.htm")
//    public void showAllData(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "reportId", required = false) String reportId,
//            @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//
//        log(request);
//
//        HttpSession session = request.getSession();
//        //FileObjectDescriptor fod = new FileObjectDescriptor((byte[]) session.getAttribute(reportId));
//        FileObjectDescriptor fod = (FileObjectDescriptor) session.getAttribute(reportId);
//
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType(fod.getFileContentType());
//        session.removeAttribute(reportId);
//
//        // flush it in the response
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        if (fod.getFileContent() != null) {
//            responseOutputStream.write(fod.getFileContent());
//        } else {
//            responseOutputStream.write("Ошибка выполнения операции".getBytes());
//        }
//
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }
//
//    @RequestMapping("/secure/jreport.htm")
//    public void showJasperReport(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "fund", required = false) String fund,
//            @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//
//        log(request);
//        byte[] bytes = safetyFundService.getJasperReportListOfFundBytes(fund);
//
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType("application/pdf");
//        response.addHeader("Content-Disposition", "attachment; filename=\"" + "jReport" + "." + "pdf" + "\"");
//
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(bytes);
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }
//    /*
//    @RequestMapping("/secure/jreportrequests.htm")
//    public void showCommonReportJR(HttpServletRequest request, HttpServletResponse response,
//    @RequestParam(value = "recDateFrom", required = false) Long recDateFrom,
//    @RequestParam(value = "recDateTo", required = false) Long recDateTo,
//    @RequestParam(value = "depId", required = false) String depId,
//    @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//    
//    log(request);
//    RequestsSearchCondition condition = new RequestsSearchCondition();
//    if (recDateFrom != null) {
//    condition.setReceiptDateFrom(new Date(recDateFrom));
//    }
//    if (recDateTo != null) {
//    condition.setReceiptDateTo(new Date(recDateTo));
//    }
//    if ((depId != null) && (!depId.equals("null")) && (!depId.isEmpty())) {
//    condition.setDepartmentId(Integer.parseInt(depId));
//    }
//    
//    byte[] bytes = requestsService.getJRCommonReportBytes(condition);
//    
//    response.setHeader("Cache-Control", "no-store");
//    response.setHeader("Pragma", "no-cache");
//    response.setDateHeader("Expires", 0);
//    response.setContentType("application/msword");
//    response.addHeader("Content-Disposition", "attachment; filename=\"" + "Report" + "." + "doc" + "\"");
//    
//    ServletOutputStream responseOutputStream = response.getOutputStream();
//    responseOutputStream.write(bytes);
//    responseOutputStream.flush();
//    responseOutputStream.close();
//    }
//     */
//
//    @RequestMapping("/secure/cardsave.htm")
//    public void showCards(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "fundFrom", required = false) Integer fundFrom,
//            @RequestParam(value = "fundTo", required = false) Integer fundTo) throws Exception {
//        log(request);
//        byte[] bytes = null;
//        if (fundFrom == null || fundTo == null || fundFrom <= fundTo) {
//            bytes = safetyFundService.getConvertTableBytes(fundFrom, fundTo);
//        }
//
//        // flush it in the response
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType("application/pdf");
//
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(bytes);
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }
//
//    @RequestMapping("/secure/rhuserform.htm")
//    public void getRhuserForm(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "userFormId", required = true) String userFormId) throws Exception {
//
//        log(request);
//
//        HttpSession session = request.getSession();
//        FileObjectDescriptor fod = (FileObjectDescriptor) session.getAttribute(userFormId);
//
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType(fod.getFileContentType());
//        response.addHeader("Content-Disposition", "attachment; filename=\"" + fod.getFileName() + "." + fod.getFileExt() + "\"");
//
//        session.removeAttribute(userFormId);
//
//        // flush it in the response
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(fod.getFileContent());
//        responseOutputStream.flush();
//        responseOutputStream.close();
//    }
//
//    @RequestMapping("/secure/report-fundstat.htm")
//    public void getReportFundStat(HttpServletRequest request, HttpServletResponse response,
//            @RequestParam(value = "year", required = true) Integer year,
//            @RequestParam(value = "month", required = false) Integer month) throws Exception {
//
//        log(request);
//        Integer archiveId = ConfigurationManager.getInstance().getArchiveId();
//        String path = "/stat/fund-" + archiveId + ".xls";
//        InputStream inStream = getClass().getResourceAsStream(path);
//        HSSFWorkbook inBook = new HSSFWorkbook(inStream);
//        HSSFSheet inShit = inBook.getSheet(year.toString());
//
//        HSSFWorkbook outBook = new HSSFWorkbook();
//        HSSFSheet outSheet = outBook.createSheet(year.toString());
//
//        String archiveName = getArchiveName(archiveId);
//
//        FormulaEvaluator evaluator = inBook.getCreationHelper().createFormulaEvaluator();
//
//        HSSFRow inRow = null;
//        HSSFRow outRow = null;
//        HSSFCell inCell = null;
//        HSSFCell outCell = null;
//        int rowNum = 0;
//        inRow = inShit.getRow(rowNum);
//        int totalCount = 0;
//        while (inRow != null) {
//            int collNum = 0;
//            outRow = outSheet.createRow(rowNum);
//            outRow.setHeight((short) 500);
//            outCell = outRow.createCell(collNum);
//            outCell.setCellType(Cell.CELL_TYPE_STRING);
//            HSSFCellStyle cellStyle = outBook.createCellStyle();
//            cellStyle.setVerticalAlignment(HSSFCellStyle.VERTICAL_CENTER);
//            cellStyle.setBorderTop(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderRight(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderBottom(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setBorderLeft(HSSFCellStyle.BORDER_THIN);
//            cellStyle.setWrapText(true);
//            outCell.setCellStyle(cellStyle);
//            outCell.setCellValue(cell2Str(inRow.getCell(collNum), evaluator));
//            if (month != null) {
//                outCell = outRow.createCell(1);
//                outCell.setCellValue(cell2Str(inRow.getCell(month), evaluator));
//                outCell.setCellStyle(cellStyle);
//            } else {
//                if (totalCount == 0) {
//                    while (((inCell = inRow.getCell(++collNum)) != null) && (!cell2Str(inCell, evaluator).isEmpty())) {
//                        outCell = outRow.createCell(collNum);
//                        outCell.setCellType(Cell.CELL_TYPE_STRING);
//                        outCell.setCellValue(cell2Str(inCell, evaluator));
//                        outCell.setCellStyle(cellStyle);
//                        totalCount++;
//                    }
//                } else {
//                    for(int i = 0; i <= totalCount; i++) {
//                        inCell = inRow.getCell(i);
//                        outCell = outRow.createCell(i);
//                        outCell.setCellType(Cell.CELL_TYPE_STRING);
//                        if(inCell != null) {
//                        outCell.setCellValue(cell2Str(inCell, evaluator));
//                        } else {
//                            outCell.setCellValue("");
//                        }
//                        outCell.setCellStyle(cellStyle);
//                    }
//                }
//            }
//            inRow = inShit.getRow(++rowNum);
//        }
//
//        outSheet.setColumnWidth(0, 5000);
//        for (int i = 1; i < 20; i++) {
//            outSheet.setColumnWidth(i, 2700);
//        }
//
//
//        inStream.close();
//
//        String fileName = archiveName + "_" + year;
//        if (month != null) {
//            fileName += "_" + getMonthName(month);
//        }
//        fileName += ".xls";
//        fileName = Translit.toTranslit(fileName);
//
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType("application/msexcel");
//        response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "\"");
//
//        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
//        outBook.write(outStream);
//
//        // flush it in the response
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(outStream.toByteArray());
//        responseOutputStream.flush();
//        responseOutputStream.close();
//
//
//
//        /*
//        HttpSession session = request.getSession();
//        FileObjectDescriptor fod = (FileObjectDescriptor) session.getAttribute(userFormId);
//        response.setHeader("Cache-Control", "no-store");
//        response.setHeader("Pragma", "no-cache");
//        response.setDateHeader("Expires", 0);
//        response.setContentType(fod.getFileContentType());
//        response.addHeader("Content-Disposition", "attachment; filename=\"" + fod.getFileName() + "." + fod.getFileExt() + "\"");
//        session.removeAttribute(userFormId);
//        // flush it in the response
//        ServletOutputStream responseOutputStream = response.getOutputStream();
//        responseOutputStream.write(fod.getFileContent());
//        responseOutputStream.flush();
//        responseOutputStream.close();
//         *
//         */
//    }
//
//    /*
//    
//     *
//     *  @RequestParam(value = "reportId", required = false) String reportId,
//    @RequestParam(value = "fileName", required = false) String fileName) throws Exception {
//    
//    log(request);
//    
//    HttpSession session = request.getSession();
//    //FileObjectDescriptor fod = new FileObjectDescriptor((byte[]) session.getAttribute(reportId));
//    FileObjectDescriptor fod = (FileObjectDescriptor) session.getAttribute(reportId);
//    
//    response.setHeader("Cache-Control", "no-store");
//    response.setHeader("Pragma", "no-cache");
//    response.setDateHeader("Expires", 0);
//    response.setContentType(fod.getFileContentType());
//    String strDate = DateFormatUtils.format(System.currentTimeMillis(), "dd-MM-yy_HH-mm-ss");
//    if ((fileName == null) || (fileName.equals(""))) {
//    response.addHeader("Content-Disposition", "attachment; filename=\"Report_" + strDate + "." + fod.getFileExt() + "\"");
//    } else {
//    response.addHeader("Content-Disposition", "attachment; filename=\"" + fileName + "." + fod.getFileExt() + "\"");
//    }
//    session.removeAttribute(reportId);
//    
//    // flush it in the response
//    ServletOutputStream responseOutputStream = response.getOutputStream();
//    responseOutputStream.write(fod.getFileContent());
//    responseOutputStream.flush();
//    responseOutputStream.close();
//    
//     */
//    protected String cell2Str(HSSFCell cell, FormulaEvaluator evaluator) {
//        if (cell != null) {
//            int cellType = cell.getCellType();
//            if (cellType == HSSFCell.CELL_TYPE_NUMERIC) {
//                return getStrFromNumericCell(cell);
//            } else if (cellType == HSSFCell.CELL_TYPE_STRING) {
//                return getStrFromStringCell(cell);
//            } else if (cellType == HSSFCell.CELL_TYPE_FORMULA) {
//                CellValue evaluate = evaluator.evaluate(cell);
//                return String.valueOf((int) evaluate.getNumberValue());
//            }
//        }
//        return "";
//    }
//
//    protected String getStrFromNumericCell(HSSFCell cell) {
//        double value = cell.getNumericCellValue();
//        if (HSSFDateUtil.isCellDateFormatted(cell)) {
//            Date date = cell.getDateCellValue();
//            DateFormat df = new SimpleDateFormat("dd.MM.yyyy");
//            return df.format(date);
//        } else {
//            if (isDoubleLikeInteger(value)) {
//                return String.valueOf((int) value);
//            }
//        }
//
//        return "";
//    }
//
//    protected boolean isDoubleLikeInteger(double d) {
//        Double d1 = new Double(d);
//        Double d2 = Math.floor(d);
//
//        return d1.equals(d2);
//    }
//
//    protected String getStrFromStringCell(HSSFCell cell) {
//        return cell.getStringCellValue();
//    }
//
//    protected Date stringToDate(String strDate) {
//
//        List<String> sa = new ArrayList<String>(3);
//
//        Scanner scanner = new Scanner(strDate);
//        scanner.useDelimiter("\\.");
//
//        while (scanner.hasNext()) {
//            sa.add(scanner.next());
//        }
//
//        if (sa.size() != 3) {
//            return null;
//        }
//
//        Calendar c = Calendar.getInstance();
//        c.clear();
//        c.set(Integer.parseInt(sa.get(2)), Integer.parseInt(sa.get(1)) - 1, Integer.parseInt(sa.get(0)));
//
//        return c.getTime();
//    }
//
//    // TODO: should be replaced by smart one function
//    protected String getMonthName(Integer month) {
//        if (month != null) {
//            switch (month) {
//                case 1:
//                    return "январь";
//                case 2:
//                    return "февраль";
//                case 3:
//                    return "март";
//                case 4:
//                    return "апрель";
//                case 5:
//                    return "май";
//                case 6:
//                    return "июнь";
//                case 7:
//                    return "июль";
//                case 8:
//                    return "август";
//                case 9:
//                    return "сентябрь";
//                case 10:
//                    return "октябрь";
//                case 11:
//                    return "ноябрь";
//                case 12:
//                    return "декабрь";
//                default:
//                    return "";
//            }
//        }
//        return "";
//    }
//
//    // TODO: should be replaced by smart one function
//    protected String getArchiveName(Integer archiveId) {
//        String archiveName = "";
//        if (archiveId == null) {
//            return archiveName;
//        }
//
//        switch (archiveId.intValue()) {
//            case 0:
//                archiveName = "АК";
//                break;
//            case 1:
//                archiveName = "ЦГА";
//                break;
//            case 2:
//                archiveName = "ЦГИА";
//                break;
//            case 3:
//                archiveName = "ЦГАИПД";
//                break;
//            case 4:
//                archiveName = "ЦГАНТД";
//                break;
//            case 5:
//                archiveName = "ЦГАЛИ";
//                break;
//            case 6:
//                archiveName = "ЦГАКФФД";
//                break;
//            case 7:
//                archiveName = "ЦГАЛС";
//                break;
//            default:
//                break;
//        }
//
//        return archiveName;
//    }
//
//    /**
//     * @return the safetyFundService
//     */
//    public SafetyFundService getSafetyFundService() {
//        return safetyFundService;
//    }
//
//    /**
//     * @param safetyFundService the safetyFundService to set
//     */
//    public void setSafetyFundService(SafetyFundService safetyFundService) {
//        this.safetyFundService = safetyFundService;
//    }
//
//    /**
//     * @return the searchService
//     */
//    public SearchService getSearchService() {
//        return searchService;
//    }
//
//    /**
//     * @param searchService the searchService to set
//     */
//    public void setSearchService(SearchService searchService) {
//        this.searchService = searchService;
//    }
    protected void log(HttpServletRequest request) {
        statLog.info("PAGE " + request.getRequestedSessionId() + " " + userService.getCurrentUser().getUserLogin() + " " + getClass().getSimpleName() + " " + request.getRemoteAddr() + " " + request.getRequestURL() + "?" + request.getQueryString());
    }
}