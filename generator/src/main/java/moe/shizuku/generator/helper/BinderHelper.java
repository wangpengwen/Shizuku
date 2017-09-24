package moe.shizuku.generator.helper;

import java.util.HashMap;
import java.util.Map;

/**
 * Created by rikka on 2017/9/24.
 */

public class BinderHelper {

    private static final Map<String, String> sNameMap = new HashMap<>();
    private static final Map<String, Integer> sIndexMap = new HashMap<>();

    private static void addName(String binderName, String serviceName) {
        sNameMap.put(binderName, serviceName);
        sIndexMap.put(binderName, sIndexMap.size());
    }

    static {
        addName("IPackageManager", "package");
        addName("IActivityManager", "activity");
        addName("IAppOpsService", "appops");
        addName("IUserManager", "user");
    }

    public static String getServiceName(String binderName) {
        if (!sNameMap.containsKey(binderName)) {
            throw new IllegalArgumentException("not found " + binderName);
        }
        return sNameMap.get(binderName);
    }

    public static int getIndex(String binderName) {
        if (!sIndexMap.containsKey(binderName)) {
            throw new IllegalArgumentException("not found " + binderName);
        }
        return sIndexMap.get(binderName);
    }
}
