package pst.arm.server.modules.sync1c.util;

import java.io.InputStream;
import java.io.InputStreamReader;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.math.NumberUtils;
import org.apache.commons.lang.time.DateUtils;
import org.apache.log4j.Logger;
import org.dom4j.Document;
import org.dom4j.Element;
import org.dom4j.io.SAXReader;
import pst.arm.client.modules.sync1c.domain.Department1C;
import pst.arm.client.modules.sync1c.domain.Post1C;
import pst.arm.client.modules.sync1c.domain.Worker1C;

/**
 * Created by akozhin on 28.04.15.
 */
public class Sync1CParser {

    static Logger logger = Logger.getLogger(Sync1CParser.class);
    static DateFormat df = new SimpleDateFormat("dd.MM.yyyy");

    public static List<Post1C> getPosts(InputStream is) {
        List<Post1C> posts = new ArrayList<Post1C>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("/catalogues/posts/post");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Element post = (Element) iterator.next();
                Post1C post1C = new Post1C();
                if (StringUtils.isNotBlank(post.attributeValue("code"))) {
                    post1C.setPmascCode(post.attributeValue("code").trim());
                }
                if (StringUtils.isNotBlank(post.attributeValue("name"))) {
                    post1C.setName(post.attributeValue("name").trim());
                }
                if (StringUtils.isNotBlank(post.attributeValue("okpdtr"))) {
                    post1C.setCode(post.attributeValue("okpdtr").trim());
                }
                if (StringUtils.isNotEmpty(post1C.getPmascCode()) && 9 == post1C.getPmascCode().length()) {
                    post1C.setInteractingSysId(2L);
                } else {
                    post1C.setInteractingSysId(1L);
                }
                posts.add(post1C);
            }
        } catch (Exception ex) {
            logger.error("Error get posts from xml", ex);
        }
        return posts;
    }

    public static List<Worker1C> getWorkers(InputStream is) {
        List<Worker1C> workers = new ArrayList<Worker1C>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("/catalogues/workers/worker");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Element el = (Element) iterator.next();
                Worker1C worker = new Worker1C();
                worker.setPersonalNumber(attributeValue(el, "code"));
                worker.setName(attributeValue(el, "name"));
                worker.setCompanyId(attributeValueInt(el, "codeorg"));
                worker.setPostCode(attributeValue(el, "post"));
                worker.setDepartmentCode(attributeValue(el, "depart"));

                worker.setDateIn(getDateWithDefaultTime(attributeValueDate(el, "datein")));
                worker.setDateOut(getDateWithDefaultTime(attributeValueDate(el, "dateout")));
                worker.setDatePost(getDateWithDefaultTime(attributeValueDate(el, "datepost")));

                worker.setQuantity(attributeValueDouble(el, "rate"));
                worker.setSex(attributeValueInt(el, "sex"));
                worker.setCombinationSign(attributeValueInt(el, "coop"));
                worker.setIsFolder(attributeValueInt(el, "is_folder"));
                worker.setIdent(attributeValue(el, "ident"));
                worker.setIdentfiz(attributeValue(el, "identfiz"));
                worker.setReason(attributeValue(el, "reason"));
                worker.setBirthday(attributeValueDate(el, "birthday"));
                worker.setPersonalCode(attributeValue(el, "codefiz"));
                //Присвоить значение 1, если (codeorg>0 AND  codeorg<4);
                //Присвоить значение 2, если (codeorg=5 OR  codeorg=7);
                //В остальных случаях присвоить значение NULL
                if (worker.getCompanyId() != null) {
                    if (worker.getCompanyId() > 0 && worker.getCompanyId() < 4) {
                        worker.setInteractingSysId(1);
                    }
                    if (worker.getCompanyId() == 5 || worker.getCompanyId() == 7) {
                        worker.setInteractingSysId(2);
                    }
                }
                worker.setInn(attributeValue(el, "inn"));
                workers.add(worker);
            }
        } catch (Exception ex) {
            logger.error("Error get workers from xml", ex);
        }
        return workers;

    }

    private static String attributeValue(Element el, String code) {
        if (StringUtils.isNotBlank(el.attributeValue(code))) {
            return el.attributeValue(code).trim();
        }
        return null;
    }

    private static Integer attributeValueInt(Element el, String code) {
        if (StringUtils.isNotBlank(el.attributeValue(code)) && NumberUtils.isNumber(el.attributeValue(code))) {
            return Integer.parseInt(el.attributeValue(code).trim());
        }
        return null;
    }

    private static Date attributeValueDate(Element el, String code) {
        String dateStr = "";
        try {
            dateStr = el.attributeValue(code);
            if (StringUtils.isNotBlank(el.attributeValue(code))) {
                return df.parse(dateStr);
            }
        } catch (ParseException e) {
            logger.warn("Parse date " + dateStr, e);
        }
        return null;
    }

    public static List<Department1C> getDepartments(InputStream is) {
        List<Department1C> departments = new ArrayList<Department1C>();
        try {
            SAXReader reader = new SAXReader();
            reader.setEncoding("UTF-8");
            Document document = reader.read(new InputStreamReader(is, "UTF-8"));
            List list = document.selectNodes("/catalogues/departs/depart");
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Department1C department1C = getDepartment((Element) iterator.next());
                departments.add(department1C);
            }
        } catch (Exception e) {
            logger.error("Error get departments", e);
        }
        return departments;
    }

    private static Department1C getDepartment(Element departElement) {
        Department1C department1C = new Department1C();
        department1C.setCode(attributeValue(departElement, "code"));
        department1C.setCompanyId(attributeValueInt(departElement, "codeorg"));
        department1C.setName(attributeValue(departElement, "name"));

        List list = departElement.selectNodes("depart");
        if (list != null && !list.isEmpty()) {
            Iterator iterator = list.iterator();
            while (iterator.hasNext()) {
                Department1C child = getDepartment((Element) iterator.next());
                child.setParent(department1C);
                department1C.addChild(child);
            }
        }
        return department1C;
    }

    private static Date getDateWithDefaultTime(Date date) {
        if (date == null) {
            return null;
        }
        return DateUtils.addHours(date, 12);
    }

    private static Double attributeValueDouble(Element el, String code) {
        if (StringUtils.isNotBlank(el.attributeValue(code))) {
            return Double.parseDouble(el.attributeValue(code).trim().replace(",", "."));
        }
        return null;
    }
}
