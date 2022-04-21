package pst.arm.server.common.dao.jdbc;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.codec.digest.DigestUtils;
import org.apache.commons.io.FileUtils;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.io.IOUtils;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.SqlParameterSource;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.jdbc.support.KeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import pst.arm.client.common.domain.FsFile;
import pst.arm.client.common.domain.reports.FileObjectDescriptor;
import pst.arm.client.common.domain.store.StoreItem;
import pst.arm.server.common.dao.FileDAO;
import pst.arm.server.common.dao.jdbc.mappers.FileMapper;
import pst.arm.server.common.dao.jdbc.mappers.StoreItemMapper;
import static pst.arm.server.utils.ServerUtil.isNotEmpty;

/**
 *
 * @author vmaslov
 * @version 1.2.0
 */
@Repository
public class FileDAOJdbcImpl extends ArmNamedJdbcImpl implements FileDAO {

    private String currentRootSequence = null;
    private String rootPath = null;
    private String instanceName = "tirapst";
    private Integer currentRootId;

    @Override
    @Transactional("txArm")
    public Integer placeStoreItem(StoreItem item) {
        saveToFileSystem(item);

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", item.getName());
        map.put("path", item.getPath());
        map.put("size", item.getFileBytes().length);
        map.put("md5", item.getMd5());
        map.put("rootId", currentRootId);
        SqlParameterSource namedParameters = new MapSqlParameterSource(map);
        String sql = "INSERT INTO FS_FILE(FILE_ROOT_ID, FILE_DIR, FILE_NAME, FILE_SIZE, HASH_MD5) "
                + "VALUES(:rootId,:path,:name,:size,:md5)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql, namedParameters, keyHolder, new String[]{"FILE_ID"});
        Integer fileId = keyHolder.getKey().intValue();

        sql = "INSERT INTO STORE(ITEM_TYPE,ITEM_STATUS,ENTITY_KIND,ENTITY_ID) VALUES('" + item.getFormat() + "','" + item.getSourceName() + "','"
                + item.getEntityKind() + "'," + item.getEntityId() + ")";
        keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql, null, keyHolder, new String[]{"STORE_ITEM_ID"});
        Integer storeItemId = keyHolder.getKey().intValue();

        sql = "INSERT INTO STORE_FILE(STORE_ITEM_ID,FILE_ID) VALUES(" + storeItemId + "," + fileId + ")";
        getJdbcTemplate().getJdbcOperations().update(sql);


