package pst.arm.client.modules.aiscontracts.domain;

import java.util.HashMap;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Created by akozhin on 20.12.14.
 */
public class AisSyncResult {
    private StringBuilder log = new StringBuilder();
    private Map<String, Map<SyncOperationType, Integer>> results = new LinkedHashMap();

    public void addSyncType(Class type) {
        results.put(type.getName(), getEmptyResultsMap());
    }

    private Map<SyncOperationType, Integer> getEmptyResultsMap() {
        Map<SyncOperationType, Integer> resultsMap = new HashMap<SyncOperationType, Integer>();
        for (SyncOperationType type : SyncOperationType.values()) {
            resultsMap.put(type, 0);
        }
        return resultsMap;
    }

    public void addResult(Class type, SyncOperationType operationType, String logMessage) {
        if (!results.containsKey(type.getName())) {
            addSyncType(type);
        }
        results.get(type.getName()).put(operationType, results.get(type.getName()).get(operationType) + 1);
        log.append(operationType.sign()).append(" ").append(logMessage).append("\n");
    }

    public Integer getResult(Class type, SyncOperationType operation) {
        if (results.containsKey(type.getName())) {
            return results.get(type.getName()).get(operation);
        }
        return 0;
    }

    @Override
    public String toString() {
        StringBuilder stringBuilder = new StringBuilder();
        for (Map.Entry<String, Map<SyncOperationType, Integer>> entrySet : results.entrySet()) {
            stringBuilder.append(entrySet.getKey()).append(":\n\n");
            for (Map.Entry<SyncOperationType, Integer> subEntrySet : entrySet.getValue().entrySet()) {
                stringBuilder.append(subEntrySet.getKey().name()).append("(").append(subEntrySet.getKey().sign()).append("):")
                        .append(subEntrySet.getValue()).append("\n");
            }
        }
        stringBuilder.append("\n\nLog:\n\n").append(log.toString());
        return stringBuilder.toString();
    }
}