        return storeItemId;
    }

    @Override
    @Transactional("txArm")
    public Integer replaceStoreItem(StoreItem item) {
        try {
            IOUtils.write(item.getFileBytes(), new FileOutputStream(item.getPath()));
            item.setMd5(DigestUtils.md5Hex(item.getFileBytes()));
        } catch (Exception ex) {
            Logger.getLogger(FileDAOJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }

        Map<String, Object> map = new HashMap<String, Object>();
        map.put("size", item.getFileBytes().length);
        map.put("md5", item.getMd5());
        map.put("storeItemId", item.getStoreId());

        SqlParameterSource namedParameters = new MapSqlParameterSource(map);
        String sql = "UPDATE FS_FILE SET FILE_SIZE = :size, HASH_MD5 = :md5 WHERE"
                + " FILE_ID = (SELECT FILE_ID FROM STORE_FILE WHERE STORE_ITEM_ID = :storeItemId)";

        getJdbcTemplate().update(sql, namedParameters);


        sql = "UPDATE STORE SET ITEM_TYPE = '" + item.getFormat() + "',"
                + "ITEM_STATUS = '" + item.getSourceName() + "' "
                + " WHERE STORE_ITEM_ID = " + item.getStoreId();

        getJdbcTemplate().getJdbcOperations().update(sql);


        return item.getStoreId();
    }

    private void saveToFileSystem(StoreItem item) {
        if (rootPath == null || currentRootSequence == null) {
            getStoreProperties();
        }
        Long uid = getNextRootId();
        String storePath = generateStoreRelativePath(uid);
        File fDir = new java.io.File(FilenameUtils.getFullPath(rootPath + storePath));
        try {
            if (!fDir.exists()) {
                FileUtils.forceMkdir(fDir);
            }
            IOUtils.write(item.getFileBytes(), new FileOutputStream(rootPath + storePath));
        } catch (Exception ex) {
            Logger.getLogger(FileDAOJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
        }
        File distFile = new File(rootPath + storePath);
        distFile.setReadable(true, false);
        item.setMd5(DigestUtils.md5Hex(item.getFileBytes()));
        item.setPath(FilenameUtils.getFullPath(storePath));
        item.setName(FilenameUtils.getName(storePath));
    }

    @Override
    public Integer attachFile(FileObjectDescriptor fileObj) {
        Map<String, Object> map = new HashMap<String, Object>();
        map.put("name", fileObj.getFileName());
        map.put("type", fileObj.getFileContentType());
        map.put("data", fileObj.getFileContent());
        map.put("size", fileObj.getFileContent().length);
        SqlParameterSource namedParameters = new MapSqlParameterSource(map);
        String sql = "INSERT INTO FILES(NAME,TYPE,FILE_DATA,FILE_SIZE) VALUES(:name,:type,:data,:size)";
        KeyHolder keyHolder = new GeneratedKeyHolder();
        getJdbcTemplate().update(sql, namedParameters, keyHolder, new String[]{"FILE_ID"});
        return keyHolder.getKey().intValue();
    }

    @Override
    public FileObjectDescriptor getFile(Integer fileId) {
        String sql = "SELECT * FROM FILES WHERE FILE_ID = :fileId";
        List<FileObjectDescriptor> list = getJdbcTemplate().query(sql, new MapSqlParameterSource("fileId", fileId), new FileMapper());
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public void deleteFile(Integer fileId) {
        String sql = "DELETE FROM FILES WHERE FILE_ID = :fileId";
        getJdbcTemplate().update(sql, new MapSqlParameterSource("fileId", fileId));
    }

    public void placeFile(FsFile fsFile) throws IOException {
        String storePath = addFile(fsFile.getFile(), getNextRootId());
        fsFile.setFile(new File(rootPath + storePath));
        fsFile.setRelativeStoreDir(FilenameUtils.getFullPath(storePath));
    }

    private String addFile(File file, Long uid) throws IOException {
        String storePath = generateStoreRelativePath(uid);
        File fDir = new java.io.File(FilenameUtils.getFullPath(rootPath + storePath));
        if (!fDir.exists()) {
            FileUtils.forceMkdir(fDir);
        }

        File distFile = new File(rootPath + storePath);
        FileUtils.moveFile(file, distFile);
        distFile.setReadable(true, false);

        return storePath;
    }

    private String generateStoreRelativePath(Long uid) {
        int n1 = (int) (uid & 0xFF000000) >>> 24;
        int n2 = (int) (uid & 0x00FF0000) >>> 16;
        int n3 = (int) (uid & 0x0000FF00) >>> 8;
        int n4 = (int) (uid & 0x000000FF);

        String separator = File.separator;
        String filePath = String.format("%03d%s%03d%s%03d%s%03d.dat", n1, separator, n2, separator, n3, separator, n4);

        return filePath;
    }

    public Long getNextRootId() {
        Long nextid = null;
        String sql = "SELECT SEQ_NEXTVAL('" + currentRootSequence + "') FROM DUAL";
        nextid = getJdbcTemplate().getJdbcOperations().queryForLong(sql);
        return nextid;
    }

    @Override
    public Integer storeFile(FileObjectDescriptor fileObj) {
        if (rootPath == null || currentRootSequence == null) {
            getStoreProperties();
        }
        Long uid = getNextRootId();
        String storePath = generateStoreRelativePath(uid);
        File fDir = new java.io.File(FilenameUtils.getFullPath(rootPath + storePath));
        try {
            if (!fDir.exists()) {
                FileUtils.forceMkdir(fDir);
            }
            IOUtils.write(fileObj.getFileContent(), new FileOutputStream(rootPath + storePath));
        } catch (Exception ex) {
            Logger.getLogger(FileDAOJdbcImpl.class.getName()).log(Level.SEVERE, null, ex);
            return -1;
        }
        File distFile = new File(rootPath + storePath);
//        FileUtils.moveFile(file, distFile);
        distFile.setReadable(true, false);
        return 1;
    }

    private void getStoreProperties() {
        String sql = "SELECT FILE_ROOT_ID, "
                + "ROOT_BASE_DIR || ROOT_PATH as ABSOLUTE_PATH, "
                + "ROOT_SEQUENCE FROM FS_FILE_ROOT "
                + "WHERE IS_CURRENT=1 AND INSTANCE_NAME='" + instanceName + "'";
        Map<String, Object> map = getJdbcTemplate().getJdbcOperations().queryForMap(sql);
        currentRootId = ((BigDecimal) map.get("FILE_ROOT_ID")).intValue();
        rootPath = (String) map.get("ABSOLUTE_PATH");
        currentRootSequence = (String) map.get("ROOT_SEQUENCE");
    }

    @Override
    public StoreItem getStoreItem(Integer storeItemId) {
        String sql =
                "SELECT "
                + "S.*, FFR.ROOT_BASE_DIR || FFR.ROOT_PATH || FF.FILE_DIR || FF.FILE_NAME AS PATH "
                + "FROM STORE S "
                + "JOIN STORE_FILE SF ON (SF.STORE_ITEM_ID=S.STORE_ITEM_ID) "
                + "JOIN FS_FILE FF ON (SF.FILE_ID=FF.FILE_ID) "
                + "JOIN FS_FILE_ROOT FFR ON (FFR.FILE_ROOT_ID=FF.FILE_ROOT_ID) "
                + "WHERE S.STORE_ITEM_ID= :storeItemId";
        List<StoreItem> list = getJdbcTemplate().query(sql, new MapSqlParameterSource("storeItemId", storeItemId), new StoreItemMapper());
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public StoreItem getStoreItem(String entityKind, Integer entityId) {
        String sql =
                "SELECT "
                + "S.*, FFR.ROOT_BASE_DIR || FFR.ROOT_PATH || FF.FILE_DIR || FF.FILE_NAME AS PATH "
                + "FROM STORE S "
                + "JOIN STORE_FILE SF ON (SF.STORE_ITEM_ID=S.STORE_ITEM_ID) "
                + "JOIN FS_FILE FF ON (SF.FILE_ID=FF.FILE_ID) "
                + "JOIN FS_FILE_ROOT FFR ON (FFR.FILE_ROOT_ID=FF.FILE_ROOT_ID) "
                + "WHERE S.ENTITY_KIND= :entityKind AND S.ENTITY_ID = :entityId";
        MapSqlParameterSource map = new MapSqlParameterSource("entityKind", entityKind);
        map.addValue("entityId", entityId);
        List<StoreItem> list = getJdbcTemplate().query(sql, map, new StoreItemMapper());
        if (list.size() == 1) {
            return list.get(0);
        }
        return null;
    }

    @Override
    public List<Integer> getStoreItemIds(String entityKind, Integer entityId) {
        String sql = "SELECT STORE_ITEM_ID FROM STORE "
                + "WHERE ENTITY_KIND= '" + entityKind + "' AND ENTITY_ID = " + entityId;
        List<Integer> list = getJdbcTemplate().getJdbcOperations().queryForList(sql, Integer.class);
        return list;
    }
    @Override
    public String getFilePath() {
        String sql = "SELECT SRVR_NAME, ROOT_PATH FROM FILE_ROOT WHERE DOC_TYPE_ID = 999 AND ENABLED = 1";

        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, new MapSqlParameterSource());
        if (isNotEmpty(list)) {
            String path = (String) list.get(0).get("SRVR_NAME") + "/" + (String) list.get(0).get("ROOT_PATH");
            return path;
        }
        return "";

    }
    @Override
    public List<Map<String, Object>> getFileHelpManualPath(String origfilename) {

        String sql = "SELECT SRVR_NAME, ROOT_PATH, FILENAME, EXTENSION FROM DOC_FILE WHERE  DOC_TYPE_ID=999 AND ORIG_FILENAME LIKE '" + origfilename + "'";
        List<Map<String, Object>> list = getJdbcTemplate().queryForList(sql, new MapSqlParameterSource());
                
        return list;
    }
}
